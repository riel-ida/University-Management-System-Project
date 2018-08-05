package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
import bgu.spl.a2.sim.Computer;


import java.util.*;

public class PassesPrerequisitesAction extends Action<Boolean> {
    private String courseID;
    private List<String> coursePrerequisites;
    private Integer grade;

    public PassesPrerequisitesAction(String courseID, List<String> coursePrerequisites, Integer grade) {
        this.courseID = courseID;
        this.coursePrerequisites = coursePrerequisites;
        this.grade = grade;
    }

    @Override
    protected void start() {
        StudentPrivateState studentState = ((StudentPrivateState)actorState);
        HashMap<String, Integer> studentGrades = studentState.getGrades();
        boolean pass = true;
        Iterator<String> iterator = coursePrerequisites.iterator();
        while(iterator.hasNext() && pass){
            if(studentGrades.get(iterator.next()) < 57)
                pass = false;
        }
        if(pass) {
            studentGrades.put(courseID, grade);
            actorState.addRecord("Passes Prerequisites");
            complete(true);
        }
        else
            complete(false);
    }
}

