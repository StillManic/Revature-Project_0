package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.revature.models.Account;
import com.revature.utils.JDBCConnection;

public class AccountRepository implements GenericRepository<Account> {
	private static AccountRepository instance;
	private Connection conn = JDBCConnection.getConnection();
	
	private AccountRepository() {}
	
	public static AccountRepository getInstance() {
		if (instance == null) instance = new AccountRepository();
		return instance;
	}
	
	@Override
	public Account add(Account a) {
		String sql = "insert into accounts values (default, ?, ?, ?) returning *;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setFloat(1, a.getBalance());
			ps.setInt(2, a.getCustomerId());
			ps.setBoolean(3, a.isPending());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				a.setId(rs.getInt("id"));
				return a;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Account getById(Integer id) {
		String sql = "select * from accounts where id = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Account a = new Account();
				a.setId(rs.getInt("id"));
				a.setBalance(rs.getFloat("balance"));
				a.setCustomerId(rs.getInt("customer"));
				a.setPending(rs.getBoolean("pending"));
				return a;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Map<Integer, Account> getAllByCustomerId(Integer customer_id) {
		String sql = "select * from accounts where customer = ?;";
		try {
			Map<Integer, Account> map = new HashMap<Integer, Account>();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, customer_id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Account a = new Account();
				a.setId(rs.getInt("id"));
				a.setBalance(rs.getFloat("balance"));
				a.setCustomerId(rs.getInt("customer"));
				a.setPending(rs.getBoolean("pending"));
				map.put(a.getId(), a);
			}
			
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Account> getPendingAccounts() {
		String sql = "select * from accounts where pending = true;";
		try {
			List<Account> list = new ArrayList<Account>();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Account a = new Account();
				a.setId(rs.getInt("id"));
				a.setBalance(rs.getFloat("balance"));
				a.setCustomerId(rs.getInt("customer"));
				a.setPending(rs.getBoolean("pending"));
				list.add(a);
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Map<Integer, Account> getAll() {
		Map<Integer, Account> map = new HashMap<Integer, Account>();
		String sql = "select * from accounts;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Account a = new Account();
				a.setId(rs.getInt("id"));
				a.setBalance(rs.getFloat("balance"));
				a.setCustomerId(rs.getInt("customer"));
				a.setPending(rs.getBoolean("pending"));
				map.put(a.getId(), a);
			}
			
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean update(Account a) {
		String sql = "update accounts set balance = ?, pending = ? where id = ? returning *;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setFloat(1, a.getBalance());
			ps.setBoolean(2, a.isPending());
			ps.setInt(3, a.getId());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean delete(Account a) {
		String sql = "delete from accounts where id = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, a.getId());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
