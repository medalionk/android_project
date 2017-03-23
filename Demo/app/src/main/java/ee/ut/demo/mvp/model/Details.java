package ee.ut.demo.mvp.model;

/**
 * Created by Bilal Abdullah on 3/18/2017.
 */

public class Details {
    private final String description;
    private final String organizer;
    private final String additionalInfo;
    private final String songBook;
    private final String imageUrl;

    private Details(Builder builder){
        description = builder.description;
        organizer = builder.organizer;
        additionalInfo = builder.additionalInfo;
        songBook = builder.songBook;
        imageUrl = builder.imageUrl;
    }

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

    public static class Builder{

        private String description;
        private String organizer;
        private String additionalInfo;
        private String songBook;
        private String imageUrl;

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

        public Details build(){
            return new Details(this);
        }
    }
}
