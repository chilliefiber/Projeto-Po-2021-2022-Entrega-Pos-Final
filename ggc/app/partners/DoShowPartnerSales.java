package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.UnknownPartnerException;
import ggc.app.exception.UnknownPartnerKeyException;
//FIXME import classes

/**
 * Show all transactions for a specific partner.
 */
class DoShowPartnerSales extends Command<WarehouseManager> {

  DoShowPartnerSales(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER_SALES, receiver);
    addStringField("id", Message.requestPartnerKey());
    //FIXME add command fields
  }

  @Override
  public void execute() throws CommandException, UnknownPartnerKeyException {
    String id = stringField("id");
      
    try {
    	_display.popup( _receiver.getPartnerSales(id));
    } catch (UnknownPartnerException upe) {
    	throw new UnknownPartnerKeyException(id);
    }
    
  }

}
