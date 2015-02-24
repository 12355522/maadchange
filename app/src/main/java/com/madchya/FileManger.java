package com.madchya;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class FileManger extends Activity implements OnClickListener,
        OnItemClickListener {

    private TextView mTextView;
    private ListView mListView;
    private ArrayList<File> fileList;
    private MyAdapter adapter;
    private File currFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.filemanger_activity);
        this.mTextView = (TextView) this.findViewById(R.id.textView1);
        this.mListView = (ListView) this.findViewById(R.id.listView1);
        this.findViewById(R.id.fileok).setOnClickListener(this);
        this.findViewById(R.id.filecencle).setOnClickListener(this);
        this.findViewById(R.id.fileback).setOnClickListener(this);
        this.findViewById(R.id.filenew).setOnClickListener(this);
        this.mListView.setOnItemClickListener(this);
        this.fileList = new ArrayList<File>();
        this.adapter = new MyAdapter(this, fileList);
        this.mListView.setAdapter(adapter);

        this.currFile = new File("/sdcard");// 設置 初始 目錄
        this.refreshView();
    }

    private void refreshView() {
        this.fileList.clear();
        File[] fl = currFile.listFiles();
        if (fl != null)
            for (File f : currFile.listFiles()) {
                if (f.isDirectory())// 是目錄才加入，也就是過濾掉不是目錄的文件
                    this.fileList.add(f);
            }
        this.adapter.notifyDataSetChanged();
        this.mTextView.setText(currFile.getPath());
    }

    @Override
    public void onItemClick(AdapterView<?> pa, View v, int po, long id) {
        this.currFile = fileList.get(po);
        this.refreshView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fileback:
                File parent = this.currFile.getParentFile();
                if (parent == null)
                    return;
                this.currFile = parent;
                this.refreshView();
                break;
            case R.id.filenew:
                AlertDialog.Builder dialog = new Builder(this);
                dialog.setMessage("新建文件夾");
                final EditText et = new EditText(this);
                dialog.setView(et);
                dialog.setPositiveButton("Cancle", null);
                dialog.setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File f = new File(currFile, et.getText().toString());
                                if (f.mkdir()) {
                                    refreshView();
                                    return;
                                }
                                Toast.makeText(getApplicationContext(), "文件夾創建失敗",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog.show();
                break;
            case R.id.fileok:
                Toast.makeText(this, "你選擇了文件夾：" + currFile.getPath(),
                        Toast.LENGTH_LONG).show();
                        this.finish();
                break;
            case R.id.filecencle:
                this.finish();
                break;
        }
    }
    static class MyAdapter extends BaseAdapter {
        private ArrayList<File> fileList;
        private LayoutInflater inflater;
        public MyAdapter(Context context, ArrayList<File> fileList) {
            this.fileList = fileList;
            this.inflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return this.fileList.size();
        }
        @Override
        public Object getItem(int po) {
            return this.fileList.get(po);
        }
        @Override
        public long getItemId(int po) {
            return po;
        }
        @Override
        public View getView(int po, View v, ViewGroup pa) {
            TextView tv = null;
            if (v == null) {
                v = this.inflater.inflate(R.layout.item_layout, pa, false);
                tv = (TextView) v.findViewById(R.id.textView1);
                v.setTag(tv);
            } else {
                tv = (TextView) v.getTag();
            }
            tv.setText(fileList.get(po).getName());
            return v;
        }
    }
}
