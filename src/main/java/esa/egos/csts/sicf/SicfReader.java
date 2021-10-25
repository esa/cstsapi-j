package esa.egos.csts.sicf;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import esa.egos.csts.sicf.model.MDSicf;

public class SicfReader {
	public static MDSicf createSicf(String sicfPath) {
		File file = new File(sicfPath);
		MDSicf sicf = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(MDSicf.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			sicf = (MDSicf) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
		}
		return sicf;
	}
}
