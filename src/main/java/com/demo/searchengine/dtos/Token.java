package com.demo.searchengine.dtos;

public class Token implements ITokenDto{
    private String Content = "";

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
