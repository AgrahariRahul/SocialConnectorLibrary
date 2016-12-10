package rahul.agrahari.socialconnectorlibrary.social.connection;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rahul.agrahari.socialconnectorlibrary.R;
import rahul.agrahari.socialconnectorlibrary.social.socialinterface.TwiiterAppInvite;

/**
 * Created by Rahul Agrahari on 11/30/2016.
 * @version 1.0
 *
 * In this class shows a Twitter App Invite Dialog where we see {@link TwitterAppInviteDialog#resourceid }
 * is the resource id of app icon and {@link TwitterAppInviteDialog#appName} shows the application name and
 * {@link TwitterAppInviteDialog#appDesription} represent the app desription.
 *
 */
public class TwitterAppInviteDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private LinearLayout parentLayout;
    private LinearLayout containerLayout;
    private ImageView imageView;
    private int resourceid;
    private String appName;
    private String appDesription;
    private TwiiterAppInvite twiiterAppInvite;

    public TwitterAppInviteDialog(Context context, int resourceid, String appname, String appdesc, TwiiterAppInvite twiiterAppInvite) {
        super(context);
        this.mContext = context;
        this.resourceid = resourceid;
        this.appName = appname;
        this.appDesription = appdesc;
        this.twiiterAppInvite = twiiterAppInvite;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(createView());
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == android.R.id.button1) {
            containerLayout.setDrawingCacheEnabled(true);
            containerLayout.buildDrawingCache(true);
            Bitmap bmp = Bitmap.createBitmap(containerLayout.getDrawingCache());
            if (bmp == null)
                return;
            new AppInviteTask().execute(bmp);
            containerLayout.setDrawingCacheEnabled(false);
        }

    }


    /**
     * This method is create a dialog view which
     * @return {@link TwitterAppInviteDialog#parentLayout}
     */
    private View createView() {
        parentLayout = new LinearLayout(mContext);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        parentLayout.setGravity(Gravity.CENTER_VERTICAL);

        containerLayout = new LinearLayout(mContext);
        containerLayout.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams containerparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        containerparams.setMargins(10, 0, 10, 10);
        containerLayout.setLayoutParams(containerparams);

        LinearLayout contentLayout = new LinearLayout(mContext);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams contentparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, .6f);
        contentLayout.setLayoutParams(contentparams);

        imageView = new ImageView(mContext);
        LinearLayout.LayoutParams imageparams = new LinearLayout.LayoutParams(150, 150);
        imageparams.setMargins(10, 10, 10, 10);
        imageView.setLayoutParams(imageparams);
        imageView.setImageResource(resourceid);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);


        TextView textView1 = new TextView(mContext);
        LinearLayout.LayoutParams textparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textparams.setMargins(10, 5, 5, 10);
        textView1.setLayoutParams(textparams);
        textView1.setText(appName);
        textView1.setTextAppearance(mContext, android.R.style.TextAppearance_DeviceDefault_Small);
        textView1.setTextColor(Color.BLACK);
        textView1.setTypeface(null, Typeface.BOLD);


        TextView textView2 = new TextView(mContext);
        textView2.setLayoutParams(textparams);
        textView2.setText(appDesription);
        textView2.setTextColor(Color.BLACK);
        textView2.setTextAppearance(mContext, android.R.style.TextAppearance_DeviceDefault_Small);

        contentLayout.addView(textView1);
        contentLayout.addView(textView2);


        containerLayout.addView(imageView);
        containerLayout.addView(contentLayout);


        LinearLayout btnLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams btnLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnLayout.setGravity(Gravity.RIGHT);
        btnLayoutParams.setMargins(10, 10, 10, 0);
        btnLayout.setLayoutParams(btnLayoutParams);
        btnLayout.setBackgroundColor(Color.WHITE);

        Button inviteButton = new Button(mContext);
        inviteButton.setId(android.R.id.button1);
        inviteButton.setText("Invite");

        inviteButton.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams btnparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnparams.setMargins(10, 10, 10, 10);
        btnparams.gravity = Gravity.RIGHT;
        inviteButton.setTextAppearance(mContext, android.R.style.TextAppearance_DeviceDefault_Medium);
        inviteButton.setLayoutParams(btnparams);
        inviteButton.setBackgroundColor(Color.WHITE);
        inviteButton.setTextColor(Color.parseColor("#0dc2ff"));
        //inviteButton.setTextColor(Color.WHITE);

        btnLayout.addView(inviteButton);

        inviteButton.setOnClickListener(this);

        parentLayout.addView(btnLayout);

        parentLayout.addView(containerLayout);

        return parentLayout;
    }


    /**
     * This class create a image from bitmap
     */
    private class AppInviteTask extends AsyncTask<Bitmap, Void, String> {
        @Override
        protected String doInBackground(Bitmap... params) {
            Bitmap bmp = params[0];
            try {
                File file = new File(Environment.getExternalStorageDirectory() + "/socialconnector");
                if (!file.exists())
                    file.mkdir();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                byte[] bytearray = baos.toByteArray();
                File invitefile = new File(Environment.getExternalStorageDirectory(), "/socialconnector/" + "twittercard.png");
                if (invitefile.exists())
                    invitefile.delete();
                FileOutputStream fos = new FileOutputStream(invitefile);
                fos.write(bytearray);
                fos.flush();
                fos.close();
                return invitefile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null && s.trim().length() > 0) {
                if (twiiterAppInvite != null)
                    twiiterAppInvite.appInvite(s);
            }

        }
    }


}
