package searchengine.library.dataAccess;

import searchengine.library.dtos.IDocumentDto;

import java.util.List;

public interface IDocumentData {
    void createDocument (IDocumentDto document);
    List<Integer> searchByTokensContent(String queryExpression);
}
