package rahul.agrahari.socialconnectorlibrary.social.model;

import rahul.agrahari.socialconnectorlibrary.social.socialinterface.SocialUser;

/**
 * This class {@link TwitterUser}show the basic information of Twitter user
 * which is logged in this app.
 * {@link TwitterUser#first_name} show the firstname of Twitter user.
 * {@link TwitterUser#last_name} show the lastname of Twitter user.
 * {@link TwitterUser#id} gives the facebook id the Twitter user.
 * {@link TwitterUser#link} gives the profile picture of  the Twitter user.
 * {@link TwitterUser#email} gives the email id the Twitter user.
 *
 */

public class TwitterUser implements SocialUser {

    private String last_name;
    private String id;
    private String gender;
    private String first_name;
    private String name;
    private String link;
    private String email;

    public TwitterUser(String last_name, String id, String gender, String first_name, String name, String link, String email) {
        this.last_name = last_name;
        this.id = id;
        this.gender = gender;
        this.first_name = first_name;
        this.name = name;
        this.link = link;
        this.email = email;
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

    @Override
    public String getUserId() {
        return id;
    }


    @Override
    public String getUserEmail() {
        return email;
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
    public String getUserImage() {
        return link;
    }
}
