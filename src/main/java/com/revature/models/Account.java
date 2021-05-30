package com.revature.models;

public class Account {
	private Integer id;
	private Integer balance;
	
	public Account() {}
	
	public Account(Integer balance) {
		this.balance = balance;
	}
	
	public Account(Integer id, Integer balance) {
		this.id = id;
		this.balance = balance;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		Account other = (Account) obj;
		if (balance == null) {
			if (other.balance != null) return false;
		} else if (!balance.equals(other.balance)) return false;
		
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", balance=" + balance + "]";
	}
}
