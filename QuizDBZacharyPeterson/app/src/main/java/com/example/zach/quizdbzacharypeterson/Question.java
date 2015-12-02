package com.example.zach.quizdbzacharypeterson;

/**
 * Created by Zach on 12/1/2015.
 */
public class Question {
    public static final int UNANSWERED = 0;
    public static final int RIGHT = 1;
    public static final int WRONG = 2;

    public long id;  // NOTE: this needs to be a long id
    private String text;
    private boolean correct;
    private int status;





    public Question (long id, String text, boolean correct) {
        super();
        this.id = id;
        this.text = text;
        this.correct = correct;
        this.status = UNANSWERED;
    }

    public String text() {
        return String.format("Question: %s", id);
    }

    public String details() {
        return text;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return correct;
    }

    public int getStatus() {
        return status;
    }

    public void makeGuess(boolean guess) {
        if (guess == correct)
            this.status = RIGHT;
        else
            this.status = WRONG;
    }

    public void reset() {
        this.status = UNANSWERED;
    }

    @Override
    public String toString() {
        return String.format("Question: %d (status: %d)", (int) id, status);
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
