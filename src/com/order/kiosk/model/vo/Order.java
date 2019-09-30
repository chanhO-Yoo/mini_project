package com.order.kiosk.model.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
	private ArrayList<Beverage> beverageList;
	private ArrayList<Food> foodList;
	private ArrayList<Product> productList;
	
	public Order() {
		
	}
	
	@Override
	public String toString() {
		return "(beverageList=" + beverageList + ", foodList=" + foodList + ", productList=" + productList + ")";
	}

	public ArrayList<Beverage> getBeverageList() {
		return beverageList;
	}
	public ArrayList<Food> getFoodList() {
		return foodList;
	}
	public ArrayList<Product> getProductList() {
		return productList;
	}
	public void setBeverageList(ArrayList<Beverage> beverageList) {
		this.beverageList = beverageList;
	}
	public void setFoodList(ArrayList<Food> foodList) {
		this.foodList = foodList;
	}
	public void setProductList(ArrayList<Product> productList) {
		this.productList = productList;
	}
}
