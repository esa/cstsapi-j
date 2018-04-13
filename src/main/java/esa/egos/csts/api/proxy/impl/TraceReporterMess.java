package esa.egos.csts.api.proxy.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.enums.Component;
import esa.egos.csts.api.enums.AlarmLevel;
import esa.egos.csts.api.enums.SLE_TraceLevel;
import esa.egos.csts.api.logging.CstsLogMessageType;

public class TraceReporterMess extends Message
{
    private static final Logger LOG = Logger.getLogger(TraceReporterMess.class.getName());

    private long messId;

    private SLE_TraceLevel level;

    private Component component;

    private CstsLogMessageType messType;

    private String sii;

    private String text;

    private AlarmLevel alarm;


    public TraceReporterMess(long messId,
                                   SLE_TraceLevel level,
                                   Component component,
                                   CstsLogMessageType messType,
                                   String sii,
                                   String text,
                                   AlarmLevel alarm)
    {
        this.messId = messId;
        this.level = level;
        this.component = component;
        this.messType = messType;
        this.sii = sii;
        this.text = text;
        this.alarm = alarm;
    }

    public TraceReporterMess()
    {
        this.level = SLE_TraceLevel.sleTL_invalid;
        this.component = Component.CP_invalid;
        this.messType = CstsLogMessageType.invalid;
        this.alarm = AlarmLevel.sleAL_invalid;
        this.sii = "";
        this.text = "";
    }

    public TraceReporterMess(byte[] data)
    {
        fromByteArray(data);
    }

    public long getMessId()
    {
        return this.messId;
    }

    public void setMessId(long messId)
    {
        this.messId = messId;
    }

    public SLE_TraceLevel getLevel()
    {
        return this.level;
    }

    public void setLevel(SLE_TraceLevel level)
    {
        this.level = level;
    }

    public Component getComponent()
    {
        return this.component;
    }

    public void setComponent(Component component)
    {
        this.component = component;
    }

    public CstsLogMessageType getMessType()
    {
        return this.messType;
    }

    public void setMessType(CstsLogMessageType messType)
    {
        this.messType = messType;
    }

    public String getSii()
    {
        return this.sii;
    }

    public void setSii(String sii)
    {
        this.sii = sii;
    }

    public String getText()
    {
        return this.text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public AlarmLevel getAlarm()
    {
        return this.alarm;
    }

    public void setAlarm(AlarmLevel alarm)
    {
        this.alarm = alarm;
    }

    @Override
    public byte[] toByteArray()
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            // set the messId
            dos.writeLong(this.messId);

            // set the trace level
            dos.writeInt(this.level.getCode());

            // set the component
            dos.writeInt(this.component.getCode());

            // set the message type
            dos.writeInt(this.messType.getCode());

            // set the sii
            dos.writeUTF(this.sii);

            // set the text
            dos.writeUTF(this.text);

            // set the alarm
            dos.writeInt(this.alarm.getCode());

            dos.flush();
        }
        catch (IOException e)
        {
            LOG.log(Level.FINE, "IOException ", e);
            return null;
        }

        return bos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] data)
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bis);

        try
        {
            // set the messId
            this.messId = dis.readLong();

            // set the trace level
            this.level = SLE_TraceLevel.getTraceLevelByCode(dis.readInt());

            // set the component
            this.component = Component.getComponentByCode(dis.readInt());

            // set the message type
            this.messType = CstsLogMessageType.getLogMessageByCode(dis.readInt());

            // set the sii
            this.sii = dis.readUTF();

            // set the text
            this.text = dis.readUTF();

            // set the alarm
            this.alarm = AlarmLevel.getAlarmByCode(dis.readInt());
        }
        catch (IOException e1)
        {
            LOG.log(Level.FINE, "IOException ", e1);
            return;
        }
    }

    @Override
    public String toString()
    {
        return "PXCS_TraceReporter Message[ messId = " + this.messId + ", level = " + this.level + ", component = "
               + this.component + ", messType = " + this.messType + ", sii = " + this.sii + ", text = " + this.text
               + ", alarm =" + this.alarm + "]";
    }
}
