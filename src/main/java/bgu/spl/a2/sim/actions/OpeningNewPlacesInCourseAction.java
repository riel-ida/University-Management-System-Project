package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;


public class OpeningNewPlacesInCourseAction extends Action<Boolean> {

    @Override
    protected void start() {
        CoursePrivateState courseState = (CoursePrivateState) actorState;
        courseState.setAvailableSpots(courseState.getAvailableSpots() + 1);
        actorState.addRecord("Open New Places in Course");
        complete(true);
    }
}
