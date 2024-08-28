import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class User <T extends Comparable<T>> {
    public Information info;
    public AccountType accountType;
    public String username;
    public int experience;
    public List<String> notifications = new ArrayList<>();
    public SortedSet<T> favorites = new TreeSet<>();

    public User(Information info, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorites) {
        this.info = info;
        this.accountType = accountType;
        this.username = username;
        this.experience = experience;
        this.notifications = notifications;
        this.favorites = favorites;
    }
    public abstract void addFavorite(T favorite);
    public abstract void removeFavorite(T favorite);
    public abstract void updateExperience();
    public abstract void logOut();
    public static class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private String gender;
        private LocalDate birthdate;
        public Information (Credentials credentials, String name, String country, int age, String gender, LocalDate birthdate) {
            this.credentials = credentials;
            this.name = name;
            this.country = country;
            this.age = age;
            this.gender = gender;
            this.birthdate = birthdate;
        }

        public Credentials getCredentials() {
            return credentials;
        }
        public static class InformationBuilder {
            private Credentials credentials;
            private String name;
            private String country;
            private int age;
            private String gender;
            private LocalDate birthdate;
            public InformationBuilder() {

            }
            public InformationBuilder setCredentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }
            public InformationBuilder setName(String name) {
                this.name = name;
                return this;
            }
            public InformationBuilder setCountry(String country) {
                this.country = country;
                return this;
            }
            public InformationBuilder setAge(int age) {
                this.age = age;
                return this;
            }
            public InformationBuilder setGender(String gender) {
                this.gender = gender;
                return this;
            }
            public InformationBuilder setBirthdate(LocalDate birthdate) {
                this.birthdate = birthdate;
                return this;
            }
            public Information build() {
                return new Information(this.credentials, this.name, this.country, this.age, this.gender, this.birthdate);
            }
        }
    }
}
