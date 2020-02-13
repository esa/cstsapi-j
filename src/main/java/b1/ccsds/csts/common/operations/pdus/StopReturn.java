/**
 * This class file was automatically generated by jASN1 v1.11.2 (http://www.beanit.com)
 */

package b1.ccsds.csts.common.operations.pdus;

import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.io.Serializable;
import com.beanit.jasn1.ber.*;
import com.beanit.jasn1.ber.types.*;
import com.beanit.jasn1.ber.types.string.*;

import b1.ccsds.csts.common.types.AbstractChoice;
import b1.ccsds.csts.common.types.AdditionalText;
import b1.ccsds.csts.common.types.DataUnitId;
import b1.ccsds.csts.common.types.Embedded;
import b1.ccsds.csts.common.types.EventValue;
import b1.ccsds.csts.common.types.Extended;
import b1.ccsds.csts.common.types.FunctionalResourceInstanceNumber;
import b1.ccsds.csts.common.types.IntUnsigned;
import b1.ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import b1.ccsds.csts.common.types.ListOfParametersEvents;
import b1.ccsds.csts.common.types.Name;
import b1.ccsds.csts.common.types.ProcedureInstanceId;
import b1.ccsds.csts.common.types.PublishedIdentifier;
import b1.ccsds.csts.common.types.QualifiedParameter;
import b1.ccsds.csts.common.types.StandardAcknowledgeHeader;
import b1.ccsds.csts.common.types.StandardInvocationHeader;
import b1.ccsds.csts.common.types.StandardReturnHeader;
import b1.ccsds.csts.common.types.Time;
import b1.ccsds.csts.common.types.TypeAndValueComplexQualified;

public class StopReturn extends StandardReturnHeader {

	private static final long serialVersionUID = 1L;

	public StopReturn() {
	}

	public StopReturn(byte[] code) {
		super(code);
	}

}
