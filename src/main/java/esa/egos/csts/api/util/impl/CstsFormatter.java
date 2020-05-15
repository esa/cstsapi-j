package esa.egos.csts.api.util.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CstsFormatter extends Formatter
{
    private static final DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");

    private static final int INIT_BUFF_SIZE = 1024;

    public String format(LogRecord record)
    {
        StringBuilder sb = new StringBuilder(INIT_BUFF_SIZE);
        sb.append(df.format(new Date(record.getMillis())));
        sb.append(" [").append(Thread.currentThread().getName());
        sb.append("][").append(record.getLevel().getName()).append("] ");
        sb.append(getSimpleClassName(record.getSourceClassName())).append(".");
        sb.append(record.getSourceMethodName()).append(' ');
        sb.append(formatMessage(record)).append('\n');
        return sb.toString();
    }

    private String getSimpleClassName(String name)
    {
        return name.substring(name.lastIndexOf('.') + 1);
    }
}
