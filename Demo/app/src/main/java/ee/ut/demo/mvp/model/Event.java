package ee.ut.demo.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {

    private final String id;
    private final String startTime;
    private final String endTime;
    private final String title;
    private final String location;
    private final String updatedAt;

    private boolean favorite;
    private final Details details;

    private Event(Builder builder){
        id = builder.id;
        startTime = builder.startTime;
        title = builder.title;
        location = builder.location;
        updatedAt = builder.updatedAt;
        endTime = builder.endTime;
        favorite = builder.favorite;
        details = builder.details;
    }

    protected Event(Parcel in) {
        id = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        title = in.readString();
        location = in.readString();
        updatedAt = in.readString();
        favorite = in.readByte() != 0;
        details = in.readParcelable(Details.class.getClassLoader());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Details getDetails() {
        return details;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(title);
        dest.writeString(location);
        dest.writeString(updatedAt);
        dest.writeParcelable(details, 1);
    }

    public static class Builder{
        // Required parameters
        private final String title;

        // Optional parameters
        private String id;
        private String startTime;
        private String location;
        private String endTime;
        private String updatedAt;
        private boolean favorite;
        private Details details;

        public Builder (String title){
            this.title = title;
        }

        public Builder id(String id){
            this.id = id;
            return this;
        }

        public Builder startTime(String startTime){
            this.startTime = startTime;
            return this;
        }

        public Builder location(String location){
            this.location = location;
            return this;
        }

        public Builder endTime(String ticket){
            this.endTime = ticket;
            return this;
        }

        public Builder updatedAt(String updatedAt){
            this.updatedAt = updatedAt;
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
