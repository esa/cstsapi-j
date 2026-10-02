package esa.egos.csts.api.util.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;
import com.beanit.jasn1.ber.types.BerInteger;
import com.beanit.jasn1.ber.types.BerNull;
import com.beanit.jasn1.ber.types.BerOctetString;

import b1.isp1.credentials.ISP1Credentials;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.util.ICredentials;
import esa.egos.proxy.GenStrUtil;
import esa.egos.proxy.enums.TimeFormat;
import esa.egos.proxy.enums.TimeRes;
import esa.egos.proxy.tml.Channel;
import esa.egos.proxy.util.ITime;
import esa.egos.proxy.util.impl.ApiTime;

public class Credentials implements ICredentials{

	
	private static final Logger LOG = Logger.getLogger(Channel.class.getName());
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

	public b1.ccsds.csts.common.types.Credentials encode(b1.ccsds.csts.common.types.Credentials cred) {
		
        if (getRandomNumber() <= 0)
        {
        	cred.setUnused(new BerNull());
        }
        else
        {
            // ISP1 Credentials
        	b1.isp1.credentials.ISP1Credentials isp1Credentials = new b1.isp1.credentials.ISP1Credentials();

            // fill the time
            ITime time = this.getTimeRef();
            if (time != null)
            {
            	isp1Credentials.setTime(new BerOctetString(time.getCDS()));
            }
            else
            {
            	isp1Credentials.setTime(new BerOctetString());
            }

            // fill the protected
            byte[] theProtected = this.getProtected();
            if (theProtected != null)
            {
            	
            	isp1Credentials.setTheProtected(new BerOctetString(theProtected));

            }

            // fill the random number
            isp1Credentials.setRandomNumber(new BerInteger(this.getRandomNumber()));

            ReverseByteArrayOutputStream encoding = new ReverseByteArrayOutputStream(72, true);
            
                try {
					isp1Credentials.encode(encoding, true);

					cred.setUsed(new BerOctetString(encoding.getArray()));
				} catch (IOException e) {
					LOG.log(Level.FINE, "ApiException: Encounter problems encoding ISP! credentials ", e);
				}                
            }
            
		return cred;
	}
	
	public b2.ccsds.csts.common.types.Credentials encode(b2.ccsds.csts.common.types.Credentials cred) {
		
		if (getRandomNumber() <= 0)
        {
        	cred.setUnused(new BerNull());
        }
        else
        {
            // ISP1 Credentials
        	b2.isp1.credentials.ISP1Credentials isp1Credentials = new b2.isp1.credentials.ISP1Credentials();

            // fill the time
            ITime time = this.getTimeRef();
            if (time != null)
            {
            	isp1Credentials.setTime(new BerOctetString(time.getCDS()));
            }
            else
            {
            	isp1Credentials.setTime(new BerOctetString());
            }

            // fill the protected
            byte[] theProtected = this.getProtected();
            if (theProtected != null)
            {
            	
            	isp1Credentials.setTheProtected(new BerOctetString(theProtected));

            }

            // fill the random number
            isp1Credentials.setRandomNumber(new BerInteger(this.getRandomNumber()));

            ReverseByteArrayOutputStream encoding = new ReverseByteArrayOutputStream(72, true);
            
                try {
					isp1Credentials.encode(encoding, true);

					cred.setUsed(new BerOctetString(encoding.getArray()));
				} catch (IOException e) {
					LOG.log(Level.FINE, "ApiException: Encounter problems encoding ISP! credentials ", e);
				}                
            }

		return cred;
	}


	/**
	 * Transforms ccsds.csts.common.types.Credentials into internal credentials.
	 * @param ccsds.csts.common.types.Credentials performerCredentials
	 * @return
	 */
	public static ICredentials decode(b1.ccsds.csts.common.types.Credentials performerCredentials) {
		Credentials cred = new Credentials();
		
		if (performerCredentials.getUnused() != null)
        {
			cred = null;
        }
        else
        {
        		cred = new Credentials();

                if (cred != null)
                {
                	InputStream is = new ByteArrayInputStream(performerCredentials.getUsed().value);
                	b1.isp1.credentials.ISP1Credentials isp1c = new b1.isp1.credentials.ISP1Credentials();

                    // decode ISP1 credentials
                    try
                    {
                        isp1c.decode(is, true);
                    }
                    catch (IOException e)
                    {
                    	LOG.log(Level.FINE, "ApiException: Encounter problems decoding credentials ", e);
                        return null;
                    }

                    // set the time
                    ITime pTime = new ApiTime();

                    if (pTime != null)
                    {
                    		try {
								pTime.setCDS(isp1c.getTime().value);
							} catch (ApiException e) {
								LOG.log(Level.FINE, "ApiException: Encounter problems setting Time CDS while decoding ", e);
							}
                            cred.setTimeRef(pTime);
                    }

                    // set the protected
                    cred.setProtected(isp1c.getTheProtected().value);

                    // set the random number
                    cred.setRandomNumber(isp1c.getRandomNumber().longValue());
                }
            }
		
		return cred;
	}
	
	public static ICredentials decode(b2.ccsds.csts.common.types.Credentials performerCredentials) {
		Credentials cred = new Credentials();
		
		if (performerCredentials.getUnused() != null)
        {
			cred = null;
        }
        else
        {
        		cred = new Credentials();

                if (cred != null)
                {
                	InputStream is = new ByteArrayInputStream(performerCredentials.getUsed().value);
                    b2.isp1.credentials.ISP1Credentials isp1c = new b2.isp1.credentials.ISP1Credentials();

                    // decode ISP1 credentials
                    try
                    {
                        isp1c.decode(is, true);
                    }
                    catch (IOException e)
                    {
                    	LOG.log(Level.FINE, "ApiException: Encounter problems decoding credentials ", e);
                        return null;
                    }

                    // set the time
                    ITime pTime = new ApiTime();

                    if (pTime != null)
                    {
                    		try {
								pTime.setCDS(isp1c.getTime().value);
							} catch (ApiException e) {
								LOG.log(Level.FINE, "ApiException: Encounter problems setting Time CDS while decoding ", e);
							}
                            cred.setTimeRef(pTime);
                    }

                    // set the protected
                    cred.setProtected(isp1c.getTheProtected().value);

                    // set the random number
                    cred.setRandomNumber(isp1c.getRandomNumber().longValue());
                }
            }
		
		return cred;
	}

}
