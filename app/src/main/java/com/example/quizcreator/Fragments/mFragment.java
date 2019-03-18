package com.example.quizcreator.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizcreator.Activities.QuizCreationActivity;
import com.example.quizcreator.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class mFragment extends Fragment {

    final int counter = QuizCreationActivity.fragmentCounter;
    final String fragmentActualNumber = "Question number : " + (counter + 1) + " ! ";
    ConstraintSet constraintSet = new ConstraintSet();

    int currentPosition = 0;
    int deleteButtonPlacement = 0;
    int answerBtnStage = 1;


    boolean answer1Accepted, answer2Accepted, answer3Accepted, answer4Accepted, questionAccepted;
    boolean answer3_isActive, answer4_isActive;
    boolean answerAdded;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        ButterKnife.bind(this, view);
        fragmentNumberT.setText(fragmentActualNumber);
        constraintSet.clone(constraintLayout);
        checkIsCompleted();
        return view;
    }

    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;

    @BindView(R.id.questionET)
    EditText questionET;
    @BindView(R.id.answer1ET)
    EditText answer1ET;
    @BindView(R.id.answer2ET)
    EditText answer2ET;
    @BindView(R.id.answer3ET)
    EditText answer3ET;
    @BindView(R.id.answer4ET)
    EditText answer4ET;
    @BindView(R.id.deleteAnswerBtn)
    Button deleteAnswerBtn;
    @BindView(R.id.addAnswerBtn)
    Button addAnswerBtn;
    @BindView(R.id.deleteTextBtn)
    Button deleteTextBtn;
    @BindView(R.id.answerAcceptBtn)
    Button answerAcceptBtn;
    @BindView(R.id.editAnswer1Btn)
    Button editAnswer1Btn;
    @BindView(R.id.editAnswer2Btn)
    Button editAnswer2Btn;
    @BindView(R.id.editAnswer3Btn)
    Button editAnswer3Btn;
    @BindView(R.id.editAnswer4Btn)
    Button editAnswer4Btn;
    @BindView(R.id.radioButton1)
    RadioButton radioButton1;
    @BindView(R.id.radioButton2)
    RadioButton radioButton2;
    @BindView(R.id.radioButton3)
    RadioButton radioButton3;
    @BindView(R.id.radioButton4)
    RadioButton radioButton4;
    @BindView(R.id.pickCorrectAnswerBtn)
    Button pickCorrectAnswerBtn;
    @BindView(R.id.fragmentNumberT)
    TextView fragmentNumberT;

    @OnClick(R.id.pickCorrectAnswerBtn)
    public void pickCorrectAnswer() {
        if (answerBtnStage == 1) {
            if ((answer1Accepted && answer2Accepted && questionAccepted) && (!answer3_isActive && !answer4_isActive)) {

                constraintSet.setVisibility(R.id.radioButton1, View.VISIBLE);
                constraintSet.setVisibility(R.id.radioButton2, View.VISIBLE);

            } else if ((answer1Accepted && answer2Accepted && questionAccepted && answer3Accepted) && (!answer4_isActive)) {
                constraintSet.setVisibility(R.id.radioButton1, View.VISIBLE);
                constraintSet.setVisibility(R.id.radioButton2, View.VISIBLE);
                constraintSet.setVisibility(R.id.radioButton3, View.VISIBLE);

            } else if (answer1Accepted && answer2Accepted && questionAccepted && answer3Accepted && answer4Accepted) {
                constraintSet.setVisibility(R.id.radioButton1, View.VISIBLE);
                constraintSet.setVisibility(R.id.radioButton2, View.VISIBLE);
                constraintSet.setVisibility(R.id.radioButton3, View.VISIBLE);
                constraintSet.setVisibility(R.id.radioButton4, View.VISIBLE);
            }
            constraintSet.setVisibility(R.id.editQuestionBtn, View.INVISIBLE);
            constraintSet.setVisibility(R.id.editAnswer1Btn, View.INVISIBLE);
            constraintSet.setVisibility(R.id.editAnswer2Btn, View.INVISIBLE);
            constraintSet.setVisibility(R.id.editAnswer3Btn, View.INVISIBLE);
            constraintSet.setVisibility(R.id.editAnswer4Btn, View.INVISIBLE);
            constraintSet.setVisibility(R.id.deleteAnswerBtn, View.INVISIBLE);


            constraintSet.applyTo(constraintLayout);
            answerBtnStage = 2;
            return;
        }
        if (answerBtnStage == 2) {
            pickCorrectAnswerBtn.setText("Pick");
            if (radioButton1.isChecked()) {
                Toast.makeText(getActivity(), "Answer 1 ", Toast.LENGTH_SHORT).show();
                isCompleted(true);
            } else if (radioButton2.isChecked()) {
                Toast.makeText(getActivity(), "Answer 2 ", Toast.LENGTH_SHORT).show();
                isCompleted(true);
            } else if (radioButton3.isChecked()) {
                Toast.makeText(getActivity(), "Answer 3 ", Toast.LENGTH_SHORT).show();
                isCompleted(true);
            } else if (radioButton4.isChecked()) {
                Toast.makeText(getActivity(), "Answer 4 ", Toast.LENGTH_SHORT).show();
                isCompleted(true);
            } else {
                Toast.makeText(getActivity(), "Musisz wybrać odpowiedź", Toast.LENGTH_SHORT).show();
                return;
            }
            correctAnswerVisibilty();
            constraintSet.applyTo(constraintLayout);
            answerBtnStage = 3;
            pickCorrectAnswerBtn.setText("Edytuj");
            return;
        }
        if (answerBtnStage == 3) {
            if (answer4_isActive) {
                constraintSet.setVisibility(R.id.editQuestionBtn, View.VISIBLE);
                constraintSet.setVisibility(R.id.editAnswer1Btn, View.VISIBLE);
                constraintSet.setVisibility(R.id.editAnswer2Btn, View.VISIBLE);
                constraintSet.setVisibility(R.id.editAnswer3Btn, View.VISIBLE);
                constraintSet.setVisibility(R.id.editAnswer4Btn, View.VISIBLE);
                constraintSet.setVisibility(R.id.deleteAnswerBtn, View.VISIBLE);
            } else if (answer3_isActive) {
                constraintSet.setVisibility(R.id.editQuestionBtn, View.VISIBLE);
                constraintSet.setVisibility(R.id.editAnswer1Btn, View.VISIBLE);
                constraintSet.setVisibility(R.id.editAnswer2Btn, View.VISIBLE);
                constraintSet.setVisibility(R.id.editAnswer3Btn, View.VISIBLE);
                constraintSet.setVisibility(R.id.deleteAnswerBtn, View.VISIBLE);
                constraintSet.setVisibility(R.id.addAnswerBtn, View.VISIBLE);
            } else {
                constraintSet.setVisibility(R.id.editQuestionBtn, View.VISIBLE);
                constraintSet.setVisibility(R.id.editAnswer1Btn, View.VISIBLE);
                constraintSet.setVisibility(R.id.editAnswer2Btn, View.VISIBLE);
                constraintSet.setVisibility(R.id.addAnswerBtn, View.VISIBLE);
            }

            answerBtnStage = 1;
            pickCorrectAnswerBtn.setText("Choose");
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
            radioButton3.setChecked(false);
            radioButton4.setChecked(false);
            correctAnswerVisibilty();
            pickCorrectAnswerBtn.setVisibility(View.INVISIBLE);
            constraintSet.applyTo(constraintLayout);
        }
    }

    @OnClick(R.id.deleteAnswerBtn)
    public void deleteAnswer() {
        switch (deleteButtonPlacement) {
            case 1:
                answer3ET.setText("");
                constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.TOP, R.id.answer3ET, ConstraintSet.TOP, 0);
                constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.END, R.id.answer3ET, ConstraintSet.END, 0);
                constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.START, R.id.answer3ET, ConstraintSet.START, 0);
                constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.BOTTOM, R.id.answer3ET, ConstraintSet.BOTTOM, 0);
                answer3ET.clearFocus();

                constraintSet.setVisibility(R.id.answer3ET, View.INVISIBLE);
                constraintSet.setVisibility(R.id.addAnswerBtn, View.VISIBLE);
                constraintSet.setVisibility(R.id.deleteAnswerBtn, View.INVISIBLE);
                constraintSet.setVisibility(R.id.editAnswer3Btn, View.GONE);
                cleanHangingButtons();
                turnOnAnswerEdit(answer3ET);
                answer3_isActive = false;
                answer3Accepted = false;
                answerAdded = false;
                answerBtnStage = 1;
                clearAndHideRadioButtons();

                break;
            case 2:
                answer4ET.setText("");
                constraintSet.connect(R.id.deleteAnswerBtn, ConstraintSet.END, R.id.answer3ET, ConstraintSet.START, 0);
                constraintSet.connect(R.id.deleteAnswerBtn, ConstraintSet.BOTTOM, R.id.answer3ET, ConstraintSet.BOTTOM, 0);
                constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.TOP, R.id.answer4ET, ConstraintSet.TOP, 0);
                constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.END, R.id.answer4ET, ConstraintSet.END, 0);
                constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.START, R.id.answer4ET, ConstraintSet.START, 0);
                constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.BOTTOM, R.id.answer4ET, ConstraintSet.BOTTOM, 0);
                constraintSet.setVisibility(R.id.addAnswerBtn, View.VISIBLE);
                constraintSet.setVisibility(R.id.answer4ET, View.INVISIBLE);
                answerAdded = true;
                answer4ET.clearFocus();
                constraintSet.setVisibility(R.id.editAnswer4Btn, View.GONE);
                cleanHangingButtons();
                deleteButtonPlacement = 1;
                answer4_isActive = false;
                answer4Accepted = false;
                turnOnAnswerEdit(answer4ET);
                answerBtnStage = 1;
                clearAndHideRadioButtons();
                break;
        }
        checkIsCompleted();
    }

    @OnClick(R.id.addAnswerBtn)
    public void addAnswer() {
        if (!answerAdded) {
            constraintSet.setVisibility(R.id.answer3ET, View.VISIBLE);
            constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.TOP, R.id.answer4ET, ConstraintSet.TOP, 0);
            constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.END, R.id.answer4ET, ConstraintSet.END, 0);
            constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.START, R.id.answer4ET, ConstraintSet.START, 0);
            constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.BOTTOM, R.id.answer4ET, ConstraintSet.BOTTOM, 0);
            constraintSet.connect(R.id.deleteAnswerBtn, ConstraintSet.END, R.id.answer3ET, ConstraintSet.START, 0);
            constraintSet.connect(R.id.deleteAnswerBtn, ConstraintSet.BOTTOM, R.id.answer3ET, ConstraintSet.BOTTOM, 0);
            answer3_isActive = true;
            constraintSet.setVisibility(R.id.deleteAnswerBtn, View.VISIBLE);
            constraintSet.applyTo(constraintLayout);
            answerAdded = true;

            answerBtnStage = 1;
            deleteButtonPlacement = 1;
        } else{
            constraintSet.setVisibility(R.id.answer4ET, View.VISIBLE);
            constraintSet.setVisibility(R.id.addAnswerBtn, View.INVISIBLE);
            constraintSet.connect(R.id.deleteAnswerBtn, ConstraintSet.END, R.id.answer4ET, ConstraintSet.START, 0);
            constraintSet.connect(R.id.deleteAnswerBtn, ConstraintSet.BOTTOM, R.id.answer4ET, ConstraintSet.BOTTOM, 0);
            answer4_isActive = true;
            constraintSet.setVisibility(R.id.deleteAnswerBtn, View.VISIBLE);
            constraintSet.applyTo(constraintLayout);

            answerBtnStage = 1;
            deleteButtonPlacement = 2;
        }
        checkIsCompleted();
    }

    @OnClick(R.id.answerAcceptBtn)
    public void acceptButton() {
        switch (currentPosition) {
            case 1:
                String odp = answer1ET.getText().toString();
                if (odp.length() == 0) {
                    Toast.makeText(getActivity(), "Pusta odpowiedz!", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    turnOffAnswerEdit(answer1ET);
                    constraintSet.setVisibility(R.id.editAnswer1Btn, View.VISIBLE);
                    answer1Accepted = true;
                    constraintSet.applyTo(constraintLayout);
                    break;
                }

            case 2:
                odp = answer2ET.getText().toString();
                if (odp.length() == 0) {
                    Toast.makeText(getActivity(), "Pusta odpowiedz!", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    turnOffAnswerEdit(answer2ET);
                    constraintSet.setVisibility(R.id.editAnswer2Btn, View.VISIBLE);
                    constraintSet.applyTo(constraintLayout);
                    answer2Accepted = true;
                    break;
                }
            case 3:
                odp = answer3ET.getText().toString();
                if (odp.length() == 0) {
                    Toast.makeText(getActivity(), "Pusta odpowiedz!", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    turnOffAnswerEdit(answer3ET);
                    answer3Accepted = true;
                    constraintSet.setVisibility(R.id.editAnswer3Btn, View.VISIBLE);
                    constraintSet.applyTo(constraintLayout);
                    break;
                }
            case 4:
                odp = answer4ET.getText().toString();
                if (odp.length() == 0) {
                    Toast.makeText(getActivity(), "Pusta odpowiedz!", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    turnOffAnswerEdit(answer4ET);
                    answer4Accepted = true;
                    constraintSet.setVisibility(R.id.editAnswer4Btn, View.VISIBLE);
                    constraintSet.applyTo(constraintLayout);
                    break;
                }
            case 5:
                odp = questionET.getText().toString();
                if (odp.length() == 0) {
                    Toast.makeText(getActivity(), "Puste pytanie!", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    turnOffAnswerEdit(questionET);
                    questionAccepted = true;
                    constraintSet.setVisibility(R.id.editQuestionBtn, View.VISIBLE);
                    constraintSet.setVisibility(R.id.answer1ET, View.VISIBLE);
                    constraintSet.setVisibility(R.id.answer2ET, View.VISIBLE);
                    constraintSet.applyTo(constraintLayout);
                    break;
                }
        }
        checkIsCompleted();
    }

    @OnClick(R.id.editAnswer1Btn)
    public void editAnswer1() {
        answer1Accepted = false;
        turnOnAnswerEdit(answer1ET);
        constraintSet.setVisibility(R.id.editAnswer1Btn, View.GONE);
        cleanHangingButtons();
    }

    @OnClick(R.id.editAnswer2Btn)
    public void editAnswer2() {
        answer2Accepted = false;
        turnOnAnswerEdit(answer2ET);
        constraintSet.setVisibility(R.id.editAnswer2Btn, View.GONE);
        cleanHangingButtons();
    }

    @OnClick(R.id.editAnswer3Btn)
    public void editAnswer3() {
        answer3Accepted = false;
        turnOnAnswerEdit(answer3ET);
        constraintSet.setVisibility(R.id.editAnswer3Btn, View.GONE);
        cleanHangingButtons();
    }

    @OnClick(R.id.editAnswer4Btn)
    public void editAnswer4() {
        answer4Accepted = false;
        turnOnAnswerEdit(answer4ET);
        constraintSet.setVisibility(R.id.editAnswer4Btn, View.GONE);
        cleanHangingButtons();
    }

    @OnFocusChange(R.id.answer1ET)
    public void onAnswer1Focus() {
        onFocusChange(answer1ET);
    }

    @OnClick(R.id.radioButton1)
    public void radioButton1Checked() {
        whatIsChecked(radioButton1);
    }

    @OnClick(R.id.radioButton2)
    public void radioButton2Checked() {
        whatIsChecked(radioButton2);
    }

    @OnClick(R.id.radioButton3)
    public void radioButton3Checked() {
        whatIsChecked(radioButton3);
    }

    @OnClick(R.id.radioButton4)
    public void radioButton4Checked() {
        whatIsChecked(radioButton4);
    }

    @OnFocusChange(R.id.answer2ET)
    public void onAnswer2Focus() {
        onFocusChange(answer2ET);
    }

    @OnFocusChange(R.id.answer3ET)
    public void onAnswer3Focus() {
        onFocusChange(answer3ET);
    }

    @OnFocusChange(R.id.answer4ET)
    public void onAnswer4Focus() {
        onFocusChange(answer4ET);
    }

    @OnFocusChange(R.id.questionET)
    public void onQuestionFocus() {
        onFocusChange(questionET);
    }


    public void correctAnswerVisibilty() {
        constraintSet.setVisibility(R.id.radioButton1, View.VISIBLE);
        constraintSet.setVisibility(R.id.radioButton2, View.VISIBLE);
        constraintSet.setVisibility(R.id.radioButton3, View.VISIBLE);
        constraintSet.setVisibility(R.id.radioButton4, View.VISIBLE);
        if (!radioButton1.isChecked()) {
            constraintSet.setVisibility(R.id.radioButton1, View.INVISIBLE);
        }
        if (!radioButton2.isChecked()) {
            constraintSet.setVisibility(R.id.radioButton2, View.INVISIBLE);
        }
        if (!radioButton3.isChecked()) {
            constraintSet.setVisibility(R.id.radioButton3, View.INVISIBLE);
        }
        if (!radioButton4.isChecked()) {
            constraintSet.setVisibility(R.id.radioButton4, View.INVISIBLE);
        }

    }

    public void constrainSetUpgrade(int viewRid) {
        constraintSet.setVisibility(R.id.deleteTextBtn, View.VISIBLE);
        constraintSet.setVisibility(R.id.answerAcceptBtn, View.VISIBLE);
        constraintSet.connect(R.id.deleteTextBtn, ConstraintSet.TOP, viewRid, ConstraintSet.BOTTOM, 0);
        constraintSet.connect(R.id.deleteTextBtn, ConstraintSet.END, viewRid, ConstraintSet.END, 0);
        constraintSet.connect(R.id.answerAcceptBtn, ConstraintSet.TOP, viewRid, ConstraintSet.BOTTOM, 0);
        constraintSet.connect(R.id.answerAcceptBtn, ConstraintSet.START, viewRid, ConstraintSet.START, 0);
        constraintSet.applyTo(constraintLayout);
    }

    public void onFocusChange(View v) {
        int i = v.getId();
        if (i == R.id.answer1ET) {
            currentPosition = 1;
            constrainSetUpgrade(i);
        } else if (i == R.id.answer2ET) {
            currentPosition = 2;
            constrainSetUpgrade(i);
        } else if (i == R.id.answer3ET) {
            currentPosition = 3;
            constrainSetUpgrade(i);
        } else if (i == R.id.answer4ET) {
            currentPosition = 4;
            constrainSetUpgrade(i);
        } else if (i == R.id.questionET) {
            currentPosition = 5;
            constrainSetUpgrade(i);
        }
    }

    public void turnOnAnswerEdit(EditText et) {
        et.setFocusableInTouchMode(true);
        et.setEnabled(true);
        et.setCursorVisible(true);
        checkIsCompleted();
    }

    public void cleanHangingButtons() {
        constraintSet.setVisibility(R.id.answerAcceptBtn, View.GONE);
        constraintSet.setVisibility(R.id.deleteTextBtn, View.GONE);
        constraintSet.applyTo(constraintLayout);
    }

    public void turnOffAnswerEdit(EditText et) {
        et.setFocusable(false);
        et.setEnabled(false);
        et.setCursorVisible(false);
        et.clearFocus();
        constraintSet.setVisibility(R.id.answerAcceptBtn, View.INVISIBLE);
        constraintSet.setVisibility(R.id.deleteTextBtn, View.INVISIBLE);
    }

    public void checkIsCompleted() {
        if ((answer1Accepted && answer2Accepted && questionAccepted) && (!answer3_isActive && !answer4_isActive)) {
            constraintSet.setVisibility(R.id.pickCorrectAnswerBtn, View.VISIBLE);
            constraintSet.setVisibility(R.id.addAnswerBtn, View.VISIBLE);
            constraintSet.applyTo(constraintLayout);
        } else if ((answer1Accepted && answer2Accepted && questionAccepted && answer3Accepted) && (!answer4_isActive)) {
            constraintSet.setVisibility(R.id.pickCorrectAnswerBtn, View.VISIBLE);
            constraintSet.setVisibility(R.id.addAnswerBtn, View.VISIBLE);
            constraintSet.applyTo(constraintLayout);
        } else if (answer1Accepted && answer2Accepted && questionAccepted && answer3Accepted && answer4Accepted) {
            constraintSet.setVisibility(R.id.pickCorrectAnswerBtn, View.VISIBLE);
            constraintSet.applyTo(constraintLayout);
        } else {
            isCompleted(false);
            constraintSet.setVisibility(R.id.pickCorrectAnswerBtn, View.INVISIBLE);
            clearAndHideRadioButtons();
        }
    }

    public void clearAndHideRadioButtons() {
        constraintSet.setVisibility(R.id.radioButton1, View.INVISIBLE);
        constraintSet.setVisibility(R.id.radioButton2, View.INVISIBLE);
        constraintSet.setVisibility(R.id.radioButton3, View.INVISIBLE);
        constraintSet.setVisibility(R.id.radioButton4, View.INVISIBLE);
        radioButton1.setChecked(false);
        radioButton2.setChecked(false);
        radioButton3.setChecked(false);
        radioButton4.setChecked(false);
        constraintSet.applyTo(constraintLayout);
    }

    public void isCompleted(boolean mIsCompleted) {
        ((QuizCreationActivity) getActivity()).isComplted(mIsCompleted, counter);
        Toast.makeText(getContext(), "ID is : " + counter, Toast.LENGTH_SHORT).show();
    }

    public void whatIsChecked(RadioButton rb) {
        radioButton1.setChecked(false);
        radioButton2.setChecked(false);
        radioButton3.setChecked(false);
        radioButton4.setChecked(false);

        rb.setChecked(true);
    }

    public void keepChoices() {

        if (answer2ET.getText().length() > 0) {
            constraintSet.setVisibility(R.id.questionET, View.VISIBLE);
            constraintSet.setVisibility(R.id.answer1ET, View.VISIBLE);
            constraintSet.setVisibility(R.id.answer2ET, View.VISIBLE);
            constraintSet.setVisibility(R.id.pickCorrectAnswerBtn, View.VISIBLE);
            constraintSet.setVisibility(R.id.addAnswerBtn, View.INVISIBLE);
            pickCorrectAnswerBtn.setText("Edit");
            turnOffAnswerEdit(questionET);
            turnOffAnswerEdit(answer1ET);
            turnOffAnswerEdit(answer2ET);
        }
        if (answer3ET.getText().length() > 0) {

            constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.TOP, R.id.answer4ET, ConstraintSet.TOP, 0);
            constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.END, R.id.answer4ET, ConstraintSet.END, 0);
            constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.START, R.id.answer4ET, ConstraintSet.START, 0);
            constraintSet.connect(R.id.addAnswerBtn, ConstraintSet.BOTTOM, R.id.answer4ET, ConstraintSet.BOTTOM, 0);
            constraintSet.setVisibility(R.id.addAnswerBtn, View.INVISIBLE);
            constraintSet.setVisibility(R.id.answer3ET, View.VISIBLE);
            turnOffAnswerEdit(answer3ET);
        }
        if (answer4ET.getText().length() > 0) {
            constraintSet.setVisibility(R.id.answer4ET, View.VISIBLE);
            constraintSet.setVisibility(R.id.addAnswerBtn, View.INVISIBLE);
            constraintSet.connect(R.id.deleteAnswerBtn, ConstraintSet.END, R.id.answer4ET, ConstraintSet.START, 0);
            constraintSet.connect(R.id.deleteAnswerBtn, ConstraintSet.BOTTOM, R.id.answer4ET, ConstraintSet.BOTTOM, 0);
            turnOffAnswerEdit(answer4ET);
        }
        correctAnswerVisibilty();
        constraintSet.applyTo(constraintLayout);
    }

    public String getAnswer1ET() {
        return answer1ET.getText().toString();
    }

    public String getAnswer2ET() {
        return answer2ET.getText().toString();
    }

    public String getAnswer3ET() {
        return answer3ET.getText().toString();
    }

    public String getAnswer4ET() {
        return answer4ET.getText().toString();
    }

    public String getQuestionET() {
        return questionET.getText().toString();
    }

    public int getCorrectAnswer() { //Getting correct answer from RadioButtons
        int correctAnswer = 0;
        if (radioButton1.isChecked()) {
            correctAnswer = 1;
        } else if (radioButton2.isChecked()) {
            correctAnswer = 2;
        } else if (radioButton3.isChecked()) {
            correctAnswer = 3;
        } else if (radioButton4.isChecked()) {
            correctAnswer = 4;
        }
        return correctAnswer;
    }
}
