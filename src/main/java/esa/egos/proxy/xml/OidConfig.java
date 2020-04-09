package esa.egos.proxy.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OidConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class OidConfig {

	@XmlAttribute(name = "number_oids", required = false)
	protected Integer numOids;

	@XmlAttribute(name = "number_fr_oids", required = false)
	protected Integer numFrOids;

	@XmlAttribute(name = "number_fr_parameter_oids", required = false)
	protected Integer numFrParameterOids;

	@XmlAttribute(name = "number_fr_event_oids", required = false)
	protected Integer numFrEventOids;

	@XmlAttribute(name = "number_fr_directive_oids", required = false)
	protected Integer numFrDirectiveOids;

	@XmlElement(name = "oid", required = true)
	private ArrayList<Oid> oidLabels;


	public Integer getNumOids() {
		return numOids;
	}

	public void setNumOids(Integer numOids) {
		this.numOids = numOids;
	}

	public Integer getNumFrOids() {
		return numFrOids;
	}

	public void setNumFrOids(Integer numFrOids) {
		this.numFrOids = numFrOids;
	}

	public Integer getNumFrParameterOids() {
		return numFrParameterOids;
	}

	public void setNumFrParameterOids(Integer numFrParameterOids) {
		this.numFrParameterOids = numFrParameterOids;
	}

	public Integer getNumFrEventOids() {
		return numFrEventOids;
	}

	public void setNumFrEventOids(Integer numFrEventOids) {
		this.numFrEventOids = numFrEventOids;
	}

	public Integer getNumFrDirectiveOids() {
		return numFrDirectiveOids;
	}

	public void setNumFrDirectiveOids(Integer numFrDirectiveOids) {
		this.numFrDirectiveOids = numFrDirectiveOids;
	}

	public ArrayList<Oid> getOidLabelList() {
		return this.oidLabels;
	}

	public void setOidLabelList(ArrayList<Oid> oidLabelList) {
		this.oidLabels = oidLabelList;
	}

    /**
     * Load the OID configuration from an XML file 
     * 
     * @param stream
     * @return the OID configuration
     */
	public static OidConfig load(String pathFileName) {
		InputStream is = null;
		try {
			is = new FileInputStream(new File(pathFileName));
			JAXBContext context = JAXBContext.newInstance(OidConfig.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (OidConfig) unmarshaller.unmarshal(is);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Save the OID configuration to a file
	 * 
	 * @param pathFileName
	 *            The path file name
	 * 
	 * @param obj
	 *            An instance of this class
	 * @throws Exception 
	 */
	public static void save(String pathFileName, Object obj) throws Exception {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(pathFileName);
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(obj, fos);

			System.out.println("created file: " + pathFileName);
		} catch (Exception e) {
			throw new Exception("Failed to create file " + pathFileName, e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Save the OID configuration to a file
	 * 
	 * @param pathFileName
	 *            The path file name
	 * @throws Exception 
	 */
	public void save(String pathFileName) throws Exception {
		save(pathFileName, this);
	}
}
