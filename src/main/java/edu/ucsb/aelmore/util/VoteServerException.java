/**
 * 
 */
package edu.ucsb.aelmore.util;

/**
 * @author aelmore
 * Base exception class for Simple-VoteServer
 */
public class VoteServerException extends Exception {

	public VoteServerException() {
		super();
	}

	public VoteServerException(String arg0) {
		super(arg0);		
	}

	public VoteServerException(Throwable arg0) {
		super(arg0);
	}

	public VoteServerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
