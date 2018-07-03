package esa.egos.proxy.tml;

import java.io.IOException;
import java.io.OutputStream;

import esa.egos.csts.api.exceptions.ApiException;

public abstract class TMLMessage
{
    public abstract void writeTo(OutputStream socketOutStream) throws ApiException, IOException;

    public abstract void processOn(Channel channel);

    public abstract int getLength();
}
