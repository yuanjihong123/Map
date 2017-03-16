package com.example.administrator.mapclient.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

/**
 * 查询缓存和清空缓存工具类
 */

public class CacheUtils {
    /**
     * 获取文件夹大小
     * @param file
     * @return
     */
    public static long getFolderSize(File file){
        long size = 0;
        File[] listFiles = file.listFiles();
        for(int i=0;i<listFiles.length;i++){
            if(listFiles[i].isDirectory()){//如果里面还有文件夹
                size = size + getFolderSize(listFiles[i]);
            }else{
                size = size + listFiles[i].length();
            }
        }
        return size;
    }

    /**
     * 总的缓存大小
     * @param context
     * @return
     */
    public static String getTotalCacheSize(Context context){
        long cacheSize = getFolderSize(context.getCacheDir());
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
             cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 清空缓存
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    /**
     * 删除文件夹
     * @param dir
     * @return
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 格式化单位
     * @return
     */
    public static String getFormatSize(double size){
        double kiloByte = size/1024;
        if (kiloByte < 1){
            return "0.00KB";
        }
        double megaByte = kiloByte/1024;
        if(megaByte < 1){
            BigDecimal bigDecimal = new BigDecimal(Double.toString(kiloByte));
            return bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString()+"KB";
        }
        double gigaByte = megaByte/1024;
        if(gigaByte < 1){
            BigDecimal bigDecimal2 = new BigDecimal(Double.toString(megaByte));
            return bigDecimal2.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString()+"MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal bigDecimal3 = new BigDecimal(Double.toString(gigaByte));
            return bigDecimal3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal bigDecimal4 = new BigDecimal(teraBytes);
        return bigDecimal4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }
}
