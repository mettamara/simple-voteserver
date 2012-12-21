package edu.ucsb.aelmore.server;

import org.mortbay.jetty.Handler;

public interface IVoteServer {

	public void StartServer();
	public void setPort(int port);
	public void setVoteHandler(Handler handler);
}
