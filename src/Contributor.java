import java.util.List;
import java.util.Objects;
import java.util.SortedSet;

public class Contributor<T extends Comparable<T>> extends Staff<T> implements RequestsManager{
    IMDB imdb;
    public Contributor(Information info, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorites, SortedSet<T> prod, IMDB imdb) {
        super(info, accountType, username, experience, notifications, favorites, prod, imdb);
    }

    @Override
    public void createRequest(Request r) {
        if (Objects.equals(r.username_admin, "ADMIN")) {
            RequestsHolder.addRequest(r);
        } else {
            for (Contributor c : imdb.contributor) {
                if (c.username.equals(r.username_admin)) {
                    c.r.add(r);
                    return;
                }
            }
            for (Admin a : imdb.admin) {
                if (a.username.equals(r.username_admin)) {
                    a.r.add(r);
                    return;
                }
            }
        }
    }

    @Override
    public void removeRequest(Request r) {
        if (Objects.equals(r.username_admin, "ADMIN")) {
            RequestsHolder.removeRequest(r);
        } else {
            for (Contributor c : imdb.contributor) {
                if (c.username.equals(r.username_admin)) {
                    c.r.remove(r);
                    return;
                }
            }
            for (Admin a : imdb.admin) {
                if (a.username.equals(r.username_admin)) {
                    a.r.remove(r);
                    return;
                }
            }
        }
    }

    @Override
    public void addFavorite(T favorite) {
        favorites.add(favorite);
    }

    @Override
    public void removeFavorite(T favorite) {
        favorites.remove(favorite);
    }
}
