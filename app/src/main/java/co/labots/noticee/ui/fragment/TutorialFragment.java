package co.labots.noticee.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import co.labots.noticee.R;
import co.labots.noticee.TutorialActivity;

public class TutorialFragment extends Fragment implements OnClickListener {

    private ImageView mBtnPlusLogin;
    private ImageView mBtnFaceLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //		restoreState(savedInstanceState);
        Bundle bundle = getArguments();
        int page = bundle.getInt("page");
        String color = bundle.getString("color");
        String description = bundle.getString("description");
        View layout = inflater.inflate(R.layout.tutorial_fragment, container, false);

        layout.findViewById(R.id.signInWithFaceBooks).setVisibility(View.INVISIBLE);
        layout.findViewById(R.id.signInWithgoogle).setVisibility(View.INVISIBLE);
        layout.setBackgroundColor(Color.parseColor(color));
        if (page == 1) {

            layout.findViewById(R.id.introduction).setVisibility(View.INVISIBLE);
            layout.findViewById(R.id.swipe_image).setVisibility(View.INVISIBLE);
            layout.findViewById(R.id.signInWithFaceBooks).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.signInWithgoogle).setVisibility(View.VISIBLE);

            mBtnPlusLogin = (ImageView) layout.findViewById(R.id.signInWithgoogle);
            mBtnFaceLogin = (ImageView) layout.findViewById(R.id.signInWithFaceBooks);
            mBtnPlusLogin.setOnClickListener(this);
            mBtnFaceLogin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TutorialActivity activity = (TutorialActivity) getActivity();
                    activity.runNextActivity(false);
                }
            });

        }

        return layout;

    }

    @Override
    public void onClick(View v) {
        TutorialActivity activity = (TutorialActivity) getActivity();
        activity.runNextActivity(true);
    }

}
