package com.demo.searchengine.console;

import javax.naming.ConfigurationException;
import java.io.FileInputStream;
import java.util.Properties;

public class Helper {

    /*/
    reads the configuration specified by the requested key
     */
    public static String readFromConfigs(String key) throws ConfigurationException {
        try{
            Properties props = new Properties();
            String fileName = "C:\\Users\\user\\Desktop\\Coder\\Java\\JSearchEngine\\ConsoleApp\\src\\main\\resources\\application.properties";

            try (FileInputStream fis = new FileInputStream(fileName)) {
                props.load(fis);
            } catch (Exception ex) {
                throw new ConfigurationException("There is a problem with the configurations file.");
            }

            var valueFound =  props.getProperty(key);
            if (valueFound == null){
                throw new ConfigurationException(String.format("The specified key: %s was not found in the configurations file", key));
            }

            return valueFound;
        }
        catch(Exception ex){
            throw new ConfigurationException("There is a problem with the configurations file.");
        }
    }
}
