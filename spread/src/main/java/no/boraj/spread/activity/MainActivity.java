package no.boraj.spread.activity;

import no.boraj.spread.R;
import no.boraj.spread.fragment.TakePhotoFragment;
import no.boraj.spread.fragment.FriendsFragment;
import no.boraj.spread.fragment.GalleryFragment;
import no.boraj.spread.fragment.LoginFragment;
import no.boraj.spread.fragment.MomentsFragment;
import no.boraj.spread.fragment.NavigationDrawerFragment;
import no.boraj.spread.fragment.ProfileFragment;
import no.boraj.spread.model.AddFriend;
import no.boraj.spread.task.AddFriendTask;
import no.boraj.spread.task.AsyncTaskListener;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, AsyncTaskListener<AddFriend> {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    
    private LoginFragment loginFragment;
    private Session session;

    private String oAuthAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        
        session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
        	Fragment fragment = null;
        	switch(position) {
        	case 0:
        		fragment = new MomentsFragment();
        		break;
        	case 1:
        		fragment = new TakePhotoFragment();
        		break;
        	case 2:
        		fragment = new GalleryFragment();
        		break;
        	case 3:
        		fragment = new FriendsFragment();
        		break;
        	case 4:
        		fragment = new ProfileFragment();
        		break;
        	}
        	
        	if(fragment != null) {
	            fragmentManager.beginTransaction()
	    				.remove(loginFragment)
	                    .replace(R.id.container, fragment)
	                    .commit();
        	} else {
        		Toast toast = Toast.makeText(getApplicationContext(), "Not implemented yet", Toast.LENGTH_SHORT);
        		toast.show();
        	}
        } else {
            loginFragment = new LoginFragment();
            fragmentManager.beginTransaction()
            		.add(R.id.container, loginFragment)
            		.commit();
        }
    }

    public String getOAuthAccessToken() {
        return oAuthAccessToken;
    }

    public void setOAuthAccessToken(String oAuthAccessToken) {
        this.oAuthAccessToken = oAuthAccessToken;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_create_moment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new TakePhotoFragment())
                    .commit();
        } else if (id == R.id.action_add_friend) {

            // Open dialog to send friend request
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Add friend");
            alert.setMessage("Enter your friend's username");

            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast toast = Toast.makeText(MainActivity.this, input.getText(), Toast.LENGTH_LONG);
                    toast.show();

                    // Send request to server
                    new AddFriendTask(MainActivity.this).execute(Session.getActiveSession().getAccessToken(), input.getText().toString());
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Dont do shit
                }
            });

            alert.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostExecute(AddFriend result) {
        Toast toast = Toast.makeText(this, result.getResponseStatus(), Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
	            Request.newMeRequest(Session.getActiveSession(), new GraphUserCallback() {
					
					@Override
					public void onCompleted(GraphUser user, Response response) {
						textView.setText(String.format("User: %s", user.getName()));
					}
				}).executeAsync();
            } else {
            	textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            }
            return rootView;
        }
    }

}
