import java.time.LocalDateTime;

public class Request {
    RequestTypes requestType;
    LocalDateTime data;
    String name;
    String description;
    String username_requester;
    String username_admin;
    IMDB imdb;
    public Request(RequestTypes requestType, String name, String description, String username_requester, String username_admin, IMDB imdb) {
        this.requestType = requestType;
        this.data = LocalDateTime.now();
        this.name = name;
        this.description = description;
        this.username_requester = username_requester;
        this.username_admin = username_admin;
        this.imdb = imdb;
    }
    public String findAdmin(RequestTypes requestType) {
        if (requestType == RequestTypes.DELETE_ACCOUNT || requestType == RequestTypes.OTHERS) {
            RequestsHolder.addRequest(this);
            return "ADMIN";
        }
        return null;
    }
}
