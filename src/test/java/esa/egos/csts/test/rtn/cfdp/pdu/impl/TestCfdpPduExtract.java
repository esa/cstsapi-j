package esa.egos.csts.test.rtn.cfdp.pdu.impl;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import esa.egos.csts.rtn.cfdp.ccsds.frames.CcsdsTmFrame;
import esa.egos.csts.rtn.cfdp.ccsds.frames.FrameConfigData;
import esa.egos.csts.rtn.cfdp.ccsds.frames.PacketConfigData;
import esa.egos.csts.rtn.cfdp.extraction.CfdpPduExtract;
import esa.egos.csts.rtn.cfdp.reduction.CfdpTransferData;

public class TestCfdpPduExtract {
	
	
	@Test
	public void testFrameExtraction4() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		
		try {
			//Thread.sleep(5000);
			
			PacketConfigData packetConfigData = FrameGenerator.generatePacketconfigData();
			FrameConfigData frameConfigData = FrameGenerator.generateTestFrameConfigData();
			
			Instant t0 = Instant.now();
			FrameGenerator frameGenerator = new FrameGenerator(frameConfigData,packetConfigData);
			List<CcsdsTmFrame> frames = frameGenerator.prepareDataFrames(2000, 16384);
			System.out.println("N Frames " + frames.size());
			assertTrue(frames.size()==18000+3);
			//meta data
			//4xpdu
			//cfdp eof
			//cfdp ack
			
			Instant t1 = Instant.now();

			CfdpPduExtract extractor = new CfdpPduExtract(frameConfigData,packetConfigData);
			List<CcsdsTmFrame> extractedFrames = frames.stream()
					.map(frame -> extractor.reconstructTmFrames(frame.GetTransferFrame()))
					.collect(Collectors.toList());
			
			System.out.println("N Frames 2" + extractedFrames.size());
			assertTrue(extractedFrames.size()==18000+3);
			
			Instant t2 = Instant.now();
					
			List<CfdpTransferData>  transferData = extractedFrames.stream()
					.map(frame -> extractor.extractTransferData(frame))
					.flatMap(List::stream).collect(Collectors.toList());
			
			Instant t3= Instant.now();
					
			System.out.println("Transfer Data units " + transferData.size());
			assertTrue(transferData.size()==2000+3);

			CfdpTransferData pdu = transferData.get(1);
			System.out.println("Transfer PDU length " + pdu.getData().length);
			assertTrue(pdu.getData().length == 16384 +12 );
			
			System.out.println("Frame generation: " + 8*2000*16.384/Duration.between(t0, t1).toMillis() + "Mbit/s");
			System.out.println("Reconstruct frames from bytes: " + 8*2000*16.384/Duration.between(t1, t2).toMillis() + "Mbit/s");
			System.out.println("Extract PDUs from frames: " + 8*2000*16.384/Duration.between(t2, t3).toMillis() + "Mbit/s");
		}
		catch (Exception ee)
		{
			ee.printStackTrace();
			assertTrue(false);
		}
	}
}
