package edu.ucsb.aelmore.util;

import org.junit.Test;

import edu.ucsb.aelmore.server.IVoteService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class DAOTest {

	public DAOTest() {
		
	}
	
	@Test
	public void TestDAOSetup() throws Exception{
		IDAO dao = new DAO();		
	}
	
	@Test
	public void TestMemberDAO() throws Exception{
		IDAO dao = new DAO();
		dao.reset();
		assertFalse(dao.memberExists("Isaac"));
		assertTrue(dao.addMember("Isaac"));
		assertFalse(dao.addMember("Isaac"));
		assertTrue(dao.addMember("Frank"));
	}
	
	@Test
	public void TestVoteDAO() throws Exception{
		IDAO dao = new DAO();
		dao.reset();
		assertTrue(dao.addMember("Isaac"));
		assertTrue(dao.addMember("Frank"));
		assertTrue(dao.vote("Isaac", "Frank"));
		assertFalse(dao.vote("Isaac", "Frank"));
		assertTrue(dao.vote("Frank", "Frank"));		
	}

	@Test
	public void TestResetDAO() throws Exception{
		IDAO dao = new DAO();
		dao.reset();
		assertTrue(dao.addMember("Isaac"));
		assertTrue(dao.memberExists("Isaac"));
		dao.reset();
		assertFalse(dao.memberExists("Isaac"));
		assertTrue(dao.addMember("Isaac"));
		assertTrue(dao.memberExists("Isaac"));
	}
	
	@Test
	public void TestMajorityDAO() throws Exception{
		IDAO dao = new DAO();
		dao.reset();
		assertTrue(dao.addMember("Isaac"));
		assertTrue(dao.addMember("Frank"));
		assertEquals(null,dao.getMajorityVote());
		assertTrue(dao.vote("Isaac", "Frank"));
		assertEquals(null,dao.getMajorityVote());
		assertFalse(dao.vote("Isaac", "Frank"));
		assertEquals(null,dao.getMajorityVote());
		assertTrue(dao.vote("Frank", "Frank"));	
		assertEquals("Frank",dao.getMajorityVote());
		assertTrue(dao.addMember("Philip"));
		assertEquals("Frank",dao.getMajorityVote());
		assertTrue(dao.addMember("Dan"));
		assertEquals(null,dao.getMajorityVote());
	
		dao.reset();
		assertTrue(dao.addMember("Isaac"));
		assertTrue(dao.addMember("Frank"));
		assertEquals(null,dao.getMajorityVote());
		assertTrue(dao.vote("Isaac", "Frank"));
		assertEquals(null,dao.getMajorityVote());
		assertTrue(dao.vote("Frank", "Isaac"));	
		assertEquals(null,dao.getMajorityVote());
		
	}
	
}
