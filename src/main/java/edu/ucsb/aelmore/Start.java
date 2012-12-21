/**
 * Start.java - Copyright 2012 The Incrementum Group, LLC
 */
package edu.ucsb.aelmore;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.ucsb.aelmore.server.IVoteServer;

/**
 * @author aelmore
 * Entry point
 */
public class Start {

  /**
   * @param args
   */
  public static void main(String[] args) {
    ApplicationContext appContext = new ClassPathXmlApplicationContext("context.xml");
    IVoteServer server = (IVoteServer)appContext.getBean("voteServer");
    server.StartServer();
  }

}
