package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class RemoveGradeSheetAction extends Action<Boolean> {
    private String courseID;

    public RemoveGradeSheetAction(String courseID){
        this.courseID = courseID;
    }
    @Override
    protected void start() {
        if(((StudentPrivateState)actorState).getGrades().containsKey(courseID)){
            ((StudentPrivateState)actorState).getGrades().remove(courseID);
            complete(true);
        }
        else {
            complete(false);
            return;
        }
    }
}