package ggc.core;

import java.util.Comparator;

public class ComparePartners implements Comparator<Partner> {
	public int compare(Partner p1, Partner p2) {
		return p1.getId().toLowerCase().compareTo(p2.getId().toLowerCase());
	}
}
