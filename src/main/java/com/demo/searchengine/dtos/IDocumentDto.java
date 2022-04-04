package com.demo.searchengine.dtos;

import java.util.ArrayList;
import java.util.List;

public interface IDocumentDto {
    int Id = 0;
    List<ITokenDto> Tokens = new ArrayList<>();
}
