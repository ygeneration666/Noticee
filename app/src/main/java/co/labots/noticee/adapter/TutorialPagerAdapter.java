package co.labots.noticee.adapter;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;

import co.labots.noticee.ui.fragment.TutorialFragment;

public class TutorialPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<SparseArrayCompat<String>> mList;

    public TutorialPagerAdapter(FragmentManager fm) {
        super(fm);
        mList = new ArrayList<SparseArrayCompat<String>>();

    }

    @Override
    public Fragment getItem(int position) {

        // 対象ページの色情報を取得
        SparseArrayCompat<String> item = mList.get(position);

        // 色情報を Bundle にする
        Bundle bundle = new Bundle();
        bundle.putInt("page", position);
        bundle.putString("color", item.get(0));
        bundle.putString("name", item.get(1));
        bundle.putString("description", item.get(2));
        bundle.putString("subtitle", item.get(3));

        // Fragment をつくり Bundle をセットする
        TutorialFragment tutorial = new TutorialFragment();
        tutorial.setArguments(bundle);

        return tutorial;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    /**
     * 色情報を追加する.
     *
     * @param item 色情報
     */
    public void add(SparseArrayCompat<String> item) {
        mList.add(item);
    }

    /**
     * 色情報をリストで追加する.
     *
     * @param list 色情報リスト
     */
    public void addAll(ArrayList<SparseArrayCompat<String>> list) {
        mList.addAll(list);
    }
}
