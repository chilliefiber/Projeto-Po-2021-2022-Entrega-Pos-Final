package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.app.exception.UnknownProductKeyException;
import ggc.app.exception.UnavailableProductException;
import ggc.core.WarehouseManager;
import ggc.core.exception.InsufficientComponentUnitsException;
import ggc.core.exception.InsufficientUnitsException;
import ggc.core.exception.UnknownPartnerException;
//FIXME import classes
import ggc.core.exception.UnknownProductException;

/**
 * 
 */
public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

  public DoRegisterSaleTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    addStringField("partnerId", Message.requestPartnerKey());
    addIntegerField("deadline", Message.requestPaymentDeadline());
    addStringField("productId", Message.requestProductKey());
    addIntegerField("quantity", Message.requestAmount());
    //FIXME maybe add command fields 
  }

  @Override
  public final void execute() throws CommandException, UnknownPartnerKeyException, UnknownProductKeyException, UnavailableProductException {
    String productId = stringField("productId");
    String partnerId = stringField("partnerId");
    Integer deadline = integerField("deadline");
    Integer quantity = integerField("quantity");

    try {
    	_receiver.registerSaleByCredit(partnerId, deadline, productId, quantity);
    } catch (UnknownPartnerException upae){
    	throw new UnknownPartnerKeyException(partnerId);
    } catch (UnknownProductException upe){
    	throw new UnknownProductKeyException(productId);
    } catch (InsufficientComponentUnitsException icue) {
    	throw new UnavailableProductException(icue.getProductId(), icue.getNeededUnits(), icue.getAvailableUnits());
    }
  }

}


