package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.DuplicatePartnerException;
import ggc.app.exception.*;
//FIXME import classes

/**
 * Register new partner.
 */
class DoRegisterPartner extends Command<WarehouseManager> {

  DoRegisterPartner(WarehouseManager receiver) {
    super(Label.REGISTER_PARTNER, receiver);
    addStringField("id", Message.requestPartnerKey());
    addStringField("name", Message.requestPartnerName());
    addStringField("address", Message.requestPartnerAddress());
  }

  @Override
  public void execute() throws CommandException, DuplicatePartnerKeyException {
    String id = stringField("id");
    String name = stringField("name");
    String address = stringField("address");
      
    try {
    	_receiver.addPartner(id, name, address);
    } catch (DuplicatePartnerException dpe) {
    	throw new DuplicatePartnerKeyException(id);
    }
  }

}
