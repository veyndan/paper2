package com.veyndan.generic;

import java.util.List;

public class Post {
    private final String name, date, visibility, pins;
    private final int profile;
    private List<Description> descriptions;

    public Post(String name, String date, String visiblity, String pins, int profile, List<Description> descriptions) {
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

    public int getProfile() {
        return profile;
    }

    public List<Description> getDescriptions() {
        return descriptions;
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
