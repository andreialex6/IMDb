import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Staff<T extends Comparable<T>> extends User<T> implements StaffInterface{

    public List<Request> r;
    public SortedSet<T> prod = new TreeSet<>();
    public ExprerienceStrategy exprerienceStrategy;
    public IMDB imdb;
    public SortedSet<T> adminProd;
    public Staff(Information info, AccountType accountType, String username,int experience, List<String> notifications, SortedSet<T> favorites, SortedSet<T> prod, IMDB imdb) {
        super(info, accountType, username, experience, notifications, favorites);
        this.r = new ArrayList<>();
        this.prod = prod;
        this.imdb = imdb;
        SortedSet<T> adminProd = new TreeSet<>();
    }

    @Override
    public void addProductionSystem(Production p) {
        if (p instanceof Movie) {
            imdb.addMovie((Movie) p);
            prod.add((T) p);
        } else if (p instanceof Series) {
            imdb.addSeries((Series) p);
            prod.add((T) p);
        }
    }

    @Override
    public void addActorSystem(Actor a) {
        imdb.addActor(a);
        prod.add((T) a);
    }

    @Override
    public void removeProductionSystem(String name) {
        for (Movie production : imdb.movie) {
            if (production.title.equals(name)) {
                imdb.removeMovie(production);
                for (T p : prod) {
                    if (p instanceof Movie) {
                        if (((Movie) p).title.equals(name)) {
                            prod.remove(p);
                            return;
                        }
                    }
                }
                return;
            }
        }
        for (Series production : imdb.series) {
            if (production.title.equals(name)) {
                imdb.removeSeries(production);
                for (T p : prod) {
                    if (p instanceof Series) {
                        if (((Series) p).title.equals(name)) {
                            prod.remove(p);
                            return;
                        }
                    }
                }
                return;
            }
        }
    }

    @Override
    public void removeActorSystem(String name) {
        for (Actor actor : imdb.actor) {
            if (actor.name.equals(name)) {
                imdb.removeActor(actor);
                for (T a : prod) {
                    if (a instanceof Actor) {
                        if (((Actor) a).name.equals(name)) {
                            prod.remove(a);
                            return;
                        }
                    }
                }
                return;
            }
        }
    }

    @Override
    public void updateProduction(Production p, String description) {
        for (T aux : prod) {
            if (aux instanceof Movie) {
                if (((Movie) aux).title.equals(p.title)) {
                    p.Description = description;
                    return;
                }
            } else if (aux instanceof Series) {
                if (((Series) aux).title.equals(p.title)) {
                    p.Description = description;
                    return;
                }
            }
        }
    }

    @Override
    public void updateActor(Actor a, String biography) {
        for (T aux : prod) {
            if (aux instanceof Actor) {
                if (((Actor) aux).name.equals(a.name)) {
                    a.biography = biography;
                    return;
                }
            }
        }
    }

    public void setExprerienceStrategy(ExprerienceStrategy exprerienceStrategy) {
        this.exprerienceStrategy = exprerienceStrategy;
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

}