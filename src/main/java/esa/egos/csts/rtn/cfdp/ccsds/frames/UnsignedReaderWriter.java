package esa.egos.csts.rtn.cfdp.ccsds.frames;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Helps with reading/writing unsigned numbers from a buffer.
 * 
 * @author rendell_p
 */
public class UnsignedReaderWriter
{
    /**
     * Gets a unsigned byte.
     * 
     * @param aBb The buffer.
     * @return The unsigned byte.
     */
    public static short getUByte(ByteBuffer aBb)
    {
        return ((short) (aBb.get() & 0xff));
    }

    /**
     * Puts a unsigned byte into a buffer.
     * 
     * @param aBb The buffer.
     * @param aValue The value.
     */
    public static void putUByte(ByteBuffer aBb, int aValue)
    {
        aBb.put((byte) (aValue & 0xff));
    }

    /**
     * Gets a unsigned byte.
     * 
     * @param aBb The buffer.
     * @param aPosition The position in the buffer.
     * @return The unsigned byte.
     */
    public static short getUByte(ByteBuffer aBb, int aPosition)
    {
        return ((short) (aBb.get(aPosition) & (short) 0xff));
    }

    /**
     * Puts a unsigned byte into a buffer.
     * 
     * @param aBb The buffer.
     * @param aPosition The position in the buffer.
     * @param aValue The value.
     */
    public static void putUByte(ByteBuffer aBb, int aPosition, int aValue)
    {
        aBb.put(aPosition, (byte) (aValue & 0xff));
    }

    /**
     * Gets a unsigned short.
     * 
     * @param aBb The buffer.
     * @return The unsigned short.
     */
    public static int getUShort(ByteBuffer aBb)
    {
        return (aBb.getShort() & 0xffff);
    }

    /**
     * Puts a unsigned short into a buffer.
     * 
     * @param aBb The buffer.
     * @param aValue The value.
     */
    public static void putUShort(ByteBuffer aBb, int aValue)
    {
        aBb.putShort((short) (aValue & 0xffff));
    }

    /**
     * Gets a unsigned byte.
     * 
     * @param aBb The buffer.
     * @param aPosition The position in the buffer.
     * @return The unsigned byte.
     */
    public static int getUShort(ByteBuffer aBb, int aPosition)
    {
        return (aBb.getShort(aPosition) & 0xffff);
    }

    /**
     * Puts a unsigned short into a buffer.
     * 
     * @param aBb The buffer.
     * @param aPosition The position in the buffer.
     * @param aValue The value.
     */
    public static void putUShort(ByteBuffer aBb, int aPosition, int aValue)
    {
        aBb.putShort(aPosition, (short) (aValue & 0xffff));
    }

    /**
     * Gets a unsigned integer.
     * 
     * @param aBb The buffer.
     * @return The unsigned integer.
     */
    public static long getUInt(ByteBuffer aBb)
    {
        return (aBb.getInt() & 0xffffffffL);
    }

    /**
     * Puts a unsigned int into a buffer.
     * 
     * @param aBb The buffer.
     * @param aValue The value.
     */
    public static void putUInt(ByteBuffer aBb, long aValue)
    {
        aBb.putInt((int) (aValue & 0xffffffff));
    }

    /**
     * Gets a unsigned integer.
     * 
     * @param aBb The buffer.
     * @param aPosition The position in the buffer.
     * @return The unsigned integer.
     */
    public static long getUInt(ByteBuffer aBb, int aPosition)
    {
        return (aBb.getInt(aPosition) & 0xffffffffL);
    }

    /**
     * Puts a unsigned integer into a buffer.
     * 
     * @param aBb The buffer.
     * @param aPosition The position in the buffer.
     * @param aValue The value.
     */
    public static void putUInt(ByteBuffer aBb, int aPosition, long aValue)
    {
        aBb.putInt(aPosition, (int) (aValue & 0xffffffffL));
    }

    /**
     * Puts a string into a buffer.
     * 
     * @param aBb The buffer.
     * @param aValue The value.
     */
    public static void putString(ByteBuffer aBb, String aValue)
    {
        aBb.put(aValue.getBytes());
    }

    /**
     * Gets a string.
     * 
     * @param aBb The buffer.
     * @param aLength The length of the string.
     * @return The string.
     */
    public static String getString(ByteBuffer aBb, int aLength)
    {
        byte[] buff = new byte[aLength];
        aBb.get(buff);
        return new String(buff);
    }

    /**
     * Performs a test.
     * 
     * @param aArgs The arguments (not read)
     */
    public static void main(String[] aArgs)
    {
        ByteBuffer buff = ByteBuffer.allocate(1024);
        buff.order(ByteOrder.BIG_ENDIAN);
        putString(buff, "Hello");
        System.out.println(Arrays.toString(buff.array()));
    }
}
