package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.order.kiosk.controller.KioskController;
import com.order.kiosk.model.vo.Beverage;
import com.order.kiosk.model.vo.Card;
import com.order.kiosk.model.vo.Food;
import com.order.kiosk.model.vo.Order;
import com.order.kiosk.model.vo.Product;

public class Kiosk extends JFrame {
	private Order order;
	private Card card;
	// 컨트롤러
	private KioskController kc;
	// 레이아웃
	private CardLayout cardLayout;
	// 컨테이너
	private JPanel currentPanel;
	private JPanel startPanel;
	private JPanel mainPanel;
	private JPanel listPanel;
	private JPanel beverageList;
	private JPanel foodList;
	private JPanel productList;
	private JPanel resultPanel;
	private JPanel cartPanel;
	private JPanel totalPanel;
	private JPanel adminPanel;

	// 이벤트 컴포넌트
	private JButton btnBuy;
	private JButton btnAdmin;
	private JButton[] beverage_choose = new JButton[9];
	private JButton[] food_choose = new JButton[9];
	private JButton[] product_choose = new JButton[9];
	private JButton pay;
	private JButton cancel;
	private JButton okPay;
	private JButton cancelPay;
	private JButton btnGoStart;
	private JButton btnAddCard;
	private JButton addCard;
	private JButton addCardCancel;
	private JButton cardNumCheck;
	private JButton cardPaySuccess;
	private JButton cashCheck;
	private JButton cashPaySuccess;
	private JButton cardPayCancel;
	private JButton cashPayCancel;
	private JButton beverageCancle;
	private JButton foodCancle;
	private JButton productCancle;
	private JButton btnShowCard;
	
	private JTable cartTable;
	
	private JTextArea cart;
	
	private JLabel endPrice;
	private JLabel totalSales;
	private JLabel totalCount;
	private JLabel beverageSales;
	private JLabel beverageCount;
	private JLabel foodSales;
	private JLabel foodCount;
	private JLabel productSales;
	private JLabel productCount;

	// 사용자 입력값 저장
	private String beverageName;
	private int beveragePrice;
	private String foodName;
	private int foodPrice;
	private String productName;
	private int productPrice;
	private String size;
	private String iceHot;
	private String heat;
	private String wrap;
	private String addCart = "";
	private String adminPassword = "admin";
	private DefaultTableModel model;

	private int totalPrice = 0;
	private int cardBalance; // 카드 초기 잔액 설정
	
	private int beverageIndex=0;
	private int foodIndex=0;
	private int productIndex=0;
	
	private ArrayList<Order> orderList = new ArrayList<>();
	private ArrayList<Beverage> beverageArr = new ArrayList<>();
	private ArrayList<Food> foodArr = new ArrayList<>();
	private ArrayList<Product> productArr = new ArrayList<>();
	private Map<String, Card> cardMap = new HashMap<>();
	
	
	public Kiosk(KioskController kc) {
		this.kc = kc;

		configureFrame(); // 프레임 기본 설정
		addStartPanel(); // 시작 패널 생성
		addMainPanel(); // 메인 패널 생성(메뉴 선택)
		addAdminPanel(); // 관리자 패널 생성

		add(startPanel);
		setVisible(true);
	}

