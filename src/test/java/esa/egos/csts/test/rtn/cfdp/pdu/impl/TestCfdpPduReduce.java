package esa.egos.csts.test.rtn.cfdp.pdu.impl;

import static org.junit.Assert.assertTrue;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import esa.egos.csts.rtn.cfdp.reduction.CfdpPduReduce;

public class TestCfdpPduReduce {

	
	@Test
	@Ignore
	public void testCfdpPduReductionWithChecksum()
	{
		CfdpPduReduce cfdpPduReduce = new CfdpPduReduce();
		
        byte[] fullPdu = new byte[65535];
        Arrays.fill(fullPdu, (byte) 0x0F);
        
        fullPdu[0]=(byte) 0x10;//file data
        fullPdu[1]=(byte) 0xFF;//length 64k
        fullPdu[2]=(byte) 0xFF;//length 64k
        fullPdu[3]=(byte) 0x01;//8 bytes header
		
        byte[] reducedPdu = cfdpPduReduce.createReducedPdu(fullPdu);
        assertTrue(reducedPdu.length < fullPdu.length);
        System.out.println("Resulting RPDU Size = " + reducedPdu.length); 
	}
	
	
	@Test
	public void testCfdpPduReductionWithChecksum1M()
	{
		CfdpPduReduce cfdpPduReduce = new CfdpPduReduce();
		
        byte[] fullPdu = new byte[65535];
        Arrays.fill(fullPdu, (byte) 0x0F);
        
        fullPdu[0]=(byte) 0x10;//file data
        fullPdu[1]=(byte) 0xFF;//length 64k
        fullPdu[2]=(byte) 0xFF;//length 64k
        fullPdu[3]=(byte) 0x01;//8 bytes header
        
        Instant start = Instant.now();
		
        final int N = 1_000_000; 
        
        for(int i = 0; i < N;i++)
        {
        	byte[] reducedPdu = cfdpPduReduce.createReducedPdu(fullPdu,true);
        	//System.out.print("Created reduced PDUs " + i + "/"+ N + "\r");
        }
        
        Instant stop = Instant.now();
        Duration interval = Duration.between(start,stop);
        
        double timeSpan = interval.getSeconds() + interval.getNano()/(1000.0*1000*1000);
        //assertTrue(reducedFiledata.length < bigFiledata.length);
        System.out.println("Unit rate: " + N/timeSpan + " PDUs/s over " + timeSpan + "s for " + N + " PDUs"); 
	}
}
