import java.util.ArrayList;
import java.util.List;
public abstract class Production implements Comparable<Production> {
    String title;
    List<String> Directors;
    List<String> Actors;
    List<Genre> Genres;
    List<Rating> Ratings;
    String Description;
    Double TotalRating;
    public Production(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String description) {
        this.title = title;
        this.Directors = directors != null ? new ArrayList<>(directors) : new ArrayList<>();
        this.Actors = actors != null ? new ArrayList<>(actors) : new ArrayList<>();
        this.Genres = genres != null ? new ArrayList<>(genres) : new ArrayList<>();
        this.Ratings = ratings != null ? new ArrayList<>(ratings) : new ArrayList<>();
        this.Description = description;
        this.TotalRating = CalculateRating();
    }
    public double CalculateRating() {
        if (Ratings.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Rating i : Ratings) {
            sum += i.getRating();
        }
        return sum / Ratings.size();
    }
    public abstract void displayInfo();

    public int compareTo(Production p) {
        return this.title.compareTo(p.title);
    }
    public String getTitles() {
        return this.title;
    }
    public List<String> getDirectors() {
        return this.Directors;
    }
    public List<String> getActors() {
        return this.Actors;
    }
    public List<Genre> getGenres() {
        return this.Genres;
    }
    public String getDescription() {
        return this.Description;
    }
    public double getRating() {
        return this.TotalRating;
    }
}
