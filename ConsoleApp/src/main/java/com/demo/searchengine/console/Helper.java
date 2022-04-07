package com.demo.searchengine.console;

import javax.naming.ConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class Helper {

    /*/
    reads the configuration specified by the requested key
     */
    public static String readFromConfigs(String key) throws ConfigurationException, FileNotFoundException {
        String result = "";
        InputStream inputStream;

        try{
            Properties props = new Properties();
            String propFileName = "application.properties";
            inputStream = Helper.class.getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null){
                props.load(inputStream);
            }
            else{
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            result = props.getProperty(key);

            if (result == null){
                throw new ConfigurationException(String.format("The specified key: %s was not found in the configurations file", key));
            }

            return result;
        }
        catch(FileNotFoundException e){
            throw e;
        }
        catch(Exception ex){
            throw new ConfigurationException("There is a problem with the configurations file.");
        }
    }
}
