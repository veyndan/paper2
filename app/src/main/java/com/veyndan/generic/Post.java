package com.veyndan.generic;

import java.util.List;

public class Post {
    private String name;
    private String date;
    private String visibility;
    private String pins;
    private String profile;
    private List<Description> descriptions;

    public Post() {

    }

    public Post(String name, String date, String visiblity, String pins, String profile, List<Description> descriptions) {
        this.name = name;
        this.date = date;
        this.visibility = visiblity;
        this.pins = pins;
        this.profile = profile;
        this.descriptions = descriptions;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getPins() {
        return pins;
    }

    public String getProfile() {
        return profile;
    }

    public List<Description> getDescriptions() {
        return descriptions;
    }

    @Override
    public String toString() {
        return "Post{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", visibility='" + visibility + '\'' +
                ", pins='" + pins + '\'' +
                ", profile='" + profile + '\'' +
                ", descriptions=" + descriptions +
                '}';
    }

    public static class Description {
        public static final int TYPE_PARAGRAPH = 0;
        public static final int TYPE_IMAGE = 1;

        private String body;
        private int type;

        public Description(String body, int type) {
            this.body = body;
            this.type = type;
        }

        public String getBody() {
            return body;
        }

        public int getType() {
            return type;
        }
    }
}