	// 프레임 기본 설정
	private void configureFrame() {
		setSize(910, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
		Image img = new ImageIcon("images/logo.png").getImage();
		setIconImage(img);
		setTitle("커피 주문");
	}

	// 시작 패널 생성
	private void addStartPanel() {
		Image img = new ImageIcon("images/ad.png").getImage();
		startPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(img, 0, 0, null);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		startPanel.setBounds(0, 0, 900, 1000);
		startPanel.setLayout(null);

		btnBuy = new JButton(new ImageIcon("images/icon/order.png"));
		btnBuy.setBounds(200, 725, 500, 200);
		btnBuy.setFont(new Font("돋움", Font.BOLD, 60));
		//버튼 배경 투명화
        btnBuy.setBorderPainted(false);
        btnBuy.setContentAreaFilled(false);
        btnBuy.setOpaque(false);
        btnBuy.setFocusPainted(false);

		btnAdmin = new JButton(new ImageIcon("images/icon/admin.png"));
		btnAdmin.setBounds(750, 850, 100, 100);
		//버튼 배경 투명화
        btnAdmin.setBorderPainted(false);
        btnAdmin.setContentAreaFilled(false);
        btnAdmin.setOpaque(false);
        btnAdmin.setFocusPainted(false);
        
		// 관리자 버튼 누르면 adminPanel로 패널 교체
		btnAdmin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new newAdminWindow();
			}
		});

		// 구매하기 버튼 리스너 - 버튼 클릭시 메인 패널로 패널 교체
		btnBuy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentPanel = startPanel;
				changePanel(mainPanel);
				initKiosk();
			}
		});

		startPanel.add(btnBuy);
		startPanel.add(btnAdmin);
	}

	private void addMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 900, 1000);
		mainPanel.setLayout(null);

		// 메인 패널 중 상단 패널 - 종류 선택 패널
		JPanel categoryPanel = new JPanel();
		categoryPanel.setSize(900, 200);
		categoryPanel.setLayout(null);

		// 상단 패널에 버튼 3개 추가
		JButton selectBeverage = new JButton("음료", new ImageIcon("images/icon/beverageIcon.jpg"));
		selectBeverage.setBounds(0, 0, 300, 200);
		selectBeverage.setContentAreaFilled(false);
		selectBeverage.setOpaque(false);
		selectBeverage.setFocusPainted(false);

		JButton selectFood = new JButton("음식", new ImageIcon("images/icon/foodIcon.jpg"));
		selectFood.setBounds(300, 0, 300, 200);
		selectFood.setContentAreaFilled(false);
		selectFood.setOpaque(false);
		selectFood.setFocusPainted(false);

		JButton selectProduct = new JButton("상품", new ImageIcon("images/icon/productIcon.jpg"));
		selectProduct.setBounds(600, 0, 300, 200);
		selectProduct.setContentAreaFilled(false);
		selectProduct.setOpaque(false);
		selectProduct.setFocusPainted(false);

		categoryPanel.add(selectBeverage);
		categoryPanel.add(selectFood);
		categoryPanel.add(selectProduct);

		mainPanel.add(categoryPanel);

		// 메인 패널 중 중단 패널 - 메뉴 선택 패널
		cardLayout = new CardLayout();
		listPanel = new JPanel();
		listPanel.setBounds(0, 200, 900, 500);
		listPanel.setLayout(cardLayout);

		// 카테고리 선택 이벤트 리스너 등록
		MenuListListener listener = new MenuListListener();
		selectBeverage.addActionListener(listener);
		selectFood.addActionListener(listener);
		selectProduct.addActionListener(listener);

		// 빈 패널
		JPanel nonePanel = new JPanel();
		nonePanel.setBackground(Color.white);
		nonePanel.setBounds(0, 0, 900, 500);

		// 음료 이름 배열 지정
		String[] beverageImageName = { "americano", "caffelatte", "caffemocha", "cappuccino", "hotchoco", "matchalatte",
				"chaitealatte", "dolcelatte", "greentealatte" };

		String[] beverageName = { "아메리카노", "카페라떼", "카페모카", "카푸치노", "핫초콜릿", "말차샷라떼", "차이티라떼", "돌체라떼", "그린티라떼" };
		int[] beveragePrice = { 3600, 4100, 4600, 4100, 4800, 5600, 4600, 5100, 5400 };
		
		// 음료 리스트 패널
		beverageList = new JPanel();
		beverageList.setBounds(0, 0, 900, 500);
		beverageList.setLayout(new GridLayout(3, 3));
		for (int i = 0; i < 9; i++) {
			beverage_choose[i] = new JButton(new ImageIcon("images/beverage/" + beverageImageName[i] + ".jpg"));
			beverage_choose[i].setText(beverageName[i].toString() + "/" + beveragePrice[i]);
			beverage_choose[i].setBorderPainted(false);
			beverage_choose[i].setContentAreaFilled(true);
			beverageList.add(beverage_choose[i]);
			BeverageListener beverageListener = new BeverageListener();
			beverage_choose[i].addActionListener(beverageListener);
		}

		// 음식 리스트 패널
		foodList = new JPanel();
		foodList.setBounds(0, 0, 900, 500);
		foodList.setLayout(new GridLayout(3, 3));
		String[] foodImageName = { "baconcheesetoast", "bananacake", "cheesebagel", "chocolatecake", "ciabatta",
				"classicscone", "greenteamuffin", "heartpie", "walnut" };

		String[] foodName = { "베이컨치즈토스트", "바나나케이크", "치즈베이글", "초코케이크", "치아바타", "클래식스콘", "녹차머핀", "하트파이", "호두" };
		int[] foodPrice = { 4900, 4600, 2800, 5900, 5800, 3300, 4200, 3200, 3400 };
		for (int i = 0; i < 9; i++) {
			food_choose[i] = new JButton(new ImageIcon("images/food/" + foodImageName[i] + ".jpg"));
			food_choose[i].setText(foodName[i].toString() + "/" + foodPrice[i]);
			food_choose[i].setBorderPainted(false);
			food_choose[i].setContentAreaFilled(true);
			foodList.add(food_choose[i]);
			FoodListener foodListener = new FoodListener();
			food_choose[i].addActionListener(foodListener);
		}

		// 상품 리스트 패널
		productList = new JPanel();
		productList.setBounds(0, 0, 900, 500);
		productList.setLayout(new GridLayout(3, 3));
		String[] productImageName = { "classicWhiteBottle", "classicWhiteGlass", "creativeTumbler", "marbleWhiteBottle",
				"parkEcobag", "realDaeguTumbler", "reserveBlueCardHolder", "reserveGoldHandleGlass",
				"whiteFlatColdcup" };

		String[] productName = { "클래식화이트보틀", "클래식화이트글라스", "크리에이티브텀블러", "마블화이트보틀", "파크에코백", "리얼대구텀블러", "리저브블루카드홀더",
				"리저브골드핸들글라스", "화이트플랫콜드컵" };
		int[] productPrice = { 43000, 12000, 11000, 38000, 27000, 17000, 23000, 17000, 17000 };

		for (int i = 0; i < 9; i++) {
			product_choose[i] = new JButton(new ImageIcon("images/product/" + productImageName[i] + ".jpg"));
			product_choose[i].setText(productName[i].toString() + "/" + productPrice[i]);
			product_choose[i].setBorderPainted(false);
			product_choose[i].setContentAreaFilled(true);
			productList.add(product_choose[i]);
			ProductListener productListener = new ProductListener();
			product_choose[i].addActionListener(productListener);
		}

		listPanel.add("none", nonePanel);
		listPanel.add("bl", beverageList);
		listPanel.add("fl", foodList);
		listPanel.add("pl", productList);

		mainPanel.add(listPanel);

		// 메인 패널 중 하단 패널
		resultPanel = new JPanel();
		resultPanel.setBackground(Color.GRAY);
		resultPanel.setBounds(0, 700, 900, 300);
		resultPanel.setLayout(null);

		// 선택한 메뉴 출력 패널
		cartPanel = new JPanel();
		cartPanel.setBounds(0, 0, 900, 200);
		cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));

		// 제목이있는 border추가
		TitledBorder titleBorderCart = new TitledBorder(new LineBorder(Color.BLACK), "선택 상품");
		titleBorderCart.setTitleFont(new Font("CookieRunOTF Bold", Font.BOLD, 20));
		cartPanel.setBorder(titleBorderCart);

		//테이블 추가
		String[] header = {"종류/번호","주문내역"};
		String[][] contents = {{""}};
		model = new DefaultTableModel(contents,header);
		cartTable = new JTable(model);
		cartTable.getColumn("종류/번호").setPreferredWidth(200);
		cartTable.getColumn("주문내역").setPreferredWidth(600);
		JScrollPane scrollPane = new JScrollPane(cartTable);
		
		JButton orderCancelBtn = new JButton("삭제");
		Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalStrut(800));
        box.add(orderCancelBtn);
        
		orderCancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cartTable.getSelectedRow()==-1) {
					return;
				}
				else {
					String removeItem = cartTable.getValueAt(cartTable.getSelectedRow(), 0).toString();
					String[] removeItemArr = removeItem.split("/");
					if(removeItemArr[0].equals("beverage")) {
						totalPrice -= beverageArr.get(Integer.parseInt(removeItemArr[1])).getPrice();
						endPrice.setText(Integer.toString(totalPrice)+"원");
						beverageArr.remove(Integer.parseInt(removeItemArr[1]));
					}
					else if(removeItemArr[0].equals("food")) {
						totalPrice -= foodArr.get(Integer.parseInt(removeItemArr[1])).getPrice();
						endPrice.setText(Integer.toString(totalPrice)+"원");
						foodArr.remove(Integer.parseInt(removeItemArr[1]));
					}
					else if(removeItemArr[0].equals("product")) {
						totalPrice -= productArr.get(Integer.parseInt(removeItemArr[1])).getPrice();
						endPrice.setText(Integer.toString(totalPrice)+"원");
						productArr.remove(Integer.parseInt(removeItemArr[1]));
					}
					model.removeRow(cartTable.getSelectedRow());
				}
			}
		});
		
		cartPanel.add(scrollPane);
		cartPanel.add(box);

		// 선택한 메뉴의 금액 합계 패널
		totalPanel = new JPanel();
		totalPanel.setBackground(Color.black);
		totalPanel.setBounds(0, 200, 900, 100);
		totalPanel.setLayout(null);

		// 총 금액 표시할 라벨
		endPrice = new JLabel();
		endPrice.setText(totalPrice + "원");
		endPrice.setForeground(Color.WHITE);
		endPrice.setFont(new Font("돋움", Font.BOLD, 25));
		endPrice.setBounds(250, 5, 600, 50);
		totalPanel.add(endPrice);

		pay = new JButton("결제");
		pay.setBounds(600, 5, 100, 50);
		pay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                if(!(beverageArr.isEmpty() && foodArr.isEmpty() && productArr.isEmpty())) {
                    new newPayWindow();
                }
                else {
                	return;
                }
			}
		});
		

		cancel = new JButton("취소");
		cancel.setBounds(720, 5, 100, 50);
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				currentPanel = mainPanel;
				changePanel(startPanel);
			}
		});

		totalPanel.add(pay);
		totalPanel.add(cancel);
		resultPanel.add(cartPanel);
		resultPanel.add(totalPanel);
		mainPanel.add(resultPanel);
	}

	private void addAdminPanel() {
		adminPanel = new JPanel();
		adminPanel.setBackground(Color.white);
		adminPanel.setBounds(0, 0, 900, 1000);
    	adminPanel.setLayout(null);

		JLabel titleLabel = new JLabel("관리자 페이지");
		titleLabel.setBounds(400, 50, 200, 50);
		
		JLabel totalSalesLabel = new JLabel("총 매출액");
		totalSalesLabel.setBounds(175,100,100,50);
		totalSales = new JLabel("원");
		totalSales.setBounds(175,150,100,50);
		JLabel totalCountLabel = new JLabel("총 판매 수량");
		totalCountLabel.setBounds(575,100,100,50);
		totalCount = new JLabel("개");
		totalCount.setBounds(575,150,100,50);
		
		JLabel beverageSalesLabel = new JLabel("총 음료 매출액");
		beverageSalesLabel.setBounds(175,200,100,50);
		beverageSales = new JLabel("원");
		beverageSales.setBounds(175,250,100,50);
		JLabel beverageCountLabel = new JLabel("총 음료 판매 수량");
		beverageCountLabel.setBounds(575,200,100,50);
		beverageCount = new JLabel("개");
		beverageCount.setBounds(575,250,100,50);
		
		JLabel foodSalesLabel = new JLabel("총 음식 매출액");
		foodSalesLabel.setBounds(175,300,100,50);
		foodSales = new JLabel("원");
		foodSales.setBounds(175,350,100,50);
		JLabel foodCountLabel = new JLabel("총 음식 판매 수량");
		foodCountLabel.setBounds(575,300,100,50);
		foodCount = new JLabel("개");
		foodCount.setBounds(575,350,100,50);

		JLabel productSalesLabel = new JLabel("총 상품 매출액");
		productSalesLabel.setBounds(175,400,100,50);
		productSales = new JLabel("원");
		productSales.setBounds(175,450,100,50);
		JLabel productCountLabel = new JLabel("총 상품 판매 수량");
		productCountLabel.setBounds(575,400,100,50);
		productCount = new JLabel("개");
		productCount.setBounds(575,450,100,50);
		
		JPanel cardListPanel = new JPanel();
		cardListPanel.setBounds(175,550,550,200);
		cardListPanel.setLayout(new BoxLayout(cardListPanel, BoxLayout.Y_AXIS));
		
		JTextArea cardListArea = new JTextArea(2,10);
		cardListArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(cardListArea);
		cardListPanel.add(scrollPane);
		
		btnGoStart = new JButton("처음으로");
		btnGoStart.setBounds(575, 750, 150, 50);
		btnGoStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentPanel = adminPanel;
				changePanel(startPanel);
			}
		});
		
		btnShowCard = new JButton("새로고침");
        btnShowCard.setBounds(375,750,150,50);
        btnShowCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	cardListArea.setText("");
                String cardListStr = "";
                for(String key : kc.fileReadCard().keySet()) {
                	cardListStr =  kc.fileReadCard().get(key).toString()+"\n";
                	cardListArea.append(cardListStr);
                }
            }
        });
		
		btnAddCard = new JButton("카드추가/충전");
		btnAddCard.setBounds(175,750,150,50);
		btnAddCard.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new newCardWindow();
				
			}
		});
		

		adminPanel.add(totalSalesLabel);
		adminPanel.add(totalSales);
		adminPanel.add(totalCountLabel);
		adminPanel.add(totalCount);
		
		adminPanel.add(beverageSalesLabel);
		adminPanel.add(beverageSales);
		adminPanel.add(beverageCountLabel);
		adminPanel.add(beverageCount);

		adminPanel.add(foodSalesLabel);
		adminPanel.add(foodSales);
		adminPanel.add(foodCountLabel);
		adminPanel.add(foodCount);
		
		adminPanel.add(productSalesLabel);
		adminPanel.add(productSales);
		adminPanel.add(productCountLabel);
		adminPanel.add(productCount);

		adminPanel.add(titleLabel);
		adminPanel.add(cardListPanel);
		adminPanel.add(btnGoStart);
		adminPanel.add(btnShowCard);
		adminPanel.add(btnAddCard);
	}

	// 음료패널 눌렀을 때, 선택 옵션 창
	class newBeverageWindow extends JFrame {
		newBeverageWindow() {
			setTitle("Starbuck Coffee");
			setSize(600, 450);
			setResizable(false);
			JPanel NewWindowContainer = new JPanel();
			NewWindowContainer.setLayout(null);
			setContentPane(NewWindowContainer);
			JLabel label = new JLabel();
			label.setText("음료 사이즈와 종류를 선택해주세요.");
			label.setBounds(30, 70, 300, 100);
			NewWindowContainer.add(label);

			// 사이즈 라디오 버튼
			JRadioButton short_ = new JRadioButton("Short");
			short_.setBounds(50, 150, 70, 100);
			short_.setSelected(true);
			JRadioButton tall = new JRadioButton("Tall (+500원)");
			tall.setBounds(130, 150, 120, 100);
			JRadioButton grande = new JRadioButton("Grande (+1000원)");
			grande.setBounds(250, 150, 150, 100);
			JRadioButton venti = new JRadioButton("Venti (+1500원)");
			venti.setBounds(400, 150, 150, 100);
			ButtonGroup sizeGroup = new ButtonGroup();
			sizeGroup.add(short_);
			sizeGroup.add(tall);
			sizeGroup.add(grande);
			sizeGroup.add(venti);

			// 아이스,핫 라디오 버튼
			JRadioButton ice = new JRadioButton("ICE 음료");
			ice.setBounds(50, 250, 150, 100);
			ice.setSelected(true);
			JRadioButton hot = new JRadioButton("HOT 음료");
			hot.setBounds(200, 250, 150, 100);
			ButtonGroup iceHotGroup = new ButtonGroup();
			iceHotGroup.add(ice);
			iceHotGroup.add(hot);
			
			beverageCancle = new JButton("취소");
			beverageCancle.setBounds(460, 300, 100, 50);
			beverageCancle.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});

			// 확인 버튼 누르면 입력한 사이즈와 음료 종류 저장
			JButton okBtn = new JButton("확인");
			okBtn.setBounds(350, 300, 100, 50);
			okBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (short_.isSelected()) {
						short_.setText("S");
						size = short_.getText();
					} else if (tall.isSelected()) {
						tall.setText("T");
						beveragePrice += 500;
						size = tall.getText();
					} else if (grande.isSelected()) {
						grande.setText("G");
						beveragePrice += 1000;
						size = grande.getText();
					}else if (venti.isSelected()) {
						venti.setText("V");
						beveragePrice += 1500;
						size = venti.getText();
					}

					if (ice.isSelected()) {
						ice.setText("Ice");
						iceHot = ice.getText();
					} else if (hot.isSelected()) {
						hot.setText("Hot");
						iceHot = hot.getText();
					}
					beverageArr.add(new Beverage(beverageName, beveragePrice, size, iceHot));
					addCart = beverageName + "/" + beveragePrice + "(" + size + ", " + iceHot + ")";
					String[] inputStr = new String[2];
					inputStr[0] = "beverage/"+beverageIndex;
					inputStr[1] = beverageArr.get(beverageIndex).toString();
					beverageIndex++;
					model.addRow(inputStr);
					totalPrice += beveragePrice;
					endPrice.setText(Integer.toString(totalPrice) + "원");
					dispose();
				}
			});

			NewWindowContainer.add(okBtn);
			NewWindowContainer.add(short_);
			NewWindowContainer.add(tall);
			NewWindowContainer.add(grande);
			NewWindowContainer.add(venti);
			NewWindowContainer.add(ice);
			NewWindowContainer.add(hot);
			NewWindowContainer.add(beverageCancle);
			
			setVisible(true);
		}
	}

	// 음식패널 눌렀을 때, 선택 옵션 창
	class newFoodWindow extends JFrame {
		newFoodWindow() {
			setTitle("Starbuck Coffee");
			setSize(600, 450);
			setResizable(false);
			JPanel NewWindowContainer = new JPanel();
			NewWindowContainer.setLayout(null);
			setContentPane(NewWindowContainer);
			JLabel label = new JLabel();
			label.setText("데워드릴까요?");
			label.setBounds(30, 70, 300, 100);
			NewWindowContainer.add(label);

			JRadioButton yesHeat = new JRadioButton("네");
			yesHeat.setBounds(50, 150, 150, 100);
			yesHeat.setSelected(true);
			JRadioButton noHeat = new JRadioButton("아니오");
			noHeat.setBounds(200, 150, 150, 100);

			ButtonGroup heatGroup = new ButtonGroup();
			heatGroup.add(yesHeat);
			heatGroup.add(noHeat);
			
			foodCancle = new JButton("취소");
			foodCancle.setBounds(460, 300, 100, 50);
			foodCancle.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});

			JButton okBtn = new JButton("확인");
			okBtn.setBounds(350, 300, 100, 50);
			okBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (yesHeat.isSelected()) {
						yesHeat.setText("데움");
						heat = yesHeat.getText();
					} else if (noHeat.isSelected()) {
						noHeat.setText("그냥");
						heat = noHeat.getText();
					}
					
					foodArr.add(new Food(foodName, foodPrice, heat));
					addCart = foodName + "/" + foodPrice + "(" + heat + ")";
					String[] inputStr = new String[2];
					inputStr[0] = "food/"+foodIndex;
					inputStr[1] = foodArr.get(foodIndex).toString();
					foodIndex++;
					model.addRow(inputStr);
