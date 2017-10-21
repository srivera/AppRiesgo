package activities.riesgo.yacare.com.ec.appriesgo.redessociales.twitter;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

public class TwitterActivity extends ListActivity {

    final static String twitterScreenName = "Riesgos_Ec";
    final static String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
        if (androidNetworkUtility.isConnected(this)) {
            new TwitterAsyncTask().execute(twitterScreenName,this);
        } else {
            Log.v(TAG, "Network not Available!");
        }
    }
}