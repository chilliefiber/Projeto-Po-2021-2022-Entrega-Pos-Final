package ggc.core.exception;

public class InsufficientUnitsException extends Exception {
	private int _availableUnits;
	
	public InsufficientUnitsException(int availableUnits) {
		_availableUnits = availableUnits;
	}
	
	public int getAvailableUnits() {
		return _availableUnits;
	}
}
