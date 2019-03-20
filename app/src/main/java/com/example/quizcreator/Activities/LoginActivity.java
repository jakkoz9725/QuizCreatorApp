package com.example.quizcreator.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizcreator.Classes.User;
import com.example.quizcreator.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindAnim;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String loggedInAs;
    private Pattern userNamePattern, emailPattern;
    private Matcher matcher;

    @BindView(R.id.messageTV)
    TextView startMessageTV;
    @BindView(R.id.loginAsTV)
    TextView loginAsTV;
    @BindView(R.id.continueAsBT)
    Button continueAsBT;
    @BindView(R.id.logoutCT)
    TextView logoutAtStartCT;
    @BindView(R.id.loadingUserPB)
    ProgressBar loadingUserPB;
    @BindView(R.id.welcomeImage)
    ImageView welcomeImage;


    @BindView(R.id.blockerLayout)
    LinearLayout blockerLayout;
    @BindView(R.id.loginLayout)
    ConstraintLayout loginLayout;
    @BindView(R.id.registerLayout)
    ConstraintLayout registerLayout;
    @BindView(R.id.appStartLayout)
    ConstraintLayout appStartLayout;


    @BindView(R.id.userEmailET)
    EditText userEmailET;
    @BindView(R.id.userPasswordET)
    EditText userPasswordET;
    @BindView(R.id.signInBtn)
    Button acceptBT;
    @BindView(R.id.createAccCT)
    TextView createAccCT;

    @BindDrawable(R.drawable.ghost_emoji)
    Drawable ghostEmoji;
    @BindView(R.id.imageViewGhostEmoji)
    ImageView imageViewGhostEmoji;

    @BindView(R.id.loginInPB)
    ProgressBar loginInPB;
    @BindView(R.id.newUsernameET)
    EditText newUsernameET;
    @BindView(R.id.newUserEmailET)
    EditText newUserEmailET;
    @BindView(R.id.newUserPasswordET)
    EditText newUserPasswordET;
    @BindView(R.id.newUserPasswordRepET)
    EditText newUserPasswordRepET;
    @BindView(R.id.loginCT)
    TextView loginCT;
    @BindAnim(R.anim.transparent_anim_appear)
    Animation transparent_anim;
    @BindAnim(R.anim.fromthesky_anim)
    Animation fromthesky_anim;
    @BindAnim(R.anim.transparent_anim_disapear)
    Animation transparent_anim_disapear;
    @BindAnim(R.anim.transparent_anim_appear)
    Animation transparent_anim_appear;
    @BindAnim(R.anim.ghost_emoji_anim)
    Animation ghost_emoji_anim;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_menu_layout);

        userNamePattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
                "|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])" +
                "*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]" +
                "?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\" +
                "x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",Pattern.CASE_INSENSITIVE); // pattern from http://emailregex.com/

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Accounts");

        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        imageViewGhostEmoji.setImageDrawable(ghostEmoji);
        scarryGhostAnim(ghost_emoji_anim, imageViewGhostEmoji);

    }

    public void scarryGhostAnim(Animation animation, View view) {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                scarryGhostAnim(ghost_emoji_anim, imageViewGhostEmoji);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.setVisibility(View.INVISIBLE);
        view.startAnimation(animation);
    }

    @OnClick(R.id.logoutCT)
    public void onLogoutCTclick() {
        FirebaseAuth.getInstance().signOut();
        transparent_anim_disapear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                appStartLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loginLayout.setVisibility(View.VISIBLE);
                loginLayout.startAnimation(transparent_anim_appear);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        appStartLayout.startAnimation(transparent_anim_disapear);
    }

    @OnClick(R.id.createAccCT)
    public void OnClickCreateNewAcc() {
        startAnimation(transparent_anim_appear, registerLayout, transparent_anim_disapear, loginLayout);
    }

    @OnClick(R.id.loginCT)
    public void OnClickNext() {
        startAnimation(transparent_anim_appear, loginLayout, transparent_anim_disapear, registerLayout);
    }

    @OnClick(R.id.createAccBT)
    public void createNewUser() {
        String newUserEmail = newUserEmailET.getText().toString();
        String newUserPassword = newUserPasswordET.getText().toString();
        String newUserPasswordRep = newUserPasswordRepET.getText().toString();
        String newUsername = newUsernameET.getText().toString();
       // isDataCorrect(newUsername, userNamePattern);
        isDataCorrect(newUserEmail,emailPattern);
       /* if (newUserPassword.equals(newUserPasswordRep)) {
            mAuth.createUserWithEmailAndPassword(newUserEmail, newUserPassword).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    User user = new User(newUserEmail, newUsername, newUserPassword);
                    databaseReference.child(newUsername).setValue(user);
                    Toast.makeText(LoginActivity.this, "Created Succesfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.w("LoginActivity", "Failed", task.getException());
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
        }*/
    }

    @OnClick(R.id.signInBtn)
    public void signIn() {
        loginInPB.setVisibility(View.VISIBLE);
        String email = userEmailET.getText().toString();
        String password = userPasswordET.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        loginInPB.setVisibility(View.GONE);
                        Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(this, MenuActivity.class);
                        intent.putExtra("email", user.getEmail());
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        loginInPB.setVisibility(View.GONE);
                        task.getException().printStackTrace();
                        Toast.makeText(this, "Email or password incorrect", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick(R.id.continueAsBT)
    public void onContinueAsBTclick() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("email", mAuth.getCurrentUser().getEmail());
        startActivity(intent);
        overridePendingTransition(R.anim.transparent_anim_appear, R.anim.transparent_anim_disapear);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            welcomeImage.setVisibility(View.VISIBLE);
            appStartLayout.setVisibility(View.VISIBLE);
            loadingUserPB.setVisibility(View.VISIBLE);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getValue(User.class).getEmail().equals(currentUser.getEmail())) {
                            loggedInAs = ds.getValue(User.class).getUserName();
                        } else {
                            continue;
                        }
                    }
                    loginAsTV.setVisibility(View.VISIBLE);
                    startMessageTV.setVisibility(View.VISIBLE);
                    continueAsBT.setVisibility(View.VISIBLE);
                    logoutAtStartCT.setVisibility(View.VISIBLE);
                    loadingUserPB.setVisibility(View.GONE);
                    loginAsTV.setText("Log in as " + loggedInAs);
                    continueAsBT.setText("CONTINUE");
                    startMessageTV.setText("Do you want to continue?");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            loginLayout.setVisibility(View.VISIBLE);
            loginLayout.startAnimation(transparent_anim_appear);
        }
    }

    public boolean matcher(String word, Pattern pattern) {
        matcher = pattern.matcher(word);
        return matcher.find();
    }

    public boolean isDataCorrect(String userSendData, Pattern pattern) {
        if(pattern.equals(userNamePattern)) {
            if (userSendData.length() < 5) {
                Toast.makeText(this, userMessagesForLogAndReg(pattern, 1), Toast.LENGTH_SHORT).show();
                return false;
            } else if (userSendData.length() > 10) {
                Toast.makeText(this, userMessagesForLogAndReg(pattern, 2), Toast.LENGTH_SHORT).show();
                return false;
            } else if (matcher(userSendData, pattern)) {
                Toast.makeText(this, userMessagesForLogAndReg(pattern, 3), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Toast.makeText(this, userMessagesForLogAndReg(pattern, 4), Toast.LENGTH_SHORT).show();
                return true;
            }
        } else if (pattern.equals(emailPattern)) {
            if(!matcher(userSendData,pattern)){
                Toast.makeText(this, userMessagesForLogAndReg(emailPattern,1), Toast.LENGTH_SHORT).show();
                return false;
            }else{
                Toast.makeText(this, userMessagesForLogAndReg(emailPattern,2), Toast.LENGTH_SHORT).show();
                return true;
            }
        }
       return false;
    }
    public String userMessagesForLogAndReg(Pattern pattern, int whichOne) {
        if (pattern.equals(userNamePattern)) {
            switch (whichOne) {
                case 1:
                    return "Your username need to be between 5-10 characters";
                case 2:
                    return "Your username need to be between 5-10 characters";
                case 3:
                    return "You can only use letters and numbers for username";
                case 4:
                    return "Well done, checking data";
            }
        }else if(pattern.equals(emailPattern)){
            switch (whichOne){
                case 1:
                    return "email is incorrect";
                case 2:
                    return "Good";

            }
        }
        return null;
    }

    public void startAnimation(Animation animationApear, View viewApear, Animation animationDisapear, View viewDisapear) {
        animationDisapear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                blockerLayout.setVisibility(View.VISIBLE);
                viewDisapear.setVisibility(View.GONE);
                viewDisapear.startAnimation(animation);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                blockerLayout.setVisibility(View.GONE);
                viewApear.setVisibility(View.VISIBLE);
                viewApear.setClickable(true);
                viewApear.startAnimation(animationApear);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        viewDisapear.startAnimation(animationDisapear);
    }
}
