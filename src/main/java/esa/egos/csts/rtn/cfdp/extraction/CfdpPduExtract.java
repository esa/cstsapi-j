package esa.egos.csts.rtn.cfdp.extraction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import esa.egos.csts.rtn.cfdp.ccsds.frames.CcsdsTmFrame;
import esa.egos.csts.rtn.cfdp.ccsds.frames.CcsdsTmPacket;
import esa.egos.csts.rtn.cfdp.ccsds.frames.FrameConfigData;
import esa.egos.csts.rtn.cfdp.ccsds.frames.IUtFrameElementMonitor;
import esa.egos.csts.rtn.cfdp.ccsds.frames.PacketConfigData;
import esa.egos.csts.rtn.cfdp.ccsds.frames.Packetizer;
import esa.egos.csts.rtn.cfdp.reduction.CfdpTransferData;


public class CfdpPduExtract {

	private static Logger logger = Logger.getLogger(CfdpPduExtract.class.getName());

	private static IUtFrameElementMonitor monitor = new IUtFrameElementMonitor() {

		@Override
		public void updateExtendedFrameDropCount(long lCurExtFrmCnt, long lPrvExtFrmCnt) {
			// TODO Auto-generated method stub

		}

		@Override
		public void incPacketsDiscarded() {
			// TODO Auto-generated method stub

		}

		@Override
		public void incPacketCount() {
			// TODO Auto-generated method stub

		}

		@Override
		public void incFrameGapCount() {
			// TODO Auto-generated method stub

		}

		@Override
		public long getPacketCount() {
			// TODO Auto-generated method stub
			return 0;
		}
	};

	private final FrameConfigData mOutFrmConfigData;


	private final PacketConfigData mOutPktConfigData;
	
	private final Packetizer packetizer;
	
	
	public CfdpPduExtract(FrameConfigData frameConfigData,PacketConfigData packetConfigData )
	{
		mOutFrmConfigData = frameConfigData;
		mOutPktConfigData = packetConfigData;
		packetizer = new Packetizer(mOutFrmConfigData.GetOutputVC(),logger,monitor);
	}
	
	public CcsdsTmFrame reconstructTmFrames(byte[] extractData)
	{
		CcsdsTmFrame frame = new CcsdsTmFrame(mOutFrmConfigData, mOutPktConfigData);
		frame.InitTmFrameFromByteArray(extractData);
		return frame;
	}

	public List<CfdpTransferData> extractTransferData(CcsdsTmFrame frame) 
	{
		List<CfdpTransferData> transferData = new ArrayList<>();
			
			List<CcsdsTmPacket> packets = packetizer.ProcessFrame(frame);
			
			for(CcsdsTmPacket packet: packets)
			{
				transferData.add(new CfdpTransferData(packet.GetUserData()));
			}
		return transferData;

	}

}
