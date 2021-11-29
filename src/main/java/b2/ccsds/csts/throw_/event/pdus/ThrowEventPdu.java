/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b2.ccsds.csts.throw_.event.pdus;

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
import b2.ccsds.csts.pdus.CstsFrameworkPdu;

public class ThrowEventPdu extends CstsFrameworkPdu {

	private static final long serialVersionUID = 1L;

	public ThrowEventPdu() {
	}

	public ThrowEventPdu(byte[] code) {
		super(code);
	}

}
