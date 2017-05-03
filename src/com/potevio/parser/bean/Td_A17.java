/**
 * 
 */
package com.potevio.parser.bean;

import java.nio.ByteBuffer;

/**
 * @author wangy1
 *
 */
public final class Td_A17 {
	private byte minute;
	private byte hour;
	private byte day;
	private byte month;
	
	public Td_A17(ByteBuffer buffer){
		byte temp = buffer.get();
		setMinute((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setHour((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setDay((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setMonth((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
	}

	/**
	 * @return the minute
	 */
	public byte getMinute() {
		return minute;
	}

	/**
	 * @param minute the minute to set
	 */
	public void setMinute(byte minute) {
		this.minute = minute;
	}

	/**
	 * @return the hour
	 */
	public byte getHour() {
		return hour;
	}

	/**
	 * @param hour the hour to set
	 */
	public void setHour(byte hour) {
		this.hour = hour;
	}

	/**
	 * @return the day
	 */
	public byte getDay() {
		return day;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(byte day) {
		this.day = day;
	}

	/**
	 * @return the month
	 */
	public byte getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(byte month) {
		this.month = month;
	}

}
