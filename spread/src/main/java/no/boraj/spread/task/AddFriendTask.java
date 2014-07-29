package no.boraj.spread.task;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import no.boraj.spread.Util;
import no.boraj.spread.model.AddFriend;
import no.boraj.spread.model.User;

/**
 * Created by borgizzle on 08.07.2014.
 */
public class AddFriendTask extends AsyncTask<String, Void, AddFriend> {
    private String TAG = "AddFriendTask";
    private AsyncTaskListener<AddFriend> listener;

    public AddFriendTask(AsyncTaskListener<AddFriend> listener) {
        this.listener = listener;
    }

    @Override
    protected AddFriend doInBackground(String... params) {
        String oauthToken = params[0];
        String username = params[1];

        try {
            final String url = Util.getServerHost() + "/api/friends";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            AddFriend addFriendRequest = new AddFriend();
            addFriendRequest.setOauthToken(oauthToken);
            addFriendRequest.setUsername(username);

            return restTemplate.postForObject(url, addFriendRequest, AddFriend.class);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(AddFriend addFriend) {
        listener.onPostExecute(addFriend);
    }
}
