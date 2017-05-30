package com.timemanagement.zxg.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.utils.DateModelUtil;
import com.timemanagement.zxg.utils.LogUtils;
import com.timemanagement.zxg.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.timemanagement.zxg.utils.TimeUtils.strToDateShort;

/**
 * Created by zxg on 17/3/15.
 */


public class DatabaseUtil {
    private static String TAG = DatabaseUtil.class.getSimpleName();
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
                eventModel = cursor2EventModel(cursor);
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
            list.add(cursor2EventModel(cursor));
        }
        db.close();
        return list;
    }

    /**
     * 查询startDate到endDate时间内的数据
     * @return
     **/
    public List<EventModel> queryByInterval(Date startDate, Date endDate){
        if (startDate == null || endDate == null) {
            return null;
        }

        List<EventModel> list = new ArrayList<EventModel>();

        list.addAll(queryByDate(startDate));
        // startDate和endDate不在同一天时
        // 只考虑了startDate和endDate相差一天的情况，相差多天时需要改造
        if (!TimeUtils.dateToStrShort(startDate).equals(TimeUtils.dateToStrShort(endDate))) {
            list.addAll(queryByDate(endDate));
        }

        for (int i = 0; i < list.size(); i++) {
            EventModel _eventModel = list.get(i);
            if (_eventModel.getRepeat() == 0) {
                if (!isCorrespondInterval(startDate, endDate, _eventModel.getRemind(), false)
                        && !isCorrespondInterval(startDate, endDate, _eventModel.getRemindAgain(), false)) {
                    list.remove(i);
                }
            } else {
                if (!isCorrespondInterval(startDate, endDate, _eventModel.getRemind(), true)
                        && !isCorrespondInterval(startDate, endDate, _eventModel.getRemindAgain(), true)) {
                    list.remove(i);
                }
            }
        }

        return list;
    }

    /**
     * 按日期查询
     * @return
     */
    public List<EventModel> queryByDate(Date date){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<EventModel> list1 = new ArrayList<EventModel>();
        Cursor cursor = db.query(helper.TABLE_NAME, null, "date=?",new String[]{TimeUtils.dateToStrShort(date)}, null, null, "datetime(remind)");

        while(cursor.moveToNext()){
            list1.add(cursor2EventModel(cursor));
        }

        List<EventModel> list2 = new ArrayList<EventModel>();
        // 查询符合条件的重复事件
        cursor = db.query(helper.TABLE_NAME, null, "repeat!=?",new String[]{"0"}, null, null, "repeat");
        while(cursor.moveToNext()){
            Date _date = TimeUtils.dateToShort(date);
            if (_date.equals(date)) {
                continue;
            }
            Date remindDate = TimeUtils.strToDateShort(cursor.getString(cursor.getColumnIndex("date")));
            Date repeatEndDate = strToDateShort(cursor.getString(cursor.getColumnIndex("repeatEnd")));
            switch (cursor.getInt(cursor.getColumnIndex("repeat"))) {
                case 1:
                    if (isCorrespond(_date, remindDate, repeatEndDate)) {
                        list2.add(cursor2EventModel(cursor));
                    }
                    break;
                case 2:
                    // 工作日，待完善
                    break;
                case 3:
                    if (isCorrespond(_date, remindDate, repeatEndDate) && (_date.getTime()-remindDate.getTime())/(24*60*60*1000)%7 == 0) {
                        list2.add(cursor2EventModel(cursor));
                    }
                    break;
                case 4:
                    if (isCorrespond(_date, remindDate, repeatEndDate) && (_date.getDate() == remindDate.getDate())) {
                        list2.add(cursor2EventModel(cursor));
                    }
                    break;
                case 5:
                    if (isCorrespond(_date, remindDate, repeatEndDate) && (_date.getMonth() == remindDate.getMonth()) && (_date.getDate() == remindDate.getDate())) {
                        list2.add(cursor2EventModel(cursor));
                    }
                    break;
            }
        }

        db.close();
        return DateModelUtil.sort(list1, list2);
    }

    /**
     * 按重复类型查询，可以一次性将所有重复类型的数据查询出来，然后根据重复类型分类
     * @return
     */
    public Map<String, List<EventModel>> queryByRepeatType(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Map<String, List<EventModel>> maps = new HashMap<String, List<EventModel>>();

        for (int i = 1; i < 6; i++) {
            List<EventModel> list = new ArrayList<EventModel>();
            Cursor cursor = db.query(helper.TABLE_NAME, null, "repeat=?",new String[]{""+i}, null, null, "remind");
            while(cursor.moveToNext()){
                list.add(cursor2EventModel(cursor));
            }
            maps.put(""+i, list);
        }
        db.close();
        return maps;
    }

    private EventModel cursor2EventModel (Cursor cursor) {
        EventModel eventModel = new EventModel();
        eventModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
        eventModel.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        eventModel.setDate(strToDateShort(cursor.getString(cursor.getColumnIndex("date"))));
        eventModel.setRemind(TimeUtils.strToDate(cursor.getString(cursor.getColumnIndex("remind"))));
        eventModel.setRemindAgain(TimeUtils.strToDate(cursor.getString(cursor.getColumnIndex("remindAgain"))));
        eventModel.setEmergency(cursor.getInt(cursor.getColumnIndex("emergency")));
        eventModel.setImportance(cursor.getInt(cursor.getColumnIndex("importance")));
        eventModel.setRepeat(cursor.getInt(cursor.getColumnIndex("repeat")));
        eventModel.setRepeatEnd(strToDateShort(cursor.getString(cursor.getColumnIndex("repeatEnd"))));
        eventModel.setComment(cursor.getString(cursor.getColumnIndex("comment")));
        eventModel.setIsFinished(cursor.getInt(cursor.getColumnIndex("isFinished"))>0);
        return eventModel;
    }

    private boolean isCorrespond(Date date, Date remindDate, Date repeatEndDate) {
        if (!remindDate.after(date)
                && (repeatEndDate == null || (repeatEndDate != null && !repeatEndDate.before(date)))){
            return true;
        }
        return false;
    }

    /**
     * 判断某个日期是否在某个区间内
     * @param startDate
     * @param endDate
     * @param date
     * @param isRepeat
     * @return
     */
    private boolean isCorrespondInterval(Date startDate, Date endDate, Date date, boolean isRepeat){
        if (startDate == null || endDate == null || date == null) {
            return false;
        }

        if (!date.after(endDate)) {
            long offset = 0;

            if (isRepeat) {
                offset = TimeUtils.dateToShort(startDate).getTime() - TimeUtils.dateToShort(date).getTime();
            }

            if ((date.getTime()+offset) >= startDate.getTime() && (date.getTime()+offset) <= endDate.getTime()) {
                return true;
            }
        }
        return false;
    }
}
