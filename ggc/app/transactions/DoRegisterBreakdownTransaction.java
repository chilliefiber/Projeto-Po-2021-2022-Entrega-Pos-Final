package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.UnavailableProductException;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.app.exception.UnknownProductKeyException;
import ggc.core.WarehouseManager;
import ggc.core.exception.InsufficientUnitsException;
import ggc.core.exception.UnknownPartnerException;
//FIXME import classes
import ggc.core.exception.UnknownProductException;

/**
 * Register order.
 */
public class DoRegisterBreakdownTransaction extends Command<WarehouseManager> {

  public DoRegisterBreakdownTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_BREAKDOWN_TRANSACTION, receiver);
    addStringField("idPartner", Message.requestPartnerKey());
    addStringField("idProduct", Message.requestProductKey());
    addIntegerField("amount", Message.requestAmount());


    //FIXME maybe add command fields
  }
  
  @Override
  public final void execute() throws CommandException {
	  String partnerId = stringField("idPartner");
	  String productId = stringField("idProduct");
	  int amount = integerField("amount");
	  try {
		  _receiver.registerBreakdown(partnerId, productId, amount);
	  } catch (UnknownPartnerException upae) {
		  throw new UnknownPartnerKeyException(partnerId);
	  } catch (UnknownProductException uprae) {
		  throw new UnknownProductKeyException(productId);
	  } catch (InsufficientUnitsException iue) {
		  throw new UnavailableProductException(productId, amount, iue.getAvailableUnits());
	  }
  }

}
