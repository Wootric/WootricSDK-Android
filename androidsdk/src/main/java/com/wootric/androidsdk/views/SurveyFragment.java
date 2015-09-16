package com.wootric.androidsdk.views;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wootric.androidsdk.R;
import com.wootric.androidsdk.Wootric;
import com.wootric.androidsdk.objects.CustomMessage;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.LocalizedTexts;
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
    private static final String ARG_LOCALIZED_TEXTS = "com.wootric.androidsdk.arg.localized_texts";
    private static final String ARG_CUSTOM_MESSAGE = "com.wootric.androidsdk.arg.custom_message";
    private static final String ARG_SELECTED_SCORE = "com.wootric.androidsdk.arg.selected_score";
    private static final String ARG_CURRENT_STATE = "com.wootric.androidsdk.arg.current_state";

    private NpsLayout mNpsLayout;
    private FeedbackLayout mFeedbackLayout;

    private EndUser mEndUser;
    private User mUser;
    private String mOriginUrl;
    private LocalizedTexts mLocalizedTexts;
    private CustomMessage mCustomMessage;

    private boolean mSurveyFinished;

    private static final int STATE_NPS_LAYOUT = 0;
    private static final int STATE_FEEDBACK_LAYOUT = 1;
    private int mCurrentState = STATE_NPS_LAYOUT;

    public static SurveyFragment newInstance(User user, EndUser endUser, String originUrl,
                                             LocalizedTexts localizedTexts, CustomMessage customMessage) {
        SurveyFragment fragment = new SurveyFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        args.putParcelable(ARG_END_USER, endUser);
        args.putString(ARG_ORIGIN_URL, originUrl);
        args.putParcelable(ARG_LOCALIZED_TEXTS, localizedTexts);
        args.putParcelable(ARG_CUSTOM_MESSAGE, customMessage);

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

        if(STATE_NPS_LAYOUT == mCurrentState) {
            mNpsLayout.setVisibility(View.VISIBLE);
            mFeedbackLayout.setVisibility(View.GONE);
        } else if(STATE_FEEDBACK_LAYOUT == mCurrentState) {
            mNpsLayout.setVisibility(View.GONE);
            mFeedbackLayout.setVisibility(View.VISIBLE);
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
            mLocalizedTexts = args.getParcelable(ARG_LOCALIZED_TEXTS);
            mCustomMessage = args.getParcelable(ARG_CUSTOM_MESSAGE);
        } else {
            mUser = savedInstanceState.getParcelable(ARG_USER);
            mEndUser = savedInstanceState.getParcelable(ARG_END_USER);
            mOriginUrl = savedInstanceState.getString(ARG_ORIGIN_URL);
            mLocalizedTexts = savedInstanceState.getParcelable(ARG_LOCALIZED_TEXTS);
            mCustomMessage = savedInstanceState.getParcelable(ARG_CUSTOM_MESSAGE);
            mCurrentState = savedInstanceState.getInt(ARG_CURRENT_STATE);
        }
    }

    private void setupLayoutElements(View view) {
        mNpsLayout = (NpsLayout) view.findViewById(R.id.wootric_nps_layout);
        mNpsLayout.setNpsLayoutListener(this);

        mFeedbackLayout = (FeedbackLayout) view.findViewById(R.id.wootric_feedback_layout);
        mFeedbackLayout.setFeedbackLayoutListener(this);
    }

    private void setupLayoutElementsValues(Bundle savedInstanceState) {
        mNpsLayout.setAnchorNotLikely(mLocalizedTexts.getAnchorNotLikely());
        mNpsLayout.setAnchorLikely(mLocalizedTexts.getAnchorLikely());
        mNpsLayout.setNpsQuestion(mLocalizedTexts.getNpsQuestion());
        mNpsLayout.setSubmitBtn(mLocalizedTexts.getSend());
        mNpsLayout.setBtnCancel(mLocalizedTexts.getDismiss());

        if(savedInstanceState != null) {
            int selectedScore = savedInstanceState.getInt(ARG_SELECTED_SCORE);
            mNpsLayout.setSelectedScore(selectedScore);
            mFeedbackLayout.setScore(selectedScore);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ARG_END_USER, mEndUser);
        outState.putString(ARG_ORIGIN_URL, mOriginUrl);
        outState.putParcelable(ARG_USER, mUser);
        outState.putParcelable(ARG_LOCALIZED_TEXTS, mLocalizedTexts);
        outState.putParcelable(ARG_CUSTOM_MESSAGE, mCustomMessage);
        outState.putInt(ARG_SELECTED_SCORE, mNpsLayout.getSelectedScore());
        outState.putInt(ARG_CURRENT_STATE, mCurrentState);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onNpsLayoutSubmit(int score) {
        mFeedbackLayout.setScore(score);
        updateState(STATE_FEEDBACK_LAYOUT);
    }

    @Override
    public void onNpsLayoutDismiss() {
        mSurveyFinished = true;
        dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();

        if(mSurveyFinished)
            Wootric.notifySurveyFinished();
    }

    @Override
    public void onFeedbackDismiss() {
        mSurveyFinished = true;
        dismiss();
    }

    @Override
    public void onFeedbackSubmit(String text) {

    }

    @Override
    public void onFeedbackEditScoreClick() {
        updateState(STATE_NPS_LAYOUT);
    }
}
