package com.example.zach.quizdbzacharypeterson;

import android.content.Context;

/**
 * Created by Zach on 12/1/2015.
 */
public class ShareData {
    private static ShareData ourInstance;

    private QuizDB db;
    private Context context;

    public static ShareData getInstance() {
        return ourInstance;
    }

    private ShareData(Context c) {
        db = new QuizDB(c);
    }

    public static ShareData get(Context c){
       if(ourInstance == null){
           ourInstance = new ShareData(c.getApplicationContext());
       }
        return ourInstance;
    }

    public QuizDB getQuizDB(){
        return db;
    }
}
