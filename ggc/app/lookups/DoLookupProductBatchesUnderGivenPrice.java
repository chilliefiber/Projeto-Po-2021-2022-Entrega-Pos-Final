package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
//FIXME import classes

/**
 * Lookup products cheaper than a given price.
 */
public class DoLookupProductBatchesUnderGivenPrice extends Command<WarehouseManager> {

  public DoLookupProductBatchesUnderGivenPrice(WarehouseManager receiver) {
    super(Label.PRODUCTS_UNDER_PRICE, receiver);
    addRealField("limite", Message.requestPriceLimit());
    //FIXME add command fields
  }

  @Override
  public void execute() throws CommandException {
    double limite = realField("limite");
    _display.popup(_receiver.getBatchesUnderLimit(limite));
    //FIXME implement command
  }

}
