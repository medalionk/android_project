package ee.ut.demo.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bilal Abdullah on 3/18/2017.
 */

public class Details implements Parcelable {

    private final String description;
    private final String organizer;
    private final String additionalInfo;
    private final String songBook;
    private final String imageUrl;
    private final String ticket;
    private final String publicUrl;
    private final String date;

    private Details(Builder builder){
        description = builder.description;
        organizer = builder.organizer;
        additionalInfo = builder.additionalInfo;
        songBook = builder.songBook;
        imageUrl = builder.imageUrl;
        ticket = builder.ticket;
        publicUrl = builder.publicUrl;
        date = builder.date;
    }

    protected Details(Parcel in) {

        description = in.readString();
        organizer = in.readString();
        additionalInfo = in.readString();
        songBook = in.readString();
        imageUrl = in.readString();
        ticket = in.readString();
        publicUrl = in.readString();
        date = in.readString();
    }

    public static final Creator<Details> CREATOR = new Creator<Details>() {
        @Override
        public Details createFromParcel(Parcel in) {
            return new Details(in);
        }

        @Override
        public Details[] newArray(int size) {
            return new Details[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public String getSongBook() {
        return songBook;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTicket() {
        return ticket;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(organizer);
        dest.writeString(additionalInfo);
        dest.writeString(songBook);
        dest.writeString(imageUrl);
        dest.writeString(ticket);
        dest.writeString(publicUrl);
        dest.writeString(date);
    }

    public static class Builder{

        private String description;
        private String organizer;
        private String additionalInfo;
        private String songBook;
        private String imageUrl;
        private String ticket;
        private String publicUrl;
        private String date;

        public Builder (){

        }

        public Builder description(String description){
            this.description = description;
            return this;
        }

        public Builder organizer(String organizer){
            this.organizer = organizer;
            return this;
        }

        public Builder additionalInfo(String additionalInfo){
            this.additionalInfo = additionalInfo;
            return this;
        }

        public Builder songBook(String songBook){
            this.songBook = songBook;
            return this;
        }

        public Builder imageUrl(String imageUrl){
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder ticket(String ticket){
            this.ticket = ticket;
            return this;
        }
        public Builder publicUrl(String publicUrl){
            this.publicUrl = publicUrl;
            return this;
        }

        public Builder date(String date){
            this.date = date;
            return this;
        }

        public Details build(){
            return new Details(this);
        }
    }
}
