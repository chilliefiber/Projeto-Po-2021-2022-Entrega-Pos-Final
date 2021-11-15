package ggc.core;

import java.io.Serializable;

import ggc.core.exception.NegativeDateException;

class Date implements Serializable {
    
    private int _date;
    
    Date() {}

    Date(int date) {
      _date = date;
    }

    @Override
    public String toString() {
    	return "" + _date;
    }
    
    int getDate() {
      return _date;
    }

    void add(int days) throws NegativeDateException {
    	if (days < 0)
    		throw new NegativeDateException();
      _date += days;
    }
    
    int difference(Date other) {
    	return _date - other.getDate();
    }
    
    Date makeCopy() {
    	return new Date(_date);
    }
}
