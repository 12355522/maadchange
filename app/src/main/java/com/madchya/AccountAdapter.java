package com.madchya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by steven on 2015/2/15.
 */
public  class AccountAdapter extends BaseAdapter {
    LayoutInflater myInflater ;
    List<AccountContent> myAcCotent ;

    public AccountAdapter(Context context,List<AccountContent> myAcCotent){
        myInflater = LayoutInflater.from(context);
        this.myAcCotent = myAcCotent;
    }

    private class ViewHolder{
        TextView arg0,arg1;
        public ViewHolder(TextView arg0 ,TextView arg1){
            this.arg0 = arg0;
            this.arg1 = arg1;
        }
    }

    @Override
    public int getCount() {
        return myAcCotent.size();
    }

    @Override
    public Object getItem(int arg0) {
        return myAcCotent.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return myAcCotent.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = myInflater.inflate(R.layout.listview_define, null);
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.accountname),
                    (TextView) convertView.findViewById(R.id.logintype)
            );
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        AccountContent movie = (AccountContent)getItem(position);
        holder.arg0.setText(movie.getAccountName());
        holder.arg1.setText(movie.getAccountLogin());
        return convertView;
    }
}
