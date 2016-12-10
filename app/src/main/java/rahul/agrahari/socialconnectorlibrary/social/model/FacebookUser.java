package rahul.agrahari.socialconnectorlibrary.social.model;

import rahul.agrahari.socialconnectorlibrary.social.socialinterface.SocialUser;

/**
 * This class show the basic information of facebook user
 * which is logged in this app.and implements {@link SocialUser}
 * {@link FacebookUser#first_name} show the firstname of facebook user.
 * {@link FacebookUser#last_name} show the lastname of facebook user.
 * {@link FacebookUser#id} gives the facebook id the facebook user.
 * {@link FacebookUser#link} gives the profile picture of  the facebook user.
 * {@link FacebookUser#email} gives the email id the facebook user.
 *
 * Created by Rahul Agrahari on 10/10/2016.
 * @version 1.0
 */
public class FacebookUser implements SocialUser {
    private String last_name;
    private String id;
    private String gender;
    private String first_name;
    private String name;
    private String link;
    private String email;

    public FacebookUser(String first_name, String last_name, String id, String link, String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.id = id;
        this.link = link;
        this.email = email;
    }

    private PictureBean picture;

    public PictureBean getPicture() {
        return picture;
    }

    public void setPicture(PictureBean picture) {
        this.picture = picture;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class PictureBean {
        /**
         * url : https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=f2a67d28b2c5825839d0bf36c6194d65&oe=57FC792F
         * is_silhouette : true
         */

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            private String url;
            private boolean is_silhouette;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public boolean isIs_silhouette() {
                return is_silhouette;
            }

            public void setIs_silhouette(boolean is_silhouette) {
                this.is_silhouette = is_silhouette;
            }
        }
    }

    @Override
    public String getUserFirstName() {
        return first_name;
    }

    @Override
    public String getUserLastName() {
        return last_name;
    }

    @Override
    public String getUserId() {
        return id;
    }

    @Override
    public String getUserEmail() {
        return email;
    }

    @Override
    public String getUserImage() {
        if (getPicture() == null)
            return link;
        else
            return getPicture().getData().getUrl() + "";
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
