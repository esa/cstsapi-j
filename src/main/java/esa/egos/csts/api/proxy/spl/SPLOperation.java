package esa.egos.csts.api.proxy.spl;

import esa.egos.csts.api.operations.IOperation;

public class SPLOperation
{
    private IOperation pOperation;

    private boolean lastPdu;

    private boolean isInvoke;

    private boolean reportTransmission;


    public SPLOperation(IOperation pOperation, boolean lastPdu, boolean isInvoke, boolean reportTransmission)
    {
        this.pOperation = pOperation;
        this.lastPdu = lastPdu;
        this.isInvoke = isInvoke;
        this.reportTransmission = reportTransmission;
    }

    public SPLOperation()
    {
        this.pOperation = null;
        this.lastPdu = false;
        this.isInvoke = false;
        this.reportTransmission = false;
    }

    public IOperation getpOperation()
    {
        return this.pOperation;
    }

    public void setpOperation(IOperation pOperation)
    {
        this.pOperation = pOperation;
    }

    public boolean isLastPdu()
    {
        return this.lastPdu;
    }

    public void setLastPdu(boolean lastPdu)
    {
        this.lastPdu = lastPdu;
    }

    public boolean isInvoke()
    {
        return this.isInvoke;
    }

    public void setInvoke(boolean isInvoke)
    {
        this.isInvoke = isInvoke;
    }

    public boolean isReportTransmission()
    {
        return this.reportTransmission;
    }

    public void setReportTransmission(boolean reportTransmission)
    {
        this.reportTransmission = reportTransmission;
    }
}
