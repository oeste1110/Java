/**
 * 
 */
package com.potevio.parser.bean;

import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.Vector;

import com.google.gson.Gson;

/**
 * @author wangy1
 *
 */
public class PacketBase {

	/**
	 * 
	 */
	private byte Dir;
	private byte Prm;
	private byte FcbOrAcd;
	private byte Fcv;
	private byte FuncCode;
	
	private short RegionalismCode;
    private short TerminalEq;
    private byte HostAndEqAddress;
    private byte Afn;
    
    private byte Tpv;
    private byte Fir;
    private byte Fin;
    private byte Con;
    private byte Seq;
    
    Vector<FnBase> FVector;
    private EC Ec;
    private Tp Tp;
	
	public PacketBase(byte [] Data) {
		// TODO Auto-generated constructor stub
		short Temp = 0;
		byte bTemp = 0;
		byte TempH = 0;
		byte TempL = 0;
		ByteBuffer buffer = ByteBuffer.allocate(Data.length); 
		buffer=ByteBuffer.wrap(Data);
		buffer.position(6); 
		bTemp = buffer.get();
		Dir = (byte) ((bTemp&0x80)>>7);
	    Prm = (byte) ((bTemp&0x40)>>6);
	    FcbOrAcd = (byte) ((bTemp&0x20)>>5);
	    Fcv = (byte) ((bTemp&0x10)>>4);
	    FuncCode = (byte) (bTemp&0xf);
	    TempL = buffer.get();
	    TempH = buffer.get();
	    RegionalismCode = (short) (TempL|((short)TempH<<8));
	    TempL = buffer.get();
	    TempH = buffer.get();
	    TerminalEq = (short) (TempL|((short)TempH<<8));
	    HostAndEqAddress = buffer.get();
	    Afn = buffer.get();
	    bTemp = buffer.get();
	    Tpv = (byte) ((bTemp&0x80)>>7);
	    Fir = (byte) ((bTemp&0x40)>>6);
	    Fin = (byte) ((bTemp&0x20)>>5);
	    Con = (byte) ((bTemp&0x10)>>4);
	    Seq = (byte) (bTemp&0xf);
	    
	    FVector = new Vector<FnBase>(1,1);
	    byte Mantissa = 2;
	    if((this.Dir == 1)&&(this.FcbOrAcd == 1)){
	    	Mantissa = 8;
	    }
	    
	    if (this.Tpv == 1){
	    	Mantissa = 12;
	    }
	    for(;;){
	    	byte result = ObjectParse(buffer,Afn, Dir);
	    	if(((buffer.limit() - buffer.position()) <= Mantissa)||(1 == result)){
	    		break;
	    	}
	    }
	    if((this.Dir == 1)&&(this.FcbOrAcd == 1)){
	    	Ec = new EC(buffer);
	    }
	    
	    if (this.Tpv == 1){
	    	Tp = new Tp(buffer);
	    }	    
	}

	public Vector<FnBase> getFVector()
	{
		return FVector;
	}

