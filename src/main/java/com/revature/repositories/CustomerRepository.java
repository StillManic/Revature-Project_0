package com.revature.repositories;

import java.util.Map;

import com.revature.models.Customer;
import com.revature.utils.MockDB;

public class CustomerRepository implements GenericRepository<Customer> {
	private static CustomerRepository instance;
	
	private CustomerRepository() {}
	
	public static CustomerRepository getInstance() {
		if (instance == null) instance = new CustomerRepository();
		return instance;
	}
	
	public Customer getByUsernameAndPassword(String username, String password) {
		if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
			return MockDB.customers.values().stream().filter((c) -> c.getUsername().equals(username) && c.getPassword().equals(password)).findFirst().orElse(null);
		}
		return null;
	}
	
	@Override
	public Customer add(Customer c) {
		// "c" does not yet have an id, so we need to assign it to the max that we have plus one
		Customer maxCustomer = MockDB.customers.values().stream().max((customer1, customer2) -> customer1.getId().compareTo(customer2.getId())).orElse(null);
		Integer id = maxCustomer != null ? maxCustomer.getId() + 1 : 1;
		c.setId(id);
		MockDB.customers.put(id, c);
		return c;
	}

	@Override
	public Customer getById(Integer id) {
		return MockDB.customers.get(id);
	}

	@Override
	public Map<Integer, Customer> getAll() {
		return MockDB.customers;
	}

	@Override
	public void update(Customer c) {
		if (MockDB.customers.containsKey(c.getId())) {
			Customer storedCustomer = MockDB.customers.get(c.getId());
			storedCustomer.setUsername(c.getUsername());
			storedCustomer.setPassword(c.getPassword());
			storedCustomer.setAccounts(c.getAccounts());
		}
	}

	@Override
	public void delete(Customer c) {
		MockDB.customers.remove(c.getId());
	}
}
