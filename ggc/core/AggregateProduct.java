package ggc.core;

import java.util.ArrayList;
import java.util.List;

import ggc.core.exception.InsufficientComponentUnitsException;
import ggc.core.exception.InsufficientUnitsException;

class AggregateProduct extends Product{

    Recipe _recipe;

    AggregateProduct(String id){
        super(id,3);
    }

    AggregateProduct(String id, Recipe recipe) {
        super(id, 3);
        _recipe = recipe;
    }
    
    public AggregateProduct(String productId, double alpha, List<Product> componentProducts, List<Integer> componentAmounts) {
    	super(productId, 3);
    	_recipe = new Recipe(alpha);
    	for (int i = 0; i < componentProducts.size(); i++)
    		_recipe.addComponent(new Component(componentProducts.get(i), componentAmounts.get(i)));
    	
    	
	}

	@Override
    public String toString() {
    	List<Component> components = _recipe.getComponents();
    	String s = super.toString()  + "|" + components.get(0);
    	
    	for (int i = 1; i < _recipe.getComponents().size(); i++)
    		s += "#" + components.get(i);
    	return s;
    }

    @Override
    void addComponentRecipe(Product product, int amount) {
        _recipe.addComponent(new Component(product, amount));
    }

    void createRecipe( double alpha){
        _recipe = new Recipe(alpha);
    }
    
    // vai criar os batches novos
	@Override
	List<Batch> getNewBatchesFromBreakdown(int quantity, Partner partner) throws InsufficientUnitsException {
		if (this.getCurrentStock() < quantity)
			throw new InsufficientUnitsException(this.getCurrentStock());
		List<Batch> createdBatches = new ArrayList<Batch>();
		Batch newBatch = null;
		// iterar pela receita, criando os lotes um a um e adicionando à lista de lotes criados
		for (Component component : _recipe.getComponents()) {
			newBatch = new Batch(component.getProduct().getPriceOfBatchCreatedByBreakdown(), quantity * component.getQuantity(), component.getProduct(), partner);
			createdBatches.add(newBatch);
		}
		return createdBatches;
	}
	
	// aqui se não houver a quantidade suficiente vai tentar fazer a agregação
	@Override
	void checkQuantity(int quantity) throws InsufficientComponentUnitsException {
		if (this.getCurrentStock() >= quantity)
			return; // há existências suficientes sem precisar de agregar

		// neededQuantity é a quantidade de produto agregado a criar
		int neededQuantity = quantity - this.getCurrentStock();
		
		for (Component component : _recipe.getComponents()) 
			component.getProduct().checkQuantity(component.getQuantity() * neededQuantity);
	}
	
	@Override 
	double calculateBaseValue(int quantity) {
		
		int quantityToAggregate = quantity - this.getCurrentStock();
		if (quantityToAggregate <= 0)
			return super.calculateBaseValue(quantity);
		// neste caso temos de realizar a agregação
		double value = super.calculateBaseValue(this.getCurrentStock());
		double highestPrice = 0;
		double priceOfUnitWithoutAggravation = 0;

		
		
		// criamos as unidades uma a uma
		// porque ao criar a última unidade (que será sempre tão ou mais cara como todas as outras)
		// temos de ver se o preço dela não é superior ao preço máximo
		// histórico deste produto. Nesse caso
		// temos de atualizar o preço máximo histórico deste produto que estamos a agregar
		
		// este processo pode parecer confuso. Aqui não criamos lotes mesmo reais porque esta operação
		// é só quando estamos a vender portanto é desnecessário cria-los só para os destruir logo a seguir,
		// mas se criássemos os lotes poderiam ter valores diferentes. Isto porque podemos utilizar
		// lotes diferentes, com preços também diferentes, do mesmo componente para agregar todas
		// as unidades que necessitamos
		
		for (int i = 0; i < quantityToAggregate; i++) {
			priceOfUnitWithoutAggravation = 0;
			for (Component component : _recipe.getComponents())
				priceOfUnitWithoutAggravation += component.getProduct().calculateBaseValue(component.getQuantity());
			highestPrice = (1 + _recipe.getAlfa()) * priceOfUnitWithoutAggravation;
			value += highestPrice;
		}
		
		// verificação do preço mais alto
		super.checkMaxPrice(highestPrice);
		
		return value;
		
		
	}
	
	
}
