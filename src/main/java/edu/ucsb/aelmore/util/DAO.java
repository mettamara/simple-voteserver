/**
 * 
 */
package edu.ucsb.aelmore.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author aelmore
 * Store models in containers. Coarsely thread safe.
 * Larger project would call for proper mock framework.
 */
public class DAO implements IDAO {
	private static Logger log = LoggerFactory.getLogger(DAO.class);
	public List<String> members;
	public Map<String,Boolean> memberVoted;
	public Map<String, Integer> votes;
	/**
	 * 
	 */
	public DAO() {
		members = new ArrayList<String>();
		memberVoted = new HashMap<String, Boolean>();
		votes = new HashMap<String, Integer>();
	}

	/* (non-Javadoc)
	 * @see edu.ucsb.aelmore.util.IDAO#addMember(java.lang.String)
	 */
	@Override
	public boolean addMember(String member) {
		synchronized (this) {
			if (members.contains(member))
				return false;
	    members.add(member);
	    memberVoted.put(member, false);
	    return true;
    }
	}

	/* (non-Javadoc)
	 * @see edu.ucsb.aelmore.util.IDAO#memberExists(java.lang.String)
	 */
	@Override
	public boolean memberExists(String member) {
		synchronized (this) {
			return members.contains(member);
		}
	}

	/* (non-Javadoc)
	 * @see edu.ucsb.aelmore.util.IDAO#vote(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean vote(String member, String vote) {
		synchronized (this) {
			if (memberVoted.containsKey(member) && memberVoted.get(member)==false){
				memberVoted.put(member, true);
				int voteValue = (votes.containsKey(vote)) ? votes.get(vote)+1 : 1;	
				votes.put(vote,voteValue);
				return true;
			}
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see edu.ucsb.aelmore.util.IDAO#getMajorityVote()
	 */
	@Override
	public String getMajorityVote() {
		synchronized (this) {
			//Could be done more efficiently. Simple for mock
			String maxVal = null;
			int maxCount = -1;
			for (Map.Entry<String, Integer> vote: votes.entrySet()) {
		    if(vote.getValue()>maxCount){
		    	
		    	maxCount = vote.getValue();
		    	maxVal = vote.getKey();
		    }
	    }
			log.debug(String.format("Max Vote:%s count:%d  Memebers:%d  Votes Required:%s", maxVal,maxCount,members.size(),(float)members.size()/2.0));
			if (maxCount > (float)members.size()/2.0)
				return maxVal;
			
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.ucsb.aelmore.util.IDAO#reset()
	 */
	@Override
	public boolean reset() {
		synchronized (this) {
			members = new ArrayList<String>();
			memberVoted = new HashMap<String, Boolean>();
			votes = new HashMap<String, Integer>();
			return true;
		}
	}

}
