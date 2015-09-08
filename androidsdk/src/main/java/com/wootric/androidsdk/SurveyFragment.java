package com.wootric.androidsdk;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wootric.androidsdk.objects.CustomMessage;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.LocalizedTexts;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;

/**
 * Created by maciejwitowski on 9/4/15.
 */
public class SurveyFragment extends DialogFragment {

    public static final String ARG_ORIGIN_URL = "com.wootric.androidsdk.arg.origin_url";
    public static final String ARG_USER = "com.wootric.androidsdk.arg.user";
    public static final String ARG_END_USER = "com.wootric.androidsdk.arg.end_user";
    public static final String ARG_LOCALIZED_TEXTS = "com.wootric.androidsdk.arg.localized_texts";
    public static final String ARG_CUSTOM_MESSAGE = "com.wootric.androidsdk.arg.custom_message";

    private EndUser mEndUser;
    private User mUser;
    private String mOriginUrl;
    private LocalizedTexts mLocalizedTexts;
    private CustomMessage mCustomMessage;

    private TextView mTvNpsQuestion;

    public static SurveyFragment newInstance(User user, EndUser endUser, String originUrl, LocalizedTexts localizedTexts, CustomMessage customMessage) {
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
        View view = inflater.inflate(R.layout.fragment_survey, container, false);
        setupLayoutElements(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mTvNpsQuestion.setText(mLocalizedTexts.getNpsQuestion());
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
        }
    }

    private void setupLayoutElements(View view) {
        mTvNpsQuestion = (TextView) view.findViewById(R.id.wootric_tv_nps_question);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ARG_END_USER, mEndUser);
        outState.putString(ARG_ORIGIN_URL, mOriginUrl);
        outState.putParcelable(ARG_USER, mUser);
        outState.putParcelable(ARG_LOCALIZED_TEXTS, mLocalizedTexts);
        outState.putParcelable(ARG_CUSTOM_MESSAGE, mCustomMessage);

        super.onSaveInstanceState(outState);
    }
}
