package co.labots.noticee.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.labots.noticee.R;
import co.labots.noticee.model.CalendarItem;
import co.labots.noticee.module.swipeListView.SwipeMenu;
import co.labots.noticee.module.swipeListView.SwipeMenuCreator;
import co.labots.noticee.module.swipeListView.SwipeMenuItem;
import co.labots.noticee.module.swipeListView.SwipeMenuListView;
import co.labots.noticee.module.swipeListView.SwipeMenuListView.OnMenuItemClickListener;
import co.labots.noticee.module.swipeListView.SwipeMenuListView.OnSwipeListener;

;


/**
 * Created by labots on 15/06/06.
 */
public class TodoFragment extends Fragment implements View.OnTouchListener  {

    private static String TAG = TodoFragment.class.getSimpleName();

    private static final String config ="noticee_config";

    //Widget Setting
    private ImageView target;
    private int targetLocalX;
    private int targetLocalY;
    private int currentX;
    private int currentY;
    private int screenX;
    private int screenY;
    private boolean mode;

    private List<CalendarItem> mCalendarItem;
    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;

    View layout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " Start" );

        layout = inflater.inflate(R.layout.layout_fragment_todo, container, false);
        target = (ImageView) layout.findViewById(R.id.img_add);
        target.bringToFront();
        target.setOnTouchListener(this);

        //initRecycle(savedInstanceState , layout );
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " End" );

        return layout;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " Start " );
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " EnD " );
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " Start " );
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " EnD " );
    }

    @Override
    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(bundle);
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " Start " );
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " EnD " );
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " Start " );
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " EnD " );
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " Start " );
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " EnD " );
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " Start " );
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " EnD " );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " Start " );
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " EnD " );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " Start " );
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " EnD " );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " Start " );
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " EnD " );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " Start " );
        Log.v(TAG , new Throwable().getStackTrace()[0].getMethodName()  + " EnD " );
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                targetLocalX = target.getLeft();
                targetLocalY = target.getTop();

                screenX = x;
                screenY = y;
                currentX = x;
                currentY = y;
                break;

            case MotionEvent.ACTION_MOVE:

                int diffX = screenX - x;
                int diffY = screenY - y;

                targetLocalX -= diffX;
                targetLocalY -= diffY;

                target.layout(targetLocalX,
                        targetLocalY,
                        targetLocalX + target.getWidth(),
                        targetLocalY + target.getHeight());

                screenX = x;
                screenY = y;

                break;

            case MotionEvent.ACTION_UP:

                if (currentX == screenX && currentY == screenY) {
                    //position nothing change goto detail

                    // update the main content by replacing fragments
                    nextSection(null);
                }
                break;
        }
        return true;
    }


    public void nextSection(@Nullable CalendarItem item) {

        FragmentManager manager = this.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        DetailFragment fragment = new DetailFragment();
        Bundle bundle = null;
        if (item !=null ) {
            bundle = new Bundle();
            bundle.putString("title", item.title);

            if (item.day != null) {
                bundle.putInt("year"    , Integer.parseInt(item.year));
                bundle.putInt("month"   , Integer.parseInt(item.month));
                bundle.putInt("day"     , Integer.parseInt(item.day));

                bundle.putInt("hour"    , Integer.parseInt(item.hour));
                bundle.putInt("minute"  , Integer.parseInt(item.minute));

            }

            fragment.setArguments(bundle);
        }

        transaction.setCustomAnimations(
                R.anim.abc_fade_in,
                R.anim.abc_fade_out);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);//前のfragmentへもどるのに必要
        transaction.commit();

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void initSwiper(List<CalendarItem> items) {
        mCalendarItem = items;

        mListView = (SwipeMenuListView) layout.findViewById(R.id.listView);
        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                CalendarItem item = mCalendarItem.get(position);
                switch (index) {
                    case 0:
                        // open
                        //open(item);
                        break;
                    case 1:
                        // delete
//					delete(item);
                        mCalendarItem.remove(position);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });


        // set SwipeListener
        mListView.setOnSwipeListener(new OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                nextSection(mCalendarItem.get(position));
//                Toast.makeText(getActivity() , mCalendarItem.get(position).title , Toast.LENGTH_SHORT).show();
            }
        });

    }

    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mCalendarItem.size();
        }

        @Override
        public CalendarItem getItem(int position) {
            return mCalendarItem.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity().getApplicationContext(),
                        R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            CalendarItem item = getItem(position);
//            holder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.test_back));
            //holder.iv_icon.setImageDrawable(R.drawable.logonotice);

            //Toast.makeText(getActivity() , item.start , Toast.LENGTH_SHORT).show();

            holder.tv_name.setText(item.title);
            holder.day.setText(item.day);
            holder.time.setText(item.time);
            return convertView;
        }

        class ViewHolder {
            //ImageView iv_icon;
            TextView  day;
            TextView  time;
            TextView  tv_name;

            public ViewHolder(View view) {
                //iv_icon = (ImageView) view.findViewById(R.id.dayIcon);
                day     = (TextView) view.findViewById(R.id.day);
                time    = (TextView) view.findViewById(R.id.time);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }
    }




}
