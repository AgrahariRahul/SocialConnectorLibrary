package rahul.agrahari.socialconnectorsample;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import rahul.agrahari.socialconnectorlibrary.social.SocialConnector;
import rahul.agrahari.socialconnectorlibrary.social.Utils.ConnectionType;
import rahul.agrahari.socialconnectorlibrary.social.Utils.SocialConnection;
import rahul.agrahari.socialconnectorlibrary.social.socialinterface.ConnectionCallBack;
import rahul.agrahari.socialconnectorlibrary.social.socialinterface.SocialUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ConnectionCallBack {
    RadioGroup rgSocial;
    RadioButton rdFacebook, rdGoogle, rdTwitter;
    ImageView imgProfile;
    TextView tvName;
    TextView tvEmail;
    SocialConnection mSocialConnection;
    SocialConnector socialConnector;

    public static final int REQ_PHOTO = 0x11;

    private final String fb_image_url = "https://icons.better-idea.org/lettericons/F-120-3b5998.png";
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        findView();
    }

    private void findView() {
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        rgSocial = (RadioGroup) findViewById(R.id.rgSocial);
        rdFacebook = (RadioButton) findViewById(R.id.rdFacebook);
        rdGoogle = (RadioButton) findViewById(R.id.rdGoogle);
        rdTwitter = (RadioButton) findViewById(R.id.rdTwitter);
        findViewById(R.id.btnAppInvite).setOnClickListener(this);
        findViewById(R.id.btnShare).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnSelect).setOnClickListener(this);
        rgSocial.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAppInvite) {
            requestAppInvite();
        } else if (v.getId() == R.id.btnShare) {
            requestSharePost();
        } else if (v.getId() == R.id.btnLogin) {
            requestLogin();
        } else if (v.getId() == R.id.btnSelect) {
            selectPhoto();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQ_PHOTO) {
            if (socialConnector != null)
                socialConnector.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQ_PHOTO) {
            if (data != null && resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                mUri = fileUri;
                requestSharePost();
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (checkedId == R.id.rdFacebook) {
            mSocialConnection = SocialConnection.FACEBOOK;
            updateView(1);
        } else if (checkedId == R.id.rdGoogle) {
            mSocialConnection = SocialConnection.GPLUS;
            updateView(1);
        } else if (checkedId == R.id.rdTwitter) {
            mSocialConnection = SocialConnection.TWITTER;
            updateView(1);
        } else {
            updateView(0);
        }
    }

    @Override
    public void connectionSuccess(SocialUser user, SocialConnection socialConnection) {
      
        Picasso.with(MainActivity.this).load(user.getUserImage()).fit().into(imgProfile);
        tvName.setText(user.getUserFirstName() + " " + user.getUserLastName());
        tvEmail.setText(user.getUserEmail() + " ");
    }

    @Override
    public void connectionFailed(String error, SocialConnection socialConnection) {

    }

    @Override
    public void error(String errormessage) {

    }


    @Override
    public void postSuccess(String message) {

    }


    private void requestAppInvite() {
        if (mSocialConnection == SocialConnection.FACEBOOK) {
            socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.FACEBOOK, ConnectionType.APPINVITE).setSocialConnection(this)
                    .setAppLinkUrl(getString(R.string.fb_app_url))
                    .setUrl(fb_image_url)
                    .setAppName(getString(R.string.app_name)).setDescription("This is sample application of social connector libarary").build();

        } else if (mSocialConnection == SocialConnection.GPLUS) {
            socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.GPLUS, ConnectionType.APPINVITE)
                    .setSocialConnection(this)
                    .setAppName(getString(R.string.app_name))
                    .setUrl("http://www.policeabuse.com/")
                    .setAppLinkUrl("https://play.google.com/store/apps/details?id=com.policeabuse.tv")
                    .setDescription("This is sample of socialconnector library.").build();

        } else if (mSocialConnection == SocialConnection.TWITTER) {
            socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.TWITTER, ConnectionType.APPINVITE)
                    .setSocialConnection(this)
                    .setAppName(getString(R.string.app_name))
                    .setDescription("This is sample of socialconnector library.")
                    .setResourceId(R.mipmap.twitter)
                    .setAppLinkUrl("https://play.google.com/store?hl=en")
                    .setConsumerSecret(getString(R.string.consumer_secret))
                    .setConsumerKey(getString(R.string.consumer_key))
                    .setDescription("This is sample application of social connector libarary").build();

        }
    }

    private void requestLogin() {
        if (mSocialConnection == SocialConnection.FACEBOOK) {
            socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.FACEBOOK, ConnectionType.LOGIN)
                    .setSocialConnection(this).build();
        } else if (mSocialConnection == SocialConnection.GPLUS) {
            socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.GPLUS, ConnectionType.LOGIN)
                    .setSocialConnection(this).build();

        } else if (mSocialConnection == SocialConnection.TWITTER) {
            socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.TWITTER, ConnectionType.LOGIN)
                    .setConsumerKey(getString(R.string.consumer_key))
                    .setConsumerSecret(getString(R.string.consumer_secret)).setSocialConnection(this).build();
        }
    }

    private void requestSharePost() {
        if (mSocialConnection == SocialConnection.FACEBOOK) {
            socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.FACEBOOK, ConnectionType.POSTSHARE)
                    .setSocialConnection(MainActivity.this)
                    .setUrl(fb_image_url).setCaption("SocialConnectorSample").setDescription("Test").build();
        } else if (mSocialConnection == SocialConnection.GPLUS) {
            if (mUri == null) {
                selectPhoto();
            } else {

                socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.GPLUS, ConnectionType.POSTSHARE)
                        .setUrl(mUri.toString())
                        .setCaption("SocialConnectorSample")
                        .setDescription("Test")
                        .setSocialConnection(MainActivity.this).build();
            }

        } else if (mSocialConnection == SocialConnection.TWITTER) {
            socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.TWITTER, ConnectionType.POSTSHARE)
                    .setConsumerKey(getString(R.string.consumer_key))
                    .setConsumerSecret(getString(R.string.consumer_secret)).setSocialConnection(MainActivity.this)
                    .setUrl(mUri.getPath()).setCaption("SocialConnectorSample").setDescription("Test").build();
        }
    }


    private void selectPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/* , video/*");
        startActivityForResult(i, REQ_PHOTO);
    }

    private void updateView(int i) {
        if (i == 0) {
            findViewById(R.id.btnAppInvite).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnShare).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnLogin).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnSelect).setVisibility(View.INVISIBLE);
        } else if (i == 1) {
            findViewById(R.id.btnAppInvite).setVisibility(View.VISIBLE);
            findViewById(R.id.btnShare).setVisibility(View.VISIBLE);
            findViewById(R.id.btnLogin).setVisibility(View.VISIBLE);
            findViewById(R.id.btnSelect).setVisibility(View.VISIBLE);
        }
    }
}