package com.demo.searchengine.console;

import java.io.*;
import java.util.Properties;

public class Engine {
    public void run() {
        String response;
        String input = PromptUser();

        while (!isExitInput(input)){
            response = processInput(input);

            System.out.println(response);
            input = PromptUser();
        }

        System.out.println("Your data is safe with us, see you next time!");
    }

    private String processInput(String input) {
        if (input == null || input.isBlank()){
            return "An empty input is not valid.";
        }

        if (isIndexInput(input)){
            input = removeInputPrefix(input);
            return insertInput(input);
        }

        if (isQueryInput(input)){
            input = removeInputPrefix(input);
            return searchData(input);
        }

        return "No valid input has been provided";
    }

    /**
     * @param query Query to ask for information
     * @return The results found for the given query
     */
    private String searchData(String query) {
        return "";
    }

    /**
     * Validates and inserts the given input by the user to the storage.
     */
    private String insertInput(String input) {
        Properties props = new Properties();
        String fileName = "application.properties";
        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (Exception ex) {
            return "There is a problem with the configurations file.";
        }

        String connectionUrl = props.getProperty("dbUrl");




        return "";
    }

    private String PromptUser() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.print("Press 'Exit' or insert a token: ");
            return reader.readLine();
        }
        catch(Exception e){
            return "Invalid input";
        }
    }

    /*
    * Checks if the input by the user is exit related.
    */
    private boolean isExitInput(String input){
        return input.equalsIgnoreCase("exit");
    }

    /**
     * @param input The input given to be scanned
     * @return true if the input uses the standard index indicator
     * for inserting to the index, false otherwise.
     */
    private boolean isIndexInput(String input){
        if (input.length() < 7){
            return false;
        }

        return input.toLowerCase().startsWith("index");
    }

    /**
     * @param input The input given to be scanned
     * @return true if the input provided uses the standard query
     * indicator at the beginning, false otherwise.
     */
    private boolean isQueryInput(String input){
        if (input.length() < 6){
            return false;
        }

        return input.toLowerCase().startsWith("query");
    }

    /**
     * @param input The input provided
     * @return Removes the prefix standard for search engine commands
     */
    private String removeInputPrefix(String input){
        return input.substring(5);
    }
}
