package searchengine.library.repositories;

import searchengine.library.dtos.IDocumentDto;
import searchengine.library.entities.Document;

public interface IDocumentRepository {
    Document getDocument(int id) throws Exception;
    void insertDocument(IDocumentDto document) throws Exception;
    void deleteDocument(IDocumentDto document) throws Exception;
    void insertTokensForDocument(IDocumentDto document) throws Exception;
}
