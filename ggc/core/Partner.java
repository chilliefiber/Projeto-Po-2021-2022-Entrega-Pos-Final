package ggc.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Partner implements Serializable, ProductObserver {
    private String _id;
    private String _name;
    private String _address;
    private double _points;
    private List<Batch> _batches;
    private PartnerStatus _status;
    private List<Acquisition> _acquisitions;
    private List<Sale> _sales;
    private DeliveryMode _deliveryMode;
    private List<Notification> _notifications;
    
    public Partner(String id, String name, String address) { 
        _id = id;
        _name = name;
        _address = address;
        _status = new Normal();
        _batches = new ArrayList<Batch>();
        _acquisitions = new ArrayList<Acquisition>();
        _sales = new ArrayList<Sale>();
        _notifications = new ArrayList<Notification>();
        _deliveryMode = new RegisterInApp();
    }

    public String getId() {
        return _id;
    }

    public String toString() {
    	String s =  _id +"|"+ _name + "|" + _address + "|" + _status.toString() + 
        		"|" + (int) Math.round(_points) + "|" + (int) Math.round(this.getPurchasesValue()) +
        		"|" + (int) Math.round(this.getSalesValue()) + "|" + (int) Math.round(this.getReceivedValue());
    	if (_notifications.size() != 0)
    		s += "\n";
    	for (Notification notification: _notifications)
    		s += notification + "\n";
    	_notifications.clear();
    	return s;
    }
    
    private double getPurchasesValue() {
    	double purchasesValues = 0;
    	for (Acquisition acquisition : _acquisitions)
    		purchasesValues += acquisition.getBaseValue();
    	return purchasesValues;
    }
    
    private double getSalesValue() {
    	double salesValue = 0;
    	for (Sale sale: _sales)
    		salesValue += sale.getSalesValue();
    	return salesValue;
    }
    
    private double getReceivedValue() {
    	double receivedValue = 0;
    	for (Sale sale : _sales)
    		receivedValue += sale.getReceivedValue();
    	return receivedValue;
    }
    
    void addBatch(Batch b) {
    	_batches.add(b);
    }
    
    void removeBatch(Batch b) {
    	_batches.remove(b);
    }
    
    // aqui remover os Batches que estão com quantidade nula
    // a maneira correta de fazer isto seria com 1 observer
    // mas assim simplifica
    Collection<Batch> getBatches() {
    	Iterator<Batch> iter = _batches.iterator();
    	Batch b = null;
    	while (iter.hasNext()) {
    		b = iter.next();
    		if (b.getQuantity() == 0)
    			iter.remove();
    	}
    	List<Batch> ordered = new ArrayList<Batch>(_batches);
    	Collections.sort(ordered, new CompareBatches());
        return ordered;
    }
    
    void addAcquisition(Acquisition a) {
    	_acquisitions.add(a);
    }

	Collection<Acquisition> getAcquisitions() {
		return new ArrayList<Acquisition>(_acquisitions);
	}

	void addSale(Sale sale) {
		_sales.add(sale);	
	}

	Collection<Sale> getSales() {
		return new ArrayList<Sale>(_sales);
	}

	double calculateDiscountOrPenalty(SaleByCredit sale, Date currentDate) {
		return _status.calculateDiscountOrPenalty(sale, currentDate);
	}
	
	void addPoints(double pointsToAdd) {
		_points += pointsToAdd;
	}
	
	double getPoints() {
		return _points;
	}
	
	void setStatus(PartnerStatus newStatus) {
		_status = newStatus;
	}
    
	void setDeliveryMode(DeliveryMode mode) {
		_deliveryMode = mode;
	}

	@Override
	public void update(Notification notification) {
		_deliveryMode.deliverNotification(notification, this);
	}
	
	void addNotification(Notification notification) {
		_notifications.add(notification);
	}
	
	void paySale(Sale sale) {
		_status.calculatePointsAndSwitchState(sale, this);
	}
}