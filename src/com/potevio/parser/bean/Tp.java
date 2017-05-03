package com.potevio.parser.bean;

import java.nio.ByteBuffer;

public class Tp {
	private byte Pfc;//����֡������
	private byte SendSencond;//����ʱ��
	private byte SendMinute;//����ʱ��
	private byte SendHour;//����ʱ��
	private byte SendDay;//����ʱ��
	private byte SendTransDelay;//�����ʹ�����ʱʱ�䣨���ӣ�
	

	public Tp(ByteBuffer buffer) {
		// TODO Auto-generated constructor stub
		Pfc = buffer.get();
		byte temp = buffer.get();
		setSendSencond((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setSendMinute((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setSendHour((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		temp = buffer.get();
		setSendDay((byte) (((temp&0xF0)>>4)*10 + (temp&0xF)));
		SendTransDelay = buffer.get();
	}


	/**
	 * @return the pfc
	 */
	public byte getPfc() {
		return Pfc;
	}


	/**
	 * @param pfc the pfc to set
	 */
	public void setPfc(byte pfc) {
		Pfc = pfc;
	}


	/**
	 * @return the sendSencond
	 */
	public byte getSendSencond() {
		return SendSencond;
	}


	/**
	 * @param sendSencond the sendSencond to set
	 */
	public void setSendSencond(byte sendSencond) {
		SendSencond = sendSencond;
	}


	/**
	 * @return the sendMinute
	 */
	public byte getSendMinute() {
		return SendMinute;
	}


	/**
	 * @param sendMinute the sendMinute to set
	 */
	public void setSendMinute(byte sendMinute) {
		SendMinute = sendMinute;
	}


	/**
	 * @return the sendHour
	 */
	public byte getSendHour() {
		return SendHour;
	}


	/**
	 * @param sendHour the sendHour to set
	 */
	public void setSendHour(byte sendHour) {
		SendHour = sendHour;
	}


	/**
	 * @return the sendDay
	 */
	public byte getSendDay() {
		return SendDay;
	}


	/**
	 * @param sendDay the sendDay to set
	 */
	public void setSendDay(byte sendDay) {
		SendDay = sendDay;
	}


	/**
	 * @return the sendTransDelay
	 */
	public byte getSendTransDelay() {
		return SendTransDelay;
	}


	/**
	 * @param sendTransDelay the sendTransDelay to set
	 */
	public void setSendTransDelay(byte sendTransDelay) {
		SendTransDelay = sendTransDelay;
	}

}