	public PacketBase(byte [] Data, byte afn, String Fn) {
		short Temp = 0;
		byte bTemp = 0;
		byte TempH = 0;
		byte TempL = 0;
		Dir = 0;
	    Prm = 0;
	    FcbOrAcd = 0;
	    Fcv = 0;
	    FuncCode = 0xb;
	    TempL = Data[7];
	    TempH = Data[8];
	    RegionalismCode = (short) (TempL|((short)TempH<<8));
	    TempL = Data[9];
	    TempH = Data[10];
	    TerminalEq = (short) (TempL|((short)TempH<<8));
	    HostAndEqAddress = Data[11];
	    Afn = 0;
	    bTemp = Data[13];
	    Tpv = 0;
	    Fir = 1;
	    Fin = 1;
	    Con = 1;
	    Seq = (byte) (bTemp&0xf);
	    
	    FVector = new Vector<FnBase>(1,1);
	    Afn00F1Dl Confirm = new Afn00F1Dl("P0", "F1");
		FVector.add(Confirm);

	}
	//����ת����json
	public String ToJson(){
		String json = null;
		Gson gson=new Gson();
		json = gson.toJson(this);
		//for (int i = 0; i < FVector.size(); i++){
		//	json += FVector.elementAt(i).ToJson();
		//}
		//String json=gson.toJson(FVector);
		//json = json + gson.toJson(FVector);
    	return json;
    }
	public byte[] ToBytes(){
		Vector<byte[]> data = new Vector(1,1);
		int Len = 0;
		int LenTemp = 0;
		//.... 
		
		byte[] Head = this.HeadToBytes();
		Len +=Head.length;
		data.add(Head);
		for (int i = 0; i < FVector.size(); i++){
			byte[] VectorData = FVector.elementAt(i).ToBytes();
			Len +=VectorData.length;
			data.add(VectorData);
		}
		byte[] Result = new byte[Len+2];
		for (int i = 0; i < data.size(); i++){
			System.arraycopy(data.elementAt(i),0,Result,LenTemp,data.elementAt(i).length);
			LenTemp +=data.elementAt(i).length;
		}
		short DataLen = (short) (((Len-6)<<2)|0x2);
		Result[1] = (byte) (DataLen&0x00ff);
		Result[2] = (byte) ((DataLen&0xff00)>>8);
		Result[3] = Result[1];
		Result[4] = Result[2];

		for (int Loop = 6; Loop <Len; Loop ++){

			Result[Len] += Result[Loop];
		}
		Result[Len+1] = 0x16;
		return Result;
	}
	private byte[] HeadToBytes(){
		byte[] Result = new byte[14];
		byte Temp = 0;
		Result[0] = 0x68;
		Result[5] = 0x68;
		Temp = (byte) ((this.Dir<<7)|(this.Prm<<6)|(this.FcbOrAcd<<5)|
				(this.Fcv<<4)|(this.FuncCode));
		Result[6] = Temp;
		Temp = 0;
		Result[7] = (byte) (this.RegionalismCode&0x00ff);
		Result[8] = (byte) ((this.RegionalismCode&0xff00)>>8);
		Result[9] = (byte) (this.TerminalEq&0x00ff);
		Result[10] = (byte) ((this.TerminalEq&0xff00)>>8);
		Result[11] = this.HostAndEqAddress;
		Result[12] = this.Afn;
		Temp = (byte) ((this.Tpv<<7)|(this.Fir<<6)|(this.Fin<<5)|
				(this.Con<<4)|(this.Seq));
		Result[13] = Temp;
		return Result;
	}
	private byte ObjectParse(ByteBuffer buffer, byte Afn, byte flag) {
		// TODO Auto-generated method stub
		byte Loop = 0;
		byte Da1 = buffer.get();
		byte Da2 = buffer.get();
		byte Dt1 = buffer.get();
		byte Dt2 = buffer.get();
		int Result = 0;
		String Fn = new String("F"); 
		String Pn = new String("P");
	    
		
		for(Loop = 0; Loop < 8; Loop++){
			if (0 != (Dt1&(0x1<<Loop))){
				Result = (Dt2 * 8) + (Loop +1);
				break;
			}
		}
		Fn = Fn + String.valueOf(Result);
		if((0 == Da1)&&(0 == Da2)){
			Pn = Pn + String.valueOf("0");
			if ((0 == Afn)&&(0==Fn.compareTo("F1"))&&(Dir == 0)){
				Afn00F1Dl Confirm = new Afn00F1Dl(Pn, Fn);
				FVector.add(Confirm);
			}
			else if((2 == Afn)&&(0==Fn.compareTo("F1"))&&(Dir == 1)){
				Afn02F1Up Login = new Afn02F1Up(buffer, Pn,Fn);
				FVector.add(Login);
			}
			else if((2 == Afn)&&(0==Fn.compareTo("F3"))&&(Dir == 1)){
				Afn02F3Up HeartBeat = new Afn02F3Up(buffer, Pn,Fn);
				FVector.add(HeartBeat);
			}
			else if((0xc == Afn)&&(0==Fn.compareTo("F2"))&&(Dir == 1)){
				Afn0cF2Up req = new Afn0cF2Up(buffer, Pn,Fn);
				FVector.add(req);
			}
			else if((0xc == Afn)&&(0==Fn.compareTo("F2"))&&(Dir == 0)){
				Afn0cF2Dl cnf = new Afn0cF2Dl(Pn,Fn);
				FVector.add(cnf);
			}
			else{
				return 1;
			}
		}
		else if((0xFF == Da1)&&(0 == Da2)){
			
		}
		else{
			for(Loop = 0; Loop < 8; Loop++){
				if (0 != (Da1&(1<<Loop))){
					Result = ((Da2 - 1) * 8) + (Loop +1);
					Pn = Pn + String.valueOf(Result);
					if ((0 == Afn)&&(0==Fn.compareTo("F1"))&&(Dir == 0)){
						Afn00F1Dl Confirm = new Afn00F1Dl(Pn, Fn);
						FVector.add(Confirm);
					}
					else if((2 == Afn)&&(0==Fn.compareTo("F1"))&&(Dir == 1)){
						Afn02F1Up Login = new Afn02F1Up(buffer, Pn,Fn);
						FVector.add(Login);
					}
					else if((2 == Afn)&&(0==Fn.compareTo("F3"))&&(Dir == 1)){
						Afn02F3Up HeartBeat = new Afn02F3Up(buffer, Pn,Fn);
						FVector.add(HeartBeat);
					}
					else if((0xc == Afn)&&(0==Fn.compareTo("F2"))&&(Dir == 1)){
						Afn0cF2Up req = new Afn0cF2Up(buffer, Pn,Fn);
						FVector.add(req);
					}
					else if((0xc == Afn)&&(0==Fn.compareTo("F2"))&&(Dir == 0)){
						Afn0cF2Dl cnf = new Afn0cF2Dl(Pn,Fn);
						FVector.add(cnf);
					}
					else{
						return 1;
					}
					/*Afn0dF161Up DFPAPower = new Afn0dF161Up(buffer, Pn, Fn);
					FVector.add(DFPAPower);*/
				}
			}
		}
		return 0;
//
		
	}

