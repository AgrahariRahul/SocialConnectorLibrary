package rahul.agrahari.socialconnectorlibrary.social.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rahul Agrahari on 11/25/2016.
 */
public class PrefUtils {
    static PrefUtils mPrefUtils;
    SharedPreferences mSharedPreferences;
    static SharedPreferences.Editor editor;
    static Context mContext;

    private PrefUtils() {
        getSharedPref();
    }

    public static PrefUtils getPrefInstance(Context context) {
        mContext = context;
        if (mPrefUtils == null) {
            synchronized (PrefUtils.class) {
                if (mPrefUtils == null)
                    mPrefUtils = new PrefUtils();
                return mPrefUtils;
            }
        }
        return mPrefUtils;
    }

    private void getSharedPref() {
        mSharedPreferences = mContext.getSharedPreferences("SocialConnectorLibrary", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public String getPrefString(String key) {
        if (mSharedPreferences == null)
            return "";
        return mSharedPreferences.getString(key, "");
    }

    public void savePrefString(String key, String value) {
        if (editor == null)
            return;
        editor.putString(key, value);
        editor.commit();
    }
}
