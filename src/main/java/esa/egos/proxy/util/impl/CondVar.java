package esa.egos.proxy.util.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CondVar
{

    private final ReentrantLock lock;

    private final Condition condVar;


    public CondVar()
    {
        this.lock = new ReentrantLock();
        this.condVar = this.lock.newCondition();
    }

    public CondVar(ReentrantLock lock)
    {
        this.lock = lock;
        this.condVar = this.lock.newCondition();
    }

    public Condition getConditionVariable()
    {
        return this.condVar;
    }

    public void signalAll()
    {
        this.condVar.signalAll();
    }

    public void lock()
    {
        this.lock.lock();
    }

    public void unlock()
    {
        this.lock.unlock();
    }

    public void await() throws InterruptedException
    {
        this.condVar.await();
    }

    /**
     * Timed wait like java.util.concurrent.locks.Condition
     * @param time
     * @param unit
     * @return	false if the waiting time elapsed	
     * @throws InterruptedException
     */
    public boolean timeWait(long time, TimeUnit unit) throws InterruptedException
    {
        return this.condVar.await(time, unit);
    }
}
