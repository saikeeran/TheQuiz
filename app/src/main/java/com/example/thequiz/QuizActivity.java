package com.example.thequiz;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    ArrayList<Question> listQuestion = new ArrayList();
    int score = 0;
    TextView txtQuestion,txtOption1,txtOption2,txtOption3,txtOption4,txtProgress,txtCounter;
    RelativeLayout rlQuiz;
    TextView txtNExt;
    Boolean isSoundEnabled = true;
    int counter = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_quiz);
        getSupportActionBar().hide();
        SharedPreferences prefs =  getSharedPreferences("MySharedPRefs", MODE_PRIVATE);
        isSoundEnabled = prefs.getBoolean("hasSound",true);

        txtQuestion = findViewById(R.id.txtQuestion);
        txtOption1 = findViewById(R.id.txtOption1);
        txtOption2 = findViewById(R.id.txtOption2);
        txtOption3 = findViewById(R.id.txtOption3);
        txtOption4 = findViewById(R.id.txtOption4);
        txtCounter = findViewById(R.id.txtCounter);
        txtNExt = findViewById(R.id.txtNext);
        rlQuiz = findViewById(R.id.rlQuiz);
        txtProgress = findViewById(R.id.txtProgress);

        txtOption1.setOnClickListener(view -> {
            txtOption2.setEnabled(false);
            txtOption3.setEnabled(false);
            txtOption4.setEnabled(false);
            if(isAnswerCorrect(currentQuestion,txtOption1.getText().toString())){
                setAnswerWrongOrRight(currentQuestion,txtOption1,true);
                if(isSoundEnabled)
                    playMusicFor(true);
            }else{
                setAnswerWrongOrRight(currentQuestion,txtOption1,false);
                if(isSoundEnabled)
                    playMusicFor(false);
            }
            txtNExt.setVisibility(View.VISIBLE);
            txtNExt.setEnabled(true);
            if(isQuizFinished(currentQuestion)) {
                txtNExt.setText("FINISH");
            }
        });
        txtOption2.setOnClickListener(view -> {
            txtOption1.setEnabled(false);
            txtOption3.setEnabled(false);
            txtOption4.setEnabled(false);
            if(isAnswerCorrect(currentQuestion,txtOption2.getText().toString())){
                setAnswerWrongOrRight(currentQuestion,txtOption2,true);
                if(isSoundEnabled)
                    playMusicFor(true);
            }else{
                setAnswerWrongOrRight(currentQuestion,txtOption2,false);
                if(isSoundEnabled)
                    playMusicFor(false);
            }
            txtNExt.setVisibility(View.VISIBLE);
            txtNExt.setEnabled(true);
            if(isQuizFinished(currentQuestion)) {
                txtNExt.setText("FINISH");
            }
        });
        txtOption3.setOnClickListener(view -> {
            txtOption2.setEnabled(false);
            txtOption1.setEnabled(false);
            txtOption4.setEnabled(false);
            if(isAnswerCorrect(currentQuestion,txtOption3.getText().toString())){
                setAnswerWrongOrRight(currentQuestion,txtOption3,true);
                if(isSoundEnabled)
                    playMusicFor(true);
            }else{
                setAnswerWrongOrRight(currentQuestion,txtOption3,false);
                if(isSoundEnabled)
                    playMusicFor(false);
            }
            txtNExt.setVisibility(View.VISIBLE);
            txtNExt.setEnabled(true);
            if(isQuizFinished(currentQuestion)) {
                txtNExt.setText("FINISH");
            }
        });
        txtOption4.setOnClickListener(view -> {
            txtOption2.setEnabled(false);
            txtOption3.setEnabled(false);
            txtOption1.setEnabled(false);
            if(isAnswerCorrect(currentQuestion,txtOption4.getText().toString())){
                setAnswerWrongOrRight(currentQuestion,txtOption4,true);
                if(isSoundEnabled)
                    playMusicFor(true);
            }else{
                setAnswerWrongOrRight(currentQuestion,txtOption4,false);
                if(isSoundEnabled)
                    playMusicFor(false);
            }
            txtNExt.setVisibility(View.VISIBLE);
            txtNExt.setEnabled(true);
            if(isQuizFinished(currentQuestion)) {
                txtNExt.setText("FINISH");
            }
        });
        setQuestions();
        startQuiz();
        txtNExt.setOnClickListener(view -> {
            if(isQuizFinished(currentQuestion)) {
                //show result quiz finish here
                showThankYouDialog();
            }else {
                txtOption1.setEnabled(true);
                txtOption2.setEnabled(true);
                txtOption3.setEnabled(true);
                txtOption4.setEnabled(true);
                currentQuestion++;
                showQuestion(currentQuestion);
                txtNExt.setVisibility(View.INVISIBLE);
                txtNExt.setEnabled(false);
            }
        });
    }
    private void showThankYouDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_thank_you);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        window.setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);

        TextView btnReset = dialog.findViewById(R.id.btnResetQuiz);
        TextView btnExit = dialog.findViewById(R.id.btnExit);
        TextView txtScore = dialog.findViewById(R.id.txtScore);
        txtScore.setText("Your Score : "+score);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(dialog.getContext());

        btnReset.setOnClickListener(view -> {
            dialog.dismiss();
            startQuiz();
            //start again quiz here
        });
        btnExit.setOnClickListener(view -> {
            dialog.dismiss();
            finishAffinity();
        });
        dialog.show();
    }
    private void playMusicFor(boolean isRight) {
        if(isRight){
            MediaPlayer mediaPlayer=MediaPlayer.create(this,R.raw.correct);
            mediaPlayer.start();
        }else{
            MediaPlayer mediaPlayer=MediaPlayer.create(this,R.raw.wrong);
            mediaPlayer.start();
        }
    }

    private boolean isQuizFinished(int currentQuestion) {
        return currentQuestion >= listQuestion.size();
    }

    private void setAnswerWrongOrRight(int currentQuestion, TextView selectedOption,boolean isRight) {

        if(isRight){
            selectedOption.setBackgroundResource(R.drawable.bg_right);
            score++;
        }else{
            showRightAnswer(currentQuestion);
            selectedOption.setBackgroundResource(R.drawable.bg_wrong);
        }

    }

    private void showRightAnswer(int currentQuestion) {
        String temp = listQuestion.get(currentQuestion-1).answer;
        if(listQuestion.get(currentQuestion-1).option1.equals(temp))
            txtOption1.setBackgroundResource(R.drawable.bg_right);
        else if(listQuestion.get(currentQuestion-1).option2.equals(temp))
            txtOption2.setBackgroundResource(R.drawable.bg_right);
        else if(listQuestion.get(currentQuestion-1).option3.equals(temp))
            txtOption3.setBackgroundResource(R.drawable.bg_right);
        else if(listQuestion.get(currentQuestion-1).option4.equals(temp))
            txtOption4.setBackgroundResource(R.drawable.bg_right);
    }

    private boolean isAnswerCorrect(int currentQuestion, String answer) {
        return listQuestion.get(currentQuestion-1).answer.equals(answer);
    }

    int currentQuestion =1;
    private void startQuiz() {
        txtOption1.setEnabled(true);
        txtOption2.setEnabled(true);
        txtOption3.setEnabled(true);
        txtOption4.setEnabled(true);
        score = 0;
        txtNExt.setText("Next");
        currentQuestion=1;
        counter = 3;
        txtNExt.setVisibility(View.GONE);
        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(counter<=0){
                    handler.removeCallbacksAndMessages(null);
                    txtCounter.setVisibility(View.GONE);
                    rlQuiz.setVisibility(View.VISIBLE);
                }else {
                    handler.postDelayed(this::run, 1000);
                    txtCounter.setText(""+counter);
                    counter--;
                    txtCounter.setVisibility(View.VISIBLE);
                    rlQuiz.setVisibility(View.GONE);
                }
            }
        },10);
        showQuestion(currentQuestion);
    }

    private void showQuestion(int currentQuestion) {
        txtQuestion.setText(listQuestion.get(currentQuestion-1).question);
        txtOption1.setText(listQuestion.get(currentQuestion-1).option1);
        txtOption2.setText(listQuestion.get(currentQuestion-1).option2);
        txtOption3.setText(listQuestion.get(currentQuestion-1).option3);
        txtOption4.setText(listQuestion.get(currentQuestion-1).option4);
        txtOption1.setBackgroundResource(R.drawable.bg_unclick);
        txtOption2.setBackgroundResource(R.drawable.bg_unclick);
        txtOption3.setBackgroundResource(R.drawable.bg_unclick);
        txtOption4.setBackgroundResource(R.drawable.bg_unclick);
        txtProgress.setText("Question "+currentQuestion+" of 10");
    }

    private void setQuestions() {
        listQuestion.add(new Question(
                "For how many days is a Test match scheduled??",
                "1","5","2","6",
                "5"));
        listQuestion.add(new Question(
                "For how many days is a One day match scheduled?",
                "1","3","2","4",
                "1"));
        listQuestion.add(new Question(
                "In which year were the first laws of cricket believed to have been written??",
                "1771","1772","1773","None of the above.",
                "None of the above."));
        listQuestion.add(new Question(
                "What is the slang term given to a ball that is bowled so well that it is considered unplayable by the batsman??",
                "Inswinger","Outswinger","Jaffa","FullToss",
                "Jaffa"));
        listQuestion.add(new Question(
                "When was the Ashes first played?",
                "1871","1872","1877","1878",
                "1877"));
        listQuestion.add(new Question(
                "Who won the first ever Cricket World Cup in 1975??",
                "Australia","England","India","WestIndies",
                "WestIndies"));
        listQuestion.add(new Question(
                "How long is the wicket on a cricket pitch?",
                "18 yards","22 yards","16 yards","14 yards",
                "22 yards"));
        listQuestion.add(new Question(
                "Which fast bowler has taken the most test wickets??",
                "Glenn McGrath","James Anderson","Brett Lee","Stuart Broad",
                "James Anderson"));
        listQuestion.add(new Question(
                "Who bowled the fastest delivery ever of 100.2mph?",
                "Brett Lee","Shoaib Akthar","Shan Tait","Zaheer Khan",
                "Shoaib Akthar"));
        listQuestion.add(new Question(
                "Which Australian player has scored the most test runs??",
                "Steven Smith","David Warner","Ricky Ponting","Adam Gilchrist",
                "Ricky Ponting"));

    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        AlertDialog dialog = new AlertDialog.Builder(QuizActivity.this).create();
        dialog.setTitle(R.string.app_name);
        dialog.setMessage("Are You Sure To Exit Quiz?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        dialog.show();
    }
}