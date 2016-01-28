package com.myself.c01b1rd.mynote;


import android.support.v7.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetailFragment extends Fragment {

    LayoutInflater mLaLayoutInflater;
    ListView detailView;
    EditText newContent;
    View v;
    ItemDatabase db;
    List<Map<String, String>> itemDetails;
    String id;
    String type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLaLayoutInflater = inflater;
        v = inflater.inflate(R.layout.item_detial_layout, container, false);

        initialization();
        refreshView(id);

        return v;
    }

    public void initialization() {
        detailView = (ListView) v.findViewById(R.id.item_detail_view);
        setDetailViewOnClickListener(detailView);
        db = new ItemDatabase(mLaLayoutInflater.getContext(), "itemDb.db3", null, 1);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder addDialog = new AlertDialog.Builder(mLaLayoutInflater.getContext());
                View v = mLaLayoutInflater.inflate(R.layout.add_new_detail, null, false);
                newContent = (EditText) v.findViewById(R.id.add_new_content);
                LinearLayout l = (LinearLayout) v.findViewById(R.id.add_new_detail_background);
                addDialog.setView(v);
                switch(type) {
                    case "Android":
                        l.setBackgroundColor(Color.rgb(140, 180, 70));
                        break;
                    case "Other":
                        l.setBackgroundColor(Color.rgb(40, 90, 150));
                        setContentColor();
                        break;
                    case "Word":
                        l.setBackgroundColor(Color.rgb(204, 201, 201));
                        break;
                    case "Python2.x":
                        l.setBackgroundColor(Color.rgb(220, 230, 130));
                        break;
                    case "Python3.x":
                        l.setBackgroundColor(Color.rgb(240, 230, 100));
                        break;
                    case "Java":
                        l.setBackgroundColor(Color.rgb(200, 100, 20));
                        setContentColor();
                        break;
                    case "C/C++":
                        l.setBackgroundColor(Color.rgb(100, 100, 100));
                        setContentColor();
                        break;
                    case "Lua":
                        l.setBackgroundColor(Color.rgb(180, 140, 130));
                        setContentColor();
                        break;
                    case "Security":
                        l.setBackgroundColor(Color.rgb(50, 50, 50));
                        setContentColor();
                        break;
                    case "IDE":
                        l.setBackgroundColor(Color.rgb(60, 150, 80));
                        setContentColor();
                        break;
                    case "Tool":
                        l.setBackgroundColor(Color.rgb(150, 150, 150));
                        setContentColor();
                        break;
                    case "Website":
                        l.setBackgroundColor(Color.rgb(100, 140, 130));
                        setContentColor();
                        break;
                    case "PHP":
                        l.setBackgroundColor(Color.rgb(80, 100, 170));
                        setContentColor();
                        break;
                    case "Database":
                        l.setBackgroundColor(Color.rgb(150, 230, 230));
                        break;
                    case "Network":
                        l.setBackgroundColor(Color.rgb(200, 200, 255));
                        break;
                    case "Windows":
                        l.setBackgroundColor(Color.rgb(71, 148, 180));
                        setContentColor();
                        break;
                    case "Javascript":
                        l.setBackgroundColor(Color.rgb(200, 150, 100));
                        setContentColor();
                        break;
                    case "HTML5":
                        l.setBackgroundColor(Color.rgb(210, 120, 70));
                        setContentColor();
                        break;
                    case "Shell":
                        l.setBackgroundColor(Color.rgb(200, 200, 100));
                        break;
                    case "People":
                        l.setBackgroundColor(Color.rgb(230, 200, 180));
                        break;
                    case "Linux":
                        l.setBackgroundColor(Color.rgb(200, 90, 160));
                        setContentColor();
                        break;
                    case "Skill":
                        l.setBackgroundColor(Color.rgb(200, 40, 40));
                        setContentColor();
                        break;
                }
                addDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addItemDetail(id, getNewItemId(), newContent.getText().toString());
                        refreshView(id);
                        detailView.setSelection(detailView.getCount());
                        Toast.makeText(mLaLayoutInflater.getContext(), "Add success.", Toast.LENGTH_SHORT).show();
                    }
                });
                addDialog.setNegativeButton("Cancel", null);
                addDialog.create().show();
            }
        });
    }

    public boolean isHaveContent() {
        Cursor cursor = db.getReadableDatabase().rawQuery("select content from Item_Detail_Form where id=?", new String [] {id});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        else{
            cursor.close();
            return false;
        }
    }

    public void refreshView(String id) {
        Cursor cursor = db.getReadableDatabase().rawQuery("select sub_id, content from Item_Detail_Form where id=?", new String[]{id});
        itemDetails = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String, String> item = new HashMap<>();
            item.put("sub_id", cursor.getString(0));
            item.put("content", cursor.getString(1));
            itemDetails.add(item);
        }
        cursor.close();
        BaseAdapter adapter;
        if (isHaveContent()) {
            adapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return itemDetails.size();
                }

                @Override
                public Map<String, String> getItem(int i) {
                    return itemDetails.get(i);
                }

                @Override
                public long getItemId(int i) {
                    return i;
                }

                @Override
                public View getView(int i, View view, ViewGroup viewGroup) {

                    View itemVIew = mLaLayoutInflater.inflate(R.layout.detail_layout, viewGroup, false);
                    if (i+1 < 10) {
                        ((TextView) itemVIew.findViewById(R.id.showItemDetail1)).setText("0" + (i + 1) + ". ");
                    } else {
                        ((TextView) itemVIew.findViewById(R.id.showItemDetail1)).setText((i + 1) + ". ");
                    }
                    ((TextView) itemVIew.findViewById(R.id.showItemDetail2)).setText(getItem(i).get("content"));
                    return itemVIew;
                }
            } ;
        } else {
            adapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return 1;
                }

                @Override
                public Map<String, String> getItem(int i) {
                    return itemDetails.get(i);
                }

                @Override
                public long getItemId(int i) {
                    return i;
                }

                @Override
                public View getView(int i, View view, ViewGroup viewGroup) {

                    View itemVIew = mLaLayoutInflater.inflate(R.layout.not_detail_layout, viewGroup, false);
                    ((TextView) itemVIew.findViewById(R.id.notItemDetail)).setText("Null");

                    return itemVIew;
                }
            };
        }

        detailView.setAdapter(adapter);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDetailViewOnClickListener(ListView view) {
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                String [] items = {"Modify", "Delete", "Cancel"};
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                AlertDialog.Builder modifyDialog = new AlertDialog.Builder(mLaLayoutInflater.getContext());
                                View view1 = mLaLayoutInflater.inflate(R.layout.add_new_detail, null, false);
                                ((TextView) view1.findViewById(R.id.dialog_title)).setText("Modify");
                                newContent = (EditText) view1.findViewById(R.id.add_new_content);
                                newContent.setText(itemDetails.get(position).get("content"));
                                LinearLayout l = (LinearLayout) view1.findViewById(R.id.add_new_detail_background);
                                modifyDialog.setView(view1);
                                switch(type) {
                                    case "Android":
                                        l.setBackgroundColor(Color.rgb(140, 180, 70));
                                        break;
                                    case "Other":
                                        l.setBackgroundColor(Color.rgb(40, 90, 150));
                                        setContentColor();
                                        break;
                                    case "Word":
                                        l.setBackgroundColor(Color.rgb(204, 201, 201));
                                        break;
                                    case "Python2.x":
                                        l.setBackgroundColor(Color.rgb(220, 230, 130));
                                        break;
                                    case "Python3.x":
                                        l.setBackgroundColor(Color.rgb(240, 230, 100));
                                        break;
                                    case "Java":
                                        l.setBackgroundColor(Color.rgb(200, 100, 20));
                                        setContentColor();
                                        break;
                                    case "C/C++":
                                        l.setBackgroundColor(Color.rgb(100, 100, 100));
                                        setContentColor();
                                        break;
                                    case "Lua":
                                        l.setBackgroundColor(Color.rgb(180, 140, 130));
                                        setContentColor();
                                        break;
                                    case "Security":
                                        l.setBackgroundColor(Color.rgb(50, 50, 50));
                                        setContentColor();
                                        break;
                                    case "IDE":
                                        l.setBackgroundColor(Color.rgb(60, 150, 80));
                                        setContentColor();
                                        break;
                                    case "Tool":
                                        l.setBackgroundColor(Color.rgb(150, 150, 150));
                                        setContentColor();
                                        break;
                                    case "Website":
                                        l.setBackgroundColor(Color.rgb(100, 140, 130));
                                        setContentColor();
                                        break;
                                    case "PHP":
                                        l.setBackgroundColor(Color.rgb(80, 100, 170));
                                        setContentColor();
                                        break;
                                    case "Database":
                                        l.setBackgroundColor(Color.rgb(150, 230, 230));
                                        break;
                                    case "Network":
                                        l.setBackgroundColor(Color.rgb(200, 200, 255));
                                        break;
                                    case "Windows":
                                        l.setBackgroundColor(Color.rgb(71, 148, 180));
                                        setContentColor();
                                        break;
                                    case "Javascript":
                                        l.setBackgroundColor(Color.rgb(200, 150, 100));
                                        setContentColor();
                                        break;
                                    case "HTML5":
                                        l.setBackgroundColor(Color.rgb(210, 120, 70));
                                        setContentColor();
                                        break;
                                    case "Shell":
                                        l.setBackgroundColor(Color.rgb(200, 200, 100));
                                        break;
                                    case "People":
                                        l.setBackgroundColor(Color.rgb(230, 200, 180));
                                        break;
                                    case "Linux":
                                        l.setBackgroundColor(Color.rgb(200, 90, 160));
                                        setContentColor();
                                        break;
                                    case "Skill":
                                        l.setBackgroundColor(Color.rgb(200, 40, 40));
                                        setContentColor();
                                        break;
                                }
                                modifyDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        updateItemDetail(id, itemDetails.get(position).get("sub_id"), newContent.getText().toString());
                                        refreshView(id);
                                        Toast.makeText(mLaLayoutInflater.getContext(), "Modification success.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                modifyDialog.setNegativeButton("Cancel", null);
                                modifyDialog.create().show();
                                break;
                            case 1:
                                final AlertDialog.Builder delDialog = new AlertDialog.Builder(mLaLayoutInflater.getContext());
                                View view2 = mLaLayoutInflater.inflate(R.layout.delete_item_layout, null, false);
                                String msg = "Delete \"" + getItemObject(position, "content") + "\" ? ";
                                ((TextView) view2.findViewById(R.id.delete_item_msg)).setText(msg);
                                delDialog.setView(view2);
                                delDialog.setView(view2);
                                delDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String content = getItemObject(position, "content");
                                        deleteItemDetail(content);
                                        refreshView(id);
                                        detailView.setSelection(position);
                                        Toast.makeText(mLaLayoutInflater.getContext(), "Delete success.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                delDialog.setNegativeButton("No", null);
                                delDialog.create().show();
                                break;
                            case 2:
                                break;
                        }
                    }
                });
                dialog.create().show();
                return false;
            }
        });
    }

    public void addItemDetail(String id, String sub_id, String content) {
        db.getReadableDatabase().execSQL("insert into Item_Detail_Form values(?, ?, ?)", new String [] {id, sub_id, content});
    }

    public void updateItemDetail(String id, String sub_id, String content) {
        db.getReadableDatabase().execSQL("update Item_Detail_Form set content=? where id=? and sub_id=?", new String [] {content, id, sub_id});
    }

    public void deleteItemDetail(String content) {
        db.getReadableDatabase().execSQL("delete from Item_Detail_Form where id=? and content=?", new String [] {this.id, content});
    }

    public void setContentColor() {
        int rgb = 200;
        newContent.setTextColor(Color.rgb(rgb, rgb, rgb));
    }

    public void setType(String type) {
        this.type = type;
    }

    private String getItemObject(int position, String object) {
        return itemDetails.get(position).get(object);
    }

    public String getNewItemId() {
        Calendar calendar = Calendar.getInstance();
        String hour = calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : calendar.get(Calendar.HOUR_OF_DAY) + "";
        String minute = calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE) + "";
        String year = calendar.get(Calendar.YEAR) + "";
        String month = (calendar.get(Calendar.MONTH ) + 1) + "";
        String second = calendar.get(Calendar.MILLISECOND) + "";
        String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH) + "";
        return year + month + day + hour + minute + second;
    }

    public void showSearchResult(String content) {
        itemDetails = new ArrayList<>();
        Cursor cursor = db.getReadableDatabase().rawQuery("select sub_id, content from Item_Detail_Form where id=? and content=?", new String [] {id, content});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Map<String, String> item = new HashMap<>();
                item.put("sub_id", cursor.getString(0));
                item.put("content", cursor.getString(1));
                itemDetails.add(item);
            }
            BaseAdapter adapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return itemDetails.size();
                }

                @Override
                public Map<String, String> getItem(int i) {
                    return itemDetails.get(i);
                }

                @Override
                public long getItemId(int i) {
                    return i;
                }

                @Override
                public View getView(int i, View view, ViewGroup viewGroup) {

                    View itemVIew = mLaLayoutInflater.inflate(R.layout.detail_layout, viewGroup, false);
                    if (i + 1 < 10) {
                        ((TextView) itemVIew.findViewById(R.id.showItemDetail1)).setText("0" + (i + 1) + ". ");
                    } else {
                        ((TextView) itemVIew.findViewById(R.id.showItemDetail1)).setText((i + 1) + ". ");
                    }
                    ((TextView) itemVIew.findViewById(R.id.showItemDetail2)).setText(getItem(i).get("content"));
                    return itemVIew;
                }
            };
            detailView.setAdapter(adapter);
        } else {
            Toast.makeText(mLaLayoutInflater.getContext(), "Not complete data.", Toast.LENGTH_SHORT).show();
        }

    }
}
