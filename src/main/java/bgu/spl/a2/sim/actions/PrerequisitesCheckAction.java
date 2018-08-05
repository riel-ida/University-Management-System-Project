package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
import bgu.spl.a2.sim.Computer;


import java.util.*;

public class PrerequisitesCheckAction extends Action<Boolean> {
    private String courseID;
    private List<String> coursePrerequisites;
    private Integer grade;

    public PrerequisitesCheckAction(String courseID, List<String> coursePrerequisites, Integer grade) {
        this.courseID = courseID;
        this.coursePrerequisites = coursePrerequisites;
        this.grade = grade;
    }

    @Override
    protected void start() {
        StudentPrivateState studentState = ((StudentPrivateState)actorState);
        HashMap<String, Integer> studentGrades = studentState.getGrades();

        boolean pass = true;
        for(int i=0; i<coursePrerequisites.size() && pass; i++) {
            if (!studentGrades.containsKey(coursePrerequisites.get(i))) {
                pass = false;
            }
        }
        if(pass) {
            complete(true);
        }
        else {
            complete(false);
            return;
        }
    }
}

