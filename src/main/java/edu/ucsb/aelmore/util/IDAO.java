/**
 * 
 */
package edu.ucsb.aelmore.util;

/**
 * @author aelmore
 * Simple Data Access Object
 * If functionality was greater would use individual dao's or an ORM.
 * Separating functionality to different component for unit tests to be isolated from persistance/db 
 */
public interface IDAO {
	
	/**
	 * Persist member 
	 * @param member 
	 * @return If member was successfully added or if member was already present
	 */
	public boolean addMember(String member);
	
	/**
	 * @param member
	 * @return If the member has already been added
	 */
	public boolean memberExists(String member);
	
	/**
	 * Persist a vote
	 * @param member
	 * @param vote
	 * @return If the vote was recorded or already exists
	 */
	public boolean vote(String member, String vote);
	
	/**
	 * @return The name of a vote value having the vote of a majority of members or null if no value is found 
	 */
	public String getMajorityVote();
	
	/**
	 * Reset the persistence layer
	 * @return
	 */
	public boolean reset();
	
}
