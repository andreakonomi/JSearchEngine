package searchengine.library.dataAccess;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import searchengine.library.dtos.DocumentDto;
import searchengine.library.dtos.IDocumentDto;
import searchengine.library.dtos.ITokenDto;
import searchengine.library.dtos.TokenDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocumentDataTest {

    private static IDocumentData docData;

    @BeforeAll
    static void init(){
        docData = new DocumentData("jdbc:sqlite:TestDb\\SearchEng.db");
    }

    @Test
    void insertAndQueryDocumentShould(){
        // Arrange
        List<Integer> idsFound = new ArrayList<>();
        int id = 103;
        String tokenContent = String.valueOf(id);

        IDocumentDto newDoc = CreateDocumentDto(id, tokenContent);

        //Act
        try{
            docData.createDocument(newDoc);
            idsFound = docData.searchByTokensContent(tokenContent);
        }
        catch(Exception ex){
            idsFound.add(0);
        }

        //Assert
        assertEquals(id, idsFound.get(0));

    }

    @Test
    void NonAlphaNumericTokenShould(){
        //Arrange
        IDocumentDto newDoc = CreateDocumentDto(1, "-???token");

        //Act

        //Assert
        assertThrows(RuntimeException.class, () ->
                docData.createDocument(newDoc));
    }

    private IDocumentDto CreateDocumentDto(int id, String content){
        IDocumentDto newDoc = new DocumentDto();
        newDoc.setId(id);

        List<ITokenDto> tokens = new ArrayList<>();
        ITokenDto newToken = new TokenDto();
        newToken.setContent(content);
        tokens.add(newToken);
        newDoc.setTokens(tokens);

        return newDoc;
    }

}