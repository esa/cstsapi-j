package esa.egos.proxy.util;

public interface ITimeoutProcessor {

    /**
     * Invoked when the timer expires
     * 
     * @param timer the elapsed timer
     * @param invocationId the invocation identifier
     */
    void processTimeout(Object timer, int invocationId);

    /**
     * Invoked when a timer can no longer provide timer notifications
     * 
     * @param timer the elapsed timer
     */
    void handlerAbort(Object timer);
}
