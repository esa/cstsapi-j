/**
 * This class file was automatically generated by jASN1 v1.8.1 (http://www.openmuc.org)
 */

package ccsds.csts.association.control.types;

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
import ccsds.csts.common.types.AuthorityIdentifier;
import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;
import ccsds.csts.common.types.IntPos;
import ccsds.csts.common.types.PortId;
import ccsds.csts.common.types.PublishedIdentifier;
import ccsds.csts.common.types.StandardInvocationHeader;
import ccsds.csts.common.types.StandardReturnHeader;
import ccsds.csts.pdus.CstsFrameworkPdu;
import ccsds.csts.service.instance.id.ServiceInstanceIdentifier;

public class VersionNumber extends IntPos {

	private static final long serialVersionUID = 1L;

	public VersionNumber() {
	}

	public VersionNumber(byte[] code) {
		super(code);
	}

	public VersionNumber(BigInteger value) {
		super(value);
	}

	public VersionNumber(long value) {
		super(value);
	}

}
