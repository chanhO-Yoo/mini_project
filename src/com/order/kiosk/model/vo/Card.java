package com.order.kiosk.model.vo;

import java.io.Serializable;

public class Card implements Serializable{
	private String cardNo;
	private int cardBalance;
	
	public Card() {
		
	}
	public Card(String cardNo, int cardBalance) {
		this.cardNo = cardNo;
		this.cardBalance = cardBalance;
	}
	
	public String getCardNo() {
		return cardNo;
	}
	public int getCardBalance() {
		return cardBalance;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public void setCardBalance(int cardBalance) {
		this.cardBalance = cardBalance;
	}
	@Override
	public String toString() {
		return "[cardNo=" + cardNo + ", cardBalance=" + cardBalance + "]";
	}
	
	
}
