package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;


import java.util.ArrayList;
import java.util.List;

public class UnregisterAction extends Action<Boolean> {
    private String courseID;
    private String studentID;

    public UnregisterAction(String courseID, String studentID) {
        this.courseID = courseID;
        this.studentID = studentID;
    }

    @Override
    protected void start() {
        actorState.addRecord("Unregister");

        List<Action<Boolean>> emptyActions = new ArrayList<>();
        EmptyAction unregisterEmptyAction = new EmptyAction();
        emptyActions.add(unregisterEmptyAction);
        sendMessage(unregisterEmptyAction, studentID, new StudentPrivateState());

        then(emptyActions, () -> {
            CoursePrivateState courseState = ((CoursePrivateState) actorState);
            if (courseState.getRegStudents().contains(studentID)) {
                List<Action<Boolean>> thenActions = new ArrayList<>();
                RemoveGradeSheetAction removeGradeSheetAction = new RemoveGradeSheetAction(courseID);//remove the course from the student's grades Sheet
                thenActions.add(removeGradeSheetAction);
                sendMessage(removeGradeSheetAction, studentID, new StudentPrivateState());
                then(thenActions, () -> {
                    Boolean result = thenActions.get(0).getResult().get();
                    if (result == true) {
                        courseState.getRegStudents().remove(studentID);
                        courseState.setRegistered(courseState.getRegistered() - 1);

                        if(((CoursePrivateState) actorState).getAvailableSpots() != -1) {
                            courseState.setAvailableSpots(courseState.getAvailableSpots() + 1);
                        }

                        complete(true);
                    } else {
                        complete(false);
                        return;
                    }
                });
            } else {
                complete(false);
                return;
            }
        });


    }
}




