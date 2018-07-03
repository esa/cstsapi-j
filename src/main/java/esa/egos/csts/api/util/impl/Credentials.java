package esa.egos.csts.api.util.impl;

import java.util.Arrays;

import org.openmuc.jasn1.ber.types.BerNull;
import org.openmuc.jasn1.ber.types.BerOctetString;

import esa.egos.csts.api.util.ICredentials;
import esa.egos.proxy.GenStrUtil;
import esa.egos.proxy.enums.TimeFormat;
import esa.egos.proxy.enums.TimeRes;
import esa.egos.proxy.util.ITime;

public class Credentials implements ICredentials{

    /**
     * The time when the message digest was generated.
     */
    private ITime timeRef;

    /**
     * The random number
     */
    private long randomNumber;

    /**
     * The message digest (the protected)
     */
    private byte[] messageDigest;
	
	public Credentials() {
        this.timeRef = null;
        this.randomNumber = 0;
        this.messageDigest = null;
	}
	
	public Credentials(Credentials right) {
        if ((right.messageDigest != null) && (right.messageDigest.length != 0))
        {
            this.messageDigest = new byte[right.messageDigest.length];
            System.arraycopy(right.messageDigest, 0, this.messageDigest, 0, right.messageDigest.length);
        }
        else
        {
            this.messageDigest = null;
        }

        if (right.timeRef != null)
        {
            this.timeRef = right.timeRef.copy();
        }
        else
        {
            this.timeRef = null;
        }

        this.randomNumber = right.randomNumber;
	}

	@Override
	public long getRandomNumber() {
		return this.randomNumber;
	}

	@Override
	public byte[] getProtected() {
        if ((this.messageDigest.length != 0) && (this.messageDigest != null))
        {
            return Arrays.copyOf(this.messageDigest, this.messageDigest.length);
        }

        return null;
	}

	@Override
	public ITime getTimeRef() {
		return this.timeRef;
	}

	@Override
	public void setRandomNumber(long number) {
		this.randomNumber = number;
	}

	@Override
	public void setProtected(byte[] hashCode) {
        if ((hashCode != null) && (hashCode.length != 0))
        {
            this.messageDigest = Arrays.copyOf(hashCode, hashCode.length);
        }
	}

	@Override
	public void setTimeRef(ITime time) {
		this.timeRef = time.copy();
	}

	@Override
	public ICredentials copy() {
		return new Credentials(this);
	}

	@Override
	public String dump() {
        StringBuilder dumpStr = new StringBuilder();
        dumpStr.append("\n");
        dumpStr.append("       Random Number        : " + this.randomNumber + "\n");

        if (this.timeRef != null)
        {
            dumpStr.append("       Generation Time      : "
                           + this.timeRef.getDateAndTime(TimeFormat.TF_dayOfMonth, TimeRes.TR_microSec)
                           + "\n");
        }

        byte[] time_cds = this.timeRef.getCDS();
        if (time_cds != null)
        {
            dumpStr.append("       Generation Time (CDS): " + GenStrUtil.convAscii(time_cds, time_cds.length) + "\n");
        }

        if ((this.messageDigest != null) && (this.messageDigest.length != 0))
        {
            dumpStr.append("       Hash Code            : "
                           + GenStrUtil.convAscii(this.messageDigest, this.messageDigest.length) + "\n");
        }

        dumpStr.append("       Hash Code size       : " + this.messageDigest.length);

        return dumpStr.toString();
	}

	@Override
	public ccsds.csts.common.types.Credentials encode() {
		
		ccsds.csts.common.types.Credentials cred = new ccsds.csts.common.types.Credentials();
		
		if (getRandomNumber() != 0){
		    BerOctetString string = new BerOctetString();
			string.value = getProtected(); // TODO check
			cred.setUsed(string);
		} else {
			cred.setUnused(new BerNull());
		}

		return cred;
	}

	/**
	 * Transforms ccsds.csts.common.types.Credentials into internal credentials.
	 * @param ccsds.csts.common.types.Credentials performerCredentials
	 * @return
	 */
	public static ICredentials decode(ccsds.csts.common.types.Credentials performerCredentials) {
		Credentials cred = new Credentials();
		
		if(performerCredentials.getUsed() != null)
			cred.setProtected(performerCredentials.getUsed().value);
		
		return cred;
	}

}
