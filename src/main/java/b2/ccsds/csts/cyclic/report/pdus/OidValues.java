/**
 * This class file was automatically generated by jASN1 v1.11.3-SNAPSHOT (http://www.beanit.com)
 */

package b2.ccsds.csts.cyclic.report.pdus;

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

import b2.ccsds.csts.common.types.AdditionalText;
import b2.ccsds.csts.common.types.Embedded;
import b2.ccsds.csts.common.types.Extended;
import b2.ccsds.csts.common.types.IntPos;
import b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import b2.ccsds.csts.common.types.ListOfParametersEvents;
import b2.ccsds.csts.common.types.QualifiedParameter;
import b2.ccsds.csts.unbuffered.data.delivery.pdus.UnbufferedDataDeliveryPdu;

public final class OidValues {
	public static final BerObjectIdentifier crStartDiagExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 2, 1, 1, 2, 2});
	public static final BerObjectIdentifier crStartInvocExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 2, 1, 1, 2, 1});
	public static final BerObjectIdentifier crTransferDataInvocDataRef = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 2, 1, 1, 2, 3});
}
