package com.example.zach.quizdbzacharypeterson;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Zach on 12/1/2015.
 */
public class QuizDB {
    public static final String DB_NAME = "quiz.db";
    public static final int    DB_VERSION = 1;

    public static final String QUIZ_TABLE = "quiz";

    public static final String QUESTION_ID = "_id";
    public static final int    QUESTION_ID_COL = 0;

    public static final String TEXT = "text";
    public static final int    TEXT_COL = 1;

    public static final String CORRECT = "correct";
    public static final int CORRECT_COL = 2;

    public static final String STATUS = "runtime";
    public static final int STATUS_COL = 3;

    public static final String CREATE_QUIZ_TABLE =
            "CREATE TABLE " + QUIZ_TABLE + " (" +
                    QUESTION_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TEXT       + " TEXT    NOT NULL, " + CORRECT + "TEXT NOT NULL" +
                    STATUS + " INTEGER NOT NULL);";

    public static final String DROP_QUIZ_TABLE =
            "DROP TABLE IF EXISTS " + QUIZ_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_QUIZ_TABLE);

            // insert sample quizs
            // db.execSQL("INSERT INTO quiz VALUES (1, 'Harry Potter', 120)");
            // db.execSQL("INSERT INTO quiz VALUES (2, 'Lord of the Rings', 600)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            Log.d("*** Quiz ***", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            db.execSQL(QuizDB.DROP_QUIZ_TABLE);
            onCreate(db);
        }
    }

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public QuizDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }


    public long insertQuiz(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QUESTION_ID, question.getId() );
        cv.put(TEXT, question.getText());
        cv.put(STATUS, question.getStatus());
        String correct;
        if(question.isCorrect())
            correct = "TRUE";
        else
            correct = "FALSE";

        cv.put(CORRECT,correct);

        this.openWriteableDB();
        long rowID = db.insert(QUIZ_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    /*
     * The id field in the Quiz object is ignored -- the system will assign the next
     * valid value.
     */

    public long insertQuizAutoId(Question question) {
        ContentValues cv = new ContentValues();

        cv.put(TEXT, question.getText());
        cv.put(STATUS, question.getStatus());
        String correct;
        if(question.isCorrect())
            correct = "TRUE";
        else
            correct = "FALSE";
        cv.put(CORRECT,correct);


        this.openWriteableDB();
        long rowID = db.insert(QUIZ_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public int updateQuiz(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QUESTION_ID, question.getId());
        cv.put(TEXT, question.getText());
        cv.put(STATUS, question.getStatus());
        String correct;
        if(question.isCorrect())
            correct = "TRUE";
        else
            correct = "FALSE";
        cv.put(CORRECT,correct);

        String where = QUESTION_ID + "= ?";
        String[] whereArgs = { String.valueOf(question.getId()) };

        this.openWriteableDB();
        int rowCount = db.update(QUIZ_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteQuestion(long id) {
        String where = QUESTION_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };

        this.openWriteableDB();
        int rowCount = db.delete(QUIZ_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    /*
     * Retrieves all of the quizs in the database and returns as an ArrayList of Quiz objects.
     */

    public ArrayList<Question> getQuestions() {
        this.openReadableDB();
        Cursor cursor = db.query(QUIZ_TABLE, null,
                null, null,  // using null for where and whereArgs to get all of the quizs
                null, null, null);
        ArrayList<Question> questions = new ArrayList<Question>();
        while (cursor.moveToNext()) {
            questions.add(getQuizFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return questions;
    }

    public Cursor getQuestionsAsCursor() {
        this.openReadableDB();
        Cursor cursor = db.query(QUIZ_TABLE, null,
                null, null,  // using null for where and whereArgs to get all of the quizs
                null, null, null);

        return cursor;
    }

    public Question getQuiz(int id) {
        String where = QUESTION_ID + "= ?";
        String[] whereArgs = { Integer.toString(id) };

        this.openReadableDB();
        Cursor cursor = db.query(QUIZ_TABLE,
                null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        Question question = getQuizFromCursor(cursor);
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return question;
    }

    private static Question getQuizFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {



                Question question = new Question (
                        cursor.getInt(QUESTION_ID_COL),
                        cursor.getString(TEXT_COL),
                        cursor.getString(CORRECT_COL).equals("TRUE"));
                question.setStatus(cursor.getInt(STATUS_COL));

                return question;
            }
            catch(Exception e) {
                return null;
            }
        }
    }
}
