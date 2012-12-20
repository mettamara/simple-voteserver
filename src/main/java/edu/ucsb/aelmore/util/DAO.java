package edu.ucsb.aelmore.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DAO implements IDAO {
	private static Logger log = LoggerFactory.getLogger(DAO.class);
	private static JdbcConnectionPool cp = null;
	// To not create DB on connect use:
	// private String dbURL = "jdbc:h2:voteServer";
	// Will create db file in target dir
	private String dbURL = "jdbc:h2:voteServer;INIT=RUNSCRIPT FROM 'CreateDB.sql'";

	private String dbUser = "sa";
	private String dbPassword = "sa";

	public static final String INSERT_MEMBER = "insert into members values (?)";
	public static final String CHECK_MEMBER = "select * from members where agent=?";
	public static final String INSERT_VOTE = "insert into vote values (?,?)";
	public static final String RESET_VOTE = "delete from vote";
	public static final String RESET_MEMBERS = "delete from members";
	public static final String GET_MAJORITY_VOTE = "select vote,count(*) from vote group by vote having count(*) > (select count(*) from members)/2";

	public DAO() {
		synchronized (DAO.class) {
			if (cp == null)
				cp = JdbcConnectionPool.create(dbURL, dbUser, dbPassword);
			log.debug("Database and connection pool created");
		}

	}

	@Override
	public boolean addMember(String member) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = cp.getConnection();
			ps = conn.prepareStatement(INSERT_MEMBER);
			ps.setString(1, member);
			int rows = ps.executeUpdate();
			if (rows == 1)
				return true;
			else
				return false;
		} catch (SQLException e) {
			log.info("Excpetions executing query", e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				log.error("Excpetions closing connection", e);
			}
		}
		return false;
	}

	@Override
	public boolean memberExists(String member) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = cp.getConnection();
			ps = conn.prepareStatement(CHECK_MEMBER);
			ps.setString(1, member);
			rs = ps.executeQuery();
			if (rs.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			log.error("Excpetions executing query", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				log.error("Excpetions closing connection", e);
			}
		}
		return false;
	}

	@Override
	public boolean vote(String member, String vote) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = cp.getConnection();
			ps = conn.prepareStatement(INSERT_VOTE);
			ps.setString(1, vote);
			ps.setString(2, member);
			int rows = ps.executeUpdate();
			if (rows == 1)
				return true;

		} catch (SQLException e) {
			log.info("Excpetions executing query", e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				log.error("Excpetions closing connection", e);
			}
		}
		return false;
	}

	@Override
	public String getMajorityVote() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = cp.getConnection();
			ps = conn.prepareStatement(GET_MAJORITY_VOTE);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("vote");
			}
		} catch (SQLException e) {
			log.error("Excpetions executing query", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				log.error("Excpetions closing connection", e);
			}
		}
		return null;
	}

	@Override
	public boolean reset() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = cp.getConnection();
			ps = conn.prepareStatement(RESET_VOTE);
			int rows = ps.executeUpdate();
			ps.close();
			ps = conn.prepareStatement(RESET_MEMBERS);
			int rows2 = ps.executeUpdate();
			return true;

		} catch (SQLException e) {
			log.error("Excpetions executing query", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				log.error("Excpetions closing connection", e);
			}
		}
		return false;
	}

}
