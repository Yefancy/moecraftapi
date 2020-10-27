package net.moecraft.Utils;

import net.moecraft.Interfaces.IResult;

public class ResultBasic implements IResult {
	private boolean result = false;
	private String info= "";
	
	public ResultBasic(boolean result, String info) {
		// TODO Auto-generated constructor stub
		this.result = result;
		this.info = info;
	}
	
	@Override
	public boolean GetResult() {
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public String GetInfo() {
		// TODO Auto-generated method stub
		return info;
	}

}
