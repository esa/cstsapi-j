package esa.egos.csts.api.enums;

public enum Component {
	
    CP_application(0), 
    CP_serviceElement(1),
    CP_proxy(2),
    CP_operations(3), // TODO check
    CP_utilities(4), // TODO check
    CP_invalid(-1);

    private int code;
    
    /**
     * Constructor SLE_Component.
     * 
     * @param code
     * @param msg
     */
    private Component(int code)
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
    
	public static Component getComponentByCode(int readInt) {
		
        for (Component e : values())
        {
            if (e.code == readInt)
            {
                return e;
            }
        }

        return null;
	}
    
}
