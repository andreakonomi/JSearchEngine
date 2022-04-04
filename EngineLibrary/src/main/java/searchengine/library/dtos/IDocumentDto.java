package searchengine.library.dtos;

import java.util.List;

public interface IDocumentDto {
    int getId();
    void setId(int id);
    List<ITokenDto> getTokens();
    void setTokens(List<ITokenDto> tokens);
}
