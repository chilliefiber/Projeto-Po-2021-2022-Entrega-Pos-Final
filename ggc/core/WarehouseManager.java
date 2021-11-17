package ggc.core;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import ggc.core.exception.UnavailableFileException;
import ggc.core.exception.UnknownPartnerException;
import ggc.core.exception.UnknownProductException;
import ggc.core.exception.UnknownTransactionException;
import ggc.core.exception.BadEntryException;
import ggc.core.exception.DuplicatePartnerException;
import ggc.core.exception.ImportFileException;
import ggc.core.exception.InsufficientComponentUnitsException;
import ggc.core.exception.InsufficientUnitsException;
import ggc.core.exception.MissingFileAssociationException;
import ggc.core.exception.NegativeDateException;

/** Façade for access. */
public class WarehouseManager {

  /** Name of file storing current warehouse. */
  private String _filename = "";

  /** The wharehouse itself. */
  private Warehouse _warehouse = new Warehouse();
  
  public void addPartner(String id, String name, String address) throws DuplicatePartnerException{
    _warehouse.addPartner(id, name, address);
  }

  public int currentDate() {
    return _warehouse.getDate();
  }

  public void advanceCurrentDate(int days) throws NegativeDateException{
    _warehouse.addDays(days);
  }

  public int getAccountingBalance() {
    return (int) Math.round(_warehouse.getAccountingBalance());
  }

  public int getAvailableBalance() {
    return (int) Math.round(_warehouse.getAvailableBalance());
  }

  public Transaction getTransaction(int id) throws UnknownTransactionException {
    return _warehouse.getTransaction(id);
  }

  // sem fuga de privacidade
  public Collection<Product> getProducts() {
    return  _warehouse.getProducts();
  }

  
  public Product getProduct(String id) throws UnknownProductException {
	return _warehouse.getProduct(id);
  }

  // sem fuga de privacidade
  public List<Batch> getBatchsOf(String product) throws UnknownProductException {

    return _warehouse.getProduct(product).getAllBatches();
  }

  // sem fuga
  public List<Batch> getAllBatchs() {
    return _warehouse.getAllBatches();
  }
  
  // sem fuga
  public List<Batch> getBatchesUnderLimit( double limit) {
	  return _warehouse.getBatchesUnderLimit(limit);
  }

 
  public Partner getPartner(String id) throws UnknownPartnerException{
	  Partner partner = _warehouse.getPartner(id);
	  partner.setShowNotifications(true);
    return partner;
  }

  // sem fuga
  public Collection<Partner> getPartners() {
    return _warehouse.getPartners();
  }
  // sem fuga
  public Collection<Batch> getBatchesByPartner(String id) throws UnknownPartnerException {
	  return _warehouse.getPartner(id).getBatches();
  }

  
  public void registerAcquisition(int quantity, String productId, String partnerId, double pricePerUnit ) throws UnknownProductException, UnknownPartnerException {
    _warehouse.registerAcquisition(quantity, _warehouse.getProduct(productId), _warehouse.getPartner(partnerId), pricePerUnit);
  }
  
  // sem fuga
  public Collection<Acquisition> getPartnerAcquisitions(String partnerId) throws UnknownPartnerException {
	  return _warehouse.getPartnerAcquisitions(_warehouse.getPartner(partnerId));
  }

  // sem fuga
  public Collection<Sale> getPartnerSales(String partnerId) throws UnknownPartnerException {
    return _warehouse.getPartnerSales(_warehouse.getPartner(partnerId));
  }

  public void addAggregateProduct(String productId, double alpha, List<String> componentKeys, List<Integer> componentAmounts) throws UnknownProductException{
	  List<Product> components = new ArrayList<Product>();
	  for (int i = 0; i < componentKeys.size(); i++)
		  components.add(_warehouse.getProduct(componentKeys.get(i)));
    AggregateProduct newProduct = new AggregateProduct(productId, alpha, components, componentAmounts);
    _warehouse.addProduct(newProduct);
  }

  public void addSimpleProduct(String productId, double price) {
    _warehouse.addProduct(new SimpleProduct(productId,price));
  }
  
  public void addComponentRecipe(String aggregate, String productId, int amount) throws UnknownProductException {  
	  Product product = null;
	  try {
	    product = _warehouse.getProduct(aggregate);
	  } catch (UnknownProductException upe) {
		  throw new RuntimeException(); // isto nunca deve acontecer, este método não deve nunca ser chamado com 1 produto que não existe
	  }
	  product.addComponentRecipe(_warehouse.getProduct(productId),amount);
  }
  
