package com.example.quizcreator.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quizcreator.Classes.Quiz;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuActivity extends Activity {

    String currentUserEmail;
    Dialog dialog;
    List<Quiz> quizList;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReference;

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

    @BindView(R.id.menuConstraintLayout)
    ConstraintLayout menuConstraintLayout;

    @BindView(R.id.listOfQuizesConstraintLayout)
    ConstraintLayout listOfQuizes;

    @BindView(R.id.listOfQuizesLV)
    ListView listOfQuizesLV;

    @BindView(R.id.listOfQuizesBT)
    Button listOfQuizesBT;

    @OnClick(R.id.listOfQuizesBT)
    public void showAllQuizes() {
        databaseReference.addValueEventListener(new ValueEventListener() {
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

        listOfQuizes.setVisibility(View.VISIBLE);
        menuConstraintLayout.setVisibility(View.GONE);
    }


    @OnClick(R.id.createNewQuizBT)
    public void createNewQuiz() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.newquiz_dialogue);
        Button createNewQuiz = dialog.findViewById(R.id.createQuizBTdialogue);
        EditText quizNameET = dialog.findViewById(R.id.quizNameETdialogue);

        createNewQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuizCreationActivity.class);
            intent.putExtra("quizName", quizNameET.getText().toString());
            startActivity(intent);
        });
        dialog.show();
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
}
