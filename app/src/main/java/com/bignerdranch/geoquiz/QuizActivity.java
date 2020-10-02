package com.bignerdranch.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_australia, true)
    };

    private int mCurrentIndex;
    private boolean mIsCheater;
    private int cheatQuantity;

    private static final String CHEAT_QUANTITY_INTENT = "cheatQuantity";
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    public static final String IS_CHEATER_INDEX = "mIsCheaterIndex";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String ANSWER_CHECKER_INDEX = "answerCheckerIndex";

    private int rightAnswers = 0;
    private boolean[] answerChecker = new boolean[mQuestionBank.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(IS_CHEATER_INDEX, false);
            answerChecker = savedInstanceState.getBooleanArray(ANSWER_CHECKER_INDEX);
            cheatQuantity = savedInstanceState.getInt(CHEAT_QUANTITY_INTENT);
        }
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].geTextResId());
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNextPrevQuestion(true);
            }
        });

        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue, cheatQuantity);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               goNextPrevQuestion(true);
            }
        });

        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNextPrevQuestion(false);
            }
        });
        updateQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(IS_CHEATER_INDEX, mIsCheater);
        savedInstanceState.putBooleanArray(ANSWER_CHECKER_INDEX, answerChecker);
        savedInstanceState.putInt(CHEAT_QUANTITY_INTENT, cheatQuantity);
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].geTextResId();
        mQuestionTextView.setText(question);
        if (answerChecker[mCurrentIndex] == false) {
            lockUnlockButtons(true);
        } else {
            lockUnlockButtons(false);
        }
    }

    private void checkAnswer(Boolean userPressedTrue) {
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
            breakVariables();
            mIsCheater = false;
        } else {

            if (userPressedTrue == mQuestionBank[mCurrentIndex].isAnswerTrue()) {
                messageResId = R.string.correct_toast;
                lockUnlockButtons(false);
                rightAnswers +=1;
                answerChecker[mCurrentIndex] = true;
                isAllQuestionAswered();

            } else {
                messageResId = R.string.incorrect_toast;
                lockUnlockButtons(false);
                answerChecker[mCurrentIndex] = true;
                isAllQuestionAswered();
            }
        }
        Toast toast = Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 200);
        toast.show();

    }

    private void goNextPrevQuestion(Boolean isNext) {
        if (isNext) {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
            updateQuestion();
        } else {
            if (mCurrentIndex == 0) {
                mCurrentIndex = mQuestionBank.length - 1;
                updateQuestion();
            } else {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        }

    }

    //проверяет, все ли вопросы отвечены если да - выдает Toast и обнуляет ответы, если нет - возвращает false
    private boolean isAllQuestionAswered() {
        for (int i = 0; i < answerChecker.length; i++) {
            if (answerChecker[i] == true) {
                continue;
            } else {
                return false;
            }
        }

        Integer persentAnswer = rightAnswers * 100 /mQuestionBank.length;
        Toast toast = Toast.makeText(QuizActivity.this, "Right answers: "+ persentAnswer.toString()+"%", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 300);
        toast.show();
        breakVariables();
        return true;
    }

    private void breakVariables() {
        for (int i = 0; i < answerChecker.length; i++) {
            answerChecker[i] = false;
        }
        rightAnswers = 0;
        lockUnlockButtons(true);
    }

    private void lockUnlockButtons(Boolean isUnlock) {
        mTrueButton.setEnabled(isUnlock);
        mFalseButton.setEnabled(isUnlock);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            cheatQuantity = CheatActivity.getCheatQuantity(data);
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                cheatQuantity = CheatActivity.getCheatQuantity(data);
                return;
            }
            cheatQuantity = CheatActivity.getCheatQuantity(data);
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }
}