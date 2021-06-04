package com.revature.repositories;

import java.sql.Connection;
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
		return null;
	}

	@Override
	public Transaction getById(Integer id) {
		return null;
	}

	@Override
	public Map<Integer, Transaction> getAll() {
		return null;
	}

	@Override
	public boolean update(Transaction t) {
		return false;
	}

	@Override
	public boolean delete(Transaction t) {
		return false;
	}

}
