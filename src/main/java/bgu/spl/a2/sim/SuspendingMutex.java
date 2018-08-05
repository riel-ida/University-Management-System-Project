package bgu.spl.a2.sim;
import bgu.spl.a2.Promise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * this class is related to {@link Computer}
 * it indicates if a computer is free or not
 * 
 * Note: this class can be implemented without any synchronization. 
 * However, using synchronization will be accepted as long as the implementation is blocking free.
 *
 */
public class SuspendingMutex {
	Computer computer;
	AtomicBoolean lock = new AtomicBoolean(false);
	private ConcurrentLinkedDeque<Promise<Computer>> ListOfDep = new ConcurrentLinkedDeque<>();


	/**
	 * Constructor
	 *
	 * @param computer
	 */
	public SuspendingMutex(Computer computer) {
		this.computer = computer;

	}

	/**
	 * Computer acquisition procedure
	 * Note that this procedure is non-blocking and should return immediatly
	 *
	 * @return a promise for the requested computer
	 */
	public Promise<Computer> down() {
		Promise<Computer> promise = new Promise<>();
		if (lock.compareAndSet(false, true)) {  //makes sure the department locks only an available computer
			promise.resolve(computer);
			return promise;
		} else {
			ListOfDep.add(promise);
			return promise;
		}
	}

	/**
	 * Computer return procedure
	 * releases a computer which becomes available in the warehouse upon completion
	 */

	public void up() {
		lock.set(false);
		while (!(ListOfDep.isEmpty())) {
	
			Promise promise = ListOfDep.poll();
			if (promise != null) {
				promise.resolve(computer);
			}//passes the computer to the next department
		}
	}
}

