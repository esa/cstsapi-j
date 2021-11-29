/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b1.ccsds.csts.fw.procedure.parameters.events.directives;

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

import b1.ccsds.csts.common.types.AuthorityIdentifier;
import b1.ccsds.csts.common.types.BufferSize;
import b1.ccsds.csts.common.types.DataTransferMode;
import b1.ccsds.csts.common.types.DeliveryLatencyLimit;
import b1.ccsds.csts.common.types.DeliveryMode;
import b1.ccsds.csts.common.types.IntPos;
import b1.ccsds.csts.common.types.Label;
import b1.ccsds.csts.common.types.ProcessingLatencyLimit;
import b1.ccsds.csts.common.types.ServiceUserRespTimer;
import b1.ccsds.csts.service.instance.id.ServiceInstanceIdentifier;

public class PACresponderIdType extends AuthorityIdentifier {

	private static final long serialVersionUID = 1L;

	public PACresponderIdType() {
	}

	public PACresponderIdType(byte[] value) {
		super(value);
	}

}
