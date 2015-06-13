package co.labots.noticee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;

import com.nifty.cloud.mb.NCMB;

import java.util.ArrayList;

import co.labots.noticee.adapter.TutorialPagerAdapter;
import co.labots.noticee.ui.TopActivity;

public class TutorialActivity extends FragmentActivity {

    private static final String TAG = "TutorialActivity";
    private static final String config = "noticee_config";

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);
        viewPager = (ViewPager) findViewById(R.id.pager);

        NCMB.initialize(this, "fe90a6693c6b931d8158aa8d2a350836395a75efa7eb1af377427d8c09715e80"
                , "6ec2af7fe37ebc911e08466783d624c0c13bf3d8af62c6de4dc98e2720378463");


        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);

        String account = settings.getString("accountName", null);
        if (account != null) {
            runTopActivity();
            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        TutorialPagerAdapter adapter = new TutorialPagerAdapter(fm);
        adapter.addAll(getColorList());
        viewPager.setAdapter(adapter);

    }

    /**
     * 色情報リストを返す.
     *
     * @return 色情報リスト
     */
    private ArrayList<SparseArrayCompat<String>> getColorList() {
        ArrayList<SparseArrayCompat<String>> list = new ArrayList<SparseArrayCompat<String>>();

        SparseArrayCompat<String> color2 = new SparseArrayCompat<String>();
        color2.append(0, "#F5B755");
        SparseArrayCompat<String> color8 = new SparseArrayCompat<String>();
        color8.append(0, "#F5B755");
        list.add(color2);
        list.add(color8);

        return list;
    }

    public void runNextActivity(boolean which) {

        Intent intent = new Intent(this, TopActivity.class);

        startActivity(intent);
        finish();
    }

    public void runTopActivity() {

        Intent intent = new Intent(this, TopActivity.class);
        startActivity(intent);
        finish();
    }

}
