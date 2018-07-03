package esa.egos.proxy.tml;

import java.io.IOException;
import java.io.OutputStream;

import esa.egos.csts.api.exceptions.ApiException;


public class UrgentByteMessage extends TMLMessage
{
    private final int paDiag;


    public UrgentByteMessage(int diag)
    {
        this.paDiag = diag;
    }

    @Override
    public void writeTo(OutputStream socketOutStream) throws ApiException, IOException
    {       
    }

    @Override
    public void processOn(Channel channel)
    {
        channel.peerAbortInd(this);
    }

    @Override
    public int getLength()
    {
        return 1;
    }

    public int getUBDiagnostic()
    {
        return this.paDiag;
    }
}
