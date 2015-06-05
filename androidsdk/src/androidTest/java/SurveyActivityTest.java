import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.wootric.androidsdk.R;
import com.wootric.androidsdk.SurveyActivity;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.objects.CustomMessage;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;


public class SurveyActivityTest extends ActivityInstrumentationTestCase2<SurveyActivity> {

    private static final String CUSTOM_TARGET                   = "Custom Target";
    private static final String CUSTOM_PRODUCT_NAME             = "Custom Product Name";
    private static final String CUSTOM_FOLLOWUP_QUESTION        = "Custom Followup Question";
    private static final String CUSTOM_PLACEHOLDER              = "Custom Placeholder";
    private static final String CUSTOM_DETRACTORS_QUESTION      = "Custom Detractors Question";
    private static final String CUSTOM_PASSIVES_QUESTION        = "Custom Passives Question";
    private static final String CUSTOM_PROMOTERS_QUESTION       = "Custom Promoters Question";
    private static final String CUSTOM_DETRACTORS_PLACEHOLDER   = "Custom Detractors Placeholder";
    private static final String CUSTOM_PASSIVES_PLACEHOLDER     = "Custom Passives Placeholder";
    private static final String CUSTOM_PROMOTERS_PLACEHOLDER    = "Custom Promoters Placeholder";

