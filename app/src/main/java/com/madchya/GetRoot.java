package com.madchya;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steven on 2015/2/17.
 */
public class GetRoot {
    public GetRoot(String pkgCodePath,String tospath){
        upgradeRootPermission(pkgCodePath);
        upgradeRootroot(tospath);
    }

    public GetRoot(){
        ;
    }

    public static boolean upgradeRootPermission(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd="chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }
    public static boolean upgradeRootroot(String pkgCodePath) {
            Process process = null;
            DataOutputStream os = null;
            try {
                String command0 = "chmod 777 " + "data" + File.separator + "data" +  File.separator
                        +pkgCodePath+File.separator+"shared_prefs\n";
                String command = "chmod 777 " + "data" + File.separator + "data" +  File.separator
                    +pkgCodePath+File.separator+"shared_prefs/*";
                process = Runtime.getRuntime().exec("su");
                os = new DataOutputStream(process.getOutputStream());
                os.writeBytes(command0);
                os.writeBytes(command + "\n");
                os.writeBytes("exit\n");
                os.flush();
                process.waitFor();
            } catch (Exception e) {

            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                    process.destroy();
                } catch (Exception e) {
                }
            }
            return true;
    }

    /********************************************
     * this can copy data from shared_prefs file to to madchya then change name
     * @param version tos version
     * @param mypath madchyapath
     * @param accountname user give name for his account
     ********************************************/
    public void copydataToSD(String version , String mypath ,String accountname){
        Process process = null;
        DataOutputStream os = null;
        try {

            if(mypath.contains("/0/")){
                mypath = mypath.replace("0","legacy");
            }
            String command1 = "cp -r data" + File.separator + "data" +  File.separator
                    +version + File.separator+ "shared_prefs"+" "+ mypath+File.separator+accountname +File.separator;
            String command = "busybox cp -r data" + File.separator + "data" +  File.separator
                +version + File.separator+ "shared_prefs"+" "+ mypath+File.separator+accountname +File.separator;

            command = command.concat("\n");
            command1 = command1.concat("\n");
            System.out.println(command);
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(command.getBytes());
            os.write(command1.getBytes());
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            System.out.println("false");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
    }
    public String execCommand(String command) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec(command);
        try {
            if (proc.waitFor() != 0) {
                System.err.println("exit value = " + proc.exitValue());
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    proc.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line);
            }
            return stringBuffer.toString();

        } catch (InterruptedException e) {
            System.err.println(e);
            return "";
        }
    }
    public void checkversion(String version,String mypath,String accountname){
        File file = new File(mypath+File.separator+accountname);
        List<File> f = new ArrayList<File>();
        File[] files ;
        if(file.isDirectory()){
            files = file.listFiles();
        }else {
            files = file.listFiles();
        }
        String s1 = mypath+File.separator+accountname+File.separator+version+".xml";
        String s2 = mypath+File.separator+accountname+File.separator+version+"_preferences.xml";
        System.out.println("1 " +s1);
        System.out.println("2 " +s2);
        for (int i = 0 ; i<files.length ; i++){
            System.out.println(files[i].getPath());
            if(!(files[i].getPath().equals(s1)||files[i].getPath().equals(s2))){
                if(!files[i].isDirectory()) {
                    if(files[i].getPath().contains(version.substring(0,16))) {
                        System.out.println(version  + " : " +files[i].getPath().contains(version));
                        if (files[i].getPath().contains("_preferences.xml")) {
                            System.out.println(true);
                            File file1 = new File(files[i].getPath());
                            File file2 = new File(s2);
                            file1.renameTo(file2);
                        } else if(files[i].getPath().contains("toswidget")){
                            ;
                        } else {
                            System.out.println(false);
                            File file1 = new File(files[i].getPath());
                            File file2 = new File(s1);
                            file1.renameTo(file2);
                        }
                    }
                }
            }
        }
    }
    public void copydataToTOS(String version , String arg1 ,String arg2){
        Process process = null;
        DataOutputStream os = null;
        try {
            checkversion(version ,arg1 ,arg2);
            if(arg1.contains("/0/")){
                arg1 = arg1.replace("0","legacy");
            }
            String command = "busybox cp -r " +arg1+File.separator+arg2+File.separator+"*"+ " "+"data" +File.separator + "data" +  File.separator
                    +version + File.separator+ "shared_prefs"+" \n";
            String command1 = "cp -r " +arg1+File.separator+arg2+File.separator+"*"+ " "+"data" +File.separator + "data" +  File.separator
                    +version + File.separator+ "shared_prefs"+" \n";
            System.out.println(command);
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(command.getBytes());
            os.write(command1.getBytes());
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            System.out.println("false");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
    }
    public void renamedata(String arg1,String arg2,String arg3){
        Process process = null;
        DataOutputStream os = null ;
        try {
            if(arg1.contains("/0/")){
                arg1 = arg1.replace("0","legacy");
            }
            String command = "mv "+arg1+File.separator+arg2 +" "+ arg1+File.separator+arg3;
            System.out.println(command);
            command = command.concat("\n");
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(command.getBytes());
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            System.out.println("false");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
    }
    public void deletdata(String arg1,String arg2){
        Process process = null;
        DataOutputStream os = null ;
        try {
            if(arg1.contains("/0/")){
                arg1 = arg1.replace("0","legacy");
            }
            String command = "rm -r "+arg1+File.separator+arg2;
            System.out.println(command);
            command = command.concat("\n");
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(command.getBytes());
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            System.out.println("false");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
    }
    public void clearshared_prefs(String arg1){
        Process process = null;
        DataOutputStream os = null ;
        try {
            String command = "rm -r data/data/"+arg1+File.separator+"shared_prefs/*\n";
            System.out.println(command);
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(command.getBytes());
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            System.out.println("false");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
    }
    public void getroots() {
        ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
        pb.directory(new File("/"));//设置shell的当前目录。
        try {
            Process proc = pb.start();
            //获取输入流，可以通过它获取SHELL的输出。
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader err = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            //获取输出流，可以通过它向SHELL发送命令。
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc
                    .getOutputStream())), true);
            out.println("pwd");
            out.println("su root");//执行这一句时会弹出对话框（以下程序要求授予最高权限...），要求用户确认。
            out.println("cd /data/data");//这个目录在系统中要求有root权限才可以访问的。
            out.println("ls -l");//这个命令如果能列出当前安装的APK的数据文件存放目录，就说明我们有了ROOT权限。
            out.println("exit");
            // proc.waitFor();
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            while ((line = err.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            out.close();
            proc.destroy();
        } catch (Exception e) {
            System.out.println("exception:" + e);
        }
    }
    public void rebuiledmadheadchange(String mypath,String filename){
        Process process = null;
        DataOutputStream os = null;
        try {
            if(mypath.contains("/0/")){
                mypath = mypath.replace("0","legacy");
            }
            if(filename.contains("/0/")){
                filename = filename.replace("0","legacy");
            }
            String command = "mv "+filename+File.separator+"*"+" "+mypath;
            command = command.concat("\n");
            System.out.println(command);
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(command.getBytes());
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            System.out.println("false");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
    }

}
