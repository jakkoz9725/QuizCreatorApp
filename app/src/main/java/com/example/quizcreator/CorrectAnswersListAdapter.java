package com.example.quizcreator;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.quizcreator.Activities.QuizReadingActivity;
import com.example.quizcreator.Classes.Question;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CorrectAnswersListAdapter extends ArrayAdapter<Question> {
    private Activity context;
    private List<Question> questionList;

    public CorrectAnswersListAdapter(QuizReadingActivity context, List<Question> questionList) {
        super(context, R.layout.answercheck_rows,questionList);
        this.context = context;
        this.questionList = questionList;

    }
    @BindView(R.id.answerConstraintLayout)
    ConstraintLayout answerConstraintLayout;

    @BindView(R.id.questionTextView)
    TextView questionTextView;

    @BindView(R.id.scoreIcon)
    TextView scoreIcon;

    @BindDrawable(R.drawable.points_plus_one)
    Drawable pointsPlusOne;

    @BindDrawable(R.drawable.points_plus_zero)
    Drawable pointsPlusZero;

    @BindDrawable(R.drawable.correct_answer_ic)
    Drawable correctAnswerIcon;

    @BindDrawable(R.drawable.wrong_answer_ic)
    Drawable wrongAnswerIcon;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.answercheck_rows, null, true);
        ButterKnife.bind(this, listViewItem);
        Question question = questionList.get(position);
        setColor(question);
        return listViewItem;
    }
    public void setColor(Question question){
        String correctAnswerString = "";
        if(question.getCorrectAnswer() == 1){
            correctAnswerString = question.getAnswer1();
        }else if(question.getCorrectAnswer() == 2){
            correctAnswerString = question.getAnswer2();
        }else if(question.getCorrectAnswer() == 3){
            correctAnswerString = question.getAnswer3();
        }else if(question.getCorrectAnswer() == 4){
            correctAnswerString = question.getAnswer4();
        }
        if(question.getUserAnswer().equals(correctAnswerString)){
            questionTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,correctAnswerIcon,null);
            scoreIcon.setCompoundDrawablesWithIntrinsicBounds(pointsPlusOne,null,null,null);
            //constraintLayout.setBackgroundColor(Color.parseColor("#B22F970B"));
        }else{
            questionTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,wrongAnswerIcon,null);
            scoreIcon.setCompoundDrawablesWithIntrinsicBounds(pointsPlusZero,null,null,null);
            //constraintLayout.setBackgroundColor(Color.parseColor("#B10D0D"));
        }
        questionTextView.setText(question.getQuestion());
    }
}
