/**
 * 
 */
package edu.ucsb.aelmore.server;

/**
 * @author aelmore
 * Web Server to handle http requests
 */
public class VoteServerJetty implements IVoteServer{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IVoteServer server = new VoteServerJetty();
		server.StartServer();
			
	}

	@Override
  public void StartServer() {
	  
  }

}
