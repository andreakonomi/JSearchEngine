package searchengine.library.dataAccess;

import searchengine.library.Factory;
import searchengine.library.QueryBuilder;
import searchengine.library.dtos.IDocumentDto;
import searchengine.library.dtos.ITokenDto;
import searchengine.library.entities.Document;
import searchengine.library.repositories.DocumentRepository;
import searchengine.library.repositories.IDocumentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentData implements IDocumentData{

    private final String _connectionUrl;

    public DocumentData(String connectionUrl) throws IllegalArgumentException{
        if (connectionUrl.isBlank()){
            throw new IllegalArgumentException("Connection url can't be null or blank");
        }

        _connectionUrl = connectionUrl;
    }

    @Override
    public void createDocument(IDocumentDto document) throws Exception{
        boolean valid = isDocumentValid(document);
        if (!valid){
            throw new IllegalArgumentException("The content provided is invalid, all tokens need to be alphanumerical!");
        }

        IDocumentRepository docRepo = new DocumentRepository(_connectionUrl);
        int id = document.getId();

        Document doc = docRepo.getDocument(id);
        if(doc == null){
            docRepo.insertDocument(document);
        }
        else{
            docRepo.deleteDocument(document);
        }

        docRepo.insertTokensForDocument(document);
    }

    /*/
    takes the queryExpression requested by the user and returns the ids of the documents
    matching that query request
     */
    @Override
    public List<Integer> searchByTokensContent(String queryExpression) throws Exception {

        if (queryExpression == null || queryExpression.isBlank()){
            return null;
        }

        List<String> parameters = new ArrayList<>();
        String queryToPass = QueryBuilder.getFormatedQueryToExec(queryExpression.trim(), parameters);

        var repo = Factory.getDocumentRepository(_connectionUrl);
        return repo.queryDocumentsForTokensContent(queryToPass, parameters);
    }

    private List<Integer> getDocumentsForTokens(String queryExpression, List<String> parameters) throws Exception {
        List<Integer> response = new ArrayList<>();
        int counter = 1;

        try(Connection connection = DriverManager.getConnection(_connectionUrl)){
            PreparedStatement statement = connection.prepareStatement(queryExpression);

            for (String param:parameters) {
                statement.setString(counter++ , param);
            }

            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    response.add(resultSet.getInt("DocumentId"));
                }
            }
        }
        catch (SQLException ex){
            throw new Exception("There is a connection problem to the database.");
        }
        catch(Exception e){
            throw new Exception("There was a problem while trying to query the tokens from the database.");
        }

        return response;
    }

    /**
     *
     * @param document Document entity to be checked for validity.
     * @return true if document passes all validity tests.
     */
    private boolean isDocumentValid(IDocumentDto document){
        if (document == null){
            return false;
        }

        return AreTokensValid(document.getTokens());
    }

    /**
     *
     * @param tokens list of tokens to check for validity
     * @return true if all tokens are valid, otherwise false.
     */
    private boolean AreTokensValid(List<ITokenDto> tokens){
        return tokens.stream().allMatch(x -> isTokenContentValid(x.getContent()));
    }

    /**
     * Validates if the content of a token is valid.
     * @return true if content is valid, otherwise false.
     */
    private boolean isTokenContentValid(String content){
        return content.chars().allMatch(Character::isLetterOrDigit);
    }
}
