package rahul.agrahari.socialconnectorlibrary.social.socialinterface;

import java.util.List;

import rahul.agrahari.socialconnectorlibrary.social.Utils.SocialConnection;

/**
 * This interface gives a callback method which is call after perform action on google,facebook and twiiter
 *
 * Created by Rahul Agrahari on 10/11/2016.
 * @version 1.0
 */
public interface ConnectionCallBack {
    /** this method call after successfully login on facebook , google and twitter  and gives user information.
     *
     * @param user
     * @param socialConnection
     */
    void connectionSuccess(SocialUser user, SocialConnection socialConnection);

    /** this method is called if connection failed with facebook, google and twitter.
     *
     * @param error
     * @param socialConnection
     */
    void connectionFailed(String error, SocialConnection socialConnection);

    /** this method is called after successfully share post on facebook, google plus, twitter
     *
     * @param message
     */
    void postSuccess(String message);

    /**this method is called if any error occur.
     *
     * @param errormessage
     */
    void error(String errormessage);
}
