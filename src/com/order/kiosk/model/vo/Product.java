package com.order.kiosk.model.vo;

import java.io.Serializable;

import com.order.kiosk.model.vo.Menu;

public class Product extends Menu implements Serializable {
	
//	private String name;
//	private int price;
	private String wrapping;
	
	public Product() {}

	public Product(String name, int price, String wrapping) {
//		this.name = name;
//		this.price = price;
		this.setName(name);
		this.setPrice(price);
		this.wrapping = wrapping;
	}
	
//	public String getName() {
//		return name;
//	}
//
//	public int getPrice() {
//		return price;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public void setPrice(int price) {
//		this.price = price;
//	}
	
	public String getWrapping() {
		return wrapping;
	}

	public void setWrapping(String wrapping) {
		this.wrapping = wrapping;
	}
	
	@Override
	public String toString() {
		return this.getName()+"/"+this.getPrice()+"/"+wrapping;
	}
	
}
