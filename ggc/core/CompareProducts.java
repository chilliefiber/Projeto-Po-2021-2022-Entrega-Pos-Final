package ggc.core;

import java.util.Comparator;

public class CompareProducts implements Comparator<Product> {
	public int compare(Product p1, Product p2) {
		return p1.getId().toLowerCase().compareTo(p2.getId().toLowerCase());
	}
}
