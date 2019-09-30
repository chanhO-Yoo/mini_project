package com.order.kiosk.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.order.kiosk.model.vo.Card;
import com.order.kiosk.model.vo.Order;

public class KioskController {

	private int i = 0;
	private ArrayList<Order> existList = new ArrayList<>();
	private Map<String,Card> existCardMap = new HashMap<>();
	
	private Order order;
	private Card card;
	
	SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
	Date today = new Date();
	String day = format1.format(today);
	
	File fileName = new File(day+".ser");
	File cardFileName = new File("cardList.ser");
	
	public void fileWrite(ArrayList<Order> orderList) {
		if(fileName.exists() == true) {
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			ObjectInputStream ois = null;
			
			try {
				fis = new FileInputStream(fileName);
				bis = new BufferedInputStream(fis);
				ois = new ObjectInputStream(bis);
				
				existList = (ArrayList<Order>)ois.readObject();
				
				for(int orderListNum=0; orderListNum<orderList.size(); orderListNum++) {
					existList.add(orderList.get(orderListNum));
				}

				FileOutputStream fos = null;
				BufferedOutputStream bos = null;
				ObjectOutputStream oos = null;
				
				try {
					fos = new FileOutputStream(fileName);
					bos = new BufferedOutputStream(fos);
					oos = new ObjectOutputStream(bos);
					
					
					oos.writeObject(existList);
					
				}catch (IOException e) {
					e.printStackTrace();
				}finally {
					try {
						oos.close();
					}catch(IOException e) {
						e.printStackTrace();
					}
				}
				
			} catch(IOException e) {
				e.printStackTrace();
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			} finally{
				try {
					ois.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			
			
		}
		else {
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			ObjectOutputStream oos = null;
			
			try {
				fos = new FileOutputStream(fileName);
				bos = new BufferedOutputStream(fos);
				oos = new ObjectOutputStream(bos);

				oos.writeObject(orderList);
				
			}catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					oos.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public ArrayList<Order> fileRead() {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		ObjectInputStream ois = null;
		
		try {
			
			fis = new FileInputStream(fileName);
			bis = new BufferedInputStream(fis);
			ois = new ObjectInputStream(bis);
			
			existList = (ArrayList<Order>)ois.readObject();

		} catch(IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			try {
				ois.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return existList;
	}
	
	// 카드 리스트 파일 저장 및 읽기
	public void fileWriteCard(Map<String,Card> cardMap) {
		if(cardFileName.exists() == true) {
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			ObjectInputStream ois = null;
			
			try {
				fis = new FileInputStream(cardFileName);
				bis = new BufferedInputStream(fis);
				ois = new ObjectInputStream(bis);
				
				existCardMap = (Map<String,Card>)ois.readObject();
				
				for(String key : cardMap.keySet()) {
					existCardMap.put(key, cardMap.get(key));
				}
				
//				for(int cardListNum=0; cardListNum<cardList.size(); cardListNum++) {
//					existCardList.add(cardList.get(cardListNum));
//				}

				FileOutputStream fos = null;
				BufferedOutputStream bos = null;
				ObjectOutputStream oos = null;
				
				try {
					fos = new FileOutputStream(cardFileName);
					bos = new BufferedOutputStream(fos);
					oos = new ObjectOutputStream(bos);
					
					
					oos.writeObject(existCardMap);
					
				}catch (IOException e) {
					e.printStackTrace();
				}finally {
					try {
						oos.close();
					}catch(IOException e) {
						e.printStackTrace();
					}
				}
				
			} catch(IOException e) {
				e.printStackTrace();
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			} finally{
				try {
					ois.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			
			
		}
		else {
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			ObjectOutputStream oos = null;
			
			try {
				fos = new FileOutputStream(cardFileName);
				bos = new BufferedOutputStream(fos);
				oos = new ObjectOutputStream(bos);

				oos.writeObject(cardMap);
				
			}catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					oos.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public Map<String,Card> fileReadCard() {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		ObjectInputStream ois = null;
		
		try {
			
			fis = new FileInputStream(cardFileName);
			bis = new BufferedInputStream(fis);
			ois = new ObjectInputStream(bis);
			
			existCardMap = (Map<String,Card>)ois.readObject();

		} catch(IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			try {
				ois.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return existCardMap;
	}
}
