package gr.uth.songbuddies.model;

public class Song {

    private	int	id;
    private	String name;
    private	String artist;
    private	String genre;
    private	double duration;
    private	int rate;
    private	String description;
    private	String link;

    public Song(int id, String name, String artist, String genre, double duration, int rate, String description, String link) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
        this.rate = rate;
        this.description = description;
        this.link = link;
    }


    public Song(String name, String artist, String genre, double duration, int rate, String description, String link) {
        this.name = name;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
        this.rate = rate;
        this.description = description;
        this.link = link;
    }

    public Song(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getRate() { return rate; }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


}
