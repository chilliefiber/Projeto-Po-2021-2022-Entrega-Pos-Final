package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.UnknownTransactionException;
import ggc.app.exception.UnknownTransactionKeyException;
//FIXME import classes

/**
 * Show specific transaction.
 */
public class DoShowTransaction extends Command<WarehouseManager> {

  public DoShowTransaction(WarehouseManager receiver) {
    super(Label.SHOW_TRANSACTION, receiver);
    addIntegerField("id", Message.requestTransactionKey());
    //FIXME maybe add command fields
  }

  @Override
  public final void execute() throws CommandException, UnknownTransactionKeyException {
    int id = integerField("id");
    try {
    	_display.popup( _receiver.getTransaction(id));
    } catch(UnknownTransactionException ute) {
    	throw new UnknownTransactionKeyException(id);
    }
  }

}
