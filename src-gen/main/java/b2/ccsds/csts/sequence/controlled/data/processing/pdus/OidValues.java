/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b2.ccsds.csts.sequence.controlled.data.processing.pdus;

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

import b2.ccsds.csts.common.types.ConditionalTime;
import b2.ccsds.csts.common.types.DataUnitId;
import b2.ccsds.csts.common.types.Embedded;
import b2.ccsds.csts.common.types.Extended;
import b2.ccsds.csts.pdus.CstsFrameworkPdu;

public final class OidValues {
	public static final BerObjectIdentifier scdpNotifyProcStatusExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 4, 1, 2, 2, 6});
	public static final BerObjectIdentifier scdpProcDataDiagExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 4, 1, 2, 2, 4});
	public static final BerObjectIdentifier scdpProcDataInvocExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 4, 1, 2, 2, 2});
	public static final BerObjectIdentifier scdpProcDataNegReturnExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 4, 1, 2, 2, 5});
	public static final BerObjectIdentifier scdpProcDataPosReturnExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 4, 1, 2, 2, 3});
	public static final BerObjectIdentifier scdpStartInvocExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 4, 1, 2, 2, 1});
}
