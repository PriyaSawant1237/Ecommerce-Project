package com.ecom.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class UserImpl implements User {

	public static Connection con = null;
	Scanner sc = new Scanner(System.in);
	private int id;
	private float price;
	private int quantity;
	private int quantityLeft;
	private double total = 0;
	String contents = "";

	public UserImpl() {
		System.out.println("\nWelcome to E-Commerce based application !! \n");
		System.out.println("User Operation\r\n" + "1. User Registration\r\n" + "2. User Login\r\n"
				+ "3. User view Product item as Sorted Order\r\n" + "4. Buy Product\r\n" + "5. View Cart \r\n"
				+ "6. Purchase the item \r\n\n" + "Admin Operation\r\n" + "7. Add product item\r\n"
				+ "8. Calculate Bill\r\n" + "9. Display amount to End User\r\n" + "10.Check Quantity\r\n"
				+ "11. Check registered user\r\n" + "12. Check the particular user history\r\n\n"
				+ "Guest Operation\r\n" + "13. View product item\r\n" + "14. Not purchase item\n");
	}

	@Override
	public void userRegistration() {
		try {
			Connection con = DatabaseConnection.getCon();
			PreparedStatement ps = con.prepareStatement("insert into user(firstName ,lastName ,"
					+ "userName ,userPassword ,city ,mailID ,mobileNumber)values(?,?,?,?,?,?,?)");

			System.out.println("Enter first name :");
			String firstname = sc.nextLine();
			ps.setString(1, firstname);
			System.out.println("Enter last name :");
			String lastname = sc.nextLine();
			ps.setString(2, lastname);
			System.out.println("Enter user name :");
			String username = sc.nextLine();
			ps.setString(3, username);
			System.out.println("Enter password :");
			String password = sc.nextLine();
			ps.setString(4, password);
			System.out.println("Enter city :");
			String city = sc.nextLine();
			ps.setString(5, city);
			System.out.println("Enter mail address :");
			String mail = sc.nextLine();
			ps.setString(6, mail);
			System.out.println("Enter mobile number :");
			String mobile = sc.nextLine();
			ps.setString(7, mobile);

			int i = ps.executeUpdate();
			System.out.println("Record is inserted." + i);

			con.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void userLogin() {
		try {
			con = DatabaseConnection.getCon();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM user ");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println("enter username");
				String name = sc.next();
				System.out.println("enter password");
				String password = sc.next();

				if (name.equals(rs.getString("userName")) && password.equals(rs.getString("userPassword"))) {
					System.out.println("Login successful!!");
					break;
				} else {
					System.out.println("Invalid username or password");
				}
			}
			con.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void viewProduct() {
		try {
			con = DatabaseConnection.getCon();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("Select * from product ORDER by prodId,prodName");

			while (rs.next()) {
				System.out.println("Product Id>> " + rs.getInt("prodId"));
				System.out.println("Product Name>> " + rs.getString("prodName"));
				System.out.println("Product Description>> " + rs.getString("prodDescription"));
				System.out.println("Available Quantity>> " + rs.getInt("quantity"));
				System.out.println("Price>> " + rs.getFloat("price"));
				System.out.println();
			}
			con.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void buyProduct() {
		try {
			con = DatabaseConnection.getCon();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM product where prodId=?");
			String keepShopping = "y";
			String viewCart = "y";
			ArrayList<Integer> cart = new ArrayList<>();
			do {
				System.out.println("Enter the product id to buy the product>> ");
				this.id = sc.nextInt();
				cart.add(id);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					System.out.println("Enter the quantity>> ");
					this.quantity = sc.nextInt();
					this.total += this.quantity * rs.getInt("price");
					this.quantityLeft = rs.getInt("quantity") - this.quantity;
					System.out.print("Continue shopping (y/n)? ");
					keepShopping = sc.next();

					contents += rs.getString("prodName") + "\t" + this.quantity + "\t\t" + rs.getFloat("price") + "\n";
				}
			} while (keepShopping.equals("y"));

			System.out.println("Do you want to view cart (y/n)?");
			viewCart = sc.next();
			if (viewCart.equals("y")) {
				viewCart();
			}
//			if (viewCart.equals("y")) {
//				for(int i:cart) {
//					System.out.println(i);
//				}
//			}
			con.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			con = DatabaseConnection.getCon();
			// Insert products into the cart table
			PreparedStatement stmt = con.prepareStatement("INSERT INTO cart (product_id,quantity) VALUES (?,?)");

			stmt.setInt(1, this.id);
			stmt.setInt(2, this.quantity);
			stmt.executeUpdate();
			System.out.println("added");

			con.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void viewCart() {
		con = DatabaseConnection.getCon();
		try {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO cart (user_id, product_id) VALUES (?, ?)");
			{
				int userId = 1;
				stmt.setInt(1, userId);
				stmt.setInt(2, this.id);
				stmt.executeUpdate();
				System.out.println("Product added to cart successfully");
				System.out.println("\nCart details >> ");
				System.out.println("\nItem\t\t\tQuantity\tPrice\t\n");
				System.out.println(contents);
				System.out.println("Your cart total is : " + this.total + " rs.\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
