package com.timemanagement.zxg.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zxg on 17/2/6.
 */

public class EventModel implements Serializable{

    private int id;
    private String title;
    private Date date;
    private Date remind;
    private Date remindAgain;
    private int emergency = -1;
    private int importance = -1;
    private int repeat;
    private Date repeatEnd;
    private String comment;
    private boolean isFinished;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getRemind() {
        return remind;
    }

    public void setRemind(Date remind) {
        this.remind = remind;
    }

    public Date getRemindAgain() {
        return remindAgain;
    }

    public void setRemindAgain(Date remindAgain) {
        this.remindAgain = remindAgain;
    }

    public int getEmergency() {
        return emergency;
    }

    public void setEmergency(int emergency) {
        this.emergency = emergency;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public Date getRepeatEnd() {
        return repeatEnd;
    }

    public void setRepeatEnd(Date repeatEnd) {
        this.repeatEnd = repeatEnd;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }
}
