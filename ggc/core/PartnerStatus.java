package ggc.core;

interface PartnerStatus {
	// talvez passar 1 SaleByCredit visto que ele nao calcula descontos nem multas para desagregações
	 double calculateDiscountOrPenalty(SaleByCredit sale, Date currentDate);
	 
	 void calculatePointsAndSwitchState(Sale sale, Partner partner);
}
