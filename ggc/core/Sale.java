package ggc.core;

public abstract class Sale extends Transaction {

	private double _amountPaid;
	
	Sale(Date paymentDate, int quantity, Product product, Partner p, int id) {
		super(paymentDate, quantity, product, p, id);
		// aqui estamos a remover também as existências 
		// das unidades que vão ser vendidas/desagregadas
		setBaseValue(product.calculateBaseValue(quantity));
	}
	
	// estes 2 métodos são para o Mostrar parceiro
	// são 0 para o BreakdownSale e overriden para
	// o SaleByCredit
	double getSalesValue() {
		return 0;
	}
	
	double getReceivedValue() {
		return 0;
	}
	
	// utilizado no construtor de BreakdownSale tem de ser final
	final void setAmountPaid(double amount) {
		_amountPaid = amount;
	}
	
	double getAmountPaid() {
		return _amountPaid;
	}
	
	abstract Period getPeriod(Date currentDate);

	int getNumberOfDaysOverdue(Date currentDate) {
		return 0;
	}
	
	boolean hasBeenPaid() {
		return true;
	}
}
