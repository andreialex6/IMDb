import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Actor {
    String name;
    Map<String, String> roles = new HashMap<>();
    String biography;
    List <Rating> Ratings;
    Double TotalRating;
    public Actor(String name, Map<String, String> roles, String biography) {
        this.name = name;
        this.roles = roles;
        this.biography = biography;
        this.Ratings = new ArrayList<>();
        TotalRating = CalculateRating();
    }
    public String getActorName() {
        return this.name;
    }
    public Map<String, String> getRoles() {
        return this.roles;
    }
    public String getBiography() {
        return this.biography;
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
    public String getName() {
        return this.name;
    }
    public double getTotalRating() {
        return this.TotalRating;
    }
}
