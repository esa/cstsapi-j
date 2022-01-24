package esa.egos.csts.api.procedures.informationquery;

import java.io.IOException;

import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.notification.NotificationCodecB1;
import esa.egos.csts.api.procedures.notification.NotificationCodecB2;
import esa.egos.csts.api.types.SfwVersion;

public class InformationQueryCodec {
	
	private AbstractInformationQuery informationQuery;
	
	public InformationQueryCodec(AbstractInformationQuery informationQuery) {
		this.informationQuery = informationQuery;
	}
	
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {
		if(informationQuery.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return InformationQueryCodecB2.encodeOperation(operation,isInvoke);
		} else {
			return InformationQueryCodecB1.encodeOperation(operation,isInvoke);
		}
	}

	
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {
		if(informationQuery.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return InformationQueryCodecB2.decodeOperation(informationQuery,encodedPdu);
		} else {
			return InformationQueryCodecB1.decodeOperation(informationQuery,encodedPdu);
		}
	}

	
}
