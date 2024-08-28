import java.util.List;
import java.util.SortedSet;

public class Regular<T extends Comparable<T>> extends User<T> implements RequestsManager {
    public ExprerienceStrategy exprerienceStrategy;
    public Regular(Information info, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorites) {
        super(info, accountType, username, experience, notifications, favorites);
    }

    public void setExprerienceStrategy(ExprerienceStrategy exprerienceStrategy) {
        this.exprerienceStrategy = exprerienceStrategy;
    }

    @Override
    public void addFavorite(T favorite) {
        favorites.add(favorite);
    }

    @Override
    public void removeFavorite(T favorite) {
        favorites.remove(favorite);
    }

    @Override
    public void updateExperience() {
        if (exprerienceStrategy != null) {
            int exp = exprerienceStrategy.calculateExprerience();
            experience += exp;
        }
    }
    @Override
    public void logOut() {
        System.out.println("Logging out...");
    }

    public void addRating(T p, Rating r) {
        if (p instanceof Production) {
            ((Production) p).Ratings.add(r);
            ((Production) p).TotalRating = ((Production) p).CalculateRating();
        }
    }
    public void removeRating(T p, Rating r) {
        if (p instanceof Production) {
            ((Production) p).Ratings.remove(r);
            ((Production) p).TotalRating = ((Production) p).CalculateRating();
        }
    }
    public void createRequest(Request r) {
        RequestsHolder.addRequest(r);
    }
    public void removeRequest(Request r) {
        RequestsHolder.removeRequest(r);
    }
    public double getExperience() {
        return experience;
    }
}
