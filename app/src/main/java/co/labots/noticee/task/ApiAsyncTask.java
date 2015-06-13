package co.labots.noticee.task;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.labots.noticee.model.CalendarItem;
import co.labots.noticee.ui.TopActivity;
import co.labots.noticee.util.DateUtil;

/**
 * An asynchronous task that handles the Google Calendar API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
public class ApiAsyncTask extends AsyncTask<Void, Void, Void> {
    private TopActivity mActivity;

    /**
     * Constructor.
     * @param topActivity MainActivity that spawned this task.
     */
    public ApiAsyncTask(TopActivity topActivity) {
        this.mActivity = topActivity;
    }


    /**
     * Background task to call Google Calendar API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            mActivity.clearResultsText();
            mActivity.updateResultsText(getDataFromApi());

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
            mActivity.showGooglePlayServicesAvailabilityErrorDialog(
                    availabilityException.getConnectionStatusCode());

        } catch (UserRecoverableAuthIOException userRecoverableException) {
            mActivity.startActivityForResult(
                    userRecoverableException.getIntent(),
                    TopActivity.REQUEST_AUTHORIZATION);

        } catch (IOException e) {
            mActivity.updateStatus("The following error occurred: " +
                    e.getMessage());
        }
        return null;
    }

    /**
     * Fetch a list of the next 10 events from the primary calendar.
     * @return List of Strings describing returned events.
     * @throws java.io.IOException
     */
    private List<CalendarItem> getDataFromApi() throws IOException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        List<CalendarItem> eItems = new ArrayList<CalendarItem>();
        Events events = mActivity.mService.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();

            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();
            }

            eItems.add(setEvent(event));
        }

        return eItems;
    }

    private CalendarItem setEvent(Event event) {
    	CalendarItem item = new CalendarItem();

    	item.id = event.getId();
    	item.title = event.getSummary();

        if (event.getStart() != null) {
            Calendar calendar = Calendar.getInstance();
            if (event.getStart().getDate() != null) {

                Log.d("ApiSyncTask", "getDate = " + event.getStart().getDate().toString());
            }

            if (event.getStart().getDateTime() != null ) {
                item.year    = DateUtil.getYear(event.getStart().getDateTime());
                item.month   = DateUtil.getMonth(event.getStart().getDateTime());
                item.day     = DateUtil.getDay  (event.getStart().getDateTime());
                item.hour    = DateUtil.getHour  (event.getStart().getDateTime());
                item.minute  = DateUtil.getMinute  (event.getStart().getDateTime());
                item.time    = DateUtil.getTime (event.getStart().getDateTime());
            }

//            Log.d("ApiSyncTask" , "getTimeZone = " + event.getStart().getTimeZone().toString());
        }

    	item.location = event.getLocation();
    	item.status = event.getStatus();
    	return item;
 }


}
