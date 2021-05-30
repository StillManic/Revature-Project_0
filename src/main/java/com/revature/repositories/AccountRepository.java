package com.revature.repositories;

import java.util.Map;

import com.revature.models.Account;
import com.revature.utils.MockDB;

public class AccountRepository implements GenericRepository<Account> {
	private static AccountRepository instance;
	
	private AccountRepository() {}
	
	public static AccountRepository getInstance() {
		if (instance == null) instance = new AccountRepository();
		return instance;
	}
	
	@Override
	public Account add(Account a) {
		// "a" does not yet have an id, so we need to assign it to the max that we have plus one
		Account maxAccount = MockDB.accounts.values().stream().max((account1, account2) -> account1.getId().compareTo(account2.getId())).orElse(null);
		Integer id = maxAccount != null ? maxAccount.getId() + 1 : 1;
		a.setId(id);
		MockDB.accounts.put(id, a);
		return a;
	}

	@Override
	public Account getById(Integer id) {
		return MockDB.accounts.get(id);
	}

	@Override
	public Map<Integer, Account> getAll() {
		return MockDB.accounts;
	}

	@Override
	public void update(Account a) {
		if (MockDB.accounts.containsKey(a.getId())) {
			MockDB.accounts.get(a.getId()).setBalance(a.getBalance());
		}
	}

	@Override
	public void delete(Account a) {
		MockDB.accounts.remove(a.getId());
	}
}
