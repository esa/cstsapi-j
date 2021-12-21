package esa.egos.csts.api.oids;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Maps an OID bit to OID bit label.
 */
public class OidNode
{
    private static final boolean PRINT_UNKNOWN_NODE = true;

    private static final String DOT = ".";

    private int oidBit;

    private String oidBitLabel;

    private Map<Integer, OidNode> childNodes = new HashMap<Integer, OidNode>();


    public OidNode(int oidBit)
    {
        this.oidBit = oidBit;
    }

    public OidNode(int oidBit, String oidBitLabel)
    {
        this.oidBit = oidBit;
        this.oidBitLabel = oidBitLabel;
    }

    public int getOidBit()
    {
        return this.oidBit;
    }

    public String getOidBitLable()
    {
        return this.oidBitLabel;
    }

    public void setOidBitLable(String oidBitLabel)
    {
        this.oidBitLabel = oidBitLabel;
    }

    public String toString()
    {
        return "OidNode [oidBit=" + this.oidBit + ", oidBitLabel=" + this.oidBitLabel + "]";
    }

    public OidNode getChildNode(int oidBit)
    {
        return this.childNodes.get(oidBit);
    }

    public List<OidNode> getChildNodes()
    {
        List<OidNode> children = new LinkedList<OidNode>();
        for (int oidBit : this.childNodes.keySet())
        {
            children.add(this.childNodes.get(oidBit));
        }
        return children;
    }

    public void addChildNode(int oidBit)
    {
        this.childNodes.put(new Integer(oidBit), new OidNode(oidBit));
    }

    public OidNode addChildNode(int oidBit, String oidBitString)
    {
        OidNode childNode = new OidNode(oidBit, oidBitString);
        this.childNodes.put(new Integer(oidBit), childNode);
        return childNode;
    }

    private static void printUnknown(StringBuilder s, int bit)
    {
        s.append("unknown(");
        s.append(bit);
        s.append(")");
    }

    private static void printCompactUnknown(StringBuilder s, int bit)
    {
        s.append("unknown");
    }

    private static void throwOnUnknown(int[] oidArray, int bitPos, String parent)
    {
        StringBuilder s = new StringBuilder(Arrays.toString(oidArray));
        s.append(", bit=");
        s.append(oidArray[bitPos]);
        s.append(", at pos=");
        s.append(bitPos);
        s.append(", in parent ");
        s.append(parent);
        throw new NoSuchElementException(s.toString());
    }

    public void print(StringBuilder s, int[] oidArray, int bitPos)
    {
        if (this.oidBit == oidArray[bitPos])
        {
        	boolean skipIso = false;
        	if(this.oidBit == 1 && bitPos == 0 ||
        	   this.oidBit == 3 && bitPos == 1 ||
        	   this.oidBit == 112 && bitPos == 2 ||
        	   this.oidBit == 4 && bitPos == 3 ||
        	   this.oidBit == 4 && bitPos == 4)
        	{
        		skipIso = true;
        	}
        	
        	if(skipIso == false)
        	{
	            s.append(this.oidBitLabel);
	            s.append("(");
	            s.append(this.oidBit);
	            s.append(")");          
        	}
        	
            int childOidBit = bitPos + 1;
            if (childOidBit < oidArray.length)
            {
            	if(skipIso != true)
            		s.append(DOT);
            	
                OidNode node = getChildNode(oidArray[childOidBit]);
                if (node != null)
                {
                    node.print(s, oidArray, childOidBit);
                }
                else
                {
                    if (PRINT_UNKNOWN_NODE)
                    {
                        printUnknown(s, oidArray[childOidBit]);
                    }
                    else
                    {
                        throwOnUnknown(oidArray, childOidBit, s.toString());
                    }
                }
            }
        }
        else
        {
            if (PRINT_UNKNOWN_NODE)
            {
                printUnknown(s, oidArray[this.oidBit]);
            }
            else
            {
                throwOnUnknown(oidArray, this.oidBit, s.toString());
            }
        }
    }

    public void printCompact(StringBuilder s, int[] oidArray, int bitPos)
    {
        if (this.oidBit == oidArray[bitPos])
        {
            if (bitPos == oidArray.length - 1)
            {
                s.append(this.oidBitLabel);
            }
            else
            {
                int childOidBit = bitPos + 1;
                if (childOidBit < oidArray.length)
                {
                    OidNode node = getChildNode(oidArray[childOidBit]);
                    if (node != null)
                    {
                        node.printCompact(s, oidArray, childOidBit);
                    }
                    else
                    {
                        if (PRINT_UNKNOWN_NODE)
                        {
                            printCompactUnknown(s, oidArray[childOidBit]);
                        }
                        else
                        {
                            throwOnUnknown(oidArray, childOidBit, s.toString());
                        }
                    }
                }
            }
        }
    }
}
