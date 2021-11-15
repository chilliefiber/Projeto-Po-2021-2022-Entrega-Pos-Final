package ggc.core;

import java.util.ArrayList;
import java.util.List;

class BreakdownSale extends Sale {
	
	// guarda, pela ordem da receita, o preço total dos lotes criados dos respetivos componentes 
	private List<Double> _prices = new ArrayList<>();
	// guarda, pela ordem da receita, o id dos produtos da receita
	private List<String> _ids = new ArrayList<>();
	// guarda, pela ordem da receita, a quantidade de produto criado  
	private List<Integer> _quantities = new ArrayList<>();
	
	BreakdownSale(Date paymentDate, int quantity, Product product, Partner p, int id, List<Batch> createdBatches) {
		super(paymentDate, quantity, product, p, id);
		setBaseValueAndAmountPaid(createdBatches);
		for (Batch createdBatch : createdBatches) {
			_prices.add(createdBatch.getPrice() * createdBatch.getQuantity());
			_ids.add(createdBatch.getProductId());
			_quantities.add(createdBatch.getQuantity());
		}
	}
	
	final void setBaseValueAndAmountPaid(List<Batch> createdBatches) {
		// no construtor de Sale já colocamos o baseValue igual ao valor da quantidade de produto não desagregado (valor da venda) ver enunciado 1.3.3 Temos de alterar aqui
		
		// o costOfAcquisition corresponde ao valor da compra, ou seja da quantidade de produtos desagregados
		double costOfAcquisition = 0;
		for (Batch batch : createdBatches) 
			costOfAcquisition += batch.getPrice() * batch.getQuantity();
		// o valor base é a diferença entre o valor do produto agregado que tínhamos antes de desagregar
		// e o valor dos produtos desagregados que criámos na desagregação
		this.setBaseValue(this.getBaseValue() - costOfAcquisition);
		// se o valor base é negativo, o valor pago é nulo (porque ficámos em nossa posse com produtos que valem mais do que antes da agregação)
		// se o valor base é positivo, esse foi o valor pago
		this.setAmountPaid(this.getBaseValue() < 0 ? 0 : this.getBaseValue());
	}

	@Override
	Period getPeriod(Date currentDate) {
		return Period.P1;
	}
	
	@Override
	public String toString() {
		String s = "DESAGREGAÇÃO" + super.toString() + "|" + (int) Math.round(this.getAmountPaid()) + "|" + this.getPaymentDate() + "|";
		for (int i = 0; i < _prices.size(); i++) {
			if (i != 0)
				s += "#";
			s += _ids.get(i) + ":" + _quantities.get(i) + ":" + (int) Math.round(_prices.get(i));
		} 
		return s;
	}
}
	
