package esa.egos.csts.test.mdslite.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.parameters.impl.QualifiedValues;
import esa.egos.csts.api.types.Name;
import frm.csts.functional.resource.types.AntActualAzimuth;
import frm.csts.functional.resource.types.OidValues;

public class DemoAntAzimuthParameter {
	private static final ObjectIdentifier antAzimuthOid = ObjectIdentifier.of(OidValues.antActualAzimuthParamOid.value);
	
	/**
	 * Demonstrates encoding of an FR parameter into a QualifiedValue
	 * @param value
	 * @return
	 */
	public static QualifiedParameter encodeAzimut(long value) {
		ObjectIdentifier antFrOid = ObjectIdentifier.of(OidValues.antennaFrOid.value);
		FunctionalResourceType antFrType = FunctionalResourceType.of(antFrOid);
		FunctionalResourceName antFrInstance = FunctionalResourceName.of(antFrType, 0 /*FR instance number*/);
		Name antennaAzimuthParamName = Name.of(antAzimuthOid, antFrInstance);
		
		QualifiedParameter qualifiedParameter = new QualifiedParameter(antennaAzimuthParamName); 
		QualifiedValues qv = new QualifiedValues(ParameterQualifier.VALID);
		
		AntActualAzimuth azimuth = new AntActualAzimuth(value);
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(12, true)) {
			azimuth.encode(os);
			azimuth.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		EmbeddedData valueExtension = EmbeddedData.of(antAzimuthOid, azimuth.code);
		System.out.println("Encoded AntActualAzimuth embedded: " + valueExtension);
		
		ParameterValue pv = new ParameterValue(valueExtension);
		qv.getParameterValues().add(pv);			
		qualifiedParameter.getQualifiedValues().add(qv);
	
		return qualifiedParameter;
	}
	/**
	 * Demonstrates the decoding of an FR parameter
	 * @param embeddedData	The embedded data carrying the parameter
	 * @return
	 */
	public static long decodeAzimut(EmbeddedData embeddedData) {
		long azimutValue = -1;
		ObjectIdentifier antAzimuthOid = ObjectIdentifier.of(OidValues.antActualAzimuthParamOid.value);
		if(embeddedData.getOid().equals(antAzimuthOid) == true) {
			try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
				AntActualAzimuth az = new AntActualAzimuth();
				az.decode(is);
				azimutValue = az.longValue();
				System.out.println("Received new actual azimuth: " + azimutValue + " OID " + antAzimuthOid);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		} else {
			System.out.println("Unexpected parameter: " + embeddedData.getOid());
		}
		
		return azimutValue;
		
	}
}
