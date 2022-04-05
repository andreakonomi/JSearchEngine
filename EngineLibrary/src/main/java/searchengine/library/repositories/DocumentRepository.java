package searchengine.library.repositories;

import searchengine.library.dtos.IDocumentDto;
import searchengine.library.dtos.ITokenDto;
import searchengine.library.entities.Document;

import javax.xml.transform.Result;
import java.security.InvalidParameterException;
import java.sql.*;

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
            throw new Exception("An error occured while reading the document from the database, docId: ".concat(String.valueOf(id)));
        }
    }

    public void insertDocument(IDocumentDto document) throws Exception{
        int id = document.getId();

        try(Connection connection = DriverManager.getConnection(_connectionUrl)){
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO Documents(Id) VALUES(?)");

            statement.setInt(1, id);

           statement.executeQuery();
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

            statement.executeQuery();
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

            statement.executeQuery();
            statement.close();
        }
        catch (SQLException ex){
            throw new Exception("There is a connection problem to the database.");
        }
        catch(Exception e){
            throw new Exception("Was not possible inserting tokens to the database for the token with id: ".concat(String.valueOf(id)));
        }

    }
}
