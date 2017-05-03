package com.potevio.parser.bean;

import java.nio.ByteBuffer;

import com.google.gson.Gson;

public class Afn0dF161Up extends FnBase  {
	private Td_d DFPAPowerTime;
	private Td_A15 TerminalReadingTime;
	byte DFPowerM = 0;
	double PaPowerToatl = 0.0;
	double [] PaPower;

	public Afn0dF161Up(ByteBuffer buffer, String Pn, String Fn) {
		// TODO Auto-generated constructor stub
		super(Pn, Fn);
		DFPAPowerTime = new Td_d(buffer);
		TerminalReadingTime = new Td_A15(buffer);
		DFPowerM = buffer.get();
		PaPower = new double[DFPowerM];
		PaPowerToatl = A14ToDouble(buffer);
		for (byte Loop = 0; Loop < DFPowerM; Loop++){
			PaPower[Loop] = A14ToDouble(buffer);
		}
	}
	public String ToJson(){
		Gson gson=new Gson();
		String json = super.ToJson();
		json += gson.toJson(this);
    	return json;
    }
	public byte [] ToBytes(){
		byte[] Result = new byte[4+9+5*(DFPowerM+1)];
		byte[] Temp = super.ToBytes();
		System.arraycopy(Temp,0,Result,0,Temp.length);
		Temp = DFPAPowerTime.ToBytes();
		System.arraycopy(Temp,0,Result,4,Temp.length);
		Temp = TerminalReadingTime.ToBytes();
		System.arraycopy(Temp,0,Result,7,Temp.length);
		Result[12] = DFPowerM;
		Temp = DoubleToA14(PaPowerToatl);
		System.arraycopy(Temp,0,Result,13,Temp.length);
		for (byte Loop = 0; Loop < DFPowerM; Loop++){
			Temp = DoubleToA14(PaPower[Loop]);
			System.arraycopy(Temp,0,Result,(18+Loop*5),Temp.length);
		}
		return Result;
	}
	private byte[] DoubleToA14(double Pa){
		byte[] Temp = new byte[5];
		double Data = Pa * 10000;
		byte Temph  = 0;
		byte Templ = 0;
		for (byte Loop = 0; Loop<5; Loop++){
			Templ = (byte) (Data%10);
			Data = Data/10;
			Temph = (byte) (Data%10);
			Data = Data/10;
			Temp[Loop] = (byte) ((Temph<<4)|Templ);
		}
		return Temp;
	}
	private double A14ToDouble(ByteBuffer buffer){
		double Dtemp = 0;
		byte Btemp;
		for(byte Loop = 2; Loop > 0; Loop--){
			Btemp = buffer.get();
			Dtemp += (Btemp&0xf)/Math.pow(10,(Loop*2));
			Dtemp += ((Btemp&0xf0)>>4)/Math.pow(10,(Loop*2-1));
		}
		for(byte Loop = 0; Loop < 3; Loop++){
			Btemp = buffer.get();
			Dtemp += (Btemp&0xf)*Math.pow(10,(Loop*2));
			Dtemp += ((Btemp&0xf0)>>4)*Math.pow(10,(Loop*2+1));
		}
		return Dtemp;
	}

}
