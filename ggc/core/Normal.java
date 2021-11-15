package ggc.core;

import java.io.Serializable;

class Normal implements PartnerStatus, Serializable {

	// isto nao devia ser public retirar a seguir
	@Override
	public double calculateDiscountOrPenalty(SaleByCredit sale, Date currentDate) {
		Period period = sale.getPeriod(currentDate);
		int numberDaysOverdue = sale.getNumberOfDaysOverdue(currentDate);
		// possivelmente ter 1 método para cada case
		// nesse caso o método do P1 podia ser partilhado por todos
		switch (period) {
			case P1:
				return -0.1 * sale.getBaseValue(); 
			case P2:
				return 0;
			case P3:
				return numberDaysOverdue * 0.05 * sale.getBaseValue();
			case P4:
				return numberDaysOverdue * 0.1 * sale.getBaseValue();
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
		if (period == Period.P1 || period == Period.P2) {
			// é imperativo garantir que já pagámos antes de chegar aqui
			partner.addPoints(10 * sale.getAmountPaid());
			if (partner.getPoints() > 250000)
				partner.setStatus(new Elite());
			else if (partner.getPoints() > 2000)
				partner.setStatus(new Selection());
		}
		// possivelmente aqui verificar se o periodo nao é nenhum dos definidios e lançar a
		// runtimeException UnknownPeriodException
		
		//limpar todos os pontos
		else
			partner.addPoints(partner.getPoints() * -1);
	}

	public String toString () {
		return "NORMAL";
	}
}
