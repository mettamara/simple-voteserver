/**
 * 
 */
package edu.ucsb.aelmore.server;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.handler.AbstractHandler;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ucsb.aelmore.util.IDAO;
import edu.ucsb.aelmore.util.NonUniqueVoteServerException;
import edu.ucsb.aelmore.util.VoteServerException;

/**
 * @author aelmore Merge the server functionality and the service/manager/view
 *         layer due to small functionality. The interfaces for request handling
 *         and service functionality are sepearated so the components can be
 *         decoupled.
 */
public class VoteHandler extends AbstractHandler implements IVoteHandler, IVoteService {
	private static Logger log = LoggerFactory.getLogger(VoteHandler.class);
	protected IDAO dao;
	protected Pattern inputPattern;
	StopWatch sw;

	public VoteHandler() {
		super();
		inputPattern = Pattern.compile("\\W+");
		sw = new Log4JStopWatch();
	}

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, int arg3)
	    throws IOException, ServletException {

		sw.start(target);
		if (request.getMethod().equals("POST")) {
			log.debug("Post Request");
			if (target.equalsIgnoreCase(MEMBER_SCV)) {
				// ****** Add a new MEMBER ****************

				log.debug("Member");
				if (request.getParameter(AGENT_PARM) == null) {
					log.debug("Missing parm");
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, MEMBER_MISSING_PARM);
				} else {
					// Add member
					try {

						addMember(request.getParameter(AGENT_PARM));
						response.getWriter().write("-");
						response.setStatus(HttpServletResponse.SC_OK);
					} catch (VoteServerException ex) {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
					}
				}

			} else if (target.equalsIgnoreCase(VOTE_SCV)) {
				// ****** Add a new VOTE ****************
				log.debug("Vote");
				if (request.getParameter(AGENT_PARM) == null || request.getParameter(VOTE_PARM) == null) {
					log.debug("Missing parm");
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, VOTE_MISSING_PARM);
				} else {
					try {
						log.debug(String.format("Vote recv : agent:%s vote:%s ", request.getParameter(AGENT_PARM),
						    request.getParameter(VOTE_PARM)));
						vote(request.getParameter(AGENT_PARM), request.getParameter(VOTE_PARM));
						response.getWriter().write("-");
						response.setStatus(HttpServletResponse.SC_OK);
					} catch (VoteServerException ex) {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
					}
				}
			} else if (target.equalsIgnoreCase(RESET_SCV)) {

				log.debug("Reset");
				// Reset
				try {
					reset();
					response.getWriter().write("-");
					response.setStatus(HttpServletResponse.SC_OK);
				} catch (VoteServerException ex) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
				}
			} else if (target.equalsIgnoreCase(VICTORY_SCV)) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, WRONG_METHOD_TYPE);
			} else {
				log.debug("Not found : " + target);
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} else if (request.getMethod().equals("GET")) {
			log.debug("GET request");
			if (target.equalsIgnoreCase(VICTORY_SCV)) {
				// Check Victory

				try {
					String victor = getVictory();
					response.getWriter().write(victor);
					response.setStatus(HttpServletResponse.SC_OK);

				} catch (VoteServerException ex) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
				}
			} else if (target.equalsIgnoreCase(RESET_SCV) || target.equalsIgnoreCase(MEMBER_SCV)
			    || target.equalsIgnoreCase(VOTE_SCV)) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, WRONG_METHOD_TYPE);
			} else if (target.equalsIgnoreCase(FAVICON)) {

			} else {
				log.debug("Not found : " + target);
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} else {

			log.debug("Bad request : " + request.getMethod());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		// Required for Jetty. Reflection to not setHandled for mock objects/testing
		if (request instanceof Request) {
			Request base_request = (Request) request;
			base_request.setHandled(true);
		}
		sw.stop(target);
	}

	protected boolean testInput(String s) {
		if (s == null || s.isEmpty())
			return false;
		Matcher m = inputPattern.matcher(s);
		return !m.find();
	}

	@Override
	public void addMember(String member) throws VoteServerException {
		if (!testInput(member))
			throw new VoteServerException(INVALID_INPUT);
		if (dao.memberExists(member) || dao.addMember(member) == false)
			throw new NonUniqueVoteServerException(MEMBER_EXISTS);

	}

	@Override
	public void vote(String member, String vote) throws VoteServerException {
		if (!testInput(member) || !testInput(vote))
			throw new VoteServerException(INVALID_INPUT);
		if (!dao.memberExists(member))
			throw new VoteServerException(MEMBER_NOT_EXISTS);
		if (!dao.vote(member, vote))
			throw new NonUniqueVoteServerException(MEMBER_VOTED);
	}

	@Override
	public String getVictory() throws VoteServerException {
		String majorityVote = dao.getMajorityVote();
		if (majorityVote != null)
			return majorityVote;
		return NO_MAJORITY;
	}

	@Override
	public void reset() throws VoteServerException {
		dao.reset();
	}

	@Override
	public void setDAO(IDAO dao) {
		this.dao = dao;
	}

}
