package com.demo.searchengine.dataAccess;

import com.demo.searchengine.dtos.IDocumentDto;

import java.util.List;

public interface IDocumentData {
    void createDocument (IDocumentDto document);
    List<int> searchByTokensContent(String queryExpression);
}
