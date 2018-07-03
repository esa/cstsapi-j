package esa.egos.proxy.impl;

import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.proxy.spl.RespondingAssoc;

public class Link
{
    private EE_APIPX_Link pLink;

    private RespondingAssoc pRspAssoc;

    private int regId;

    private IServiceInstanceIdentifier psii;


    public Link(EE_APIPX_Link pLink, RespondingAssoc pRspAssoc, int regId, IServiceInstanceIdentifier psii)
    {
        this.pLink = pLink;
        this.pRspAssoc = pRspAssoc;
        this.regId = regId;
        this.psii = psii;
    }

    public EE_APIPX_Link getpLink()
    {
        return this.pLink;
    }

    public void setpLink(EE_APIPX_Link pLink)
    {
        this.pLink = pLink;
    }

    public RespondingAssoc getpRspAssoc()
    {
        return this.pRspAssoc;
    }

    public void setpRspAssoc(RespondingAssoc pRspAssoc)
    {
        this.pRspAssoc = pRspAssoc;
    }

    public int getRegId()
    {
        return this.regId;
    }

    public void setRegId(int regId)
    {
        this.regId = regId;
    }

    public IServiceInstanceIdentifier getPsii()
    {
        return this.psii;
    }

    public void setPsii(IServiceInstanceIdentifier psii)
    {
        this.psii = psii;
    }
}
