public class Episode {
    String title;
    int Duration;

    public Episode(String title, int duration) {
        this.title = title;
        this.Duration = duration;

    }
    public void displayInfo() {
        System.out.println("Title: " + this.title);
        System.out.println("Duration: " + this.Duration);
    }
    public String getTitle() {
        return this.title;
    }
    public Integer getDuration() {
        return this.Duration;
    }
}
