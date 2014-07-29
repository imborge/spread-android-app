package no.boraj.spread.task;

/**
 * Created by borgizzle on 06.07.2014.
 */
public interface AsyncTaskListener<T> {
    public void onPostExecute(T result);
}
