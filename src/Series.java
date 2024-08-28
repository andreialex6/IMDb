import java.util.List;
import java.util.Map;
public class Series extends Production{
    Integer year;
    Integer numberOfSeasons;
    Map<String, List<Episode> > episodes;
    public Series(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String description, Integer year, Integer numberOfSeasons, Map<String, List<Episode> > episodes) {
        super(title, directors, actors, genres, ratings, description);
        this.year = year;
        this.numberOfSeasons = numberOfSeasons;
        this.episodes = episodes;
    }
    public void displayInfo() {
        if (this.title != null) {
            System.out.println("Title: " + this.title);
        }
        if (this.year != null) {
            System.out.println("Year of Release: " + this.year);
        }
        if (this.TotalRating != null) {
            System.out.println("Rating: " + this.TotalRating);
        }
        if (this.Genres != null) {
            System.out.println("Genre: " + this.Genres);
        }
        if (this.Actors != null) {
            System.out.println("Actors: " + this.Actors);
        }
        if (this.Directors != null) {
            System.out.println("Directors: " + this.Directors);
        }
        if (this.Description != null) {
            System.out.println("Description: " + this.Description);
        }
        if (this.numberOfSeasons != null) {
            System.out.println("Number of Seasons: " + this.numberOfSeasons);
        }
        if (episodes != null) {
            for (Map.Entry<String, List<Episode> > entry : episodes.entrySet()) {
                if (entry.getKey() != null) {
                    System.out.println(entry.getKey() + ":");
                    for (Episode i : entry.getValue()) {
                        i.displayInfo();
                    }
                }
            }
        }
    }
    public String getTitle() {
        return this.title;
    }
    public Integer getYear() {
        return this.year;
    }
    public Double getTotalRating() {
        return this.TotalRating;
    }
    public List<Genre> getGenres() {
        return this.Genres;
    }
    public List<String> getActors() {
        return this.Actors;
    }
    public List<String> getDirectors() {
        return this.Directors;
    }
    public String getDescription() {
        return this.Description;
    }
    public Integer getNumberOfSeasons() {
        return this.numberOfSeasons;
    }
    public Map<String, List<Episode> > getEpisodes() {
        return this.episodes;
    }
}
