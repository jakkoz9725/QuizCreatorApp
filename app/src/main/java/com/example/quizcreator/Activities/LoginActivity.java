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

import com.example.quizcreator.Classes.Patterns;
import com.example.quizcreator.Classes.User;
import com.example.quizcreator.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindAnim;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String loggedInAs;
    private Patterns patterns;

    private boolean usernameWarningBool, passwordWarningBool, passwordRepWarningBool, emialWarningBool;

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

    @BindView(R.id.usernameWarning)
    TextView usernameWarning;
    @BindView(R.id.passwordWarning)
    TextView passwordWarning;
    @BindView(R.id.passwordRepWarning)
    TextView passwordRepWarning;
    @BindView(R.id.emailWarning)
    TextView emailWarning;


    @BindView(R.id.registerPB)
    ProgressBar registerPB;
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
        patterns = new Patterns(this);

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

    @OnTextChanged(R.id.newUserEmailET)
    public void onTextChangedEmailET() {
        emialWarningBool = patterns.isDataCorrect(newUserEmailET.getText().toString(), patterns.getEmailAdressPattern());
        if (emialWarningBool) {
            emailWarning.setTextColor(getColor(R.color.green));
        } else {
            emailWarning.setTextColor(getColor(R.color.colorWhite));
        }
    }

    @OnTextChanged(R.id.newUsernameET)
    public void onTextChangedUsernameET() {
        usernameWarningBool = patterns.isDataCorrect(newUsernameET.getText().toString(), patterns.getUserNamePattern());
        if (usernameWarningBool) {
            usernameWarning.setTextColor(getColor(R.color.green));
        } else {
            usernameWarning.setTextColor(getColor(R.color.colorWhite));
        }
    }

    @OnTextChanged(R.id.newUserPasswordET)
    public void onTextChangednewUserPasswordET() {
        passwordWarningBool = patterns.isDataCorrect(newUserPasswordET.getText().toString(), patterns.getPasswordPattern());
        if (passwordWarningBool) {
            passwordWarning.setTextColor(getColor(R.color.green));
        } else {
            passwordWarning.setTextColor(getColor(R.color.colorWhite));
        }
        if (newUserPasswordRepET.getText().toString().equals(newUserPasswordET.getText().toString()) && passwordWarningBool) {
            passwordRepWarningBool = true;
            passwordRepWarning.setTextColor(getColor(R.color.green));
        } else {
            passwordRepWarningBool = false;
            passwordRepWarning.setTextColor(getColor(R.color.colorWhite));
        }
    }

    @OnTextChanged(R.id.newUserPasswordRepET)
    public void onTextChangednewUserPasswordRepET() {
        String newUserPwRep = newUserPasswordRepET.getText().toString();
        if (newUserPwRep.length() != 0 && newUserPwRep.equals(newUserPasswordET.getText().toString()) && passwordWarningBool) {
            passwordRepWarningBool = true;
            passwordRepWarning.setTextColor(getColor(R.color.green));
        } else {
            passwordRepWarningBool = false;
            passwordRepWarning.setTextColor(getColor(R.color.colorWhite));
        }
    }

    @OnClick(R.id.createAccBT)
    public void createNewUser() {
        if (usernameWarningBool && passwordWarningBool && passwordRepWarningBool && emialWarningBool) {
            String newUserEmail = newUserEmailET.getText().toString();
            String newUserPassword = newUserPasswordET.getText().toString();
            String newUsername = newUsernameET.getText().toString();
            registerPB.setVisibility(View.VISIBLE);
            checkIsUsernameAlreadyTaken(newUsername);
        } else {
            Toast.makeText(this, "Check list", Toast.LENGTH_SHORT).show();
        }
    }
    public void createNewUserPart2(boolean usernameTaken) {
        if (!usernameTaken) {
            String newUserEmail = newUserEmailET.getText().toString();
            String newUserPassword = newUserPasswordET.getText().toString();
            String newUsername = newUsernameET.getText().toString();
            mAuth.createUserWithEmailAndPassword(newUserEmail, newUserPassword).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    User user = new User(newUserEmail, newUsername, newUserPassword);
                    databaseReference.child(newUsername).setValue(user);
                    Toast.makeText(LoginActivity.this, "Created Succesfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MenuActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.transparent_anim_appear, R.anim.transparent_anim_disapear);
                } else try {
                    throw task.getException();
                } catch (FirebaseAuthUserCollisionException existEmail) {
                    Toast.makeText(this, "Email already used in this app", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                registerPB.setVisibility(View.GONE);
            });
        } else {
            Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show();
            registerPB.setVisibility(View.GONE);
        }
    }
    @OnClick(R.id.signInBtn)
    public void signIn() {
        String email = userEmailET.getText().toString();
        String password = userPasswordET.getText().toString();
        boolean isEmailCorrect = patterns.isDataCorrect(email, patterns.getEmailAdressPattern());
        if(password.length() == 0){
            Toast.makeText(this, "You need password", Toast.LENGTH_SHORT).show();
        }else {
            if (isEmailCorrect) {
                loginInPB.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                loginInPB.setVisibility(View.GONE);
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(this, MenuActivity.class);
                                intent.putExtra("email", user.getEmail());
                                startActivity(intent);
                                overridePendingTransition(R.anim.transparent_anim_appear, R.anim.transparent_anim_disapear);
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                    Toast.makeText(this, "This email is not registered", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                    Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                    Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            loginInPB.setVisibility(View.GONE);
                        });
            } else {
                loginInPB.setVisibility(View.GONE);
                Toast.makeText(this, "Email format is not correct", Toast.LENGTH_SHORT).show();
            }
        }
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

    public void checkIsUsernameAlreadyTaken(String newNickname){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<String> usersNames = new ArrayList();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    usersNames.add(ds.getValue(User.class).getUserName());
                    }
                if(usersNames.contains(newNickname)){
                    createNewUserPart2(true);
                }else{
                    createNewUserPart2(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
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
