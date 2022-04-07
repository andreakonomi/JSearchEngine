package com.demo.searchengine.console;

import javax.naming.ConfigurationException;
import java.io.FileInputStream;
import java.util.Properties;

public class Helper {

    public static String readFromConfigs(String key) throws ConfigurationException {
        try{
            Properties props = new Properties();
            String fileName = "C:\\Users\\user\\Desktop\\Coder\\Java\\JSearchEngine\\ConsoleApp\\src\\main\\resources\\application.properties";

            try (FileInputStream fis = new FileInputStream(fileName)) {
                props.load(fis);
            } catch (Exception ex) {
                throw new ConfigurationException("There is a problem with the configurations file.");
            }

            return props.getProperty("key");
        }
        catch(Exception ex){
            throw new ConfigurationException("There is a problem with the configurations file.");
        }
    }
}
