/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b1.ccsds.csts.data.processing.pdus;

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

import b1.ccsds.csts.common.types.DataUnitId;
import b1.ccsds.csts.common.types.Embedded;
import b1.ccsds.csts.common.types.Extended;
import b1.ccsds.csts.common.types.ProductionStatus;
import b1.ccsds.csts.common.types.Time;
import b1.ccsds.csts.pdus.CstsFrameworkPdu;

public final class OidValues {
	public static final BerObjectIdentifier dpNotifyInvocExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 4, 2, 2});
	public static final BerObjectIdentifier dpProcDataInvocExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 4, 2, 1});
}