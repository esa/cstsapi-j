//-----------------------------------------------------------------------------
//
// (C) 2018 European Space Agency
// European Space Operations Centre
// Darmstadt, Germany
//
//-----------------------------------------------------------------------------
//
// System : CFDP Assembly
//
// Sub-System : esa.egos.cfdpass.ut_frame
//
// File Name : Packetizer.java
//
// Author : Felix Flentge
//
// Creation Date : 18-Jan-2018
//
//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
package esa.egos.csts.rtn.cfdp.ccsds.frames;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.rtn.cfdp.ccsds.frames.CcsdsTmFrame.TrFrameFECWStatus;
import esa.egos.csts.rtn.cfdp.ccsds.frames.CcsdsTmPacket.TmPacketStatus;
import esa.egos.csts.rtn.cfdp.ccsds.frames.CcsdsTmPacket.TmPktPECWStatus;


public class Packetizer
{

    private final byte mVC;

    private short mVCFrmCnt = -1;

    private Long mExtendedVCFrmCnt = null;

    private CcsdsTmPacket mOverspillPkt = null;

    // receveive logger
    private final Logger log;

    private final IUtFrameElementMonitor monitor;


    // private FrameSendUtElement frameSendUtElement = new FrameSendUtElement();

    public Packetizer(byte bVC, Logger l, IUtFrameElementMonitor monitor)
    {
        this.mVC = bVC;
        this.log = l;
        this.monitor = monitor;
    }

