package searchengine.library.dtos;

import java.util.ArrayList;
import java.util.List;

public class DocumentDto implements IDocumentDto{
    private int Id;
    private List<ITokenDto> Tokens = new ArrayList<>();

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public List<ITokenDto> getTokens() {
        return Tokens;
    }

    public void setTokens(List<ITokenDto> tokens) {
        Tokens = tokens;
    }
}
