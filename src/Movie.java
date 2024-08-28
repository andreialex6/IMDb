import java.util.List;
public class Movie extends Production {
    Integer Duration;
    Integer Year_of_Release;
    public Movie(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String description, Integer duration, Integer year_of_release) {
        super(title, directors, actors, genres, ratings, description);
        this.Duration = duration;
        this.Year_of_Release = year_of_release;
    }
    public void displayInfo() {
        if (this.title != null) {
            System.out.println("Title: " + this.title);
        }
        if (this.Year_of_Release != null) {
            System.out.println("Year of Release: " + this.Year_of_Release);
        }
        if (this.TotalRating != null) {
            System.out.println("Rating: " + this.TotalRating);
        }
        if (this.Genres != null) {
            System.out.println("Genre: " + this.Genres);
        }
        if (this.Duration != null) {
            System.out.println("Duration: " + this.Duration);
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
    }
    public String getTitle() {
        return this.title;
    }
    public Integer getYear_of_Release() {
        return this.Year_of_Release;
    }
    public Double getTotalRating() {
        return this.TotalRating;
    }
    public List<Genre> getGenres() {
        return this.Genres;
    }
    public Integer getDuration() {
        return this.Duration;
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
}
