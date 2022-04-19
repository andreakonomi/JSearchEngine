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

    /*
    Test the case of inserting a new document and querying its content
    The method should return the id just inserted.
     */
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

    /*
    If you add a document with non-alphanumeric content, method should
    judge it as IllegalArgumentException argument and throw exception. Tests also the
    message returned by the exception.
    */
    @Test
    void NonAlphaNumericTokenShould(){
        //Arrange
        IDocumentDto newDoc = CreateDocumentDto(1, "-???token");

        //Act

        //Assert
        Throwable error = assertThrows(IllegalArgumentException.class, () ->
                docData.createDocument(newDoc));

        assertEquals("The content provided is invalid, all tokens need to be alphanumerical!",
                error.getMessage());
    }



    /*
    Tests the case of inserting a document with an already existing id. The old content
    linked with that document id should be deleted and the new content should be placed upon
    it.
     */
    @Test
    void insertAlreadyExistingIdShould(){
        // Arrange
        List<Integer> idsFound;
        Integer idOfOldTokensSearch = 0;
        Integer idOfTokenWithFinalToken = 0;

        String tokenToBeErased = "firstToken";
        String finalToken = "secondToken";

        IDocumentDto firstDoc = CreateDocumentDto(4, tokenToBeErased);
        IDocumentDto secondDoc = CreateDocumentDto(4, finalToken);

        // Act
        try{
            docData.createDocument(firstDoc);
            docData.createDocument(secondDoc);

            idsFound = docData.searchByTokensContent(tokenToBeErased);
            if (idsFound.size() != 0){
                idOfOldTokensSearch = idsFound.get(0);
            }

            idsFound = docData.searchByTokensContent(finalToken);
            if (idsFound.size() != 0){
                idOfTokenWithFinalToken = idsFound.get(0);
            }
        }
        catch(Exception ex){
        }

        // Assert
        assertNotEquals(idOfOldTokensSearch, 4);

        assertEquals(idOfTokenWithFinalToken, 4);

    }

    /*
    Sets up an IDocumentDto with the required arguments as requested.
     */
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