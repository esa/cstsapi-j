package esa.egos.csts.api.parameters.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import esa.egos.csts.api.enumerations.DurationType;
import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.types.Duration;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.types.Time;

public class QualifiedParameterTest {

	@Test
	public void testEqualsObject() {

		// reference value
		QualifiedParameter qp = new QualifiedParameter(
				Name.of(ObjectIdentifier.of(1,3,112,4,4,1,2), (FunctionalResourceName)null));
		
		List<QualifiedValues> qpList = qp.getQualifiedValues();
		QualifiedValues qv = new QualifiedValues(ParameterQualifier.VALID);
		qpList.add(qv);
		
		List<ParameterValue> pvList = qv.getParameterValues();
		ParameterValue pv;
		pv = new ParameterValue(ParameterType.BOOLEAN);
		pv.getBoolParameterValues().add(Boolean.TRUE);
		pv.getBoolParameterValues().add(Boolean.FALSE);
		pvList.add(pv);
		pv = new ParameterValue(ParameterType.CHARACTER_STRING);
		pv.getStringParameterValues().add("string1");
		pv.getStringParameterValues().add("string2");
		pvList.add(pv);
		pv = new ParameterValue(ParameterType.DURATION);
		pv.getDurationParameterValues().add(Duration.of(DurationType.SECONDS, java.time.Duration.ZERO));
		pv.getDurationParameterValues().add(Duration.of(DurationType.MILLISECONDS, java.time.Duration.ZERO));
		pvList.add(pv);
		pv = new ParameterValue(ParameterType.ENUMERATED);
		pv.getIntegerParameterValues().add(Long.valueOf(1));
		pv.getIntegerParameterValues().add(Long.valueOf(10));
		pvList.add(pv);
		pv = new ParameterValue(ParameterType.INTEGER);
		pv.getIntegerParameterValues().add(Long.valueOf(2));
		pvList.add(pv);
		pv = new ParameterValue(ParameterType.OBJECT_IDENTIFIER);
		pv.getOIDparameterValues().add(ObjectIdentifier.of(1,3,112,4,4,1,2));
		pvList.add(pv);
		pv = new ParameterValue(ParameterType.OCTET_STRING);
		pv.getOctetStringParameterValues().add(new byte[] {1,2,3});
		pvList.add(pv);
		pv = new ParameterValue(ParameterType.POSITIVE_INTEGER);
		pv.getIntegerParameterValues().add(Long.valueOf(3));
		pv.getIntegerParameterValues().add(Long.valueOf(5));
		pvList.add(pv);
		pv = new ParameterValue(ParameterType.PUBLISHED_IDENTIFIER);
		pv.getOIDparameterValues().add(ObjectIdentifier.of(1,3,112,4,4,1,3));
		pvList.add(pv);
		pv = new ParameterValue(ParameterType.REAL);
		pv.getRealParameterValues().add(Double.valueOf(4.0));
		pvList.add(pv);
		pv = new ParameterValue(ParameterType.TIME);
		pv.getTimeParameterValues().add(Time.of(new byte[] {0,1,2,3,4,5,6,7}));
		pvList.add(pv);
		pv = new ParameterValue(ParameterType.UNSIGNED_INTEGER);
		pv.getIntegerParameterValues().add(Long.valueOf(5));
		pvList.add(pv);
		pv = new ParameterValue(ParameterType.EXTENDED);
		pvList.add(pv);
		System.out.println(qp.toString());
		
		int hash = qp.hashCode();
		
		// equal value
		QualifiedParameter qp1 = new QualifiedParameter(
				Name.of(ObjectIdentifier.of(1,3,112,4,4,1,2), (FunctionalResourceName)null));
		
		List<QualifiedValues> qpList1 = qp1.getQualifiedValues();
		QualifiedValues qv1 = new QualifiedValues(ParameterQualifier.VALID);
		qpList1.add(qv1);
		
		List<ParameterValue> pvList1 = qv1.getParameterValues();
		ParameterValue pv1;
		pv1 = new ParameterValue(ParameterType.BOOLEAN);
		pv1.getBoolParameterValues().add(Boolean.TRUE);
		pv1.getBoolParameterValues().add(Boolean.FALSE);
		pvList1.add(pv1);
		pv1 = new ParameterValue(ParameterType.CHARACTER_STRING);
		pv1.getStringParameterValues().add("string1");
		pv1.getStringParameterValues().add("string2");
		pvList1.add(pv1);
		pv1 = new ParameterValue(ParameterType.DURATION);
		pv1.getDurationParameterValues().add(Duration.of(DurationType.SECONDS, java.time.Duration.ZERO));
		pv1.getDurationParameterValues().add(Duration.of(DurationType.MILLISECONDS, java.time.Duration.ZERO));
		pvList1.add(pv1);
		pv1 = new ParameterValue(ParameterType.ENUMERATED);
		pv1.getIntegerParameterValues().add(Long.valueOf(1));
		pv1.getIntegerParameterValues().add(Long.valueOf(10));
		pvList1.add(pv1);
		pv1 = new ParameterValue(ParameterType.INTEGER);
		pv1.getIntegerParameterValues().add(Long.valueOf(2));
		pvList1.add(pv1);
		pv1 = new ParameterValue(ParameterType.OBJECT_IDENTIFIER);
		pv1.getOIDparameterValues().add(ObjectIdentifier.of(1,3,112,4,4,1,2));
		pvList1.add(pv1);
		pv1 = new ParameterValue(ParameterType.OCTET_STRING);
		pv1.getOctetStringParameterValues().add(new byte[] {1,2,3});
		pvList1.add(pv1);
		pv1 = new ParameterValue(ParameterType.POSITIVE_INTEGER);
		pv1.getIntegerParameterValues().add(Long.valueOf(3));
		pv1.getIntegerParameterValues().add(Long.valueOf(5));
		pvList1.add(pv1);
		pv1 = new ParameterValue(ParameterType.PUBLISHED_IDENTIFIER);
		pv1.getOIDparameterValues().add(ObjectIdentifier.of(1,3,112,4,4,1,3));
		pvList1.add(pv1);
		pv1 = new ParameterValue(ParameterType.REAL);
		pv1.getRealParameterValues().add(Double.valueOf(4.0));
		pvList1.add(pv1);
		pv1 = new ParameterValue(ParameterType.TIME);
		pv1.getTimeParameterValues().add(Time.of(new byte[] {0,1,2,3,4,5,6,7}));
		pvList1.add(pv1);
		pv1 = new ParameterValue(ParameterType.UNSIGNED_INTEGER);
		pv1.getIntegerParameterValues().add(Long.valueOf(5));
		pvList1.add(pv1);
		pv1 = new ParameterValue(ParameterType.EXTENDED);
		pvList1.add(pv1);
		
		int hash1 = qp1.hashCode();
		
		Assert.assertTrue(qp.equals(qp1));
		Assert.assertTrue(hash == hash1);

		// not equal value
		QualifiedParameter qp2 = new QualifiedParameter(
				Name.of(ObjectIdentifier.of(1,3,112,4,4,1,2), (FunctionalResourceName)null));
		
		List<QualifiedValues> qpList2 = qp2.getQualifiedValues();
		QualifiedValues qv2 = new QualifiedValues(ParameterQualifier.UNAVAILABLE);
		qpList2.add(qv2);
		
		int hash2 = qp2.hashCode();
		
		Assert.assertFalse(qp.equals(qp2));
		Assert.assertFalse(hash == hash2);
		
		// EmbeddedData - equal
		QualifiedParameter qp3 = new QualifiedParameter(
				Name.of(ObjectIdentifier.of(1,3,112,4,4,1,3), (FunctionalResourceName)null));
		List<QualifiedValues> qpList3 = qp3.getQualifiedValues();
		QualifiedValues qv3 = new QualifiedValues(ParameterQualifier.UNDEFINED);
		qpList3.add(qv3);
		qv3.getParameterValues().add(new ParameterValue(ParameterType.EXTENDED));
		int hash3 = qp3.hashCode();
		
		QualifiedParameter qp4 = new QualifiedParameter(
				Name.of(ObjectIdentifier.of(1,3,112,4,4,1,3), (FunctionalResourceName)null));
		List<QualifiedValues> qpList4 = qp4.getQualifiedValues();
		QualifiedValues qv4 = new QualifiedValues(ParameterQualifier.UNDEFINED);
		qpList4.add(qv4);
		qv4.getParameterValues().add(new ParameterValue(ParameterType.EXTENDED));
		int hash4 = qp4.hashCode();
		
		Assert.assertTrue(qp3.equals(qp4));
		Assert.assertTrue(hash3 == hash4);
		
		// EmbeddedData - not equal
		QualifiedParameter qp5 = new QualifiedParameter(
				Name.of(ObjectIdentifier.of(1,3,112,4,4,1,4), (FunctionalResourceName)null));
		List<QualifiedValues> qpList5 = qp5.getQualifiedValues();
		QualifiedValues qv5 = new QualifiedValues(ParameterQualifier.UNDEFINED);
		qpList5.add(qv5);
		qv5.getParameterValues().add(new ParameterValue(EmbeddedData.of(ObjectIdentifier.of(1,2,3), (byte[])null)));
		int hash5 = qp5.hashCode();
		
		QualifiedParameter qp6 = new QualifiedParameter(
				Name.of(ObjectIdentifier.of(1,3,112,4,4,1,4), (FunctionalResourceName)null));
		List<QualifiedValues> qpList6 = qp6.getQualifiedValues();
		QualifiedValues qv6 = new QualifiedValues(ParameterQualifier.UNDEFINED);
		qpList6.add(qv6);
		qv6.getParameterValues().add(new ParameterValue(EmbeddedData.of(ObjectIdentifier.of(1,2,3), new byte[] { 0 })));
		int hash6 = qp6.hashCode();
		
		Assert.assertFalse(qp5.equals(qp3));
		Assert.assertFalse(hash5 == hash3);

		Assert.assertFalse(qp5.equals(qp6));
		Assert.assertFalse(hash5 == hash6);

	}

}
