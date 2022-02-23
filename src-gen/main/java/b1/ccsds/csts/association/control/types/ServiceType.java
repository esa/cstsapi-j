/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b1.ccsds.csts.association.control.types;

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

import b1.ccsds.csts.common.types.AdditionalText;
import b1.ccsds.csts.common.types.AuthorityIdentifier;
import b1.ccsds.csts.common.types.Embedded;
import b1.ccsds.csts.common.types.Extended;
import b1.ccsds.csts.common.types.IntPos;
import b1.ccsds.csts.common.types.PortId;
import b1.ccsds.csts.common.types.PublishedIdentifier;
import b1.ccsds.csts.common.types.StandardInvocationHeader;
import b1.ccsds.csts.common.types.StandardReturnHeader;
import b1.ccsds.csts.pdus.CstsFrameworkPdu;
import b1.ccsds.csts.service.instance.id.ServiceInstanceIdentifier;

public class ServiceType extends PublishedIdentifier {

	private static final long serialVersionUID = 1L;

	public ServiceType() {
	}

	public ServiceType(byte[] code) {
		super(code);
	}

	public ServiceType(int[] value) {
		super(value);
	}

}