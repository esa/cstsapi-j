/**
 * This class file was automatically generated by jASN1 v1.8.1 (http://www.openmuc.org)
 */

package ccsds.csts.data.processing.pdus;

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

import ccsds.csts.common.types.DataUnitId;
import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;
import ccsds.csts.common.types.ProductionStatus;
import ccsds.csts.common.types.Time;
import ccsds.csts.pdus.CstsFrameworkPdu;

public final class OidValues {
	public static final BerObjectIdentifier dpNotifyInvocExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 4, 2, 2});
	public static final BerObjectIdentifier dpProcDataInvocExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 4, 2, 1});
}
