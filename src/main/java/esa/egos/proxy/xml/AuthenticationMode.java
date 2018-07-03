package esa.egos.proxy.xml;

public enum AuthenticationMode
{
    NONE(0, "NONE"), /* authentication not used */
    BIND_ONLY(1, "BIND-ONLY"), /* authetication only for bind */
    ALL(2, "ALL"); /* authentication for all operations */

    private int code;

    private String msg;


    AuthenticationMode(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public int getCode()
    {
        return this.code;
    }

    @Override
    public String toString()
    {
        return this.msg;
    }
}
