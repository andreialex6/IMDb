import java.util.List;
import java.util.SortedSet;

public class Admin<T extends Comparable<T>> extends Staff<T> {
    public Admin(Information info, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorites, SortedSet<T> prod, IMDB imdb) {
        super(info, accountType, username, experience, notifications, favorites, prod, imdb);
    }

    public void removeUser(String u) {
        for (Regular r : imdb.regular) {
            if (r.username.equals(u)) {
                imdb.regular.remove(r);
                return;
            }
        }
        for (Contributor c : imdb.contributor) {
            if (c.username.equals(u)) {
                this.adminProd.addAll(c.prod);
                imdb.contributor.remove(c);
                return;
            }
        }
        for (Admin a : imdb.admin) {
            if (a.username.equals(u)) {
                imdb.admin.remove(a);
                return;
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
