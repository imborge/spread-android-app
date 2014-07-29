package no.boraj.spread;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by borgizzle on 06.07.2014.
 */
public class Util {
    public static String getServerHost() {
        return "http://api.getspread.net";
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static File getOutputMediaFile(int type) {
        String TAG = "getOutputMediaFile()";

        // To be safe, check that SD card is mounted before doing this
        // using Environment.getExternalStorageState()

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Spread");

        // Create storage dir if it does not exist
        if (!mediaStorageDir.exists()) {
            if(!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timestamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timestamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static void logKeyHash() {

    }
}
