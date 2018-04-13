package esa.egos.csts.api.proxy.impl;

public abstract class Message
{
    public abstract byte[] toByteArray();

    public abstract void fromByteArray(byte[] data);
}
