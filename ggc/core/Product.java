package ggc.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import ggc.core.exception.InsufficientComponentUnitsException;
import ggc.core.exception.InsufficientUnitsException;

/** 
 * Class of Products
 * @author Pedro Matias and Inês Simões
 */

abstract class Product implements Serializable {
    /**
	* Highest price associated with this product
	*/
    private double _maxPrice;
    /**
	* Product identifier
	*/
    private String _id;
    /**
	* List of available batches of this product
	*/
    
    private PriorityQueue<Batch> _batches = new PriorityQueue<Batch>();
    
    // tenho que definir o hashCode e o equals para ProductObserver????
    private List<ProductObserver> _observers = new ArrayList<ProductObserver>();

    // serve para não criar 1 notificação nova quando é o primeiro lote
    private boolean _firstBatch = true;
    
    private int _n;
   
	/**
	 * Constructor: Input receive the product identifier
	 *  @param id the product identifier
	 */
    Product(String id, int n) {
        _id = id;
        _n = n;
    }
    /**
	 * Overloaded constructor: Input receive the product identifier and the price <p>
     * to register a product for the first time.
	 *
	 * @param id the product identifier
     * @param firstPrice the price of a unit of the first batch
	 * @see #Product() Product
	 */
    Product(String id, int n, double firstPrice) {
        this(id, n);
        // provavelmente vamos receber 1 preço da primeira vez que 
        // adquirimos 1 produto. Assim o construtor recebe esse valor e 
        // começa o maxPrice com isso
        _maxPrice = firstPrice; 
    }

    boolean checkQuantity(int quantity) {
    	return this.getCurrentStock() >= quantity;
    }
    
    /**
	 * Creates a String representing the Product
	 * @return  product identifier + highest price + quantity
	 */
    @Override
    public String toString(){
        return _id + "|" + (int) Math.round(_maxPrice)  + "|" + this.getCurrentStock();
    }

   	/**
	 * Replaces the highest price ( _maxPrice) if necessary
	 *
	 * @param price price to check
	 */
    void checkMaxPrice(double price){
        if (price > _maxPrice)
            _maxPrice = price;
    }

    /**
	 * Get the highest price associated with this product
     * @return highest price
	 */
    double getMaxPrice(){
        return _maxPrice;
    }

    /**
	 * Get the product identifier
     * @return product identifier
	 */   
    String getId() {
        return _id;
    }
    
    /**
	 * Add a new batch to the list of batches of this product
	 *
	 * @param batch batch to add
     * @see #checkMaxPrice() Product
	 */    
    void addBatch(Batch batch) {
    	if (!_firstBatch) {
    		// se não havia lotes deste produto
    		if (_batches.size() == 0)
    			notifyObservers("NEW", batch.getPrice());  
    		// se este lote for mais barato do que o lote mais barato existente
    		else if (_batches.peek().getPrice() > batch.getPrice())
    			notifyObservers("BARGAIN", batch.getPrice());
    	}
    	_firstBatch = false;
    	_batches.add(batch);    
        checkMaxPrice(batch.getPrice());        
    }

	/**
	 * Cycles through all batches to see how many units of the product exist
	 *
	 * @return total number of product units
	 */
    int getCurrentStock() {
        int currentStock = 0;
        for (Batch b: _batches)
            currentStock += b.getQuantity();
        return currentStock;
    }
    
	/**
	 * Gets a sorted list of all batches of this product
	 *
	 * @return sorted list of batches 
     * @see #CompareBatches() CompareBatches
	 */
    List<Batch> getAllBatches() {
    	List<Batch> res = new ArrayList<Batch>(_batches); 
    	Collections.sort(res, new CompareBatches());
        return res;
    }
    
    // calcular o valor base de 1 venda com quantity
    // unidades deste produto. Também atualiza o(s) lote(s)
    // de onde foram removidas unidades
    
    // talvez criar 1 exceção aqui no core
    // para quando chamam esta função e não temos quantidade suficiente
    // notar que isso nunca deve acontecer, porque temos a função checkQuantity
    // que deve ser chamada pelO warehousemanager antes de chegar aqui 
    // notar que este metodo tambem altera os lotes existentes
    
    double calculateBaseValue(int quantity) {
    	double cost = 0;
    	// não precisamos de verificar se ainda há lotes disponíveis nesta loop
    	// pelo motivo descrito no comentário acima da função
    	while (quantity > 0) {
    		Batch b = _batches.peek();
    		// neste caso o lote com o preço mais baixo
    		// não tem unidades suficientes
    		if (b.getQuantity() < quantity) {
    			quantity -= b.getQuantity();
    			cost += b.getPrice() * b.getQuantity();
    			_batches.poll(); // retirar esse lote da lista porque foi esgotado
    		}
    		// neste caso o lote tem unidades suficientes
    		else {
    			cost += b.getPrice() * quantity;
    			b.removeUnits(quantity);
    			quantity = 0;
    			if (b.getQuantity() == 0)
    				_batches.poll();
    		}	
    	}
    	return cost;
    }
    
    int getN() {
    	return _n;
    }
    
    private void notifyObservers(String description, double price) {
    	Notification notification = new Notification(description, price, this.getId());
    	for (ProductObserver o : _observers)
    		o.update(notification);
    }
    
    void toggleSubscriber(ProductObserver o) {
    	if (_observers.contains(o))
    		_observers.remove(o);
    	else
    		_observers.add(o);
    }
    
    List<Batch> getNewBatchesFromBreakdown(int quantity, Partner partner) throws InsufficientUnitsException{
    	return null;
    }
    
    double getPriceOfBatchCreatedByBreakdown() {
    	return _batches.size() == 0 ? this.getMaxPrice() : _batches.peek().getPrice();
    }
    
    void checkComponents(int quantity) throws InsufficientComponentUnitsException {}
 
    void addComponentRecipe(Product product, int amount) {}
}
