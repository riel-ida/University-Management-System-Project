package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.HashMap;
import java.util.List;

public class RegisterStudent extends Action<Boolean> {
    private String courseID;
    private Integer grade;

    public RegisterStudent(String courseID , Integer grade) {
        this.courseID = courseID;
        this.grade = grade;
    }

    @Override
    protected void start() {
        StudentPrivateState studentState = ((StudentPrivateState)actorState);
        HashMap<String, Integer> studentGrades = studentState.getGrades();

        studentGrades.put(courseID,grade);
        complete(true);
    }
}


