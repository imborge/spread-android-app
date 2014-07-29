package no.boraj.spread.fragment;

import java.util.Arrays;

import no.boraj.spread.activity.MainActivity;
import no.boraj.spread.R;
import no.boraj.spread.model.User;
import no.boraj.spread.task.AsyncTaskListener;
import no.boraj.spread.task.CreateUserTask;

import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.widget.LoginButton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginFragment extends Fragment implements AsyncTaskListener<User> {
	private UiLifecycleHelper uiHelper;
    private String TAG = "LoginFragment";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}
	
	private Session.StatusCallback callback = new StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
		authButton.setFragment(this);
		authButton.setReadPermissions(Arrays.asList("public_profile"));
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();

        /*
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    */
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
	
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
    	if (state.isOpened()) {
            // Logged in
            Log.d(TAG, "OAuth Access token: " + session.getAccessToken());
            // Store the OAuth access token in the main activity, it is needed for authentication when making requests to the Spread Web API
            ((MainActivity) getActivity()).setOAuthAccessToken(session.getAccessToken());
            new CreateUserTask(this).execute(session.getAccessToken());
    	} else if (state.isClosed()) {
            // Logged out
    	}
    }

    @Override
    public void onPostExecute(User user) {
        if(user != null) {
            if (user.getResponseStatus().equals("created")) {
                Log.d(TAG, "User created");
                Log.d(TAG, "User: " + user.getUsername());
            } else if (user.getResponseStatus().equals("exists")) {
                // User already exist
                Log.d(TAG, "User already exists");
                Log.d(TAG, "User: " + user.getUsername());
            } else {
                // Unknown response status
                Log.d(TAG, "Unknown response_status");
            }

            // Force user to create a username if the user already doesnt have one
            if (user.getUsername() == null || user.getUsername().equals("")) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, (new CreateUsernameFragment()))
                        .commit();
            } else {
                // Set start fragment to MomentsFragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new MomentsFragment())
                        .commit();
            }
        } else {
            // Error communicating with server
            Log.d(TAG, "Error communicating with server");
        }
    }
}
