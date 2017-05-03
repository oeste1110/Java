package com.potevio.parser.bean;

import java.nio.ByteBuffer;

public final class Afn0cF2Up extends FnBase {
	private byte second;
	private byte minute;
	private byte hour;
	private byte day;
	private byte week;
	private byte month;
	private byte year;

	public Afn0cF2Up(ByteBuffer buffer, String Pn, String Fn) {
		super(Pn, Fn);
		// TODO Auto-generated constructor stub
		byte temp = buffer.get();
		setSecond((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setMinute((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setHour((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setDay((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		
		temp = buffer.get();
		setWeek((byte) ((temp&0xE0)>>5));
		setMonth((byte) (((temp&0x10)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setYear((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));	
	}
	public byte [] ToBytes(){
		byte[] Result = new byte[10];
		byte[] Temp = super.ToBytes();
		System.arraycopy(Temp,0,Result,0,Temp.length);
		
		byte Templ = (byte) (this.second%10);
		byte Temph = (byte) (this.second/10);
		Result[4] = (byte) (Temph<<4|Templ);
		Templ = (byte) (this.minute%10);
		Temph = (byte) (this.minute/10);
		Result[5] = (byte) (Temph<<4|Templ);
		Templ = (byte) (this.hour%10);
		Temph = (byte) (this.hour/10);
		Result[6] = (byte) (Temph<<4|Templ);
		Templ = (byte) (this.day%10);
		Temph = (byte) (this.day/10);
		Result[7] = (byte) (Temph<<4|Templ);
		
		Templ = (byte) (this.month%10);
		Temph = (byte) (this.month/10);
		Result[8] = (byte) (this.week<<5|Temph<<4|Templ);
		Templ = (byte) (this.year%10);
		Temph = (byte) (this.year/10);
		Result[9] = (byte) (Temph<<4|Templ);
		return Result;
	}
	/**
	 * @return the second
	 */
	public byte getSecond() {
		return second;
	}
	/**
	 * @param second the second to set
	 */
	public void setSecond(byte second) {
		this.second = second;
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
	 * @return the week
	 */
	public byte getWeek() {
		return week;
	}
	/**
	 * @param week the week to set
	 */
	public void setWeek(byte week) {
		this.week = week;
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
