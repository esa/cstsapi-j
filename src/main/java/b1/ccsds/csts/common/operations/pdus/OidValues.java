/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
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

public final class OidValues {
	public static final BerObjectIdentifier execDirAckDiagExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 2, 11, 1});
	public static final BerObjectIdentifier execDirNegReturnDiagnosticExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 2, 12, 1});
	public static final BerObjectIdentifier getDiagnosticExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 2, 14, 2});
	public static final BerObjectIdentifier getPosReturnExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 2, 14, 1});
	public static final BerObjectIdentifier startDiagnosticExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 2, 7, 1});
}
