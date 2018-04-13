package esa.egos.csts.api.proxy.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.proxy.tml.ISP1ProtocolAbortDiagnostics;
import esa.egos.csts.api.proxy.tml.types.EE_APIPX_ISP1ProtocolAbortOriginator;

public class AssocChannel_Mess extends Message
{
    static private Logger LOG = Logger.getLogger(AssocChannel_Mess.class.getName());

    private int diagnostic;

    private boolean originatorIsLocal;

    private ISP1ProtocolAbortDiagnostics paOriginator;


    public AssocChannel_Mess(int diagnostic,
                                  boolean originatorIsLocal,
                                  ISP1ProtocolAbortDiagnostics paOriginator)
    {
        this.diagnostic = diagnostic;
        this.originatorIsLocal = originatorIsLocal;
        this.paOriginator = paOriginator;
    }

    public AssocChannel_Mess(byte[] data)
    {
        fromByteArray(data);
    }

    public AssocChannel_Mess()
    {}

    public int getDiagnostic()
    {
        return this.diagnostic;
    }

    public void setDiagnostic(int diagnostic)
    {
        this.diagnostic = diagnostic;
    }

    public boolean isOriginatorIsLocal()
    {
        return this.originatorIsLocal;
    }

    public void setOriginatorIsLocal(boolean originatorIsLocal)
    {
        this.originatorIsLocal = originatorIsLocal;
    }

    public ISP1ProtocolAbortDiagnostics getPaOriginator()
    {
        return this.paOriginator;
    }

    public void setPaOriginator(ISP1ProtocolAbortDiagnostics paOriginator)
    {
        this.paOriginator = paOriginator;
    }

    @Override
    public byte[] toByteArray()
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            // set the diagnostic
            dos.writeInt(this.diagnostic);

            // set the originatorIsLocal
            dos.writeBoolean(this.originatorIsLocal);

            // set the paOriginator
            if (this.paOriginator != null)
            {
                dos.writeInt(this.paOriginator.getTcpErrorCode());
                dos.writeInt(this.paOriginator.getTmlDiagnosticCode());
                dos.writeInt(this.paOriginator.getPaOriginator().getCode());
            }

            dos.flush();
        }
        catch (IOException e)
        {
            LOG.log(Level.FINE, "IOException ", e);
            return null;
        }

        return bos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] data)
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bis);

        try
        {
            // set the diagnostic
            this.diagnostic = dis.readInt();

            // set the originatorIsLocal
            this.originatorIsLocal = dis.readBoolean();

            // set the paOriginator
            int tcpEc = dis.readInt();
            int tmlDc = dis.readInt();
            EE_APIPX_ISP1ProtocolAbortOriginator pao = EE_APIPX_ISP1ProtocolAbortOriginator.getISP1PaOByCode(dis
                    .readInt());
            this.paOriginator = new ISP1ProtocolAbortDiagnostics(pao, tmlDc, tcpEc);
        }
        catch (IOException e)
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("No available paOriginator");
            }
            return;
        }
    }

    @Override
    public String toString()
    {
        return "PXCS_AssocChannel Message[diagnostic = " + this.diagnostic + ", originatorIsLocal = "
               + this.originatorIsLocal + ", paOriginator = " + this.paOriginator + "]";
    }
}
