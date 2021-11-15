package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.UnknownTransactionException;
import ggc.app.exception.UnknownTransactionKeyException;

/**
 * Receive payment for sale transaction.
 */
public class DoReceivePayment extends Command<WarehouseManager> {

  public DoReceivePayment(WarehouseManager receiver) {
    super(Label.RECEIVE_PAYMENT, receiver);
    addIntegerField("id", Message.requestTransactionKey());
  }

  @Override
  public final void execute() throws CommandException,  UnknownTransactionKeyException {
    int id = integerField("id");
    try {
    	_receiver.receivePayment(id);
    } catch (UnknownTransactionException ute) {
    	throw new UnknownTransactionKeyException(id);
    }
    
    //FIXME implement command
  }

}