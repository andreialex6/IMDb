import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class IMDB {
    List<Regular> regular;
    List <Contributor> contributor;
    List <Admin> admin;
    List <Request> request;
    List <Actor> actor;
    List <Movie> movie;
    List <Series> series;
    private static IMDB instance;

    public IMDB () {
        this.regular = new ArrayList<>();
        this.contributor = new ArrayList<>();
        this.admin = new ArrayList<>();
        this.actor = new ArrayList<>();
        this.request = new ArrayList<>();
        this.movie = new ArrayList<>();
        this.series = new ArrayList<>();
    }

    public void addRegular(Regular r) {
        regular.add(r);
    }
    public void removeRegular(Regular r) {
        regular.remove(r);
    }
    public void addContributor(Contributor c) {
        contributor.add(c);
    }
    public void removeContributor(Contributor c) {
        contributor.remove(c);
    }
    public void addAdmin(Admin a) {
        admin.add(a);
    }
    public void removeAdmin(Admin a) {
        admin.remove(a);
    }
    public void addActor(Actor a) {
        actor.add(a);
    }
    public void removeActor(Actor a) {
        actor.remove(a);
    }
    public void addRequest(Request r) {
        request.add(r);
    }
    public void removeRequest(Request r) {
        request.remove(r);
    }
    public void addMovie(Movie m) {
        movie.add(m);
    }
    public void removeMovie(Movie m) {
        movie.remove(m);
    }
    public void addSeries(Series s) {
        series.add(s);
    }
    public void removeSeries(Series s) {
        series.remove(s);
    }

    public static IMDB getInstance() {
        if (instance == null) {
            synchronized (IMDB.class) {
                if (instance == null) {
                    instance = new IMDB();
                }
            }
        }
        return instance;
    }
    public void loadJSONProductions(String filepath) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(filepath));
            JSONArray jsonArray = (JSONArray) obj;
            for (Object o :jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String type = (String) jsonObject.get("type");
                if ("Movie".equals(type)) {
                    Movie movies = createMovie(jsonObject);
                    movie.add(movies);
                } else if ("Series".equals(type)) {
                    Series serie = createSeries(jsonObject);
                    series.add(serie);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Movie createMovie(JSONObject jsonObject) {
        String title = (String) jsonObject.get("title");
        JSONArray directorsJson = (JSONArray) jsonObject.get("directors");
        List<String> directors = new ArrayList<>();
        for (Object director : directorsJson) {
            directors.add((String) director);
        }

        JSONArray actorsJson = (JSONArray) jsonObject.get("actors");
        List<String> actors = new ArrayList<>();
        for (Object actor : actorsJson) {
            actors.add((String) actor);
        }

        JSONArray genresJson = (JSONArray) jsonObject.get("genres");
        List<Genre> genres = new ArrayList<>();
        for (Object genre : genresJson) {
            String genreName = (String) genre;
            try {
                Genre genreEnum = Genre.valueOf(genreName);
                genres.add(genreEnum);
            } catch (IllegalArgumentException e) {
                System.out.println("Genre not found for: " + genreName);
            }
        }

        JSONArray ratingsJson = (JSONArray) jsonObject.get("ratings");
        List<Rating> ratings = new ArrayList<>();
        for (Object ratingObj : ratingsJson) {
            JSONObject ratingJson = (JSONObject) ratingObj;
            String username = (String) ratingJson.get("username");
            int rating = ((Long) ratingJson.get("rating")).intValue();
            String comment = (String) ratingJson.get("comment");
            ratings.add(new Rating(username, rating, comment));
        }

        String plot = (String) jsonObject.get("plot");
        double averageRating = (double) jsonObject.get("averageRating");
        String durationString = (String) jsonObject.get("duration");
        String[] parts = durationString.split("\\s+");
        Integer duration = Integer.parseInt(parts[0]);
        Object releaseYearObj = jsonObject.get("releaseYear");
        int releaseYear = 0;
        if (releaseYearObj != null) {
            releaseYear = ((Long) releaseYearObj).intValue();
        }
        return new Movie(title, directors, actors, genres, ratings, plot, duration, releaseYear);
    }

    public Series createSeries(JSONObject jsonObject) {
        String title = (String) jsonObject.get("title");
        JSONArray directorsJson = (JSONArray) jsonObject.get("directors");
        List<String> directors = new ArrayList<>();
        for (Object director : directorsJson) {
            directors.add((String) director);
        }

        JSONArray actorsJson = (JSONArray) jsonObject.get("actors");
        List<String> actors = new ArrayList<>();
        for (Object actor : actorsJson) {
            actors.add((String) actor);
        }

        JSONArray genresJson = (JSONArray) jsonObject.get("genres");
        List<Genre> genres = new ArrayList<>();
        for (Object genre : genresJson) {
            String genreName = (String) genre;
            try {
                Genre genreEnum = Genre.valueOf(genreName);
                genres.add(genreEnum);
            } catch (IllegalArgumentException e) {
                System.out.println("Genre not found for: " + genreName);
            }
        }

        JSONArray ratingsJson = (JSONArray) jsonObject.get("ratings");
        List<Rating> ratings = new ArrayList<>();
        for (Object ratingObj : ratingsJson) {
            JSONObject ratingJson = (JSONObject) ratingObj;
            String username = (String) ratingJson.get("username");
            int rating = ((Long) ratingJson.get("rating")).intValue();
            String comment = (String) ratingJson.get("comment");
            ratings.add(new Rating(username, rating, comment));
        }

        String description = (String) jsonObject.get("plot");
        double averageRating = (double) jsonObject.get("averageRating");
        int releaseYear = ((Long) jsonObject.get("releaseYear")).intValue();
        int numSeasons = ((Long) jsonObject.get("numSeasons")).intValue();

        JSONObject seasonsJson = (JSONObject) jsonObject.get("seasons");

        Map<String, List<Episode>> episodes = new LinkedHashMap<>();
        for (Iterator iterator = seasonsJson.keySet().iterator(); iterator.hasNext();) {
            String seasonName = (String) iterator.next();
            JSONArray episodesJson = (JSONArray) seasonsJson.get(seasonName);
            List<Episode> seasonEpisodes = new ArrayList<>();
            for (Object episodeObj : episodesJson) {
                JSONObject episodeJson = (JSONObject) episodeObj;
                String episodeName = (String) episodeJson.get("episodeName");
                //int duration = (int) episodeJson.get("duration");
                String durationString = (String) episodeJson.get("duration");
                if (durationString != null) {
                    String[] parts = durationString.split("\\s+");
                    Integer duration = Integer.parseInt(parts[0]);
                    seasonEpisodes.add(new Episode(episodeName, duration));
                } else {
                    seasonEpisodes.add(new Episode(episodeName, 0));
                }
            }
            episodes.put(seasonName, seasonEpisodes);
        }

        return new Series(title, directors, actors, genres, ratings, description, releaseYear, numSeasons, episodes);
    }


    public void loadJSONActors(String filepath) {
        JSONParser parser2 = new JSONParser();
        try {
            Object obj = parser2.parse(new FileReader(filepath));
            JSONArray jsonArray = (JSONArray) obj;
            for (Object o :jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                Actor actors = createActor(jsonObject);
                actor.add(actors);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Actor createActor(JSONObject jsonObject) {
        String name = (String) jsonObject.get("name");
        JSONArray performancesJson = (JSONArray) jsonObject.get("performances");
        String biography = (String) jsonObject.get("biography");

        Map<String, String> roles = new HashMap<>();
        for (Object performance : performancesJson) {
            JSONObject performanceObj = (JSONObject) performance;
            String title = (String) performanceObj.get("title");
            String type = (String) performanceObj.get("type");
            roles.put(title, type);
        }

        return new Actor(name, roles, biography);
    }

    public void loadJSONRequest(String filepath, IMDB imdb) {
        JSONParser parser3 = new JSONParser();
        try {
            Object obj = parser3.parse(new FileReader(filepath));
            JSONArray jsonArray = (JSONArray) obj;
            for (Object o :jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                Request requests = createRequest(jsonObject, imdb);
                request.add(requests);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Request createRequest(JSONObject jsonObject, IMDB imdb) {
        //RequestTypes requestTypes = (RequestTypes) jsonObject.get("requestType");
        String req = (String) jsonObject.get("type");
        RequestTypes requestTypes;
        if (req.equals("DELETE_ACCOUNT")) {
            requestTypes = RequestTypes.DELETE_ACCOUNT;
        } else if (req.equals("OTHERS")) {
            requestTypes = RequestTypes.OTHERS;
        } else if (req.equals("MOVIE_ISSUE")) {
            requestTypes = RequestTypes.MOVIE_ISSUE;
        } else {
            requestTypes = RequestTypes.ACTOR_ISSUE;
        }
        String dateString = (String) jsonObject.get("createdDate");
        LocalDateTime data = LocalDateTime.parse(dateString);
        //LocalDateTime data = (LocalDateTime) jsonObject.get("createdDate");
        String username = (String) jsonObject.get("username");
        String username_admin = (String) jsonObject.get("to");
        String description = (String) jsonObject.get("description");

        if (requestTypes == RequestTypes.MOVIE_ISSUE) {
            String name = (String) jsonObject.get("movieTitle");
            return new Request(requestTypes, name, description, username, username_admin, imdb);
        } else if (requestTypes == RequestTypes.ACTOR_ISSUE) {
            String name = (String) jsonObject.get("actorName");
            return new Request(requestTypes, name, description, username, username_admin, imdb);
        } else {
            return new Request(requestTypes, null, description, username, username_admin, imdb);
        }
    }
    public void loadJSONUsers(String filepath, IMDB imdb) {
        JSONParser parser3 = new JSONParser();
        try {
            Object obj = parser3.parse(new FileReader(filepath));
            JSONArray jsonArray = (JSONArray) obj;
            for (Object o :jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String userType = (String) jsonObject.get("userType");
                if ("Regular".equals(userType)) {
                    Regular regulars = createRegular(jsonObject);
                    regular.add(regulars);
                } else if ("Contributor".equals(userType)) {
                    Contributor contributors = createContributor(jsonObject, imdb);
                    contributor.add(contributors);
                } else {
                    Admin admins = createAdmin(jsonObject, imdb);
                    admin.add(admins);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Regular createRegular(JSONObject jsonObject) {
        String username = (String) jsonObject.get("username");
        int experience = Integer.parseInt((String) jsonObject.get("experience"));

        JSONObject information = (JSONObject) jsonObject.get("information");
        String name = (String) information.get("name");
        String country = (String) information.get("country");
        int age = Integer.parseInt(String.valueOf(information.get("age")));
        String dateString = (String) information.get("birthDate");
        LocalDate birthDate = LocalDate.parse(dateString);
        String gender = (String) information.get("gender");

        String userType = (String) jsonObject.get("userType");

        JSONObject credentials = (JSONObject) information.get("credentials");
        String email = (String) credentials.get("email");
        String password = (String) credentials.get("password");

        Credentials cred = new Credentials(email, password);

        JSONArray notificationsJson = (JSONArray) jsonObject.get("notifications");
        List<String> notifications = new ArrayList<>();
        if (notificationsJson != null) {
            for (Object notification : notificationsJson) {
                notifications.add((String) notification);
            }
        }

        JSONArray favoriteProductionsJson = (JSONArray) jsonObject.get("favoriteProductions");
        SortedSet<String> favoriteProductions = new TreeSet<>();
        if (favoriteProductionsJson != null) {
            for (Object production : favoriteProductionsJson) {
                favoriteProductions.add((String) production);
            }
        }

        JSONArray favoriteActorsJson = (JSONArray) jsonObject.get("favoriteActors");
        if (favoriteActorsJson != null) {
            for (Object actor : favoriteActorsJson) {
                favoriteProductions.add((String) actor);
            }
        }

        User.Information.InformationBuilder informationBuilder = new User.Information.InformationBuilder();
        User.Information informations = informationBuilder
                .setCredentials(cred)
                .setName(name)
                .setCountry(country)
                .setAge(age)
                .setGender(gender)
                .setBirthdate(birthDate)
                .build();

        UserFactory<String> userFactory = new UserFactory<>();
        User regularUser = userFactory.createUser(
                informations,
                AccountType.REGULAR,
                username,
                experience,
                notifications,
                favoriteProductions,
                null,
                null
        );
        return (Regular) regularUser;
    }
    private Contributor createContributor(JSONObject jsonObject, IMDB imdb) {
        String username = (String) jsonObject.get("username");
        int experience = Integer.parseInt((String) jsonObject.get("experience"));

        JSONObject information = (JSONObject) jsonObject.get("information");
        String name = (String) information.get("name");
        String country = (String) information.get("country");
        int age = Integer.parseInt(String.valueOf(information.get("age")));
        String dateString = (String) information.get("birthDate");
        LocalDate birthDate = LocalDate.parse(dateString);
        String gender = (String) information.get("gender");

        String userType = (String) jsonObject.get("userType");

        JSONObject credentials = (JSONObject) information.get("credentials");
        String email = (String) credentials.get("email");
        String password = (String) credentials.get("password");

        Credentials cred = new Credentials(email, password);

        JSONArray notificationsJson = (JSONArray) jsonObject.get("notifications");
        List<String> notifications = new ArrayList<>();
        if (notificationsJson != null) {
            for (Object notification : notificationsJson) {
                notifications.add((String) notification);
            }
        }

        JSONArray ProductionContributionsJson = (JSONArray) jsonObject.get("productionsContribution");
        SortedSet<String> productionsContribution = new TreeSet<>();
        if (ProductionContributionsJson != null) {
            for (Object production : ProductionContributionsJson) {
                productionsContribution.add((String) production);
            }
        }

        JSONArray ActorContributionsJson2 = (JSONArray) jsonObject.get("actorsContribution");
        if (ActorContributionsJson2 != null) {
            for (Object actor : ActorContributionsJson2) {
                productionsContribution.add((String) actor);
            }
        }

        JSONArray favoriteProductionsJson = (JSONArray) jsonObject.get("favoriteProductions");
        SortedSet<String> favoriteProductions = new TreeSet<>();
        if (favoriteProductionsJson != null) {
            for (Object production : favoriteProductionsJson) {
                favoriteProductions.add((String) production);
            }
        }

        JSONArray favoriteActorsJson = (JSONArray) jsonObject.get("favoriteActors");
        if (favoriteActorsJson != null) {
            for (Object actor : favoriteActorsJson) {
                favoriteProductions.add((String) actor);
            }
        }

        User.Information.InformationBuilder informationBuilder = new User.Information.InformationBuilder();
        User.Information informations = informationBuilder
                .setCredentials(cred)
                .setName(name)
                .setCountry(country)
                .setAge(age)
                .setGender(gender)
                .setBirthdate(birthDate)
                .build();

        UserFactory<String> userFactory = new UserFactory<>();
        User ContributorUser = userFactory.createUser(
                informations,
                AccountType.CONTRIBUTOR,
                username,
                experience,
                notifications,
                favoriteProductions,
                productionsContribution,
                imdb
        );
        return (Contributor) ContributorUser;
    }

    private Admin createAdmin(JSONObject jsonObject, IMDB imdb) {
        String username = (String) jsonObject.get("username");
        //int experience = Integer.parseInt((String) jsonObject.get("experience"));
        Object experienceObj = jsonObject.get("experience");
        int experience = 0;
        if (experienceObj != null) {
            String experienceStr = (String) experienceObj;
            if (!experienceStr.isEmpty()) {
                experience = Integer.parseInt(experienceStr);
            }
        }

        JSONObject information = (JSONObject) jsonObject.get("information");
        String name = (String) information.get("name");
        String country = (String) information.get("country");
        int age = Integer.parseInt(String.valueOf(information.get("age")));
        String dateString = (String) information.get("birthDate");
        LocalDate birthDate = LocalDate.parse(dateString);
        String gender = (String) information.get("gender");

        String userType = (String) jsonObject.get("userType");

        JSONObject credentials = (JSONObject) information.get("credentials");
        String email = (String) credentials.get("email");
        String password = (String) credentials.get("password");

        Credentials cred = new Credentials(email, password);

        JSONArray notificationsJson = (JSONArray) jsonObject.get("notifications");
        List<String> notifications = new ArrayList<>();
        if (notificationsJson != null) {
            for (Object notification : notificationsJson) {
                notifications.add((String) notification);
            }
        }

        JSONArray ProductionContributionsJson = (JSONArray) jsonObject.get("productionsContribution");
        SortedSet<String> productionsContribution = new TreeSet<>();
        if (ProductionContributionsJson != null) {
            for (Object production : ProductionContributionsJson) {
                productionsContribution.add((String) production);
            }
        }

        JSONArray ActorContributionsJson2 = (JSONArray) jsonObject.get("actorsContribution");
        if (ActorContributionsJson2 != null) {
            for (Object actor : ActorContributionsJson2) {
                productionsContribution.add((String) actor);
            }
        }

        JSONArray favoriteProductionsJson = (JSONArray) jsonObject.get("favoriteProductions");
        SortedSet<String> favoriteProductions = new TreeSet<>();
        if (favoriteProductionsJson != null) {
            for (Object production : favoriteProductionsJson) {
                favoriteProductions.add((String) production);
            }
        }

        JSONArray favoriteActorsJson = (JSONArray) jsonObject.get("favoriteActors");
        if (favoriteActorsJson != null) {
            for (Object actor : favoriteActorsJson) {
                favoriteProductions.add((String) actor);
            }
        }

        User.Information.InformationBuilder informationBuilder = new User.Information.InformationBuilder();
        User.Information informations = informationBuilder
                .setCredentials(cred)
                .setName(name)
                .setCountry(country)
                .setAge(age)
                .setGender(gender)
                .setBirthdate(birthDate)
                .build();

        UserFactory<String> userFactory = new UserFactory<>();
        User AdminUser = userFactory.createUser(
                informations,
                AccountType.ADMIN,
                username,
                experience,
                notifications,
                favoriteProductions,
                productionsContribution,
                imdb
        );
        return (Admin) AdminUser;
    }

    public void run() {
        loadJSONActors("json/actors.json");
        loadJSONProductions("json/production.json");
        loadJSONRequest("json/requests.json", this);
        loadJSONUsers("json/accounts.json", this);
    }

    public static void main(String[] args) {

        IMDB imdb = IMDB.getInstance();

        imdb.run();

        Scanner scanner = new Scanner(System.in);

        boolean isAutentificated = false;

        Regular regUsr = null;
        Contributor contrUsr = null;
        Admin admUsr = null;

        for (Request r : imdb.request) {
            if (r.username_admin.equals("ADMIN")) {
                RequestsHolder.addRequest(r);
            }
            for (Contributor c : imdb.contributor) {
                if (c.username.equals(r.username_admin)) {
                    c.r.add(r);
                }
            }
            for (Admin a : imdb.admin) {
                if (a.username.equals(r.username_admin)) {
                    a.r.add(r);
                }
            }
        }

        System.out.println("Choose how you want to use the application");
        System.out.println("1) Graphic interface");
        System.out.println("2) Terminal");
        String userOption = scanner.nextLine();

        if (userOption.equals("1")) {
            SwingUtilities.invokeLater(() -> new GraphicInterface(imdb.regular, imdb.contributor, imdb.admin, imdb));
        } else if (userOption.equals("2")) {
            while (true) {
                if (!isAutentificated) {
                    System.out.println("Welcome back! Enter your credentials:");
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();

                    isAutentificated = Autentification.autentification(email, password, imdb.regular, imdb.contributor, imdb.admin);

                    if (!isAutentificated) {
                        System.out.println("Incorrect credentials! Please, try again!");
                    } else {
                        for (Regular r : imdb.regular) {
                            Credentials credentials = r.info.getCredentials();
                            if (credentials.getEmail().equals(email) && credentials.getPassword().equals(password)) {
                                regUsr = r;
                                break;
                            }
                        }
                        if (regUsr == null) {
                            for (Contributor c : imdb.contributor) {
                                Credentials credentials = c.info.getCredentials();
                                if (credentials.getEmail().equals(email) && credentials.getPassword().equals(password)) {
                                    contrUsr = c;
                                    break;
                                }
                            }
                            if (contrUsr == null) {
                                for (Admin a : imdb.admin) {
                                    Credentials credentials = a.info.getCredentials();
                                    if (credentials.getEmail().equals(email) && credentials.getPassword().equals(password)) {
                                        admUsr = a;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (regUsr != null) {
                        System.out.println("Welcome back!");
                        System.out.print("Username: ");
                        System.out.println(regUsr.username);
                        System.out.print("Experience: ");
                        System.out.println(regUsr.experience);
                        System.out.println("Account type: Regular");
                        System.out.println("1) View productions details");
                        System.out.println("2) View actors details");
                        System.out.println("3) View notifications");
                        System.out.println("4) Search for actor/movie/series");
                        System.out.println("5) View favorite productions/actors");
                        System.out.println("6) Add/Delete actor/movie/series to/from favorites");
                        System.out.println("7) Rate a production");
                        System.out.println("8) Rate an actor");
                        System.out.println("9) Create a request");
                        System.out.println("10) Delete a request");
                        System.out.println("11) Logout");

                        String option1 = scanner.nextLine();

                        if (option1.equals("1")) {
                            while (true) {
                                System.out.println("1) View movie details");
                                System.out.println("2) View series details");
                                System.out.println("3) Previous page");

                                String option2 = scanner.nextLine();

                                if (option2.equals("1")) {
                                    while (true) {
                                        System.out.println("Select movie");
                                        for (Movie m : imdb.movie) {
                                            System.out.println(imdb.movie.indexOf(m) + 1 + ") " + m.title);
                                        }
                                        String option3 = scanner.nextLine();
                                        int opt = Integer.parseInt(option3);
                                        if (opt > 0 && opt <= imdb.movie.size()) {
                                            imdb.movie.get(opt - 1).displayInfo();
                                            break;
                                        } else {
                                            System.out.println("Invalid movie! Please, try again");
                                            break;
                                        }
                                    }
                                } else if (option2.equals("2")) {
                                    while (true) {
                                        System.out.println("Select series");
                                        for (Series s : imdb.series) {
                                            System.out.println(imdb.series.indexOf(s) + 1 + ") " + s.title);
                                        }
                                        String option3 = scanner.nextLine();
                                        int opt = Integer.parseInt(option3);
                                        if (opt > 0 && opt <= imdb.series.size()) {
                                            imdb.series.get(opt - 1).displayInfo();
                                            break;
                                        } else {
                                            System.out.println("Invalid movie! Please, try again");
                                            break;
                                        }
                                    }
                                } else if (option2.equals("3")) {
                                    break;
                                } else {
                                    System.out.println("Invalid option! Try again");
                                }
                            }
                        } else if (option1.equals("2")) {
                            while (true) {
                                System.out.println("Select actor");
                                for (Actor a : imdb.actor) {
                                    System.out.println(imdb.actor.indexOf(a) + 1 + ") " + a.name);
                                }
                                String option4 = scanner.nextLine();
                                int opt = Integer.parseInt(option4);
                                if (opt > 0 && opt <= imdb.actor.size()) {
                                    System.out.println("Name: " + imdb.actor.get(opt - 1).name);
                                    if (imdb.actor.get(opt - 1).biography != null) {
                                        System.out.println("Biography: " + imdb.actor.get(opt - 1).biography);
                                    }
                                    System.out.println("Ratings: " + imdb.actor.get(opt - 1).TotalRating);
                                    if (imdb.actor.get(opt - 1).roles.keySet() != null) {
                                        System.out.println("Performances: ");
                                        for (String key : imdb.actor.get(opt - 1).roles.keySet()) {
                                            System.out.println(key + " - " + imdb.actor.get(opt - 1).roles.get(key));
                                        }
                                    }
                                    break;
                                } else {
                                    System.out.println("Invalid actor! Please, try again");
                                    break;
                                }
                            }
                        } else if (option1.equals("3")) {
                            System.out.println("Notifications: ");
                            for (Object n : regUsr.notifications) {
                                System.out.println(n);
                            }
                        } else if (option1.equals("4")) {
                            while (true) {
                                System.out.println("1) Search for production");
                                System.out.println("2) Search for actor");
                                System.out.println("3) Previous page");
                                String option2 = scanner.nextLine();
                                if (option2.equals("1")) {
                                    while (true) {
                                        System.out.println("1) Search for movie");
                                        System.out.println("2) Search for series");
                                        System.out.println("3) Previous page");
                                        String option3 = scanner.nextLine();
                                        if (option3.equals("1")) {
                                            System.out.println("Enter movie");
                                            String option4 = scanner.nextLine();
                                            boolean ok = false;
                                            for (Movie m : imdb.movie) {
                                                if (m.title.equals(option4)) {
                                                    m.displayInfo();
                                                    ok = true;
                                                    break;
                                                }
                                            }
                                            if (!ok) {
                                                System.out.println("Movie doesn't exist");
                                            }
                                        } else if (option3.equals("2")) {
                                            System.out.println("Enter Series");
                                            String option5 = scanner.nextLine();
                                            boolean ok = false;
                                            for (Series s : imdb.series) {
                                                if (s.title.equals(option5)) {
                                                    s.displayInfo();
                                                    ok = true;
                                                    break;
                                                }
                                            }
                                            if (!ok) {
                                                System.out.println("Series doesn't exist");
                                            }
                                        } else if (option3.equals("3")) {
                                            break;
                                        } else {
                                            System.out.println("Invalid command! Try again");
                                        }
                                    }
                                } else if (option2.equals("2")) {
                                    System.out.println("Enter actor");
                                    String option3 = scanner.nextLine();
                                    boolean ok = false;
                                    for (Actor a : imdb.actor) {
                                        if (a.name.equals(option3)) {
                                            System.out.println("Name: " + a.name);
                                            System.out.println("Biography: " + a.biography);
                                            System.out.println("Performances: ");
                                            for (String key : a.roles.keySet()) {
                                                System.out.println(key + " - " + a.roles.get(key));
                                            }
                                            ok = true;
                                            break;
                                        }
                                    }
                                    if (!ok) {
                                        System.out.println("Actor doesn't exist");
                                    }
                                } else if (option2.equals("3")) {
                                    break;
                                } else {
                                    System.out.println("Invalid command! Try again");
                                }
                            }
                        } else if (option1.equals("5")) {
                            for (Object o : regUsr.favorites) {
                                System.out.println(o);
                            }
                        } else if (option1.equals("6")) {
                            while (true) {
                                System.out.println("1) Add to favorites");
                                System.out.println("2) Remove from favorites");
                                System.out.println("3) Previous page");
                                String option2 = scanner.nextLine();
                                if (option2.equals("1")) {
                                    String option3 = scanner.nextLine();
                                    boolean ok = false;
                                    for (Movie m : imdb.movie) {
                                        if (m.title.equals(option3)) {
                                            ok = true;
                                            regUsr.addFavorite(option3);
                                            break;
                                        }
                                    }
                                    if (!ok) {
                                        for (Series s : imdb.series) {
                                            if (s.title.equals(option3)) {
                                                ok = true;
                                                regUsr.addFavorite(option3);
                                                break;
                                            }
                                        }
                                    }
                                    if (!ok) {
                                        for (Actor a : imdb.actor) {
                                            if (a.name.equals(option3)) {
                                                ok = true;
                                                regUsr.addFavorite(option3);
                                                break;
                                            }
                                        }
                                    }
                                    if (!ok) {
                                        System.out.println("Couldn't find production/actor");
                                    }
                                } else if (option2.equals("2")) {
                                    String option3 = scanner.nextLine();
                                    boolean ok = false;
                                    for (Object o : regUsr.favorites) {
                                        if (o.equals(option3)) {
                                            regUsr.removeFavorite(option3);
                                            ok = true;
                                            break;
                                        }
                                    }
                                    if (!ok) {
                                        System.out.println("Couldn't find production/actor");
                                    }
                                } else if (option2.equals("3")) {
                                    break;
                                } else {
                                    System.out.println("Invalid command! Try again");
                                }
                            }
                        } else if (option1.equals("7")) {
                            while (true) {
                                System.out.println("1) Rate a movie");
                                System.out.println("2) Rate a series");
                                System.out.println("3) Previous page");
                                String option2 = scanner.nextLine();
                                if (option2.equals("1")) {
                                    System.out.println("Select movie");
                                    for (Movie m : imdb.movie) {
                                        System.out.println(imdb.movie.indexOf(m) + 1 + ") " + m.title);
                                    }
                                    String option3 = scanner.nextLine();
                                    int opt = Integer.parseInt(option3);
                                    if (opt > 0 && opt <= imdb.movie.size()) {
                                        System.out.println("Introduce rating:");
                                        String rating = scanner.nextLine();
                                        int ratng = Integer.parseInt(rating);
                                        if (ratng <= 10 && ratng > 0) {
                                            System.out.println("Introduce comment");
                                            String comment = scanner.nextLine();
                                            Rating ratings = new Rating(regUsr.username, ratng, comment);
                                            imdb.movie.get(opt - 1).Ratings.add(ratings);
                                            imdb.movie.get(opt - 1).TotalRating = imdb.movie.get(opt - 1).CalculateRating();
                                            ExprerienceStrategy xp = new addReviewExperience();
                                            regUsr.setExprerienceStrategy(xp);
                                            regUsr.updateExperience();
                                        } else {
                                            System.out.println("Invalid number");
                                        }
                                        break;
                                    } else {
                                        System.out.println("Invalid movie! Please, try again");
                                        break;
                                    }

                                } else if (option2.equals("2")) {
                                    System.out.println("Select series");
                                    for (Series s : imdb.series) {
                                        System.out.println(imdb.series.indexOf(s) + 1 + ") " + s.title);
                                    }
                                    String option3 = scanner.nextLine();
                                    int opt = Integer.parseInt(option3);
                                    if (opt > 0 && opt <= imdb.series.size()) {
                                        System.out.println("Introduce rating:");
                                        String rating = scanner.nextLine();
                                        int ratng = Integer.parseInt(rating);
                                        if (ratng <= 10 && ratng > 0) {
                                            System.out.println("Introduce comment");
                                            String comment = scanner.nextLine();
                                            Rating ratings = new Rating(regUsr.username, ratng, comment);
                                            imdb.series.get(opt - 1).Ratings.add(ratings);
                                            imdb.series.get(opt - 1).TotalRating = imdb.series.get(opt - 1).CalculateRating();
                                            ExprerienceStrategy xp = new addReviewExperience();
                                            regUsr.setExprerienceStrategy(xp);
                                            regUsr.updateExperience();
                                        } else {
                                            System.out.println("Invalid number");
                                        }
                                        break;
                                    } else {
                                        System.out.println("Invalid series! Please, try again");
                                        break;
                                    }
                                } else if (option2.equals("3")) {
                                    break;
                                } else {
                                    System.out.println("Invalid command! Try again");
                                }
                            }
                        } else if (option1.equals("8")) {
                            System.out.println("1) Select an actor");
                            System.out.println("2) Previous page");
                            String option3 = scanner.nextLine();
                            if (option3.equals("1")) {
                                for (Actor a : imdb.actor) {
                                    System.out.println(imdb.actor.indexOf(a) + 1 + ") " + a.name);
                                }
                                String option2 = scanner.nextLine();
                                int opt = Integer.parseInt(option2);
                                if (opt > 0 && opt <= imdb.actor.size()) {
                                    System.out.println("Introduce rating:");
                                    String rating = scanner.nextLine();
                                    int ratng = Integer.parseInt(rating);
                                    if (ratng <= 10 && ratng > 0) {
                                        System.out.println("Introduce comment");
                                        String comment = scanner.nextLine();
                                        Rating ratings = new Rating(regUsr.username, ratng, comment);
                                        imdb.actor.get(opt - 1).Ratings.add(ratings);
                                        imdb.actor.get(opt - 1).TotalRating = imdb.actor.get(opt - 1).CalculateRating();
                                        ExprerienceStrategy xp = new addReviewExperience();
                                        regUsr.setExprerienceStrategy(xp);
                                        regUsr.updateExperience();
                                    } else {
                                        System.out.println("Invalid number");
                                    }
                                } else {
                                    System.out.println("Invalid command! Try again");
                                }
                            } else if (!option3.equals("2")) {
                                System.out.println("Invalid command!");
                            }
                        } else if (option1.equals("9")) {
                            System.out.println("Select a request type:");
                            System.out.println("    1) DELETE ACCOUNT");
                            System.out.println("    2) ACTOR ISSUE");
                            System.out.println("    3) MOVIE ISSUE");
                            System.out.println("    4) OTHERS");
                            String option2 = scanner.nextLine();
                            if (option2.equals("1") || option2.equals("2") || option2.equals("3") || option2.equals("4")) {
                                System.out.print("Request name:");
                                String name = scanner.nextLine();
                                System.out.println("Write a description:");
                                String description = scanner.nextLine();
                                switch (option2) {
                                    case "1":
                                        Request r = new Request(RequestTypes.DELETE_ACCOUNT, name, description, regUsr.username, "ADMIN", imdb);
                                        imdb.request.add(r);
                                        RequestsHolder.addRequest(r);
                                        break;
                                    case "2":
                                        for (Contributor c : imdb.contributor) {
                                            for (Object a : c.prod) {
                                                if (a instanceof Actor) {
                                                    if (((Actor) a).name.equals(name)) {
                                                        Request r2 = new Request(RequestTypes.ACTOR_ISSUE, name, description, regUsr.username, c.username, imdb);
                                                        c.r.add(r2);
                                                        imdb.request.add(r2);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        for (Admin adm : imdb.admin) {
                                            for (Object a : adm.prod) {
                                                if (a instanceof Actor) {
                                                    if (((Actor) a).name.equals(name)) {
                                                        Request r2 = new Request(RequestTypes.ACTOR_ISSUE, name, description, regUsr.username, adm.username, imdb);
                                                        adm.r.add(r2);
                                                        imdb.request.add(r2);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        //Request r2 = new Request(RequestTypes.ACTOR_ISSUE, name, description, regUsr.username, imdb);
                                        //imdb.request.add(r2);
                                        break;
                                    case "3":
//                                        Request r3 = new Request(RequestTypes.MOVIE_ISSUE, name, description, regUsr.username, imdb);
//                                        imdb.request.add(r3);
                                        for (Contributor c : imdb.contributor) {
                                            for (Object a : c.prod) {
                                                if (a instanceof Movie) {
                                                    if (((Movie) a).title.equals(name)) {
                                                        Request r2 = new Request(RequestTypes.ACTOR_ISSUE, name, description, regUsr.username, c.username, imdb);
                                                        c.r.add(r2);
                                                        imdb.request.add(r2);
                                                        break;
                                                    }
                                                }
                                                if (a instanceof Series) {
                                                    if (((Series) a).title.equals(name)) {
                                                        Request r2 = new Request(RequestTypes.ACTOR_ISSUE, name, description, regUsr.username, c.username, imdb);
                                                        c.r.add(r2);
                                                        imdb.request.add(r2);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        for (Admin adm : imdb.admin) {
                                            for (Object a : adm.prod) {
                                                if (a instanceof Movie) {
                                                    if (((Movie) a).title.equals(name)) {
                                                        Request r2 = new Request(RequestTypes.ACTOR_ISSUE, name, description, regUsr.username, adm.username, imdb);
                                                        adm.r.add(r2);
                                                        imdb.request.add(r2);
                                                        break;
                                                    }
                                                }
                                                if (a instanceof Series) {
                                                    if (((Series) a).title.equals(name)) {
                                                        Request r2 = new Request(RequestTypes.ACTOR_ISSUE, name, description, regUsr.username, adm.username, imdb);
                                                        adm.r.add(r2);
                                                        imdb.request.add(r2);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    case "4":
                                        Request r4 = new Request(RequestTypes.OTHERS, name, description, regUsr.username, "ADMIN", imdb);
                                        RequestsHolder.addRequest(r4);
                                        imdb.request.add(r4);
                                        break;
                                }
                            } else {
                                System.out.println("Invalid command");
                            }
                        } else if (option1.equals("10")) {
                            System.out.println("Your requests:");
                            int nr = 0;
                            for (Request r : imdb.request) {
                                if (r.username_requester.equals(regUsr.username)) {
                                    nr++;
                                    System.out.println(nr);
                                    System.out.println(r);
                                }
                            }
                            System.out.println("Choose request to be deleted:");
                            String option2 = scanner.nextLine();
                            if (Integer.parseInt(option2) <= nr && Integer.parseInt(option2) > 0) {
                                nr = 0;
                                for (Request r2 : imdb.request) {
                                    if (r2.username_requester.equals(regUsr.username)) {
                                        nr++;
                                        if (nr == Integer.parseInt(option2)) {
                                            imdb.request.remove(r2);
                                            ExprerienceStrategy xp = new removeRequestExperience();
                                            regUsr.setExprerienceStrategy(xp);
                                            regUsr.updateExperience();
                                            for (Contributor c : imdb.contributor) {
                                                for (Object r : c.r) {
                                                    if (((Request) r).username_requester.equals(regUsr.username) && ((Request) r).name.equals(r2.name)) {
                                                        c.r.remove(r);
                                                        break;
                                                    }
                                                }
                                            }
                                            for (Admin a : imdb.admin) {
                                                for (Object r : a.r) {
                                                    if (((Request) r).username_requester.equals(regUsr.username) && ((Request) r).name.equals(r2.name)) {
                                                        a.r.remove(r);
                                                        break;
                                                    }
                                                }
                                            }
                                            for (Request r : RequestsHolder.requests) {
                                                if (r.username_requester.equals(regUsr.username) && r.name.equals(r2.name)) {
                                                    RequestsHolder.requests.remove(r);
                                                    break;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            } else {
                                System.out.println("Invalid command");
                                break;
                            }
                        } else if (option1.equals("11")) {
                            isAutentificated = false;
                            regUsr.logOut();
                        } else {
                            System.out.println("Invalid command!");
                        }
                    }

                    if (contrUsr != null) {
                        System.out.println("Welcome back!");
                        System.out.print("Username: ");
                        System.out.println(contrUsr.username);
                        System.out.print("Experience: ");
                        System.out.println(contrUsr.experience);
                        System.out.println("Account type: Contributor");
                        System.out.println("1) View productions details");
                        System.out.println("2) View actors details");
                        System.out.println("3) View notifications");
                        System.out.println("4) Search for actor/movie/series");
                        System.out.println("5) View favorite productions/actors");
                        System.out.println("6) Add/Delete actor/movie/series to/from favorites");
                        System.out.println("7) Create a request");
                        System.out.println("8) Delete a request");
                        System.out.println("9) Solve a request");
                        System.out.println("10) Update movie details");
                        System.out.println("11) Update actor details");
                        System.out.println("12) Add/Delete actor/movie/series from system");
                        System.out.println("13) Logout");

                        String option1 = scanner.nextLine();

                        if (option1.equals("1")) {
                            while (true) {
                                System.out.println("1) View movie details");
                                System.out.println("2) View series details");
                                System.out.println("3) Previous page");

                                String option2 = scanner.nextLine();

                                if (option2.equals("1")) {
                                    while (true) {
                                        System.out.println("Select movie");
                                        for (Movie m : imdb.movie) {
                                            System.out.println(imdb.movie.indexOf(m) + 1 + ") " + m.title);
                                        }
                                        String option3 = scanner.nextLine();
                                        int opt = Integer.parseInt(option3);
                                        if (opt > 0 && opt <= imdb.movie.size()) {
                                            imdb.movie.get(opt - 1).displayInfo();
                                            break;
                                        } else {
                                            System.out.println("Invalid movie! Please, try again");
                                            break;
                                        }
                                    }
                                } else if (option2.equals("2")) {
                                    while (true) {
                                        System.out.println("Select series");
                                        for (Series s : imdb.series) {
                                            System.out.println(imdb.series.indexOf(s) + 1 + ") " + s.title);
                                        }
                                        String option3 = scanner.nextLine();
                                        int opt = Integer.parseInt(option3);
                                        if (opt > 0 && opt <= imdb.series.size()) {
                                            imdb.series.get(opt - 1).displayInfo();
                                            break;
                                        } else {
                                            System.out.println("Invalid movie! Please, try again");
                                            break;
                                        }
                                    }
                                } else if (option2.equals("3")) {
                                    break;
                                } else {
                                    System.out.println("Invalid option! Try again");
                                }
                            }
                        } else if (option1.equals("2")) {
                            while (true) {
                                System.out.println("Select actor");
                                for (Actor a : imdb.actor) {
                                    System.out.println(imdb.actor.indexOf(a) + 1 + ") " + a.name);
                                }
                                String option4 = scanner.nextLine();
                                int opt = Integer.parseInt(option4);
                                if (opt > 0 && opt <= imdb.actor.size()) {
                                    System.out.println("Name: " + imdb.actor.get(opt - 1).name);
                                    if (imdb.actor.get(opt - 1).biography != null) {
                                        System.out.println("Biography: " + imdb.actor.get(opt - 1).biography);
                                    }
                                    System.out.println("Ratings: " + imdb.actor.get(opt - 1).TotalRating);
                                    if (imdb.actor.get(opt - 1).roles.keySet() != null) {
                                        System.out.println("Performances: ");
                                        for (String key : imdb.actor.get(opt - 1).roles.keySet()) {
                                            System.out.println(key + " - " + imdb.actor.get(opt - 1).roles.get(key));
                                        }
                                    }
                                    break;
                                } else {
                                    System.out.println("Invalid actor! Please, try again");
                                    break;
                                }
                            }
                        } else if (option1.equals("3")) {
                            System.out.println("Notifications: ");
                            for (Object n : contrUsr.notifications) {
                                System.out.println(n);
                            }
                        } else if (option1.equals("4")) {
                            while (true) {
                                System.out.println("1) Search for production");
                                System.out.println("2) Search for actor");
                                System.out.println("3) Previous page");
                                String option2 = scanner.nextLine();
                                if (option2.equals("1")) {
                                    while (true) {
                                        System.out.println("1) Search for movie");
                                        System.out.println("2) Search for series");
                                        System.out.println("3) Previous page");
                                        String option3 = scanner.nextLine();
                                        if (option3.equals("1")) {
                                            System.out.println("Enter movie");
                                            String option4 = scanner.nextLine();
                                            boolean ok = false;
                                            for (Movie m : imdb.movie) {
                                                if (m.title.equals(option4)) {
                                                    m.displayInfo();
                                                    ok = true;
                                                    break;
                                                }
                                            }
                                            if (!ok) {
                                                System.out.println("Movie doesn't exist");
                                            }
                                        } else if (option3.equals("2")) {
                                            System.out.println("Enter Series");
                                            String option5 = scanner.nextLine();
                                            boolean ok = false;
                                            for (Series s : imdb.series) {
                                                if (s.title.equals(option5)) {
                                                    s.displayInfo();
                                                    ok = true;
                                                    break;
                                                }
                                            }
                                            if (!ok) {
                                                System.out.println("Series doesn't exist");
                                            }
                                        } else if (option3.equals("3")) {
                                            break;
                                        } else {
                                            System.out.println("Invalid command! Try again");
                                        }
                                    }
                                } else if (option2.equals("2")) {
                                    System.out.println("Enter actor");
                                    String option3 = scanner.nextLine();
                                    boolean ok = false;
                                    for (Actor a : imdb.actor) {
                                        if (a.name.equals(option3)) {
                                            System.out.println("Name: " + a.name);
                                            System.out.println("Biography: " + a.biography);
                                            System.out.println("Performances: ");
                                            for (String key : a.roles.keySet()) {
                                                System.out.println(key + " - " + a.roles.get(key));
                                            }
                                            ok = true;
                                            break;
                                        }
                                    }
                                    if (!ok) {
                                        System.out.println("Actor doesn't exist");
                                    }
                                } else if (option2.equals("3")) {
                                    break;
                                } else {
                                    System.out.println("Invalid command! Try again");
                                }
                            }
                        } else if (option1.equals("5")) {
                            for (Object o : contrUsr.favorites) {
                                System.out.println(o);
                            }
                        } else if (option1.equals("6")) {
                            while (true) {
                                System.out.println("1) Add to favorites");
                                System.out.println("2) Remove from favorites");
                                System.out.println("3) Previous page");
                                String option2 = scanner.nextLine();
                                if (option2.equals("1")) {
                                    String option3 = scanner.nextLine();
                                    boolean ok = false;
                                    for (Movie m : imdb.movie) {
                                        if (m.title.equals(option3)) {
                                            ok = true;
                                            contrUsr.addFavorite(option3);
                                            break;
                                        }
                                    }
                                    if (!ok) {
                                        for (Series s : imdb.series) {
                                            if (s.title.equals(option3)) {
                                                ok = true;
                                                contrUsr.addFavorite(option3);
                                                break;
                                            }
                                        }
                                    }
                                    if (!ok) {
                                        for (Actor a : imdb.actor) {
                                            if (a.name.equals(option3)) {
                                                ok = true;
                                                contrUsr.addFavorite(option3);
                                                break;
                                            }
                                        }
                                    }
                                    if (!ok) {
                                        System.out.println("Couldn't find production/actor");
                                    }
                                } else if (option2.equals("2")) {
                                    String option3 = scanner.nextLine();
                                    boolean ok = false;
                                    for (Object o : contrUsr.favorites) {
                                        if (o.equals(option3)) {
                                            contrUsr.removeFavorite(option3);
                                            ok = true;
                                            break;
                                        }
                                    }
                                    if (!ok) {
                                        System.out.println("Couldn't find production/actor");
                                    }
                                } else if (option2.equals("3")) {
                                    break;
                                } else {
                                    System.out.println("Invalid command! Try again");
                                }
                            }
                        } else if (option1.equals("7")) {
                            System.out.println("Select a request type:");
                            System.out.println("    1) DELETE ACCOUNT");
                            System.out.println("    2) ACTOR ISSUE");
                            System.out.println("    3) MOVIE ISSUE");
                            System.out.println("    4) OTHERS");
                            String option2 = scanner.nextLine();
                            if (option2.equals("1") || option2.equals("2") || option2.equals("3") || option2.equals("4")) {
                                System.out.print("Request name:");
                                String name = scanner.nextLine();
                                System.out.println("Write a description:");
                                String description = scanner.nextLine();
                                switch (option2) {
                                    case "1":
                                        Request r = new Request(RequestTypes.DELETE_ACCOUNT, name, description, regUsr.username, "ADMIN", imdb);
                                        imdb.request.add(r);
                                        RequestsHolder.addRequest(r);
                                        break;
                                    case "2":
                                        for (Contributor c : imdb.contributor) {
                                            for (Object a : c.prod) {
                                                if (a instanceof Actor) {
                                                    if (((Actor) a).name.equals(name)) {
                                                        Request r2 = new Request(RequestTypes.ACTOR_ISSUE, name, description, regUsr.username, c.username, imdb);
                                                        c.r.add(r2);
                                                        imdb.request.add(r2);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        for (Admin adm : imdb.admin) {
                                            for (Object a : adm.prod) {
                                                if (a instanceof Actor) {
                                                    Actor act = (Actor) a;
                                                    if (act.equals(name)) {
                                                        Request r2 = new Request(RequestTypes.ACTOR_ISSUE, name, description, regUsr.username, adm.username, imdb);
                                                        adm.r.add(r2);
                                                        imdb.request.add(r2);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        //Request r2 = new Request(RequestTypes.ACTOR_ISSUE, name, description, regUsr.username, imdb);
                                        //imdb.request.add(r2);
                                        break;
                                    case "3":
//                                        Request r3 = new Request(RequestTypes.MOVIE_ISSUE, name, description, regUsr.username, imdb);
//                                        imdb.request.add(r3);
                                        for (Contributor c : imdb.contributor) {
                                            for (Object a : c.prod) {
                                                if (a instanceof Movie) {
                                                    if (((Movie) a).title.equals(name)) {
                                                        Request r2 = new Request(RequestTypes.ACTOR_ISSUE, name, description, regUsr.username, c.username, imdb);
                                                        c.r.add(r2);
                                                        imdb.request.add(r2);
                                                        break;
                                                    }
                                                }
                                                if (a instanceof Series) {
                                                    if (((Series) a).title.equals(name)) {
                                                        Request r2 = new Request(RequestTypes.ACTOR_ISSUE, name, description, regUsr.username, c.username, imdb);
                                                        c.r.add(r2);
                                                        imdb.request.add(r2);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        for (Admin adm : imdb.admin) {
                                            for (Object a : adm.prod) {
                                                if (a instanceof Movie) {
                                                    if (((Movie) a).title.equals(name)) {
                                                        Request r2 = new Request(RequestTypes.ACTOR_ISSUE, name, description, regUsr.username, adm.username, imdb);
                                                        adm.r.add(r2);
                                                        imdb.request.add(r2);
                                                        break;
                                                    }
                                                }
                                                if (a instanceof Series) {
                                                    if (((Series) a).title.equals(name)) {
                                                        Request r2 = new Request(RequestTypes.ACTOR_ISSUE, name, description, regUsr.username, adm.username, imdb);
                                                        adm.r.add(r2);
                                                        imdb.request.add(r2);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    case "4":
                                        Request r4 = new Request(RequestTypes.OTHERS, name, description, regUsr.username, "ADMIN", imdb);
                                        RequestsHolder.addRequest(r4);
                                        imdb.request.add(r4);
                                        break;
                                }
                            } else {
                                System.out.println("Invalid command");
                            }
                        } else if (option1.equals("8")) {
                            System.out.println("Your requests:");
                            int nr = 0;
                            for (Request r : imdb.request) {
                                if (r.username_requester.equals(contrUsr.username)) {
                                    nr++;
                                    System.out.println(nr);
                                    System.out.println(r);
                                }
                            }
                            System.out.println("Choose request to be deleted:");
                            String option2 = scanner.nextLine();
                            if (Integer.parseInt(option2) <= nr && Integer.parseInt(option2) > 0) {
                                nr = 0;
                                for (Request r2 : imdb.request) {
                                    if (r2.username_requester.equals(contrUsr.username)) {
                                        nr++;
                                        if (nr == Integer.parseInt(option2)) {
                                            imdb.request.remove(r2);
                                            ExprerienceStrategy xp = new removeRequestExperience();
                                            contrUsr.setExprerienceStrategy(xp);
                                            contrUsr.updateExperience();
                                            for (Contributor c : imdb.contributor) {
                                                for (Object r : c.r) {
                                                    if (((Request) r).username_requester.equals(regUsr.username) && ((Request) r).name.equals(r2.name)) {
                                                        c.r.remove(r);
                                                        break;
                                                    }
                                                }
                                            }
                                            for (Admin a : imdb.admin) {
                                                for (Object r : a.r) {
                                                    if (((Request) r).username_requester.equals(regUsr.username) && ((Request) r).name.equals(r2.name)) {
                                                        a.r.remove(r);
                                                        break;
                                                    }
                                                }
                                            }
                                            for (Request r : RequestsHolder.requests) {
                                                if (r.username_requester.equals(regUsr.username) && r.name.equals(r2.name)) {
                                                    RequestsHolder.requests.remove(r);
                                                    break;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            } else {
                                System.out.println("Invalid command");
                                break;
                            }
                        } else if (option1.equals("9")) {
                            System.out.println("These are requests for you:");
                            for (Object r : contrUsr.r) {
                                Request req = (Request) r;
                                System.out.println(req.name);
                                System.out.println(req.description);
                                System.out.println(req.username_requester);
                                System.out.println("Resolve this request?");
                                String option2 = scanner.nextLine();
                                if (option2.equals("yes")) {
                                    for (Regular r2 : imdb.regular) {
                                        if (((Request) r).username_requester.equals(r2.username)) {
                                            ExprerienceStrategy xp = new addRequestExperience();
                                            r2.setExprerienceStrategy(xp);
                                            r2.updateExperience();
                                            r2.notifications.add("Request solved");
                                            contrUsr.r.remove(r);
                                            break;
                                        }
                                    }
                                    for (Contributor c2 : imdb.contributor) {
                                        if (((Request) r).username_requester.equals(c2.username)) {
                                            ExprerienceStrategy xp = new addRequestExperience();
                                            c2.setExprerienceStrategy(xp);
                                            c2.updateExperience();
                                            c2.notifications.add("Request solved");
                                            contrUsr.r.remove(r);
                                            break;
                                        }
                                    }
                                } else if (option2.equals("no")) {
                                    continue;
                                } else {
                                    System.out.println("Invalid command");
                                }
                            }
                        } else if (option1.equals("10")) {
                            System.out.println("1) Select a movie");
                            System.out.println("2) Select a series");
                            String option2 = scanner.nextLine();
                            if (option2.equals("1")) {
                                System.out.println("Type movie:");
                                String option3 = scanner.nextLine();
                                boolean ok = false;
                                for (Movie m : imdb.movie) {
                                    if (m.title.equals(option3)) {
                                        String desc = scanner.nextLine();
                                        contrUsr.updateProduction(m, desc);
                                        ok = true;
                                        break;
                                    }
                                }
                                if (!ok) {
                                    System.out.println("Invalid movie");
                                }
                            } else if (option2.equals("2")) {
                                System.out.println("Type series:");
                                String option3 = scanner.nextLine();
                                boolean ok = false;
                                for (Series s : imdb.series) {
                                    if (s.title.equals(option3)) {
                                        String desc = scanner.nextLine();
                                        contrUsr.updateProduction(s, desc);
                                        ok = true;
                                        break;
                                    }
                                }
                                if (!ok) {
                                    System.out.println("Invalid movie");
                                }
                            } else {
                                System.out.println("Invalid command");
                            }
                        } else if (option1.equals("11")) {
                            System.out.println("Select actor");
                            String option2 = scanner.nextLine();
                            boolean ok = false;
                            for (Actor a : imdb.actor) {
                                if (a.name.equals(option2)) {
                                    String bio = scanner.nextLine();
                                    contrUsr.updateActor(a, bio);
                                    ok = true;
                                    break;
                                }
                            }
                            if (!ok) {
                                System.out.println("Invalid actor");
                            }
                        } else if (option1.equals("12")) {
                            while (true) {
                                System.out.println("1) Add actor/production");
                                System.out.println("2) Delete actor/production");
                                System.out.println("3) Previous page");
                                String option2 = scanner.nextLine();
                                if (option2.equals("1")) {
                                    while (true) {
                                        System.out.println("1) Add actor");
                                        System.out.println("2) Add production");
                                        System.out.println("3) Previous page");
                                        String option3 = scanner.nextLine();
                                        if (option3.equals("1")) {
                                            while (true) {
                                                System.out.println("Introduce name");
                                                String name = scanner.nextLine();
                                                Map<String, String> map = new HashMap<>();
                                                while (true) {
                                                    System.out.println("Movie name:");
                                                    String key = scanner.nextLine();
                                                    if (key.equals("exit")) {
                                                        break;
                                                    }
                                                    System.out.println("Type:");
                                                    String value = scanner.nextLine();
                                                    map.put(key, value);
                                                }
                                                System.out.println("Introduce Biography");
                                                String bio = scanner.nextLine();
                                                Actor actor1 = new Actor(name, map, bio);
                                                imdb.actor.add(actor1);
                                            }
                                        } else if (option3.equals("2")) {
                                            while (true) {
                                                System.out.println("1) Add movie");
                                                System.out.println("2) Add series");
                                                System.out.println("3) Previous page");
                                                String option4 = scanner.nextLine();
                                                if (option4.equals("1")) {
                                                    System.out.println("Introduce title:");
                                                    String title = scanner.nextLine();
                                                    System.out.println("Introduce directors:");
                                                    List<String> Dir = new ArrayList<>();
                                                    while (true) {
                                                        String input = scanner.nextLine();
                                                        if (input.equals("exit")) {
                                                            break;
                                                        }
                                                        Dir.add(input);
                                                    }
                                                    System.out.println("Introduce actors:");
                                                    List<String> Act = new ArrayList<>();
                                                    while (true) {
                                                        String input = scanner.nextLine();
                                                        if (input.equals("exit")) {
                                                            break;
                                                        }
                                                        Act.add(input);
                                                    }
                                                    List<Genre> gen = new ArrayList<>();
                                                    while (true) {
                                                        System.out.println("Select genre:");
                                                        System.out.println("1) Action");
                                                        System.out.println("2) Comedy");
                                                        System.out.println("3) Drama");
                                                        System.out.println("4) Thriller");
                                                        String input = scanner.nextLine();
                                                        if (input.equals("1")) {
                                                            gen.add(Genre.Action);
                                                        }
                                                        if (input.equals("2")) {
                                                            gen.add(Genre.Comedy);
                                                        }
                                                        if (input.equals("3")) {
                                                            gen.add(Genre.Drama);
                                                        }
                                                        if (input.equals("4")) {
                                                            gen.add(Genre.Thriller);
                                                        }
                                                        if (input.equals("exit")) {
                                                            break;
                                                        }
                                                    }
                                                    System.out.println("Enter Description:");
                                                    String desc = scanner.nextLine();
                                                    System.out.println("Enter duration:");
                                                    String dur = scanner.nextLine();
                                                    Integer durint = Integer.parseInt(dur);
                                                    System.out.println("Enter release year");
                                                    String yr = scanner.nextLine();
                                                    Integer yrint = Integer.parseInt(yr);
                                                    Movie movie1 = new Movie(title, Dir, Act, gen, null, desc, durint, yrint);
                                                    imdb.movie.add(movie1);
                                                    ExprerienceStrategy xp = new addNewProductActorExperience();
                                                    contrUsr.setExprerienceStrategy(xp);
                                                    contrUsr.updateExperience();
                                                } else if (option4.equals("2")) {
                                                    System.out.println("Introduce title:");
                                                    String title = scanner.nextLine();
                                                    System.out.println("Introduce directors:");
                                                    List<String> Dir = new ArrayList<>();
                                                    while (true) {
                                                        String input = scanner.nextLine();
                                                        if (input.equals("exit")) {
                                                            break;
                                                        }
                                                        Dir.add(input);
                                                    }
                                                    System.out.println("Introduce actors:");
                                                    List<String> Act = new ArrayList<>();
                                                    while (true) {
                                                        String input = scanner.nextLine();
                                                        if (input.equals("exit")) {
                                                            break;
                                                        }
                                                        Act.add(input);
                                                    }
                                                    List<Genre> gen = new ArrayList<>();
                                                    while (true) {
                                                        System.out.println("Select genre:");
                                                        System.out.println("1) Action");
                                                        System.out.println("2) Comedy");
                                                        System.out.println("3) Drama");
                                                        System.out.println("4) Thriller");
                                                        String input = scanner.nextLine();
                                                        if (input.equals("1")) {
                                                            gen.add(Genre.Action);
                                                        }
                                                        if (input.equals("2")) {
                                                            gen.add(Genre.Comedy);
                                                        }
                                                        if (input.equals("3")) {
                                                            gen.add(Genre.Drama);
                                                        }
                                                        if (input.equals("4")) {
                                                            gen.add(Genre.Thriller);
                                                        }
                                                        if (input.equals("exit")) {
                                                            break;
                                                        }
                                                    }
                                                    System.out.println("Enter Description:");
                                                    String desc = scanner.nextLine();
                                                    System.out.println("Enter number of seasons:");
                                                    String dur = scanner.nextLine();
                                                    Integer durint = Integer.parseInt(dur);
                                                    System.out.println("Enter release year");
                                                    String yr = scanner.nextLine();
                                                    Integer yrint = Integer.parseInt(yr);
                                                    Map<String, List<Episode>> episodes = new HashMap<>();
                                                    System.out.println("Introduce episodes:");
                                                    while (true) {
                                                        System.out.print("Episode title:");
                                                        String titleep = scanner.nextLine();
                                                        if (titleep.equals("exit")) {
                                                            break;
                                                        }
                                                        System.out.print("Introduce episode duration:");
                                                        int duration = Integer.parseInt(scanner.nextLine());
                                                        Episode episode = new Episode(titleep, duration);
                                                        episode.title = title;
                                                        episode.Duration = duration;
                                                        System.out.print("Introduce season:");
                                                        String seasonEpisode = scanner.nextLine();
                                                        episodes.computeIfAbsent(seasonEpisode, k -> new ArrayList<>()).add(episode);
                                                    }
                                                    Series series1 = new Series(title, Dir, Act, gen, null, desc, yrint, durint, episodes);
                                                    imdb.series.add(series1);
                                                    ExprerienceStrategy xp = new addNewProductActorExperience();
                                                    contrUsr.setExprerienceStrategy(xp);
                                                    contrUsr.updateExperience();
                                                } else if (option4.equals("3")) {
                                                    break;
                                                } else {
                                                    System.out.println("Invalid command");
                                                }
                                            }
                                        } else if (option3.equals("3")) {
                                            break;
                                        } else {
                                            System.out.println("Invalid command");
                                        }
                                    }
                                } else if (option2.equals("2")) {
                                    System.out.println("Type the production:");
                                    String option3 = scanner.nextLine();
                                    boolean ok = false;
                                    for (Actor a : imdb.actor) {
                                        if (a.name.equals(option3)) {
                                            imdb.actor.remove(a);
                                            ExprerienceStrategy xp = new removeProductActorExperience();
                                            contrUsr.setExprerienceStrategy(xp);
                                            contrUsr.updateExperience();
                                            ok = true;
                                            break;
                                        }
                                    }
                                    if (!ok) {
                                        for (Movie m : imdb.movie) {
                                            if (m.title.equals(option3)) {
                                                imdb.actor.remove(m);
                                                ExprerienceStrategy xp = new removeProductActorExperience();
                                                contrUsr.setExprerienceStrategy(xp);
                                                contrUsr.updateExperience();
                                                ok = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (!ok) {
                                        for (Series s : imdb.series) {
                                            if (s.title.equals(option3)) {
                                                imdb.actor.remove(s);
                                                ExprerienceStrategy xp = new removeProductActorExperience();
                                                contrUsr.setExprerienceStrategy(xp);
                                                contrUsr.updateExperience();
                                                ok = true;
                                                break;
                                            }
                                        }
                                    }
                                } else if (option2.equals("3")) {
                                    break;
                                } else {
                                    System.out.println("Invalid command");
                                }
                            }
                        } else if (option1.equals("13")) {
                            isAutentificated = false;
                            contrUsr.logOut();
                        } else {
                            System.out.println("Invalid command!");
                        }
                    }
                    if (admUsr != null) {
                        System.out.println("Welcome back!");
                        System.out.print("Username: ");
                        System.out.println(admUsr.username);
                        System.out.print("Experience: ");
                        System.out.println(admUsr.experience);
                        System.out.println("Account type: Admin");
                        System.out.println("1) View productions details");
                        System.out.println("2) View actors details");
                        System.out.println("3) View notifications");
                        System.out.println("4) Search for actor/movie/series");
                        System.out.println("5) Update movie details");
                        System.out.println("6) Update actor details");
                        System.out.println("7) View favorite productions/actors");
                        System.out.println("8) Add/Delete actor/movie/series to/from favorites");
                        System.out.println("9) Add/Delete actor/movie/series from system");
                        System.out.println("10) Add/Delete user");
                        System.out.println("11) Solve a request");
                        System.out.println("12) Logout");

                        String option1 = scanner.nextLine();

                        if (option1.equals("1")) {
                            while (true) {
                                System.out.println("1) View movie details");
                                System.out.println("2) View series details");
                                System.out.println("3) Previous page");

                                String option2 = scanner.nextLine();

                                if (option2.equals("1")) {
                                    while (true) {
                                        System.out.println("Select movie");
                                        for (Movie m : imdb.movie) {
                                            System.out.println(imdb.movie.indexOf(m) + 1 + ") " + m.title);
                                        }
                                        String option3 = scanner.nextLine();
                                        int opt = Integer.parseInt(option3);
                                        if (opt > 0 && opt <= imdb.movie.size()) {
                                            imdb.movie.get(opt - 1).displayInfo();
                                            break;
                                        } else {
                                            System.out.println("Invalid movie! Please, try again");
                                            break;
                                        }
                                    }
                                } else if (option2.equals("2")) {
                                    while (true) {
                                        System.out.println("Select series");
                                        for (Series s : imdb.series) {
                                            System.out.println(imdb.series.indexOf(s) + 1 + ") " + s.title);
                                        }
                                        String option3 = scanner.nextLine();
                                        int opt = Integer.parseInt(option3);
                                        if (opt > 0 && opt <= imdb.series.size()) {
                                            imdb.series.get(opt - 1).displayInfo();
                                            break;
                                        } else {
                                            System.out.println("Invalid movie! Please, try again");
                                            break;
                                        }
                                    }
                                } else if (option2.equals("3")) {
                                    break;
                                } else {
                                    System.out.println("Invalid option! Try again");
                                }
                            }
                        } else if (option1.equals("2")) {
                            while (true) {
                                System.out.println("Select actor");
                                for (Actor a : imdb.actor) {
                                    System.out.println(imdb.actor.indexOf(a) + 1 + ") " + a.name);
                                }
                                String option4 = scanner.nextLine();
                                int opt = Integer.parseInt(option4);
                                if (opt > 0 && opt <= imdb.actor.size()) {
                                    System.out.println("Name: " + imdb.actor.get(opt - 1).name);
                                    if (imdb.actor.get(opt - 1).biography != null) {
                                        System.out.println("Biography: " + imdb.actor.get(opt - 1).biography);
                                    }
                                    System.out.println("Ratings: " + imdb.actor.get(opt - 1).TotalRating);
                                    if (imdb.actor.get(opt - 1).roles.keySet() != null) {
                                        System.out.println("Performances: ");
                                        for (String key : imdb.actor.get(opt - 1).roles.keySet()) {
                                            System.out.println(key + " - " + imdb.actor.get(opt - 1).roles.get(key));
                                        }
                                    }
                                    break;
                                } else {
                                    System.out.println("Invalid actor! Please, try again");
                                    break;
                                }
                            }
                        } else if (option1.equals("3")) {
                            System.out.println("Notifications: ");
                            for (Object n : admUsr.notifications) {
                                System.out.println(n);
                            }
                        } else if (option1.equals("4")) {
                            while (true) {
                                System.out.println("1) Search for production");
                                System.out.println("2) Search for actor");
                                System.out.println("3) Previous page");
                                String option2 = scanner.nextLine();
                                if (option2.equals("1")) {
                                    while (true) {
                                        System.out.println("1) Search for movie");
                                        System.out.println("2) Search for series");
                                        System.out.println("3) Previous page");
                                        String option3 = scanner.nextLine();
                                        if (option3.equals("1")) {
                                            System.out.println("Enter movie");
                                            String option4 = scanner.nextLine();
                                            boolean ok = false;
                                            for (Movie m : imdb.movie) {
                                                if (m.title.equals(option4)) {
                                                    m.displayInfo();
                                                    ok = true;
                                                    break;
                                                }
                                            }
                                            if (!ok) {
                                                System.out.println("Movie doesn't exist");
                                            }
                                        } else if (option3.equals("2")) {
                                            System.out.println("Enter Series");
                                            String option5 = scanner.nextLine();
                                            boolean ok = false;
                                            for (Series s : imdb.series) {
                                                if (s.title.equals(option5)) {
                                                    s.displayInfo();
                                                    ok = true;
                                                    break;
                                                }
                                            }
                                            if (!ok) {
                                                System.out.println("Series doesn't exist");
                                            }
                                        } else if (option3.equals("3")) {
                                            break;
                                        } else {
                                            System.out.println("Invalid command! Try again");
                                        }
                                    }
                                } else if (option2.equals("2")) {
                                    System.out.println("Enter actor");
                                    String option3 = scanner.nextLine();
                                    boolean ok = false;
                                    for (Actor a : imdb.actor) {
                                        if (a.name.equals(option3)) {
                                            System.out.println("Name: " + a.name);
                                            System.out.println("Biography: " + a.biography);
                                            System.out.println("Performances: ");
                                            for (String key : a.roles.keySet()) {
                                                System.out.println(key + " - " + a.roles.get(key));
                                            }
                                            ok = true;
                                            break;
                                        }
                                    }
                                    if (!ok) {
                                        System.out.println("Actor doesn't exist");
                                    }
                                } else if (option2.equals("3")) {
                                    break;
                                } else {
                                    System.out.println("Invalid command! Try again");
                                }
                            }
                        } else if (option1.equals("5")) {
                            for (Object o : admUsr.favorites) {
                                System.out.println(o);
                            }
                        } else if (option1.equals("6")) {
                            while (true) {
                                System.out.println("1) Add to favorites");
                                System.out.println("2) Remove from favorites");
                                System.out.println("3) Previous page");
                                String option2 = scanner.nextLine();
                                if (option2.equals("1")) {
                                    String option3 = scanner.nextLine();
                                    boolean ok = false;
                                    for (Movie m : imdb.movie) {
                                        if (m.title.equals(option3)) {
                                            ok = true;
                                            admUsr.addFavorite(option3);
                                            break;
                                        }
                                    }
                                    if (!ok) {
                                        for (Series s : imdb.series) {
                                            if (s.title.equals(option3)) {
                                                ok = true;
                                                admUsr.addFavorite(option3);
                                                break;
                                            }
                                        }
                                    }
                                    if (!ok) {
                                        for (Actor a : imdb.actor) {
                                            if (a.name.equals(option3)) {
                                                ok = true;
                                                admUsr.addFavorite(option3);
                                                break;
                                            }
                                        }
                                    }
                                    if (!ok) {
                                        System.out.println("Couldn't find production/actor");
                                    }
                                } else if (option2.equals("2")) {
                                    String option3 = scanner.nextLine();
                                    boolean ok = false;
                                    for (Object o : admUsr.favorites) {
                                        if (o.equals(option3)) {
                                            admUsr.removeFavorite(option3);
                                            ok = true;
                                            break;
                                        }
                                    }
                                    if (!ok) {
                                        System.out.println("Couldn't find production/actor");
                                    }
                                } else if (option2.equals("3")) {
                                    break;
                                } else {
                                    System.out.println("Invalid command! Try again");
                                }
                            }
                        } else if (option1.equals("7")) {
                            for (Object o : admUsr.favorites) {
                                System.out.println(o);
                            }
                        } else if (option1.equals("8")) {
                            while (true) {
                                System.out.println("1) Add to favorites");
                                System.out.println("2) Remove from favorites");
                                System.out.println("3) Previous page");
                                String option2 = scanner.nextLine();
                                if (option2.equals("1")) {
                                    String option3 = scanner.nextLine();
                                    boolean ok = false;
                                    for (Movie m : imdb.movie) {
                                        if (m.title.equals(option3)) {
                                            ok = true;
                                            admUsr.addFavorite(option3);
                                            break;
                                        }
                                    }
                                    if (!ok) {
                                        for (Series s : imdb.series) {
                                            if (s.title.equals(option3)) {
                                                ok = true;
                                                admUsr.addFavorite(option3);
                                                break;
                                            }
                                        }
                                    }
                                    if (!ok) {
                                        for (Actor a : imdb.actor) {
                                            if (a.name.equals(option3)) {
                                                ok = true;
                                                admUsr.addFavorite(option3);
                                                break;
                                            }
                                        }
                                    }
                                    if (!ok) {
                                        System.out.println("Couldn't find production/actor");
                                    }
                                } else if (option2.equals("2")) {
                                    String option3 = scanner.nextLine();
                                    boolean ok = false;
                                    for (Object o : admUsr.favorites) {
                                        if (o.equals(option3)) {
                                            admUsr.removeFavorite(option3);
                                            ok = true;
                                            break;
                                        }
                                    }
                                    if (!ok) {
                                        System.out.println("Couldn't find production/actor");
                                    }
                                } else if (option2.equals("3")) {
                                    break;
                                } else {
                                    System.out.println("Invalid command! Try again");
                                }
                            }
                        } else if (option1.equals("9")) {
                            while (true) {
                                System.out.println("1) Add actor/production");
                                System.out.println("2) Delete actor/production");
                                System.out.println("3) Previous page");
                                String option2 = scanner.nextLine();
                                if (option2.equals("1")) {
                                    while (true) {
                                        System.out.println("1) Add actor");
                                        System.out.println("2) Add production");
                                        System.out.println("3) Previous page");
                                        String option3 = scanner.nextLine();
                                        if (option3.equals("1")) {
                                            while (true) {
                                                System.out.println("Introduce name");
                                                String name = scanner.nextLine();
                                                Map<String, String> map = new HashMap<>();
                                                while (true) {
                                                    System.out.println("Movie name:");
                                                    String key = scanner.nextLine();
                                                    if (key.equals("exit")) {
                                                        break;
                                                    }
                                                    System.out.println("Type:");
                                                    String value = scanner.nextLine();
                                                    map.put(key, value);
                                                }
                                                System.out.println("Introduce Biography");
                                                String bio = scanner.nextLine();
                                                Actor actor1 = new Actor(name, map, bio);
                                                imdb.actor.add(actor1);
                                            }
                                        } else if (option3.equals("2")) {
                                            while (true) {
                                                System.out.println("1) Add movie");
                                                System.out.println("2) Add series");
                                                System.out.println("3) Previous page");
                                                String option4 = scanner.nextLine();
                                                if (option4.equals("1")) {
                                                    System.out.println("Introduce title:");
                                                    String title = scanner.nextLine();
                                                    System.out.println("Introduce directors:");
                                                    List<String> Dir = new ArrayList<>();
                                                    while (true) {
                                                        String input = scanner.nextLine();
                                                        if (input.equals("exit")) {
                                                            break;
                                                        }
                                                        Dir.add(input);
                                                    }
                                                    System.out.println("Introduce actors:");
                                                    List<String> Act = new ArrayList<>();
                                                    while (true) {
                                                        String input = scanner.nextLine();
                                                        if (input.equals("exit")) {
                                                            break;
                                                        }
                                                        Act.add(input);
                                                    }
                                                    List<Genre> gen = new ArrayList<>();
                                                    while (true) {
                                                        System.out.println("Select genre:");
                                                        System.out.println("1) Action");
                                                        System.out.println("2) Comedy");
                                                        System.out.println("3) Drama");
                                                        System.out.println("4) Thriller");
                                                        String input = scanner.nextLine();
                                                        if (input.equals("1")) {
                                                            gen.add(Genre.Action);
                                                        }
                                                        if (input.equals("2")) {
                                                            gen.add(Genre.Comedy);
                                                        }
                                                        if (input.equals("3")) {
                                                            gen.add(Genre.Drama);
                                                        }
                                                        if (input.equals("4")) {
                                                            gen.add(Genre.Thriller);
                                                        }
                                                        if (input.equals("exit")) {
                                                            break;
                                                        }
                                                    }
                                                    System.out.println("Enter Description:");
                                                    String desc = scanner.nextLine();
                                                    System.out.println("Enter duration:");
                                                    String dur = scanner.nextLine();
                                                    Integer durint = Integer.parseInt(dur);
                                                    System.out.println("Enter release year");
                                                    String yr = scanner.nextLine();
                                                    Integer yrint = Integer.parseInt(yr);
                                                    Movie movie1 = new Movie(title, Dir, Act, gen, null, desc, durint, yrint);
                                                    imdb.movie.add(movie1);
                                                    ExprerienceStrategy xp = new addNewProductActorExperience();
                                                    admUsr.setExprerienceStrategy(xp);
                                                    admUsr.updateExperience();
                                                } else if (option4.equals("2")) {
                                                    System.out.println("Introduce title:");
                                                    String title = scanner.nextLine();
                                                    System.out.println("Introduce directors:");
                                                    List<String> Dir = new ArrayList<>();
                                                    while (true) {
                                                        String input = scanner.nextLine();
                                                        if (input.equals("exit")) {
                                                            break;
                                                        }
                                                        Dir.add(input);
                                                    }
                                                    System.out.println("Introduce actors:");
                                                    List<String> Act = new ArrayList<>();
                                                    while (true) {
                                                        String input = scanner.nextLine();
                                                        if (input.equals("exit")) {
                                                            break;
                                                        }
                                                        Act.add(input);
                                                    }
                                                    List<Genre> gen = new ArrayList<>();
                                                    while (true) {
                                                        System.out.println("Select genre:");
                                                        System.out.println("1) Action");
                                                        System.out.println("2) Comedy");
                                                        System.out.println("3) Drama");
                                                        System.out.println("4) Thriller");
                                                        String input = scanner.nextLine();
                                                        if (input.equals("1")) {
                                                            gen.add(Genre.Action);
                                                        }
                                                        if (input.equals("2")) {
                                                            gen.add(Genre.Comedy);
                                                        }
                                                        if (input.equals("3")) {
                                                            gen.add(Genre.Drama);
                                                        }
                                                        if (input.equals("4")) {
                                                            gen.add(Genre.Thriller);
                                                        }
                                                        if (input.equals("exit")) {
                                                            break;
                                                        }
                                                    }
                                                    System.out.println("Enter Description:");
                                                    String desc = scanner.nextLine();
                                                    System.out.println("Enter number of seasons:");
                                                    String dur = scanner.nextLine();
                                                    Integer durint = Integer.parseInt(dur);
                                                    System.out.println("Enter release year");
                                                    String yr = scanner.nextLine();
                                                    Integer yrint = Integer.parseInt(yr);
                                                    Map<String, List<Episode>> episodes = new HashMap<>();
                                                    System.out.println("Introduce episodes:");
                                                    while (true) {
                                                        System.out.print("Episode title:");
                                                        String titleep = scanner.nextLine();
                                                        if (titleep.equals("exit")) {
                                                            break;
                                                        }
                                                        System.out.print("Introduce episode duration:");
                                                        int duration = Integer.parseInt(scanner.nextLine());
                                                        Episode episode = new Episode(titleep, duration);
                                                        episode.title = title;
                                                        episode.Duration = duration;
                                                        System.out.print("Introduce season:");
                                                        String seasonEpisode = scanner.nextLine();
                                                        episodes.computeIfAbsent(seasonEpisode, k -> new ArrayList<>()).add(episode);
                                                    }
                                                    Series series1 = new Series(title, Dir, Act, gen, null, desc, yrint, durint, episodes);
                                                    imdb.series.add(series1);
                                                    ExprerienceStrategy xp = new addNewProductActorExperience();
                                                    admUsr.setExprerienceStrategy(xp);
                                                    admUsr.updateExperience();
                                                } else if (option4.equals("3")) {
                                                    break;
                                                } else {
                                                    System.out.println("Invalid command");
                                                }
                                            }
                                        } else if (option3.equals("3")) {
                                            break;
                                        } else {
                                            System.out.println("Invalid command");
                                        }
                                    }
                                } else if (option2.equals("2")) {
                                    System.out.println("Type the production:");
                                    String option3 = scanner.nextLine();
                                    boolean ok = false;
                                    for (Actor a : imdb.actor) {
                                        if (a.name.equals(option3)) {
                                            imdb.actor.remove(a);
                                            ExprerienceStrategy xp = new removeProductActorExperience();
                                            admUsr.setExprerienceStrategy(xp);
                                            admUsr.updateExperience();
                                            ok = true;
                                            break;
                                        }
                                    }
                                    if (!ok) {
                                        for (Movie m : imdb.movie) {
                                            if (m.title.equals(option3)) {
                                                imdb.actor.remove(m);
                                                ExprerienceStrategy xp = new removeProductActorExperience();
                                                admUsr.setExprerienceStrategy(xp);
                                                admUsr.updateExperience();
                                                ok = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (!ok) {
                                        for (Series s : imdb.series) {
                                            if (s.title.equals(option3)) {
                                                imdb.actor.remove(s);
                                                ExprerienceStrategy xp = new removeProductActorExperience();
                                                admUsr.setExprerienceStrategy(xp);
                                                admUsr.updateExperience();
                                                ok = true;
                                                break;
                                            }
                                        }
                                    }
                                } else if (option2.equals("3")) {
                                    break;
                                } else {
                                    System.out.println("Invalid command");
                                }
                            }
                        } else if (option1.equals("10")) {
                            System.out.println("Type the username:");
                            String option2 = scanner.nextLine();
                            boolean ok = false;
                            for (Regular r : imdb.regular) {
                                if (r.username.equals(option2)) {
                                    imdb.regular.remove(r);
                                    ok = true;
                                    break;
                                }
                            }
                            for (Contributor c : imdb.contributor) {
                                if (c.username.equals(option2)) {
                                    imdb.contributor.remove(c);
                                    ok = true;
                                    break;
                                }
                            }
                            if (ok == false) {
                                System.out.println("Invalid username");
                            }
                        } else if (option1.equals("11")) {
                            System.out.println("These are requests for you:");
                            for (Object r : admUsr.r) {
                                System.out.println(r);
                                System.out.println("Resolve this request?");
                                String option2 = scanner.nextLine();
                                if (option2.equals("yes")) {
                                    for (Regular r2 : imdb.regular) {
                                        if (((Request) r).username_requester.equals(r2.username)) {
                                            ExprerienceStrategy xp = new addRequestExperience();
                                            r2.setExprerienceStrategy(xp);
                                            r2.updateExperience();
                                            r2.notifications.add("Request solved");
                                            admUsr.r.remove(r);
                                            break;
                                        }
                                    }
                                    for (Contributor c2 : imdb.contributor) {
                                        if (((Request) r).username_requester.equals(c2.username)) {
                                            ExprerienceStrategy xp = new addRequestExperience();
                                            c2.setExprerienceStrategy(xp);
                                            c2.updateExperience();
                                            c2.notifications.add("Request solved");
                                            admUsr.r.remove(r);
                                            break;
                                        }
                                    }
                                } else if (option2.equals("no")) {
                                    continue;
                                } else {
                                    System.out.println("Invalid command");
                                }
                            }
                            System.out.println("These are requests for all admins");
                            for (Request r : RequestsHolder.requests) {
                                System.out.println(r);
                                System.out.println("Resolve this request?");
                                String option2 = scanner.nextLine();
                                if (option2.equals("yes")) {
                                    RequestsHolder.requests.remove(r);
                                } else if (option2.equals("no")) {
                                    continue;
                                } else {
                                    System.out.println("Invalid command");
                                }
                            }
                        } else if (option1.equals("12")) {
                            isAutentificated = false;
                            admUsr.logOut();
                        } else {
                            System.out.println("Invalid command!");
                        }
                    }
                }
            }
        } else {
            System.out.println("Invalid command!");
        }
    }
}