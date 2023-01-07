package ua.nure.rebrov.wholesale_base.patterns;

import ua.nure.rebrov.wholesale_base.Test;
import ua.nure.rebrov.wholesale_base.model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.*;


public class ConsoleLogObserver implements Observer {
    static Logger LOGGER = Logger.getLogger(ConsoleLogObserver.class.getName());

    public ConsoleLogObserver(){
        try {
            LOGGER.addHandler(new FileHandler("observer.log", true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void notify(String message) {
        LOGGER.log(Level.INFO, message);

    }
}
