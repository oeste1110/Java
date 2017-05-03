package com.potevio.parser.bean;

import com.google.gson.Gson;

public class FnBase {
    private String Pn;
    private String Fn;
    
    public FnBase(String Pn1, String Fn1){
    	setPn(Pn1);
    	setFn(Fn1);
    }
    
    public String ToJson(){
		Gson gson=new Gson();
		String json=gson.toJson(this);
    	return json;
    }
    public byte[] ToBytes(){
    	byte[] Result = new byte[4];
    	String Temp = Fn.substring(1);
        int Fn1 = Integer.parseInt(Temp);
        Result[3] = (byte) (Fn1/8);
        Result[2] = (byte) (0x1<<(Fn1%8 - 1));
        Temp = Pn.substring(1);
        int Pn1 = Integer.parseInt(Temp);
        if (0 == Pn1){
        	Result[1] = 0;
            Result[0] = 0;
        }
        else{
        	Result[1] = (byte) (Pn1/8 +1);
            Result[0] = (byte) (0x1<<(Pn1%8 - 1));
        }
		return Result;
    }

	/**
	 * @return the pn
	 */
	public String getPn() {
		return Pn;
	}

	/**
	 * @param pn the pn to set
	 */
	public void setPn(String pn) {
		Pn = pn;
	}

	/**
	 * @return the fn
	 */
	public String getFn() {
		return Fn;
	}

	/**
	 * @param fn the fn to set
	 */
	public void setFn(String fn) {
		Fn = fn;
	}


}
