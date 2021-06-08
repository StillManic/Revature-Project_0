package com.revature.repository_tests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.models.Account;
import com.revature.models.Customer;
import com.revature.repositories.CustomerRepository;
import com.revature.utils.JDBCConnection;

public class CustomerRepoTest {
	private static Savepoint sp;
	private static Connection conn;
	private Integer expectedId = 4;
	
	@BeforeClass
	public static void beforeClass() {
		conn = JDBCConnection.getConnection();
	}
	
	@Before
	public void before() {
		try {
			conn.setAutoCommit(false);
			sp = conn.setSavepoint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void addTest() {
		Customer expected = new Customer(expectedId, "test", "test", true);
		Customer c = new Customer("test", "test", true);
		Customer result = CustomerRepository.getInstance().add(c);
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void getByIdTest() {
		Account a1 = new Account(1, 50000.5f);
		a1.setCustomerId(2);
		a1.setPending(false);
		Account a2 = new Account(2, 30.25f);
		a2.setCustomerId(2);
		a2.setPending(false);
		Customer expected = new Customer("visanti", "suits", a1, a2);
		expected.setId(2);
		Customer result = CustomerRepository.getInstance().getById(2);
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void getByUAndPTest() {
		Account a1 = new Account(1, 50000.5f);
		a1.setCustomerId(2);
		a1.setPending(false);
		Account a2 = new Account(2, 30.25f);
		a2.setCustomerId(2);
		a2.setPending(false);
		Customer expected = new Customer("visanti", "suits", a1, a2);
		expected.setId(2);
		Customer result = CustomerRepository.getInstance().getByUsernameAndPassword("visanti", "suits");
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void getEmployeesTest() {
		List<Customer> expected = new ArrayList<Customer>();
		expected.add(new Customer(1, "gerald", "master", true));
		List<Customer> result = CustomerRepository.getInstance().getEmployees();
		Assert.assertEquals(expected, result);
	}
	
//	@Test
//	public void getAllTest() {
//		Map<Integer, Customer> expected = new HashMap<Integer, Customer>();
//		
//	}
		
	@After
	public void after() {
		try {
			conn.rollback(sp);
			conn.setAutoCommit(true);
			String sql = String.format("alter sequence customers_id_seq restart with %d;", expectedId);
//			String sql = "alter sequence customers_id_seq restart with ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
//			ps.setString(1, Integer.toString(expectedId));
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
