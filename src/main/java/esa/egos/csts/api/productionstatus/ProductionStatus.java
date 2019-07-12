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

/**
 * This class represents the Production Status and its underlying state machine.
 */
public class ProductionStatus extends Observable {

	private static final ObjectIdentifier IDENTIFIER = OIDs.svcProductionStatusVersion1;

	private ProductionState state;

	/**
	 * Instantiates a new Production Status and sets its state to Configured.
	 */
	public ProductionStatus() {
		state = ProductionState.CONFIGURED;
	}

	private ProductionStatus(ProductionState state) {
		this.state = state;
	}

	/**
	 * Returns the version identifier of this Production Status Implementation.
	 * 
	 * @return the version identifier
	 */
	public static ObjectIdentifier getVersionIdentifier() {
		return IDENTIFIER;
	}

	/**
	 * Returns the current state.
	 * 
	 * @return the current state
	 */
	public ProductionState getCurrentState() {
		return state;
	}

	/**
	 * Transitions from the current to the specified state.
	 * 
	 * @param state the specified state to transition to
	 * @throws ApiException if the transition is illegal
	 */
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

	/**
	 * Returns this Production Status as Qualified Values for usage in a Qualified
	 * Parameter or Event Value.
	 * 
	 * @return this Production Status as Qualified Values
	 */
	public QualifiedValues toQualifiedValues() {
		QualifiedValues qualifiedValues = new QualifiedValues(ParameterQualifier.VALID);
		ParameterValue name = new ParameterValue(ParameterType.ENUMERATED);
		name.getIntegerParameterValues().add(getCurrentState().getCode());
		qualifiedValues.getParameterValues().add(name);
		return qualifiedValues;
	}

	/**
	 * Encodes this Production Status into a CCSDS ProductionStatus.
	 * 
	 * @return the CCSDS ProductionStatus representing this object
	 */
	public ccsds.csts.common.types.ProductionStatus encode() {
		ccsds.csts.common.types.ProductionStatus productionStatus = new ccsds.csts.common.types.ProductionStatus();
		productionStatus.getBerInteger().add(new BerInteger(state.getCode()));
		return productionStatus;
	}

	/**
	 * Decodes a specified CCSDS ProductionStatus into this object
	 * 
	 * @param productionStatus the specified CCSDS ProductionStatus
	 * @return a new Production Status decoded from the specified CCSDS
	 *         ProductionStatus
	 */
	public static ProductionStatus decode(ccsds.csts.common.types.ProductionStatus productionStatus) {
		ProductionState state = ProductionState.getProductionStateByCode(productionStatus.getBerInteger().get(0).longValue());
		ProductionStatus status = new ProductionStatus(state);
		return status;
	}

}