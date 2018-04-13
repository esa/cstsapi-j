package esa.egos.csts.api.procedures.impl;

import java.util.Arrays;

import ccsds.csts.object.identifiers.OidValues;
import esa.egos.csts.api.enums.ProcedureTypeEnum;
import esa.egos.csts.api.main.ObjectIdentifier;

public class ProcedureType {

	@Override
	public String toString() {
		return "ProcedureType [procedureTypeEnum=" + procedureTypeEnum + ", oid=" + oid + "]";
	}

	private final ProcedureTypeEnum procedureTypeEnum;

	private final int[] associationControlOID = OidValues.associationControl.value;
	private final int[] bufferedDataDeliveryOID = OidValues.bufferedDataDelivery.value;
	private final int[] dataProcessingOID = OidValues.dataProcessing.value;
	private final int[] informationQueryOID = OidValues.informationQuery.value;
	private final int[] notificationOID = OidValues.notification.value;
	private final int[] throwEventOID = OidValues.throwEvent.value;
	private final int[] unbufferedDataDeliveryOID = OidValues.unbufferedDataDelivery.value;
	private final int[] cyclicReportOID = OidValues.cyclicReport.value;
	private final int[] bufferedDataProcessingOID = OidValues.bufferedDataProcessing.value;
	private final int[] sequenceControlledDataProcessingOID = OidValues.sequenceControlledDataProcessing.value;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oid == null) ? 0 : oid.hashCode());
		result = prime * result + ((procedureTypeEnum == null) ? 0 : procedureTypeEnum.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcedureType other = (ProcedureType) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		if (procedureTypeEnum != other.procedureTypeEnum)
			return false;
		return true;
	}

	private final ObjectIdentifier oid;

	/**
	 * For creating a standard procedure type.
	 * 
	 * @param procedureTypeEnum
	 */
	public ProcedureType(ProcedureTypeEnum procedureTypeEnum) {
		super();
		this.procedureTypeEnum = procedureTypeEnum;

		this.oid = new ObjectIdentifier(checkOid(procedureTypeEnum));

	}

	protected int[] checkOid(ProcedureTypeEnum procedureTypeEnum2) {

		int[] oidArray = null;

		// TODO fill in OID
		switch (procedureTypeEnum) {
		case associationControl:
			oidArray = associationControlOID;
			break;
		case bufferedDataDelivery:
			oidArray = bufferedDataDeliveryOID;
			break;
		case dataProcessing:
			oidArray = dataProcessingOID;
			break;
		case extended:
			oidArray = setExtendedOID();
			break;
		case informationQuery:
			oidArray = informationQueryOID;
			break;
		case invalid:
			oidArray = null;
			break;
		case notification:
			oidArray = notificationOID;
			break;
		case throwEvent:
			oidArray = throwEventOID;
			break;
		case unbufferedDataDelivery:
			oidArray = unbufferedDataDeliveryOID;
			break;
		case bufferedDataProcessing:
			oidArray = bufferedDataProcessingOID;
			break;
		case cyclicReport:
			oidArray = cyclicReportOID;
			break;
		case sequenceControlledDataProcessing:
			oidArray = sequenceControlledDataProcessingOID;
			break;
		}
		return oidArray;
	}

	protected int[] setExtendedOID() {
		return null;
	}

	/**
	 * For creating an extended procedure type with the given object id.
	 * 
	 * @param oid
	 */
	public ProcedureType(ObjectIdentifier oid) {
		super();

		if (oid == null) {
			this.procedureTypeEnum = ProcedureTypeEnum.invalid;
			this.oid = null;
			return;
		}

		if (Arrays.equals(oid.getOid(), associationControlOID)) {
			this.procedureTypeEnum = ProcedureTypeEnum.associationControl;
			this.oid = new ObjectIdentifier(checkOid(this.procedureTypeEnum));
		} else if (Arrays.equals(oid.getOid(), informationQueryOID)) {
			this.procedureTypeEnum = ProcedureTypeEnum.informationQuery;
			this.oid = new ObjectIdentifier(checkOid(this.procedureTypeEnum));
		} else if (Arrays.equals(oid.getOid(), bufferedDataDeliveryOID)) {
			this.procedureTypeEnum = ProcedureTypeEnum.bufferedDataDelivery;
			this.oid = new ObjectIdentifier(checkOid(this.procedureTypeEnum));
		} else if (Arrays.equals(oid.getOid(), dataProcessingOID)) {
			this.procedureTypeEnum = ProcedureTypeEnum.dataProcessing;
			this.oid = new ObjectIdentifier(checkOid(this.procedureTypeEnum));
		} else if (Arrays.equals(oid.getOid(), notificationOID)) {
			this.procedureTypeEnum = ProcedureTypeEnum.notification;
			this.oid = new ObjectIdentifier(checkOid(this.procedureTypeEnum));
		} else if (Arrays.equals(oid.getOid(), throwEventOID)) {
			this.procedureTypeEnum = ProcedureTypeEnum.throwEvent;
			this.oid = new ObjectIdentifier(checkOid(this.procedureTypeEnum));
		} else if (Arrays.equals(oid.getOid(), unbufferedDataDeliveryOID)) {
			this.procedureTypeEnum = ProcedureTypeEnum.unbufferedDataDelivery;
			this.oid = new ObjectIdentifier(checkOid(this.procedureTypeEnum));
		} else if (Arrays.equals(oid.getOid(), cyclicReportOID)) {
			this.procedureTypeEnum = ProcedureTypeEnum.cyclicReport;
			this.oid = new ObjectIdentifier(checkOid(this.procedureTypeEnum));
		} else if (Arrays.equals(oid.getOid(), bufferedDataProcessingOID)) {
			this.procedureTypeEnum = ProcedureTypeEnum.bufferedDataProcessing;
			this.oid = new ObjectIdentifier(checkOid(this.procedureTypeEnum));
		} else if (Arrays.equals(oid.getOid(), sequenceControlledDataProcessingOID)) {
			this.procedureTypeEnum = ProcedureTypeEnum.sequenceControlledDataProcessing;
			this.oid = new ObjectIdentifier(checkOid(this.procedureTypeEnum));
		} else {
			this.procedureTypeEnum = ProcedureTypeEnum.extended;
			this.oid = oid;
		}
	}

	/**
	 * Returns the procedure type enum.
	 * 
	 * @return ProcedureTypeEnum
	 */
	public ProcedureTypeEnum getProcedureType() {
		return procedureTypeEnum;
	}

	/**
	 * Returns the object identifier for the procedure.
	 * 
	 * @return ObjectIdentifier
	 */
	public ObjectIdentifier getProcedureTypeOID() {
		return oid;
	}

	public ccsds.csts.common.types.ProcedureType encode() {
		return new ccsds.csts.common.types.ProcedureType(oid.getOid());
	}

	public static ProcedureTypeEnum getProcedureTypeEnum(int[] OID) {
		if (OID == null) {
			return ProcedureTypeEnum.invalid;
		} else if (Arrays.equals(OID, OidValues.associationControl.value)) {
			return ProcedureTypeEnum.associationControl;
		} else if (Arrays.equals(OID, OidValues.unbufferedDataDelivery.value)) {
			return ProcedureTypeEnum.unbufferedDataDelivery;
		} else if (Arrays.equals(OID, OidValues.bufferedDataDelivery.value)) {
			return ProcedureTypeEnum.bufferedDataDelivery;
		} else if (Arrays.equals(OID, OidValues.dataProcessing.value)) {
			return ProcedureTypeEnum.dataProcessing;
		} else if (Arrays.equals(OID, OidValues.informationQuery.value)) {
			return ProcedureTypeEnum.informationQuery;
		} else if (Arrays.equals(OID, OidValues.notification.value)) {
			return ProcedureTypeEnum.notification;
		} else if (Arrays.equals(OID, OidValues.throwEvent.value)) {
			return ProcedureTypeEnum.throwEvent;
		} else if (Arrays.equals(OID, OidValues.cyclicReport.value)) {
			return ProcedureTypeEnum.cyclicReport;
		} else if (Arrays.equals(OID, OidValues.bufferedDataProcessing.value)) {
			return ProcedureTypeEnum.bufferedDataProcessing;
		} else if (Arrays.equals(OID, OidValues.sequenceControlledDataProcessing.value)) {
			return ProcedureTypeEnum.sequenceControlledDataProcessing;
		} else {
			return ProcedureTypeEnum.extended;
		}
	}

	public static ProcedureType decode(ccsds.csts.common.types.ProcedureType procedureType) {
		return new ProcedureType(getProcedureTypeEnum(procedureType.value));
	}

}
