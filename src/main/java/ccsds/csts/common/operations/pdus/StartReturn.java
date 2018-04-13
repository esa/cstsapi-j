/**
 * This class file was automatically generated by jASN1 v1.8.1 (http://www.openmuc.org)
 */

package ccsds.csts.common.operations.pdus;

import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.io.Serializable;
import org.openmuc.jasn1.ber.*;
import org.openmuc.jasn1.ber.types.*;
import org.openmuc.jasn1.ber.types.string.*;

import ccsds.csts.common.types.AbstractChoice;
import ccsds.csts.common.types.AdditionalText;
import ccsds.csts.common.types.DataUnitId;
import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.EventValue;
import ccsds.csts.common.types.Extended;
import ccsds.csts.common.types.FunctionalResourceInstanceNumber;
import ccsds.csts.common.types.IntUnsigned;
import ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import ccsds.csts.common.types.ListOfParametersEvents;
import ccsds.csts.common.types.Name;
import ccsds.csts.common.types.ProcedureInstanceId;
import ccsds.csts.common.types.PublishedIdentifier;
import ccsds.csts.common.types.QualifiedParameter;
import ccsds.csts.common.types.StandardAcknowledgeHeader;
import ccsds.csts.common.types.StandardInvocationHeader;
import ccsds.csts.common.types.StandardReturnHeader;
import ccsds.csts.common.types.Time;
import ccsds.csts.common.types.TypeAndValueComplexQualified;

public class StartReturn extends StandardReturnHeader {

	private static final long serialVersionUID = 1L;

	public StartReturn() {
	}

	public StartReturn(byte[] code) {
		super(code);
	}

}
