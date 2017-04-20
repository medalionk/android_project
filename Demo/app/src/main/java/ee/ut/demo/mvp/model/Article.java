package ee.ut.demo.mvp.model;

/**
 * Created by Bilal Abdullah on 4/19/2017.
 */

public class Article {
    private final String id;
    private final String title;
    private final String excerpt;
    private final String imageUrl;
    private final String publicUrl;

    private Article(Builder builder){
        id = builder.id;
        excerpt = builder.excerpt;
        title = builder.title;
        imageUrl = builder.imageUrl;
        publicUrl = builder.publicUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public static class Builder{
        // Required parameters
        private final String title;

        // Optional parameters
        private String id;
        private String excerpt;
        private String imageUrl;
        private String publicUrl;

        public Builder (String title){
            this.title = title;
        }

        public Builder id(String id){
            this.id = id;
            return this;
        }

        public Builder excerpt(String excerpt){
            this.excerpt = excerpt;
            return this;
        }

        public Builder imageUrl(String imageUrl){
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder publicUrl(String publicUrl){
            this.publicUrl = publicUrl;
            return this;
        }

        public Article build(){
            return new Article(this);
        }
    }
}
