package com.mtest;

import java.util.ArrayList;
import java.util.TreeSet;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

class CustomAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<Itm> mData = new ArrayList<Itm>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public void flush(){
        mData.clear();
        sectionHeader.clear();
    }

    public CustomAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(Todo todo) {
        mData.add(new Itm(todo));
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(new Itm(item));
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position).getText();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.snippet_item1, null);
                    holder.textView = (CheckBox) convertView.findViewById(R.id.text);
                    Todo current = mData.get(position).getTodo();
                    holder.textView.setTag(current);
                    if (current.getComplete()){
                        holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    ((CheckBox) holder.textView).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Todo todoInFocus = (Todo) buttonView.getTag();
                            if (todoInFocus.getComplete() == isChecked) return;
                            todoInFocus.setComplete(isChecked);
                            if (isChecked){
                                buttonView.setPaintFlags(buttonView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            }else {
                                buttonView.setPaintFlags(buttonView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            }
                        }
                    });
                    ((CheckBox) holder.textView).setChecked(current.getComplete());
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.snippet_item2, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mData.get(position).getText());

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }

    private class Itm{
        String text;
        Todo todo;

        Itm(Todo todo){
            this.text = todo.getText();
            this. todo = todo;
        }

        Itm(String text){
            this.text = text;
            todo = null;
        }

        String getText(){
            return text;
        }

        Todo getTodo(){
            return todo;
        }
    }

}
