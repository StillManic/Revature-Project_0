package com.revature.models;

public class Employee extends Customer {
	public Employee() {}
	
	public Employee(Integer id, String username, String password) {
		super(id, username, password);
	}
	
	public Employee(String username, String password, Account... accounts) {
		super(username, password, accounts);
	}

	@Override
	public String toString() {
		return "Employee [id=" + getId() + ", username=" + getUsername() + ", password=" + getPassword() + ", accounts=" + getAccounts() + "]";
	}
}
