package com.madchya;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import util.IabHelper;
import util.IabResult;
import util.Inventory;
import util.Purchase;


public class MainActivity extends ActionBarActivity {
    private android.support.v7.app.ActionBar actionBar ;
    private Button addnewAccound;
    private Button clearTos;
    private Button explain ;
    private String oldname;
    private String oldversion;
    private AccountSplite mySQL = new AccountSplite(this);
    private int whichtype = 0;
    private SharedPreferences sharedpreference;
    private MyAlertdialog myAlertdialog;
    private ProgressBar progressBar;

    //ListView declare
    private ListView myListView ;
    private List<AccountContent> myAcContent = new ArrayList<>();
    private AccountAdapter adapter;
    private FileCreate fileCreate;

    //there declaration is about app in billing v3.
    private IabHelper mHelper;
    private boolean mHelperstate;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener;
    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhdok8YTglvXepk+BzTF8HuA+M8KKMXOv4STIkVPnUnBpBhVecmlUxxAIXZXVoY0lV73OxtrUlt5fsIsg8oUpbTCm3F9rot8KLYkvvO6obkITmVFxDin/1dRbtjYXZuGYCiH+VESzn+MU5hDw7g9QLtPY7zLpSTMgGVmsL1GEbn6jE5UGdyiVvKkd0ph6fjbze9PLFxTJ8oS4vSF3BNNzZuICLnJKD5ZxaqKZ8P9UA3/k+xTfsYaO4/IohLc5b9UwaIqAYTCIAz2AzHhB6R2yN2DZaAY+KdAOaRU9ONOFkJGDfsGM1vLmTspdWoP72PByTBaTIw5v3oNK6HeuMGUtKQIDAQAB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allfunctioninit();

    }
    private void allfunctioninit(){
        create_Listview();
        btn_setListener();
        ListsetListener();
        actionBar = getSupportActionBar();
        sharedpreference = this.getSharedPreferences("com.madchya_preferences", MODE_PRIVATE);
        fileCreate = new FileCreate();
        fileCreate.save(sharedpreference);
        progressBar = (ProgressBar)findViewById(R.id.progress0);
        if(sharedpreference.getInt("FirstTime" , 0)==0){
            fileCreate.rebuiledmadheadchange(fileCreate.getFilepath());
            checkmypathrenew(fileCreate.getFilepath());
            SharedPreferences.Editor editor = sharedpreference.edit();
            editor.putBoolean("haverenewmadchya" , false);
            editor.commit();
            alert(MainActivity.this,0);
            fileCreate.rebuiledmadheadchange(fileCreate.getFilepath());
        }
        GetRoot getRoot = new GetRoot(getPackageCodePath(),fileCreate.tosversionname(sharedpreference));
        ((TextView)findViewById(R.id.textroad)).setText(getString(R.string.path)+ fileCreate.getFilepath());
        billing_listener();
    }
    @Override
    protected void onStart() {
        super.onStart();
        String version = fileCreate.tosversionname(sharedpreference);
        String versions;
        if(version.equals("com.madhead.tos.zh")){
            versions = getString(R.string.commadheadtoszh);
        }else if(version.equals("com.madhead.tos.zh.ex")){
            versions = getString(R.string.commadheadtoszhex);
        }else if(version.equals("com.tencent.tmgp.madhead.tos")){
            versions = getString(R.string.comtencent);
        }else if(version.equals("com.madhead.tos.en")){
            versions = getString(R.string.commadheadtosen);
        }else if(version.equals("com.madhead.tos.ko")){
            versions = getString(R.string.commadheadtosko);
        }else {
            versions = "unKnow";
        }
        actionBar.setSubtitle(getString(R.string.version) + versions);
        adapter.notifyDataSetChanged();
        boolean b = sharedpreference.getBoolean("haverenewmadchya",false);
        if(b){
            checkmypathrenew(fileCreate.getFilepath());
            SharedPreferences.Editor editor = sharedpreference.edit();
            editor.putBoolean("haverenewmadchya" , false);
            editor.commit();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_startgame) {
            ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : infos) {
                System.out.println(runningAppProcessInfo.processName);
                if(runningAppProcessInfo.processName.equals(fileCreate.tosversionname(sharedpreference))) {
                    am.killBackgroundProcesses(runningAppProcessInfo.processName);
                }
            }
            Intent intent = getPackageManager().getLaunchIntentForPackage(fileCreate.tosversionname(sharedpreference));
            startActivity(intent);
        }
        if (id == R.id.action_settings){
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),SettingActivity.class);
            startActivity(intent);
        }
        if (id == R.id.explain){
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, News.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void create_Listview(){
        myListView = (ListView)findViewById(R.id.listView);
        myAcContent = mySQL.selectAll();
        adapter = new AccountAdapter(MainActivity.this, myAcContent);
        myListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void checkmypathrenew(String mypath){
        File f = new File(mypath);
        File[] files;
        String[] strings;
        if(f.isDirectory()){
            files = f.listFiles();
            if(files!=null){
               strings = new String[files.length];
               for(int i = 0 ; i<files.length;  i ++){
                   strings[i] = files[i].getName();
                   System.out.println(strings[i]);
               }
               mySQL.droptable();
               for(int i = 0 ; i<files.length;  i ++){
                   insertDataToListView(0,  strings[i]);
               }
                create_Listview();
            }
        }
    }
    private boolean insertDataToListView(int whichtype, String Accountname){
        String type = "Facebook";
        if(whichtype==0){
            type="Facebook";
        }else if(whichtype==1){
            type="Twitter";
        }else if(whichtype==2){
            type="Weibo";
        }else if(whichtype==3){
            type="No";
        }
        //write data to the SQLite if have a same data show log to user
        if(mySQL.search_Account(Accountname)){
            Toast.makeText(MainActivity.this, "have a same data in database",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            mySQL.add_Account(Accountname, whichtype);
            myAcContent.add(new AccountContent(Accountname, type));
            adapter.notifyDataSetChanged();
            return true;
        }

    }
    private boolean ChangeDataToListView(String oldversion ,int whichtype,String oldname, String Accountname){
        String type = "Facebook";
        if(whichtype==0){
            type="Facebook";
        }else if(whichtype==1){
            type="Twitter";
        }else if(whichtype==2){
            type="Weibo";
        }else if(whichtype==3){
            type="No";
        }
        //write data to the SQLite if have a same data show log to user
        if(mySQL.search_Account(oldname)){
            mySQL.updata_Account(oldversion,type,oldname,Accountname);
            return true;
        }else {
            return false;
        }
    }
    private boolean deletDataToListView(String oldname){
        String type ;
        //write data to the SQLite if have a same data show log to user
        if(mySQL.search_Account(oldname)){
            mySQL.delete_Account(oldname);
            return true;
        }else {
            return false;
        }
    }
    private void ListsetListener(){
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                new AlertDialog.Builder(view.getContext()).setTitle("please choose a function")
                .setItems(new String[]{"切換帳號" , "修改帳號名稱", "刪除此筆帳號"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case 0:
                                oldname = myAcContent.get(pos).getAccountName();
                                String Tospth  = fileCreate.tosversionname(sharedpreference);
                                fileCreate.clearshared_prefs(fileCreate.tosversionname(sharedpreference));
                                fileCreate.copydataToTOS(Tospth, fileCreate.getFilepath(),oldname);
                                Toast.makeText(MainActivity.this,"帳號切換完畢，請直接打開遊戲 :) !",Toast.LENGTH_LONG).show();
                            break;
                            case 1:
                                oldname = myAcContent.get(pos).getAccountName();
                                oldversion = myAcContent.get(pos).getAccountLogin();
                                LayoutInflater inflater = getLayoutInflater().from(MainActivity.this);
                                myAlertdialog = new MyAlertdialog(MainActivity.this,inflater,myhandler,1);
                                create_Listview();
                            break;
                            case 2:
                                oldname = myAcContent.get(pos).getAccountName();
                                if(deletDataToListView(oldname)){
                                    fileCreate.deletdata(fileCreate.getFilepath(),oldname);
                                }
                                create_Listview();
                            break;
                        }
                    }
                }).show();
            }
        });
    }
    private void btn_setListener(){
        addnewAccound = (Button)findViewById(R.id.addaccount);
        addnewAccound.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater().from(MainActivity.this);
                myAlertdialog = new MyAlertdialog(MainActivity.this,inflater,myhandler,0);

            }
        });
        clearTos = (Button)findViewById(R.id.clear1);
        clearTos.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileCreate.clearshared_prefs(fileCreate.tosversionname(sharedpreference));
            }
        });
        explain = (Button)findViewById(R.id.explanation);
        explain.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert(MainActivity.this,1);
            }
        });
    }
    public Handler myhandler = new Handler(){
        private static final int add_account = 0 ;
        private static final int change_account = 1 ;
        private static final int paycheck = 5 ;
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case add_account:
                   if((msg.obj)==null) {
                       boolean isok = insertDataToListView(myAlertdialog.getWhichtype(), myAlertdialog.getAccountname());
                       if (isok) {
                           String tosname = fileCreate.tosversionname(sharedpreference);
                           fileCreate.copydataToSD(tosname, fileCreate.getFilepath(), myAlertdialog.getAccountname(), myhandler);
                           progressBar.setVisibility(View.VISIBLE);
                       }
                   }else {
                       progressBar.setProgress(msg.arg1);
                       if(msg.arg2==1){
                           Toast.makeText(MainActivity.this,getString(R.string.buildfailed),Toast.LENGTH_SHORT).show();
                           progressBar.setVisibility(View.GONE);
                           deletDataToListView(myAlertdialog.getAccountname());
                       }else if(msg.arg2==2){
                           Toast.makeText(MainActivity.this,getString(R.string.buildoks),Toast.LENGTH_SHORT).show();
                           progressBar.setVisibility(View.GONE);
                       }else {
                           ;
                       }
                       create_Listview();
                   }

               break;
               case change_account:
                   boolean isok1 =ChangeDataToListView(oldversion,myAlertdialog.getWhichtype(),oldname, myAlertdialog.getAccountname());
                   if (isok1) {
                       fileCreate.renamedata(fileCreate.getFilepath(),oldname, myAlertdialog.getAccountname());
                   }
                   create_Listview();
               break;
               case paycheck:
                   Toast.makeText(MainActivity.this,R.string.pay_complete,Toast.LENGTH_LONG).show();
                   (findViewById(R.id.pro)).setVisibility(View.VISIBLE);
                   explain.setVisibility(View.GONE);
                   AdView mAdView = (AdView)findViewById(R.id.adView);
                   mAdView.setVisibility(View.GONE);
                   break;
           }
        }
    };
    private void alert(Context context,int type){
        switch (type){
            case 0 :
                new AlertDialog.Builder(context)
                        .setTitle(R.string.welecome_title)
                        .setMessage(R.string.welecome_madchya)
                        .setPositiveButton(R.string.welecome_setting, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = sharedpreference.edit();
                                editor.putInt("FirstTime", 1);
                                editor.commit();
                                Intent intent = new Intent();
                                intent.setClass(getApplicationContext(), SettingActivity.class);
                                startActivity(intent);
                            }
                        })
                        .show();
                break;
            case 1 :
                new AlertDialog.Builder(context)
                     .setTitle(R.string.pay_title)
                     .setMessage(R.string.pay_content)
                     .setPositiveButton(getString(R.string.certain), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mHelper.launchPurchaseFlow(MainActivity.this, "001", 10001, mPurchaseFinishedListener);
                            }
                        })
                    .setNegativeButton(getString(R.string.cencle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            ;
                    }
                })
                        .show();
                break;
        }
    }
    private void billing_listener(){
        base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhdok8YTglvXepk+BzTF8HuA+M8KKMXOv4STIkVPnUnBpBhVecmlUxxAIXZXVoY0lV73OxtrUlt5fsIsg8oUpbTCm3F9rot8KLYkvvO6obkITmVFxDin/1dRbtjYXZuGYCiH+VESzn+MU5hDw7g9QLtPY7zLpSTMgGVmsL1GEbn6jE5UGdyiVvKkd0ph6fjbze9PLFxTJ8oS4vSF3BNNzZuICLnJKD5ZxaqKZ8P9UA3/k+xTfsYaO4/IohLc5b9UwaIqAYTCIAz2AzHhB6R2yN2DZaAY+KdAOaRU9ONOFkJGDfsGM1vLmTspdWoP72PByTBaTIw5v3oNK6HeuMGUtKQIDAQAB";
        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d("TAG", "Problem setting up In-app Billing: " + result);
                }else {
                    Log.d("TAG", "Problem setting up In-app Billing: " + "ok");
                    String sss =  sharedpreference.getString("identification", "4s54s5s12d");
                    if(sss.equals("efejnjxekf")){
                        (findViewById(R.id.pro)).setVisibility(View.VISIBLE);
                        explain.setVisibility(View.GONE);
                        mHelperstate = true;
                    }else{
                        mHelper.queryInventoryAsync(mGotInventoryListener);
                    }
                }
            }
        });

        mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(IabResult result, Purchase purchase)
            {
                Log.d("ssss:", result.toString());
                if (result.isFailure()) {
                    Log.d("tag", "Error purchasing: " + result);
                    return;
                }
                else if (purchase.getSku().equals("001")) {
                    SharedPreferences.Editor editor = sharedpreference.edit();
                    editor.putString("identification" , "efejnjxekf");
                    editor.apply();
                    myhandler.obtainMessage(5).sendToTarget();
                }
            }
        };
        mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result,
                                                 Inventory inventory) {
                if (result.isFailure()) {
                    // handle error here
                    System.out.println("isFailure()");
                }
                else {
                    boolean mIsPremium = inventory.hasPurchase("001");
                    System.out.println("mIsPremium: " + mIsPremium);
                    if(mIsPremium){
                        SharedPreferences.Editor editor = sharedpreference.edit();
                        editor.putString("identification" , "efejnjxekf");
                        editor.apply();
                        (findViewById(R.id.pro)).setVisibility(View.VISIBLE);
                        explain.setVisibility(View.GONE);
                        AdView mAdView = (AdView)findViewById(R.id.adView);
                        mAdView.setVisibility(View.GONE);
                    }else {
                        AdView mAdView = (AdView)findViewById(R.id.adView);
                        AdRequest adRequest = new AdRequest.Builder().build();
                        mAdView.loadAd(adRequest);
                        SharedPreferences.Editor editor = sharedpreference.edit();
                        editor.putString("identification" , "4s54s5s12d");
                        editor.apply();
                    }
                }
            }
        };
    }
}
