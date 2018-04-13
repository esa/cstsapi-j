/**
 * This class file was automatically generated by jASN1 v1.8.1 (http://www.openmuc.org)
 */

package ccsds.csts.cyclic.report.pdus;

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

import ccsds.csts.common.types.AdditionalText;
import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;
import ccsds.csts.common.types.IntPos;
import ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import ccsds.csts.common.types.ListOfParametersEvents;
import ccsds.csts.common.types.QualifiedParameter;

public final class OidValues {
	public static final BerObjectIdentifier crStartDiagExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 2, 1, 1, 2, 2});
	public static final BerObjectIdentifier crStartInvocExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 2, 1, 1, 2, 1});
	public static final BerObjectIdentifier crTransferDataInvocDataRef = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 3, 2, 1, 1, 2, 3});
}
