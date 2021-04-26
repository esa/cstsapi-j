package esa.egos.csts.rtn.cfdp.ccsds.frames;

public interface IUtFrameElementMonitor {

	public abstract long getPacketCount();

	public abstract void incPacketCount();

	public abstract void incPacketsDiscarded();

	public abstract void incFrameGapCount();

	public abstract void updateExtendedFrameDropCount(long lCurExtFrmCnt,
			long lPrvExtFrmCnt);

}