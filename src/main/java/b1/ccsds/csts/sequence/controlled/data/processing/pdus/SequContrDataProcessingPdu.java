/**
 * This class file was automatically generated by jASN1 v1.11.2 (http://www.beanit.com)
 */

package b1.ccsds.csts.sequence.controlled.data.processing.pdus;

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

import b1.ccsds.csts.common.types.ConditionalTime;
import b1.ccsds.csts.common.types.DataUnitId;
import b1.ccsds.csts.common.types.Embedded;
import b1.ccsds.csts.common.types.Extended;
import b1.ccsds.csts.pdus.CstsFrameworkPdu;

public class SequContrDataProcessingPdu extends CstsFrameworkPdu {

	private static final long serialVersionUID = 1L;

	public SequContrDataProcessingPdu() {
	}

	public SequContrDataProcessingPdu(byte[] code) {
		super(code);
	}

}
