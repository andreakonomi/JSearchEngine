package com.demo.searchengine.console;

import searchengine.library.dataAccess.IDocumentData;
import searchengine.library.dtos.IDocumentDto;
import searchengine.library.dtos.ITokenDto;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.*;

public class Engine {

    private Hashtable<String, ArrayList<Integer>> DocumentIdsCache = new Hashtable<>();

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
        try{
            Properties props = new Properties();
            String fileName = "C:\\Users\\user\\Desktop\\Coder\\Java\\JSearchEngine\\ConsoleApp\\src\\main\\resources\\application.properties";

            try (FileInputStream fis = new FileInputStream(fileName)) {
                props.load(fis);
            } catch (Exception ex) {
                return "There is a problem with the configurations file.";
            }

            String connectionUrl = props.getProperty("dbUrl");

            IDocumentDto document = parseDocument(input);
            IDocumentData docData = Factory.createDocumentData(connectionUrl);

            docData.createDocument(document);

            //cache no longer valid after input
            DocumentIdsCache.clear();

            return "index ok ".concat(String.valueOf(document.getId()));
        }
        catch(Exception ex){
            return "index error ".concat(ex.getMessage());
        }
    }

    /**]
     *
     * @param inputDoc inputed string by the user to be converted to a DocumentDto
     * @return DocumentDto represantation of the input.
     */
    private IDocumentDto parseDocument (String inputDoc){
        var docCreation = Factory.createDocumentDto();
        var tokensArray = inputDoc.split(" ");

        boolean ok;
        int id;
        try{
            // first element is a null after split
            id = Integer.parseInt(tokensArray[1]);
        }
        catch(Exception ex){
            throw new InvalidParameterException("The id you have inserted is not numeric");
        }

        docCreation.setId(id);
        docCreation.setTokens(parseContent(tokensArray));

        return docCreation;
    }

    private String PromptUser() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.print("Press 'Exit' or insert a token: ");
            return reader.readLine().trim();
        }
        catch(Exception e){
            return "Invalid input";
        }
    }

    private List<ITokenDto> parseContent(String[] tokens){
        // array's first element is a null element from the split method
        String tokenContent = "";
        boolean isValid = true;
        int arrayLength = tokens.length;

        if (arrayLength < 3){
            throw new InvalidParameterException("The document needs to have at least one token.");
        }

        List<ITokenDto> tokensList = Factory.createTokensList();

        for (int i = 2; i < arrayLength; i++){
            tokenContent = tokens[i];
            isValid = isTokenValid(tokenContent);

            if(!isValid){
                throw new InvalidParameterException("Incorrect input!The value provided for a token is not alphanumerical: ".concat(tokenContent));
            }

            tokensList.add(Factory.createToken(tokenContent));
        }

        return tokensList;
    }

    /**
     * Validates if the content of a token is valid.
     * @return true if content is valid, otherwise false.
     */
    private boolean isTokenValid(String tokenContent){
        return tokenContent.chars().allMatch(Character::isLetterOrDigit);
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
