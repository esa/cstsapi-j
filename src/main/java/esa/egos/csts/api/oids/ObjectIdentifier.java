package esa.egos.csts.api.oids;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ObjectIdentifier {

	private final int[] oidArray;

	private ObjectIdentifier(int[] oidArray) {
		this.oidArray = oidArray.clone();
	}

	public static ObjectIdentifier of(int... oidArray) {
		return new ObjectIdentifier(oidArray);
	}

	public static ObjectIdentifier of(ObjectIdentifier oid, int... oidArray) {
		int[] concatOidArray = IntStream.concat(Arrays.stream(oid.oidArray), Arrays.stream(oidArray)).toArray();
		return new ObjectIdentifier(concatOidArray);
	}

	public int[] toArray() {
		return oidArray.clone();
	}

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
