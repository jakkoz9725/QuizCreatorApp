package com.example.quizcreator.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quizcreator.Classes.LoginClass;
import com.example.quizcreator.Classes.Patterns;
import com.example.quizcreator.Classes.RegisterClass;
import com.example.quizcreator.Classes.User;
import com.example.quizcreator.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindAnim;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends Activity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String loggedInAs;
    private LoginClass loginClass;
    private RegisterClass registerClass;

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
    @BindView(R.id.usernameRequirement)
    TextView usernameRequirement;
    @BindView(R.id.passwordRequirement)
    TextView passwordRequirement;
    @BindView(R.id.passwordRepRequirement)
    TextView passwordRepRequirement;
    @BindView(R.id.emailRequirement)
    TextView emailRequirement;
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
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Accounts");
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        imageViewGhostEmoji.setImageDrawable(ghostEmoji);
        scarryGhostAnim(ghost_emoji_anim, imageViewGhostEmoji);
        overridePendingTransition(R.anim.transparent_anim_appear, R.anim.transparent_anim_disapear);
        Patterns patterns = new Patterns(this);


        loginClass = new LoginClass.Builder()
                .loginProgressBar(loginInPB)
                .activityContext(this)
                .firebaseAuth(mAuth)
                .patterns(patterns)
                .build();
        registerClass = new RegisterClass.Builder()
                .activityContext(this)
                .textViewsRequirments(passwordRequirement,passwordRepRequirement,emailRequirement,usernameRequirement)
                .firebaseAuth(mAuth)
                .databaseReference(databaseReference)
                .patterns(patterns)
                .colors(getColor(R.color.colorWhite),getColor(R.color.green))
                .progressBar(registerPB)
                .build();

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
    public void onEmailETChange() {
        registerClass.emailIsCorrect(newUserEmailET.getText().toString());
    }

    @OnTextChanged(R.id.newUsernameET)
    public void onTextUsernameETChange() {
        registerClass.usernameIsCorrect(newUsernameET.getText().toString());
    }

    @OnTextChanged(R.id.newUserPasswordET)
    public void onTextPasswordETChange() {
        registerClass.passwordIsCorrect(newUserPasswordET.getText().toString(), newUserPasswordRepET.getText().toString());
    }

    @OnTextChanged(R.id.newUserPasswordRepET)
    public void onTextPasswordRepETChange() {
        registerClass.passwordRepIsCorrect(newUserPasswordRepET.getText().toString(),newUserPasswordET.getText().toString());
    }

    @OnClick(R.id.createAccBT)
    public void tryToCreate(){
        Map<String, String> map = new HashMap<>();
        map.put("userEmail",newUserEmailET.getText().toString());
        map.put("userName",newUsernameET.getText().toString());
        map.put("userPassword",newUserPasswordET.getText().toString());
        registerClass.checkIsUsernameAlreadyTaken(map);
    }
    @OnClick(R.id.signInBtn)
    public void LoginWithEmailAndPassword() {
        loginClass.loginWithEmailAndPassword(userEmailET.getText().toString(),userPasswordET.getText().toString());
    }

    @OnClick(R.id.continueAsBT)
    public void onContinueAsBTClick() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("email", Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
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
                        if (Objects.requireNonNull(ds.getValue(User.class)).getEmail().equals(currentUser.getEmail())) {
                            loggedInAs = Objects.requireNonNull(ds.getValue(User.class)).getUserName();
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
