package rahul.agrahari.socialconnectorlibrary.social.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;

import rahul.agrahari.socialconnectorlibrary.R;
import rahul.agrahari.socialconnectorlibrary.social.SocialConnector;
import rahul.agrahari.socialconnectorlibrary.social.Utils.ConnectionType;
import rahul.agrahari.socialconnectorlibrary.social.Utils.PrefUtils;
import rahul.agrahari.socialconnectorlibrary.social.Utils.SocialConnection;
import rahul.agrahari.socialconnectorlibrary.social.model.TwitterUser;
import rahul.agrahari.socialconnectorlibrary.social.socialinterface.ConnectionCallBack;
import rahul.agrahari.socialconnectorlibrary.social.socialinterface.TwiiterAppInvite;
import rahul.agrahari.socialconnectorlibrary.social.socialinterface.TwitterAuthCallback;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * This class provide the connection from twitter and perform login
 * share and appinvite on twitter
 * <p/>
 * <p/>
 * Created by Rahul Agrahari on 24/11/16.
 *
 * @version 1.0
 */

public class ConnectTwitter {

    private SocialConnector mSocialConnector;

    private Context mContext;

    private ConnectionCallBack mConnectionCallBack;

    private SocialConnection mSocialConnection;

    private ConnectionType mConnectionType;

    private String mConsumerKey;

    private String mConsumerSecret;

    private Twitter twitter;

    private String mCaption;

    private String mDescription;

    private String mUrl;

    private String mPackageName;

    private TwitterAuthDialog twitterDialog;

    private TwitterAppInviteDialog twitterAppInviteDialog;

    private int mResourceid;

    private String mAppName;

    private String mAppLinkUrl;


    private RequestToken requestToken;

    public static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
    private static final String URL_TWITTER_AUTH = "auth_url";
    private static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    private static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    private static final String TOKEN_ID = "tokenid";
    private static final String TOKEN_SECRET = "tokensecret";
    private static final int REQ_LOGIN = 0x16;

    public ConnectTwitter(SocialConnector socialConnector) {
        this.mSocialConnector = socialConnector;
        this.mContext = mSocialConnector.getContext();
        this.mConnectionCallBack = mSocialConnector.getConnectionCallBack();
        this.mSocialConnection = mSocialConnector.getConnection();
        this.mConnectionType = mSocialConnector.getConnectionType();
        this.mConsumerKey = mSocialConnector.getConsumerKey();
        this.mConsumerSecret = mSocialConnector.getConsumerSecret();
        this.mUrl = mSocialConnector.getUrl();
        this.mAppLinkUrl = mSocialConnector.getAppLinkUrl();
        this.mCaption = mSocialConnector.getCaption();
        this.mDescription = mSocialConnector.getDescription();
        this.mPackageName = mSocialConnector.getPackageName();
        this.mAppName = mSocialConnector.getAppName();
        this.mResourceid = mSocialConnector.getResourceid();
    }


