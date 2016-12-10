package rahul.agrahari.socialconnectorlibrary.social.connection;

import android.content.Intent;
import android.support.annotation.Nullable;

import rahul.agrahari.socialconnectorlibrary.social.SocialConnector;
import rahul.agrahari.socialconnectorlibrary.social.SocialConnector.SocialBuilder;
import rahul.agrahari.socialconnectorlibrary.social.Utils.SocialConnection;

/**
 * Created by Rahul Agrahari on 10/11/2016.
 */
public class ConnectionHelper {

    private SocialConnector mSocialConnector;
    private ConnectGPlus connectGPlus;
    private ConnectFacebook connectFacebook;
    private ConnectTwitter connectTwitter;

    public ConnectionHelper(SocialConnector socialConnector) {
        this.mSocialConnector = socialConnector;
        if (mSocialConnector.getConnection() == SocialConnection.FACEBOOK) {
            facebookConnection();
        }
        if (mSocialConnector.getConnection() == SocialConnection.GPLUS) {
            gPlusConnection();
        }

        if (mSocialConnector.getConnection() == SocialConnection.TWITTER) {
            twitterConnection();
        }
    }

    private void facebookConnection() {
        connectFacebook = new ConnectFacebook(mSocialConnector);
        connectFacebook.connect();
    }

    private void gPlusConnection() {
        connectGPlus = new ConnectGPlus(mSocialConnector);
        connectGPlus.connect();
    }

    private void twitterConnection() {
        connectTwitter = new ConnectTwitter(mSocialConnector);
        connectTwitter.connect();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSocialConnector.getConnection() == SocialConnection.FACEBOOK) {
            connectFacebook.onActivityResult(requestCode, resultCode, data);
        }

        if (mSocialConnector.getConnection() == SocialConnection.GPLUS) {
            connectGPlus.onActivityResult(requestCode, resultCode, data);
        }
        if (mSocialConnector.getConnection() == SocialConnection.TWITTER) {
            //connectTwitter.onActivityResult(requestCode, resultCode, data);
        }
    }
}
