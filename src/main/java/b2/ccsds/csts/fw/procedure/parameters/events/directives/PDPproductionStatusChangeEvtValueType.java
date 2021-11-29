/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b2.ccsds.csts.fw.procedure.parameters.events.directives;

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

import b2.ccsds.csts.common.types.AuthorityIdentifier;
import b2.ccsds.csts.common.types.BufferSize;
import b2.ccsds.csts.common.types.DataTransferMode;
import b2.ccsds.csts.common.types.DataUnitId;
import b2.ccsds.csts.common.types.DeliveryLatencyLimit;
import b2.ccsds.csts.common.types.DeliveryMode;
import b2.ccsds.csts.common.types.IdentifierString;
import b2.ccsds.csts.common.types.IntPos;
import b2.ccsds.csts.common.types.IntUnsigned;
import b2.ccsds.csts.common.types.Label;
import b2.ccsds.csts.common.types.ProcessingLatencyLimit;
import b2.ccsds.csts.common.types.ServiceUserRespTimer;
import b2.ccsds.csts.generic.service.object.identifiers.SvcProductionStatusVersion1Type;
import b2.ccsds.csts.service.instance.id.ServiceInstanceIdentifier;

public class PDPproductionStatusChangeEvtValueType extends SvcProductionStatusVersion1Type {

	private static final long serialVersionUID = 1L;

	public PDPproductionStatusChangeEvtValueType() {
	}

	public PDPproductionStatusChangeEvtValueType(byte[] code) {
		super(code);
	}

	public PDPproductionStatusChangeEvtValueType(BigInteger value) {
		super(value);
	}

	public PDPproductionStatusChangeEvtValueType(long value) {
		super(value);
	}

}
