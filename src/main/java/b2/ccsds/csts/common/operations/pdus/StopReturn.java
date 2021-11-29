/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b2.ccsds.csts.common.operations.pdus;

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

import b2.ccsds.csts.common.types.AbstractChoice;
import b2.ccsds.csts.common.types.AdditionalText;
import b2.ccsds.csts.common.types.DataUnitId;
import b2.ccsds.csts.common.types.Embedded;
import b2.ccsds.csts.common.types.EventValue;
import b2.ccsds.csts.common.types.Extended;
import b2.ccsds.csts.common.types.FunctionalResourceInstanceNumber;
import b2.ccsds.csts.common.types.FunctionalResourceName;
import b2.ccsds.csts.common.types.IntUnsigned;
import b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import b2.ccsds.csts.common.types.ListOfParametersEvents;
import b2.ccsds.csts.common.types.Name;
import b2.ccsds.csts.common.types.ProcedureName;
import b2.ccsds.csts.common.types.PublishedIdentifier;
import b2.ccsds.csts.common.types.QualifiedParameter;
import b2.ccsds.csts.common.types.StandardAcknowledgeHeader;
import b2.ccsds.csts.common.types.StandardInvocationHeader;
import b2.ccsds.csts.common.types.StandardReturnHeader;
import b2.ccsds.csts.common.types.Time;
import b2.ccsds.csts.common.types.TypeAndValue;

public class StopReturn extends StandardReturnHeader {

	private static final long serialVersionUID = 1L;

	public StopReturn() {
	}

	public StopReturn(byte[] code) {
		super(code);
	}

}
