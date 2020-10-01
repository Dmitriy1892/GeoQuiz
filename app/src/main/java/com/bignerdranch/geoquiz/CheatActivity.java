package com.bignerdranch.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.geoquiz.answer_shown";
    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    private static final String ANSWER_SHOWN_INDEX = "index";
    private static final String ANSWER_ID_INDEX = "indexId";
    private boolean mCheckAnswerShown = false;
    private int answerId;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCheckAnswerShown = savedInstanceState.getBoolean(ANSWER_SHOWN_INDEX, false);
            setAnswerShownResult(mCheckAnswerShown);
            answerId = savedInstanceState.getInt(ANSWER_ID_INDEX, 0);
        }
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        if (answerId != 0) {
            mAnswerTextView.setText(answerId);
        }
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnswerIsTrue) {
                    answerId = R.string.true_button;
                    mAnswerTextView.setText(answerId);
                } else {
                    answerId = R.string.false_button;
                    mAnswerTextView.setText(answerId);
                }
                setAnswerShownResult(true);
                mCheckAnswerShown = true;
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(ANSWER_SHOWN_INDEX, mCheckAnswerShown);
        savedInstanceState.putInt(ANSWER_ID_INDEX, answerId);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}