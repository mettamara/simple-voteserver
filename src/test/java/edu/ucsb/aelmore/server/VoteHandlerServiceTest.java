package edu.ucsb.aelmore.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import edu.ucsb.aelmore.util.MockDAO;
import edu.ucsb.aelmore.util.NonUniqueVoteServerException;
import edu.ucsb.aelmore.util.VoteServerException;



/**
 * @author aelmore
 * Test the service functionality
 */
public class VoteHandlerServiceTest {

	VoteHandler handler;
	@Before
	public void setUp() throws Exception {
		handler = new VoteHandler();
		handler.setDAO(new MockDAO());
	}

	@After
	public void tearDown() throws Exception {
		handler = null;
	}
	
  
  @Test(timeout = 100)
  public void testNameCheck() throws Exception {
  	String t = "ada2kh1";
  	assertTrue(handler.testInput(t));
  	t = "takah";
  	assertTrue(handler.testInput(t));
  	t = "139719";
  	assertTrue(handler.testInput(t));
  	t = "aaron j";
  	assertFalse(handler.testInput(t));
  	t = "";
  	assertFalse(handler.testInput(t));
  	t = "--drop table";
  	assertFalse(handler.testInput(t));
  	assertFalse(handler.testInput(null));
 	
  }

  
  @Test(timeout = 100)
  public void testAddMember() throws Exception {
  	handler.addMember("Isaac");
  	handler.addMember("Philip");
  	handler.addMember("1Q84");
		
  	assertTrue(true);
  	try{
  		handler.addMember("Philip");
  		assertTrue("Should throw exception on duplicate member",false);
  	}
  	catch(NonUniqueVoteServerException ex){
  		assertTrue(true);
  	}
		
  	try{
  		handler.addMember("11-*");
  		assertTrue("Should throw exception on invalid  input",false);
  	}
  	catch(VoteServerException ex){
  		assertTrue(true);
  	}
  }
  
  @Test(timeout = 100)
  public void testVoteFunction() throws Exception {
  	handler.reset();
  	handler.addMember("William");
  	handler.vote("William", "Vote");
  	assertTrue(true);
  	
  	assertEquals("One member,one vote","Vote",handler.getVictory());
  	
  	try{
  		handler.vote("Isaac","Vote");
  		assertTrue("Should throw exception on non member",false);
  	}
  	catch(VoteServerException ex){
  		assertTrue(true);
  	}
  	handler.addMember("Isaac");
  	assertEquals("Two member,one vote",IVoteService.NO_MAJORITY,handler.getVictory());
  	
  	try{
  		handler.vote("Isaac","Vote -");
  		assertTrue("Should throw exception on invalid  input",false);
  	}
  	catch(VoteServerException ex){
  		assertTrue(true);
  	}
  	
  	try{
  		handler.vote("William","Vote");
  		assertTrue("Should throw exception on invalid  input",false);
  	}
  	catch(VoteServerException ex){
  		assertTrue(true);
  	}

  	assertEquals("Two member,one vote",IVoteService.NO_MAJORITY,handler.getVictory());
  	handler.vote("Isaac","Vote2");
  	assertEquals("Two member,two disjoint votes",IVoteService.NO_MAJORITY,handler.getVictory());
  	handler.addMember("Frank");
  	handler.vote("Frank", "Vote2");
  	assertEquals("Majority for vote2","Vote2", handler.getVictory());
  	handler.addMember("Dan");
  	assertEquals("No Majority ",IVoteService.NO_MAJORITY, handler.getVictory());
  	handler.addMember("Philip");
  	assertEquals("No Majority ",IVoteService.NO_MAJORITY, handler.getVictory());
  	handler.vote("Philip", "Vote2");
  	assertEquals("Majority for vote2 with unvoting member","Vote2", handler.getVictory());
  	handler.vote("Dan", "Vote");
  	assertEquals("Majority for vote2","Vote2", handler.getVictory());
  	
  }

  
  @Test(timeout = 100)
  public void testReset() throws Exception {
  	handler.reset();
  	handler.addMember("William");
  	handler.vote("William", "Vote");
  	assertTrue(true);  	
  	assertEquals("One member,one vote","Vote",handler.getVictory());
  	handler.reset();
  	assertEquals("no members,no votes",IVoteService.NO_MAJORITY,handler.getVictory());
  	handler.addMember("William");
  	handler.vote("William", "Vote");
  	assertEquals("One member,one vote","Vote",handler.getVictory());
  	assertTrue(true);  	
  }

}
