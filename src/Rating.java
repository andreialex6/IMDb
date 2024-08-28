public class Rating {
    public String Username;
    int rating;
    String comments;
    public Rating(String username, int rating, String comments) {
        this.Username = username;
        this.rating = rating;
        this.comments = comments;
    }
    public String getUsername() {
        return this.Username;
    }
    public int getRating() {
        return this.rating;
    }
    public String getComments() {
        return this.comments;
    }
}
