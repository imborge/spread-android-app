package no.boraj.spread.fragment;

import no.boraj.spread.R;
import no.boraj.spread.activity.MainActivity;
import no.boraj.spread.model.Friend;
import no.boraj.spread.model.Friends;
import no.boraj.spread.task.AsyncTaskListener;
import no.boraj.spread.task.GetFriendsTask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendsFragment extends Fragment implements AsyncTaskListener<Friends> {
    private ListView listFriends;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friends, container, false);

        listFriends = (ListView) view.findViewById(R.id.list_friends);

        new GetFriendsTask(this).execute(((MainActivity) getActivity()).getOAuthAccessToken());
		
		return view;
	}

    @Override
    public void onPostExecute(Friends result) {
        ArrayList<String> items = new ArrayList<String>();

        for(Friend friend : result.getFriends()) {
            items.add(friend.getUsername());
        }

        listFriends.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items));
    }
}
