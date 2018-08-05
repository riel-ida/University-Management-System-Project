package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class OpenCourseAction extends Action<Boolean> {
    private List<String> prerequisites;
    private String courseID;
    private int availableSpaces;


    public OpenCourseAction(List<String> prerequisites, String courseID, int availableSpacs) {
        this.prerequisites = prerequisites;
        this.courseID = courseID;
        this.availableSpaces = availableSpacs;

    }

    @Override
    protected void start() {
        actorState.addRecord("Open Course");
        List<Action<Boolean>> actions = new ArrayList<>();
        InitializeNewCourseAction initializeNewCourse = new InitializeNewCourseAction(prerequisites, availableSpaces);
        actions.add(initializeNewCourse);
        sendMessage(initializeNewCourse, courseID, new CoursePrivateState());
        then(actions, ()->{

            Boolean result = actions.get(0). getResult().get();
            if(result.equals(true)){
                DepartmentPrivateState departmentState = ((DepartmentPrivateState)actorState);
                departmentState.getCourseList().add(courseID);
                complete(true);
            } else {
                complete(false);
                return;
            }

        });



    }
}
