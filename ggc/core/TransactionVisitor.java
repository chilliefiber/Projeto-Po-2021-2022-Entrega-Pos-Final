package ggc.core;

public interface TransactionVisitor {
	void visit(Acquisition acquisition);
	void visit(SaleByCredit sale);
	// void visit(BreakdownSale breakdownSale);
}
