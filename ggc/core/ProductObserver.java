package ggc.core;

interface ProductObserver {
	void update(Notification notification);
	// sera que precisa dos hashCode e equals para o hashSet????
}
