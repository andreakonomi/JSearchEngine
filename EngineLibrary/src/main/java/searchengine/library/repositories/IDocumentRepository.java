package searchengine.library.repositories;

import searchengine.library.dtos.IDocumentDto;
import searchengine.library.entities.Document;
import java.util.List;

public interface IDocumentRepository {
    Document getDocument(int id) throws Exception;
    void insertDocument(IDocumentDto document) throws Exception;
    void deleteDocument(IDocumentDto document) throws Exception;
    void insertTokensForDocument(IDocumentDto document) throws Exception;
    List<Integer> queryDocumentsForTokensContent(String query, List<String> parameters) throws Exception;
}
