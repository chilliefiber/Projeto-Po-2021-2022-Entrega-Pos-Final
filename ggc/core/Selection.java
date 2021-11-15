package ggc.core;
import java.io.Serializable;


public class Selection implements PartnerStatus, Serializable {

	@Override
	public double calculateDiscountOrPenalty(SaleByCredit sale, Date currentDate) {
		Period period = sale.getPeriod(currentDate);
		// 
		int numberDaysOverdue = sale.getNumberOfDaysOverdue(currentDate);
		// possivelmente ter 1 método para cada case
		// nesse caso o método do P1 podia ser partilhado por todos
		switch (period) {
			case P1:
				return -0.1 * sale.getBaseValue(); 
			case P2:
				return numberDaysOverdue <= -2 ? -0.05*sale.getBaseValue() : 0;
			case P3:
				return numberDaysOverdue > 1? numberDaysOverdue * 0.02 * sale.getBaseValue() : 0;
			case P4:
				return numberDaysOverdue * 0.05 * sale.getBaseValue();
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
		}
		// se a data de pagamento efetuado for mais do que 2 dias depois da data limite
		else if (sale.getNumberOfDaysOverdue(null) > 2) {
			partner.addPoints(-0.9 * partner.getPoints()); // perde 90% dos pontos acumulados
			partner.setStatus(new Normal());
			return; 
		}		
		// caso o prof responda no mail que não passa a Elite naquele caso
		// e só passa quando paga a tempo devemos passar esta condição
		// para dentro da primeira do método
		if (partner.getPoints() > 250000)
			partner.setStatus(new Elite());
	}

	public String toString() {
		return "SELECTION";
	}

}
