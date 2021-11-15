package ggc.core;

import java.io.Serializable;

/** 
 * Class that represents one Batch of the Product
 * @author Pedro Matias and Inês Simões
 */
class Batch implements Serializable, Comparable<Batch> {
    /**
	* Unit price
	*/
    private double _price;
    /**
	* Number of units in the batch
	*/
    private int _quantity;
    /**
	* type of product in the batch
	*/
    private Product _product;
    /**
	* Partner associated with the batch
	*/
    private Partner _partner;
    
    /** 
     * Constructor: Creates a batch 
     *
     * @param price The batch unit price
     * @param initialQuantity The number of units in the batch
     * @param product The type of product contained in this batch
     * @param partner The partner associated with the batch
     */
    Batch(double price, int initialQuantity, 
                 Product product, Partner partner) {
        _price = price;
        _quantity = initialQuantity;
        _product = product;
        _partner = partner;
    }
    
    /**
	 * Creates a String representing the Batch
	 * @return  String with product identifier + partner identifier + unit price + quantity
	 * @see #getId() Product
	 * @see #getId() Partner
	 */
    @Override
    public String toString() {
        return _product.getId() + "|" + _partner.getId() + "|" + (int) Math.round(_price) + "|" + _quantity;
    }

    /** 
    * Gets the number of units in the batch.
    * @return A integer representing quantity of products in the Batch
    */
    int getQuantity() {
        return _quantity;
    }

    /** 
    * Gets the price of the product in that Batch
    * @return A double representing the unit price
    */
    double getPrice() {
        return _price;
    }
    
    /** 
    * Gets the type of product contained in the batch
    * @return A String representing the product identifier
    * @see #getId() Product
    */
    String getProductId() {
    	return _product.getId();
    }
    
    /** 
    * Gets the identifier of the partner associated with the batch
    * @return A String representing the partner identifier
    * @see #getId() Partner
    */
    String getPartnerId() {
    	return _partner.getId();
    }
    
    // confirmar com o professor que pode ser assim
    @Override
    public int compareTo(Batch other) {   	
    	if (this.getPrice() == other.getPrice())
    		return 0;
    	if (this.getPrice() > other.getPrice())
    		return 1;
    	return -1;
    }
    
    // possivelmente aqui adicionar 1 exceção caso tentemos
    // remover 1 numero negativo de coisas 
    // ou caso tentemos remover mais unidades do que aquelas disponiveis
    void removeUnits(int quantity) {
    	_quantity -= quantity;
    }
    
    Product getProduct() {
    	return _product;
    }
}
