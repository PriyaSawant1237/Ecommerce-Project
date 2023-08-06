package com.ecom.user;

import java.util.Scanner;

public class Test {
	public static void main(String args[]) {

		Scanner sc = new Scanner(System.in);
		User user = new UserImpl();
		
		while (true) {
			System.out.println("\nEnter your choice : ");
			int choice = sc.nextInt();
			System.out.println("");
			switch (choice) {
			case 1:
				user.userRegistration();
				break;
			case 2:
				user.userLogin();
				break;
			case 3:
				user.viewProduct();
				break;
			case 4:
				user.buyProduct();
				break;
			case 5:
				user.viewCart();
				break;
			case 6:
				return;
			}
		}
	}
}
