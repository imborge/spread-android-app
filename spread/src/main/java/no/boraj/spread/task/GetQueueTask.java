package no.boraj.spread.task;

import android.os.AsyncTask;
import android.text.style.EasyEditSpan;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import no.boraj.spread.Util;
import no.boraj.spread.model.Queue;

/**
 * Created by borgizzle on 13.07.2014.
 */
public class GetQueueTask extends AsyncTask<Void, Void, Queue> {
    private String oauthToken;
    private AsyncTaskListener<Queue> listener;

    public GetQueueTask(String oauthToken, AsyncTaskListener<Queue> listener) {
        this.oauthToken = oauthToken;
        this.listener = listener;
    }

    @Override
    protected Queue doInBackground(Void... params) {
        final String url = Util.getServerHost() + "/api/queue/" + oauthToken + "?timestamp=" + (System.currentTimeMillis() / 1000);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        return restTemplate.getForObject(url, Queue.class);
    }

    @Override
    protected void onPostExecute(Queue queue) {
        listener.onPostExecute(queue);
    }
}
