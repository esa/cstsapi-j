package esa.egos.proxy.enums;

public enum TimeRes
{
    TR_minutes(0),
    TR_seconds(1),
    TR_hundredMilliSec(2),
    TR_tenMilliSec(3),
    TR_milliSec(4),
    TR_hundredMicroSec(5),
    TR_tenMicroSec(6),
    TR_microSec(7),
    TR_hundredNanoSec(8),
    TR_tenNanoSec(9),
    TR_nanoSec(10),
    TR_hundredPicoSec(11),
    TR_tenPicoSec(12),
    TR_picoSec(13);

    private int code;


    /**
     * Constructor SLE_TimeRes.
     * 
     * @param code
     */
    private TimeRes(int code)
    {
        this.code = code;
    }

    /**
     * Gets the code.
     * 
     * @return
     */
    public int getCode()
    {
        return this.code;
    }
}
