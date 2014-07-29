package no.boraj.spread.fragment;

import no.boraj.spread.R;
import no.boraj.spread.R.id;
import no.boraj.spread.Util;

import com.facebook.Session;
import com.fasterxml.jackson.annotation.JsonProperty;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class ProfileFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile, container, false);
		
		final TextView tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        Button signOut = (Button) view.findViewById(id.profile_sign_out);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.getActiveSession().closeAndClearTokenInformation();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(id.container, new LoginFragment())
                        .commit();
            }
        });

        new HttpRequestTask().execute();

        /*
		Request.newMeRequest(Session.getActiveSession(), new GraphUserCallback() {
			
			@Override
			public void onCompleted(GraphUser user, Response response) {
				tvUsername.setText(user.getName() + " - Token: " + Session.getActiveSession().getAccessToken());
			}
		}).executeAsync();
        */
		
		return view;
	}

    public static class Greeting {
        @JsonProperty()
        private String message;

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }
    }

    protected class HttpRequestTask extends AsyncTask<Void, Void, Greeting> {
        private final String TAG = "HttpRequestTask";
        @Override
        protected Greeting doInBackground(Void... params) {
            try {
                final String url = Util.getServerHost() + "/api";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Greeting greeting = restTemplate.getForObject(url, Greeting.class);
                return greeting;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            Log.e(TAG, "greeting null? " + (greeting == null));
            TextView tvUsername = (TextView) getActivity().findViewById(id.tvUsername);
            Log.e(TAG, "tvUsername null? " + (tvUsername == null));
            tvUsername.setText(greeting.getMessage());
        }
    }
}
