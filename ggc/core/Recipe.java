package ggc.core;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

class Recipe implements Serializable {

    private double _alfa;
    List <Component> _components = new ArrayList<>();
   
    Recipe(double alfa) {
        _alfa = alfa;      
    }

    double getAlfa() {
        return _alfa;
    }

    List <Component> getComponents() {
        return _components;
    }

    void addComponent(Component c) {
       _components.add(c);
    }
    
    int getNumberOfComponents() {
    	return _components.size();
    }
     
}
