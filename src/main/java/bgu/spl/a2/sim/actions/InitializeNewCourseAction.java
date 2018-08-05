package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

import java.util.List;

public class InitializeNewCourseAction extends Action<Boolean> {
    private List<String> prerequisites;
    private int availableSpaces;

    public InitializeNewCourseAction(List<String> prerequisites, int availableSpaces) {
        this.prerequisites = prerequisites;
        this.availableSpaces = availableSpaces;
    }

    @Override
    protected void start() {
        CoursePrivateState courseState = ((CoursePrivateState)actorState);
        courseState.getPrequisites().addAll(prerequisites);
        courseState.setAvailableSpots(availableSpaces);
        courseState.setRegistered(0);
        complete(true);
    }


}
