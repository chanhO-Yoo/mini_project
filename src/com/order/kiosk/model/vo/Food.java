package com.order.kiosk.model.vo;

import java.io.Serializable;

public class Food extends Menu implements Serializable {

//	private String name;
//	private int price;
	private String heat;
	
	public Food() {}
	public Food(String name, int price, String heat) {
//		this.name = name;
//		this.price = price;
		this.setName(name);
		this.setPrice(price);
		this.heat = heat;
	}
	
//	@Override
//	public String getName() {
//		return name;
//	}
//	@Override
//	public int getPrice() {
//		return price;
//	}
//	@Override
//	public void setName(String name) {
//		this.name = name;
//	}
//	@Override
//	public void setPrice(int price) {
//		this.price = price;
//	}
	
	public String getHeat() {
		return heat;
	}
	public void setHeat(String heat) {
		this.heat = heat;
	}
	
	@Override
	public String toString() {
		return this.getName()+"/"+this.getPrice()+"/"+heat;
	}
}