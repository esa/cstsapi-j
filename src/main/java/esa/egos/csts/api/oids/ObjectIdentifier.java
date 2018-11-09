package esa.egos.csts.api.oids;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * This class represents the CCSDS ObjectIdentifier type.
 */
public class ObjectIdentifier {

	private final int[] oidArray;

	private ObjectIdentifier(int[] oidArray) {
		this.oidArray = oidArray.clone();
	}

	/**
	 * Creates a new Object Identifier from specified Integer values.
	 * 
	 * @param oidArray
	 *            the ordered Integer values representing the Object Identifier
	 * @return an Object Identifier of the specified Integer values
	 */
	public static ObjectIdentifier of(int... oidArray) {
		return new ObjectIdentifier(oidArray);
	}

	/**
	 * Creates a new Object Identifier from another Object Identifier and specified
	 * Integer values.
	 * 
	 * @param oid
	 *            the prepender Object Identifier
	 * @param oidArray
	 *            the ordered Integer values representing the rest of the Object
	 *            Identifier
	 * @return an Object Identifier of the specified Object Identifier the specified
	 *         Integer values
	 */
	public static ObjectIdentifier of(ObjectIdentifier oid, int... oidArray) {
		int[] concatOidArray = IntStream.concat(Arrays.stream(oid.oidArray), Arrays.stream(oidArray)).toArray();
		return new ObjectIdentifier(concatOidArray);
	}

	/**
	 * Returns a cloned array of the internal Object Identifier values. The cloning
	 * guarantees, that the Object Identifier is not changed externally, since this
	 * class is considered immutable.
	 * 
	 * @return the cloner Object Identifier array
	 */
	public int[] toArray() {
		return oidArray.clone();
	}

	/**
	 * Indicates whether the internal Object Identifier array is equal to the
	 * specified array.
	 * 
	 * @param identifier
	 *            the specified array
	 * @return true if the arrays are equal, false otherwise
	 */
	public boolean equals(int[] identifier) {
		return Arrays.equals(oidArray, identifier);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(oidArray);
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
		ObjectIdentifier other = (ObjectIdentifier) obj;
		if (!Arrays.equals(oidArray, other.oidArray))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return Arrays.toString(oidArray);
	}

}
