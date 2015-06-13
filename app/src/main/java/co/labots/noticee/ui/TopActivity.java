package co.labots.noticee.ui;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.labots.noticee.R;
import co.labots.noticee.mockedFragments.FragmentButton;
import co.labots.noticee.model.CalendarItem;
import co.labots.noticee.task.ApiAsyncTask;
import co.labots.noticee.ui.fragment.TodoFragment;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialAccountListener;

/**
 * Created by labots on 15/06/06.
 */
public class TopActivity extends MaterialNavigationDrawer implements MaterialAccountListener {


    private static String TAG = TopActivity.class.getSimpleName();

    //calendar
    public com.google.api.services.calendar.Calendar mService;
    GoogleAccountCredential credential;
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = AndroidJsonFactory.getDefaultInstance();

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};
//    private static final String config ="noticee_config";

    public static List<CalendarItem> calendars = new ArrayList<>();

    private static TodoFragment todoFragment = null;

    @Override
    public void onAccountOpening(MaterialAccount materialAccount) {

    }

    @Override
    public void onChangeAccount(MaterialAccount materialAccount) {

    }

    @Override
    public void init(Bundle bundle) {

        // add accounts
        MaterialAccount account = new MaterialAccount(this.getResources(), "labots", "labots.co@gmail.com", R.drawable.photo, R.drawable.bamboo);
        this.addAccount(account);

        // set listener
        this.setAccountListener(this);

        // create sections
        todoFragment = new TodoFragment();
        this.addSection(newSection("設定", R.drawable.menu_setting, todoFragment).setSectionColor(Color.parseColor("#F5B755")));
        this.addSection(newSection("ヘルプ", R.drawable.menu_help, new FragmentButton()).setSectionColor(Color.parseColor("#F5B755")));
        this.addSection(newSection("アプリについて", R.drawable.menu_person, new FragmentButton()).setSectionColor(Color.parseColor("#F5B755")));

        initCalendar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.closeDrawer();

        if (isGooglePlayServicesAvailable()) {
            refreshResults();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // set the indicator for child fragments
        // N.B. call this method AFTER the init() to leave the time to instantiate the ActionBarDrawerToggle
        this.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    public void chgActionBar(Fragment fragment , boolean disp) {


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        super.onCreateOptionsMenu(menu);
//
//
//        // メニューの要素を追加して取得
//        MenuItem actionItem = menu.add("Action Button");
//
//        // SHOW_AS_ACTION_IF_ROOM:余裕があれば表示
//        actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//
//        // アイコンを設定
//        actionItem.setIcon(android.R.drawable.ic_menu_save);
//
//        return true;
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        final MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menus_detail, menu);
//
////        mainMenu = menu;
//
//        return super.onCreateOptionsMenu(menu);
//    }
    public void initCalendar() {

        Log.v(TAG, new Throwable().getStackTrace()[0].getMethodName() + " ★ Start");

        // Initialize credentials and service object.
        //SharedPreferences settings = getSharedPreferences(config , Context.MODE_PRIVATE);
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        credential = GoogleAccountCredential
                        .usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES))
                        .setBackOff(new ExponentialBackOff())
                        .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
        mService = new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential)
                        .setApplicationName("noticee")
                        .build();
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " ★ End" );

    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == RESULT_OK) {
                    refreshResults();
                } else {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        //SharedPreferences settings = getSharedPreferences(config , Context.MODE_PRIVATE);
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.commit();
                        refreshResults();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    //mStatusText.setText("Account unspecified.");
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    refreshResults();
                } else {
                    chooseAccount();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Attempt to get a set of data from the Google Calendar API to display. If the
     * email address isn't known yet, then call chooseAccount() method so the
     * user can pick an account.
     */
    private void refreshResults() {
        if (credential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            if (isDeviceOnline()) {
                new ApiAsyncTask(this).execute();
            } else {
                //mStatusText.setText("No network connection available.");
            }
        }
    }

    /**
     * Clear any existing Google Calendar API data from the TextView and update
     * the header message; called from background threads and async tasks
     * that need to update the UI (in the UI thread).
     */
    public void clearResultsText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mStatusText.setText("Retrieving data…");
//                mResultsText.setText("");
            }
        });
    }

    /**
     * Fill the data TextView with the given List of Strings; called from
     * background threads and async tasks that need to update the UI (in the
     * UI thread).
     * @param dataStrings a List of Strings to populate the main TextView with.
     */
    public void updateResultsText(final List<CalendarItem> dataStrings) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dataStrings == null) {
                    //mStatusText.setText("Error retrieving data!");
                } else if (dataStrings.size() == 0) {
                    //mStatusText.setText("No data found.");
                } else {

                    first : for (CalendarItem data : dataStrings) {
                        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + "★id : " + data.id);
                        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + "★id : " + data.title);

                        for (CalendarItem calendar : calendars) {
                            if (calendar.id.equals(data.id)) continue first ;
                        }
                        calendars.add(data);
                    }

                    todoFragment.initSwiper(calendars);
                }
            }
        });
    }

    /**
     * Show a status message in the list header TextView; called from background
     * threads and async tasks that need to update the UI (in the UI thread).
     * @param message a String to display in the UI header TextView.
     */
    public void updateStatus(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //mStatusText.setText(message);
                //Do Nothing
            }
        });
    }

    /**
     * Starts an activity in Google Play Services so the user can pick an
     * account.
     */
    private void chooseAccount() {
        startActivityForResult(
                credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date. Will
     * launch an error dialog for the user to update Google Play Services if
     * possible.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS ) {
            return false;
        }
        return true;
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    public void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode,
                        TopActivity.this,
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }

}
