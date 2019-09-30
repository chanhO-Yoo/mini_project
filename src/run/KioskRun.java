package run;

import com.order.kiosk.controller.KioskController;

import view.Kiosk;

public class KioskRun {

	public static void main(String[] args) {
		new Kiosk(new KioskController());
	}

}
