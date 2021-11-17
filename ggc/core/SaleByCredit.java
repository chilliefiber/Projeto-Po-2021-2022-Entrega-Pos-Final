package ggc.core;

class SaleByCredit extends Sale {
	
	private Date _deadline;
	
	SaleByCredit(int quantity, Product product, Partner p, int id, Date deadline) {
		super(null, quantity, product, p, id);
		_deadline = deadline;
	}
	
	// parecem estranhos a maneira como foram overriden, 
	// mas tem a ver com o Mostrar Parceiro não incluir desagregações
	// nas valor das vendas efetuadas e pagas
	@Override
	double getSalesValue() {
		return this.getBaseValue();
	}
	
	@Override
	double getReceivedValue() {
		return this.getAmountPaid();
	}
	
	@Override
	Period getPeriod(Date currentDate) {
    	int n = this.getProduct().getN();
    	int dif = _deadline.difference(currentDate);
    	if (dif >= n)
            return Period.P1;
        else if (dif >=0 && dif < n )
            return Period.P2;
        else if (dif*(-1) > 0 && dif*(-1) <=n )
            return Period.P3; 
        return Period.P4;
        
    }
	
	@Override
	int getNumberOfDaysOverdue(Date currentDate) {
		if (!this.hasBeenPaid())
			return currentDate.difference(this.getDeadline());
		return this.getPaymentDate().difference(this.getDeadline());
	}
	
	@Override
	double getExtantDebt(Date currentDate) {
		if (this.hasBeenPaid())
			return 0;
		return this.getBaseValue() + this.getPartner().calculateDiscountOrPenalty(this, currentDate);
	}
	
	@Override
	boolean hasBeenPaid() {
		return this.getAmountPaid() != 0;
	}

	private double getPaymentValue() {
		if (this.hasBeenPaid())
			return  this.getReceivedValue();
		return this.getExtantDebt(_deadline);
	}
	
	private String getLimitDateString() {
		if (this.hasBeenPaid())
			return "|" + this.getPaymentDate();
		return "";
	}
	
	@Override
    public String toString() {
        return "VENDA" + super.toString() + "|" + (int) Math.round(this.getPaymentValue()) + "|" + _deadline + this.getLimitDateString();
    }
	
	Date getDeadline() {
		return _deadline;
	}

	@Override
	double receivePayment(Date currentDate) {
		if (! this.hasBeenPaid()) {
			this.setAmountPaid(this.getExtantDebt(currentDate));
			this.setPaymentDate(currentDate.makeCopy());
			this.getPartner().paySale(this);	
			return this.getAmountPaid();
		}
		// se já tinha sido pago devolvemos 0 para depois não alterar o saldo disponivel
		return 0;
	}
}
