package com.revature.models;

import java.util.HashMap;
import java.util.Map;

public class Customer {
	private Integer id;
	private String username, password;
	
	private Map<Integer, Account> accounts;
	
	public Customer() {}
	
	public Customer(Integer id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.accounts = new HashMap<Integer, Account>();
	}
	
	public Customer(String username, String password, Account... accounts) {
		this.username = username;
		this.password = password;
		this.accounts = new HashMap<Integer, Account>();
		for (Account a : accounts) this.accounts.put(a.getId(), a);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<Integer, Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Map<Integer, Account> accounts) {
		this.accounts = accounts;
	}
	
	public void addAccount(Account account) {
		if (this.accounts == null) this.accounts = new HashMap<Integer, Account>();
		if (account != null && account.getId() != null && account.getId() >= 1) {
			this.accounts.put(account.getId(), account);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accounts == null) ? 0 : accounts.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		Customer other = (Customer) obj;
		if (accounts == null) {
			if (other.accounts != null) return false;
		} else if (!accounts.equals(other.accounts)) return false;
		
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		
		if (password == null) {
			if (other.password != null) return false;
		} else if (!password.equals(other.password)) return false;
		if (username == null) {
			if (other.username != null) return false;
		} else if (!username.equals(other.username)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", username=" + username + ", password=" + password + ", accounts=" + accounts + "]";
	}
}
