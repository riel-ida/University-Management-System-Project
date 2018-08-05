package bgu.spl.a2;

import java.util.Map;


public class EventLoop implements Runnable {

    private ActorThreadPool pool;


    public EventLoop(ActorThreadPool pool) {
        this.pool = pool;
    }

    public  void run() {
        while (!Thread.currentThread().isInterrupted()) {
            int curVer = pool.versionMonitor.getVersion();
            for (Map.Entry<String, ActorThreadPool.ActorData> entry : pool.getActorData().entrySet()) {
                if (((entry.getValue().lock.tryLock() && !(entry.getValue().getActions().isEmpty())))) {

                    Action action = entry.getValue().getActions().poll(); //pulls the action out of the queue
                    action.handle(pool, entry.getKey(), pool.getPrivateState(entry.getKey())); //handle the action
                    entry.getValue().lock.unlock(); //release the actor
                    pool.versionMonitor.inc(); //increments the version number after releasing the actor
                }
                else
                    continue;
            }
            if (curVer == pool.versionMonitor.getVersion()) {
                try {
                        pool.versionMonitor.await(curVer);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        }
    }
}



