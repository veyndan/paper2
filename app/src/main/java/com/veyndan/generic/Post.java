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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (name != null ? !name.equals(post.name) : post.name != null) return false;
        if (date != null ? !date.equals(post.date) : post.date != null) return false;
        if (visibility != null ? !visibility.equals(post.visibility) : post.visibility != null)
            return false;
        if (pins != null ? !pins.equals(post.pins) : post.pins != null) return false;
        if (profile != null ? !profile.equals(post.profile) : post.profile != null) return false;
        return descriptions != null ? descriptions.equals(post.descriptions) : post.descriptions == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (visibility != null ? visibility.hashCode() : 0);
        result = 31 * result + (pins != null ? pins.hashCode() : 0);
        result = 31 * result + (profile != null ? profile.hashCode() : 0);
        result = 31 * result + (descriptions != null ? descriptions.hashCode() : 0);
        return result;
    }

    public static class Description {
        public static final int TYPE_PARAGRAPH = 0;
        public static final int TYPE_IMAGE = 1;

        private String body;
        private int type;

        public Description() {

        }

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Description that = (Description) o;

            if (type != that.type) return false;
            return body != null ? body.equals(that.body) : that.body == null;

        }

        @Override
        public int hashCode() {
            int result = body != null ? body.hashCode() : 0;
            result = 31 * result + type;
            return result;
        }
    }
}
