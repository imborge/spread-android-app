package no.boraj.spread.fragment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import no.boraj.spread.R;
import no.boraj.spread.model.Moment;
import no.boraj.spread.model.Queue;
import no.boraj.spread.model.QueueItem;
import no.boraj.spread.model.ResponseStatus;
import no.boraj.spread.task.AsyncTaskListener;
import no.boraj.spread.task.GetQueueTask;
import no.boraj.spread.task.NopeMomentTask;
import no.boraj.spread.task.SpreadMomentTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.TokenCachingStrategy;

public class MomentsFragment extends Fragment implements AsyncTaskListener<ResponseStatus> {
	private ImageView momentImage;
    private Button nopeButton, shareButton;
    private QueueItem currentQueueItem;
    private TextView momentText, momentShares;

    @Override
    public void onPostExecute(ResponseStatus result) {
        Toast t = Toast.makeText(getActivity(), result.getResponseStatus(), Toast.LENGTH_SHORT);
        t.show();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new MomentsFragment())
                .commit();
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_moments, container, false);
		
		momentImage = (ImageView) view.findViewById(R.id.momentImage);
        nopeButton = (Button) view.findViewById(R.id.moments_nope);
        shareButton = (Button) view.findViewById(R.id.moments_share);
        momentText = (TextView) view.findViewById(R.id.moment_text);
        momentShares = (TextView) view.findViewById(R.id.moments_shares);

        new GetQueueTask(Session.getActiveSession().getAccessToken(), new AsyncTaskListener<Queue>() {
            @Override
            public void onPostExecute(Queue result) {
                // Set image
                if(result.getItems().size() > 0) {
                   currentQueueItem = result.getItems().get(0);
                    new DownloadImageTask().execute(currentQueueItem.getMoment().getUrl());
                } else  {
                    momentImage.setImageResource(R.drawable.no_new_moments);
                }
            }
        }).execute();

        nopeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nopeButton.setVisibility(View.GONE);
                shareButton.setVisibility(View.GONE);

                Toast t = Toast.makeText(getActivity(), "Noped", Toast.LENGTH_SHORT);
                t.show();

                new NopeMomentTask(currentQueueItem.getId(), MomentsFragment.this).execute();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nopeButton.setVisibility(View.GONE);
                shareButton.setVisibility(View.GONE);

                Toast t = Toast.makeText(getActivity(), "Sharing", Toast.LENGTH_SHORT);
                t.show();

                new SpreadMomentTask(currentQueueItem.getId(), MomentsFragment.this).execute();
            }
        });

		return view;
	}
	
	public Bitmap loadImageFromNetwork(String addr) {
		try {
			URL url = new URL(addr);
			return BitmapFactory.decodeStream(url.openConnection().getInputStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... urls) {
			return loadImageFromNetwork(urls[0]);
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			if(result != null) {
                nopeButton.setVisibility(View.VISIBLE);
                shareButton.setVisibility(View.VISIBLE);
                momentText.setVisibility(View.VISIBLE);
                momentShares.setVisibility(View.VISIBLE);
				momentImage.setImageBitmap(result);
                momentText.setText(currentQueueItem.getMoment().getText());
                momentShares.setText(String.format("Shares: %s", currentQueueItem.getMoment().getShares()));
			} else {
				momentImage.setImageResource(R.drawable.error_moment);
			}
		}
	}
}
