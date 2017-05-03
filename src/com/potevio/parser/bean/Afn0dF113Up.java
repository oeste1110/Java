package com.potevio.parser.bean;

import java.nio.ByteBuffer;

public final class Afn0dF113Up extends FnBase {
	private Td_d DailyFrozenData_Td_d;
	private float A2IMaximum;
	private Td_A17 A2IMaximumTd;
	private float A3IMaximum;
	private Td_A17 A3IMaximumTd;
	private float A4IMaximum;
	private Td_A17 A4IMaximumTd;
	private float A5IMaximum;
	private Td_A17 A5IMaximumTd;
	private float A6IMaximum;
	private Td_A17 A6IMaximumTd;
	private float A7IMaximum;
	private Td_A17 A7IMaximumTd;
	private float A8IMaximum;
	private Td_A17 A8IMaximumTd;
	private float A9IMaximum;
	private Td_A17 A9IMaximumTd;
	private float A10IMaximum;
	private Td_A17 A10IMaximumTd;
	private float A11IMaximum;
	private Td_A17 A11IMaximumTd;
	private float A12IMaximum;
	private Td_A17 A12IMaximumTd;
	private float A13IMaximum;
	private Td_A17 A13IMaximumTd;
	private float A14IMaximum;
	private Td_A17 A14IMaximumTd;
	private float A15IMaximum;
	private Td_A17 A15IMaximumTd;
	private float A16IMaximum;
	private Td_A17 A16IMaximumTd;
	private float A17IMaximum;
	private Td_A17 A17IMaximumTd;
	private float A18IMaximum;
	private Td_A17 A18IMaximumTd;
	private float A19IMaximum;
	private Td_A17 A19IMaximumTd;
	
	public Afn0dF113Up(ByteBuffer buffer, String Pn, String Fn){
		super(Pn, Fn);
		DailyFrozenData_Td_d = new Td_d(buffer);
		A2IMaximum = buffer.getFloat();
		A2IMaximumTd = new Td_A17(buffer);
		A3IMaximum = buffer.getFloat();
		A3IMaximumTd = new Td_A17(buffer);
		A4IMaximum = buffer.getFloat();
		A4IMaximumTd = new Td_A17(buffer);
		A5IMaximum = buffer.getFloat();
		A5IMaximumTd = new Td_A17(buffer);
		A6IMaximum = buffer.getFloat();
		A6IMaximumTd = new Td_A17(buffer);
		A7IMaximum = buffer.getFloat();
		A7IMaximumTd = new Td_A17(buffer);
		A8IMaximum = buffer.getFloat();
		A8IMaximumTd = new Td_A17(buffer);
		A9IMaximum = buffer.getFloat();
		A9IMaximumTd = new Td_A17(buffer);
		A10IMaximum = buffer.getFloat();
		A10IMaximumTd = new Td_A17(buffer);
		A11IMaximum = buffer.getFloat();
		A11IMaximumTd = new Td_A17(buffer);
		A12IMaximum = buffer.getFloat();
		A12IMaximumTd = new Td_A17(buffer);
		A13IMaximum = buffer.getFloat();
		A13IMaximumTd = new Td_A17(buffer);
		A14IMaximum = buffer.getFloat();
		A14IMaximumTd = new Td_A17(buffer);
		A15IMaximum = buffer.getFloat();
		A15IMaximumTd = new Td_A17(buffer);
		A16IMaximum = buffer.getFloat();
		A16IMaximumTd = new Td_A17(buffer);
		A17IMaximum = buffer.getFloat();
		A17IMaximumTd = new Td_A17(buffer);
		A18IMaximum = buffer.getFloat();
		A18IMaximumTd = new Td_A17(buffer);
		A19IMaximum = buffer.getFloat();
		A19IMaximumTd = new Td_A17(buffer);
	}
	/**
	 * @return the dailyFrozenData_Td_d
	 */
	public Td_d getDailyFrozenData_Td_d() {
		return DailyFrozenData_Td_d;
	}

	/**
	 * @param dailyFrozenData_Td_d the dailyFrozenData_Td_d to set
	 */
	public void setDailyFrozenData_Td_d(Td_d dailyFrozenData_Td_d) {
		DailyFrozenData_Td_d = dailyFrozenData_Td_d;
	}
	

}
