package com.potevio.parser.bean;

import java.nio.ByteBuffer;

public final class EC {
	private byte EC1;//��Ҫ�¼�����
	private byte EC2;//һ���¼�����

	public EC(ByteBuffer buffer) {
		// TODO Auto-generated constructor stub
		EC1 = buffer.get();
		EC2 = buffer.get();
	}

	/**
	 * @return the eC2
	 */
	public byte getEC2() {
		return EC2;
	}

	/**
	 * @param eC2 the eC2 to set
	 */
	public void setEC2(byte eC2) {
		EC2 = eC2;
	}

	/**
	 * @return the eC1
	 */
	public byte getEC1() {
		return EC1;
	}

	/**
	 * @param eC1 the eC1 to set
	 */
	public void setEC1(byte eC1) {
		EC1 = eC1;
	}

}
