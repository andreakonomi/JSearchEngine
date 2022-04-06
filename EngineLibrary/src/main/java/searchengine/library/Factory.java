package searchengine.library;

import searchengine.library.repositories.DocumentRepository;
import searchengine.library.repositories.IDocumentRepository;

public class Factory {

    public static IDocumentRepository getDocumentRepository(String connectionUrl){
        return new DocumentRepository(connectionUrl);
    }
}
