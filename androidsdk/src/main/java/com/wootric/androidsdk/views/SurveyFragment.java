package com.wootric.androidsdk.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wootric.androidsdk.R;
import com.wootric.androidsdk.Wootric;
import com.wootric.androidsdk.network.WootricApiClient;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.ScreenUtils;

/**
 * Created by maciejwitowski on 9/4/15.
 */
public class SurveyFragment extends DialogFragment
    implements NpsLayout.NpsLayoutListener, FeedbackLayout.FeedbackLayoutListener {

    private static final String ARG_ORIGIN_URL = "com.wootric.androidsdk.arg.origin_url";
    private static final String ARG_USER = "com.wootric.androidsdk.arg.user";
    private static final String ARG_END_USER = "com.wootric.androidsdk.arg.end_user";
    private static final String ARG_SETTINGS = "com.wootric.androidsdk.arg.settings";
    private static final String ARG_SELECTED_SCORE = "com.wootric.androidsdk.arg.selected_score";
    private static final String ARG_CURRENT_STATE = "com.wootric.androidsdk.arg.current_state";
    private static final String ARG_RESPONSE_SENT = "com.wootric.androidsdk.arg.response_sent";
    private static final String ARG_ACCESS_TOKEN =  "com.wootric.androidsdk.arg.access_token";

    private NpsLayout mNpsLayout;
    private FeedbackLayout mFeedbackLayout;

    private EndUser mEndUser;
    private User mUser;
    private String mOriginUrl;
    private String mAccessToken;
    private Settings mSettings;

    private boolean mSurveyFinished;

    private static final int STATE_NPS = 0;
    private static final int STATE_FEEDBACK = 1;
    private static final int STATE_THANK_YOU = 3;

    private int mCurrentState = STATE_NPS;

    private boolean mResponseSent;

    private final WootricApiClient mWootricApiClient = new WootricApiClient();

    public static SurveyFragment newInstance(User user, EndUser endUser, String originUrl, String accessToken,
                                             Settings settings) {
        SurveyFragment fragment = new SurveyFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        args.putParcelable(ARG_END_USER, endUser);
        args.putString(ARG_ORIGIN_URL, originUrl);
        args.putString(ARG_ACCESS_TOKEN, accessToken);
        args.putParcelable(ARG_SETTINGS, settings);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.app.DialogFragment.STYLE_NO_TITLE, 0);

        setupState(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wootric_fragment_survey, container, false);
        setupLayoutElements(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupLayoutElementsValues(savedInstanceState);
        updateState(mCurrentState);
    }

    private void updateState(int state) {
        mCurrentState = state;

        if(STATE_NPS == mCurrentState) {
            mNpsLayout.setVisibility(View.VISIBLE);
            mFeedbackLayout.setVisibility(View.GONE);
        } else if(STATE_FEEDBACK == mCurrentState) {
            mNpsLayout.setVisibility(View.GONE);
            mFeedbackLayout.setVisibility(View.VISIBLE);
        } else if(STATE_THANK_YOU == mCurrentState) {
            mNpsLayout.setVisibility(View.GONE);
            mFeedbackLayout.setVisibility(View.GONE);
            showThankYouLayout();
        }
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
                int screenHeight = ScreenUtils.getScreenHeight(activity) *4/5;
                dialog.getWindow().setLayout(screenWidth, screenHeight);
            }
        }
    }

    private void setupState(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            Bundle args = getArguments();
            mUser = args.getParcelable(ARG_USER);
            mEndUser = args.getParcelable(ARG_END_USER);
            mOriginUrl = args.getString(ARG_ORIGIN_URL);
            mAccessToken = args.getString(ARG_ACCESS_TOKEN);
            mSettings = args.getParcelable(ARG_SETTINGS);
        } else {
            mUser = savedInstanceState.getParcelable(ARG_USER);
            mEndUser = savedInstanceState.getParcelable(ARG_END_USER);
            mOriginUrl = savedInstanceState.getString(ARG_ORIGIN_URL);
            mAccessToken = savedInstanceState.getString(ARG_ACCESS_TOKEN);
            mSettings = savedInstanceState.getParcelable(ARG_SETTINGS);
            mCurrentState = savedInstanceState.getInt(ARG_CURRENT_STATE);
            mResponseSent = savedInstanceState.getBoolean(ARG_RESPONSE_SENT);
        }
    }

    private void setupLayoutElements(View view) {
        mNpsLayout = (NpsLayout) view.findViewById(R.id.wootric_nps_layout);
        mNpsLayout.setNpsLayoutListener(this);

        mFeedbackLayout = (FeedbackLayout) view.findViewById(R.id.wootric_feedback_layout);
        mFeedbackLayout.setFeedbackLayoutListener(this);
    }

    private void setupLayoutElementsValues(Bundle savedInstanceState) {
        mNpsLayout.setTexts(mSettings);

        if(savedInstanceState != null) {
            int selectedScore = savedInstanceState.getInt(ARG_SELECTED_SCORE);
            mNpsLayout.setSelectedScore(selectedScore);
            mFeedbackLayout.setTextsForScore(mSettings, selectedScore);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ARG_END_USER, mEndUser);
        outState.putString(ARG_ORIGIN_URL, mOriginUrl);
        outState.putParcelable(ARG_USER, mUser);
        outState.putString(ARG_ACCESS_TOKEN, mAccessToken);
        outState.putParcelable(ARG_SETTINGS, mSettings);
        outState.putInt(ARG_SELECTED_SCORE, mNpsLayout.getSelectedScore());
        outState.putInt(ARG_CURRENT_STATE, mCurrentState);
        outState.putBoolean(ARG_RESPONSE_SENT, mResponseSent);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onNpsLayoutSubmit() {
        mFeedbackLayout.setTextsForScore(mSettings, mNpsLayout.getSelectedScore());
        updateState(STATE_FEEDBACK);
        createResponse();
    }

    @Override
    public void onNpsLayoutDismiss() {
        mSurveyFinished = true;
        dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();

        if(mSurveyFinished) {
            if(!mResponseSent) {
                createDecline();
            }

            Wootric.notifySurveyFinished();
        }
    }

    @Override
    public void onFeedbackDismiss() {
        mSurveyFinished = true;
        updateState(STATE_THANK_YOU);
    }

    @Override
    public void onFeedbackSubmit() {
        createResponse();
        updateState(STATE_THANK_YOU);
    }

    private void createResponse() {
        mWootricApiClient.createResponse(mEndUser, mAccessToken, mOriginUrl,
                mNpsLayout.getSelectedScore(), mFeedbackLayout.getFeedback());

        mResponseSent = true;
    }

    private void createDecline() {
        mWootricApiClient.createDecline(mEndUser, mAccessToken, mOriginUrl);
    }

    @Override
    public void onFeedbackEditScoreClick() {
        updateState(STATE_NPS);
    }

    private void showThankYouLayout() {
        dismiss();

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setMessage(mSettings.getFinalThankYou())
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

        alertDialog.show();

        Button btnPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnPositive.setTextColor(getResources().getColor(R.color.wootric_dialog_header_background));
    }
}
