package esa.egos.proxy.util;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.proxy.enums.TimeFormat;
import esa.egos.proxy.enums.TimeRes;

public interface ITime {

    /**
     * Sets CDS.
     * 
     * @param time
     * @throws ApiException
     */
    void setCDS(byte[] time) throws ApiException;

    /**
     * Gets CDS.
     * 
     * @return
     */
    byte[] getCDS();

    /**
     * Sets date and time.
     * 
     * @param dateAndTime
     * @throws ApiException
     */
    void setDateAndTime(String dateAndTime) throws ApiException;

    /**
     * Sets time.
     * 
     * @param time
     * @throws ApiException
     */
    void setTime(String time) throws ApiException;

    /**
     * Gets date.
     * 
     * @param fmt
     * @return
     */
    String getDate(TimeFormat fmt);

    /**
     * Gets time.
     * 
     * @param fmt
     * @param res
     * @return
     */
    String getTime(TimeFormat fmt, TimeRes res);

    /**
     * Gets data and time.
     * 
     * @param fmt
     * @param res
     * @return
     */
    String getDateAndTime(TimeFormat fmt, TimeRes res);

    /**
     * Gets data and time.
     * 
     * @param fmt
     * @return
     */
    String getDateAndTime(TimeFormat fmt);

    /**
     * Updates.
     */
    void update();

    /**
     * Copies the object.
     * 
     * @return
     */
    ITime copy();

    /**
     * Sets CDS to picoseconds.
     * 
     * @param time
     * @throws ApiException
     */
    void setCDSToPicosecondsRes(byte[] time) throws ApiException;

    /**
     * Gets CDS to picoseconds.
     * 
     * @return
     */
    byte[] getCDSToPicosecondsRes();

    /**
     * Gets used picoseconds.
     * 
     * @return
     */
    boolean getPicosecondsResUsed();

    /**
     * Subtraction from this the time object given as argument.
     * 
     * @param time
     * @return
     */
    double subtract(ITime time);

    /**
     * @param o
     * @return
     */
    @Override
    boolean equals(Object o);

    /**
     * @return
     */
    @Override
    int hashCode();

	int compareTo(ITime provisionPeriodStop);
	
}
