package com.timemanagement.zxg.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.timemanagement.zxg.config.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zxg on 2016/9/19.
 * QQ:1092885570
 *
 * 日志打印输出类
 */
public class LogUtils {
    // log标签
    private static final String TAG = LogUtils.class.getSimpleName();
    //自定义log的TAG
    private static String LOG_TAG = null;
    // 日志保存的默认文件名
    public static final String DEFAULT_FILE_NAME = "myProgram_log";
    // LOG保存目录
    public static String LOG_DIR = "logDir";

    // 日志保存模式1：固定日志文件
    public static final int SAVE_MODE_FIXED = 1;
    // 日志保存模式2：按日期日志文件
    public static final int SAVE_MODE_DATE = 2;
    //日志保存模式
    public static int SAVE_MODE = SAVE_MODE_FIXED;
    //固定日志文件名
    public static String LOG_FILENAME_FIXED = "android";
    //log时间格式
    public static SimpleDateFormat LOG_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    //log时间格式
    public static SimpleDateFormat LOG_FILENAME_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // log前缀
    public static String LOG_PRE = "+===+";
    //日志文件后缀
    private static String LOG_FILE_SUFFIX = ".txt";
    //日志分隔符
    private static final String LOG_SPLIT = " \t<||> ";
    /**
     * 安全级别日志，
     * true：不输入和保存任何日志，false:可选择输入和保存日志
     */
    public static boolean IS_SECURITY_LOG = false;
    //是否为调试模式，true：在控制台输出，false：不在控制台输出
    public static boolean IS_DEBUG = Config.DEBUG;

    // 日志级别
    public static final int LEVEL_V = 1;
    public static final int LEVEL_D = 2;
    public static final int LEVEL_I = 3;
    public static final int LEVEL_W = 4;
    public static final int LEVEL_E = 5;
    // debug日志级别
    public static int DEBUG_LEVEL = LEVEL_V;

    //是否输入LOG的位置，true：输出，false：不输出
    public static boolean IS_LOG_POSITION = false;
    //是否保存E级别的log信息，true：保存，false：不保存
    public static boolean IS_SAVE_LOG_E = true;
    //是否保存W级别的log信息，true：保存，false：不保存
    public static boolean IS_SAVE_LOG_W = true;
    //是否保存I级别的log信息，true：保存，false：不保存
    public static boolean IS_SAVE_LOG_I = false;
    //是否保存D级别的log信息，true：保存，false：不保存
    public static boolean IS_SAVE_LOG_D = false;
    //是否保存V级别的log信息，true：保存，false：不保存
    public static boolean IS_SAVE_LOG_V = false;

    //日志绝对路径前缀
    private static String LOG_ABS_PATH_PRE = null;
    //日志绝对路径
    private static String LOG_ABS_PATH = null;

    /**
     * 初始化日志
     */
    public static void initLog(Context context){
        LOG_ABS_PATH_PRE = getAppStorageDir(context);
        i(TAG, "日志保存路径：" + LOG_ABS_PATH_PRE);
    }

    /**
     * 输出信息级别v的log
     * @param tag
     * @param msg
     */
    public static void v (String tag, String msg){
        printLog(LEVEL_V, tag, msg, new String[0]);
    }

    /**
     * 输出信息级别d的log
     * @param tag
     * @param msg
     */
    public static void d (String tag, String msg){
        printLog(LEVEL_D, tag, msg, new String[0]);
    }

    /**
     * 输出信息级别i的log
     * @param tag
     * @param msg
     */
    public static void i (String tag, String msg){
        printLog(LEVEL_I, tag, msg, new String[0]);
    }


    /**
     * 输出信息级别w的log
     * @param tag
     * @param msg
     */
    public static void w (String tag, String msg){
        printLog(LEVEL_W, tag, msg, new String[0]);
    }

    /**
     * 输出信息级别e的log
     * @param tag
     * @param msg
     */
    public static void e (String tag, String msg){
        printLog(LEVEL_E, tag, msg, new String[0]);
    }

