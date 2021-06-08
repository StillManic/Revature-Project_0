package com.revature.repositories;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.revature.models.Transaction;
import com.revature.utils.JDBCConnection;

public class TransactionRepository implements GenericRepository<Transaction> {
	private static TransactionRepository instance;
	private Connection conn = JDBCConnection.getConnection();
	
	private TransactionRepository() {}
	
	public static TransactionRepository getInstance() {
		if (instance == null) instance = new TransactionRepository();
		return instance;
	}
	
	@Override
	public Transaction add(Transaction t) {
		String sql = "insert into transactions values (default, ?, ?, ?, ?) returning *;";
//		String sql = "call \"Project_0\".log_transaction(?, ?, ?, ?)";
		try {
//			CallableStatement cs = conn.prepareCall(sql);
//			cs.setInt(1, t.getSource().getId());
//			cs.setString(2, t.getType());
//			cs.setFloat(3, t.getAmount());
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, t.getSource().getId());
			ps.setString(2, t.getType());
			ps.setFloat(3, t.getAmount());
			
//			if (t.getReceiver() != null && t.getType().equalsIgnoreCase("transfer")) cs.setInt(4, t.getReceiver().getId());
//			else cs.setInt(4, t.getSource().getId());
			
			if (t.getReceiver() != null && t.getType().equalsIgnoreCase("transfer")) ps.setInt(4, t.getReceiver().getId());
			else ps.setInt(4, t.getSource().getId());
			
//			ResultSet rs = cs.executeQuery();
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				t.setId(rs.getInt("id"));
				return t;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Transaction getById(Integer id) {
		String sql = "select * from transactions where id = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Transaction t = new Transaction();
				t.setId(rs.getInt("id"));
				t.setSource(AccountRepository.getInstance().getById(rs.getInt("source")));
				t.setType(rs.getString("type"));
				t.setAmount(rs.getFloat("amount"));
				
				if (t.getType().equalsIgnoreCase("transfer")) {
					t.setReceiver(AccountRepository.getInstance().getById(rs.getInt("receiver")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Map<Integer, Transaction> getAll() {
		Map<Integer, Transaction> map = new HashMap<Integer, Transaction>();
		String sql = "select * from transactions;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Transaction t = new Transaction();
				t.setId(rs.getInt("id"));
				t.setSource(AccountRepository.getInstance().getById(rs.getInt("source")));
				t.setType(rs.getString("type"));
				t.setAmount(rs.getFloat("amount"));
				
				if (t.getType().equalsIgnoreCase("transfer")) {
					t.setReceiver(AccountRepository.getInstance().getById(rs.getInt("receiver")));
				}
				
				map.put(t.getId(), t);
			}
			
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean update(Transaction t) {
//		String sql = "update transactions set type = ?, amount = ? returning *;";
		String sql = "call \"Project_0\".update_transaction(?, ?, ?)";
		try {
			CallableStatement cs = conn.prepareCall(sql);
			cs.setInt(1, t.getId());
			cs.setString(2, t.getType());
			cs.setFloat(3, t.getAmount());
			return cs.execute();
//			cs.executeQuery();
//			return true;
			
//			PreparedStatement ps = conn.prepareStatement(sql);
//			ps.setString(1, t.getType());
//			ps.setFloat(2, t.getAmount());
//			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean delete(Transaction t) {
		String sql = "delete from transactions where id = ?;";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, t.getId());
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
