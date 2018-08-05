package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

import java.util.ArrayList;
import java.util.List;


public class UnregisteredAllStudentsAction extends Action<Boolean> {
    private String courseID;

    public UnregisteredAllStudentsAction(String courseID) {
        this.courseID = courseID;
    }

    @Override
    protected void start() {
        ((CoursePrivateState) actorState).setAvailableSpots(-1);
        List<Action<Boolean>> actions = new ArrayList<>();
        int regSize = ((CoursePrivateState) actorState).getRegStudents().size();
        for (int i = 0; i < regSize; i++) {
            String currStudent = ((CoursePrivateState) actorState).getRegStudents().get(i);
            UnregisterAction unregisterStudent = new UnregisterAction(courseID, currStudent);
            actions.add(unregisterStudent);
            sendMessage(unregisterStudent, courseID, new CoursePrivateState());
        }

        then(actions, () -> {
            for (int i = 0; i < actions.size(); i++) {
                if(actions.get(i).getResult().get() == false)
                {
                    complete(false);
                    return;
                }
            }

            complete(true);
        });
    }
}
