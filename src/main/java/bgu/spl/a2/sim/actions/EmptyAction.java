package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;

public class EmptyAction extends Action<Boolean>{

    public EmptyAction() {
    }

    @Override
    protected void start() {
        complete(true);
    }
}
