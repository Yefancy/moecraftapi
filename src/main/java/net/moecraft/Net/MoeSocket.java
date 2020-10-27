package net.moecraft.Net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import net.moecraft.Interfaces.IAction;
import net.moecraft.Interfaces.IResult;
import net.moecraft.Utils.ResultBasic;

public class MoeSocket {
	private ServerSocket ss = null;
	private int port;
	private boolean init = false;
	private Thread acceptThread;
	private Thread sendThread;
	private Thread reciveThread;
	private Socket client;
	private BufferedReader br;
	private BufferedWriter bw;

	public MoeSocket(int port) {
		this.port = port;
	}

	public boolean BlindPort() {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			return false;
		}
		init = true;
		return true;
	}

	public boolean BeginAccept(final IAction<IResult> callBack) {
		if (!init)
			return false;
		try {
			if (acceptThread != null)
				acceptThread.interrupt();
			acceptThread = new Thread() {
				public void run() {
					try {
						if (br != null)
							br.close();
						if (bw != null) {
							bw.close();
						}		
						if (client != null)
							client.close();
						client = ss.accept();				
						br = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
						bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
						callBack.invoke(new ResultBasic(true, client.getInetAddress().toString()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						callBack.invoke(new ResultBasic(false, e.getMessage()));
					}
				}
			};
			acceptThread.start();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean BeginSend(final String info, final IAction<IResult> callBack) {
		if (!init || client == null)
			return false;
		if (!client.isConnected())
			return false;
		try {
			if (sendThread != null)
				sendThread.interrupt();
			sendThread = new Thread() {
				public void run() {
					try {
						bw.write(info);
						bw.flush();
						callBack.invoke(new ResultBasic(true, info));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						callBack.invoke(new ResultBasic(false, e.getMessage()));
					}
				}
			};
			sendThread.start();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean BeginSend(final String info) {
		if (!init || client == null)
			return false;
		if (!client.isConnected())
			return false;
		try {
			if (sendThread != null)
				sendThread.interrupt();
			sendThread = new Thread() {
				public void run() {
					try {
						String send =info +  "\r\n";
						bw.write(send);
						bw.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
					}
				}
			};
			sendThread.start();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean Send(String info) {
		if (!init || client == null)
			return false;
		if (!client.isConnected())
			return false;
		try {
			String send = info + "\r\n";
			bw.write(send);
			bw.flush();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean BeginRecive(final IAction<IResult> callBack) {
		if (!init || client == null)
			return false;
		if (!client.isConnected())
			return false;
		try {
			if (reciveThread != null)
				reciveThread.interrupt();
			reciveThread = new Thread() {
				public void run() {
					try {
						while (true) {
							char[] buff = new char[2048];
							int len = br.read(buff);
							if (len == -1) {
								callBack.invoke(new ResultBasic(false, "Connect Reset"));
								return;
							}
							String msg = String.valueOf(buff).substring(0, len);
							callBack.invoke(new ResultBasic(true, msg));
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						callBack.invoke(new ResultBasic(false, e.getMessage()));
					}
				}
			};
			reciveThread.start();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
