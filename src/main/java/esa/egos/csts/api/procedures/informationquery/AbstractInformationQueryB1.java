package esa.egos.csts.api.procedures.informationquery;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

import b1.ccsds.csts.common.operations.pdus.GetInvocation;
import b1.ccsds.csts.common.operations.pdus.GetReturn;
import b1.ccsds.csts.information.query.pdus.InformationQueryPdu;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.operations.IOperation;

public abstract class AbstractInformationQueryB1 extends AbstractInformationQuery {

	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {

		byte[] encodedOperation;
		InformationQueryPdu pdu = new InformationQueryPdu();

		if (operation.getType() == OperationType.GET) {
			IGet get = (IGet) operation;
			if (isInvoke) {
				pdu.setGetInvocation((GetInvocation)get.encodeGetInvocation());
			} else {
				pdu.setGetReturn((GetReturn)get.encodeGetReturn());
			}
		}

		try (ReverseByteArrayOutputStream berBAOStream = new ReverseByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}

	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {

		InformationQueryPdu pdu = new InformationQueryPdu();

		try (ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu)) {
			pdu.decode(is);
		}

		IGet get = createGet();

		if (pdu.getGetInvocation() != null) {
			get.decodeGetInvocation(pdu.getGetInvocation());
		} else if (pdu.getGetReturn() != null) {
			get.decodeGetReturn(pdu.getGetReturn());
		}

		return get;
	}

}