    /**
     * 输出各个信息级别的log
     * @param tag
     * @param msg
     */
    public static void printLog (int LEVEL, String tag, String msg, String... fileArray){
        if(IS_SECURITY_LOG)
            return;
        else{
            tag = LOG_TAG==null ? tag : LOG_TAG;
            String message = (msg==null ? "" : msg);

            if(IS_LOG_POSITION)
                message = getPositionInfo() + LOG_SPLIT + message;

            switch (LEVEL) {
                case LEVEL_V:
                    if (IS_DEBUG && DEBUG_LEVEL <= LEVEL_V)
                        Log.v(LOG_PRE + tag, message);

                    if (IS_SAVE_LOG_V)
                        saveLog(tag, message, "V", fileArray);
                    break;
                case LEVEL_D:
                    if (IS_DEBUG && DEBUG_LEVEL <= LEVEL_D)
                        Log.d(LOG_PRE + tag, message);

                    if (IS_SAVE_LOG_D)
                        saveLog(tag, message, "D", fileArray);
                    break;
                case LEVEL_I:
                    if (IS_DEBUG && DEBUG_LEVEL <= LEVEL_I)
                        Log.i(LOG_PRE + tag, message);

                    if (IS_SAVE_LOG_I)
                        saveLog(tag, message, "I", fileArray);
                    break;
                case LEVEL_W:
                    if (IS_DEBUG && DEBUG_LEVEL <= LEVEL_W)
                        Log.w(LOG_PRE + tag, message);

                    if (IS_SAVE_LOG_W)
                        saveLog(tag, message, "W", fileArray);
                    break;
                case LEVEL_E:
                    if (IS_DEBUG && DEBUG_LEVEL <= LEVEL_E)
                        Log.e(LOG_PRE + tag, message);

                    if (IS_SAVE_LOG_E)
                        saveLog(tag, message, "E", fileArray);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 获取log的具体位置
     * @return
     */
    private static String getPositionInfo(){
        /**
         * StackTraceElement堆栈帧，用来获取方法的调用信息
         * 栈中先执行的方法是VM和Thread中的方法，第3条才是你调用的方法
         */
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[2];
        return stackTraceElement.getFileName() + " : Line " + stackTraceElement.getLineNumber();
    }

    /**
     * 保存日志
     * @param tag
     * @param msg
     * @param priority
     * @param fileArray
     */
    private synchronized static void saveLog(String tag, String msg,
                                             String priority, String[] fileArray){
        if(fileArray!=null && fileArray.length>0){
            for (int i = 0; i < fileArray.length; i++) {
                saveLog(tag, msg, priority, fileArray[i]);
            }
        }else
            saveLog(tag, msg, priority, DEFAULT_FILE_NAME);
    }

    /**
     * 保存日志
     * @param tag
     * @param msg
     * @param priority
     * @param fileName
     */
    private synchronized static void saveLog(String tag, String msg,
                                             String priority, String fileName){
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String currentTime = LOG_TIME_FORMAT.format(date);
        String currentTime2 = LOG_FILENAME_FORMAT.format(date);

        //打印到哪个文件
        File logFile = getLogFile(currentTime2, fileName);

        FileWriter printWriter = null;
        try{
            if(logFile!=null && logFile.isFile()){
                String logMessage = "" + currentTime + " : " + priority + " / "
                        + tag + LOG_SPLIT + msg + "\r\n";
                printWriter = new FileWriter(logFile, true);
                /**
                 * 测试环境直接输出，生产环境需要加密
                 * 生产环境暂时没用加密算法处理，有待后续开发
                 */
                if(IS_DEBUG) {
                    printWriter.append(logMessage);
                } else {
                    //TODO
                    //生产环境需要加密
//                    printWriter.append(logMessage);
                }
                printWriter.flush();
            }
        }catch (FileNotFoundException e){
            Log.e(LOG_PRE + TAG, e.toString());
        }catch (IOException e){
            Log.e(LOG_PRE + TAG, e.toString());
        } finally {
            try {
                if(printWriter != null)
                    printWriter.close();
            }catch (IOException e){
                Log.e(LOG_PRE + TAG, e.toString());
            }
        }
    }

    /**
     * 获取log打印输出的文件
     * @param currentTime
     * @param fileName
     * @return
     */
    private synchronized static File getLogFile(String currentTime, String fileName){
        File logFile = null;
        try {
            if (FileUtils.isSDCardValiable() && isEnoughFreeSize()){
                String logDir = getLogPath();
                if(logDir!=null && !logDir.trim().equals("")){
                    String tempLogDir = null;
                    if (fileName!=null && !fileName.trim().equals(""))
                        tempLogDir = logDir + File.separator + fileName + LOG_FILE_SUFFIX;
                    else if (SAVE_MODE == SAVE_MODE_DATE)
                        tempLogDir = logDir + File.separator + currentTime + LOG_FILE_SUFFIX;
                    else {
                        if(LOG_FILENAME_FIXED!=null && !LOG_FILENAME_FIXED.trim().equals(""));
                            tempLogDir = logDir + File.separator + LOG_FILENAME_FIXED + LOG_FILE_SUFFIX;
                    }

                    if (tempLogDir == null)
                        return null;

                    logFile = new File(tempLogDir);
                    if(logFile==null || !logFile.isFile()){
                        if(!logFile.createNewFile())
                            logFile = null;
                    }

                    if (logFile!=null && !logFile.isFile())
                        logFile = null;
                }
            } else
                Log.e(LOG_PRE + TAG, "SDCard不可用或SDCard空间不足2M");
        }catch (IOException e){
            Log.e(LOG_PRE + TAG, e.toString());
            logFile = null;
        }
        return logFile;
    }

    /**
     * 判断是否有足够的空间打印输出log，至少2M
     * @return
     */
    private static boolean isEnoughFreeSize(){
        if(LOG_ABS_PATH_PRE == null)
            LOG_ABS_PATH = FileUtils.getSDCardDir() + File.separator + LOG_DIR;
        else
            LOG_ABS_PATH = LOG_ABS_PATH_PRE + File.separator + LOG_DIR;

        long lSDFreeSize = 0;
        try {
            File file = new File(LOG_ABS_PATH);
            if (!file.exists()){
                file.mkdir();
            }
            lSDFreeSize = FileUtils.getFileFreeSize(LOG_ABS_PATH);
        }catch (Exception e){
            return false;
        }

        long lTwoM = 2 * 1024 * 1024;
        if(lSDFreeSize > lTwoM)
            return true;

        return false;
    }

    /**
     * 获取log输出文件的路径
     * @return
     */
    private static String getLogPath(){
        if(LOG_ABS_PATH_PRE == null)
            LOG_ABS_PATH = FileUtils.getSDCardDir() + File.separator + LOG_DIR;
        else
            LOG_ABS_PATH = LOG_ABS_PATH_PRE + File.separator + LOG_DIR;

        File logFile = new File(LOG_ABS_PATH);
        //如果文件存在且不是文件夹
        if(logFile.exists() && !logFile.isDirectory()){
            logFile.delete();
            logFile = new File(LOG_ABS_PATH);
            if(!logFile.mkdirs())
                return null;
        }else{
            if (logFile!=null && !logFile.exists()) {
                if (!logFile.mkdirs())
                    return null;
            }
        }

        return LOG_ABS_PATH;
    }

    /**
     * 获取APP的存储路径
     * @param context
     * @return
     */
    private static String getAppStorageDir(Context context){
        //获取Android程序在SD上的保存目录约定；当程序卸载时，系统会自动删除
        File file = context.getExternalFilesDir(null);
        if(file == null){
            //获取外部的存储目录，即SDCard
            String storageDir = Environment.getExternalStorageDirectory().toString();
            File fDir = new File(storageDir);
            //如果即SDCard不可用
            if(!fDir.canWrite()) {
                //获取可用目录
                storageDir = FileUtils.getSDCardDir();
                if (storageDir != null) {
                    storageDir = storageDir + File.separator + context.getApplicationInfo().packageName;
                    //项目存储路径自动寻找可用存储空间storageDir
                    return storageDir;
                } else
                    //没有找到可用的存储路径，则用cacheDir
                    return context.getCacheDir().toString();
            }else{
                //存储路径采用SDCard地址
                storageDir = storageDir + File.separator + context.getApplicationInfo().packageName;
                return storageDir;
            }
        }else {
            //存储路径采用系统分配的路径
            String storageDir = file.getAbsolutePath();
            return storageDir;
        }
    }

    public static void setLogSaveLevel(int LOG_LEVEL){
        if (LOG_LEVEL == LEVEL_V){
            IS_SAVE_LOG_V = true;
            IS_SAVE_LOG_D = true;
            IS_SAVE_LOG_I = true;
            IS_SAVE_LOG_W = true;
            IS_SAVE_LOG_E = true;
        } else if (LOG_LEVEL == LEVEL_D){
            IS_SAVE_LOG_V = false;
            IS_SAVE_LOG_D = true;
            IS_SAVE_LOG_I = true;
            IS_SAVE_LOG_W = true;
            IS_SAVE_LOG_E = true;
        } else if (LOG_LEVEL == LEVEL_I){
            IS_SAVE_LOG_V = false;
            IS_SAVE_LOG_D = false;
            IS_SAVE_LOG_I = true;
            IS_SAVE_LOG_W = true;
            IS_SAVE_LOG_E = true;
        } else if (LOG_LEVEL == LEVEL_W){
            IS_SAVE_LOG_V = false;
            IS_SAVE_LOG_D = false;
            IS_SAVE_LOG_I = false;
            IS_SAVE_LOG_W = true;
            IS_SAVE_LOG_E = true;
        } else if (LOG_LEVEL == LEVEL_E){
            IS_SAVE_LOG_V = false;
            IS_SAVE_LOG_D = false;
            IS_SAVE_LOG_I = false;
            IS_SAVE_LOG_W = false;
            IS_SAVE_LOG_E = true;
        }else {
            IS_SAVE_LOG_V = false;
            IS_SAVE_LOG_D = false;
            IS_SAVE_LOG_I = false;
            IS_SAVE_LOG_W = false;
            IS_SAVE_LOG_E = false;
        }
    }

    /**
     * 打印全部异常信息
     * @param exception
     * @return
     */
    public static String getExceptionAllInfo(Exception exception){
        String out = "" + exception.toString() + "\n";
        StackTraceElement[] trace = exception.getStackTrace();
        for(StackTraceElement s : trace)
            out += "\tat " + s + "\r\n";

        return out;
    }
}
