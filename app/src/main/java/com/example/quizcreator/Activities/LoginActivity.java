package com.example.quizcreator.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String loggedInAs;

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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_menu_layout);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Accounts");
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.createAccCT)
    public void OnClick(){
        loginLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.loginCT)
    public void OnClickNext(){
        registerLayout.setVisibility(View.GONE);
        loginLayout.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.createAccBT)
    public void createNewUser(){
        String newUserEmail = newUserEmailET.getText().toString();
        String newUserPassword =  newUserPasswordET.getText().toString();
        String newUserPasswordRep =  newUserPasswordRepET.getText().toString();
        String newUsername = newUsernameET.getText().toString();
        if(newUserPassword.equals(newUserPasswordRep)) {
            mAuth.createUserWithEmailAndPassword(newUserEmail,newUserPassword).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    User user = new User(newUserEmail,newUsername,newUserPassword);
                    databaseReference.child(newUsername).setValue(user);
                    Toast.makeText(LoginActivity.this, "Created Succesfully", Toast.LENGTH_SHORT).show();
                }else{
                    Log.w("LoginActivity","Failed", task.getException());
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.signInBtn)
    public void signIn(){
        String email = userEmailET.getText().toString();
        String password = userPasswordET.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(this, MenuActivity.class);
                        intent.putExtra("email",user.getEmail());
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        task.getException().printStackTrace();
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @OnClick(R.id.continueAsBT)
    public void onContinueAsBTclick(){
        Intent intent = new Intent(this,MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("email",mAuth.getCurrentUser().getEmail());
        startActivity(intent);
        overridePendingTransition(R.anim.transparent_anim_appear,R.anim.transparent_anim_disapear);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            appStartLayout.setVisibility(View.VISIBLE);
            loadingUserPB.setVisibility(View.VISIBLE);
            startAnimation(fromthesky_anim,welcomeImage);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(ds.getValue(User.class).getEmail().equals(currentUser.getEmail())){
                            loggedInAs = ds.getValue(User.class).getUserName();
                        }else{
                            continue;
                        }
                    }
                    loginAsTV.setText("Log in as " + loggedInAs);
                    loginAsTV.setVisibility(View.VISIBLE);
                    startMessageTV.setVisibility(View.VISIBLE);
                    continueAsBT.setVisibility(View.VISIBLE);
                    loadingUserPB.setVisibility(View.GONE);
                    startAnimation(transparent_anim,loginAsTV);
                    startAnimation(transparent_anim,startMessageTV);
                    startAnimation(transparent_anim,continueAsBT);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{

        }
    }
    public void startAnimation(Animation animation,View v){
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setClickable(false);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.setAnimation(animation);
    }
}
