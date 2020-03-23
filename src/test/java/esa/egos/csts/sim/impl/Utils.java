package esa.egos.csts.sim.impl;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class Utils
{
    /**
     * Serialise JAX bean to an XML file
     * @param pathFileName the path file name
     * @param obj the bean
     * @throws Exception
     */
    public static void save(String pathFileName, Object obj) throws Exception
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(pathFileName);
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(obj, fos);
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String toHex(byte[] array)
    {
        StringBuilder sb = new StringBuilder(array.length * 2);
        for (byte octet : array)
        {
            sb.append(String.format("%02x", octet));
        }
        return sb.toString();
    }

    public static String firstToUpperCase(String str) {
	return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String firstToLowerCase(String str) {
	return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
