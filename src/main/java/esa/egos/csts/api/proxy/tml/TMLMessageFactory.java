package esa.egos.csts.api.proxy.tml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.proxy.GenStrUtil;
import esa.egos.csts.api.proxy.tml.types.EE_APIPX_TMLErrors;
import esa.egos.csts.api.util.impl.Reference;

public class TMLMessageFactory
{
    private static final Logger LOG = Logger.getLogger(TMLMessageFactory.class.getName());

    public Map<Integer, ITMLMessageFactory> factories = new TreeMap<Integer, ITMLMessageFactory>();

    private final byte[] buf = new byte[8];


    public TMLMessageFactory()
    {
        this.factories.put(0x03, new HBMessageFactory());
        this.factories.put(0x01, new PDUMessageFactory());
        this.factories.put(0x02, new CtxMessageFactory());
    }

    public TMLMessage decodeFrom(InputStream is, Reference<EE_APIPX_TMLErrors> error) throws IOException,
                                                                                                 ApiException
    {
        int read = 0;
        try
        {
            read = is.read(this.buf, 0, this.buf.length);
            if (read < 0)
            {
                error.setReference(EE_APIPX_TMLErrors.eeAPIPXtml_unexpectedClose);
                throw new IOException("Received <0 bytes from socket");
            }
        }
        catch (IOException e)
        {
            error.setReference(EE_APIPX_TMLErrors.eeAPIPXtml_unexpectedClose);
            throw e;
        }

        GenStrUtil.print("Read from socket: ", this.buf);

        if (read == 8)
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("8 bytes read from socket");
            }

            ITMLMessageFactory selectedFactory = this.factories.get((int) this.buf[0]);
            if (selectedFactory != null)
            {
                return selectedFactory.createTmlMessage(this.buf, is);
            }
            else
            {
                // the 8 bytes read from the input stream do not represent a
                // valid TML message
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("the 8 bytes read from the input stream do not represent a valid TML message");
                }
                error.setReference(EE_APIPX_TMLErrors.eeAPIPXtml_badTMLMsg);
                return null;
            }
        }
        else
        {
            if (read == 1)
            {
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("1 byte read from the socket - assume a PEER ABORT received with diagnostic "
                               + this.buf[0]);
                }
                // assume buf[0] peer abort diagnostic
                return new UrgentByteMessage(this.buf[0]);

            }
            else
            {
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest(">1 and < 8 byte read from the socket - assume a PEER ABORT received with diagnostic "
                               + this.buf[read - 1]);
                }
                // assume buf[read - 1] peer abort diagnostic
                return new UrgentByteMessage(this.buf[read - 1]);
            }
        }
    }

    public TMLMessage createPDUMsg(byte[] buffer)
    {
        PDUMessageFactory pduMsgFactory = (PDUMessageFactory) this.factories.get(0x01);
        return pduMsgFactory.createPDUMessage(buffer);
    }

    public TMLMessage createHBMsg()
    {
        HBMessageFactory hbMsgFactory = (HBMessageFactory) this.factories.get(0x03);
        return hbMsgFactory.createTmlMessage();
    }

    public TMLMessage createCtxMsg(int hbt, int deadFactor)
    {
        CtxMessageFactory cxtMessageFactory = (CtxMessageFactory) this.factories.get(0x02);
        return cxtMessageFactory.createTmlMessage(hbt, deadFactor);
    }
}
