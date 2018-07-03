package esa.egos.csts.api.enumerations;

public enum OperationResult {
	
	RES_positive(0, "positive"), 
	RES_negative(1, "negative"), 
	RES_invalid(-1, "invalid");

    private int code;

    private String msg;


    /**
     * Constructor SLE_Result.
     * 
     * @param code
     * @param msg
     */
    private OperationResult(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
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

    @Override
    public String toString()
    {
        return this.msg;
    }

    /**
     * Gets the SLE result by code.
     * 
     * @param code
     * @return null if there is no SLE result on the given code.
     */
    public static OperationResult getSLE_ResultByCode(int code)
    {
        for (OperationResult e : values())
        {
            if (e.code == code)
            {
                return e;
            }
        }

        return null;
    }

}
