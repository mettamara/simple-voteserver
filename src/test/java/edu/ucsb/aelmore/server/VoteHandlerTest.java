/**
 * 
 */
package edu.ucsb.aelmore.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import edu.ucsb.aelmore.util.InMemDAO;
import edu.ucsb.aelmore.util.MockDAO;
import edu.ucsb.aelmore.util.NonUniqueVoteServerException;
import edu.ucsb.aelmore.util.VoteServerException;

/**
 * @author aelmore
 * 
 */
public class VoteHandlerTest {
	private static Logger log = LoggerFactory.getLogger(VoteHandlerTest.class);

	VoteHandler handler;

	MockHttpServletRequest request = new MockHttpServletRequest();

	MockHttpServletResponse response = new MockHttpServletResponse();

	@Before
	public void setUp() throws Exception {
		handler = new VoteHandler();
		handler.setDAO(new MockDAO());
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@After
	public void tearDown() throws Exception {
		handler = null;
	}

	@Test
	public void testHandleBadRequest() throws Exception {
		request.setRequestURI("/taa");
		request.setMethod("GET");
		handler.handle("taa", request, response, 0);
		assertEquals("Invalid Request", HttpServletResponse.SC_NOT_FOUND, response.getStatus());
		request.setMethod("PUT");
		handler.handle("taa", request, response, 0);
		assertEquals("Invalid Request", HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
		// assertEquals("Expected - ", "-", response.getContentAsString());

	}

	@Test
	public void testVoteRequest() throws Exception {

		// MAlformed Votes
		request.setMethod("POST");
		request.setRequestURI("/" + IVoteHandler.VOTE_SCV);
		handler.handle(IVoteHandler.VOTE_SCV, request, response, 0);
		assertEquals("Invalid Request", HttpServletResponse.SC_BAD_REQUEST, response.getStatus());

		request.addParameter(IVoteHandler.AGENT_PARM, "Bruce");
		// Missing vote

		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		handler.handle(IVoteHandler.VOTE_SCV, request, response, 0);
		assertEquals("Invalid Request", HttpServletResponse.SC_BAD_REQUEST, response.getStatus());

		// VOTE
		handler.addMember("Bruce");
		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		request.setMethod("POST");
		request.setRequestURI("/" + IVoteHandler.VOTE_SCV);
		request.addParameter(IVoteHandler.AGENT_PARM, "Bruce");
		request.addParameter(IVoteHandler.VOTE_PARM, "Bruce");
		handler.handle(IVoteHandler.VOTE_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("Expected - ", "-", response.getContentAsString());
	}

	@Test
	public void testMemberRequest() throws Exception {

		// Malformed type
		request.setMethod("GET");
		request.setRequestURI("/" + IVoteHandler.MEMBER_SCV);
		handler.handle(IVoteHandler.MEMBER_SCV, request, response, 0);
		assertEquals("Invalid Request", HttpServletResponse.SC_BAD_REQUEST, response.getStatus());

		// Missing agent parm
		response = new MockHttpServletResponse();
		request.setMethod("POST");
		request.setRequestURI("/" + IVoteHandler.MEMBER_SCV);
		handler.handle(IVoteHandler.MEMBER_SCV, request, response, 0);
		assertEquals("Invalid Request", HttpServletResponse.SC_BAD_REQUEST, response.getStatus());

		// valid
		response = new MockHttpServletResponse();
		request.setRequestURI("/" + IVoteHandler.MEMBER_SCV);
		request.addParameter(IVoteHandler.AGENT_PARM, "Bruce");
		handler.handle(IVoteHandler.MEMBER_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("Expected - ", "-", response.getContentAsString());
	}

	@Test
	public void testVictoryRequest() throws Exception {

		// Check Victory get
		request.setRequestURI("/" + IVoteHandler.VICTORY_SCV);
		request.setMethod("GET");
		handler.handle(IVoteHandler.VICTORY_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("Expected UNKNOWN ", IVoteService.NO_MAJORITY, response.getContentAsString());

	}

	@Test
	public void testResetRequest() throws Exception {
		// Check Victory get
		request.setRequestURI("/" + IVoteHandler.RESET_SCV);
		request.setMethod("POST");
		handler.handle(IVoteHandler.RESET_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("Expected - ", "-", response.getContentAsString());

		response = new MockHttpServletResponse();
		request.setMethod("GET");
		handler.handle(IVoteHandler.RESET_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}

	@Test
	public void testVote() throws Exception {
		handler.reset();
		
		//Add member
		response = new MockHttpServletResponse();

		request.setMethod("POST");
		request.setRequestURI("/" + IVoteHandler.MEMBER_SCV);
		request.addParameter(IVoteHandler.AGENT_PARM, "Bruce");
		handler.handle(IVoteHandler.MEMBER_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("Expected - ", "-", response.getContentAsString());
		
		//Vote 
		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		request.setMethod("POST");
		request.setRequestURI("/" + IVoteHandler.VOTE_SCV);
		request.addParameter(IVoteHandler.AGENT_PARM, "Bruce");
		request.addParameter(IVoteHandler.VOTE_PARM, "Bruce");
		handler.handle(IVoteHandler.VOTE_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("Expected - ", "-", response.getContentAsString());
		
		//Check Victor
		response = new MockHttpServletResponse();
		request.setRequestURI("/" + IVoteHandler.VICTORY_SCV);
		request.setMethod("GET");
		handler.handle(IVoteHandler.VICTORY_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("Expected Bruce ", "Bruce", response.getContentAsString());

		//bad vote
		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		request.setMethod("POST");
		request.setRequestURI("/" + IVoteHandler.VOTE_SCV);
		request.addParameter(IVoteHandler.AGENT_PARM, "Isaac");
		request.addParameter(IVoteHandler.VOTE_PARM, "Bruce");
		handler.handle(IVoteHandler.VOTE_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
		
		
		//Add member
		response = new MockHttpServletResponse();
		request.setMethod("POST");
		request.setRequestURI("/" + IVoteHandler.MEMBER_SCV);
		request.addParameter(IVoteHandler.AGENT_PARM, "Isaac");
		handler.handle(IVoteHandler.MEMBER_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("Expected - ", "-", response.getContentAsString());

		//Dulpicate Add member
		response = new MockHttpServletResponse();
		request.setRequestURI("/" + IVoteHandler.MEMBER_SCV);
		request.addParameter(IVoteHandler.AGENT_PARM, "Isaac");
		handler.handle(IVoteHandler.MEMBER_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
		

		//vote
		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		request.setMethod("POST");
		request.setRequestURI("/" + IVoteHandler.VOTE_SCV);
		request.addParameter(IVoteHandler.AGENT_PARM, "Isaac");
		request.addParameter(IVoteHandler.VOTE_PARM, "Philip");
		handler.handle(IVoteHandler.VOTE_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("Expected - ", "-", response.getContentAsString());
		
		//Check Victor
		response = new MockHttpServletResponse();
		request.setRequestURI("/" + IVoteHandler.VICTORY_SCV);
		request.setMethod("GET");
		handler.handle(IVoteHandler.VICTORY_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("Expected UNKNOWN ", IVoteService.NO_MAJORITY, response.getContentAsString());
		
		//Add member
		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		request.setMethod("POST");
		request.setRequestURI("/" + IVoteHandler.MEMBER_SCV);
		request.addParameter(IVoteHandler.AGENT_PARM, "Frank");
		handler.handle(IVoteHandler.MEMBER_SCV, request, response, 0);
		//log.debug(response.getContentAsString());
		assertEquals("Valid Request", HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("Expected - ", "-", response.getContentAsString());
		
		//vote
		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		request.setMethod("POST");
		request.setRequestURI("/" + IVoteHandler.VOTE_SCV);
		request.addParameter(IVoteHandler.AGENT_PARM, "Frank");
		request.addParameter(IVoteHandler.VOTE_PARM, "Philip");
		handler.handle(IVoteHandler.VOTE_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("Expected - ", "-", response.getContentAsString());
		
		//duplicate vote
		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		request.setMethod("POST");
		request.setRequestURI("/" + IVoteHandler.VOTE_SCV);
		request.addParameter(IVoteHandler.AGENT_PARM, "Frank");
		request.addParameter(IVoteHandler.VOTE_PARM, "Philip");
		handler.handle(IVoteHandler.VOTE_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
		
		//Check Victor
		response = new MockHttpServletResponse();
		request.setRequestURI("/" + IVoteHandler.VICTORY_SCV);
		request.setMethod("GET");
		handler.handle(IVoteHandler.VICTORY_SCV, request, response, 0);
		assertEquals("Valid Request", HttpServletResponse.SC_OK, response.getStatus());
		assertEquals("Expected Philip ", "Philip", response.getContentAsString());
	}
}
