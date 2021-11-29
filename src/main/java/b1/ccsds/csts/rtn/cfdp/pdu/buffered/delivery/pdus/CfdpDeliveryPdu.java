/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b1.ccsds.csts.rtn.cfdp.pdu.buffered.delivery.pdus;

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

import b1.ccsds.csts.buffered.data.delivery.pdus.BufferedDataDeliveryPdu;
import b1.ccsds.csts.common.types.Extended;

public class CfdpDeliveryPdu extends BufferedDataDeliveryPdu {

	private static final long serialVersionUID = 1L;

	public CfdpDeliveryPdu() {
	}

	public CfdpDeliveryPdu(byte[] code) {
		super(code);
	}

}
