/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b2.ccsds.csts.association.control.types;

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
import b2.ccsds.csts.common.types.AuthorityIdentifier;
import b2.ccsds.csts.common.types.Embedded;
import b2.ccsds.csts.common.types.Extended;
import b2.ccsds.csts.common.types.IntPos;
import b2.ccsds.csts.common.types.PortId;
import b2.ccsds.csts.common.types.PublishedIdentifier;
import b2.ccsds.csts.common.types.StandardInvocationHeader;
import b2.ccsds.csts.common.types.StandardReturnHeader;
import b2.ccsds.csts.pdus.CstsFrameworkPdu;
import b2.ccsds.csts.service.instance.id.ServiceInstanceIdentifier;

public class PeerAbortDiagnostic extends BerOctetString {

	private static final long serialVersionUID = 1L;

	public PeerAbortDiagnostic() {
	}

	public PeerAbortDiagnostic(byte[] value) {
		super(value);
	}

}
