package com.madchya;

import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;

import java.io.File;

/**
 * Created by steven on 2015/2/17.
 */
public class FileCreate extends GetRoot{
    String fileload;
    public FileCreate(){
        checkmadchya();
    }
    public void checkmadchya(){
        String filename = "madheadchange";
        File F = Environment.getExternalStorageDirectory();
        fileload = F.getPath()+File.separator+ filename;
        F = new File(fileload);
        if(!F.mkdir()){
            F.mkdir();
        }
    }
    public void save( SharedPreferences sharedpreference){
        SharedPreferences.Editor editor = sharedpreference.edit();
        editor.putString("localpath" ,fileload);
        editor.commit();
    }
    public String tosversionname( SharedPreferences sharedpreference){
        return  sharedpreference.getString("Version","com.madhead.tos.zh");
    }
    public String getFilepath(){
        return fileload;
    }
    public void copydataToTOS(String arg0 ,String arg1,String arg2){
        clearSurplus(arg2,arg0);
        super.copydataToTOS(arg0, arg1, arg2);
        super.upgradeRootroot(arg0);
    }
    public void copydataToSD(String arg0 ,String arg1,String arg2,Handler handler){
        super.copydataToSD(arg0, arg1, arg2);
        clearSurplus(arg2,arg0);
        FileCheck fileCheck = new FileCheck(arg1,arg2,handler);
        fileCheck.start();
    }
    public void renamedata(String arg1,String arg2,String arg3){
        super.renamedata(arg1, arg2, arg3);
    }
    public void deletdata(String arg1,String arg2){
        super.deletdata(arg1, arg2);
    }
    public void clearshared_prefs(String arg1){
        super.clearshared_prefs(arg1);
    }
    public void clearSurplus(String accountname,String version){
        String path= getFilepath()+File.separator+accountname;
        System.out.println(path);
        File file = new File(path);
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files!=null){
                for(int i = 0 ; i < files.length ; i++){
                    System.out.println(files[i].getName());
                    if(!(files[i].getName().equals(version+".xml")||
                       files[i].getName().equals(version+"_preferences.xml"))){
                        if(files[i].isFile()){
                            files[i].delete();
                        }
                    }
                }
            }
        }else {
            System.out.println("file.mkdir()");
        }
    }
    private class FileCheck extends Thread{
        int count = 0 ;
        String pathname;
        String accountname;
        String path;
        Handler handler;
        Integer integer = new Integer(10);
        public FileCheck(String pathname, String accountname,Handler handler){
            this.pathname = pathname;
            this.accountname = accountname;
            this.handler = handler;
            path = pathname+File.separator+accountname;
            System.out.println(path);
        }
        public void run(){
            while (true){
                try {
                    Thread.sleep(1000);
                    count += 20 ;
                    handler.obtainMessage(0,count,3,integer).sendToTarget();
                    if(count>60){
                        File f = new File(path);
                        if(!f.exists()){
                            handler.obtainMessage(0,count,1,integer).sendToTarget();
                            return;
                        }else {
                            handler.obtainMessage(0,count,2,integer).sendToTarget();
                            return;
                        }
                    }
                }catch (Exception e){
                    handler.obtainMessage(0,count,1,integer).sendToTarget();
                }
            }
        }
    }
    public void rebuiledmadheadchange(String mypath){
        File f = new File(mypath);
        File[] files;
        File[] files1 ;
        String[] strings;
        String[] stringss;
        if(f.isDirectory()){
            files =  f.listFiles();
            strings = new String[files.length];
            for(int i = 0 ; i < files.length ; i++){
                strings[i] = files[i].getPath();
                System.out.println(strings[i]);
                File fd = new File(strings[i]);
                files1 = fd.listFiles();
                if(files1!=null) {
                    stringss = new String[files1.length];
                    for (int ii = 0; ii < files1.length; ii++) {
                        stringss[ii] = files1[ii].getPath();
                        System.out.println("ii" + stringss[ii]);
                        if (stringss[ii] != null) {
                            if (stringss[ii].contains("shared_prefs")) {
                                super.rebuiledmadheadchange(files[i].getPath(), stringss[ii]);
                            }
                        }
                    }
                }
            }
        }
    }
}
