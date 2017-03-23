package ee.ut.demo.mvp.model;

public class Event {
    private final int id;
    private final String time;
    private final String title;
    private final String location;
    private final String ticket;
    private final String url;


    private final boolean favorite;
    private final Details details;

    private Event(Builder builder){
        id = builder.id;
        time = builder.time;
        title = builder.title;
        location = builder.location;
        ticket = builder.ticket;
        url = builder.url;
        favorite = builder.favorite;
        details = builder.details;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getTicket() {
        return ticket;
    }

    public String getUrl() {
        return url;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public Details getDetails() {
        return details;
    }

    public static class Builder{
        // Required parameters
        private final String title;

        // Optional parameters
        private int id;
        private String time;
        private String location;
        private String ticket;
        private String url;
        private boolean favorite;
        private Details details;

        public Builder (String title){
            this.title = title;
        }

        public Builder id(int id){
            this.id = id;
            return this;
        }

        public Builder time(String time){
            this.time = time;
            return this;
        }

        public Builder location(String location){
            this.location = location;
            return this;
        }

        public Builder ticket(String ticket){
            this.ticket = ticket;
            return this;
        }

        public Builder url(String url){
            this.url = url;
            return this;
        }

        public Builder favorite(int favorite){
            this.favorite = favorite == 1;
            return this;
        }

        public Builder details(Details details){
            this.details = details;
            return this;
        }

        public Event build(){
            return new Event(this);
        }
    }
}
