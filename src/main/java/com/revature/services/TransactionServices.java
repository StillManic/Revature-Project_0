package com.revature.services;

import java.util.Map;

import com.revature.models.Transaction;

public interface TransactionServices {
	void add(Transaction t);
	Map<Integer, Transaction> getAll();
}
