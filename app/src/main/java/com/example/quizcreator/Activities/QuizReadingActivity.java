package com.example.quizcreator.Activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizcreator.Classes.Question;
import com.example.quizcreator.Classes.Quiz;
import com.example.quizcreator.CorrectAnswersListAdapter;
import com.example.quizcreator.QuizArrayListAdapter;
import com.example.quizcreator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizReadingActivity extends AppCompatActivity {
    String quizCreatorName;
    String quizName;

    private int quizPoints = 0;
    private int actualQuestion = 0;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReferenceToQuiz;
    private ArrayList<Question> questionList = new ArrayList<>();
    private int questionNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_reading_layout);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        quizCreatorName = intent.getStringExtra("creatorName");
        quizName = intent.getStringExtra("quizName");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceToQuiz = mFirebaseDatabase.getReference("Quizzes").child(quizName);
        getQuiz();

    }
    public void getQuiz() {
        databaseReferenceToQuiz.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.getKey().equals("creatorEmail") || ds.getKey().equals("creatorUsername") || ds.getKey().equals("quizName")) { // We reading whole quiz data from database, we need to dodge creatorName and quizName, we got it already from MainMenu.
                        continue;
                    }
                    questionList.add(new Question());
                    questionList.get(questionNumber).setAnswer1(ds.getValue(Question.class).getAnswer1());
                    questionList.get(questionNumber).setAnswer2(ds.getValue(Question.class).getAnswer2());
                    questionList.get(questionNumber).setAnswer3(ds.getValue(Question.class).getAnswer3());
                    questionList.get(questionNumber).setAnswer4(ds.getValue(Question.class).getAnswer4());
                    questionList.get(questionNumber).setQuestion(ds.getValue(Question.class).getQuestion());
                    questionList.get(questionNumber).setCorrectAnswer(ds.getValue(Question.class).getCorrectAnswer());
                    questionNumber++;
                    setQuestionToUI(actualQuestion);
                    Toast.makeText(getApplicationContext(), questionList.size() + "", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void setQuestionToUI(int actualQuestion){
        quizNameT.setText(quizName);
        quizQuestionT.setText(questionList.get(actualQuestion).getQuestion());
        quizAnswerOneBT.setText(questionList.get(actualQuestion).getAnswer1());
        quizAnswerTwoBT.setText(questionList.get(actualQuestion).getAnswer2());
        quizAnswerThreeBT.setText(questionList.get(actualQuestion).getAnswer3());
        quizAnswerFourBT.setText(questionList.get(actualQuestion).getAnswer4());
        checkNumberOfAnswers();
    }
    public void checkNumberOfAnswers(){
        if(quizAnswerFourBT.getText().equals("")){
            quizAnswerFourBT.setVisibility(View.GONE);
        }else{
            quizAnswerFourBT.setVisibility(View.VISIBLE);
        }
        if(quizAnswerThreeBT.getText().equals("")){
            quizAnswerThreeBT.setVisibility(View.GONE);
        }else{
            quizAnswerThreeBT.setVisibility(View.VISIBLE);
        }
    }
    public void checkCorrectAnswer(int buttonAnswer,Button button){
        if(buttonAnswer == questionList.get(actualQuestion).getCorrectAnswer()){
            quizPoints++;
        }
        questionList.get(actualQuestion).setUserAnswer(button.getText().toString());
        if((actualQuestion + 1) == questionList.size()){
            CorrectAnswersListAdapter adapter = new CorrectAnswersListAdapter(QuizReadingActivity.this, questionList);
            listOfAnswers.setAdapter(adapter);

            questionsConstraintLayout.setVisibility(View.GONE);
            listConstraintLayout.setVisibility(View.VISIBLE);
        }else {
            actualQuestion++;
            setQuestionToUI(actualQuestion);
        }

    }

    @OnClick(R.id.quizAnswerOneBT)
    public void quizAnswerOneBTanswer(){
        checkCorrectAnswer(1,quizAnswerOneBT);
        Toast.makeText(this, actualQuestion + " actual question", Toast.LENGTH_SHORT).show();

    }
    @OnClick(R.id.quizAnswerTwoBT)
    public void quizAnswerTwoBTanswer(){
        checkCorrectAnswer(2,quizAnswerTwoBT);
    }
    @OnClick(R.id.quizAnswerThreeBT)
    public void quizAnswerThreeBTanswer(){
        checkCorrectAnswer(3,quizAnswerThreeBT);
    }
    @OnClick(R.id.quizAnswerFourBT)
    public void quizAnswerFourBTanswer(){
        checkCorrectAnswer(4,quizAnswerFourBT);
    }

    @BindView(R.id.questionsConstraintLayout)
    ConstraintLayout questionsConstraintLayout;

    @BindView(R.id.listConstraintLayout)
    ConstraintLayout listConstraintLayout;

    @BindView(R.id.listOfAnswers)
    ListView listOfAnswers;

    @BindView(R.id.quizNameT)
    TextView quizNameT;

    @BindView(R.id.quizQuestionT)
    TextView quizQuestionT;

    @BindView(R.id.quizAnswerOneBT)
    Button quizAnswerOneBT;

    @BindView(R.id.quizAnswerTwoBT)
    Button quizAnswerTwoBT;

    @BindView(R.id.quizAnswerThreeBT)
    Button quizAnswerThreeBT;

    @BindView(R.id.quizAnswerFourBT)
    Button quizAnswerFourBT;

}
