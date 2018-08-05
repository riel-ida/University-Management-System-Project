package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.Map;

public class CheckStudentObligationsAction extends Action<Boolean> {
    private Computer computer;
    private ArrayList<String> obligations;


    public CheckStudentObligationsAction(Computer computer, ArrayList<String> obligations) {
        this.computer = computer;
        this.obligations = obligations;
    }

    @Override
    protected void start() {
        Map gradeSheet = ((StudentPrivateState)actorState).getGrades();
        long sig = computer.checkAndSign(obligations,gradeSheet);
        ((StudentPrivateState)actorState).setSignature(sig);
        complete(true);
    }
}