  /**
   * @@throws IOException
   * @@throws FileNotFoundException
   * @@throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
	  if (!storedFilename())
		  throw new MissingFileAssociationException();
	  try (ObjectOutputStream obOut = new ObjectOutputStream(new FileOutputStream(_filename))){
		  obOut.writeObject(_warehouse);
	  } 
  }

  /**
   * @@param filename
   * @@throws MissingFileAssociationException
   * @@throws IOException
   * @@throws FileNotFoundException
   */
  public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
	_filename = filename;    
    save();
  }

  /**
   * @@param filename
   * @@throws UnavailableFileException
   */
  public void load(String filename) throws UnavailableFileException, ClassNotFoundException  {
	  _filename = filename;
	  try (ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(_filename))) {
		    _warehouse = (Warehouse) objIn.readObject();
      }
	  catch (IOException ioe) {
		 throw new UnavailableFileException(_filename);
	  } catch (ClassNotFoundException cnfe) {
		  throw cnfe;
	  }
  }

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException {
    try {
      _warehouse.importFile(textfile);
    } catch (IOException | BadEntryException e) {
      throw new ImportFileException(textfile, e);
    }
  }

  	public boolean storedFilename() {	
  		return ! "".equals(_filename);
  	}
  	
  	public String getFilename() {
  		return _filename;
  	}
  	
  	// throws Exceptions when partner doesn't exist, product doesn't exist, 
  	// price is not positive and quantity is not positive
  	// este método deve estar no warehouse...
  	public void registerAcquisition(String partnerId, String productId, double price, int quantity) throws UnknownPartnerException, UnknownProductException {
  		Product product = _warehouse.getProduct(productId);
  		Partner partner = _warehouse.getPartner(partnerId);
  		Batch newBatch = new Batch(price, quantity, product, partner);
  		product.addBatch(newBatch);
  		partner.addBatch(newBatch);
  	}
  	
  	// lançar 1 exceção quando não há suficiente. Na app é a 
  	// perguntar se esse é o melhor comportamento, se devemos lançar uma exceção do core e apanhar na app
  	// se a devemos criar etc...
  	// se devemos devolver 1 boolean e lançar a exceção nesse caso
  	public void registerSaleByCredit(String partnerId, int deadline, String productId, int quantity) throws InsufficientComponentUnitsException,
  	                                                                                                        UnknownProductException, UnknownPartnerException {
  		Product product = _warehouse.getProduct(productId);
  		Partner partner = _warehouse.getPartner(partnerId);
  		
  		if (partner == null)
  			throw new UnknownPartnerException();
  		if (product == null)
  			throw new UnknownProductException();
  		_warehouse.registerSaleByCredit(_warehouse.getPartner(partnerId), deadline, _warehouse.getProduct(productId), quantity);
  	}

    public void receivePayment(int transactionId) throws UnknownTransactionException {
    	Transaction transaction = _warehouse.getTransaction(transactionId);
    	if (transaction == null)
    		throw new UnknownTransactionException();
      _warehouse.receivePayment(transaction);
    }
    
    public Collection<Sale> getPartnerPaidSales(String id) throws UnknownPartnerException {
    	Partner partner = _warehouse.getPartner(id);
    	// fuga de privacidade lidada no Warehouse
        return _warehouse.getPartnerPaidSales(partner);
    }
    
    public void toggleNotifications(String partnerId, String productId) throws UnknownPartnerException, UnknownProductException{
    	Product product = _warehouse.getProduct(productId);
    	Partner partner = _warehouse.getPartner(partnerId);
    	
    	if (partner == null)
    		throw new UnknownPartnerException();
    	if (product == null)
    		throw new UnknownProductException();
    	
    	_warehouse.toggleNotifications(partner, product);
    }
    
    public void registerBreakdown(String partnerId, String productId, int quantity) throws UnknownPartnerException, UnknownProductException, InsufficientUnitsException {
    	Product product = _warehouse.getProduct(productId);
    	Partner partner = _warehouse.getPartner(partnerId);
    	
    	if (partner == null)
    		throw new UnknownPartnerException();
    	if (product == null)
    		throw new UnknownProductException();
    	
    	_warehouse.registerBreakdown(partner, product, quantity);
    	
    }
}
