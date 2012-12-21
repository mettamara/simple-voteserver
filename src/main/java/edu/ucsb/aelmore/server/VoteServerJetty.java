/**
 * 
 */
package edu.ucsb.aelmore.server;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.ucsb.aelmore.util.DAO;
import edu.ucsb.aelmore.util.IDAO;

/**
 * @author aelmore Web Server to handle http requests
 */
public class VoteServerJetty implements IVoteServer {
  private static Logger log = LoggerFactory.getLogger(VoteServerJetty.class);
  private int port = 8080;
  private Handler handler;

  /**
   * @param args
   */
  public static void main(String[] args) {
    IVoteServer server = new VoteServerJetty();
    server.StartServer();

  }

  @Override
  public void StartServer() {
    try {
      Server server = new Server();

      Connector connector = new SelectChannelConnector();
      connector.setPort(port);
      // connector.setHost("127.0.0.1");
      server.addConnector(connector);
      ApplicationContext appContext = new ClassPathXmlApplicationContext(
          "context.xml");
      //Handler handler = (Handler) appContext.getBean("voteHandler");// new
                                                                    // VoteHandler();
      //((VoteHandler) handler).setDAO((IDAO) appContext.getBean("dao"));
      server.setHandler(handler);
      server.setStopAtShutdown(true);

      // another way is to use an external jetty configuration file
      // XmlConfiguration configuration =
      // new XmlConfiguration(new File("/path/jetty-config.xml").toURL());
      // configuration.configure(server);

      server.start();
      server.join();
    } catch (Exception ex) {
      log.error("Exception starting server", ex);
      System.exit(-1);
    }
  }

  public void setPort(int port) {
    this.port = port;
  }


  public void setVoteHandler(Handler handler){
    this.handler = handler;
  }

}
