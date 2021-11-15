package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.UnknownProductKeyException;
import ggc.core.exception.UnknownProductException;
import ggc.core.WarehouseManager;


/**
 * Show all products.
 */
class DoShowBatchesByProduct extends Command<WarehouseManager> {

  DoShowBatchesByProduct(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_BY_PRODUCT, receiver);
    addStringField("id", Message.requestProductKey());
  }

  @Override
  public final void execute() throws CommandException, UnknownProductKeyException {
    String id = stringField("id");
    
    try {
    	_display.popup( _receiver.getBatchsOf(id));
    } catch (UnknownProductException upe) {
    	throw new  UnknownProductKeyException(id);
    }
  }
}


