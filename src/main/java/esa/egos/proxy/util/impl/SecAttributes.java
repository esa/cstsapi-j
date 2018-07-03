package esa.egos.proxy.util.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.util.ICredentials;
import esa.egos.proxy.GenStrUtil;
import esa.egos.proxy.util.ISecAttributes;
import esa.egos.proxy.util.ITime;
import esa.egos.proxy.util.IUtil;

public class SecAttributes implements ISecAttributes {

	private static final Logger LOG = Logger.getLogger(SecAttributes.class.getName());
	
    /**
     * The user name.
     */
    private String username;

    /**
     * The password.
     */
    private byte[] password;
	
    /**
     * The api access between credentials, time and secattributes.
     */
	private IUtil util;

	/**
	 * Connect with api
	 */
	public SecAttributes(IUtil util) {
		this.util = util;
	}
	
    /**
     * Copy constructor
     */
    private SecAttributes(SecAttributes right)
    {
        if ((right.password != null) && (right.password.length != 0))
        {
            this.password = Arrays.copyOf(right.password, right.password.length);
        }
        else
        {
            this.password = null;
        }

        if (!right.username.isEmpty())
        {
            this.username = right.username;
        }
        
        if(right.util != null)
        {
        	this.util = right.util;
        }
    }
	
	@Override
	public void setUserName(String name) {
        this.username = name;
	}

	@Override
	public void setPassword(byte[] pwd) {
        if ((pwd != null) && (pwd.length != 0))
        {
            this.password = Arrays.copyOf(pwd, pwd.length);
        }
	}

	@Override
	public void setHexPassword(String pwd) {
        byte[] password = GenStrUtil.hexToBin(pwd);
        if ((password != null) && (password.length != 0))
        {
            this.password = password;
        }
	}

	@Override
	public ICredentials generateCredentials() {
		
		ICredentials credentials = this.util.createCredentials();
		ITime time = this.util.createTime();
		
		time.update();
		credentials.setTimeRef(time);
		
        // Fill the random of the credentials
        Random rand = new Random();
        long max = 2147483647;
        long randomNumber = (Math.abs(rand.nextLong())) % max;
		credentials.setRandomNumber(randomNumber);
		
        // Set the protected of the credentials
        byte[] hash_buffer = buildProtected(credentials);
        credentials.setProtected(hash_buffer);

        if (LOG.isLoggable(Level.FINE))
        {
            LOG.fine("Generating credentials with result " + credentials);
            LOG.fine("Username=" + this.username + ", password=" + Arrays.toString(this.password));
        }
        
		return credentials;
	}

	@Override
	public boolean authenticate(ICredentials credentials, int acceptableDelay) {
        if (LOG.isLoggable(Level.FINE))
        {
            LOG.fine("Authenticating credentials " + credentials);
            LOG.fine("Username=" + this.username + ", password=" + Arrays.toString(this.password));
        }
        ITime pTime = this.util.createTime();

        // check diff between the current time and the time of the credentials
        ITime pTimeCredentials = credentials.getTimeRef();
        pTime.update();

        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("pTime = " + pTime + ", pTimeCredentials = " + pTimeCredentials);
        }

        double diff = 0.0;
        diff = pTime.subtract(pTimeCredentials);
        if (diff < 0)
        {
            diff = -diff;
        }

        if (diff > acceptableDelay)
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("Time difference = " + diff + " > Acceptable delay = " + acceptableDelay);
            }
            return false;
        }

        // encode and check
        byte[] hashBuffer = buildProtected(credentials);

        // compare the buffer with the one of the credentials
        byte[] pProtected = credentials.getProtected();

        GenStrUtil.print("hashBuffer=", hashBuffer);
        GenStrUtil.print("pProtected=", pProtected);

        return Arrays.equals(hashBuffer, pProtected);
	}

	@Override
	public ISecAttributes copy() {
		return new SecAttributes(this);
	}
	
    /**
     * @param credentials
     * @return
     */
    private byte[] buildProtected(ICredentials credentials)
    {
        // encode the security attribute
        byte[] buffer = encodeSecAttributes(credentials);
        // call the hashCode function
        byte[] hashBuffer = null;

        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            // md.update(buffer, 0, buffer.length);
            md.update(buffer);
            hashBuffer = md.digest();
        }
        catch (NoSuchAlgorithmException e)
        {
            LOG.log(Level.FINE, "NoSuchAlgorithmException ", e);
        }

        return hashBuffer;
    }

	private byte[] encodeSecAttributes(ICredentials credentials) {
		
        byte[] buffer = null;
        long random = 0;
        int i = 0, l = 0;
        int maxSecAttrSize = 2500;

        buffer = new byte[maxSecAttrSize];

        // encode SEQUENCE
        buffer[l++] = 0x30;
        buffer[l++] = 0; // fix length field later

        // encode TimeCCSDS (OCTET STRING)
        if (LOG.isLoggable(Level.FINE))
        {
            LOG.log(Level.FINE, "Credentials attributes to be encoded: " + credentials);
        }
        ITime pTime = credentials.getTimeRef();

        if (LOG.isLoggable(Level.FINE))
        {
            LOG.log(Level.FINE, "Time to use to extract CDS time: " + pTime);
        }
        byte[] time_cds = pTime.getCDS();

        if (LOG.isLoggable(Level.FINE))
        {
            LOG.log(Level.FINE, "Credentials attributes to be encoded: time_cds=" + Arrays.toString(time_cds)
                                + ", random=" + credentials.getRandomNumber());
            LOG.fine("Username=" + this.username + ", password=" + Arrays.toString(this.password));
        }
        buffer[l++] = 0x04;
        buffer[l++] = 0x08;

        System.arraycopy(time_cds, 0, buffer, l++, 8);
        l += 7;

        // encode random number (INTEGER)
        buffer[l++] = 0x02;
        random = credentials.getRandomNumber();
        i = 4;
        while (((random & 0xff800000L) == 0) || (random & 0xff800000L) == 0xff800000L)
        {
            if (i == 1)
            {
                break;
            }
            random <<= 8;
            i--;
        }

        buffer[l++] = (byte) i;

        for (; i > 0; i--)
        {
            buffer[l++] = (byte) (random >> 24);
            random <<= 8;
        }

        // encode username (VisibleString)
        buffer[l++] = 0x1A;
        buffer[l++] = (byte) this.username.length();
        for (i = 0; i < this.username.length(); i++)
        {
            buffer[l++] = (byte) this.username.charAt(i);
        }

        // encode password (OCTET STRING)
        buffer[l++] = 0x04;
        buffer[l++] = (byte) this.password.length;
        System.arraycopy(this.password, 0, buffer, l++, this.password.length);

        l += this.password.length - 1;

        // now fill the length field for the whole sequence
        buffer[1] = (byte) (l - 2);

        byte[] bufferToRtn = new byte[l];
        System.arraycopy(buffer, 0, bufferToRtn, 0, l);
        return bufferToRtn;
	}

}
