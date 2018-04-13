package esa.egos.csts.api.enums;

public enum TimeFormat
{

    TF_dayOfMonth(0), 
    TF_dayOfYear(1);

    private int code;


    /**
     * Constructor SLE_TimeFmt.
     * 
     * @param code
     */
    private TimeFormat(int code)
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
