package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.core.WarehouseManager;
import ggc.core.exception.UnknownPartnerException;

/**
 * Show batches supplied by partner.
 */
class DoShowBatchesByPartner extends Command<WarehouseManager> {

  DoShowBatchesByPartner(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_SUPPLIED_BY_PARTNER, receiver);
    addStringField("id", Message.requestPartnerKey());
    
  }

  @Override
  public final void execute() throws CommandException, UnknownPartnerKeyException {
    String id = stringField("id");
      
    try {
    	_display.popup( _receiver.getBatchesByPartner(id));
    } catch (UnknownPartnerException upae) {
    	throw new UnknownPartnerKeyException(id);
    }
    
   
  }

}
