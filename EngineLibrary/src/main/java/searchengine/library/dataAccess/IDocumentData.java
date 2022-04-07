package searchengine.library.dataAccess;

import searchengine.library.dtos.IDocumentDto;

import java.sql.SQLException;
import java.util.List;

public interface IDocumentData {
    void createDocument (IDocumentDto document) throws Exception;
    List<Integer> searchByTokensContent(String queryExpression) throws Exception;
}
