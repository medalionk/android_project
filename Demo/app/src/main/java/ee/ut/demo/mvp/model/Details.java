package ee.ut.demo.mvp.model;

/**
 * Created by Bilal Abdullah on 3/18/2017.
 */

public class Details {
    private final String description;
    private final String organizer;
    private final String additionalInfo;
    private final String songBook;
    private final String imagePath;

    private Details(Builder builder){
        description = builder.description;
        organizer = builder.organizer;
        additionalInfo = builder.additionalInfo;
        songBook = builder.songBook;
        imagePath = builder.imagePath;
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

    public String getImagePath() {
        return imagePath;
    }

    public static class Builder{
        // Required parameters
        private final String description;

        // Optional parameters
        private String organizer;
        private String additionalInfo;
        private String songBook;
        private String imagePath;

        public Builder (String description){
            this.description = description;
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

        public Builder imagePath(String imagePath){
            this.imagePath = imagePath;
            return this;
        }

        public Details build(){
            return new Details(this);
        }
    }
}
