package bgu.spl.a2;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {

	int nthreads;
    private ConcurrentHashMap<String, PrivateState> actors = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ActorData> actorData = new ConcurrentHashMap<>();
	private Set<Thread> threads = new HashSet<Thread>();
	VersionMonitor versionMonitor = new VersionMonitor();


	/**
	 * creates a {@link ActorThreadPool} which has nthreads. Note, threads
	 * should not get started until calling to the {@link #start()} method.
	 * <p>
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads the number of threads that should be started by this thread
	 *                 pool
	 */
	public ActorThreadPool(int nthreads) {
		this.nthreads = nthreads;
		for (int i = 0; i < nthreads; i++) {
			EventLoop eventLoop = new EventLoop(this);
			Thread thread = new Thread(eventLoop);
			threads.add(thread);
		}
	}

    /**
     * getter for actors
     *
     * @return actors
     */
    public Map<String, PrivateState> getActors() {
        if (!actors.isEmpty())
            return actors;
        return null;
    }

    /**
     * getter for actor's private state
     *
     * @param actorId actor's id
     * @return actor's private state
     */
    public PrivateState getPrivateState(String actorId) {
        PrivateState privateState = actors.get(actorId);
        return privateState;
    }

    /**
     * getter for actorData
     *
     * @return actorData
     */
    protected Map<String, ActorData> getActorData() {
        return actorData;
    }

	/**
	 * submits an action into an actor to be executed by a thread belongs to
	 * this thread pool
	 *
	 * @param action     the action to execute
	 * @param actorId    corresponding actor's id
	 * @param actorState actor's private state (actor's information)
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {
        actors.putIfAbsent(actorId,actorState);
        actorData.putIfAbsent(actorId, new ActorData());
        actorData.get(actorId).getActions().add(action);
		versionMonitor.inc();
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and waits
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 * <p>
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException if the thread that shut down the threads is interrupted
	 */
	public void shutdown() throws InterruptedException {
        for (Thread t : threads ) {
            t.interrupt();
        }
        for (Thread t : threads ) {
            t.join();
        }
	}


	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		for (Thread t : threads ) {
			t.start();
		}
	}

	class ActorData { //an inner class that represents an actor's queue of actions and it's lock flag (not PrivateState)

		//Fields
		protected ReentrantLock lock = new ReentrantLock();
		private ConcurrentLinkedQueue<Action> actions = new ConcurrentLinkedQueue<Action>();

		//Methods
		protected ConcurrentLinkedQueue<Action> getActions() { //Returns the actor's queue of actions
			return actions;
		}

    }
}


