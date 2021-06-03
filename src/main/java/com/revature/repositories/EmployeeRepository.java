package com.revature.repositories;

import java.util.Map;

import com.revature.models.Employee;
import com.revature.utils.MockDB;

public class EmployeeRepository implements GenericRepository<Employee> {
	private static EmployeeRepository instance;
	
	private EmployeeRepository() {}
	
	public static EmployeeRepository getInstance() {
		if (instance == null) instance = new EmployeeRepository();
		return instance;
	}
	
	@Override
	public Employee add(Employee e) {
		// "e" does not yet have an id, so we need to assign it to the max that we have plus one
		Employee maxEmployee = MockDB.employees.values().stream().max((employee1, employee2) -> employee1.getId().compareTo(employee2.getId())).orElse(null);
		Integer id = maxEmployee != null ? maxEmployee.getId() + 1 : 1;
		e.setId(id);
		MockDB.employees.put(id, e);
		return e;
	}

	@Override
	public Employee getById(Integer id) {
		return MockDB.employees.get(id);
	}

	@Override
	public Map<Integer, Employee> getAll() {
		return MockDB.employees;
	}

	@Override
	public void update(Employee e) {
		if (MockDB.employees.containsKey(e.getId())) {
			Employee storedEmployee = MockDB.employees.get(e.getId());
			storedEmployee.setUsername(e.getUsername());
			storedEmployee.setPassword(e.getPassword());
			storedEmployee.setAccounts(e.getAccounts());
		}
	}

	@Override
	public void delete(Employee e) {
		MockDB.employees.remove(e.getId());
	}
	
//	public void print() {
//		System.out.println("Employee repo is printing!");
//	}
}
