package net.moecraft.Utils;

public class InfoProtocol {
	public int type;// 0-request // 1-respond // 2-inform // 3-chat
	public int id;
	public String info;
	
	public InfoProtocol(int type, int id, String info) 
	{
		this.type = type;
		this.id = id;
		this.info = info;
	}
	
	public InfoProtocol(int type, String info) 
	{
		this.type = type;
		this.info = info;
		this.id = -1;
	}
}
