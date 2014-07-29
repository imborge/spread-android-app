package no.boraj.spread.task;

import android.os.AsyncTask;

import com.facebook.Session;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import no.boraj.spread.Util;
import no.boraj.spread.model.ResponseStatus;
import no.boraj.spread.model.SpreadMoment;

/**
 * Created by borgizzle on 14.07.2014.
 */
public class NopeMomentTask extends AsyncTask<Void, Void, ResponseStatus> {
    private long queueItemId;
    private AsyncTaskListener<ResponseStatus> listener;

    public NopeMomentTask(long queueItemId, AsyncTaskListener<ResponseStatus> listener) {
        this.queueItemId = queueItemId;
        this.listener = listener;
    }

    @Override
    protected ResponseStatus doInBackground(Void... params) {
        final String url = Util.getServerHost() + "/api/moments/nope";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        SpreadMoment request = new SpreadMoment();
        request.setOAuthToken(Session.getActiveSession().getAccessToken());
        request.setQueueItemId(queueItemId);

        return restTemplate.postForObject(url, request, ResponseStatus.class);
    }

    @Override
    protected void onPostExecute(ResponseStatus responseStatus) {
        listener.onPostExecute(responseStatus);
    }
}
