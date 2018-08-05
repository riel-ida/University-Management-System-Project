package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CloseCourseAction extends Action<Boolean> {

    private String courseID;

    public CloseCourseAction( String courseID) {
        this.courseID = courseID;
    }


    @Override
    protected void start() {
        actorState.addRecord("Close Course");
        List<Action<Boolean>> actions = new ArrayList<>();
        UnregisteredAllStudentsAction unregisteredAllStudents = new UnregisteredAllStudentsAction(courseID);
        actions.add(unregisteredAllStudents);
        sendMessage(unregisteredAllStudents, courseID, new CoursePrivateState());
        then(actions, () -> {
            Boolean result = actions.get(0).getResult().get();
            if (result == true) {
                DepartmentPrivateState departmentState = ((DepartmentPrivateState) actorState);
                departmentState.getCourseList().remove(courseID);
                complete(true);
            } else {
                complete(false);
                return;
            }

        });


    }
}

