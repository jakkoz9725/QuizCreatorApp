package com.example.quizcreator.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizcreator.Classes.Quiz;
import com.example.quizcreator.QuizArrayListAdapter;
import com.example.quizcreator.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MenuActivity extends Activity {

    String currentUserEmail;
    Dialog dialog;
    List<Quiz> quizList;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu_layout);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        quizList = new ArrayList<>();
        currentUserEmail = intent.getStringExtra("email");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference("Quizzes");


    }


    public void startAnimation(Animation animation, View v, View v2) {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v2.setVisibility(View.VISIBLE);
                v2.startAnimation(transparent_anim_appear);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(animation);
    }

    @BindView(R.id.blockerLayout)
    LinearLayout blockerLayout;

    @BindView(R.id.searchListET)
    EditText searchListET;

    @BindView(R.id.menuConstraintLayout)
    ConstraintLayout menuConstraintLayout;

    @BindView(R.id.listOfQuizesConstraintLayout)
    ConstraintLayout listOfQuizesConstraintLayout;

    @BindView(R.id.listOfQuizesLV)
    ListView listOfQuizesLV;

    @BindView(R.id.listOfQuizesBT)
    Button listOfQuizesBT;

    @BindView(R.id.createNewQuizBT)
    Button createNewQuizBT;

    @OnClick(R.id.backToMenuBT)
    public void onClickBackToMenuBT() {
        listOfQuizesConstraintLayout.setVisibility(View.GONE);
        menuConstraintLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.listOfQuizesBT)
    public void showAllQuizes() {
        startAnimation(transparent_anim_disapear, menuConstraintLayout, listOfQuizesConstraintLayout);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quizList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Quiz quiz = new Quiz();
                    quiz.setCreatorUsername(ds.getValue(Quiz.class).getCreatorUsername());
                    quiz.setQuizName(ds.getValue(Quiz.class).getQuizName());
                    quizList.add(quiz);
                }
                QuizArrayListAdapter adapter = new QuizArrayListAdapter(MenuActivity.this, quizList);
                listOfQuizesLV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnItemClick(R.id.listOfQuizesLV)
    public void onItemListClick(int i) {
        Quiz quiz = quizList.get(i);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.checkquiz_dialogue);
        TextView quizName = dialog.findViewById(R.id.quizNameT);
        TextView creatorName = dialog.findViewById(R.id.creatorNameT);

        quizName.setText("Quiz name : " + quiz.getQuizName());
        creatorName.setText("Creator name : " + quiz.getCreatorUsername());

        Button startQuizBT = dialog.findViewById(R.id.startQuizBT);
        Button closeDialogueBT = dialog.findViewById(R.id.closeDialogueBT);

        startQuizBT.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuizReadingActivity.class);
            intent.putExtra("quizName", quiz.getQuizName());
            intent.putExtra("creatorName", quiz.getCreatorUsername());
            startActivity(intent);
        });
        closeDialogueBT.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @OnClick(R.id.searchListBT)
    public void searchQuizzes() {
        String informationToSearch = searchListET.getText().toString();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quizList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue(Quiz.class).getQuizName().toLowerCase().contains(informationToSearch.toLowerCase()) ||
                            ds.getValue(Quiz.class).getCreatorUsername().toLowerCase().contains(informationToSearch.toLowerCase())) {
                        Quiz quiz = new Quiz();
                        quiz.setCreatorUsername(ds.getValue(Quiz.class).getCreatorUsername());
                        quiz.setQuizName(ds.getValue(Quiz.class).getQuizName());
                        quizList.add(quiz);
                    }
                }
                QuizArrayListAdapter adapter = new QuizArrayListAdapter(MenuActivity.this, quizList);
                listOfQuizesLV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @OnClick(R.id.createNewQuizBT)
    public void createNewQuiz() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.newquiz_dialogue);
        Button createQuizBTdialogue = dialog.findViewById(R.id.createQuizBTdialogue);
        TextView letterQ = dialog.findViewById(R.id.letterQ);
        TextView letterU = dialog.findViewById(R.id.letterU);
        TextView letterI = dialog.findViewById(R.id.letterI);
        TextView letterZ = dialog.findViewById(R.id.letterZ);
        EditText quizNameETdialogue = dialog.findViewById(R.id.quizNameETdialogue);

        ConstraintLayout constraintLayout = dialog.findViewById(R.id.dialogConstraintLayout);
        createQuizBTdialogue.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuizCreationActivity.class);
            intent.putExtra("quizName", quizNameETdialogue.getText().toString());
            startActivity(intent);
        });
        constraintLayout.startAnimation(transparent_anim_appear);
        constraintLayout.clearAnimation();
        dialog.show();
        ArrayList<TextView> letters = new ArrayList<>();
        letters.add(letterQ);
        letters.add(letterU);
        letters.add(letterI);
        letters.add(letterZ);
        lettersAnimation(transparent_anim_appear, letters, 0);
    }

    public void lettersAnimation(Animation animation, ArrayList<TextView> letters, int letterNr) {
        if (letterNr != (letters.size())) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    letters.get(letterNr).setVisibility(View.VISIBLE);
                    letters.get(letterNr).startAnimation(animation);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    int newLetterNr = letterNr + 1;
                    lettersAnimation(animation, letters, newLetterNr);
                    letters.get(letterNr).clearAnimation();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            letters.get(letterNr).startAnimation(animation);
        }
    }

    @OnClick(R.id.settingsBT)
    public void onSettingsBtnClick() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.settings_dialogue);
        Button logoutDialogBT = dialog.findViewById(R.id.logoutDialogueBT);
        TextView userEmailDialogueT = dialog.findViewById(R.id.quizNameETdialogue);
        userEmailDialogueT.setText(currentUserEmail);

        logoutDialogBT.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        dialog.show();
    }

    @BindAnim(R.anim.myanim)
    Animation testAnim;

    @BindAnim(R.anim.transparent_anim_appear)
    Animation transparent_anim_appear;

    @BindAnim(R.anim.transparent_anim_disapear)
    Animation transparent_anim_disapear;

    @BindAnim(R.anim.flash_anim)
    Animation flashAnimation;
}
