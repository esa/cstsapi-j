package esa.egos.csts.api.operations.impl.b1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

import com.beanit.jasn1.ber.types.BerOctetString;
import com.beanit.jasn1.ber.types.BerType;

import b1.ccsds.csts.common.operations.pdus.TransferDataInvocation;
import b1.ccsds.csts.common.types.AbstractChoice;
import b1.ccsds.csts.common.types.IntUnsigned;
import b1.ccsds.csts.cyclic.report.pdus.CyclicReportTransferDataInvocDataRef;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.functionalresources.FunctionalResourceMetadata;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.types.Time;
import esa.egos.csts.api.util.CSTS_LOG;

/**
 * This class represents a TRANSFER-DATA operation.
 */
public class TransferData extends AbstractOperation implements ITransferData {

	private static final OperationType TYPE = OperationType.TRANSFER_DATA;

	/**
	 * The generation time
	 */
	private Time generationTime;

	/**
	 * The sequence counter
	 */
	private long sequenceCounter;

	/**
	 * The encoded data
	 */
	private byte[] data;

	/**
	 * The embedded data
	 */
	private EmbeddedData embeddedData;

	/**
	 * The invocation extension
	 */
	private Extension invocationExtension;

	/**
	 * The constructor of a TRANSFER-DATA operation.
	 */
	public TransferData() {
		invocationExtension = Extension.notUsed();
	}

	@Override
	public OperationType getType() {
		return TYPE;
	}

	@Override
	public Time getGenerationTime() {
		return generationTime;
	}

	@Override
	public void setGenerationTime(Time generationTime) {
		this.generationTime = generationTime;
	}

	@Override
	public long getSequenceCounter() {
		return sequenceCounter;
	}

	@Override
	public void setSequenceCounter(long sequenceCounter) {
		this.sequenceCounter = sequenceCounter;
	}

	@Override
	public byte[] getData() {
		return data;
	}

	@Override
	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public EmbeddedData getEmbeddedData() {
		return embeddedData;
	}

	@Override
	public void setEmbeddedData(EmbeddedData embeddedData) {
		this.embeddedData = embeddedData;
	}

	@Override
	public Extension getInvocationExtension() {
		return invocationExtension;
	}

	@Override
	public void setInvocationExtension(EmbeddedData embedded) {
		invocationExtension = Extension.of(embedded);
	}

	@Override
	public void verifyInvocationArguments() throws ApiException {
		if (generationTime == null) {
			throw new ApiException("Invalid TRANSFER-DATA invocation arguments.");
		}
		if (sequenceCounter < 0) {
			throw new ApiException("Invalid TRANSFER-DATA invocation arguments.");
		}
		if (data == null && embeddedData == null) {
			throw new ApiException("Invalid TRANSFER-DATA invocation arguments.");
		}
	}

	/**
	 * Return a String w/ CSTS Transfer-Data operation parameters
	 * @param i capacity
	 * @return String w/ CSTS Transfer-Data parameters
	 */
	@Override
    public String print(int i)
    {
        StringBuilder sb = new StringBuilder(i);
        boolean dataAvailable = this.data != null || this.embeddedData != null;
        String dataString = dataAvailable ? "<logged for csts.api.operations.level = FINEST>" : "";

        if (dataAvailable && CSTS_LOG.CSTS_OP_LOGGER.isLoggable(Level.FINEST))
        {
            if (this.data != null)
            {
                StringBuilder hexData = new StringBuilder(System.lineSeparator());
                CSTS_LOG.dumpHex(this.data, this.data.length, hexData);
                dataString = hexData.toString();
            }
            else
            {
                dataString = print(this.embeddedData);
            }
        }

        sb.append("\nOperation                      : TRANSFER-DATA").append('\n');
        sb.append(super.print(i));
        sb.append("Confirmed Operation            : false\n");
        sb.append("Generation time                : ").append(this.generationTime.toInstant()).append('\n');
        sb.append("Sequence counter               : ").append(this.sequenceCounter).append('\n');
        sb.append("Data                           : ").append(dataString).append('\n');

        return sb.toString();
    }

    protected String print(EmbeddedData embeddedData)
    {
        String ret = "";
        if (embeddedData.getOid().equals(OIDs.crTransferDataInvocDataRef))
        {
            StringBuilder sb = new StringBuilder("\n");
            CyclicReportTransferDataInvocDataRef dataRefinement = new CyclicReportTransferDataInvocDataRef();
            try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData()))
            {
                dataRefinement.decode(is);
                sb.append("Number of QualifiedParameter(s): ")
                        .append(dataRefinement.getQualifiedParameters().getQualifiedParameter().size()).append('\n');
                for (b1.ccsds.csts.common.types.QualifiedParameter param : dataRefinement.getQualifiedParameters()
                        .getQualifiedParameter())
                {
                    QualifiedParameter qualifiedParameter = QualifiedParameter.decode(param);
                    String value = FunctionalResourceMetadata.getInstance().toString(qualifiedParameter);
                    if (value != null)
                    {
                        sb.append(value);
                    }
                    else
                    {
                        sb.append(qualifiedParameter.toString()).append('\n');
                    }
                }

                ret = sb.toString();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            ret = this.embeddedData.toString();
        }

        return ret;
    }

	@Override
	public TransferDataInvocation encodeTransferDataInvocation() {
		TransferDataInvocation transferDataInvocation = new TransferDataInvocation();
		transferDataInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		transferDataInvocation.setGenerationTime(generationTime.encode(new b1.ccsds.csts.common.types.Time ()));
		transferDataInvocation.setSequenceCounter(new IntUnsigned(sequenceCounter));
		AbstractChoice choice = new AbstractChoice();
		if (data != null) {
			choice.setOpaqueString(new BerOctetString(data));
		} else if (embeddedData != null) {
			choice.setExtendedData(embeddedData.encode(new b1.ccsds.csts.common.types.Embedded()));
		}
		transferDataInvocation.setData(choice);
		transferDataInvocation.setTransferDataInvocationExtension(invocationExtension.encode(
				new b1.ccsds.csts.common.types.Extended()));
		return transferDataInvocation;
	}

	@Override
	public void decodeTransferDataInvocation(BerType transferDataInvocation) {
		TransferDataInvocation transferDataInvocation1 = (TransferDataInvocation )transferDataInvocation;
		decodeStandardInvocationHeader(transferDataInvocation1.getStandardInvocationHeader());
		generationTime = Time.decode(transferDataInvocation1.getGenerationTime());
		sequenceCounter = transferDataInvocation1.getSequenceCounter().longValue();
		if (transferDataInvocation1.getData().getOpaqueString() != null) {
			data = transferDataInvocation1.getData().getOpaqueString().value;
		} else if (transferDataInvocation1.getData().getExtendedData() != null) {
			embeddedData = EmbeddedData.decode(transferDataInvocation1.getData().getExtendedData());
		}
		invocationExtension = Extension.decode(transferDataInvocation1.getTransferDataInvocationExtension());
	}

	@Override
	public String toString() {
		return "TransferData [generationTime=" + generationTime + ", sequenceCounter=" + sequenceCounter + ", data="
				+ Arrays.toString(data) + ", embeddedData=" + embeddedData + ", invocationExtension="
				+ invocationExtension + "]";
	}

}
