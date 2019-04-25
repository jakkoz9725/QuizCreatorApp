package com.example.quizcreator.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizcreator.Classes.DialogOverride;
import com.example.quizcreator.Classes.Patterns;
import com.example.quizcreator.Classes.Quiz;
import com.example.quizcreator.Classes.User;
import com.example.quizcreator.QuizArrayListAdapter;
import com.example.quizcreator.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    boolean quizNameIsCorrect = false;
    Patterns patterns;
    private String currentlyLoggedInUserName;
    private DatabaseReference databaseReference;
    private DatabaseReference myRefAccounts;
    private FirebaseAuth mAuth;

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
        patterns = new Patterns(this);
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference("Quizzes");
        myRefAccounts = mFirebaseDatabase.getReference("Accounts");
        mAuth = FirebaseAuth.getInstance();
        getCurrentUsername();
    }


    public void startAnimation(Animation animation, View v, View v2) {
        animation.setAnimationListener(new AnimationListener() {
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
    ConstraintLayout blockerLayout;

    @BindView(R.id.searchListET)
    EditText searchListET;

    @BindView(R.id.menuConstraintLayout)
    ConstraintLayout menuConstraintLayout;

    @BindView(R.id.listOfQuizzesConstraintLayout)
    ConstraintLayout listOfQuizzesConstraintLayout;

    @BindView(R.id.listOfQuizesLV)
    ListView listOfQuizzesLV;
    
    @BindView(R.id.listOfMyQuizzesLV)
    ListView listOfMyQuizzesLV;

    @BindView(R.id.listOfQuizzesBT)
    Button listOfQuizzes;

    @BindView(R.id.createNewQuizBT)
    Button createNewQuizBT;

    @BindView(R.id.listOfMyQuizesBtn)
    Button listOfMyQuizesBtn;

    @BindView(R.id.settingsLayout)
    ConstraintLayout settingsLayout;

    @BindView(R.id.currectLoggedUserEmailT)
    TextView currectLoggedUserEmailT;

    @BindView(R.id.logoutBT)
    Button logoutBT;

    public void getCurrentUsername() {
        myRefAccounts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue(User.class).getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                        currentlyLoggedInUserName = ds.getValue(User.class).getUserName();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @OnClick(R.id.backToMenuBT)
    public void onClickBackToMenuBT() {
        listOfQuizzesConstraintLayout.setVisibility(View.GONE);
        listOfQuizzesLV.setVisibility(View.GONE);
        listOfMyQuizzesLV.setVisibility(View.GONE);
        menuConstraintLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.listOfQuizzesBT)
    public void showAllQuizes() {
        listOfQuizzesLV.setVisibility(View.VISIBLE);
        startAnimation(transparent_anim_disapear, menuConstraintLayout, listOfQuizzesConstraintLayout);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quizList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Quiz quiz = new Quiz();
                    quiz.setCreatorUsername(Objects.requireNonNull(ds.getValue(Quiz.class)).getCreatorUsername());
                    quiz.setQuizName(Objects.requireNonNull(ds.getValue(Quiz.class)).getQuizName());
                    quizList.add(quiz);
                }
                QuizArrayListAdapter adapter = new QuizArrayListAdapter(MenuActivity.this, quizList);
                listOfQuizzesLV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.listOfMyQuizesBtn)
    public void showMyQuizes() {
        listOfMyQuizzesLV.setVisibility(View.VISIBLE);
        startAnimation(transparent_anim_disapear, menuConstraintLayout, listOfQuizzesConstraintLayout);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quizList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Quiz quiz = new Quiz();
                    quiz.setCreatorUsername(Objects.requireNonNull(ds.getValue(Quiz.class)).getCreatorUsername());
                    quiz.setQuizName(Objects.requireNonNull(ds.getValue(Quiz.class)).getQuizName());
                    if(quiz.getCreatorUsername().equals(currentlyLoggedInUserName)) {
                        quizList.add(quiz);
                    }
                }

                QuizArrayListAdapter adapter = new QuizArrayListAdapter(MenuActivity.this, quizList);
                listOfMyQuizzesLV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("SetTextI18n")

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
        closeDialogueBT.setOnClickListener(v -> dialog.dismiss());
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
                    if (Objects.requireNonNull(ds.getValue(Quiz.class)).getQuizName().toLowerCase().contains(informationToSearch.toLowerCase()) ||
                            Objects.requireNonNull(ds.getValue(Quiz.class)).getCreatorUsername().toLowerCase().contains(informationToSearch.toLowerCase())) {
                        Quiz quiz = new Quiz();
                        quiz.setCreatorUsername(Objects.requireNonNull(ds.getValue(Quiz.class)).getCreatorUsername());
                        quiz.setQuizName(Objects.requireNonNull(ds.getValue(Quiz.class)).getQuizName());
                        quizList.add(quiz);
                    }
                }
                QuizArrayListAdapter adapter = new QuizArrayListAdapter(MenuActivity.this, quizList);
                listOfQuizzesLV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.createNewQuizBT)
    public void createNewQuiz() {
        dialog = new DialogOverride.Builder()
                .context(this)
                .top_animation_disappear(from_top_disappear)
                .bottom_animation_disappear(from_bottom_dissapear)
                .top_animation_appear(from_top_appear)
                .bottom_animation_appear(from_bottom_appear)
                .build();

        dialog.setContentView(R.layout.newquiz_dialogue);
        Button createQuizBTdialogue = dialog.findViewById(R.id.createQuizBTdialogue);
        ConstraintLayout firstPiece = dialog.findViewById(R.id.firstPiece);
        ConstraintLayout secondPiece = dialog.findViewById(R.id.secondPiece);
        TextView quizNameRequirement = dialog.findViewById(R.id.quizNameRequirement);
        EditText quizNameETdialogue = dialog.findViewById(R.id.loggedUserEmailT);
        ConstraintLayout constraintLayout = dialog.findViewById(R.id.dialogConstraintLayout);
        ProgressBar quizCreationPB = dialog.findViewById(R.id.quizCreationPB);

        int startColor = quizNameRequirement.getCurrentTextColor();

        ((DialogOverride) dialog).setFirstPartOfDialogue(firstPiece);
        ((DialogOverride) dialog).setSecondPartOfDialogue(secondPiece);

        quizNameETdialogue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                quizNameIsCorrect = patterns.isDataCorrect(quizNameETdialogue.getText().toString(), patterns.getUserNamePattern()); // same pattern as for username
                if (quizNameIsCorrect) {
                    quizNameRequirement.setTextColor(getColor(R.color.green));
                } else {
                    quizNameRequirement.setTextColor(startColor);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        createQuizBTdialogue.setOnClickListener(v -> {
            if (quizNameIsCorrect) {
                checkIfQuizNameAlreadyExist(quizNameETdialogue.getText().toString(), dialog, quizCreationPB);
            } else {
                Toast.makeText(this, "Check requirement", Toast.LENGTH_SHORT).show();
            }
        });
        constraintLayout.startAnimation(transparent_anim_appear);
        constraintLayout.clearAnimation();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public void checkIfQuizNameAlreadyExist(String quizName, Dialog dialog, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        List<String> quizzesNames = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    quizzesNames.add(Objects.requireNonNull(ds.getValue(Quiz.class)).getQuizName().toLowerCase());
                }
                if (quizzesNames.contains(quizName.toLowerCase())) {
                    Toast.makeText(MenuActivity.this, "Quiz named " + quizName + " already exist", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), QuizCreationActivity.class);
                    intent.putExtra("quizName", quizName);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    public void lettersAnimation(Animation animation, ArrayList<TextView> letters, int letterNr) {
//        if (letterNr != (letters.size())) {
//            animation.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//                    letters.get(letterNr).startAnimation(animation);
//                    letters.get(letterNr).setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//
//                    int newLetterNr = letterNr + 1;
//                    lettersAnimation(animation, letters, newLetterNr);
//                    letters.get(letterNr).clearAnimation();
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//            letters.get(letterNr).startAnimation(animation);
//        }else{
//            if(!animationsDone) {
//                animationsDone = true;
//                lettersAnimation(letters_animation_appear, secondRowLetters, 0);
//            }
//        }
//    }


    @OnClick(R.id.settingsBT)
    public void onSettingsBtnClick() {
        currectLoggedUserEmailT.setText(currentUserEmail);
        blockerLayout.setVisibility(View.VISIBLE);
        settingsLayout.setVisibility(View.VISIBLE);
        settingsLayout.startAnimation(settings_menu_appear);
        blockerLayout.setClickable(true);
    }

    @OnClick(R.id.logoutBT)
    public void onLogOutBtnClick() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.blockerLayout)
    public void onBlockLayoutClick() {
        if (settingsLayout.getVisibility() == View.VISIBLE) {
            settings_menu_disappear.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    blockerLayout.setClickable(false);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    blockerLayout.setVisibility(View.INVISIBLE);
                    settingsLayout.setVisibility(View.INVISIBLE);
                    settingsLayout.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            settingsLayout.startAnimation(settings_menu_disappear);

        }
    }

    @BindAnim(R.anim.from_top_appear)
    Animation from_top_appear;

    @BindAnim(R.anim.from_bottom_appear)
    Animation from_bottom_appear;

    @BindAnim(R.anim.from_bottom_dissapear)
    Animation from_bottom_dissapear;

    @BindAnim(R.anim.from_top_disappear)
    Animation from_top_disappear;

    @BindAnim(R.anim.myanim)
    Animation testAnim;

    @BindAnim(R.anim.transparent_anim_appear)
    Animation transparent_anim_appear;

    @BindAnim(R.anim.transparent_anim_disapear)
    Animation transparent_anim_disapear;

    @BindAnim(R.anim.letters_animation_appear)
    Animation letters_animation_appear;

    @BindAnim(R.anim.flash_anim)
    Animation flashAnimation;

    @BindAnim(R.anim.settings_menu_appear)
    Animation settings_menu_appear;

    @BindAnim(R.anim.settings_menu_disappear)
    Animation settings_menu_disappear;
}
