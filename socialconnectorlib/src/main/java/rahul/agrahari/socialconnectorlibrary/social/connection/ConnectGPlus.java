package rahul.agrahari.socialconnectorlibrary.social.connection;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.PlusShare;

import rahul.agrahari.socialconnectorlibrary.social.SocialConnector;
import rahul.agrahari.socialconnectorlibrary.social.SocialConnector.SocialBuilder;
import rahul.agrahari.socialconnectorlibrary.social.Utils.ConnectionType;
import rahul.agrahari.socialconnectorlibrary.social.Utils.SocialConnection;
import rahul.agrahari.socialconnectorlibrary.social.model.GooglePlusUser;
import rahul.agrahari.socialconnectorlibrary.social.socialinterface.ConnectionCallBack;

/**
 * The ConnectGPlus program provides different method to
 * perform action on Google like login, share and appinvite
 *
 * @author Rahul Agrahari on 22/6/16.
 * @version 1.0
 */
public class ConnectGPlus {
    private GoogleApiClient mGoogleApiClient;

    public static final int RC_SIGNIN = 0x22;

    public static final int REQ_GOOGLE_POST = 0x23;

    public static final int REQ_APP_INVITE = 0x24;

    public static boolean mSignInClicked;

    private SocialConnector mSocialConnector;

    private ConnectionResult mConnectionResult;
    private ConnectionCallBack mConnectionCallBack;
    private Context mContext;
    private GoogleSignInAccount acct;
    private String mCaption;
    private String mUrl;
    private String mAppName;
    private String mDescription;
    private ConnectionType mConnectionType;
    private String mApplinkUrl;


    public ConnectGPlus(SocialConnector socialConnector) {
        this.mSocialConnector = socialConnector;
        this.mContext = mSocialConnector.getContext();
        this.mConnectionType = mSocialConnector.getConnectionType();
        this.mCaption = mSocialConnector.getCaption();
        this.mDescription = mSocialConnector.getDescription();
        this.mUrl = mSocialConnector.getUrl();
        this.mAppName = mSocialConnector.getAppName();
        this.mApplinkUrl = mSocialConnector.getAppLinkUrl();
        this.mConnectionCallBack = mSocialConnector.getConnectionCallBack();

        getSignInOption();
    }

    /**
     * This method connect with {@link GoogleApiClient}
     * and provide the Google SignIn Api for login.
     */
    private void getSignInOption() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    /**
     * This method provide the connection with Google on the basis of
     * {@link ConnectionType} like we wants to share post on Google Plus so we set {@link ConnectionType ConnectionType.POSTSHARE}
     */
    public void connect() {
        if (ConnectionType.LOGIN == mConnectionType) {
            getGoogleSignIn();
        } else if (ConnectionType.POSTSHARE == mConnectionType) {
            postImage(mCaption, mDescription, mUrl);
        } else if (ConnectionType.APPINVITE == mConnectionType) {
            appInvite(mAppName, mDescription, mApplinkUrl);
        }
    }

    private void getGoogleSignIn() {
        if (mGoogleApiClient == null)
            getSignInOption();
        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        ((Activity) mContext).startActivityForResult(signIntent, RC_SIGNIN);
        mSignInClicked = true;
    }


    private GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(@Nullable Bundle bundle) {
            mSignInClicked = false;
        }

        @Override
        public void onConnectionSuspended(int i) {
            mSignInClicked = false;
            if (mGoogleApiClient != null)
                connect();

        }
    };

    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            mSignInClicked = false;
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSignInClicked = false;
        if (requestCode == RC_SIGNIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                acct = result.getSignInAccount();
                if (mConnectionCallBack == null)
                    throw new IllegalArgumentException("ConnectionCallback is null");

                if (ConnectionType.LOGIN == mConnectionType) {
                    GooglePlusUser googlePlusUser = new GooglePlusUser(acct.getGivenName(), acct.getFamilyName(),
                            acct.getId(), acct.getPhotoUrl() == null ? "" : acct.getPhotoUrl().getPath(), acct.getEmail());
                    mConnectionCallBack.connectionSuccess(googlePlusUser, SocialConnection.GPLUS);
                }
            }
        } else if (requestCode == REQ_GOOGLE_POST) {
            if (mConnectionCallBack == null)
                throw new IllegalArgumentException("ConnectionCallback is null");
            mConnectionCallBack.postSuccess("Success");
        } else if (requestCode == REQ_APP_INVITE) {

        }
    }

    /**
     * This method is call for to post image on Google+.
     *
     * @param caption     show the caption of the Google+ post
     * @param description show the description of the Google+ post.
     * @param url         is the  path of the Google+  post
     */
    private void postImage(String caption, String description, String url) {
        if (url == null && url.trim().length() == 0)
            throw new IllegalArgumentException(" Post url is empty or null");

        Intent shareIntent = new PlusShare.Builder(mContext)
                .setType(getMimeType(mUrl))
                .setText(caption + "\n" + description)
                .addStream(Uri.parse(url)).getIntent();

        if (mContext.getPackageManager().resolveActivity(shareIntent, 0) == null)
            throw new ActivityNotFoundException("Not find Google Plus Application.");

        ((Activity) mContext).startActivityForResult(shareIntent, REQ_GOOGLE_POST);
    }


    /**
     * This method find the data type of the post.
     *
     * @param url
     * @return type of post
     */
    private String getMimeType(String url) {
        String mimeType = "";
        if (url != null) {
            Uri selectedPost = Uri.parse(url);
            ContentResolver cr = mContext.getContentResolver();
            mimeType = cr.getType(selectedPost);
        }
        return mimeType;
    }

    /**
     * This method is call for to invite the people on Google Plus for sharing applink.
     *
     * @param appname    show the application name on the invite post.
     * @param invitetext show the app link with application description in invite post
     * @param applinkurl show the content url of the app invite post.
     */
    private void appInvite(String appname, String invitetext, String applinkurl) {
        if (applinkurl == null && applinkurl.trim().length() == 0)
            throw new IllegalArgumentException(" Invite link is empty or null");
        PlusShare.Builder builder = new PlusShare.Builder((Activity) mContext);
        builder.setType("text/plain");
        builder.addCallToAction("INSTALL", Uri.parse(applinkurl), "");
        builder.setText(appname + "\n" + invitetext);
        builder.setContentUrl(Uri.parse(mUrl));
        builder.addStream(Uri.parse(mUrl));
        Intent shareIntent = builder.getIntent();

        if (mContext.getPackageManager().resolveActivity(shareIntent, 0) == null)
            throw new ActivityNotFoundException("Not find Google Plus Application.");

        ((Activity) mContext).startActivityForResult(shareIntent, REQ_APP_INVITE);
    }


}


