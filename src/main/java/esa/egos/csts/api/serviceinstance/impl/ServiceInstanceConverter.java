package esa.egos.csts.api.serviceinstance.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;

public class ServiceInstanceConverter {

	static private Logger LOG = Logger.getLogger(ServiceInstanceConverter.class.getName());
	
    /**
     * The attribute separator.
     */
    private static char attrSep = '.';

    /**
     * The name-value separator.
     */
    private static char valSep = '=';
	
	public static IServiceInstanceIdentifier decodeServiceInstanceIdentifier(String siiString) throws ApiException{
		
		IServiceInstanceIdentifier serviceInstance =  null;
		
		if (siiString.isEmpty())
			throw new ApiException("Empty string passed as input argument");
		
        // Remove spaces
        siiString = siiString.replaceAll("\\s+", "");

        if (siiString.isEmpty())
        {
            throw new ApiException("Empty string passed as input argument");
        }

        int numMembers = checkSyntax(siiString, false);

        int numSeparators = numMembers - 1;

        // separate the name-attribute pairs
        List<String> nameList = new ArrayList<String>();
        for (int j = 0; j <= numSeparators; j++)
        {
            int p = siiString.indexOf(attrSep);

            String nameStr = "";

            if (p == -1)
            {
                nameStr = siiString;
            }
            else
            {
                nameStr = siiString.substring(0, p);
            }

            nameList.add(nameStr);

            if (p != -1)
            {
                siiString = siiString.substring(p + 1, siiString.length());
            }
            else
            {
                siiString = "";
            }
        }

        // generate name-value pairs
        ObjectIdentifier spacecraftIdentifier = null;
        ObjectIdentifier facilityIdentifier = null;
        ObjectIdentifier typeIdentifier = null;
        int serviceInstanceNumber = 0;
        
        for (String member : nameList)
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest(member);
            }
            int p = member.indexOf(valSep);
            if (p == -1)
            {
                throw new ApiException("Invalid argument in service instance identifier decoding.");
            }

            String nameAttr = member.substring(0, p);
            String valAttr = member.substring(p + 1);

            // check and get the name OID
            if(nameAttr.equals("spacecraft") || nameAttr.equals("facility") || nameAttr.equals("type"))
            {
            	ObjectIdentifier id = ObjectIdentifier.parseOid(sanitizeOid(valAttr));

	            if (id == null)
	            	throw new ApiException("Invalid name attribute " + nameAttr + " in service instance decoding.");
	            
	            // S/C
	            if( nameAttr.equals("spacecraft"))
	            	spacecraftIdentifier = id;
	            // ANT
	            else if(nameAttr.equals("facility"))
	            	facilityIdentifier = id;
	            //TSP
	            else if (nameAttr.equals("type"))	
	            	typeIdentifier = id;
	        } else if (nameAttr.equals("serviceinstance"))
	        	serviceInstanceNumber = Integer.parseInt(valAttr);
        }

		serviceInstance = new ServiceInstanceIdentifier
				(spacecraftIdentifier, facilityIdentifier, typeIdentifier, serviceInstanceNumber);
	
		return serviceInstance;
	}
	
    private static String sanitizeOid(String valAttr) {
		String santiziedValAttr = valAttr;
		
		santiziedValAttr = santiziedValAttr.replace("[", "");
		santiziedValAttr = santiziedValAttr.replace("]", "");
		santiziedValAttr = santiziedValAttr.trim();
		
		return santiziedValAttr;
	}

	/**
     * ////////////////////////////////////////////////// Checks the syntax of
     * the supplied attribute-value pairs. This member function can also be used
     * for the syntax check of an attribute value. The number of attribute-value
     * pairs is returned by the <members> argument (if it is needed by the
     * client). If there is only an attribute value to be checked, <valueOnly>
     * must be set to true. //////////////////////////////////////////////////
     */
    private static int checkSyntax(String siiVal, boolean valueOnly) throws ApiException
    {
        int numSeparators = 0;
        int numValueSepar = 0;
        int members = 0;

        // Check syntax
        if (!siiVal.matches("\\A\\p{ASCII}*\\z"))
        {
            throw new ApiException("The input string " + siiVal + " contains not ASCII characters");
        }

        for (int i = 0; i < siiVal.length(); i++)
        {
            if (Character.isWhitespace(siiVal.charAt(i)))
            {
                throw new ApiException( "The input string " + siiVal + " contains white spaces or not ASCII characters");
            }

            if (siiVal.charAt(i) == attrSep)
            {
                numSeparators++;
            }
            else if (siiVal.charAt(i) == valSep)
            {
                numValueSepar++;
            }
        }

        members = numSeparators + 1;

        if (valueOnly)
        {
            // the passed string is an attribute value
            if (!(members == 1 && numValueSepar == 0))
            {
                throw new ApiException("Invalid argument in service instance decoding.");
            }
            return members;
        }

        if (numValueSepar != members)
        {
            throw new ApiException("Invalid argument in service instance decoding.");
        }

        return members;
    }
	
}
