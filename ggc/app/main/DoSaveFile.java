package ggc.app.main;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import java.io.IOException;
import ggc.app.exception.FileOpenFailedException;
import ggc.core.WarehouseManager;
import ggc.core.exception.MissingFileAssociationException;

/**
 * Save current state to file under current name (if unnamed, query for name).
 */
class DoSaveFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoSaveFile(WarehouseManager receiver) {
    super(Label.SAVE, receiver);
  }

  @Override
  public final void execute() throws CommandException {
	  if (!_receiver.storedFilename()){
	      Form form = new Form();
	      form.addStringField("file", Message.newSaveAs());
	      form.parse();
	      String file = form.stringField("file");
	      try {
	    		_receiver.saveAs(file);
	    } catch (IOException | MissingFileAssociationException e) {
	    	throw new FileOpenFailedException(file);
	    }
	  }
	else {
		try {
			_receiver.save();
		} catch (IOException | MissingFileAssociationException e) {
			// 
			throw new FileOpenFailedException(_receiver.getFilename());
		}
	}
  }  
}
    
