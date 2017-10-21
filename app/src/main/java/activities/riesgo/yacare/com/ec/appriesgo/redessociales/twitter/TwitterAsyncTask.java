package activities.riesgo.yacare.com.ec.appriesgo.redessociales.twitter;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;

public class TwitterAsyncTask extends AsyncTask<Object, Void, ArrayList<TwitterTweet>> {
    ListActivity callerActivity;

    final static String TWITTER_API_KEY = "NEh2Xa7wGqeboBib4Z2SEYUgO";
    final static String TWITTER_API_SECRET = "zS7dZYC3yCcmCKWeXqoHNPFjtFUqwsZvFkCE3dCHkWAublLjTn";

    @Override
    protected ArrayList<TwitterTweet> doInBackground(Object... params) {
        ArrayList<TwitterTweet> twitterTweets = null;
        callerActivity = (ListActivity) params[1];
        if (params.length > 0) {
            TwitterAPI twitterAPI = new TwitterAPI(TWITTER_API_KEY,TWITTER_API_SECRET);
            twitterTweets = twitterAPI.getTwitterTweets(params[0].toString());
        }
        return twitterTweets;
    }

    @Override
    protected void onPostExecute(ArrayList<TwitterTweet> twitterTweets) {
        ArrayAdapter<TwitterTweet> adapter =
                new ArrayAdapter<TwitterTweet>(callerActivity, R.layout.twitter_tweets_list,
                        R.id.listTextView, twitterTweets);
        callerActivity.setListAdapter(adapter);
        ListView lv = callerActivity.getListView();
        lv.setDividerHeight(0);
        //lv.setDivider(this.getResources().getDrawable(android.R.color.transparent));
        lv.setBackgroundColor(callerActivity.getResources().getColor(R.color.light_gray_header_color));
    }
}
