package com.demo.searchengine.console;

import searchengine.library.dataAccess.DocumentData;
import searchengine.library.dataAccess.IDocumentData;
import searchengine.library.dtos.DocumentDto;
import searchengine.library.dtos.IDocumentDto;
import searchengine.library.dtos.ITokenDto;
import searchengine.library.dtos.TokenDto;

import java.util.ArrayList;
import java.util.List;

public class Factory {

    public static IDocumentData createDocumentData(String connectionUrl){
        return new DocumentData(connectionUrl);
    }

    public static IDocumentDto createDocumentDto(){
        return new DocumentDto();
    }

    public static ITokenDto createToken(String content){
        TokenDto token = new TokenDto();
        token.setContent(content);

        return  token;
    }

    public  static List<ITokenDto> createTokensList(){
        return new ArrayList<>();
    }
}