    private Twitter getTwitterInstance() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(mConsumerKey);
        configurationBuilder.setOAuthConsumerSecret(mConsumerSecret);
        configurationBuilder.setOAuthAccessToken(PrefUtils.getPrefInstance(mContext).getPrefString(TOKEN_ID));
        configurationBuilder.setOAuthAccessTokenSecret(PrefUtils.getPrefInstance(mContext).getPrefString(TOKEN_SECRET));
        Configuration configuration = configurationBuilder.build();
        Twitter twitter = new TwitterFactory(configuration).getInstance();
        return twitter;
    }

    public void connect() {
        if (mConnectionType == ConnectionType.LOGIN)
            twitterLoginTask.execute();
        else if (mConnectionType == ConnectionType.POSTSHARE) {
            if (!PrefUtils.getPrefInstance(mContext).getPrefString(TOKEN_ID).equals(""))
                tweetAsyncTask.execute();
            else
                twitterLoginTask.execute();
        } else if (mConnectionType == ConnectionType.APPINVITE) {
            if (!PrefUtils.getPrefInstance(mContext).getPrefString(TOKEN_ID).equals("")) {
                showAppInviteDialog();
            } else
                twitterLoginTask.execute();
        }
    }


    /**
     * This method show the Twitter App invite dialog
     */
    private void showAppInviteDialog() {
        if (mResourceid == 0)
            throw new IllegalArgumentException("Resource Id is null");
        twitterAppInviteDialog = new TwitterAppInviteDialog(mContext, mResourceid, mAppName, mDescription, twiiterAppInvite);
        twitterAppInviteDialog.show();
    }


    /**
     * This method setup the configration builder of twitter and get the authorization url of twitter
     */
    private String twitterLogin() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(mConsumerKey);
        configurationBuilder.setOAuthConsumerSecret(mConsumerSecret);
        Configuration configuration = configurationBuilder.build();
        TwitterFactory factory = new TwitterFactory(configuration);
        twitter = factory.getInstance();
        try {
            requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
            if (requestToken != null)
                return requestToken.getAuthorizationURL();
            else
                return "";

        } catch (TwitterException e) {
            e.printStackTrace();
            if (mConnectionCallBack != null)
                mConnectionCallBack.error(e.getMessage());
        }
        return "";
    }


    /**
     * This anonymous class perform the login on twitter
     */
    private AsyncTask<Void, Void, String> twitterLoginTask = new AsyncTask<Void, Void, String>() {
        @Override
        protected String doInBackground(Void... params) {
            return twitterLogin();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equals("")) {
                twitterDialog = new TwitterAuthDialog(mContext, s, getTwitterAuthCallback());
                twitterDialog.show();
            }
        }
    };

    /**
     * This class provide the access token of twitter.
     */
    private AsyncTask<String, Void, User> accessTokenTask = new AsyncTask<String, Void, User>() {
        @Override
        protected User doInBackground(String... params) {
            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
                if (accessToken == null)
                    return null;
                PrefUtils.getPrefInstance(mContext).savePrefString(TOKEN_ID, accessToken.getToken());
                PrefUtils.getPrefInstance(mContext).savePrefString(TOKEN_SECRET, accessToken.getTokenSecret());
                User user = twitter.showUser(accessToken.getUserId());
                return user;
            } catch (TwitterException e) {
                e.printStackTrace();
                if (mConnectionCallBack != null)
                    mConnectionCallBack.error(e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (mConnectionType == ConnectionType.LOGIN) {
                if (user != null) {
                    TwitterUser twitterUser = new TwitterUser("", String.valueOf(user.getId()), "", user.getName(), user.getScreenName(), user.getOriginalProfileImageURL(), "");
                    if (mConnectionCallBack == null)
                        throw new IllegalArgumentException("ConnectionCallback is null");
                    mConnectionCallBack.connectionSuccess(twitterUser, SocialConnection.TWITTER);
                }
            } else if (mConnectionType == ConnectionType.POSTSHARE) {
                tweetAsyncTask.execute();
            } else if (mConnectionType == ConnectionType.APPINVITE) {
                showAppInviteDialog();
            }
        }
    };

    /**
     * This method perform tweet on twiiter.
     *
     * @return
     */
    private boolean tweetPost() {
        boolean isTweet;
        if (mCaption == null || mDescription == null)
            throw new IllegalArgumentException("Post message is empty or null.");
        Twitter twitter = getTwitterInstance();
        try {
            StatusUpdate statusUpdate = new StatusUpdate(mCaption + "\n" + mDescription);
            statusUpdate.setInReplyToStatusId(System.currentTimeMillis());
            if (getMediaFile() != null) {
                statusUpdate.setMedia(getMediaFile());
            }
            twitter.updateStatus(statusUpdate);
            isTweet = true;
        } catch (TwitterException e) {
            e.printStackTrace();
            isTweet = false;
            if (mConnectionCallBack != null)
                mConnectionCallBack.error(e.getMessage());
        }
        return isTweet;
    }


    /**
     * This anonymous class perform the tweet on twitter and return the callback result.
     */
    private AsyncTask<Void, Void, Boolean> tweetAsyncTask = new AsyncTask<Void, Void, Boolean>() {
        @Override
        protected Boolean doInBackground(Void... params) {
            return tweetPost();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                if (mConnectionCallBack == null)
                    throw new IllegalArgumentException("ConnectionCallback is null");
                else
                    mConnectionCallBack.postSuccess("Success");
            }
        }
    };


    private boolean appInvite() {
        boolean isInvite;
        Twitter twitter = getTwitterInstance();
        try {
            StatusUpdate statusUpdate = new StatusUpdate("You can install this app from" + mAppLinkUrl);
            if (getMediaFile() != null) {
                statusUpdate.setMedia(getMediaFile());
            }
            statusUpdate.setInReplyToStatusId(System.currentTimeMillis());
            twitter.updateStatus(statusUpdate);
            isInvite = true;
        } catch (TwitterException e) {
            e.printStackTrace();
            isInvite = false;
            if (mConnectionCallBack != null)
                mConnectionCallBack.error(e.getMessage());
        }
        return isInvite;
    }


    /**
     * This anonymous class perform the app invite task on twitter.
     */
    private AsyncTask<Void, Void, Boolean> appInviteTask = new AsyncTask<Void, Void, Boolean>() {
        @Override
        protected Boolean doInBackground(Void... params) {
            if (mAppLinkUrl == null && mAppLinkUrl.equals(""))
                throw new IllegalArgumentException("application link is null or empty");
            return appInvite();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (twitterAppInviteDialog != null)
                twitterAppInviteDialog.dismiss();
            if (result) {
                if (mConnectionCallBack == null)
                    throw new IllegalArgumentException("ConnectionCallback is null");
                else
                    mConnectionCallBack.postSuccess("Success");
            }

        }
    };


    private TwiiterAppInvite twiiterAppInvite = new TwiiterAppInvite() {
        @Override
        public void appInvite(String url) {
            mUrl = url;
            appInviteTask.execute();
        }
    };

    private TwitterAuthCallback getTwitterAuthCallback() {
        return new TwitterAuthCallback() {
            @Override
            public void getAuthUrl(String url) {
                Uri uri = Uri.parse(url);
                String oauth_verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
                if (oauth_verifier != null && oauth_verifier.trim().length() > 0)
                    accessTokenTask.execute(oauth_verifier);
                twitterDialog.dismiss();
            }
        };
    }

    private File getMediaFile() {
        if (mUrl != null && mUrl.trim().length() > 0) {
            File file = new File(mUrl);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

}
