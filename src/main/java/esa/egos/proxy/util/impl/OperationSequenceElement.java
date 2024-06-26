/**
 * @(#) EE_APIOpSeqElement.java
 */

package esa.egos.proxy.util.impl;

import esa.egos.csts.api.operations.IOperation;


/**
 * /////////////////////////////////////////////////////// All information
 * needed for the sequenecing of operaton objects. That this class acts as a
 * container of sequencing information for one operation object. The operation
 * object is not ref-counted, this has to be done by the client.
 * ///////////////////////////////////////////////////////
 */
public class OperationSequenceElement extends CondVar
{

    public IOperation op = null;

    public long seqCount = 0;


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.op == null) ? 0 : this.op.hashCode());
        result = prime * result + (int) (this.seqCount ^ (this.seqCount >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        OperationSequenceElement other = (OperationSequenceElement) obj;
        if (this.op == null)
        {
            if (other.op != null)
            {
                return false;
            }
        }
        else if (!this.op.equals(other.op))
        {
            return false;
        }
        if (this.seqCount != other.seqCount)
        {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unused")
    private OperationSequenceElement(final OperationSequenceElement right)
    {
        this.seqCount = right.seqCount;
        this.op = right.op;
    }

    public OperationSequenceElement()
    {
        this.seqCount = 0;
        this.op = null;
    }

    public long getSeqCount()
    {
        return this.seqCount;
    }

    public void setSeqCount(long seqCount)
    {
        this.seqCount = seqCount;
    }

    public IOperation getOp()
    {
        return this.op;
    }

    public void setOp(IOperation op)
    {
        this.op = op;
    }

}
