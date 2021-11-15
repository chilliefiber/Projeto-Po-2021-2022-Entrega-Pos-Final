package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.UnknownPartnerException;
import ggc.app.exception.UnknownPartnerKeyException;

//FIXME import classes

/**
 * Lookup payments by given partner.
 */
public class DoLookupPaymentsByPartner extends Command<WarehouseManager> {

  public DoLookupPaymentsByPartner(WarehouseManager receiver) {
    super(Label.PAID_BY_PARTNER, receiver);
    addStringField("id", Message.requestPartnerKey());
    //FIXME add command fields
  }

  @Override
  public void execute() throws CommandException, UnknownPartnerKeyException {
    String id = stringField("id");
    try {
    	_display.popup(_receiver.getPartnerPaidSales(id));
    } catch (UnknownPartnerException upae) {
    	throw new UnknownPartnerKeyException(id);
    }
      
    
    //FIXME implement command
  }

}
