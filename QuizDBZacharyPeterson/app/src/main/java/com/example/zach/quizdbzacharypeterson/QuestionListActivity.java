package com.example.zach.quizdbzacharypeterson;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class QuestionListActivity extends AppCompatActivity {
    QuizDB db;
    ListView listView;
    static SimpleCursorAdapter adapter;

    public static void notifyValuesChanged(){

    }

    public void reAssignDB(){
        Cursor cursor = db.getQuestionsAsCursor();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[]{QuizDB.TEXT,QuizDB.STATUS},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }

    private String changeStatusToString(String statusString){
        int status = Integer.parseInt(statusString);
        if(status == Question.UNANSWERED){
            return "*****";
        }
        else if(status == Question.RIGHT){
            return "Correct";
        }
        else
        {
            return "Incorrect";
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        db = ShareData.get(this).getQuizDB();




        listView = (ListView) findViewById(R.id.listView);

        Cursor cursor = db.getQuestionsAsCursor();
        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[]{QuizDB.TEXT,QuizDB.STATUS},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getBaseContext(),QuestionActivity.class);
                i.putExtra("position",id);

                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        reAssignDB();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.About:
                Intent i = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(i);


                return true;
            case R.id.Reset:
                //db.insertQuiz(new Question(2,"I'm a question 2",false));
                for(int d = 0; d<5; d++){db.deleteQuestion(d);}
                reAssignDB();

                return true;
            case R.id.AddMore:
                db.insertQuiz(new Question(7, "I'm a quest\nion 2", false));
                db.insertQuiz(new Question(1,"I'm a question",false));
                reAssignDB();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        if (v.getId()==R.id.listView){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(db.getQuestion((int)info.id).text());
            menu.add(Menu.NONE, 1,1,"Remove");
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
       // ArrayList<Question> temp = db.getQuestions();
        db.deleteQuestion(info.id);
        reAssignDB();
        return true;
    }
}
