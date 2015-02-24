package com.madchya;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by steven on 2015/2/17.
 */
public class MyAlertdialog  {
    private final static int addnewAccount = 0 ;
    private final static int changeoldAccount = 1 ;
    private int type ;

    private AccountSplite accountSplite;

    private String Title ="Add a new name";
    private String Message = "Please insert a new name for your Tos account";

    private String accountname,accounttype;

    private int whichtype;
    private Handler myhandler;

    public MyAlertdialog (Context context ,LayoutInflater inflater,Handler myhandler ,int x){
        accountSplite = new AccountSplite(context);
        this.myhandler = myhandler;
        type_init(context,x);
        alert(context, inflater);
    }


    private void alert(Context context,LayoutInflater inflater){
        //define view body
        final EditText editText;
        final Spinner spinner ;

        //use Layout inflater
        LayoutInflater inflaters = inflater;
        final View alertview  = inflaters.inflate(R.layout.insertaccount, null);

        editText = (EditText)alertview.findViewById(R.id.editText);
        spinner  = (Spinner )alertview.findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter1 =new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item);
        ArrayList<String> logictype= new ArrayList<String>();
        logictype.add("Facebook");
        logictype.add("Twitter ");
        logictype.add("Webio");
        logictype.add("No ");
        adapter1.addAll(logictype);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                whichtype = (int)id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        new AlertDialog.Builder(context)
                .setView(alertview)
                .setTitle(Title)
                .setMessage(Message)
                .setPositiveButton(context.getString(R.string.certain), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText() != null) {
                            myhandler.obtainMessage(type).sendToTarget();
                            accountname = editText.getText().toString();
                        }
                    }
                })
                .setNegativeButton(context.getString(R.string.cencle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                })
        .show();
    }

    private void type_init(Context context ,int x ) {
        switch (x){
            case addnewAccount:
                type = addnewAccount ;
                Title = context.getString(R.string.addanewaccount);
                Message = context.getString(R.string.addanewaccount_conetnt);
                break;
            case  changeoldAccount:
                type = changeoldAccount ;
                Title = context.getString(R.string.changeaccount);
                Message = context.getString(R.string.changeaccount_content);
                break;
        }
    }

    public String getAccountname(){
        return accountname;
    }

    public String getAccounttype(){
        return accounttype;
    }

    public int getWhichtype(){
        return whichtype;
    }
}
