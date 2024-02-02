package com.ml.app.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class DatabaseUtility {
	private static Connection con;
	private static final Logger logger = LogManager.getLogger(DatabaseUtility.class);

	static {
		try (InputStream in = DatabaseUtility.class.getResourceAsStream("/app.properties")) {
			if (in == null) {
				throw new IOException("\napp.properties file is either missing or not available in speciefied classpath");
			}
			Properties p = new Properties();
			p.load(in);
			String url = p.getProperty("dburl");
			String user = p.getProperty("user");
			String pwd = p.getProperty("password");
			if (url == null || user == null || pwd == null)
				throw new SQLException("\nProperties required for database connections are missing, take a look on your"
						+ "app.properties file.");
			con = DriverManager.getConnection(p.getProperty("dburl"), p.getProperty("user"), p.getProperty("password"));
		} catch (SQLException | IOException e) {
			printDetailedStackTrace(e);
		}
	}

	public static Connection getConnection() {
		if (con == null) {
			System.out.println(
					"\nConnection could not be established, make sure your database details are correct and in place.");
		}
		return con;
	}

	public static void closeConnection() throws SQLException {
		if (con != null) {
			con.close();
			con = null;
		}
	}

	private static void printDetailedStackTrace(Throwable t) {
		while (t != null) {
			Throwable cause = t.getCause();
			String message = t.getMessage();
			Optional<String> o = Optional.ofNullable(message);
			logger.error("Exception : " + o.orElse("\n\nEnd of stacktrace for this module."));
			if (cause != null) {
				Optional<Throwable> c = Optional.ofNullable(cause);
				c.ifPresentOrElse(throwable -> logger.error(throwable.getMessage()),
						() -> logger.info("\nCause is either unknown or null."));
				logger.error(o.orElse("\n\nEnd of stacktrace for this module."));
				t = cause;
			} else {
				logger.info("\n\nEnd Of StackTrace");
				t = null;
			}
		}
	}
}
