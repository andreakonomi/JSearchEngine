package com.demo.searchengine.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Engine {
    public void Run() {
        String response;
        String input = PromptUser();

        while (!IsExitInput(input)){
            response = ProcessInput(input);

            System.out.println(response);
            input = PromptUser();
        }

        System.out.println("Your data is safe with us, see you next time!");
    }

    private String ProcessInput(String input) {
        if (input == null || input.isBlank()){
            return "An empty input is not valid.";
        }

        if (IsIndexInput(input)){
            input = RemoveInputPrefix(input);
            return InsertInput(input);
        }

        if (IsQueryInput(input)){
            input = RemoveInputPrefix(input);
            return SearchData(input);
        }

        return "No valid input has been provided";
    }

    /**
     * @param query Query to ask for information
     * @return The results found for the given query
     */
    private String SearchData(String query) {
        return "";
    }

    /**
     * Validates and inserts the given input by the user to the storage.
     */
    private String InsertInput(String input) {
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
    private boolean IsExitInput(String input){
        return input.equalsIgnoreCase("exit");
    }

    /**
     * @param input The input given to be scanned
     * @return true if the input uses the standard index indicator
     * for inserting to the index, false otherwise.
     */
    private boolean IsIndexInput(String input){
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
    private boolean IsQueryInput(String input){
        if (input.length() < 6){
            return false;
        }

        return input.toLowerCase().startsWith("query");
    }

    /**
     * @param input The input provided
     * @return Removes the prefix standard for search engine commands
     */
    private String RemoveInputPrefix(String input){
        return input.substring(5);
    }
}
