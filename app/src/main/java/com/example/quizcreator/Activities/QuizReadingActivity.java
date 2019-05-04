package com.example.quizcreator.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizcreator.Classes.DialogOverride;
import com.example.quizcreator.Classes.Question;
import com.example.quizcreator.Classes.Quiz;
import com.example.quizcreator.CorrectAnswersListAdapter;
import com.example.quizcreator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindAnim;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

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
        overridePendingTransition(R.anim.transparent_anim_appear, R.anim.transparent_anim_disapear);
    }

    @Override
    protected void onStart() {
        questionsConstraintLayout.setVisibility(View.VISIBLE);
        questionsConstraintLayout.startAnimation(appear_animation);
        super.onStart();
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setQuestionToUI(int actualQuestion) {
        quizNameT.setText(quizName);
        quizQuestionT.setText(questionList.get(actualQuestion).getQuestion());
        quizAnswerOneBT.setText(questionList.get(actualQuestion).getAnswer1());
        quizAnswerTwoBT.setText(questionList.get(actualQuestion).getAnswer2());
        quizAnswerThreeBT.setText(questionList.get(actualQuestion).getAnswer3());
        quizAnswerFourBT.setText(questionList.get(actualQuestion).getAnswer4());
        checkNumberOfAnswers();
    }

    public void checkNumberOfAnswers() {
        if (quizAnswerFourBT.getText().equals("")) {
            quizAnswerFourBT.setVisibility(View.GONE);
        } else {
            quizAnswerFourBT.setVisibility(View.VISIBLE);
        }
        if (quizAnswerThreeBT.getText().equals("")) {
            quizAnswerThreeBT.setVisibility(View.GONE);
        } else {
            quizAnswerThreeBT.setVisibility(View.VISIBLE);
        }
    }

    public void checkCorrectAnswer(int buttonAnswer, String userAnswer) {
        if (buttonAnswer == questionList.get(actualQuestion).getCorrectAnswer()) {
            quizPoints++;
        }
        questionList.get(actualQuestion).setUserAnswer(userAnswer);
        if ((actualQuestion + 1) == questionList.size()) {
            CorrectAnswersListAdapter adapter = new CorrectAnswersListAdapter(QuizReadingActivity.this, questionList);
            listOfAnswers.setAdapter(adapter);


            questionsConstraintLayout.setVisibility(View.GONE);
            listConstraintLayout.setVisibility(View.VISIBLE);
            setFinalScore(quizPoints, questionList.size());
            listConstraintLayout.startAnimation(quiz_reading_appear_animation_part2);
        } else {
            questionsConstraintLayout.startAnimation(quiz_reading_appear_animation_part2);
            actualQuestion++;
            setQuestionToUI(actualQuestion);
        }

    }

    public void animationAfterAnswerPick(int whichButton, String answerText) {
        quiz_reading_appear_animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                checkCorrectAnswer(whichButton, answerText);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        questionsConstraintLayout.startAnimation(quiz_reading_appear_animation);
    }

    public void setFinalScore(int points, int numberOfQuestions) {
        finalScore.setText("Your score is : " + points + "/" + numberOfQuestions);
    }

    @OnClick(R.id.quizAnswerOneBT)
    public void quizAnswerOneBTanswer() {
        animationAfterAnswerPick(1, quizAnswerOneBT.getText().toString());
    }

    @OnClick(R.id.quizAnswerTwoBT)
    public void quizAnswerTwoBTanswer() {
        animationAfterAnswerPick(2, quizAnswerTwoBT.getText().toString());
    }

    @OnClick(R.id.quizAnswerThreeBT)
    public void quizAnswerThreeBTanswer() {
        animationAfterAnswerPick(3, quizAnswerThreeBT.getText().toString());
    }

    @OnClick(R.id.quizAnswerFourBT)
    public void quizAnswerFourBTanswer() {
        animationAfterAnswerPick(4, quizAnswerFourBT.getText().toString());
    }

    @BindView(R.id.questionsConstraintLayout)
    ConstraintLayout questionsConstraintLayout;


    @BindView(R.id.listConstraintLayout)
    ConstraintLayout listConstraintLayout;

    @BindView(R.id.finalScore)
    TextView finalScore;

    @BindView(R.id.listOfAnswersLV)
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

    @BindAnim(R.anim.transparent_anim_appear)
    Animation appear_animation;

    @BindAnim(R.anim.quiz_reading_appear_animation)
    Animation quiz_reading_appear_animation;


    @BindAnim(R.anim.quiz_reading_appear_animation_part2)
    Animation quiz_reading_appear_animation_part2;

    @OnItemClick(R.id.listOfAnswersLV)
    public void onItemClick(int i) {
        Question question = questionList.get(i);
        Dialog dialog = new DialogOverride.Builder()
                .context(this)
                .top_animation_disappear(from_top_disappear)
                .bottom_animation_disappear(from_bottom_dissapear)
                .top_animation_appear(from_top_appear)
                .bottom_animation_appear(from_bottom_appear)
                .build();

        dialog.setContentView(R.layout.question_correct_answer_check);
        ConstraintLayout firstPiece = dialog.findViewById(R.id.firstPiece);
        ConstraintLayout secondPiece = dialog.findViewById(R.id.secondPiece);
        TextView questionTextView = dialog.findViewById(R.id.questionTV);
        TextView userAnswerTextView = dialog.findViewById(R.id.answerTV);
        TextView correctAnswerTextView = dialog.findViewById(R.id.correctAnswer);
        Button showCorrectAnswer = dialog.findViewById(R.id.showCorrectAnswer);
        questionTextView.setText("Question \n" + question.getQuestion());
        userAnswerTextView.setText("Your answer \n" + question.getUserAnswer());
        correctAnswerTextView.setVisibility(View.GONE);
        if (question.getUserAnswer().equals(getCorrectAnswer(question))) {
            userAnswerTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct_answer_ic, 0);
        } else {
            userAnswerTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.wrong_answer_ic, 0);
            correctAnswerTextView.setText("Correct answer \n" + getCorrectAnswer(question));
        }
        ((DialogOverride) dialog).setFirstPartOfDialogue(firstPiece);
        ((DialogOverride) dialog).setSecondPartOfDialogue(secondPiece);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        showCorrectAnswer.setOnClickListener(v -> {
            correctAnswerTextView.setVisibility(View.VISIBLE);
            showCorrectAnswer.setVisibility(View.GONE);
        });


        dialog.show();
    }

    public String getCorrectAnswer(Question question) {
        String correctAnswerString = "";
        if (question.getCorrectAnswer() == 1) {
            correctAnswerString = question.getAnswer1();
        } else if (question.getCorrectAnswer() == 2) {
            correctAnswerString = question.getAnswer2();
        } else if (question.getCorrectAnswer() == 3) {
            correctAnswerString = question.getAnswer3();
        } else if (question.getCorrectAnswer() == 4) {
            correctAnswerString = question.getAnswer4();
        }
        return correctAnswerString;
    }

    @BindAnim(R.anim.from_top_appear)
    Animation from_top_appear;

    @BindAnim(R.anim.from_bottom_appear)
    Animation from_bottom_appear;

    @BindAnim(R.anim.from_bottom_dissapear)
    Animation from_bottom_dissapear;

    @BindAnim(R.anim.from_top_disappear)
    Animation from_top_disappear;

}
