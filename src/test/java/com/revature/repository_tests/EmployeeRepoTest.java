package com.revature.repository_tests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Test;

import com.revature.models.Employee;
import com.revature.repositories.EmployeeRepository;
import com.revature.utils.MockDB;

public class EmployeeRepoTest {
	@Test
	public void addTest() {
		EmployeeRepository repo = EmployeeRepository.getInstance();
		Employee e1 = new Employee(1, "one", "one");
		MockDB.employees.put(e1.getId(), e1);
		Employee e2 = new Employee(2, "two", "two");
		MockDB.employees.put(e2.getId(), e2);
		
		Employee test = new Employee("test", "test");
		Employee result = new Employee(3, "test", "test");
		Assert.assertEquals(repo.add(test), result);
		Assert.assertNotNull(MockDB.employees.get(3));
	}
	
//	@Test
//	public void printTest() {
//		// The string we are testing for is "Employee repo is printing!"
//		// testStream and testPrintStream are used to make sure that the formatting/positions of new lines in the output string
//		// are the same as they would be if System.out.println() was called.
//		ByteArrayOutputStream testStream = new ByteArrayOutputStream();
//		PrintStream testPrintStream = new PrintStream(testStream);
//		testPrintStream.println("Employee repo is printing!");
//		
//		// we have to save the original printstream from System.out so that we can reset it later
//		PrintStream originalPrintStream = System.out;
//		
//		// create a new ByteArrayOutputStream and PrintStream
//		// set System.out to our new PrintStream, this allows us to have access to any strings sent to System.out
//		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//		System.setOut(new PrintStream(outStream));
//		
//		// The method we are testing
//		EmployeeRepository.getInstance().print();
//		
//		// The strings we are comparing
//		String consoleOutput = outStream.toString();
//		String testOutput = testStream.toString();
//		
//		// Do the assert
//		Assert.assertEquals(consoleOutput, testOutput);
//		
//		// Reset System.out
//		System.setOut(originalPrintStream);
//		
//		// Close our testPrintStream
//		testPrintStream.close();
//	}
}
