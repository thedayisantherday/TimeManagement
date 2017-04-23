package com.timemanagement.zxg.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.utils.LogUtils;
import com.timemanagement.zxg.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxg on 17/3/15.
 */


public class DatabaseUtil {
    private DatabaseHelper helper;

    public DatabaseUtil(Context context) {
        super();
        helper = new DatabaseHelper(context);
    }

    /**插入数据
     * @param eventModel
     **/
    public boolean insertData(EventModel eventModel){
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "insert into "+helper.TABLE_NAME
                +" (title, date, remind, remindAgain, emergency, importance"
                +", repeat, repeatEnd, comment, isFinished) values ("
                + "'" + eventModel.getTitle()
                + "', '" + TimeUtils.dateToStrShort(eventModel.getDate())
                + "', '" + TimeUtils.dateToStr(eventModel.getRemind())
                + "', '" + TimeUtils.dateToStr(eventModel.getRemindAgain())
                + "', '" + eventModel.getEmergency()
                + "', '" + eventModel.getImportance()
                + "', '" + eventModel.getRepeat()
                + "', '" + TimeUtils.dateToStrShort(eventModel.getRepeatEnd())
                + "', '" + eventModel.getComment()
                + "', '" + eventModel.getIsFinished() + "')";
        try {
            db.execSQL(sql);
            return true;
        } catch (SQLException e){
            Log.e("err", "insert failed");
            return false;
        }finally{
            db.close();
        }
    }

    /**插入数据并返回最新的一条数据
     * @param eventModel
     **/
    public EventModel insertReturnLatest(EventModel eventModel){
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "insert into "+helper.TABLE_NAME
                +" (title, date, remind, remindAgain, emergency, importance"
                +", repeat, repeatEnd, comment, isFinished) values ("
                + "'" + eventModel.getTitle()
                + "', '" + TimeUtils.dateToStrShort(eventModel.getDate())
                + "', '" + TimeUtils.dateToStr(eventModel.getRemind())
                + "', '" + TimeUtils.dateToStr(eventModel.getRemindAgain())
                + "', '" + eventModel.getEmergency()
                + "', '" + eventModel.getImportance()
                + "', '" + eventModel.getRepeat()
                + "', '" + TimeUtils.dateToStrShort(eventModel.getRepeatEnd())
                + "', '" + eventModel.getComment()
                + "', '" + eventModel.getIsFinished() + "')";

        try {
            db.execSQL(sql);

            Cursor cursor = db.query(helper.TABLE_NAME, null, null,null, null, null, "id desc limit 1");
            while(cursor.moveToNext()){
                LogUtils.i("cursor size", "cursor size is " + cursor.getCount()
                        + " id is "+cursor.getInt(cursor.getColumnIndex("id")));
                eventModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
                eventModel.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                eventModel.setDate(TimeUtils.strToDateShort(cursor.getString(cursor.getColumnIndex("date"))));
                eventModel.setRemind(TimeUtils.strToDate(cursor.getString(cursor.getColumnIndex("remind"))));
                eventModel.setRemindAgain(TimeUtils.strToDate(cursor.getString(cursor.getColumnIndex("remindAgain"))));
                eventModel.setEmergency(cursor.getInt(cursor.getColumnIndex("emergency")));
                eventModel.setImportance(cursor.getInt(cursor.getColumnIndex("importance")));
                eventModel.setRepeat(cursor.getInt(cursor.getColumnIndex("repeat")));
                eventModel.setRepeatEnd(TimeUtils.strToDateShort(cursor.getString(cursor.getColumnIndex("repeatEnd"))));
                eventModel.setComment(cursor.getString(cursor.getColumnIndex("comment")));
                eventModel.setIsFinished(cursor.getInt(cursor.getColumnIndex("isFinished"))>0);
            }
        } catch (SQLException e){
            Log.e("err", "insert failed");
        }finally{
            db.close();
            return eventModel;
        }

    }

    /**更新数据
     * @param eventModel , id
     **/

    public boolean updateData(EventModel eventModel){

        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "update "+helper.TABLE_NAME
                +" set title='" + eventModel.getTitle()
                + "', date='" + TimeUtils.dateToStrShort(eventModel.getDate())
                +"', remind='"+ TimeUtils.dateToStr(eventModel.getRemind())
                +"', remindAgain='"+ TimeUtils.dateToStr(eventModel.getRemindAgain())
                +"', emergency='"+ eventModel.getEmergency()
                +"', importance='" + eventModel.getImportance()
                +"', repeat='"+ eventModel.getRepeat()
                +"', repeatEnd='"+ TimeUtils.dateToStrShort(eventModel.getRepeatEnd())
                +"', comment='"+ eventModel.getComment()
                +"', isFinished='"+ eventModel.getIsFinished()
                + "' where id=" + eventModel.getId();
        try {
            db.execSQL(sql);
            return true;
        } catch (SQLException e){
            Log.e("err", "update failed");
            return false;
        }finally{
            db.close();
        }
        /*SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", person.getName());
        values.put("sex", person.getSex());
        int rows = db.update(MyHelper.TABLE_NAME, values, "_id=?", new String[] { id + "" });
        db.close();*/
    }

