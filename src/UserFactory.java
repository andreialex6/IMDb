import java.util.List;
import java.util.SortedSet;

public class UserFactory<T extends Comparable<T>> {
    public User createUser(User.Information information, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorites, SortedSet<T> prod, IMDB imdb) {
        switch (accountType) {
            case REGULAR:
                return new Regular<T>(information, accountType, username, experience, notifications, favorites);
            case CONTRIBUTOR:
                return new Contributor<T>(information, accountType, username, experience, notifications, favorites, prod, imdb);
            case ADMIN:
                return new Admin<>(information, accountType, username, experience, notifications, prod, favorites, imdb);
            default:
                throw new IllegalArgumentException("Invalid account type");
        }
    }
}
