package ggc.core;

public class Acquisition extends Transaction {
	
    public Acquisition(Date paymentDate, int quantity, Product product, Partner partner, int id, double baseValue) {
    	super(paymentDate, quantity, product, partner, id);
    	setBaseValue(baseValue);
    }
    
    @Override
    public String toString() {
    	return "COMPRA" + super.toString() + "|" + this.getPaymentDate();
    }
    
    
}
