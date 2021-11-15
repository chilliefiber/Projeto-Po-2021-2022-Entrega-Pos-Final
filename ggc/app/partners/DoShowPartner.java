package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.core.WarehouseManager;
import ggc.core.exception.UnknownPartnerException;

/**
 * Show partner.
 */
class DoShowPartner extends Command<WarehouseManager> {

  DoShowPartner(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER, receiver);
    addStringField("id", Message.requestPartnerKey());
    
  }

  @Override
  public void execute() throws CommandException, UnknownPartnerKeyException {
    String id = stringField("id");
      
   try {
	   _display.popup( _receiver.getPartner(id));
   } catch (UnknownPartnerException upe) {
	   throw new UnknownPartnerKeyException(id);
   }
    
  }

}
