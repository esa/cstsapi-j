package esa.egos.csts.api.productionstatus;

import java.util.Arrays;

import esa.egos.csts.api.exceptions.CodeNotFoundException;

/**
 * The underlying state enumeration with its corresponding codes of a
 * {@link ProductionStatus} class.
 */
public enum ProductionState {

	CONFIGURED(0),
	OPERATIONAL(1),
	INTERRUPTED(2),
	HALTED(3);

	private long code;

	private ProductionState(long code) {
		this.code = code;
	}

	/**
	 * Returns the code of the current state.
	 * 
	 * @return the code of the current state
	 */
	public long getCode() {
		return code;
	}

	/**
	 * Returns a new state of the specified code.
	 * 
	 * @param code the specified code
	 * @return a new state of the specified code
	 */
	public static ProductionState getProductionStateByCode(long code) {
		return Arrays.stream(values())
				.filter(d -> d.code == code)
				.findFirst()
				.orElseThrow(CodeNotFoundException::new);
	}

}