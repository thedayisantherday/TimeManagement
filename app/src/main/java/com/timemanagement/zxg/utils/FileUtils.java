package com.timemanagement.zxg.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by zxg on 2016/9/21.
 * QQ:1092885570
 */
public class FileUtils {
    /**
     * 图片缓存的根目录
     */
    public static final String IMAGE_CACHE_DIR = "MyProgram";

    /**
     * 判断SDCard是否可用
     * @return true:SD卡可用，false:SD卡不可用
     */
    public static boolean isSDCardValiable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取指定目录的剩余空间
     * @param filePath
     * @return
     */
    public static long getFileFreeSize(String filePath){
        android.os.StatFs statFs = new android.os.StatFs(filePath);
        //获取SDCard上每一个block的size
        long lBlockSize = statFs.getBlockSize();
        //获取可用的block数量
        long lBlockAmount = statFs.getAvailableBlocks();
        //计算可用空间的总大小
        long lFreeSize = lBlockSize * lBlockAmount;
        return lFreeSize;
    }

    /**
     * 获取一个可用的存储路径
     * @return
     */
    public static String getSDCardDir(){
        String pathDir = null;
        //获取内置SDCard路径
        File sdFile = Environment.getExternalStorageDirectory();
        //获取SDCard的父路径
        File parentFile = sdFile.getParentFile();
        //该父目录下的全部路径
        File[] list_Files = parentFile.listFiles();
        if(list_Files != null){
            long lMaxFreeSize = 0L;
            for (int i = 0; i < list_Files.length; i++) {
                if (list_Files[i].canWrite()){
                    String tempPathDir = list_Files[i].getAbsolutePath();
                    long lTempSize = FileUtils.getFileFreeSize(tempPathDir);
                    if(lTempSize > lMaxFreeSize){
                        lMaxFreeSize = lTempSize;
                        pathDir = tempPathDir;
                    }
                }
            }
        }
        return pathDir;
    }
}
