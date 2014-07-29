package no.boraj.spread.task;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import no.boraj.spread.Util;
import no.boraj.spread.model.Friend;
import no.boraj.spread.model.Friends;
import no.boraj.spread.model.User;

/**
 * Created by borgizzle on 07.07.2014.
 */
public class GetFriendsTask extends AsyncTask<String, Void, Friends> {
    private String TAG = "GetFriendsTask";
    private AsyncTaskListener listener;

    public GetFriendsTask(AsyncTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected Friends doInBackground(String... params) {
        try {
            String oauthToken = params[0];
            final String url = Util.getServerHost() + "/api/friends/" + oauthToken;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());;

            return restTemplate.getForObject(url, Friends.class);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Friends friends) {
        listener.onPostExecute(friends);
    }
}
