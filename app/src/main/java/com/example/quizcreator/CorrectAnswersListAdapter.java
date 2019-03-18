package com.example.quizcreator;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.quizcreator.Classes.Question;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CorrectAnswersListAdapter extends ArrayAdapter<Question> {
    private Activity context;
    private List<Question> questionList;

    public CorrectAnswersListAdapter(Activity context, List<Question> questionList) {
        super(context, R.layout.answercheck_rows,questionList);
        this.context = context;
        this.questionList = questionList;
    }

    @BindView(R.id.questionT)
    TextView questionT;

    @BindView(R.id.userAnswer)
    TextView userAnswer;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.answercheck_rows, null, true);
        ButterKnife.bind(this, listViewItem);

        Question question = questionList.get(position);

        questionT.setText(question.getQuestion());
        userAnswer.setText(question.getUserAnswer());

        return listViewItem;
    }
}
