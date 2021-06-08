package com.revature.services;

import java.util.Map;

import com.revature.models.Transaction;
import com.revature.repositories.TransactionRepository;

public class TransactionServicesImpl implements TransactionServices {
	private static TransactionServicesImpl instance;
	
	private TransactionServicesImpl() {}
	
	public static TransactionServicesImpl getInstance() {
		if (instance == null) instance = new TransactionServicesImpl();
		return instance;
	}
	
	@Override
	public void add(Transaction t) {
		TransactionRepository.getInstance().add(t);
	}
	
	@Override
	public Map<Integer, Transaction> getAll() {
		return TransactionRepository.getInstance().getAll();
	}
}