//					cart.append(addCart+"\n");
//					cart.setCaretPosition(cart.getDocument().getLength());
					totalPrice += foodPrice;
					endPrice.setText(Integer.toString(totalPrice) + "원");
					dispose();
				}
			});
			NewWindowContainer.add(okBtn);
			NewWindowContainer.add(yesHeat);
			NewWindowContainer.add(noHeat);
			NewWindowContainer.add(foodCancle);

			setVisible(true);
		}
	}

	// 상품 패널 눌렀을 때, 선택 옵션 창
	class newProductWindow extends JFrame {
		newProductWindow() {
			setTitle("Starbuck Coffee");
			setSize(600, 450);
			setResizable(false);
			JPanel NewWindowContainer = new JPanel();
			NewWindowContainer.setLayout(null);
			setContentPane(NewWindowContainer);
			JLabel label = new JLabel();
			label.setText("선물포장하시나요?");
			label.setBounds(30, 70, 300, 100);
			NewWindowContainer.add(label);

			JRadioButton yesWrap = new JRadioButton("네");
			yesWrap.setBounds(50, 150, 150, 100);
			yesWrap.setSelected(true);
			JRadioButton noWrap = new JRadioButton("아니요");
			noWrap.setBounds(200, 150, 150, 100);
			ButtonGroup heatGroup = new ButtonGroup();
			heatGroup.add(yesWrap);
			heatGroup.add(noWrap);
			
			productCancle = new JButton("취소");
			productCancle.setBounds(460, 300, 100, 50);
			productCancle.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			

			JButton okBtn = new JButton("확인");
			okBtn.setBounds(350, 300, 100, 50);
			okBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (yesWrap.isSelected()) {
						yesWrap.setText("포장");
						wrap = yesWrap.getText();
					} else if (noWrap.isSelected()) {
						noWrap.setText("그냥");
						wrap = noWrap.getText();
					}
					

					productArr.add(new Product(productName, productPrice, wrap));
					addCart = productName + "/" + productPrice + "(" + wrap + ")";
					String[] inputStr = new String[2];
					inputStr[0] = "product/"+productIndex;
					inputStr[1] = productArr.get(productIndex).toString();
					productIndex++;
					model.addRow(inputStr);
//					cart.append(addCart+"\n");
//					cart.setCaretPosition(cart.getDocument().getLength());
					totalPrice += productPrice;
					endPrice.setText(Integer.toString(totalPrice) + "원");
					dispose();
				}
			});
			NewWindowContainer.add(okBtn);
			NewWindowContainer.add(yesWrap);
			NewWindowContainer.add(noWrap);
			NewWindowContainer.add(productCancle);

			setVisible(true);
		}
	}

	// 구매할 음료의 버튼을 누르면 사이즈선택 창이 뜨게하는 이벤트 리스너 (내부클래스로 처리)
	private class BeverageListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String[] temp = e.getActionCommand().split("/");
			beverageName = temp[0];
			beveragePrice = Integer.parseInt(temp[1]);
			new newBeverageWindow();
		}
	}

	// 구매할 음식의 버튼을 누르면 사이즈선택 창이 뜨게하는 이벤트 리스너 (내부클래스로 처리)
	private class FoodListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String[] temp = e.getActionCommand().split("/");
			foodName = temp[0];
			foodPrice = Integer.parseInt(temp[1]);
			new newFoodWindow();
		}
	}

	// 구매할 제품의 버튼을 누르면 사이즈선택 창이 뜨게하는 이벤트 리스너 (내부클래스로 처리)
	private class ProductListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String[] temp = e.getActionCommand().split("/");
			productName = temp[0];
			productPrice = Integer.parseInt(temp[1]);
			new newProductWindow();
		}
	}

	// 이벤트리스너 - 메뉴 버튼 클릭시 해당 메뉴 리스트 출력하는 이벤트 리스너
	private class MenuListListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String str = e.getActionCommand();
			if (str.equals("음료")) {
				cardLayout.show(listPanel, "bl");
			} else if (str.equals("음식")) {
				cardLayout.show(listPanel, "fl");
			} else if (str.equals("상품")) {
				cardLayout.show(listPanel, "pl");
			}
		}
	}

	// 결제 버튼 누르면 뜨는 창
	class newPayWindow extends JFrame {
		newPayWindow() {
			setTitle("결제하기");
			setSize(600, 500);
			setResizable(false);
			JPanel NewWindowContainer = new JPanel();
			NewWindowContainer.setLayout(null);
			setContentPane(NewWindowContainer);
			JLabel label = new JLabel("결제 수단을 선택해주세요.");
			label.setBounds(50, 100, 300, 100);

			JRadioButton card = new JRadioButton("카드");
			card.setBounds(50, 200, 100, 100);
			card.setSelected(true);
			JRadioButton cash = new JRadioButton("현금");
			cash.setBounds(300, 200, 100, 100);
			ButtonGroup payment = new ButtonGroup();
			payment.add(card);
			payment.add(cash);

			okPay = new JButton("결제");
			okPay.setBounds(350, 350, 100, 50);
			okPay.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (card.isSelected()) {
						newCardWindow();
					}
						else if(cash.isSelected()) {
						newCashWindow();
					}
				}
			});
			
			cancelPay = new JButton("취소");
			cancelPay.setBounds(450, 350, 100, 50);
			cancelPay.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});


			NewWindowContainer.add(label);
			NewWindowContainer.add(card);
			NewWindowContainer.add(cash);
			NewWindowContainer.add(okPay);
			NewWindowContainer.add(cancelPay);

			setVisible(true);
		}
		
		protected void newCashWindow() {
			setTitle("현금 결제하기");
			setSize(600, 500);
			setResizable(false);
			JPanel NewWindowContainer = new JPanel();
			NewWindowContainer.setLayout(null);
			setContentPane(NewWindowContainer);
			
			JLabel lable = new JLabel("투입 금액");
			lable.setBounds(30, 30, 250, 80);
			lable.setFont(new Font("돋움", Font.BOLD, 30));
			
			JTextField tfCash = new JTextField(10);
			tfCash.setBounds(30, 100, 300, 50);
			
			JLabel changeLabel = new JLabel("거스름돈:");
			changeLabel.setBounds(30, 300, 100, 80);
			changeLabel.setFont(new Font("돋움", Font.BOLD, 20));
			
			JLabel change = new JLabel("0원");
			change.setBounds(150, 300, 300, 80);
			change.setFont(new Font("돋움", Font.BOLD, 30));
			
			JLabel l = new JLabel("");
			l.setFont(new Font("돋움", Font.BOLD, 20));
			l.setBounds(30, 400, 300, 50);
			
			cashCheck = new JButton("결제");
			cashCheck.setBounds(350, 100, 80, 50);
			
			cashPayCancel = new JButton("취소");
			cashPayCancel.setBounds(450, 300, 100, 50);
			cashPayCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
			
			cashCheck.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int inputCash = Integer.parseInt(tfCash.getText());
					if(totalPrice < inputCash) {
						int changeCash = inputCash - totalPrice;
						change.setText(changeCash+"원 ");
						l.setText("결제 완료되었습니다.");
						cashPaySuccess = new JButton("결제 완료");
						cashPaySuccess.setBounds(350, 300, 100, 50);
						NewWindowContainer.add(cashPaySuccess);
						cashPaySuccess.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								saveOrder();
								dispose();
							}
						});
						setVisible(true);
					} else if(totalPrice > inputCash) {
						int addCash = totalPrice - inputCash;
						change.setFont(new Font("돋움", Font.BOLD, 20));
						change.setText(addCash+"원 부족합니다.");
					} else if(totalPrice == inputCash) {
						change.setFont(new Font("돋움", Font.BOLD, 20));
						change.setText("결제 완료되었습니다.");
						cashPaySuccess = new JButton("결제 완료");
						cashPaySuccess.setBounds(350, 300, 100, 50);
						NewWindowContainer.add(cashPaySuccess);
						cashPaySuccess.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								saveOrder();
								dispose();
							}
						});
						setVisible(true);
					}
				}
			});
			
			NewWindowContainer.add(lable);
			NewWindowContainer.add(tfCash);
			NewWindowContainer.add(cashCheck);
			NewWindowContainer.add(changeLabel);
			NewWindowContainer.add(change);
			NewWindowContainer.add(l);
			NewWindowContainer.add(cashPayCancel);
			
			
			
			setVisible(true);
		}
		
		protected void newCardWindow() {
			setTitle("카드 결제하기");
			setSize(600, 500);
			setResizable(false);
			JPanel NewWindowContainer = new JPanel();
			NewWindowContainer.setLayout(null);
			setContentPane(NewWindowContainer);
			
			JLabel label = new JLabel("카드 번호 입력");
			label.setBounds(30, 30, 300, 80);
			label.setFont(new Font("돋움", Font.BOLD, 30));
			JTextField tf = new JTextField(16);
			tf.setBounds(30, 110, 300, 50);
			
			cardPayCancel = new JButton("취소");
			cardPayCancel.setBounds(450, 350, 100, 50);
			cardPayCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
			
			cardNumCheck = new JButton("확인");
			cardNumCheck.setBounds(400, 110, 100, 50);
			JLabel changeLabel = new JLabel("잔액:");
			changeLabel.setBounds(30, 300, 80, 150);
			changeLabel.setFont(new Font("돋움", Font.BOLD, 30));
			JLabel change = new JLabel("0원");
			change.setBounds(200, 300, 300, 150);
			change.setFont(new Font("돋움", Font.BOLD, 30));
			cardNumCheck.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String inputCardNum = tf.getText();
					Map<String, Card> readCardMap = kc.fileReadCard();
					
					if(readCardMap.containsKey(inputCardNum)==true) {
						int cardBalance = readCardMap.get(inputCardNum).getCardBalance();
						int balance = readCardMap.get(inputCardNum).getCardBalance() - totalPrice;
						if(balance >= 0){
							change.setText(balance+"원");
							readCardMap.get(inputCardNum).setCardBalance(balance);
							System.out.println(readCardMap.get(inputCardNum));
							cardPaySuccess = new JButton("결제 완료");
							cardPaySuccess.setBounds(350, 350, 100, 50);
							NewWindowContainer.add(cardPaySuccess);
							
							setVisible(true);
							cardPaySuccess.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									saveOrder();
									dispose();
									kc.fileWriteCard(readCardMap);
								}
							});
						} else {
							int lackCash = totalPrice - cardBalance;
							change.setFont(new Font("돋움", Font.BOLD, 25));
							change.setText(lackCash+"원 부족");
						}
					} else {
						change.setFont(new Font("돋움", Font.BOLD, 25));
						change.setText("카드번호 조회 불가");
					}
				}
			});
			NewWindowContainer.add(label);
			NewWindowContainer.add(tf);
			NewWindowContainer.add(cardNumCheck);
			NewWindowContainer.add(changeLabel);
			NewWindowContainer.add(change);
            NewWindowContainer.add(cardPayCancel);

			
			setVisible(true);
		}
	}
	
	// 카드추가 버튼 누르면 뜨는 창
	class newCardWindow extends JFrame {
		newCardWindow() {
			setTitle("카드추가하기");
			setSize(600, 500);
			setResizable(false);
			JPanel NewWindowContainer = new JPanel();
			NewWindowContainer.setLayout(null);
			setContentPane(NewWindowContainer);
			JLabel label = new JLabel("신규 카드 추가 / 카드 잔액 충전");
			label.setBounds(50, 100, 300, 100);
			JLabel cardNoLabel = new JLabel("카드 번호 입력");
			cardNoLabel.setBounds(50, 200, 150, 100);
			JTextField inputCardNo = new JTextField();
			inputCardNo.setBounds(50, 300, 150, 30);
				
			JLabel cardBalanceLabel = new JLabel("금액 입력");
			cardBalanceLabel.setBounds(250, 200, 100, 100);
				
			JTextField inputCardBalance = new JTextField();
			inputCardBalance.setBounds(250, 300, 100, 30);
			
			JLabel error = new JLabel();
			error.setBounds(100, 350, 300, 50);
				
			addCard = new JButton("추가/충전");
			addCard.setBounds(200, 350, 100, 50);
			addCard.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(inputCardNo.getText().equals("")) {
						error.setText("카드번호를 입력하세요.");
						error.setFont(new Font("돋움", Font.BOLD, 20));
					} else if(inputCardBalance.getText().equals("")) {
						error.setText("금액을 입력하세요.");
						error.setFont(new Font("돋움", Font.BOLD, 20));
					} else {
						card = new Card();
						String cardNo = inputCardNo.getText();
						card.setCardNo(cardNo);
						int cardBalance = Integer.parseInt(inputCardBalance.getText());
						card.setCardBalance(cardBalance);
						cardMap.put(cardNo,card);
						kc.fileWriteCard(cardMap);
						System.out.println(kc.fileReadCard());
						currentPanel = mainPanel;
						changePanel(startPanel);
						dispose();
					}
				}
			});
			addCardCancel = new JButton("취소");
			addCardCancel.setBounds(350, 350, 100, 50);
			addCardCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});

			NewWindowContainer.add(label);
			NewWindowContainer.add(cardNoLabel);
			NewWindowContainer.add(inputCardNo);
			NewWindowContainer.add(cardBalanceLabel);
			NewWindowContainer.add(inputCardBalance);
			NewWindowContainer.add(error);
			NewWindowContainer.add(addCard);
			NewWindowContainer.add(addCardCancel);

				setVisible(true);
		}
	}
	
	// 관리자 버튼 누르면 뜨는 비밀번호 입력 창
	class newAdminWindow extends JFrame {
		newAdminWindow() {
			setTitle("관리자 확인");
			setSize(400, 400);
			setResizable(false);
			JPanel NewWindowContainer = new JPanel();
			NewWindowContainer.setLayout(null);
			setContentPane(NewWindowContainer);
			JLabel l = new JLabel("비밀번호 입력");
			l.setBounds(30, 75, 150, 30);
			
			
			JPasswordField pf = new JPasswordField();
			pf.setBounds(30, 100, 150, 30);
			pf.setEchoChar('*');
			
			JButton okPassword = new JButton("입력");
			okPassword.setBounds(30, 200, 100, 40);
			
			JLabel error = new JLabel("");
			error.setBounds(50, 300, 150, 40);
			
			
			NewWindowContainer.add(error);
			NewWindowContainer.add(l);
			NewWindowContainer.add(pf);
			NewWindowContainer.add(okPassword);
			
			okPassword.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					kc.fileWrite(orderList);
					orderList.clear();
					String inputPass = new String(pf.getPassword());
					boolean bool = inputPass.equals(adminPassword);
					if(bool == true) {
						currentPanel = startPanel;
						changePanel(adminPanel);
						System.out.println("==============");
						ArrayList<Order> readOrderList = kc.fileRead();
		
						int beverageCount = 0;
						int beverageTotalSales = 0;
						int foodCount = 0;
						int foodTotalSales = 0;
						int productCount = 0;
						int productTotalSales = 0;
						
						for(int orderListIndex=0;orderListIndex<readOrderList.size();orderListIndex++) {
							Order order = readOrderList.get(orderListIndex);
							System.out.println(order);
							
							//음료 총 판매 수량  및 총 매출액
							for(int beverageListIndex=0;beverageListIndex<order.getBeverageList().size();beverageListIndex++) {
								beverageCount++;
								beverageTotalSales += order.getBeverageList().get(beverageListIndex).getPrice();
							}
							//음식 총 판매 수량  및 총 매출액
							for(int foodListIndex=0;foodListIndex<order.getFoodList().size();foodListIndex++) {
								foodCount++;
								foodTotalSales += order.getFoodList().get(foodListIndex).getPrice();
							}
							//상품 총 판매 수량  및 총 매출액
							for(int productListIndex=0;productListIndex<order.getProductList().size();productListIndex++) {
								productCount++;
								productTotalSales += order.getProductList().get(productListIndex).getPrice();
							}
						}
						
						System.out.println(productTotalSales);
						beverageSales.setText(beverageTotalSales+"원");
						Kiosk.this.beverageCount.setText(beverageCount+"개");
						foodSales.setText(foodTotalSales+"원");
						Kiosk.this.foodCount.setText(foodCount+"개");
						productSales.setText(productTotalSales+"원");
						Kiosk.this.productCount.setText(productCount+"개");
						totalSales.setText(beverageTotalSales+foodTotalSales+productTotalSales+"원");
						totalCount.setText(beverageCount+foodCount+productCount+"개");
						System.out.println("==============");
						dispose();
					}
					else {
						error.setText("비밀번호 오류");
						error.setFont(new Font("돋움", Font.BOLD, 20));
					}
				}
			});
			setVisible(true);
		}
	}
		

	// 패널 교체하기
	public void changePanel(JPanel next) {
		remove(currentPanel);
		add(next);
		repaint();
		currentPanel = next;
	}

	//초기화하기
	private void initKiosk() {
		addCart = "";
		totalPrice = 0;
		model = (DefaultTableModel)cartTable.getModel();
		model.setNumRows(0);
		endPrice.setText("");
		cardLayout.show(listPanel, "none");
		beverageArr = new ArrayList<>();
		foodArr = new ArrayList<>();
		productArr = new ArrayList<>();
		beverageIndex=0;
		foodIndex=0;
		productIndex=0;
	}
	
	//주문리스트에 주문 추가하기
	private void saveOrder() {
		order = new Order();
		order.setBeverageList(beverageArr);
		order.setFoodList(foodArr);
		order.setProductList(productArr);
		orderList.add(order);
		System.out.println(orderList);
		currentPanel = mainPanel;
		changePanel(startPanel);
	}
	
}
