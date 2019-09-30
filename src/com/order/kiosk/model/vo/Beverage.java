package com.order.kiosk.model.vo;

import java.io.Serializable;

public class Beverage extends Menu implements Serializable{
	
//	private String name;
//	private int price;
	private String size;
	private String iceHot;
	
	public Beverage() {}
	
	public Beverage(String name, int price, String size, String iceHot) {
//		this.name = name;
//		this.price = price;
//		super(name,price);
		this.setName(name);
		this.setPrice(price);
		this.size = size;
		this.iceHot = iceHot;
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
	
	public String getSize() {
		return size;
	}

	public String getIceHot() {
		return iceHot;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setIceHot(String iceHot) {
		this.iceHot = iceHot;
	}
	
	@Override
	public String toString() {
		return this.getName()+"/"+this.getPrice()+"/"+size+"/"+iceHot;
	}
}
