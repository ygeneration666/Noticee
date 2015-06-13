package co.labots.noticee.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import co.labots.noticee.R;
import co.labots.noticee.ui.TopActivity;
import co.labots.noticee.util.DateUtil;
import jp.seesaa.android.datetimepicker.date.DatePickerDialog;
import jp.seesaa.android.datetimepicker.time.RadialPickerLayout;
import jp.seesaa.android.datetimepicker.time.TimePickerDialog;
//import android.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View layout;
   //private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menus_detail, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = true;

        Log.v("DetailFragment","onOptionsItemSelected is start");
        switch (item.getItemId()){
            case 1:
                Log.v("DetailFragment","menu 1a is pushed");
                break;
            case 2 :
                Log.v("DetailFragmen","menu 1b is pushed");
                break;
            default:

                Toast.makeText(this.getActivity(), "保存しました" , Toast.LENGTH_SHORT).show();
                Log.v("DetailFragment","menu default is pushed");

                //Thread.sleep("1000");
                ret = super.onOptionsItemSelected(item);
                nextSection();

                break;
        }
        Log.v("DetailFragment","onOptionsItemSelected is end");
        return ret;
    }

    public void nextSection() {

        int backStackCnt = getActivity().getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCnt != 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        layout = inflater.inflate(R.layout.layout_fragment_detail, container, false);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString("title");
        }

        TextView title_todo = (TextView)layout.findViewById(R.id.title_todo);
        title_todo.setText(title);

        layout.findViewById(R.id.datePicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView ymd = (TextView)v;
                DatePickerDialog.newInstance(
                        DetailFragment.this
                        , DateUtil.getYear(ymd.getText().toString())
                        , DateUtil.getMonth(ymd.getText().toString())
                        , DateUtil.getDay(ymd.getText().toString())
                )
                        .show(getFragmentManager(), "datepicker");
            }
        });

        TextView dateEditor = (TextView)layout.findViewById(R.id.datePicker);

        Calendar c = Calendar.getInstance();
        if (getArguments().containsKey("day")) {
            c.set(
                    getArguments().getInt("year"),
                    getArguments().getInt("month"),
                    getArguments().getInt("day")
            );


            Log.d( "DetailFragment" , "init_Hour   = " + getArguments().getInt("hour") );
            Log.d( "DetailFragment" , "init_Minute = " + getArguments().getInt("minute") );

            c.set(Calendar.HOUR_OF_DAY , getArguments().getInt("hour"));
            c.set(Calendar.MINUTE , getArguments().getInt("minute"));
        }

        dateEditor.setText(String.format("%04d年%02d月%02d日　(%s) "
                    , c.get(Calendar.YEAR)
                    , getArguments() != null ? c.get(Calendar.MONTH) : c.get(Calendar.MONTH) + 1
                    , c.get(Calendar.DAY_OF_MONTH)
                    , getWeekDay(
                        c.get(Calendar.YEAR),
                        getArguments() != null ? c.get(Calendar.MONTH) : c.get(Calendar.MONTH) + 1,
                        c.get(Calendar.DAY_OF_MONTH)
                      )
        ));


        layout.findViewById(R.id.timePicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView hm = (TextView)v;

                Log.d( "DetailFragment" , "onClick_Time = " + DateUtil.getHour(hm.getText().toString()));

                TimePickerDialog.newInstance(
                        DetailFragment.this
                        , DateUtil.getHour(hm.getText().toString())
                        , DateUtil.getMinute(hm.getText().toString())
                        , true
                )
                        .show(getFragmentManager(), "timepicker");
            }
        });

        TextView timeEditor = (TextView)layout.findViewById(R.id.timePicker);
        timeEditor.setText(String.format("%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)));

        if (savedInstanceState != null) {
            final FragmentManager fm = getFragmentManager();

            DatePickerDialog datePicker = (DatePickerDialog) fm.findFragmentByTag("datepicker");
            if (datePicker != null) {
                datePicker.setOnDateSetListener(this);
            }

            TimePickerDialog timePicker = (TimePickerDialog) fm.findFragmentByTag("timepicker");
            if (timePicker != null) {
                timePicker.setOnTimeSetListener(this);
            }
        }
        return layout;
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        //Toast.makeText(this.getActivity(), String.format("onDateSet: %d/%d/%d", year, monthOfYear, dayOfMonth), Toast.LENGTH_SHORT).show();
        TextView edit = (TextView)layout.findViewById(R.id.datePicker);
        edit.setText(String.format("%04d年%02d月%02d日　(%s) ", year, monthOfYear + 1, dayOfMonth , getWeekDay(year,monthOfYear, dayOfMonth)));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        //Toast.makeText(this.getActivity(), String.format("onTimeSet: %d:%d", hourOfDay, minute), Toast.LENGTH_SHORT).show();
        TextView edit = (TextView)layout.findViewById(R.id.timePicker);
        edit.setText(String.format("%02d:%02d", hourOfDay, minute));

    }

    @Override
    public void onResume() {

        super.onResume();
        TopActivity activity = (TopActivity)getActivity();
        activity.chgActionBar(this , true);

    }

    private String getWeekDay (int y , int m , int d) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(y , m , d);
        String ret = null;
        switch (calendar.get(Calendar.DAY_OF_WEEK)){

            case Calendar.SUNDAY:
                ret = "日曜日";
                break;
            case Calendar.MONDAY:
                ret = "月曜日";
                break;
            case Calendar.TUESDAY:
                ret = "火曜日";
                break;
            case Calendar.WEDNESDAY:
                ret = "水曜日";
                break;
            case Calendar.THURSDAY:
                ret = "木曜日";
                break;
            case Calendar.FRIDAY:
                ret = "金曜日";
                break;
            case Calendar.SATURDAY:
                ret = "土曜日";
                break;
        }
        return ret;
    }

}
