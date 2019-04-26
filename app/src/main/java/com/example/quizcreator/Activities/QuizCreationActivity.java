package com.example.quizcreator.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import com.example.quizcreator.Classes.User;
import com.example.quizcreator.Fragments.FragmentManagement;
import com.example.quizcreator.Classes.Quiz;
import com.example.quizcreator.R;
import com.example.quizcreator.Fragments.mFragment;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class QuizCreationActivity extends AppCompatActivity {

    private String quizName;
    private FragmentManagement adapter = new FragmentManagement(getSupportFragmentManager());
    private ArrayList<mFragment> mFragmentsArrayList = new ArrayList<>();
    private int fragmentNr = 0;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRefAccounts;
    private DatabaseReference myRef;
    private String username;
    private Quiz quiz;
    public static int fragmentCounter = 0;
    boolean frag1, frag2, frag3, frag4, frag5;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_creator_layout);
        overridePendingTransition(R.anim.transparent_anim_appear, R.anim.transparent_anim_disapear);
        ButterKnife.bind(this);
        mViewPager.startAnimation(transparent_anim_appear);
        Intent intent = getIntent();
        quizName = intent.getStringExtra("quizName");
        mViewPager.setOffscreenPageLimit(8);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRefAccounts = mFirebaseDatabase.getReference("Accounts");
        myRef = mFirebaseDatabase.getReference("Quizzes");
        getFirstQuestion();
            myRefAccounts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue(User.class).getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                        username = ds.getValue(User.class).getUserName();
                        Toast.makeText(QuizCreationActivity.this, "Username: " + username, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @BindAnim(R.anim.transparent_anim_appear)
    Animation transparent_anim_appear;

    @BindView(R.id.finishQuizBtn)
    Button finishQuizBtn;

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    @BindView(R.id.nextFragmentBtn)
    Button nextFragmentBtn;

    @BindView(R.id.previousFragmentBtn)
    Button previousFragmentBtn;

    @OnClick(R.id.finishQuizBtn)
    public void finishQuiz() {
        int numberOfQuestions;
        if (mFragmentsArrayList.size() == 2) {
            numberOfQuestions = 2;
        } else if (mFragmentsArrayList.size() == 3) {
            numberOfQuestions = 3;
        } else if (mFragmentsArrayList.size() == 4) {
            numberOfQuestions = 4;
        } else if (mFragmentsArrayList.size() == 5) {
            numberOfQuestions = 5;
        } else {
            numberOfQuestions = 0;
        }


        quiz = new Quiz(quizName, FirebaseAuth.getInstance().getCurrentUser().getEmail(), username);
        for (int n = 0; numberOfQuestions > n; n++) { // loop though all questions

            String a1 = mFragmentsArrayList.get(n).getAnswer1ET();  //Answer 1
            String a2 = mFragmentsArrayList.get(n).getAnswer2ET();  //Answer 2 etc
            String a3 = mFragmentsArrayList.get(n).getAnswer3ET();
            String a4 = mFragmentsArrayList.get(n).getAnswer4ET();
            String questionN = mFragmentsArrayList.get(n).getQuestionET();

            int correctAnswer = mFragmentsArrayList.get(n).getCorrectAnswer();

            if (a3.length() == 0) { //if question dont have answer3, then add only a1 / a2
                if (n == 0) {//question number, n0 is question 1
                    quiz.setQuestion1(questionN, a1, a2, correctAnswer);
                } else if (n == 1) {
                    quiz.setQuestion2(questionN, a1, a2, correctAnswer);
                } else if (n == 2) {
                    quiz.setQuestion3(questionN, a1, a2, correctAnswer);
                } else if (n == 3) {
                    quiz.setQuestion4(questionN, a1, a2, correctAnswer);
                } else if (n == 4) {
                    quiz.setQuestion5(questionN, a1, a2, correctAnswer);
                }
            } else if (a3.length() != 0 && a4.length() == 0) { //if question dont have answer4, but still got answer 3, then add answers a1/a2/a3
                if (n == 0) {
                    quiz.setQuestion1(questionN, a1, a2, a3, correctAnswer);
                } else if (n == 1) {
                    quiz.setQuestion2(questionN, a1, a2, a3, correctAnswer);
                } else if (n == 2) {
                    quiz.setQuestion3(questionN, a1, a2, a3, correctAnswer);
                } else if (n == 3) {
                    quiz.setQuestion4(questionN, a1, a2, a3, correctAnswer);
                } else if (n == 4) {
                    quiz.setQuestion5(questionN, a1, a2, a3, correctAnswer);
                }

            } else {
                if (n == 0) {   //if all answers are set, add all of them to the question
                    quiz.setQuestion1(questionN, a1, a2, a3, a4, correctAnswer);
                } else if (n == 1) {
                    quiz.setQuestion2(questionN, a1, a2, a3, a4, correctAnswer);
                } else if (n == 2) {
                    quiz.setQuestion3(questionN, a1, a2, a3, a4, correctAnswer);
                } else if (n == 3) {
                    quiz.setQuestion4(questionN, a1, a2, a3, a4, correctAnswer);
                } else if (n == 4) {
                    quiz.setQuestion5(questionN, a1, a2, a3, a4, correctAnswer);
                }
            }
        }
        myRef.child(quizName).setValue(quiz); // Our Quiz class got 5 object of Questions created, even if they are empty they will be added to Database, soo
        // here we simply deleting empty questions from database (Need to find better solution ofc, just fixed it like this atm)

        if (numberOfQuestions == 2) { // if out quiz got 2 questions filled up, we delete 3 of them
            DatabaseReference question3 = myRef.child(quiz.getQuizName()).child("question3");
            DatabaseReference question4 = myRef.child(quiz.getQuizName()).child("question4");
            DatabaseReference question5 = myRef.child(quiz.getQuizName()).child("question5");
            question3.removeValue();
            question4.removeValue();
            question5.removeValue();
        } else if (numberOfQuestions == 3) // etc
        {
            DatabaseReference question4 = myRef.child(quiz.getQuizName()).child("question4");
            DatabaseReference question5 = myRef.child(quiz.getQuizName()).child("question5");
            question4.removeValue();
            question5.removeValue();
        } else if (numberOfQuestions == 4) { // etc
            DatabaseReference test3 = myRef.child(quiz.getQuizName()).child("question5");
            test3.removeValue();
        }
        fragmentCounter = 0;
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Quiz is done, now we go to mainactivity, deleting QuizCreationActivity from stack
        startActivity(intent);
        finish();
    }
    public void getFirstQuestion(){
        mViewPager.setVisibility(View.VISIBLE);
        mFragmentsArrayList.add(new mFragment());
        while (fragmentNr != mFragmentsArrayList.size()) {
            adapter.addFragment((mFragmentsArrayList.get(fragmentNr)));
            fragmentNr++;
        }
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(fragmentNr);
        fragmentCounter++;
    }

    @OnClick(R.id.nextFragmentBtn)
    public void nextFragment() {
        if (fragmentCounter < 5) {
            mViewPager.setVisibility(View.VISIBLE);
            mFragmentsArrayList.add(new mFragment());
            while (fragmentNr != mFragmentsArrayList.size()) {
                adapter.addFragment((mFragmentsArrayList.get(fragmentNr)));
                fragmentNr++;
            }
            mViewPager.setAdapter(adapter);
            mViewPager.setCurrentItem(fragmentNr);
            fragmentCounter++;
            keepAnswers();
        }
    }

    public void isComplted(boolean isCompleted, int fragmentID) {
        nextFragmentBtn.setVisibility(View.INVISIBLE);
        switch (fragmentID) {
            case 0:
                frag1 = isCompleted;
                break;

            case 1:
                frag2 = isCompleted;
                break;
            case 2:
                frag3 = isCompleted;
                break;
            case 3:
                frag4 = isCompleted;
                break;
            case 4:
                frag5 = isCompleted;
                break;
            default:
                Toast.makeText(this, "Something is wrong", Toast.LENGTH_SHORT).show();
                break;
        }
        if (mFragmentsArrayList.size() == 1) {
            if (frag1) {
                nextFragmentBtn.setVisibility(View.VISIBLE);
            } else {
                nextFragmentBtn.setVisibility(View.INVISIBLE);
            }
        }
        if (mFragmentsArrayList.size() == 2) {
            if (frag1 && frag2) {
                nextFragmentBtn.setVisibility(View.VISIBLE);
                finishQuizBtn.setVisibility(View.VISIBLE);
            } else {
                nextFragmentBtn.setVisibility(View.INVISIBLE);
                finishQuizBtn.setVisibility(View.INVISIBLE);
            }
        }
        if (mFragmentsArrayList.size() == 3) {
            if (frag1 && frag2 && frag3) {
                nextFragmentBtn.setVisibility(View.VISIBLE);
                finishQuizBtn.setVisibility(View.VISIBLE);
            } else {
                nextFragmentBtn.setVisibility(View.INVISIBLE);
                finishQuizBtn.setVisibility(View.INVISIBLE);
            }
        }
        if (mFragmentsArrayList.size() == 4) {
            if (frag1 && frag2 && frag3 && frag4) {
                nextFragmentBtn.setVisibility(View.VISIBLE);
                finishQuizBtn.setVisibility(View.VISIBLE);
            } else {
                nextFragmentBtn.setVisibility(View.INVISIBLE);
                finishQuizBtn.setVisibility(View.INVISIBLE);
            }
        }
        if (mFragmentsArrayList.size() == 5) {
            if (frag1 && frag2 && frag3 && frag4 && frag5) {
                nextFragmentBtn.setVisibility(View.VISIBLE);
                finishQuizBtn.setVisibility(View.VISIBLE);
            } else {
                nextFragmentBtn.setVisibility(View.INVISIBLE);
                finishQuizBtn.setVisibility(View.INVISIBLE);
            }
        }
    }


    public void keepAnswers() {
        int i = 0;
        while (i != mFragmentsArrayList.size()) {
            mFragmentsArrayList.get(i).keepChoices();
            i++;
        }
    }
}
