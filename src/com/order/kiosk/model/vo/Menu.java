package com.order.kiosk.model.vo;

import java.io.Serializable;

public class Menu implements Serializable {
	
	private String name;
	private int price;
	
	public Menu() {}
	
	// 메뉴 생성자
	public Menu(String name, int price) {
		this.name = name;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	private void readObjectNoData() {
		
	}
}
