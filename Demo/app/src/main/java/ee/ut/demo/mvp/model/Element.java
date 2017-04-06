package ee.ut.demo.mvp.model;

/**
 * Created by Bilal Abdullah on 4/5/2017.
 */

public class Element {
    private final String id;
    private final String title;
    private final String updatedAt;
    private boolean shouldUpdate;

    public Element(String id, String title, String updatedAt, boolean shouldUpdate) {
        this.id = id;
        this.title = title;
        this.updatedAt = updatedAt;
        this.shouldUpdate = shouldUpdate;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public boolean isShouldUpdate() {
        return shouldUpdate;
    }

    public void setShouldUpdate(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }
}
