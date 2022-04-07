package searchengine.library.repositories;

import searchengine.library.dtos.IDocumentDto;
import searchengine.library.dtos.ITokenDto;
import searchengine.library.entities.Document;

import java.security.InvalidParameterException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentRepository implements IDocumentRepository {
    private String _connectionUrl = "";

    public DocumentRepository(String connectionUrl){
        if (connectionUrl == null || connectionUrl.isBlank()){
            throw new InvalidParameterException("The database configuration can't be empty!");
        }

        _connectionUrl = connectionUrl;
    }

    public Document getDocument(int id) throws Exception {
        try(Connection connection = DriverManager.getConnection(_connectionUrl)){
            PreparedStatement statement = connection
                    .prepareStatement("SELECT Id FROM Documents WHERE Id = ?");

            statement.setInt(1, id);

            try(ResultSet resultSet = statement.executeQuery()){
                // no elements were found
                if (!resultSet.isBeforeFirst()){
                    return null;
                }
            }

            Document doc = new Document();
            doc.setId(id);
            return doc;
        }
        catch (SQLException ex){
            throw new Exception("An error occurred while reading the document from the database, docId: ".concat(String.valueOf(id)));
        }
    }

    public void insertDocument(IDocumentDto document) throws Exception{
        int id = document.getId();

        try(Connection connection = DriverManager.getConnection(_connectionUrl)){
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO Documents(Id) VALUES(?)");

            statement.setInt(1, id);

           statement.executeUpdate();
           statement.close();
        }
        catch (SQLException ex){
            throw new Exception("There is a connection problem to the database.");
        }
        catch(Exception e){
            throw new Exception("Was not possible inserting document to the database, id: ".concat(String.valueOf(id)));
        }
    }

    public void deleteDocument(IDocumentDto document) throws Exception {
        int id = document.getId();

        try(Connection connection = DriverManager.getConnection(_connectionUrl)){
            PreparedStatement statement = connection
                    .prepareStatement("DELETE FROM Tokens WHERE DocumentId = ?");

            statement.setInt(1, id);

            statement.execute();
            statement.close();
        }
        catch (SQLException ex){
            throw new Exception("There is a connection problem to the database.");
        }
        catch(Exception e){
            throw new Exception("Was not possible deleting document from the database, id: ".concat(String.valueOf(id)));
        }

    }

    public void insertTokensForDocument(IDocumentDto document) throws Exception{
        int id = document.getId();

        try(Connection connection = DriverManager.getConnection(_connectionUrl)){
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO Tokens(Content, DocumentId) VALUES(?, ?)");

            for (ITokenDto token :document.getTokens()) {
                statement.setString(1, token.getContent());
                statement.setInt(2, id);
                statement.addBatch();
                statement.clearParameters();
            }

            statement.executeBatch();
            statement.close();
        }
        catch (SQLException ex){
            throw new Exception("There is a connection problem to the database.");
        }
        catch(Exception e){
            throw new Exception("Was not possible inserting tokens to the database for the token with id: ".concat(String.valueOf(id)));
        }

    }

    @Override
    public List<Integer> queryDocumentsForTokensContent(String query, List<String> parameters) throws Exception {
        List<Integer> response = new ArrayList<>();
        int counter = 1;

        try(Connection connection = DriverManager.getConnection(_connectionUrl)){
            PreparedStatement statement = connection.prepareStatement(query);

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


}
