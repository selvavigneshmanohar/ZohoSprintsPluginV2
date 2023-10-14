package io.jenkins.plugins.model;

public class FeedStatus extends BaseModel {
    private String feed;

    private FeedStatus(String prefix, String feed) {
        super(prefix);
        this.feed = feed;
    }

    public String getFeed() {
        return getValue(feed);
    }

    public static FeedStatus getInstance(String prefix, String feed) {
        return new FeedStatus(prefix, feed);
    }
}