    public List<CcsdsTmPacket> ProcessFrame(CcsdsTmFrame tmFrame)
    {

        CcsdsTmPacket tmPacket;
        boolean BStatus;
        int iFECW;
        int iPECW;

        List<CcsdsTmPacket> tmPktList = new LinkedList<CcsdsTmPacket>();

        if (tmFrame.GetVCId() != this.mVC)
        {
            this.log.log(Level.SEVERE, "Frame UT Element: ProcessFrame for VC " + this.mVC
                                       + " - Called with unexpected VC " + tmFrame.GetVCId());
            return new LinkedList<CcsdsTmPacket>(); // CFDPASS-378
        }

        // Check FECW okay, if one present and if not log error and discard
        // frame;
        if ((tmFrame.GetFECWStatus() == TrFrameFECWStatus.BadFECW)
            || (tmFrame.GetFECWStatus() == TrFrameFECWStatus.Unknown))
        {
            // FECW Present, calculate FECW, log error message and drop frame.

            iFECW = tmFrame.CalculateFECW();
            this.log.log(Level.SEVERE, "Frame UT Element: ProcessFrame for VC " + this.mVC
                                       + " - invalid FECW, expected = " + tmFrame.GetFECW() + " actual = " + iFECW
                                       + " - Frame Discarded");

            return new LinkedList<CcsdsTmPacket>(); // CFDPASS-378
        }
        // Check if any previous frames have been received, if there were check
        // VC Frame counter is continuous (mod 256) with the
        // previous value.

        if (this.mVCFrmCnt != -1)
        {
            // Previous frame received, check mod 256 that the VC frame counter
            // is continuous with that received previously. If
            // not log warning and discard any packet overspill.

            if ((this.mVCFrmCnt + 1) != tmFrame.GetVCFrmCnt())
            {
                if ((this.mVCFrmCnt + 1) % 256 != tmFrame.GetVCFrmCnt())
                {
                    // Log warning and discard any existing packet overspill.
                    if (this.log.isLoggable(Level.FINE))
                    {
                        this.log.log(Level.FINE, "Frame UT Element: ProcessFrame for VC " + this.mVC
                                                 + " - gap in VC frame counter detected, previous = " + this.mVCFrmCnt
                                                 + ", current = " + tmFrame.GetVCFrmCnt());
                    }
                    // Discard any existing packet overspill
                    this.monitor.incFrameGapCount(); // no statement about gap
                                                     // size, it is ambigous
                                                     // anyway. Just flag the
                                                     // gap
                    this.mOverspillPkt = null;
                }
            }
        }
        // Set VC Frame counter to current value.

        this.mVCFrmCnt = tmFrame.GetVCFrmCnt();

        // Check if extended VC frame count is present (i.e. the current frame
        // has a non-null value of the extended frame count)
        if (tmFrame.GetExtendedVCFrmCnt() != null)
        {
            // Check if any previous frames have been received, if there were
            // check extended VC Frame counter is continuous (mod 0x100000000)
            // with the previous value.

            if (this.mExtendedVCFrmCnt != null)
            {
                // Previous frame received, check mod 0x100000000 that the VC
                // frame counter is continuous with that received previously. If
                // not log warning and discard any packet overspill.

                if ((this.mExtendedVCFrmCnt + 1) != tmFrame.GetExtendedVCFrmCnt())
                {
                    if ((this.mExtendedVCFrmCnt + 1) % 0x100000000l != tmFrame.GetVCFrmCnt())
                    {
                        // Log warning and discard any existing packet
                        // overspill.
                        if (this.log.isLoggable(Level.FINE))
                        {
                            this.log.log(Level.FINE,
                                         "Frame UT Element: ProcessFrame for VC " + this.mVC
                                                 + " - gap in Extended VC frame counter detected, previous = "
                                                 + this.mExtendedVCFrmCnt + ", current = "
                                                 + tmFrame.GetExtendedVCFrmCnt());
                        }
                        // Discard any existing packet overspill
                        this.monitor
                                .updateExtendedFrameDropCount(tmFrame.GetExtendedVCFrmCnt(), this.mExtendedVCFrmCnt); // As
                        // the
                        // extended
                        // fame
                        // count
                        // is
                        // 32
                        // bits
                        // long
                        // we
                        // can
                        // use
                        // it
                        // to
                        // calculate
                        // frame
                        // loss.
                        this.mOverspillPkt = null;
                    }

                }
            }
            // Set Extended VC Frame counter to current value.

            this.mExtendedVCFrmCnt = tmFrame.GetExtendedVCFrmCnt();

            if (this.log.isLoggable(Level.FINER))
            {
                System.out.println("<<<<<<<<<<  VC Frm Cnt=" + this.mVCFrmCnt + ";   Extended VC Frm Cnt="
                                   + this.mExtendedVCFrmCnt + "  >>>>>>>>>>>");
            }

        }

        // Now check if there is a current packet overspill. If there is and the
        // first header pointer is 0 this indicates that part
        // of the packet has been lost.

        if ((this.mOverspillPkt != null) && (tmFrame.GetFrstHdrPtr() == 0))
        {
            // Log error and discard current packet overspill.

            this.log.log(Level.FINE, "Frame UT Element: ProcessFrame for VC " + this.mVC + " - first header pointer 0 "
                                     + "when expecting remainder of overspill packet. Overspill packet data Discarded.");

            // Discard any existing packet overspill
            this.monitor.incPacketsDiscarded();
            this.mOverspillPkt = null;
        }

        // Get first packet (or first packet part) in frame.

        tmPacket = tmFrame.GetFirstPkt();

        while (tmPacket != null)
        {
            // Process all packets in frame. Check if what was extracted was a
            // partial packet. If it was and it is a segment or
            // trailer partial packet check if there is a current overspill
            // packet to which this belongs, if not log a warning
            // and discard the data.

            if ((this.mOverspillPkt == null)
                && ((tmPacket.GetTmPacketStatus() == TmPacketStatus.Segment) || (tmPacket.GetTmPacketStatus() == TmPacketStatus.Trailer)))
            {

                this.log.log(Level.FINE, "Frame UT Element: ProcessFrame for VC " + this.mVC
                                         + " - Packet segement received with no "
                                         + "corresponding header. Data discarded.");
                this.monitor.incPacketsDiscarded();
            }
            else if (tmPacket.GetTmPacketStatus() == TmPacketStatus.Leader)
            {
                // Leader part of packet received. Move it to overspill;

                this.mOverspillPkt = tmPacket;

            }
            else if ((tmPacket.GetTmPacketStatus() == TmPacketStatus.Segment)
                     || (tmPacket.GetTmPacketStatus() == TmPacketStatus.Trailer))
            {
                // Partial packet segment or trailer received, append it to the
                // current overspill packet.

                BStatus = this.mOverspillPkt.ConcatPartialPkt(tmPacket);

                if (!BStatus)
                {
                    this.log.log(Level.SEVERE, "Frame UT Element: ProcessFrame for VC " + this.mVC
                                               + " - Error concatenating packet segements or trailer");
                }
                // Check if packet now complete and if so move it from overspill
                // to tm packaet and set overspill packet to null.

                if (this.mOverspillPkt.GetTmPacketStatus() == TmPacketStatus.Complete)
                {
                    // Packet complete

                    tmPacket = this.mOverspillPkt;
                    this.mOverspillPkt = null;
                }
            }
            // If its a complete packet then add it to the list.

            if (tmPacket.GetTmPacketStatus() == TmPacketStatus.Complete)
            {
                // Complete packet, check PECW if present.

                if ((tmPacket.GetPECWStatus() == TmPktPECWStatus.BadPECW)
                    || (tmPacket.GetPECWStatus() == TmPktPECWStatus.Unknown))
                {
                    // Invalid PECW Present, calculate PECW, log error message
                    // and drop packet.

                    iPECW = tmFrame.CalculateFECW();
                    this.log.log(Level.SEVERE, "Frame UT Element: ProcessFrame for VC " + this.mVC
                                               + " - invalid PECW for APID = " + tmPacket.GetAPID() + " expected = "
                                               + tmFrame.GetFECW() + " actual = " + iPECW + " - Packet Discarded");
                    this.monitor.incPacketsDiscarded();
                }
                else
                {
                    //this.monitor.incPacketCount(); // CFDPASS-400
                    tmPktList.add(tmPacket);
                }
            }
            // Get Next packet

            tmPacket = tmFrame.GetNextPkt();
        }
        return tmPktList;
    }
}
