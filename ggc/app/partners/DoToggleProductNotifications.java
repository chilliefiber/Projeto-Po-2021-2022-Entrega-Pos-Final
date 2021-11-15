package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.app.exception.UnknownProductKeyException;
import ggc.core.WarehouseManager;
import ggc.core.exception.UnknownPartnerException;
import ggc.core.exception.UnknownProductException;

/**
 * Toggle product-related notifications.
 */
class DoToggleProductNotifications extends Command<WarehouseManager> {

  DoToggleProductNotifications(WarehouseManager receiver) {
    super(Label.TOGGLE_PRODUCT_NOTIFICATIONS, receiver);
    addStringField("partnerId", Message.requestPartnerKey());
    addStringField("productId", Message.requestProductKey());
    //FIXME add command fields
  }

  @Override
  public void execute() throws CommandException, UnknownPartnerKeyException, UnknownProductKeyException {
    String partnerId = stringField("partnerId");
    String productId = stringField("productId");
    try{
      _receiver.toggleNotifications(partnerId, productId);
    } catch(UnknownPartnerException upae) {
      throw new UnknownPartnerKeyException(partnerId);
    } catch(UnknownProductException upue) {
      throw new UnknownProductKeyException(productId);
  }
}
}
