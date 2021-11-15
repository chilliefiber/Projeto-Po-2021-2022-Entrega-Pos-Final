package ggc.core;

import java.util.Comparator;

public class CompareBatches implements Comparator<Batch> {
	public int compare(Batch b1, Batch b2) {
		if (b1.getProductId().equals(b2.getProductId()))
			if (b1.getPartnerId().equals(b2.getPartnerId()))
				if (b1.getPrice() == b2.getPrice())
					return b1.getQuantity() - b2.getQuantity();
				else
					// ter em atenção se por exemplo b1.getPrice() - b2.getPrice() for 0.3 devolver 1, se for -0.3 devolver -1 (mesma logica para outros decimais)
					return b1.getPrice() > b2.getPrice() ? (int) (Math.ceil(b1.getPrice() - b2.getPrice())) : (int) (Math.floor(b1.getPrice() - b2.getPrice()));
			else
				return b1.getPartnerId().toLowerCase().compareTo(b2.getPartnerId().toLowerCase());
		else
			return b1.getProductId().toLowerCase().compareTo(b2.getProductId().toLowerCase());
	}
}
