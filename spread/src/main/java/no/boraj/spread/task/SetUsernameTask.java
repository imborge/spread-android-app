package no.boraj.spread.task;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import no.boraj.spread.Util;
import no.boraj.spread.model.User;

/**
 * Created by borgizzle on 06.07.2014.
 */
public class SetUsernameTask extends AsyncTask<String, Void, User> {
    private AsyncTaskListener<User> listener;
    private String TAG = "SetUsernameTask";

    public SetUsernameTask(AsyncTaskListener<User> listener) {
        this.listener = listener;
    }

    @Override
    protected User doInBackground(String... params) {
        try {
            String oauthToken = params[0];
            String username = params[1];
            final String url = Util.getServerHost() + "/api/users";

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            User user = new User();
            user.setOAuthToken(oauthToken);
            user.setUsername(username);

            return restTemplate.postForObject(url, user, User.class);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(User user) {
        listener.onPostExecute(user);
    }
}
