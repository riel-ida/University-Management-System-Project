package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

import java.util.ArrayList;
import java.util.List;


public class SupportPreferencesListAction extends Action<Boolean> {
    private List<String> preferencesList;
    private List<String> grades;
    private String studentID;


    public SupportPreferencesListAction(List<String> preferencesList,String studentID,List<String> grades){
        this.preferencesList = preferencesList;
        this.studentID = studentID;
        this.grades = grades;
    }

    @Override
    protected void start() {
        actorState.addRecord("Register With Preferences");
        ArrayList<Action<Boolean>> actions = new ArrayList<>();
        List<String> getGrade = new ArrayList<>();

        String courseID = preferencesList.get(0);
        getGrade.add(grades.get(0));

        preferencesList.remove(0);
        grades.remove(0);

        ParticipatingInCourseAction tryToRegister = new ParticipatingInCourseAction(courseID,studentID,getGrade);
        sendMessage(tryToRegister,courseID,new CoursePrivateState());
        actions.add(tryToRegister);
        then(actions,()->{
            if(actions.get(0).getResult().get()){
                complete(true);
            }
            else if(!preferencesList.isEmpty())
                         tryToRegisterStudent();
            else {
                complete(false);
                return;
            }

        });
    }

    private void tryToRegisterStudent(){
        ArrayList<Action<Boolean>> actions = new ArrayList<>();
        List<String> getGrade = new ArrayList<>();

        String courseID = preferencesList.get(0);
        getGrade.add(grades.get(0));

        preferencesList.remove(0);
        grades.remove(0);

        ParticipatingInCourseAction tryToRegister = new ParticipatingInCourseAction(courseID,studentID,getGrade);
        sendMessage(tryToRegister,courseID,new CoursePrivateState());
        actions.add(tryToRegister);

        then(actions,()->{
            if(actions.get(0).getResult().get()){
                complete(true);
            }
            else if(!preferencesList.isEmpty()){
                    tryToRegisterStudent();
            }
            else
                complete(false);
                return;
        });
    }
}
