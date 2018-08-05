package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class ParticipatingInCourseAction extends Action<Boolean> {
    private String courseID;
    private String studentID;
    private List<String> getGrade;
    private Integer grade;

    public ParticipatingInCourseAction(String courseID, String studentID, List<String> getGrade) {
        this.courseID = courseID;
        this.studentID = studentID;
        this.getGrade = getGrade;
        if(getGrade.get(0).contains("-"))
            this.grade = (-1);
        else
            this.grade = Integer.parseInt(getGrade.get(0));
        //this.grade = getGrade.get(0).equals("-") ? -1 : Integer.parseInt(getGrade.get(0));

    }

    @Override
    protected void start() {
        actorState.addRecord("Participate In Course");
        CoursePrivateState courseState = ((CoursePrivateState)actorState);
        List<Action<Boolean>> actions = new ArrayList<>();
        PrerequisitesCheckAction passesPrerequisites = new PrerequisitesCheckAction(
                courseID, courseState.getPrequisites(), grade);
        actions.add(passesPrerequisites);
        sendMessage(passesPrerequisites, studentID, new StudentPrivateState());
        then(actions, ()->{
                Boolean result = actions.get(0). getResult().get();
                if(result == true && courseState.getAvailableSpots() > 0){
                        int availableSpaces = ((CoursePrivateState)actorState).getAvailableSpots();
                        int registered = ((CoursePrivateState)actorState).getRegistered();
                        courseState.setRegistered(++registered);
                        courseState.setAvailableSpots(--availableSpaces);
                        courseState.addRegStudent(studentID);

                        RegisterStudent registerAction = new RegisterStudent(courseID, grade);
                        List<Action<Boolean>> actionsInThen = new ArrayList<>();
                        actionsInThen.add(registerAction);
                        sendMessage(registerAction, studentID, new StudentPrivateState());

                        then(actionsInThen,() -> {
                            complete(true);
                        });
                }else {
                    complete(false);
                    return;
                 }
            });
        }
}



