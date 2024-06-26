package ggc.core;
import java.io.Serializable;


public class Elite implements PartnerStatus, Serializable {

	@Override
	public double calculateDiscountOrPenalty(SaleByCredit sale, Date currentDate) {
		Period period = sale.getPeriod(currentDate);
		// possivelmente ter 1 método para cada case
		// nesse caso o método do P1 podia ser partilhado por todos
		switch (period) {
			case P1:
				return -0.1 * sale.getBaseValue(); 
			case P2:
				return -0.1 * sale.getBaseValue();
			case P3:
				return -0.05 * sale.getBaseValue();
			case P4:
				return 0;
			default:
				return 0;
				// throw new UnknownPeriodException();
				// perguntar ao prof isto
				// nao esquecer de indicar na declaração do metodo que lançamos esta exceção
		}
	}

	@Override
	public void calculatePointsAndSwitchState(Sale sale, Partner partner) {
		Period period = sale.getPeriod(sale.getPaymentDate());
		if (period == Period.P1 || period == Period.P2) 
			// é imperativo garantir que já pagámos antes de chegar aqui
			partner.addPoints(10 * sale.getAmountPaid());
		else if (sale.getNumberOfDaysOverdue(null) > 15) {
			partner.addPoints(-0.75 * partner.getPoints()); // perde 75% dos pontos acumulados
			partner.setStatus(new Selection());
			return; 
		}		
		
	}

	public String toString() {
		return "ELITE";
	}

}
