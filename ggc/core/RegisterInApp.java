package ggc.core;

import java.io.Serializable;

public class RegisterInApp implements DeliveryMode, Serializable {

	@Override
	public void deliverNotification(Notification notification, Partner partner) {
		partner.addNotification(notification);
	// será que tenho de fazer isto com 1 visitor???????
	// seria tipo ProductObserver era 1 elemento como descrito no slide do visitor
	// (ou seja, ProductObserver extends Element
	}

}
