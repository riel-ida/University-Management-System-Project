package bgu.spl.a2.sim;

import bgu.spl.a2.Promise;

import java.util.concurrent.ConcurrentHashMap;

/**
 * represents a warehouse that holds a finite amount of computers
 *  and their suspended mutexes.
 * 
 */


public class Warehouse {

        private ConcurrentHashMap<String, SuspendingMutex> computers;

        public Warehouse() {
            this.computers = new ConcurrentHashMap<String, SuspendingMutex>();
        }

        public void addNewComputer(String computerType, SuspendingMutex suspendingMutex){
            if(!(computers.containsKey(computerType)))
                computers.put(computerType,suspendingMutex);
        }

        public Promise<Computer> acquireComputer(String computerType) {
            return computers.get(computerType).down();
        }

        public void releaseComputer(String computerType) {
            computers.get(computerType).up();
        }

}


