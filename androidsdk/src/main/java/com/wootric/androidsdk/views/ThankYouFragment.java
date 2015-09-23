package com.wootric.androidsdk.views;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wootric.androidsdk.R;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.utils.ScreenUtils;

/**
 * Created by maciejwitowski on 9/21/15.
 */
public class ThankYouFragment extends DialogFragment
        implements ThankYouLayout.ThankYouLayoutListener {

    private static final String ARG_SETTINGS = "com.wootric.androidsdk.arg.settings";
    private static final String ARG_SCORE = "com.wootric.androidsdk.arg.score";
    private static final String ARG_FEEDBACK = "com.wootric.androidsdk.arg.feedback";

    private static final String THANK_YOU_DIALOG_TAG = "thank_you_dialog_tag";

    private ThankYouLayout mThankYouLayout;
    private Settings mSettings;
    private int mScore;
    private String mFeedback;

    public static void show(Context context, Settings settings, int score, String feedback) {
        if(context == null)
            return;

        final FragmentManager fragmentManager = ((Activity) context).getFragmentManager();

        ThankYouFragment fragment = new ThankYouFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_SETTINGS, settings);
        args.putInt(ARG_SCORE, score);
        args.putString(ARG_FEEDBACK, feedback);
        fragment.setArguments(args);

        fragment.show(fragmentManager, THANK_YOU_DIALOG_TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.app.DialogFragment.STYLE_NO_TITLE, 0);

        if(savedInstanceState == null) {
            Bundle args = getArguments();
            mSettings = args.getParcelable(ARG_SETTINGS);
            mScore = args.getInt(ARG_SCORE);
            mFeedback = args.getString(ARG_FEEDBACK);
        } else {
            mSettings = savedInstanceState.getParcelable(ARG_SETTINGS);
            mScore = savedInstanceState.getInt(ARG_SCORE);
            mFeedback = savedInstanceState.getString(ARG_FEEDBACK);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wootric_fragment_thank_you, container, false);

        mThankYouLayout = (ThankYouLayout) view.findViewById(R.id.wootric_thank_you_layout);
        mThankYouLayout.setThankYouLayoutListener(this);
        mThankYouLayout.initValues(mSettings, mScore, mFeedback);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        final Activity activity = getActivity();

        int orientation = activity.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Dialog dialog = getDialog();
            if (dialog != null) {
                int screenWidth = ScreenUtils.getScreenWidth(activity);
                int screenHeight = ScreenUtils.getScreenHeight(activity);
                dialog.getWindow().setLayout(screenWidth, screenHeight);
            }
        } else {
            Dialog dialog = getDialog();
            if (dialog != null) {
                int screenWidth = ScreenUtils.getScreenWidth(activity);
                int screenHeight = ScreenUtils.getScreenHeight(activity) *3/5;
                dialog.getWindow().setLayout(screenWidth, screenHeight);
            }
        }
    }

    @Override
    public void onThankYouFinished() {
        dismiss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_SETTINGS, mSettings);
        outState.putInt(ARG_SCORE, mScore);
        outState.putString(ARG_FEEDBACK, mFeedback);
    }
}
