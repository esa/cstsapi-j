package esa.egos.csts.api.procedures;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;

import ccsds.csts.information.query.pdus.InformationQueryPdu;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ConfigException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.impl.Get;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.serviceinstance.states.InactiveState;

public abstract class AbstractInformationQuery extends AbstractStatelessProcedure implements IInformationQuery {

	private final ProcedureType type = new ProcedureType(OIDs.informationQuery);

	protected AbstractInformationQuery() {
		super(new InactiveState());
	}

	@Override
	public ProcedureType getType() {
		return type;
	}

	@Override
	protected void initOperationSet() {
		getDeclaredOperations().add(IGet.class);
	}

	@Override
	protected void initialiseOperationFactory() {
		getOperationFactoryMap().put(IGet.class, this::createGet);
	}

	/**
	 * Creates a new GET operation and sets relevant information.
	 * 
	 * @return the created GET operation
	 */
	protected IGet createGet() {
		IGet get = new Get();
		get.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			get.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			e.printStackTrace();
		}
		return get;
	}

	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws ApiException, IOException {

		byte[] encodedOperation;
		InformationQueryPdu pdu = new InformationQueryPdu();

		if (IGet.class.isAssignableFrom(operation.getClass())) {
			IGet get = (IGet) operation;
			if (isInvoke) {
				pdu.setGetInvocation(get.encodeGetInvocation());
			} else {
				pdu.setGetReturn(get.encodeGetReturn());
			}
		}

		try (BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}

	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws ApiException, IOException {

		InformationQueryPdu pdu = new InformationQueryPdu();

		try (ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu)) {
			pdu.decode(is);
		}

		IGet get = createGet();

		if (pdu.getGetInvocation() != null) {
			get.decodeGetInvocation(pdu.getGetInvocation());
		} else if (pdu.getGetReturn() != null) {
			get.decodeGetReturn(pdu.getGetReturn());
		} else {
			throw new ApiException("PDU not compatible to specified operation.");
		}

		return get;
	}

}