    /**删除数据
     * @param id
     **/
    public void deleteData(int id){

        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(helper.TABLE_NAME, "id=?", new String[]{id+""});
        db.close();
    }

    /**
     * 查询所有数据
     * @return
     **/
    public List<EventModel> queryAll(){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<EventModel> list = new ArrayList<EventModel>();
        Cursor cursor = db.query(helper.TABLE_NAME, null, null,null, null, null, null);

        while(cursor.moveToNext()){
            EventModel eventModel = new EventModel();
            eventModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
            eventModel.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            eventModel.setDate(TimeUtils.strToDateShort(cursor.getString(cursor.getColumnIndex("date"))));
            eventModel.setRemind(TimeUtils.strToDate(cursor.getString(cursor.getColumnIndex("remind"))));
            eventModel.setRemindAgain(TimeUtils.strToDate(cursor.getString(cursor.getColumnIndex("remindAgain"))));
            eventModel.setEmergency(cursor.getInt(cursor.getColumnIndex("emergency")));
            eventModel.setImportance(cursor.getInt(cursor.getColumnIndex("importance")));
            eventModel.setRepeat(cursor.getInt(cursor.getColumnIndex("repeat")));
            eventModel.setRepeatEnd(TimeUtils.strToDateShort(cursor.getString(cursor.getColumnIndex("repeatEnd"))));
            eventModel.setComment(cursor.getString(cursor.getColumnIndex("comment")));
            eventModel.setIsFinished(cursor.getInt(cursor.getColumnIndex("isFinished"))>0);
            list.add(eventModel);
        }
        db.close();
        return list;
    }

    /**
     * 按日期查询
     * @return
     */
    public List<EventModel> queryByDate(Date date){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<EventModel> list = new ArrayList<EventModel>();
        Cursor cursor = db.query(helper.TABLE_NAME, null, "date=?",new String[]{TimeUtils.dateToStrShort(date)}, null, null, null);

        while(cursor.moveToNext()){
            EventModel eventModel = new EventModel();
            eventModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
            eventModel.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            eventModel.setDate(TimeUtils.strToDateShort(cursor.getString(cursor.getColumnIndex("date"))));
            eventModel.setRemind(TimeUtils.strToDate(cursor.getString(cursor.getColumnIndex("remind"))));
            eventModel.setRemindAgain(TimeUtils.strToDate(cursor.getString(cursor.getColumnIndex("remindAgain"))));
            eventModel.setEmergency(cursor.getInt(cursor.getColumnIndex("emergency")));
            eventModel.setImportance(cursor.getInt(cursor.getColumnIndex("importance")));
            eventModel.setRepeat(cursor.getInt(cursor.getColumnIndex("repeat")));
            eventModel.setRepeatEnd(TimeUtils.strToDateShort(cursor.getString(cursor.getColumnIndex("repeatEnd"))));
            eventModel.setComment(cursor.getString(cursor.getColumnIndex("comment")));
            eventModel.setIsFinished(cursor.getInt(cursor.getColumnIndex("isFinished"))>0);
            list.add(eventModel);
        }
        db.close();
        return list;
    }

    /**
     * 按重复类型查询
     * @return
     */
    public Map<String, List<EventModel>> queryByRepeatType(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Map<String, List<EventModel>> maps = new HashMap<String, List<EventModel>>();

        for (int i = 1; i < 6; i++) {
            List<EventModel> list = new ArrayList<EventModel>();
            Cursor cursor = db.query(helper.TABLE_NAME, null, "repeat=?",new String[]{""+i}, null, null, "remind");
            while(cursor.moveToNext()){
                EventModel eventModel = new EventModel();
                eventModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
                eventModel.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                eventModel.setDate(TimeUtils.strToDateShort(cursor.getString(cursor.getColumnIndex("date"))));
                eventModel.setRemind(TimeUtils.strToDate(cursor.getString(cursor.getColumnIndex("remind"))));
                eventModel.setRemindAgain(TimeUtils.strToDate(cursor.getString(cursor.getColumnIndex("remindAgain"))));
                eventModel.setEmergency(cursor.getInt(cursor.getColumnIndex("emergency")));
                eventModel.setImportance(cursor.getInt(cursor.getColumnIndex("importance")));
                eventModel.setRepeat(cursor.getInt(cursor.getColumnIndex("repeat")));
                eventModel.setRepeatEnd(TimeUtils.strToDateShort(cursor.getString(cursor.getColumnIndex("repeatEnd"))));
                eventModel.setComment(cursor.getString(cursor.getColumnIndex("comment")));
                eventModel.setIsFinished(cursor.getInt(cursor.getColumnIndex("isFinished"))>0);
                list.add(eventModel);
            }
            maps.put(""+i, list);
        }
        db.close();
        return maps;
    }
}
