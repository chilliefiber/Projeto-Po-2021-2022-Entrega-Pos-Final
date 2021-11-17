package ggc.core;

// FIXME import classes (cannot import from pt.tecnico or ggc.app)

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import ggc.core.exception.BadEntryException;
import ggc.core.exception.DuplicatePartnerException;
import ggc.core.exception.InsufficientComponentUnitsException;
import ggc.core.exception.InsufficientUnitsException;
import ggc.core.exception.NegativeDateException;
import ggc.core.exception.UnknownPartnerException;
import ggc.core.exception.UnknownProductException;
import ggc.core.exception.UnknownTransactionException;
/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

    /** Serial number for serialization. */
    private static final long serialVersionUID = 202109192006L;

    private Map<String, Product> _products = new HashMap<>();
    private Map<String, Partner> _partners = new HashMap<>();

    // transactions vai deixar de ser 1 list,
    // passa a ser 1 hashmap com o id como chave
    // depois criar 1 SortedSet para a coleção que enviamos para a 
    // camada de cima
    
    private Map<Integer, Transaction> _transactions = new HashMap<Integer, Transaction>();
    
    private Date _date = new Date();
    
    private int _transactionId;

    private double _availableBalance;

    public Warehouse() {
    }
    
    Transaction getTransaction(int id) throws UnknownTransactionException {
    	Transaction transaction = _transactions.get(id);
    	if (transaction == null)
    		throw new UnknownTransactionException();
        return transaction;
    }
    
    Collection<Product> getProducts() {
    	List<Product> ordered = new ArrayList<Product>(_products.values());
    	Collections.sort(ordered, new CompareProducts());
        return ordered;
    }
    
    int getDate() {
    	return _date.getDate();
    }

    double getAvailableBalance() {
        return _availableBalance;
    }

    double getAccountingBalance() {
    	// o saldo contabilistico é o saldo disponível mais o valor que teríamos se pagassem todas as vendas a crédito neste instante
    	// aqui acho que deviamos retirar o polimorfismo e usar instanceof
    	double debt = 0;
    	for (Transaction t: _transactions.values())
    		debt += t.getExtantDebt(_date);
        return _availableBalance + debt;
    }
    
    void addDays(int days) throws NegativeDateException {
    	_date.add(days);
    }
    
    Partner getPartner(String id) throws UnknownPartnerException {
    	Partner partner = _partners.get(id.toLowerCase());
    	if (partner == null)
    		throw new UnknownPartnerException();
    	return partner;
    }

    // previne fugas de privacidade
    Collection<Partner> getPartners() {
    	List<Partner> ordered = new ArrayList<Partner>(_partners.values());
    	Collections.sort(ordered, new ComparePartners());
        return ordered;
    }
    /**
     * @param txtfile filename to be loaded.
     * @throws IOException
     * @throws BadEntryException
     */
    void importFile(String txtfile) throws IOException, BadEntryException {
        Parser parser = new Parser(this);
        parser.parseFile(txtfile);
    }

    void addPartner(String id, String name, String address) throws DuplicatePartnerException {
    	if (_partners.containsKey(id.toLowerCase())) {
    		throw new DuplicatePartnerException();
    	}
    		
    	Partner p = new Partner(id, name, address);
    	// quando adicionamos 1 parceiro novo devemos
    	// colocá-lo como estando interessado
    	// em receber em notificações acerca de todos os produtos
    	for (Product product: _products.values())
    		product.toggleSubscriber(p);
        _partners.put(p.getId().toLowerCase(), p);
    }

    void addProduct(Product p) {
    	// quando adicionamos 1 produto novo, devemos colocar
    	// todos os parceiros como entidades interessadas
    	// em receber notificações acerca deste produto
    	for (Partner partner: _partners.values())
    		p.toggleSubscriber(partner);
    	
        _products.put(p.getId().toLowerCase(), p);
    }

    Product getProduct(String id) throws UnknownProductException {
    	Product product = _products.get(id.toLowerCase());
    	if (product == null)
    		throw new UnknownProductException();
        return product;
    }

    void registerAcquisition(int quantity, Product product, Partner partner, double pricePerUnit) {
    	Acquisition acquisition = new Acquisition(new Date(_date.getDate()), quantity, product, partner, _transactionId++, pricePerUnit * quantity);
    	Batch newBatch = new Batch(pricePerUnit, quantity, product, partner);
    	product.addBatch(newBatch);
    	partner.addBatch(newBatch);
        _transactions.put(acquisition.getId(), acquisition);
        partner.addAcquisition(acquisition);
        _availableBalance -= pricePerUnit*quantity;
    }
    
    // fuga de privacidade aqui!!!!!
    Collection<Acquisition> getPartnerAcquisitions(Partner partner) {
    	return partner.getAcquisitions();
    }

    Collection<Sale> getPartnerSales(Partner partner) {
    	return partner.getSales();
    }

    List<Batch> getAllBatches() {
        List<Batch> res = new ArrayList<>();
        for(Product p: this.getProducts())
            res.addAll(p.getAllBatches());
        Collections.sort(res, new CompareBatches());
        return res;
    }
    
    List<Batch> getBatchesUnderLimit(double limit) {
        List<Batch> res= new ArrayList<>();
        for(Batch b: this.getAllBatches())
          if (b.getPrice()<= limit)
            res.add(b);
        Collections.sort(res, new CompareBatches());
        return res;
      }
    Collection<Sale> getPartnerPaidSales(Partner partner) {
    	// não há fuga de privacidade
        List<Sale> res = new ArrayList<>();
        for(Sale sale: partner.getSales()) {
            if (sale.hasBeenPaid()){
                res.add(sale);
            }
        }
    	return res;
    }

    void registerSaleByCredit(Partner partner, int deadline, Product product, int quantity) throws InsufficientUnitsException, InsufficientComponentUnitsException {
    	// lança a exceção InsufficientUnitsException
    	product.checkComponents(quantity); 
  		if (!product.checkQuantity(quantity))
  			throw new InsufficientUnitsException(product.getCurrentStock());
    	Date deadlineDate = new Date(deadline);
    	SaleByCredit sale = new SaleByCredit(quantity, product, partner, _transactionId++, deadlineDate, _date);
    	_transactions.put(sale.getId(), sale);
    	partner.addSale(sale);
    }
    
    void receivePayment(Transaction transaction) {
    	// alterado para atualizar o saldo disponivel
    	_availableBalance += transaction.receivePayment(_date);
    	
   }
    
    void toggleNotifications(Partner partner, Product product) {
    	product.toggleSubscriber(partner);
    }

	void registerBreakdown(Partner partner, Product product, int quantity) throws InsufficientUnitsException {
		Date paymentDate = _date.makeCopy();
		List<Batch> createdBatches = product.getNewBatchesFromBreakdown(quantity, partner);
		if (createdBatches != null) {
			BreakdownSale sale = new BreakdownSale(paymentDate, quantity, product, partner, _transactionId++, createdBatches);
			_transactions.put(sale.getId(), sale);
			partner.addSale(sale);
			partner.paySale(sale); // efetuar a contagem dos pontos/mudança de estatuto
			_availableBalance += sale.getAmountPaid(); // atualizar saldo disponivel caso tenhamos recebido dinheiro
			
			// adicionar lotes criados aos produtos e parceiros
			for (Batch newBatch : createdBatches) {
				// o produto que ficará associado a este lote é o tipo de produto do lote
		    	newBatch.getProduct().addBatch(newBatch);
		    	// o parceiro que ficará associado a este lote é aquele gerou o pedido
		    	partner.addBatch(newBatch);
			}
		}
			
		

	}

   
}
