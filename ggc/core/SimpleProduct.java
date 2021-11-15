package ggc.core;

class SimpleProduct extends Product {

    SimpleProduct(String id, double firstPrice ) {
    	// não sei se esta é a melhor maneira
    	// colocando aqui esta constante mágica
        super(id, 3, firstPrice);
    }

}
