/**
 * @(#) EE_ElapsedTimer.java
 */

package esa.egos.csts.api.time;

import java.util.Timer;
import java.util.TimerTask;

import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.util.ITimeoutProcessor;

/**
 * Class provides an elapsed time timer. Client of this class should not call
 * the base class (thread) functions with the exception of _AddRef(), and
 * _Release(). When the timer expires, the timer will call ProcessTimeout A
 * condition variable to provide the actual waiting. Note that due to threading
 * scheduling considerations, it is not guaranteed that the accuracy is to the
 * precision of the arguments.
 */
public class ElapsedTimer
{

    private Timer timer = new Timer();;

    private RemindTask currentTask;

    private ITimeoutProcessor gtp;


    public ElapsedTimer()
    {
        this.timer = new Timer();
    }

    public void start(CstsDuration argexpiry, ITimeoutProcessor argtimeoutproc, int invocationId) throws ApiException
    {
        this.gtp = argtimeoutproc;
        if (argexpiry.getSeconds() > 50000000)
        {
            throw new ApiException("no valid time");
        }
        else
        {
            synchronized (this.timer)
            {
                if (this.currentTask != null)
                {
                    throw new ApiException("can't synchronize timer. A task already exists.");
                }
                RemindTask rt = new RemindTask(argtimeoutproc, invocationId);

                this.timer.schedule(rt, argexpiry.getSeconds() * 1000);
                this.currentTask = rt;
            }
        }
    }

    public void restart(CstsDuration argexpiry, int invocationId) throws ApiException
    {
        synchronized (this.timer)
        {
            if (this.currentTask != null)
            {
                this.currentTask.cancelTask();
                this.currentTask = null;
            }
            start(argexpiry, this.gtp, invocationId);
        }
    }

    public void cancel()
    {
        this.timer.cancel();
        synchronized (this.timer)
        {
            if (this.currentTask != null)
            {
                this.currentTask.cancelTask();
                this.currentTask = null;
            }
        }
    }


    class RemindTask extends TimerTask
    {

        private int localInvocationId = 0;

        private final ITimeoutProcessor localTimeoutProcessor;

        private volatile boolean mustRun = true;


        public RemindTask(ITimeoutProcessor argtimeoutproc, int invocationId)
        {
            this.localInvocationId = invocationId;
            this.localTimeoutProcessor = argtimeoutproc;
        }

        public void cancelTask()
        {
            synchronized (ElapsedTimer.this.timer)
            {
                this.mustRun = false;
                cancel();
            }
        }

        @Override
        public void run()
        {
            synchronized (ElapsedTimer.this.timer)
            {
                if (!this.mustRun)
                {
                    return;
                }
                else
                {
                    ElapsedTimer.this.currentTask = null;
                }
            }
            this.localTimeoutProcessor.processTimeout(ElapsedTimer.this, this.localInvocationId);
        }
    }
}
