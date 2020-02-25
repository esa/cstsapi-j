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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OidConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class OidConfig {

	@XmlElement(name = "oid", required = true)
	private ArrayList<Oid> oidLabels;


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
	 */
	public static void save(String pathFileName, Object obj) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(pathFileName);
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(obj, fos);

			System.out.println("created file: " + pathFileName);
		} catch (Exception e) {
			e.printStackTrace();
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
	 */
	public void save(String pathFileName) {
		save(pathFileName, this);
	}
}
