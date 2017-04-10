package com.bignerdranch.android.geoquiz;

//import android.support.v7.app.AppCompatActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
//import android.view.Menu;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index"; //ключа для сохраняемого значения
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private TrueFalse[] mAnswerKey = new TrueFalse[] {
            new TrueFalse(R.string.question_oceans, true),
            new TrueFalse(R.string.question_4, false),
            new TrueFalse(R.string.question_2, true),
            new TrueFalse(R.string.question_mideast, false),
            new TrueFalse(R.string.question_africa, true),
            new TrueFalse(R.string.question_6, true),
            new TrueFalse(R.string.question_americas, false),
            new TrueFalse(R.string.question_asia, true),
            new TrueFalse(R.string.question_1, true),
            new TrueFalse(R.string.question_3, false),
            new TrueFalse(R.string.question_5, false),
            new TrueFalse(R.string.question_7, true)
    };
    private int mCurrentIndex = 0;
    private boolean mIsCheater; //переменную для хранения значения, возвращаемого CheatActivity.
    private TextView mVersionTextView;

    private void updateQuestion() {
        int question = mAnswerKey[mCurrentIndex].getQuestion();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mAnswerKey[mCurrentIndex].isTrueQuestion();
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate(Bundle) called");

        setContentView(R.layout.activity_quiz);

        Log.d(TAG, "Build.VERSION.SDK_INT = "+Integer.toString(Build.VERSION.SDK_INT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setSubtitle("Вопросник");
            }
        }

        //получение ссылоки на виджет
        mTrueButton = (Button)findViewById(R.id.true_button);
        //установка слушателя
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        //получение ссылки на виджет
        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        //Подключение кнопки Next
        mNextButton = (Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mAnswerKey.length;
        //        Log.d(TAG, "Updating question text for question #" + mCurrentIndex,
        //                new Exception());
                int question = mAnswerKey[mCurrentIndex].getQuestion();
                mIsCheater = false;
                updateQuestion();
            }
        });

        //добавление слушателя к тексту с повросом как в кнопке Next
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mAnswerKey.length;
                int question = mAnswerKey[mCurrentIndex].getQuestion();
                mIsCheater = false;
                updateQuestion();
            }
        });

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запуск CheatActivity
                //
                Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mAnswerKey[mCurrentIndex].isTrueQuestion();
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                startActivityForResult(i, 0);
            }
        });

        //version
        mVersionTextView = (TextView)findViewById(R.id.version_text_view);
        CharSequence Char = "API Level "+Integer.toString(Build.VERSION.SDK_INT);
        mVersionTextView.setText(Char);

        mIsCheater = false;
        updateQuestion();
    } // конец onCreate

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
    }
    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
     //   getMenuInflater().inflate(R.menu.activity_quiz, menu);
     //   return true;
    //}
}
