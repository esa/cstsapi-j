package esa.egos.csts.api.productionstatus;

import java.util.Observable;

import org.openmuc.jasn1.ber.types.BerInteger;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.parameters.impl.QualifiedValues;

public class ProductionStatus extends Observable {

	private static final ObjectIdentifier IDENTIFIER = OIDs.svcProductionStatusVersion1;

	private ProductionState state;

	public ProductionStatus() {
		state = ProductionState.CONFIGURED;
	}

	private ProductionStatus(ProductionState state) {
		this.state = state;
	}

	public static ObjectIdentifier getVersionIdentifier() {
		return IDENTIFIER;
	}

	public ProductionState getCurrentState() {
		return state;
	}

	public void transitionTo(ProductionState state) throws ApiException {

		switch (this.state) {
		case CONFIGURED:
			if (state == ProductionState.INTERRUPTED) {
				throw new ApiException("Illegal Production State transition from " + this.state + " to " + state);
			}
			break;
		case HALTED:
			if (state != ProductionState.CONFIGURED) {
				throw new ApiException("Illegal Production State transition from " + this.state + " to " + state);
			}
			break;
		case INTERRUPTED:
			if (state == ProductionState.CONFIGURED) {
				throw new ApiException("Illegal Production State transition from " + this.state + " to " + state);
			}
			break;
		case OPERATIONAL:
			if (state == ProductionState.CONFIGURED) {
				throw new ApiException("Illegal Production State transition from " + this.state + " to " + state);
			}
			break;
		}

		this.state = state;

		setChanged();
		notifyObservers();
	}

	public QualifiedValues toQualifiedValues() {
		QualifiedValues qualifiedValues = new QualifiedValues(ParameterQualifier.VALID);
		ParameterValue name = new ParameterValue(ParameterType.ENUMERATED);
		name.getIntegerParameterValues().add(getCurrentState().getCode());
		qualifiedValues.getParameterValues().add(name);
		return qualifiedValues;
	}

	public ccsds.csts.common.types.ProductionStatus encode() {
		ccsds.csts.common.types.ProductionStatus productionStatus = new ccsds.csts.common.types.ProductionStatus();
		productionStatus.getBerInteger().add(new BerInteger(state.getCode()));
		return productionStatus;
	}

	public static ProductionStatus decode(ccsds.csts.common.types.ProductionStatus productionStatus) {
		ProductionState state = ProductionState
				.getProductionStateByCode(productionStatus.getBerInteger().get(0).longValue());
		ProductionStatus status = new ProductionStatus(state);
		return status;
	}

}