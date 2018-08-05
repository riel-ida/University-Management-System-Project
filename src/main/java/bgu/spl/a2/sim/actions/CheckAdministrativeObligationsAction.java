package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class CheckAdministrativeObligationsAction extends Action<Boolean> {
    private ArrayList<String> studentsList;
    private ArrayList<String> obligations;
    private String computerType;
    private Warehouse warehouse;

    public CheckAdministrativeObligationsAction(ArrayList<String> studentsList, ArrayList<String> obligations, String computerType, Warehouse warehouse) {
        this.studentsList = studentsList;
        this.obligations = obligations;
        this.computerType = computerType;
        this.warehouse = warehouse;
    }

    @Override
    protected void start() {
        actorState.addRecord("Administrative Check");
        Promise<Computer> computer = warehouse.acquireComputer(computerType);

            computer.subscribe(() -> {

                List<Action<Boolean>> actions = new ArrayList<>();
                for (int i = 0; i < studentsList.size(); i++) {
                    String studentID = studentsList.get(i);
                    CheckStudentObligationsAction checkStudentObligations = new CheckStudentObligationsAction(computer.get(), obligations);
                    actions.add(checkStudentObligations);
                    sendMessage(checkStudentObligations, studentID, new StudentPrivateState());
                }
                then(actions, () -> {
                    warehouse.releaseComputer(computerType);
                    complete(true);
                });

            });
    }
}