    public SurveyActivityTest() {
        super(SurveyActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    @Override
    public void tearDown() throws Exception {
        getActivity().clearAfterSurvey();
        super.tearDown();
    }

    public void testRatingView_initState() {
        setupActivity(getTestIntent());
        wait(1000);
        // Rating is displayed
        onView(withId(R.id.rl_rating)).check(matches(isDisplayed()));

        // Feedback is not displayed
        onView(withId(R.id.rl_feedback)).check(matches(not(isDisplayed())));

        // NPS question is displayed
        onView(
                withText("How likely are you to recommend us to a friend or coworker ?")
        ).check(matches(isDisplayed()));

        // Submit question is disabled
        onView(withText("SUBMIT")).check(matches(not(isEnabled())));
    }

    public void testFeedbackView_initState() {
        setupActivity(getTestIntent());

        // Select grade and submit
        goToFeedbackView(4);

        // Check is feedback layout is displayed
        onView(withId(R.id.rl_rating)).check(matches(not(isDisplayed())));
        onView(withId(R.id.rl_feedback)).check(matches(isDisplayed()));

        // Show thank you texts
        onView(withId(R.id.tv_thank_you_score)).check(matches(withText("You gave us 4")));
        onView(withId(R.id.tv_thank_you)).check(matches(withText(R.string.default_followup_question)));

        // Check feedback edit text state
        onView(withId(R.id.et_feedback))
                .check(matches(hasFocus()))
                .check(matches(withHint(R.string.default_placeholder)));

        // Check feedback btn is disabled
        onView(withId(R.id.btn_send_feedback)).check(matches(not(isEnabled())));
    }

    public void testFeedbackView_onSubmit() {
        setupActivity(getTestIntent());

        // Select score and click submit
        goToFeedbackView(4);

        // Add text in feedback edit text
        onView(withId(R.id.et_feedback)).perform(typeText("Great service"));
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

            onView(withId(R.id.et_feedback)).perform(closeSoftKeyboard());
        }
        // Check if feedback btn is enabled
        wait(500);
        onView(withId(R.id.btn_send_feedback))
                .check(matches(isEnabled()))
                .perform(click());

        onView(withId(R.id.rl_rating)).check(matches(not(isDisplayed())));
        onView(withId(R.id.rl_feedback)).check(matches(not(isDisplayed())));
        onView(withText(R.string.final_thank_you)).check(matches(isDisplayed()));
    }

    public void testFeedbackForm_goBackToRating() {
        setupActivity(getTestIntent());

        goToFeedbackView(4);
        // Go back to rating
        onView(withId(R.id.btn_back_to_rating)).perform(click());

        // Check rating is displayed but feedback not
        onView(withId(R.id.rl_rating)).check(matches(isDisplayed()));
        onView(withId(R.id.rl_feedback)).check(matches(not(isDisplayed())));

        // Go back to feedback
        onView(withText(R.string.submit)).check(matches(isEnabled()))
                .perform(click());

        // Check is feedback state saved
        onView(withId(R.id.rl_feedback)).check(matches(isDisplayed()));
    }

    public void testFeedbackView_dismissBtn() {
        setupActivity(getTestIntent());

        goToFeedbackView(4);

        onView(withId(R.id.btn_dismiss)).perform(click());

        // Check that final thank you is shown
        onView(withText(R.string.final_thank_you)).check(matches(isDisplayed()));
    }

    public void testProductNameIsDisplayed() {
        Intent intent = getTestIntent();
        intent.putExtra(SurveyActivity.ARG_PRODUCT_NAME, CUSTOM_PRODUCT_NAME);
        setupActivity(intent);

        onView(withId(R.id.tv_survey_question)).check(matches(withText(containsString(CUSTOM_PRODUCT_NAME))));
    }

    /***********************************************************************************************
     * Custom messages tests
     **/

    private void setupActivity(Intent intent) {
        setActivityIntent(intent);
        getActivity();
    }

    public void testCustomTextsAreUsed() {
        Intent intent = getTestIntent();

        CustomMessage customMessage = CustomMessage.create()
                .recommendTarget(CUSTOM_TARGET)
                .placeholder(CUSTOM_PLACEHOLDER)
                .followupQuestion(CUSTOM_FOLLOWUP_QUESTION);

        intent.putExtra(SurveyActivity.ARG_CUSTOM_MESSAGE, customMessage);
        setupActivity(intent);

        onView(withId(R.id.tv_survey_question)).check(matches(withText(endsWith(CUSTOM_TARGET + " ?"))));

        goToFeedbackView(2);

        onView(withId(R.id.tv_thank_you)).check(matches(withText(CUSTOM_FOLLOWUP_QUESTION)));
        onView(withId(R.id.et_feedback)).check(matches(withHint(CUSTOM_PLACEHOLDER)));
    }

    public void testCustomDetractorsTextsAreUsed() {
        Intent intent = getTestIntent();

        CustomMessage customMessage = CustomMessage.create()
                .detractorFollowupQuestion(CUSTOM_DETRACTORS_QUESTION)
                .detractorPlaceholder(CUSTOM_DETRACTORS_PLACEHOLDER);

        intent.putExtra(SurveyActivity.ARG_CUSTOM_MESSAGE, customMessage);
        setupActivity(intent);

        goToFeedbackView(2);

        onView(withId(R.id.tv_thank_you)).check(matches(withText(CUSTOM_DETRACTORS_QUESTION)));
        onView(withId(R.id.et_feedback)).check(matches(withHint(CUSTOM_DETRACTORS_PLACEHOLDER)));
    }

    public void testCustomPassivesTextsAreUsed() {
        Intent intent = getTestIntent();

        CustomMessage customMessage = CustomMessage.create()
                .passiveFollowupQuestion(CUSTOM_PASSIVES_QUESTION)
                .passivePlaceholder(CUSTOM_PASSIVES_PLACEHOLDER);


        intent.putExtra(SurveyActivity.ARG_CUSTOM_MESSAGE, customMessage);
        setupActivity(intent);

        goToFeedbackView(7);

        onView(withId(R.id.tv_thank_you)).check(matches(withText(CUSTOM_PASSIVES_QUESTION)));
        onView(withId(R.id.et_feedback)).check(matches(withHint(CUSTOM_PASSIVES_PLACEHOLDER)));
    }

    public void testCustomPromotersTextsAreUsed() {
        Intent intent = getTestIntent();

        CustomMessage customMessage = CustomMessage.create()
                .promoterFollowupQuestion(CUSTOM_PROMOTERS_QUESTION)
                .promoterPlaceholder(CUSTOM_PROMOTERS_PLACEHOLDER);


        intent.putExtra(SurveyActivity.ARG_CUSTOM_MESSAGE, customMessage);
        setupActivity(intent);

        goToFeedbackView(9);

        onView(withId(R.id.tv_thank_you)).check(matches(withText(CUSTOM_PROMOTERS_QUESTION)));
        onView(withId(R.id.et_feedback)).check(matches(withHint(CUSTOM_PROMOTERS_PLACEHOLDER)));
    }

    /**
     ***********************************************************************************************
     **/
    private void wait(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void goToFeedbackView(int score) {
        wait(1000);
        onView(withText(String.valueOf(score))).perform(click());
        onView(withText(R.string.submit)).perform(click());
    }

    private Intent getTestIntent() {
        Intent surveyIntent = new Intent(getInstrumentation().getContext(), SurveyActivity.class);

//        surveyIntent.putExtra(SurveyActivity.ARG_ACCESS_TOKEN, "testToken");

        EndUser endUser = new EndUser(1, "nps@example.com");
        surveyIntent.putExtra(SurveyActivity.ARG_END_USER, endUser);

        User user = new User("client_id", "client_secret", "account_token");
        surveyIntent.putExtra(SurveyActivity.ARG_USER, user);

        surveyIntent.putExtra(SurveyActivity.ARG_ORIGIN_URL, "http://example.com");

        return surveyIntent;
    }
}