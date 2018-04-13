package esa.egos.csts.api.main;

import java.util.Arrays;

import org.openmuc.jasn1.ber.types.BerObjectIdentifier;

public class ObjectIdentifier {

	private final int[] oid;

    /**
     * The attribute separator.
     */
    private static char attrSep = ',';
	
	/**
	 * @param oid
	 */
	public ObjectIdentifier(int... oid) {
		super();
		this.oid = oid;
	}

	public int[] getOid() {
		return oid.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(oid);
		return result;
	}

	@Override
	public String toString() {
		return Arrays.toString(oid);
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
		if (!Arrays.equals(oid, other.oid))
			return false;
		return true;
	}

	public boolean equals(BerObjectIdentifier identifier) {
		return Arrays.equals(oid, identifier.value);
	}
	
	public static ObjectIdentifier parseOid(String nameAttr) {
		
		int numSeparators = 0 ;
		
        for (int i = 0; i < nameAttr.length(); i++)
        {
            if (nameAttr.charAt(i) == attrSep)
            {
                numSeparators++;
            }
        }
		
		int[] list = new int[numSeparators + 1];
		
        // separate the name-attribute pairs
        for (int j = 0; j <= numSeparators; j++)
        {
            int p = nameAttr.indexOf(attrSep);

            String nameStr = "";

            if (p == -1)
            {
                nameStr = nameAttr;
            }
            else
            {
                nameStr = nameAttr.substring(0, p);
            }

            list[j] = Integer.parseInt(nameStr);

            if (p != -1)
            {
            	nameAttr = nameAttr.substring(p + 1, nameAttr.length());
            }
            else
            {
            	nameAttr = "";
            }
        }
		
		return new ObjectIdentifier(list);
	}
	
}
