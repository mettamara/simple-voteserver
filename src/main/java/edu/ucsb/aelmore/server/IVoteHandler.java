package edu.ucsb.aelmore.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ucsb.aelmore.util.VoteServerException;

/**
 * @author aelmore
 */
public interface IVoteHandler {

	//URI Request parameters
	public static final String GOOD_RESPONSE = "-";
	public static final String MEMBER_SCV ="/member";
	public static final String VOTE_SCV ="/vote";
	public static final String VICTORY_SCV ="/victory";
	public static final String RESET_SCV ="/rst";
	public static final String AGENT_PARM = "agent";
	public static final String VOTE_PARM ="vote";
	public static final String FAVICON ="/favicon.ico";
	

	public static final String VOTE_MISSING_PARM ="vote request requires a valid "+AGENT_PARM + " and " + VOTE_PARM + " parameters";
	public static final String MEMBER_MISSING_PARM ="member request requires a valid "+AGENT_PARM + " parameter";
	public static final String  WRONG_METHOD_TYPE = "Wrong method type for request (POST/GET)";
	
  public void handle(String target, HttpServletRequest request,
      HttpServletResponse response, int arg3) throws IOException, ServletException ;
	

	
}
