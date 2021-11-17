package ggc.app.transactions;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import java.util.ArrayList;
import java.util.List;

import ggc.app.exception.UnknownPartnerKeyException;
import ggc.app.exception.UnknownProductKeyException;
import ggc.core.WarehouseManager;
import ggc.core.exception.UnknownPartnerException;
import ggc.core.exception.UnknownProductException;

//FIXME import classes

/**
 * Register order.
 */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);

    addStringField("partnerId", Message.requestPartnerKey());
    addStringField("productId", Message.requestProductKey());
    addRealField("price", Message.requestPrice());
    addIntegerField("quantity", Message.requestAmount());
    //FIXME maybe add command fields
  }

  @Override
  public final void execute() throws CommandException, UnknownPartnerKeyException, UnknownProductKeyException {

    String productId = stringField("productId");
    String partnerId = stringField("partnerId");
    Double price = realField("price");
    Integer quantity = integerField("quantity");

    
    // provavelmente aqui alterar tudo
    // para try _receiver.registerAcquisition e receber exceções do core e lançar
    // as exceções depois disso (para garantir que não é possível correr
    // o registerAcquisition do core com argumentos mal, como dito no email sobre a UnknownPartnerKeyException)
    
    try {
    	_receiver.registerAcquisition( quantity, productId, partnerId, price);
    } catch (UnknownPartnerException upae) {
    	throw new UnknownPartnerKeyException(partnerId);
    } catch (UnknownProductException upe) {
        Form isAggregateForm = new Form();
  	    isAggregateForm.addBooleanField("isAggregate", Message.requestAddRecipe());
  	    isAggregateForm.parse();
  	    Boolean isAggregate = isAggregateForm.booleanField("isAggregate");
        if(isAggregate) {
          Form recipeForm = new Form();
          recipeForm.addIntegerField("numComp", Message.requestNumberOfComponents());
          recipeForm.addRealField("alpha", Message.requestAlpha());
          recipeForm.parse();
          Integer i = recipeForm.integerField("numComp");
          double alpha = recipeForm.realField("alpha");
          
          List<String> componentKeys = new ArrayList<>();
          List<Integer> componentAmounts = new ArrayList<>();
          while(i>0) {
            Form componentForm = new Form();
            componentForm.addStringField("product", Message.requestProductKey());
            componentForm.addIntegerField("amount", Message.requestAmount());
            componentForm.parse();
            
            String componentProductId = componentForm.stringField("product");
            Integer amount = componentForm.integerField("amount");            
            try {
            	_receiver.getProduct(componentProductId);
            } catch (UnknownProductException uce) {
            	throw new UnknownProductKeyException(componentProductId);
            }
            componentKeys.add(componentProductId);
            componentAmounts.add(amount);

            i--;
          }
          try {
          _receiver.addAggregateProduct(productId, alpha, componentKeys, componentAmounts);
          } catch (UnknownProductException upce) {
        	  throw new RuntimeException(); //nunca deverá acontecer, foi tudo verificado em cima por motivos do output corresponder ao pedido. devia ter sido feita 1 interface em que recebiamos todos os componentes e so nos queixavamos se nao existia 1 componente no fim de termos recebido todos para facilitar
          }

        }
        else {
          _receiver.addSimpleProduct(productId,price);
        }  
        try {
        	_receiver.registerAcquisition( quantity, productId, partnerId, price);
        } catch (UnknownPartnerException | UnknownProductException upe2) {
        	throw new RuntimeException(); // se chegando aqui há problemas já houve bug antes, é crashar
        }
    }

    
      
  }

}
