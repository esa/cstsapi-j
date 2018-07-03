package esa.egos.proxy.time;

public enum CstsTimerState
{

    eeTIME_TimerCREATED(0),
    eeTIME_TimerIDLE(1),
    eeTIME_TimerSTARTING(2),
    eeTIME_TimerWAITING(3),
    eeTIME_TimerEXITING(4);

    private int code;


    private CstsTimerState(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return this.code;
    }
}
