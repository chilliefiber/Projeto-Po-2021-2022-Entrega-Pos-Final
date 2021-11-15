package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.NegativeDateException;
import ggc.app.exception.*;
//FIXME import classes

/**
 * Advance current date.
 */
class DoAdvanceDate extends Command<WarehouseManager> {

  DoAdvanceDate(WarehouseManager receiver) {
    super(Label.ADVANCE_DATE, receiver);
    addIntegerField("number", Message.requestDaysToAdvance());
  }

  @Override
  public final void execute() throws CommandException, InvalidDateException {
    Integer numDaysToAdvance = integerField("number");
    try {
    	_receiver.advanceCurrentDate(numDaysToAdvance);
    } catch (NegativeDateException nde) {
    	throw new InvalidDateException(numDaysToAdvance);
    }
    
  }

}