	public void putVector(FnBase FnObject){
		
	}
	
	public byte getDir() {
		return Dir;
	}

	public void setDir(byte dir) {
		Dir = dir;
	}


	public byte getPrm() {
		return Prm;
	}


	public void setPrm(byte prm) {
		Prm = prm;
	}


	public byte getFcbOrAcd() {
		return FcbOrAcd;
	}


	public void setFcbOrAcd(byte fcbOrAcd) {
		FcbOrAcd = fcbOrAcd;
	}


	public byte getFcv() {
		return Fcv;
	}


	public void setFcv(byte fcv) {
		Fcv = fcv;
	}


	/**
	 * @return the regionalismCode
	 */
	public short getRegionalismCode() {
		return RegionalismCode;
	}


	/**
	 * @param regionalismCode the regionalismCode to set
	 */
	public void setRegionalismCode(short regionalismCode) {
		RegionalismCode = regionalismCode;
	}


	/**
	 * @return the terminalEq
	 */
	public short getTerminalEq() {
		return TerminalEq;
	}


	/**
	 * @param terminalEq the terminalEq to set
	 */
	public void setTerminalEq(short terminalEq) {
		TerminalEq = terminalEq;
	}


	/**
	 * @return the funcCode
	 */
	public byte getFuncCode() {
		return FuncCode;
	}


	/**
	 * @param funcCode the funcCode to set
	 */
	public void setFuncCode(byte funcCode) {
		FuncCode = funcCode;
	}


	/**
	 * @return the hostAndEqAddress
	 */
	public byte getHostAndEqAddress() {
		return HostAndEqAddress;
	}


	/**
	 * @param hostAndEqAddress the hostAndEqAddress to set
	 */
	public void setHostAndEqAddress(byte hostAndEqAddress) {
		HostAndEqAddress = hostAndEqAddress;
	}


	/**
	 * @return the afn
	 */
	public byte getAfn() {
		return Afn;
	}


	/**
	 * @param afn the afn to set
	 */
	public void setAfn(byte afn) {
		Afn = afn;
	}


	/**
	 * @return the tpv
	 */
	public byte getTpv() {
		return Tpv;
	}


	/**
	 * @param tpv the tpv to set
	 */
	public void setTpvs(byte tpv) {
		Tpv = tpv;
	}


	/**
	 * @return the fir
	 */
	public byte getFir() {
		return Fir;
	}


	/**
	 * @param fir the fir to set
	 */
	public void setFir(byte fir) {
		Fir = fir;
	}


	/**
	 * @return the fin
	 */
	public byte getFin() {
		return Fin;
	}


	/**
	 * @param fin the fin to set
	 */
	public void setFin(byte fin) {
		Fin = fin;
	}


	/**
	 * @return the con
	 */
	public byte getCon() {
		return Con;
	}


	/**
	 * @param con the con to set
	 */
	public void setCon(byte con) {
		Con = con;
	}


	/**
	 * @return the seq
	 */
	public byte getSeq() {
		return Seq;
	}


	/**
	 * @param seq the seq to set
	 */
	public void setSeq(byte seq) {
		Seq = seq;
	}

}
