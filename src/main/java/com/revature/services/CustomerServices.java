package com.revature.services;

import java.util.Scanner;

public interface CustomerServices {
	boolean login(Scanner scanner);
	boolean signUp(Scanner scanner);
	boolean logout();
}
