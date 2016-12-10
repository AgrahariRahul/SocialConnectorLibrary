package rahul.agrahari.socialconnectorlibrary.social.connection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import rahul.agrahari.socialconnectorlibrary.social.socialinterface.TwitterAuthCallback;

/**
 * Created by Rahul Agrahari on 24/11/16.
 *
 *
 */


public class TwitterAuthDialog extends Dialog {
    private WebView webView;
    private String auth_url;
    private String url;
    private ProgressBar progressBar;
    private Context mContext;
    private TwitterAuthCallback mTwitterAuthCallback;

    public TwitterAuthDialog(Context context, String auth_url, TwitterAuthCallback twitterAuthCallback) {
        super(context);
        this.mContext=context;
        this.auth_url = auth_url;
        this.mTwitterAuthCallback=twitterAuthCallback;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(getLayout());
        getIntentData();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(auth_url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(webViewClient);
    }

    private void getIntentData() {
       // auth_url = getIntent().getStringExtra("auth_url");
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url != null && url.contains(ConnectTwitter.TWITTER_CALLBACK_URL)) {
                /*Intent i = getIntent();
                i.putExtra("outh_url", url);
                setResult(RESULT_OK, i);
                finish();*/
                setUrl(url);
                if (mTwitterAuthCallback !=null)
                    mTwitterAuthCallback.getAuthUrl(url);
            }
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private View getLayout() {
        setProgressBar();
        FrameLayout linearLayout = new FrameLayout(mContext);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        webView = new WebView(linearLayout.getContext());
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.addView(progressBar);
        linearLayout.addView(webView);
        return linearLayout;
    }

    private void setProgressBar() {
        progressBar = new ProgressBar(mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressBar.setLayoutParams(params);
        params.gravity = Gravity.CENTER;
        progressBar.setIndeterminate(true);
    }
}
