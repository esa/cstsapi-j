package esa.egos.csts.api.enums;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import ccsds.csts.association.control.types.PeerAbortDiagnostic;

public enum PeerAbortDiagnostics {
	
    PAD_accessDenied(0, "access denied"),
    PAD_unexpectedResponderId(1, "unexpected responder id"),
    PAD_operationalRequirement(2, "operational requirement"),
    PAD_protocolError(3, "protocol error"),
    PAD_communicationsFailure(4, "communications failure"),
    PAD_encodingError(5, "encoding-decoding error"),
    PAD_returnTimeout(6, "return timeout"),
    PAD_endOfServiceProvisionPeriod(7, "end of service provision period"),
    PAD_unsolicitedInvokeId(8, "unsolicited invocation id"),
    PAD_otherReason(127, "other reason"),
    PAD_invalid(-1, "invalid");

    private int code;

    private String msg;


    /**
     * Constructor PeerAbortDiagnostic.
     * 
     * @param code
     * @param msg
     */
    private PeerAbortDiagnostics(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    /**
     * Gets the code.
     * 
     * @return
     */
    public int getCode()
    {
        return this.code;
    }

    @Override
    public String toString()
    {
        return this.msg;
    }

    /**
     * Gets peer abort diagnostic by code.
     * 
     * @param code
     * @return null if there is no peer abort diagnostic for the given code.
     */
    public static PeerAbortDiagnostics getDiagByCode(int code)
    {
        for (PeerAbortDiagnostics e : values())
        {
            if (e.code == code)
            {
                return e;
            }
        }

        return null;
    }
    
    public static PeerAbortDiagnostics getDiagByString(String mess)
    {
        for (PeerAbortDiagnostics e : values())
        {
            if (e.msg.equals(mess))
            {
                return e;
            }
        }

        return null;
    }

	public byte[] getBytes() throws IOException {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    ObjectOutput out = new ObjectOutputStream(bos);
	    out.writeInt(getCode());
	    out.close();
	    byte[] int_bytes = bos.toByteArray();
	    bos.close();
		return int_bytes;
	}

	public static PeerAbortDiagnostics encode(PeerAbortDiagnostic diagnostic) {
		
		// TODO is it a String or int?
		PeerAbortDiagnostics diag = getDiagByString(diagnostic.toString());
		
		if (diag == null)
			diag = getDiagByCode(Integer.parseInt(diagnostic.toString()));
		
		// still null? send invalid
		if (diag == null)	
			return PeerAbortDiagnostics.PAD_invalid;
		
		return diag;
	}
}
