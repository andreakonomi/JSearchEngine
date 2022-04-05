package searchengine.library.repositories;

import searchengine.library.dtos.IDocumentDto;
import searchengine.library.entities.Document;

public interface IDocumentRepository {
    Document GetDocument(int id) throws Exception;
    void InsertDocument(IDocumentDto document) throws Exception;
    void DeleteDocument(IDocumentDto document) throws Exception;
}
