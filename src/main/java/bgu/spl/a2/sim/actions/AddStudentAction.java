package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AddStudentAction extends Action<Boolean> {
    private String studentID;

    public AddStudentAction(String studentID) {
        this.studentID = studentID;
    }

    @Override
    protected void start() {
        actorState.addRecord("Add Student");
        DepartmentPrivateState departmentState = ((DepartmentPrivateState)actorState);
        departmentState.getStudentList().add(studentID);

        sendMessage(new EmptyAction(),studentID,new StudentPrivateState());

        complete(true);
        
    }
}



