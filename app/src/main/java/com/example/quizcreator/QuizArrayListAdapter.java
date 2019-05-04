package com.example.quizcreator;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.quizcreator.Classes.Quiz;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class QuizArrayListAdapter extends ArrayAdapter<Quiz> {

    private Activity context;
    private List<Quiz> quizList;

    public QuizArrayListAdapter(Activity context, List<Quiz> quizList){
        super(context,R.layout.quizlist_rows,quizList);
        this.context = context;
        this.quizList = quizList;
    }
    @BindView(R.id.quizNameT)
    TextView quizNameT;

    @BindView(R.id.creatorNameT)
    TextView creatorNameT;
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.quizlist_rows,null,true);
        ButterKnife.bind(this,listViewItem);

        Quiz quiz = quizList.get(position);
        quizNameT.setText(quiz.getQuizName());
        creatorNameT.setText(creatorNameT.getText() + " " + quiz.getCreatorUsername());
        return listViewItem;
    }
}
