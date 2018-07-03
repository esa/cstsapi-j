package esa.egos.proxy.tml;

import java.io.InputStream;

public class HBMessageFactory implements ITMLMessageFactory
{

    @Override
    public TMLMessage createTmlMessage(byte[] initialEightBytes, InputStream is)
    {
        return new HBMessage();
    }

    public TMLMessage createTmlMessage()
    {
        return new HBMessage();
    }

}
