package esa.egos.proxy.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Keep an OID bit number and related OID label
 * e.g. {"[1, 3, 112, 4, 4, 2, 1, 1000, 1, 3, 1, 1]", "antActualAzimuth"}
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Oid {
	
	@XmlAttribute(name = "array", required = true)
	private String array;
	@XmlAttribute(name = "label", required = true)
	private String label;

	public Oid() {
		this.array = "";
		this.label = "";
	}

	public Oid(String oid, String label) {
		this.array = oid;
		this.label = label;
	}

	public Oid(int[] oid, String label) {
		this.array = Arrays.toString(oid);
		this.label = label;
	}

	public String getArray() {
		return this.array;
	}

	public int[] getAsArray() {
		List<Integer> ints = new ArrayList<Integer>();
		String[] bits = this.array.replaceAll("[\\[\\]]", "").split(",");
		for (String bit : bits) {
			ints.add(Integer.parseInt(bit.trim()));
		}
		return ints.stream().mapToInt(Integer::intValue).toArray();
	}

	public void setArray(String array) {
		this.array = array;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "OidLabel [oid=" + this.array + ", label=" + this.label + "]";
	}
}
