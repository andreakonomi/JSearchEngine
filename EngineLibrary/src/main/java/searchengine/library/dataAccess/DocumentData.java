package searchengine.library.dataAccess;

import searchengine.library.Factory;
import searchengine.library.QueryBuilder;
import searchengine.library.dtos.IDocumentDto;
import searchengine.library.dtos.ITokenDto;
import searchengine.library.entities.Document;
import searchengine.library.repositories.DocumentRepository;
import searchengine.library.repositories.IDocumentRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    @Override
    public List<Integer> searchByTokensContent(String queryExpression) {

        if (queryExpression == null || queryExpression.isBlank()){
            return null;
        }

        return getTokensForQuery(queryExpression.trim());
    }

    private List<Integer> getTokensForQuery(String queryExpression) throws Exception {
        List<String> parameters = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(_connectionUrl)){
            PreparedStatement statement = connection
                    .prepareStatement(QueryBuilder.getFormatedQueryToExec(queryExpression, parameters));








            statement.executeQuery();
            statement.close();
        }
        catch (SQLException ex){
            throw new Exception("There is a connection problem to the database.");
        }
        catch(Exception e){
            throw new Exception("Was not possible inserting tokens to the database for the token with id: ".concat(String.valueOf(id)));
        }


        return null;
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
