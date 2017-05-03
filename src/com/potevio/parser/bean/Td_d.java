package com.potevio.parser.bean;

import java.nio.ByteBuffer;

import com.google.gson.Gson;

public final class Td_d {
	private byte day;
	private byte month;
	private byte year;
	public Td_d(ByteBuffer buffer){
		byte temp = buffer.get();
		setDay((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setMonth((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setYear((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
	}
	public String ToJson(){
		Gson gson=new Gson();
		String json=gson.toJson(this);
    	return json;
    }
    public byte[] ToBytes(){
    	byte[] Result = new byte[3];
		byte Templ = (byte) (this.day%10);
		byte Temph = (byte) (this.day/10);
		Result[0] = (byte) (Temph<<4|Templ);
		Templ = (byte) (this.month%10);
		Temph = (byte) (this.month/10);
		Result[1] = (byte) (Temph<<4|Templ);
		Templ = (byte) (this.year%10);
		Temph = (byte) (this.year/10);
		Result[2] = (byte) (Temph<<4|Templ);
		return Result;
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
