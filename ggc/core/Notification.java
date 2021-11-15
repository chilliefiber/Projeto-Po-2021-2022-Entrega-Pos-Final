package ggc.core;

class Notification {
	private String _description;
	private double _price;
	private String _productId;
	
	Notification(String description, double price, String productId) {
		_description = description;
		_price = price;
		_productId = productId;
	}
	
	public String toString() {
		return _description + "|" + _productId + "|" + (int) Math.round(_price);
	}
	
	
	
}
