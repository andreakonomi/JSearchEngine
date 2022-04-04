package searchengine.library.dtos;

public class TokenDto implements ITokenDto{
    private String Content = "";

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
