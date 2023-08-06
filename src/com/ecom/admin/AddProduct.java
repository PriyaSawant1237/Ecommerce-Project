package com.ecom.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import com.ecom.user.DatabaseConnection;

public class AddProduct {

	public void addProduct() {
		Scanner sc = new Scanner(System.in);

		try {
			Connection con = DatabaseConnection.getCon();
			PreparedStatement ps = con
					.prepareStatement("insert into product( prodName, prodDescription, quantity, price)values(?,?,?,?)");

			System.out.println("Enter the Product Name");
			String prodName = sc.nextLine();
			ps.setString(1, prodName);

			System.out.println("Enter the Product Description");
			String prodDesc = sc.nextLine();
			ps.setString(2, prodDesc);

			System.out.println("Enter the Quantity");
			int quantity = sc.nextInt();
			ps.setInt(3, quantity);

			System.out.println("Enter the Price");
			int price = sc.nextInt();
			ps.setInt(4, price);

			int i = ps.executeUpdate();
			System.out.println("Product Item has been added successfully!!!" + i);

			// close resources
			sc.close();
			ps.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
