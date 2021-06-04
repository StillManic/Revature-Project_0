package com.revature.repositories;

import java.util.Map;

public interface GenericRepository<T> {
	// Create
	public T add(T t);
	
	// Read
	public T getById(Integer id);
	public Map<Integer, T> getAll();
	
	// Update
	public boolean update(T t);
	
	// Delete
	public boolean delete(T t);
}
