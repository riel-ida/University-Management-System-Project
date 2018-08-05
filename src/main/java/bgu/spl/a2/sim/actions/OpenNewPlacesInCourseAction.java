package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;


public class OpenNewPlacesInCourseAction extends Action<Boolean> {
    private Integer number;
    public OpenNewPlacesInCourseAction(Integer theNumber) {
        number = theNumber;
    }

    @Override
    protected void start() {
        actorState.addRecord("Open New Places in Course");
        CoursePrivateState courseState = (CoursePrivateState) actorState;
        if (courseState.getAvailableSpots() != -1) {
            courseState.setAvailableSpots(courseState.getAvailableSpots() + number);
            complete(true);
        } else {
            complete(false);
        }
    }
}