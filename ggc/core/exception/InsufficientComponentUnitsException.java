package ggc.core.exception;

public class InsufficientComponentUnitsException extends Exception {
	private int _availableUnits;
	private int _neededUnits;
	private String _productId;
	
	public InsufficientComponentUnitsException(int availableUnits, int neededUnits, String productId) {
		_availableUnits = availableUnits;
		_neededUnits = neededUnits;
		_productId = productId;
	}
	
	public int getAvailableUnits() {
		return _availableUnits;
	}
	
	public int getNeededUnits() {
		return _neededUnits;
	}
	
	public String getProductId() {
		return _productId;
	}
}
