package ggc.core;

import java.io.Serializable;
 
 /* Class of Products
 * @author Pedro Matias and Inês Simões
 */

abstract class Transaction implements Serializable {
    
    /* Transaction identifier
    */
    private int _id;
    
    /* data que em que a transação foi paga
     * possivelmente utilizar um null object para as SaleByCredit ao inicio,
     * ou entao colocar a null na criação
    */
    private Date _paymentDate;
    
    /* Valor base
    */
    private double _baseValue;
    /**
    * quantity of product involved
    */
    private int _quantity;
    
    private Product _product;

    private Partner _partner; 
    
    Transaction(Date paymentDate, int quantity, Product product, Partner partner, int id ) {
        _paymentDate = paymentDate;
    	_quantity = quantity;
    	_product = product;
        _partner = partner;
        _id = id;
    }

    @Override
    public String toString() {
    	return "|" + _id + "|" + _partner.getId() + "|" + _product.getId() + "|" + _quantity + "|" + (int) Math.round(_baseValue) ; 
    }

    // código utilizado no construtor de Sale, será que fica aqui bem?
    final void setBaseValue(double baseValue) {
    	_baseValue = baseValue;
    }    
    
    Date getPaymentDate() {
    	return _paymentDate;
    }
    
    Product getProduct() {
    	return _product;
    }
    
    // utilizado no construtor de BreakdownSale
    final double getBaseValue() {
    	return _baseValue;
    }
    
    Partner getPartner() {
    	return _partner;
    }
    
    int getId() {
    	return _id;
    }
    
    // este método de 1 transação calcula o quanto teríamos de pagar (valor negativo)
    // ou receber (valor positivo) caso o pagamento desta transação fosse totalmente efetuado
    // na data currentDate. 
    // Para o enunciado do projeto apenas será overriden pela classe SaleByCredit
    // foi colocada aqui por causa do princípio open closed: caso um dia quiséssemos
    // adicionar a possibilidade de fazer compras a crédito, apenas tínhamos de 
    // implementar este método diferentemente nessa nova classe e o cálculo do saldo
    // contabilistico da maneira que está feito no warehouse (iterando por todas as
    // transações e chamando este método) não teria de ser alterado
    double getExtantDebt(Date currentDate) {
    	return 0;
    }
    
    double receivePayment(Date currentDate) {
    	return 0;
    }
    
    void setPaymentDate(Date paymentDate) {
    	_paymentDate = paymentDate;
    }
}
