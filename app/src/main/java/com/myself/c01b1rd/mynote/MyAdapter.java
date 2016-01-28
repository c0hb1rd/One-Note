package com.myself.c01b1rd.mynote;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by c01b1rd on 1/18/16.
 */
public class MyAdapter extends BaseAdapter {

    List<Map<String, Object>> listItems;
    int resourceLayout;
    String [] from = null;
    int [] to;
    LayoutInflater inflater;

    public MyAdapter(Context context, List<Map<String, Object>> listItems, int resourceLayout, String [] from, int [] to) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listItems = listItems;
        this.resourceLayout = resourceLayout;
        this.from = from;
        this.to = to;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Map<String, Object> getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(resourceLayout, parent, false);
        for (int i = 0; i < from.length; i++) {
            ((TextView) view.findViewById(to[i])).setText(getItem(position).get(from[i]).toString());
        }

        TextView kindView = (TextView) view.findViewById(R.id.mShowType);
        String kind = getItem(position).get("type").toString();
        switch(kind) {
            case "Android":
                kindView.setBackgroundColor(Color.rgb(140, 180, 70));
                break;
            case "Other":
                kindView.setBackgroundColor(Color.rgb(40, 90, 150));
                kindView.setTextColor(Color.rgb(200, 200, 200));
                break;
            case "Word":
                kindView.setBackgroundColor(Color.rgb(204, 201, 201));
                break;
            case "Python2.x":
                kindView.setBackgroundColor(Color.rgb(220, 230, 130));
                break;
            case "Python3.x":
                kindView.setBackgroundColor(Color.rgb(240, 230, 100));
                break;
            case "Java":
                kindView.setBackgroundColor(Color.rgb(200, 100, 20));
                kindView.setTextColor(Color.rgb(200, 200, 200));
                break;
            case "C/C++":
                kindView.setBackgroundColor(Color.rgb(100, 100, 100));
                kindView.setTextColor(Color.rgb(200, 200, 200));
                break;
            case "Lua":
                kindView.setBackgroundColor(Color.rgb(180, 140, 130));
                kindView.setTextColor(Color.rgb(200, 200, 200));
                break;
            case "Security":
                kindView.setBackgroundColor(Color.rgb(50, 50, 50));
                kindView.setTextColor(Color.rgb(200, 200, 200));
                break;
            case "IDE":
                kindView.setBackgroundColor(Color.rgb(60, 150, 80));
                kindView.setTextColor(Color.rgb(200, 200, 200));
                break;
            case "Tool":
                kindView.setBackgroundColor(Color.rgb(150, 150, 150));
                kindView.setTextColor(Color.rgb(200, 200, 200));
                break;
            case "Website":
                kindView.setBackgroundColor(Color.rgb(100, 140, 130));
                kindView.setTextColor(Color.rgb(200, 200, 200));
                break;
            case "PHP":
                kindView.setBackgroundColor(Color.rgb(80, 100, 170));
                kindView.setTextColor(Color.rgb(200, 200, 200));
                break;
            case "Database":
                kindView.setBackgroundColor(Color.rgb(150, 230, 230));
                break;
            case "Network":
                kindView.setBackgroundColor(Color.rgb(200, 200, 255));
                break;
            case "Windows":
                kindView.setBackgroundColor(Color.rgb(71, 148, 180));
                kindView.setTextColor(Color.rgb(200, 200, 200));
                break;
            case "Javascript":
                kindView.setBackgroundColor(Color.rgb(200, 150, 100));
                kindView.setTextColor(Color.rgb(200, 200, 200));
                break;
            case "HTML5":
                kindView.setBackgroundColor(Color.rgb(210, 120, 70));
                kindView.setTextColor(Color.rgb(200, 200, 200));
                break;
            case "Shell":
                kindView.setBackgroundColor(Color.rgb(200, 200, 100));
                break;
            case "People":
                kindView.setBackgroundColor(Color.rgb(230, 200, 180));
                break;
            case "Linux":
                kindView.setBackgroundColor(Color.rgb(200, 90, 160));
                kindView.setTextColor(Color.rgb(200, 200, 200));
                break;
            case "Skill":
                kindView.setBackgroundColor(Color.rgb(200, 40, 40));
                kindView.setTextColor(Color.rgb(200, 200, 200));
                break;
        }

        return view;
    }
}
