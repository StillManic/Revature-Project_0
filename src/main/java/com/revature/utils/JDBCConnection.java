package com.revature.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnection {
	private static Connection conn = null;
	
	public static Connection getConnection() {
		try {
			if (conn == null) {
				/*
				 * Hot-Fix to ensure that the driver loads correctly when the application starts
				 */
				Class.forName("org.postgresql.Driver");
				
				// need 3 connections: url (endpoint), username, password (DO NOT INCLUDE THESE IN THE CODE, USE A connection.properties FILE IN src/main/resources)
				Properties props = new Properties();
				InputStream propsInput = JDBCConnection.class.getClassLoader().getResourceAsStream("connection.properties");
				props.load(propsInput);
				String url = props.getProperty("url");
				String username = props.getProperty("username");
				String password = props.getProperty("password");
				conn = DriverManager.getConnection(url, username, password);
				return conn;
			}
		} catch (SQLException | IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
