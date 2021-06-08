package com.revature.models;

public class Account {
	private Integer id;
	private Float balance;
	private Integer customer_id;
	private Boolean pending;
	
	public Account() {}
	
	public Account(Float balance) {
		this.balance = balance;
	}
	
	public Account(Integer id, Float balance) {
		this.id = id;
		this.balance = balance;
		this.pending = true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}
	
	public Integer getCustomerId() {
		return this.customer_id;
	}
	
	public void setCustomerId(Integer customer_id) {
		this.customer_id = customer_id;
	}
	
	public Boolean isPending() {
		return this.pending;
	}
	
	public void setPending(Boolean pending) {
		this.pending = pending;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((customer_id == null) ? 0 : customer_id.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((pending == null) ? 0 : pending.hashCode());
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
		
		if (customer_id == null) {
			if (other.customer_id != null) return false;
		} else if (!customer_id.equals(other.customer_id)) return false;
		
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		
		if (pending == null) {
			if (other.pending != null) return false;
		} else if (!pending.equals(other.pending)) return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", balance=" + balance + ", customer_id=" + customer_id + ", pending=" + pending + "]";
	}
}
