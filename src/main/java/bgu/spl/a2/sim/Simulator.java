/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.actions.*;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {


    public static ActorThreadPool actorThreadPool;
    private static List<List<Object>> phase1Actions;
    private static List<List<Object>> phase2Actions;
    private static List<List<Object>> phase3Actions;


    /**
     * Begin the simulation Should not be called before attachActorThreadPool()
     */
    public static void start(){

        actorThreadPool.start();

        //////////Start of Phase 1//////////

        for(List<Object> a:phase1Actions){
            actorThreadPool.submit((Action)a.get(0),(String)a.get(1),(PrivateState)a.get(2));
        }
        CountDownLatch phase1 = new CountDownLatch(phase1Actions.size());
        for(List<Object> a: phase1Actions){
            ((Action)a.get(0)).getResult().subscribe(()->{
                phase1.countDown();
            });



        }
        try {
            phase1.await();
        } catch (InterruptedException e) {
        }



        //////////End of Phase 1//////////
        if(phase1.getCount()==0) { //Double check
            //////////Start of Phase 2//////////

            for (List<Object> b : phase2Actions) {
                actorThreadPool.submit((Action) b.get(0), (String) b.get(1), (PrivateState) b.get(2));
            }
            CountDownLatch phase2 = new CountDownLatch(phase2Actions.size());
            for (List<Object> b : phase2Actions) {
                ((Action) b.get(0)).getResult().subscribe(() -> {
                    phase2.countDown();
                });

            }

            try {
                phase2.await();
            } catch (InterruptedException e) {
            }
            //////////End of Phase 2//////////

            if (phase2.getCount() == 0) { //Double check

                //////////Start of Phase 3//////////

                for (List<Object> c : phase3Actions) {
                    actorThreadPool.submit((Action) c.get(0), (String) c.get(1), (PrivateState) c.get(2));
                }
                CountDownLatch phase3 = new CountDownLatch(phase3Actions.size());
                for (List<Object> c : phase3Actions) {
                    ((Action) c.get(0)).getResult().subscribe(() -> {
                        phase3.countDown();
                    });

                }
                try {
                    phase3.await();
                } catch (InterruptedException e) {
                }
            }
        }
        //////////End of Phase 3//////////





    }

    /**
     * attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
     *
     * @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
     */
    public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
        actorThreadPool = myActorThreadPool;
    }

    /**
     * shut down the simulation
     * returns list of private states
     */
    public static HashMap<String,PrivateState> end(){
        try {
            actorThreadPool.shutdown();
        } catch (InterruptedException e) { }
        HashMap<String,PrivateState> temp = new HashMap<>();
        for(String key:actorThreadPool.getActors().keySet()){
            temp.put(key,actorThreadPool.getActors().get(key));
        }
        return
                temp;
    }


    public static void main(String[] args) {
        String json = null;
        try {
            json = new String(Files.readAllBytes(Paths.get(args[0])));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();

        JSON_Data o = gson.fromJson(json, JSON_Data.class);



        ActorThreadPool pool = new ActorThreadPool(o.jsonThreads);//create a thread pool with n threads

        Warehouse warehouse = new Warehouse();
        warehouse = createComputers(o,warehouse);     //create the computers and add them to the warehouse

        phase1Actions = new ArrayList<>();
        for(int i=0;i<o.jsonPhaseOneActions.length;i++){
            phase1Actions.add(createActionByName(o.jsonPhaseOneActions[i],o.jsonPhaseOneActions[i].jsonActionName,warehouse));
        }

        phase2Actions = new ArrayList<>();
        for(int i=0;i<o.jsonPhaseTwoActions.length;i++){
            phase2Actions.add(createActionByName(o.jsonPhaseTwoActions[i],o.jsonPhaseTwoActions[i].jsonActionName,warehouse));
        }

        phase3Actions = new ArrayList<>();
        for(int i=0;i<o.jsonPhaseThreeActions.length;i++){
            phase3Actions.add(createActionByName(o.jsonPhaseThreeActions[i],o.jsonPhaseThreeActions[i].jsonActionName,warehouse));
        }



        attachActorThreadPool(pool);
        start();

        HashMap<String,PrivateState> SimulationResult;
        SimulationResult = end();

        try {
            FileOutputStream fout = new FileOutputStream("result.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(SimulationResult);
        } catch (Exception e) {
            e.printStackTrace();
        }




    }


    //create computers and add them to the warehouse
    public static Warehouse createComputers(JSON_Data o,Warehouse warehouse){
        ;
        for(int i=0;i<o.jsonComputers.length;i++){
            Computer computer = new Computer(o.jsonComputers[i].jsonType);
            computer.successSig = o.jsonComputers[i].jsonSigSuccess;
            computer.failSig = o.jsonComputers[i].jsonSigFail;
            SuspendingMutex suspendingMutex = new SuspendingMutex(computer);
            warehouse.addNewComputer(computer.computerType,suspendingMutex);
        }
        return warehouse;
    }


    public static ArrayList<Object> createActionByName(JSON_Action o, String actionName,Warehouse warehouse){
        List<Object> temp = new ArrayList<>();
        if(actionName.contains("Open Course")){
            OpenCourseAction openCourseAction = new OpenCourseAction(o.jsonPrerequisites,o.jsonCourse,o.jsonSpace);
            String actorID = o.jsonDepartment;
            PrivateState privateState = new DepartmentPrivateState();
            temp.add((OpenCourseAction) openCourseAction);
            temp.add((String)actorID);
            temp.add((DepartmentPrivateState)privateState);


        }
        else if(actionName.contains("Add Student")){
            AddStudentAction addStudentAction = new AddStudentAction(o.jsonStudent);
            String actorID = o.jsonDepartment;
            PrivateState privateState = new DepartmentPrivateState();
            temp.add((AddStudentAction)addStudentAction);
            temp.add((String)actorID);
            temp.add((DepartmentPrivateState)privateState);

        }
        else if(actionName.contains("Participate In Course")){
            ParticipatingInCourseAction participating = new ParticipatingInCourseAction(o.jsonCourse,o.jsonStudent,o.jsonGrades);
            String actorID = o.jsonCourse;
            PrivateState privateState = new CoursePrivateState();
            temp.add((ParticipatingInCourseAction)participating);
            temp.add((String)actorID);
            temp.add((CoursePrivateState)privateState);

        }
        else if(actionName.contains("Unregister")){
            UnregisterAction unregisterAction = new UnregisterAction(o.jsonCourse,o.jsonStudent);
            String actorID = o.jsonCourse;
            PrivateState privateState = new CoursePrivateState();
            temp.add((UnregisterAction)unregisterAction);
            temp.add((String)actorID);
            temp.add((CoursePrivateState)privateState);
        }
        else if(actionName.contains("Close Course")){
            CloseCourseAction closeCourse = new CloseCourseAction(o.jsonCourse);
            String actorID = o.jsonDepartment;
            PrivateState privateState = new DepartmentPrivateState();
            temp.add((CloseCourseAction)closeCourse);
            temp.add((String)actorID);
            temp.add((DepartmentPrivateState)privateState);
        }
        else if(actionName.contains("Add Spaces")){
            OpenNewPlacesInCourseAction addSpaces = new OpenNewPlacesInCourseAction(o.jsonNumber);
            String actorID = o.jsonCourse;
            PrivateState privateState = new CoursePrivateState();
            temp.add((OpenNewPlacesInCourseAction)addSpaces);
            temp.add((String)actorID);
            temp.add((CoursePrivateState)privateState);
        }
        else if(actionName.contains("Administrative Check")){

            CheckAdministrativeObligationsAction administrativeCheck = new CheckAdministrativeObligationsAction(o.jsonStudents,o.jsonConditions,o.jsonComputer,warehouse);
            String actorID = o.jsonDepartment;
            PrivateState privateState = new DepartmentPrivateState();
            temp.add((CheckAdministrativeObligationsAction)administrativeCheck);
            temp.add((String)actorID);
            temp.add((DepartmentPrivateState)privateState);
        }
        else if(actionName.contains("Register With Preferences")){
            SupportPreferencesListAction supportPreferencesListAction = new SupportPreferencesListAction(o.jsonPreferences,o.jsonStudent,o.jsonGrades);
            String actorID = o.jsonStudent;
            PrivateState privateState = new CoursePrivateState();
            temp.add((SupportPreferencesListAction)supportPreferencesListAction);
            temp.add((String)actorID);
            temp.add((CoursePrivateState)privateState);
        }
        else{
            System.out.println("Unrecognized Action");
            return null;
        }
        return (ArrayList<Object>) temp;
    }



    private static class JSON_Data{
        @SerializedName("threads")
        int jsonThreads;

        @SerializedName("Computers")
        JSON_Computer[] jsonComputers;

        @SerializedName("Phase 1")
        JSON_Action[] jsonPhaseOneActions;

        @SerializedName("Phase 2")
        JSON_Action[] jsonPhaseTwoActions;

        @SerializedName("Phase 3")
        JSON_Action[] jsonPhaseThreeActions;
    }

    private static class JSON_Computer{
        @SerializedName("Type")
        String jsonType;

        @SerializedName("Sig Success")
        long jsonSigSuccess;

        @SerializedName("Sig Fail")
        long jsonSigFail;
    }

    private static class JSON_Action{
        @SerializedName("Action")
        String jsonActionName;

        @SerializedName("Department")
        String jsonDepartment;

        @SerializedName("Course")
        String jsonCourse;

        @SerializedName("Space")
        int jsonSpace;

        @SerializedName("Prerequisites")
        List<String> jsonPrerequisites;

        @SerializedName("Grade")
        List<String> jsonGrades;

        @SerializedName("Number")
        int jsonNumber;

        @SerializedName("Student")
        String jsonStudent;

        @SerializedName("Students")
        ArrayList<String> jsonStudents;

        @SerializedName("Preferences")
        List<String> jsonPreferences;

        @SerializedName("Conditions")
        ArrayList<String> jsonConditions;

        @SerializedName("Computer")
        String jsonComputer;
    }
}