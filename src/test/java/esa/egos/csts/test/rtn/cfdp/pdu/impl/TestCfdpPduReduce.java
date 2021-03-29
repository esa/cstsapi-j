package esa.egos.csts.test.rtn.cfdp.pdu.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import esa.egos.csts.rtn.cfdp.reduction.CfdpPduReduce;

public class TestCfdpPduReduce {

	
	@Test
	@Ignore
	public void testCfdpPduReductionWithChecksum()
	{
		CfdpPduReduce cfdpPduReduce = new CfdpPduReduce();
		
        byte[] bigFiledata = new byte[65535];
        Arrays.fill(bigFiledata, (byte) 0x0F);
        
        bigFiledata[0]=(byte) 0x10;//file data
        bigFiledata[1]=(byte) 0xFF;//length 64k
        bigFiledata[2]=(byte) 0xFF;//length 64k
        bigFiledata[3]=(byte) 0x01;//8 bytes header
		
        byte[] reducedFiledata = cfdpPduReduce.createReducedPdu(bigFiledata);
        assertTrue(reducedFiledata.length < bigFiledata.length);
        System.out.println("Resulting RPDU Size = " + reducedFiledata.length); 
	}
	
	
	@Test
	public void testCfdpPduReductionWithChecksum1M()
	{
		CfdpPduReduce cfdpPduReduce = new CfdpPduReduce();
		
        byte[] bigFiledata = new byte[65535];
        Arrays.fill(bigFiledata, (byte) 0x0F);
        
        bigFiledata[0]=(byte) 0x10;//file data
        bigFiledata[1]=(byte) 0xFF;//length 64k
        bigFiledata[2]=(byte) 0xFF;//length 64k
        bigFiledata[3]=(byte) 0x01;//8 bytes header
        
        Instant start = Instant.now();
		
        int N = 10_000_000;
        
        for(int i = 0; i < N;i++)
        {
        	byte[] reducedFiledata = cfdpPduReduce.createReducedPdu(bigFiledata,true);
        }
        
        Instant stop = Instant.now();
        Duration interval = Duration.between(start,stop);
        
        double timeSpan = interval.getSeconds() + interval.getNano()/(1000.0*1000*1000);
        //assertTrue(reducedFiledata.length < bigFiledata.length);
        System.out.println("Unit rate: " + N/timeSpan + " over " + timeSpan); 
	}
}
