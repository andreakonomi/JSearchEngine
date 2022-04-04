package com.demo.searchengine.dataAccess;

import com.demo.searchengine.dtos.IDocumentDto;

import java.util.List;

public class DocumentData implements IDocumentData{
    @Override
    public void createDocument(IDocumentDto document) {

    }

    @Override
    public List<int> searchByTokensContent(String queryExpression) {
        return null;
    }
}
