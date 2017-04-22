package ee.ut.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import ee.ut.demo.BuildConfig;
import ee.ut.demo.R;

/**
 * Created by Bilal Abdullah on 4/20/2017.
 */

public class FeedbackFragment extends Fragment {

    private final String NOT_FILLED_MSG = "Please, select the feedback type " +
            "and fill the comments field";

    @Bind(R.id.feedback_button)
    Button mSendButton;

    @Bind(R.id.feedback_name)
    EditText mNameEditText;

    @Bind(R.id.feedback_email)
    EditText mEmailEditText;

    @Bind(R.id.feedback_comments)
    EditText mCommentsEditText;

    @Bind(R.id.feedback_type)
    RadioGroup mTypeRadioGroup;

    private String mFeedbackType = null;

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        ButterKnife.bind(this, view);

        mTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radio_feedback_bug:
                        mFeedbackType = "Bug";
                        break;
                    case R.id.radio_feedback_feature:
                        mFeedbackType = "Feature";
                        break;
                    case R.id.radio_feedback_general:
                        mFeedbackType = "General";
                        break;
                }
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String emailAddress1 = BuildConfig.emailAddress1;
                String emailAddress2 = BuildConfig.emailAddress2;

                String comments = mCommentsEditText.getText().toString();
                String from = mEmailEditText.getText().toString();
                String name = mNameEditText.getText().toString();

                if(mFeedbackType != null && !comments.isEmpty()){
                    String message = "From:" + from + ", Name: " + name + ", Comments:" + comments;
                    Intent mEmail = new Intent(Intent.ACTION_SEND);
                    mEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{ emailAddress1, emailAddress2});
                    mEmail.putExtra(Intent.EXTRA_SUBJECT, mFeedbackType);
                    mEmail.putExtra(Intent.EXTRA_TEXT, message);

                    mEmail.setType("message/rfc822");
                    startActivity(Intent.createChooser(mEmail, "Choose an email client to send your feedback!"));
                }else {
                    Toast.makeText(getActivity(), NOT_FILLED_MSG, Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}
