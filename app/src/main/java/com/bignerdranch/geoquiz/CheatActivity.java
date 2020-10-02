package com.bignerdranch.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.geoquiz.answer_shown";
    public static final String EXTRA_CHEATS_QUANTITY = "com.bignerdranch.geoquiz.cheats_quantity";
    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private TextView mApiLevel;
    private TextView mCheatQuantity;

    private static final String ANSWER_SHOWN_INDEX = "index";
    private static final String ANSWER_ID_INDEX = "indexId";
    private static final String HOW_MANY_CHEATING_INDEX = "cheatIndex";
    private boolean mCheckAnswerShown = false;
    private int answerId;
    private Integer howManyCheating = 0;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue, int quantityCheats) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        intent.putExtra(EXTRA_CHEATS_QUANTITY, quantityCheats);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    public static int getCheatQuantity(Intent result) {
        return result.getIntExtra(EXTRA_CHEATS_QUANTITY, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        howManyCheating = getIntent().getIntExtra(EXTRA_CHEATS_QUANTITY, 0);
        if (savedInstanceState != null) {
            mCheckAnswerShown = savedInstanceState.getBoolean(ANSWER_SHOWN_INDEX, false);
            setAnswerShownResult(mCheckAnswerShown);
            answerId = savedInstanceState.getInt(ANSWER_ID_INDEX, 0);
            howManyCheating = savedInstanceState.getInt(HOW_MANY_CHEATING_INDEX, 0);
        }
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);


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
                mCheckAnswerShown = true;
                howManyCheating +=1;
                mCheatQuantity.setText("Number of cheats: " + howManyCheating.toString());
                setAnswerShownResult(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });
        setAnswerShownResult(false);
        mApiLevel = (TextView) findViewById(R.id.api_level_text_view);
        Integer api = Build.VERSION.SDK_INT;
        mApiLevel.setText("API Level " + api.toString());

        mCheatQuantity = (TextView) findViewById(R.id.how_many_cheating);
        mCheatQuantity.setText("Number of cheats: " + howManyCheating.toString());

        if (howManyCheating >= 3) {
            mShowAnswerButton.setEnabled(false);
        }
        if (answerId != 0) {
            mAnswerTextView.setText(answerId);
            mShowAnswerButton.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(ANSWER_SHOWN_INDEX, mCheckAnswerShown);
        savedInstanceState.putInt(ANSWER_ID_INDEX, answerId);
        savedInstanceState.putInt(HOW_MANY_CHEATING_INDEX, howManyCheating);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        data.putExtra(EXTRA_CHEATS_QUANTITY, howManyCheating);
        setResult(RESULT_OK, data);
    }

}