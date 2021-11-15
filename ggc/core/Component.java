package ggc.core;

import java.io.Serializable;

class Component implements Serializable {

    private int _quantity;
    private Product _product;

    Component(Product product, int quantity) {
        _product = product;
        _quantity = quantity;
    }
    
    int getQuantity() {
        return _quantity;
    }

    Product getProduct() {
        return _product;
    } 
    
    @Override
    public String toString() {
    	return _product.getId() + ":" + _quantity;
    }
}
