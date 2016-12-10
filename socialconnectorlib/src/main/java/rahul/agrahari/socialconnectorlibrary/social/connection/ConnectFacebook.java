package rahul.agrahari.socialconnectorlibrary.social.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import rahul.agrahari.socialconnectorlibrary.social.SocialConnector;
import rahul.agrahari.socialconnectorlibrary.social.Utils.ConnectionType;
import rahul.agrahari.socialconnectorlibrary.social.Utils.SocialConnection;
import rahul.agrahari.socialconnectorlibrary.social.model.FacebookUser;
import rahul.agrahari.socialconnectorlibrary.social.socialinterface.ConnectionCallBack;
import rahul.agrahari.socialconnectorlibrary.social.socialinterface.SocialUser;

/**
 * The ConnectFacebook program provides different method to
 * perform action on facebook like login, share and appinvite
 *
 * @author Rahul Agrahari on 10/10/2016.
 * @version 1.0
 */
public class ConnectFacebook {
    private Context mContext;
    private ConnectionType mConnectionType;
    private ConnectionCallBack mConnectionCallBack;
    private CallbackManager mCallbackManager;
    private String mCaption;
    private String mDescription;
    private String mUrl;
    private String mFbAppLinkUrl;

    private SocialConnector mSocialConnector;

    private List<String> permissionNeeds = Arrays.asList("user_photos",
            "email", "public_profile", "user_friends");


    public ConnectFacebook(SocialConnector socialConnector) {
        this.mSocialConnector = socialConnector;
        this.mContext = mSocialConnector.getContext();
        this.mConnectionType = mSocialConnector.getConnectionType();
        this.mConnectionCallBack = mSocialConnector.getConnectionCallBack();
        this.mCaption = mSocialConnector.getCaption();
        this.mDescription = mSocialConnector.getDescription();
        this.mUrl = mSocialConnector.getUrl();
        this.mFbAppLinkUrl = mSocialConnector.getAppLinkUrl();
        if (mCallbackManager == null) {
            mCallbackManager = CallbackManager.Factory.create();
        }
    }

    /**
     * This method provide the connection with facebook on the basis of
     * {@link ConnectionType} like we wants to share post on facebook so we set {@link ConnectionType ConnectionType.POSTSHARE}
     */
    public void connect() {
        if (mConnectionType == ConnectionType.LOGIN)
            facebookLogin();   // call for facebook login
        else if (mConnectionType == ConnectionType.POSTSHARE)
            postShare();     // call for share image
        else if (mConnectionType == ConnectionType.APPINVITE)
            appInvite();     // call for appinvite
    }

    /**
     * This method is call for facebook login and after suceessfull login
     * on facebook we get a user profile information
     * and call {@link ConnectionCallBack#connectionSuccess(SocialUser, SocialConnection)} connectionSuccess}
     */
    private void facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions((Activity) mContext,
                permissionNeeds);

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e("facebook data", object + "");
                        if (mConnectionType == ConnectionType.LOGIN) {
                            if (object != null) {
                                FacebookUser fb = new Gson().fromJson(object.toString(), FacebookUser.class);
                                if (mConnectionCallBack == null)
                                    throw new IllegalArgumentException("ConnectionCallback is empty or null");
                                mConnectionCallBack.connectionSuccess(fb, SocialConnection.FACEBOOK);

                            } else {
                                if (mConnectionCallBack == null)
                                    throw new IllegalArgumentException("ConnectionCallback is empty or null");
                                mConnectionCallBack.connectionFailed(response.getError().getErrorMessage(), SocialConnection.FACEBOOK);

                            }
                        }

                        if (mConnectionType == ConnectionType.POSTSHARE) {
                            postShare();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,birthday,education,email,link,picture.width(99990)");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {
                if (mConnectionCallBack == null)
                    throw new IllegalArgumentException("ConnectionCallback is empty or null");
                mConnectionCallBack.connectionFailed("Connection Cancel", SocialConnection.FACEBOOK);

            }

            @Override
            public void onError(FacebookException error) {
                if (mConnectionCallBack == null)
                    throw new IllegalArgumentException("ConnectionCallback is empty or null");
                mConnectionCallBack.connectionFailed(error.getMessage(), SocialConnection.FACEBOOK);

            }
        });
    }

    /**
     * This method is call for to any link on facebook.
     */
    public void postShare() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null)
            facebookLogin();
        if (mUrl == null || mUrl.isEmpty())
            throw new IllegalArgumentException("Image url is null or empty.");

        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle(mCaption)
                .setContentDescription(mDescription)
                .setContentUrl(Uri.parse(mUrl))
                .build();

        ShareDialog shareDialog = new ShareDialog((Activity) mContext);
        shareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                if (mConnectionCallBack == null)
                    throw new IllegalArgumentException("ConnectionCallback is empty or null");
                mConnectionCallBack.postSuccess("Success");

            }

            @Override
            public void onCancel() {
                if (mConnectionCallBack == null)
                    throw new IllegalArgumentException("ConnectionCallback is empty or null");
                mConnectionCallBack.connectionFailed("Cancel", SocialConnection.FACEBOOK);
            }

            @Override
            public void onError(FacebookException error) {
                if (mConnectionCallBack == null)
                    throw new IllegalArgumentException("ConnectionCallback is empty or null");
                mConnectionCallBack.connectionFailed(error.getMessage(), SocialConnection.FACEBOOK);
            }
        });
        shareDialog.show(linkContent);

    }


    /**
     * This method is call for to invite the people on facebook for sharing applink.
     */
    private void appInvite() {
        if (AppInviteDialog.canShow()) {
            if (mFbAppLinkUrl == null || mFbAppLinkUrl.equals(""))
                throw new IllegalArgumentException("Applink url is empty or null");
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(mFbAppLinkUrl)
                    .setPreviewImageUrl(mUrl)
                    .build();
            AppInviteDialog.show((Activity) mContext, content);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mCallbackManager == null)
            throw new IllegalArgumentException("CallbackManager is empty or null");
        if (resultCode == ((Activity) mContext).RESULT_OK)
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
