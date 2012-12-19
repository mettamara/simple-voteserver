/**
 * 
 */
package edu.ucsb.aelmore.server;

import edu.ucsb.aelmore.util.IDAO;
import edu.ucsb.aelmore.util.VoteServerException;

/**
 * @author aelmore
 *
 */
public interface IVoteService {
	
	public static final String INVALID_INPUT="Members and votes must be alphanumeric only";
	public static final String MEMBER_EXISTS="Member already exists.";
	public static final String MEMBER_NOT_EXISTS="Member does not exist.";
	public static final String MEMBER_VOTED="Member has alread voted.";
	public static final String NO_MAJORITY="UNKNOWN";
	/**
	 * Add a new voting member to the system. 
	 * Only alphanumeric characters accepted.
	 * Member must be unique
	 * @param member The member name to add.
	 * @throws VoteServerException If uniqueness or naming conventions are violated
	 */
	public void addMember(String member) throws VoteServerException;
	
	/**
	 * Register a vote for a member. Only alphanumeric values accepted. Each member may only vote once.  
	 * @param member The voting member name
	 * @param vote The vote value
	 * @throws VoteServerException If member has voted previously, or if member or vote value are malformed.
	 */
	public void vote(String member, String vote) throws VoteServerException;
	
	/**
	 * Check if a winner has been determined.
	 * If we have n registered agents/members, and we have m > n/2 votes for a given value then it is the winner.
	 * @return either the name of the winner or UNKNOWN
	 * @throws VoteServerException
	 */
	public String getVictory() throws VoteServerException;
	
	/**
	 * Reset the VoteServers persisted state.
	 * @throws VoteServerException
	 */
	public void reset() throws VoteServerException;
	
	public void setDAO(IDAO dao);
}
