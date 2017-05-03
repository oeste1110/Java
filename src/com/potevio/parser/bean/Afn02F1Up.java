package com.potevio.parser.bean;

import java.nio.ByteBuffer;

public final class Afn02F1Up extends FnBase {

	public Afn02F1Up(ByteBuffer buffer, String Pn, String Fn) {
		super(Pn, Fn);
		// TODO Auto-generated constructor stub
	}
	public byte [] ToBytes(){
		byte[] Temp = super.ToBytes();
		return Temp;
	}

}
