package ua.nure.rebrov.wholesale_base.patterns;

import ua.nure.rebrov.wholesale_base.model.Good;
import ua.nure.rebrov.wholesale_base.model.User;

import java.util.List;

public interface Observed <T> {
    public void addObserver(Observer observer);
    public void removeObserver(Observer observer);
    public void notifyObservers(T o, String action);
    public void notifyObservers(List<T> o, String action);

}
