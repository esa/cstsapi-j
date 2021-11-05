/**
 * This class file was automatically generated by jASN1 v1.11.3-SNAPSHOT (http://www.beanit.com)
 */

package b2.ccsds.csts.notification.pdus;

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

import b2.ccsds.csts.common.types.Embedded;
import b2.ccsds.csts.common.types.Extended;
import b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import b2.ccsds.csts.common.types.ListOfParametersEvents;
import b2.ccsds.csts.pdus.CstsFrameworkPdu;

public final class OidValues {
	public static final BerObjectIdentifier nStartDiagExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 6, 2, 2});
	public static final BerObjectIdentifier nStartInvocExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 6, 2, 1});
}
