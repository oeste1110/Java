package com.potevio.parser.bean;

import java.nio.ByteBuffer;

public class Td_A15 {
	private byte minute;
	private byte hour;
	private byte day;
	private byte month;
	private byte year;

	public Td_A15(ByteBuffer buffer) {
		// TODO Auto-generated constructor stub
		byte temp = buffer.get();
		setMinute((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setHour((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setDay((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setMonth((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setYear((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
	}
	
	public byte [] ToBytes(){
		byte[] Result = new byte[5];
		byte Templ = (byte) (this.minute%10);
		byte Temph = (byte) (this.minute/10);
		Result[0] = (byte) (Temph<<4|Templ);
		Templ = (byte) (this.hour%10);
		Temph = (byte) (this.hour/10);
		Result[1] = (byte) (Temph<<4|Templ);
		Templ = (byte) (this.day%10);
		Temph = (byte) (this.day/10);
		Result[2] = (byte) (Temph<<4|Templ);
		Templ = (byte) (this.month%10);
		Temph = (byte) (this.month/10);
		Result[3] = (byte) (Temph<<4|Templ);
		Templ = (byte) (this.year%10);
		Temph = (byte) (this.year/10);
		Result[4] = (byte) (Temph<<4|Templ);
		return Result;
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

	/**
	 * @return the year
	 */
	public byte getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(byte year) {
		this.year = year;
	}

}
