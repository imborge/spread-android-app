package no.boraj.spread.task;

import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import no.boraj.spread.Util;
import no.boraj.spread.model.ResponseStatus;
import no.boraj.spread.model.UploadMoment;

/**
 * Created by borgizzle on 10.07.2014.
 */
public class UploadMomentTask extends AsyncTask<String, Void, ResponseStatus> {
    private byte[] photoData;
    private String text;
    private AsyncTaskListener<ResponseStatus> listener;

    public UploadMomentTask(byte[] photoData, String text, AsyncTaskListener<ResponseStatus> listener) {
        this.photoData = photoData;
        this.text = text;
        this.listener = listener;
    }

    @Override
    protected ResponseStatus doInBackground(String... oauthToken) {
        final String url = Util.getServerHost() + "/api/moments";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        UploadMoment uploadMoment = new UploadMoment();
        uploadMoment.setOAuthToken(oauthToken[0]);
        uploadMoment.setText(text);
        uploadMoment.setPhoto(Base64.encodeToString(photoData, Base64.DEFAULT));
        return restTemplate.postForObject(url, uploadMoment, ResponseStatus.class);
    }

    @Override
    protected void onPostExecute(ResponseStatus responseStatus) {
        listener.onPostExecute(responseStatus);
    }
}
