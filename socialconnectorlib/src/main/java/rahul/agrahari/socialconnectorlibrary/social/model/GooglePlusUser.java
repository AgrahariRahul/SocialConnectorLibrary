package rahul.agrahari.socialconnectorlibrary.social.model;


import rahul.agrahari.socialconnectorlibrary.social.socialinterface.SocialUser;

/**
 * This class {@link GooglePlusUser}show the basic information of Google Plus user
 * which is logged in this app.
 * {@link GooglePlusUser#first_name} show the firstname of Google Plus user.
 * {@link GooglePlusUser#last_name} show the lastname of Google Plus user.
 * {@link GooglePlusUser#id} gives the facebook id the Google Plus user.
 * {@link GooglePlusUser#link} gives the profile picture of  the Google Plus user.
 * {@link GooglePlusUser#email} gives the email id the Google Plus user.
 *
 *
 * Created by Rahul Agrahari on 10/10/2016.
 * @version 1.0
 */
public class GooglePlusUser implements SocialUser {

    private String first_name;
    private String last_name;
    private String id;
    private String link;
    private String email;

    public GooglePlusUser(String first_name, String last_name, String id, String link, String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.id = id;
        this.link = link;
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
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
    public String getUserFirstName() {
        return first_name;
    }

    @Override
    public String getUserEmail() {
        return email;
    }

    @Override
    public String getUserId() {
        return id;
    }

    @Override
    public String getUserImage() {
        return link;
    }

    @Override
    public String getUserLastName() {
        return last_name;
    }
}
