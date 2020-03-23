package esa.egos.csts.api.oids;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * This class represents the CCSDS ObjectIdentifier type.
 */
public class ObjectIdentifier {

	private static final String DOT_EXPR = "\\.";

	/** The integer array representation of the CCSDS ObjectIdentifier type */
	private final int[] oidArray;

	/** The string representation of the CCSDS ObjectIdentifier type */
	private String oidString = null;

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
	 * Creates an Object Identifier from the given string.
	 * The string is expected to be numbers separated by a dot, e.g. 1.2.3.4 
	 *  
	 * @param str
	 * @return The created object identifier
	 * @throws NumberFormatException if elements of the string cannot be converted to an integer number
	 */
	public static ObjectIdentifier of(String str) throws NumberFormatException {
		return ObjectIdentifier.of(Stream.of(str.split(DOT_EXPR))
	      .map(elem -> new Integer(elem)).mapToInt(i -> i).toArray());
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

	/**
	 * Convert the integer array representation to the string representation with labels
	 */
	@Override
	public synchronized String toString() {
		if (this.oidString == null) {
			try {
				this.oidString = OidTree.getInstance().print(this.oidArray);
			} catch (Exception e) {
				this.oidString = "";
				System.out.println("UNKNWON OID: " + Arrays.toString(this.oidArray));
			}
		}
		return ((this.oidString.isEmpty()) ? Arrays.toString(this.oidArray) : this.oidString);
	}

}
