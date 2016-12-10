package rahul.agrahari.socialconnectorlibrary.social;

import android.content.Context;
import android.content.Intent;

import rahul.agrahari.socialconnectorlibrary.social.Utils.ConnectionType;
import rahul.agrahari.socialconnectorlibrary.social.Utils.SocialConnection;
import rahul.agrahari.socialconnectorlibrary.social.connection.ConnectionHelper;
import rahul.agrahari.socialconnectorlibrary.social.socialinterface.ConnectionCallBack;

/**
 * Created by Rahul Agrahari on 10/11/2016.
 */
public class SocialConnector {

    private SocialBuilder mSocialBuilder;
    private ConnectionHelper connectionHelper;

    private Context context;
    private SocialConnection connection;
    private ConnectionType connectionType;
    private String caption;
    private String url;
    private String description;
    private ConnectionCallBack connectionCallBack;
    private String appLinkUrl;
    private String consumerKey;
    private String consumerSecret;
    private String packageName;
    private String appName;
    private int resourceid;

    private SocialConnector(SocialBuilder socialBuilder) {
        this.mSocialBuilder = socialBuilder;
        connectionHelper = new ConnectionHelper(this);
    }

    public Context getContext() {
        context = mSocialBuilder.sContext;
        return context;
    }

    public SocialConnection getConnection() {
        connection = mSocialBuilder.sConnection;
        return connection;
    }

    public ConnectionType getConnectionType() {
        connectionType = mSocialBuilder.sConnectionType;
        return connectionType;
    }

    public String getCaption() {
        caption = mSocialBuilder.sCaption;
        return caption;
    }

    public String getUrl() {
        url = mSocialBuilder.sUrl;
        return url;
    }

    public String getDescription() {
        description = mSocialBuilder.sDescription;
        return description;
    }

    public ConnectionCallBack getConnectionCallBack() {
        connectionCallBack = mSocialBuilder.sConnectionCallBack;
        return connectionCallBack;
    }

    public String getAppLinkUrl() {
        appLinkUrl = mSocialBuilder.sAppLinkUrl;
        return appLinkUrl;
    }

    public String getConsumerKey() {
        consumerKey = mSocialBuilder.sConsumerKey;
        return consumerKey;
    }

    public String getConsumerSecret() {
        consumerSecret = mSocialBuilder.sConsumerSecret;
        return consumerSecret;
    }

    public String getPackageName() {
        packageName = mSocialBuilder.sPackageName;
        return packageName;
    }

    public String getAppName() {
        appName = mSocialBuilder.sAppName;
        return appName;
    }


    public int getResourceid() {
        resourceid=mSocialBuilder.sResourceid;
        return resourceid;
    }

    public static class SocialBuilder {
        private Context sContext;
        private SocialConnection sConnection;
        private ConnectionType sConnectionType;
        private String sCaption;
        private String sUrl;
        private String sDescription;
        private ConnectionCallBack sConnectionCallBack;
        private String sAppLinkUrl;
        private String sConsumerKey;
        private String sConsumerSecret;
        private String sPackageName;
        private String sAppName;
        private int sResourceid;


        public SocialBuilder(Context context, SocialConnection connection, ConnectionType connectionType) {
            this.sContext = context;
            this.sConnection = connection;
            this.sConnectionType = connectionType;
        }

        public SocialBuilder setSocialConnection(ConnectionCallBack connectionCallBack) {
            sConnectionCallBack = connectionCallBack;
            return this;
        }

        public SocialBuilder setPackageName(String packagename) {
            sPackageName = packagename;
            return this;
        }


        public SocialBuilder setCaption(String caption) {
            sCaption = caption;
            return this;
        }

        public SocialBuilder setUrl(String url) {
            sUrl = url;
            return this;
        }

        public SocialBuilder setDescription(String description) {
            sDescription = description;
            return this;
        }

        public SocialBuilder setAppLinkUrl(String appLinkUrl) {
            sAppLinkUrl = appLinkUrl;
            return this;
        }

        public SocialBuilder setConsumerKey(String consumerKey) {
            sConsumerKey = consumerKey;
            return this;
        }

        public SocialBuilder setConsumerSecret(String consumerSecret) {
            sConsumerSecret = consumerSecret;
            return this;
        }

        public SocialBuilder setAppName(String appname) {
            sAppName = appname;
            return this;
        }

        public SocialBuilder setResourceId(int resourceid) {
            sResourceid = resourceid;
            return this;
        }


        public synchronized SocialConnector build() {
            return new SocialConnector(this);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (connectionHelper != null)
            connectionHelper.onActivityResult(requestCode, resultCode, data);
    }


}
