package no.boraj.spread.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import no.boraj.spread.R;
import no.boraj.spread.activity.MainActivity;
import no.boraj.spread.model.User;
import no.boraj.spread.task.AsyncTaskListener;
import no.boraj.spread.task.SetUsernameTask;

/**
 * Created by borgizzle on 06.07.2014.
 */
public class CreateUsernameFragment extends Fragment implements AsyncTaskListener<User>{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_create_username, container, false);
        final TextView tvUsername = (TextView) view.findViewById(R.id.create_username_username);
        Button save = (Button) view.findViewById(R.id.create_username_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetUsernameTask(CreateUsernameFragment.this).execute(((MainActivity) getActivity()).getOAuthAccessToken(), tvUsername.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onPostExecute(User result) {
        if(result != null)
        if (result.getResponseStatus().equals("updated")) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new MomentsFragment())
                    .commit();
        } else {
            // Error, username was not set
        }
    }
}
