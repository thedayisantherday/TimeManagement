package com.timemanagement.zxg.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.timemanagement.zxg.activities.activitycontrol.ActivityManager;
import com.timemanagement.zxg.activities.activitycontrol.MyApplication;

import java.io.InputStream;

/**
 * Created by zxg on 2016/9/30.
 * QQ:1092885570
 */
public class Tools {

    private static String TAG = Tools.class.getSimpleName();

    private static Toast mToast;
    
    public static long lastClickTime;
    /**
     * 防止按钮短时间内重复点击
     * @return
     */
    public synchronized static boolean isFastClick(){
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime < 1000){
            return true;
        }
        lastClickTime = currentTime;
        return false;
    }
    /**
     * 判断字符串是否为空
     * @param text
     * @return
     */
    public static boolean isEmpty(String text) {
        if (text == null || "".equals(text.trim()) || text.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String upperCaseFirstOne(String str) {
        if(Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
           return (new StringBuilder())
                   .append(Character.toUpperCase(str.charAt(0)))
                   .append(str.substring(1)).toString();
        }
    }

    /**
     * 得到字符串的字符长度, 中文算两个字符
     * @param string
     * @return
     */
    public static int getCharCount(String string){
        int iCharCount = 0;
        if(isEmpty(string)) {
            return iCharCount;
        }

        String Reg = "^[\u4e00-\u9fa5]{1}$";
        for (int i = 0; i < string.length(); i++) {
            String b = Character.toString(string.charAt(i));
            if (b.matches(Reg)) {
                iCharCount += 2;
            } else {
                iCharCount++;
            }
        }
        return iCharCount;
    }

    /**
     * 判断password是否是否为连续字母、数字
     * @param password
     * @return
     */
    public static boolean isSimplePassword(String password){
        if (isEmpty(password)) {
            return true;
        }
        if (isSameChar(password) | isOrderNumber(password)){
            return true;
        }
        return false;
    }

    /**
     * 判断password是否所有的字符都相同
     * @param password
     * @return
     */
    public static boolean isSameChar(String password){
        if (isEmpty(password)) {
            return true;
        }
        boolean flag = true;
        char ch = password.charAt(0);
        for (int i = 0; i < password.length(); i++) {
            if (ch != password.charAt(i)){
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 判断password是否为连续递增或连续递减的数字，如123456、987654
     * @param password
     * @return
     */
    public static boolean isOrderNumber(String password){
        if (isEmpty(password)) {
            return true;
        }
        boolean flag = true; //若全是连续数字，返回true
        boolean isNumber = true; //若全是数字，返回true
        for (int i = 0; i < password.length(); i++) {
            if (!Character.isDigit(password.charAt(i))) {
                isNumber = false;
                break;
            }
        }

        if (isNumber){ //若全是数字，则继续判断是否为连续数字
            boolean isIncrease = true; //若为连续递增数字，返回true
            boolean isDecrease = true; //若为连续递减数字，返回true
            //是否连续递增
            for (int i = 0; i < password.length(); i++) {
                if (i > 0){
                    int num1 = Integer.parseInt(password.charAt(i) + "");
                    int num2 = Integer.parseInt(password.charAt(i-1) + "");
                    if (num1 != num2+1){
                        isIncrease = false;
                        break;
                    }
                }
            }
            //是否连续递减
            for (int i = 0; i < password.length(); i++) {
                if (i > 0){
                    int num1 = Integer.parseInt(password.charAt(i) + "");
                    int num2 = Integer.parseInt(password.charAt(i-1) + "");
                    if (num1 != num2-1){
                        isDecrease = false;
                        break;
                    }
                }
            }
            flag = isIncrease | isDecrease;
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 限制输入框小数点位数
     * @param editText
     * @param decimalDigits 限制的位数
     */
    public static void setEditTextDecimal(final EditText editText, final int decimalDigits) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /**
                 * 正则表达式：
                 * ^ 正则匹配开始； 0{0,6} 匹配0到6个0； \\. 等于\.匹配.；
                 * (\\.0{1,2})? 匹配一个或零个(\\.0{1,2})； \\d 等于\d也等于[0-9]
                 */
                /*String regEx = "(^10{0,6}(\\.0{1,2})?)|(^[1-9]\\d{0,5}(\\.\\d{1,2})?)";
                Pattern pattern = Pattern.compile(regEx);
                Matcher matcher = pattern.matcher(s.toString().trim());
                // 查找字符串中是否有匹配正则表达式的字符/字符串
                boolean rs = matcher.find();
                System.out.println("++++++++++++++++++++"+rs);*/

                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > decimalDigits) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + decimalDigits + 1);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }

    /**
     * 以最小内存读取本地资源图片
     * @param context
     * @param bitmapResId
     * @return
     */
    public static Bitmap readBitmap(Context context, int bitmapResId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(bitmapResId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void showToast(final String message){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Activity activity = ActivityManager.getInstance().getTopActivity();
                LogUtils.i(TAG, "Tools activity:"+activity+", message"+message);
                if (Tools.isEmpty(message)){
                    return;
                }
                if (activity == null || activity.isFinishing()){
                    return;
                }
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 不基于堆栈里的Activity，为了防止某些页面关闭Toast无法弹出
     *
     * @param message
     */
    public static void showToastNoActivity(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyApplication.getInstance(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Fragment类和Activity都可以用这个方法弹出Toast
     *
     * @param context
     * @param str
     */

    public static void showTip(Context context, String str) {
        boolean canLooper = true;
        try {
            Looper.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            canLooper = false;
        }

        if (mToast == null) {
            mToast = Toast.makeText(context, str, Toast.LENGTH_LONG);
        } else {
            mToast.setText(str);
        }
        mToast.show();
        try {
            if (canLooper) {
                Looper.loop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 格式化时间，如九点零五分：09:05
     * @param num
     * @return
     */
    public static String formatTime (int num) {
        //小于10时，在前面加0
        if (Math.abs(num) < 10) {
            return "0"+num;
        }
        return ""+num;
    }
}
