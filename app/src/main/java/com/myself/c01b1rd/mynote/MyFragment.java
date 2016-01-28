package com.myself.c01b1rd.mynote;


import android.support.v7.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyFragment extends Fragment {

    int viewMode = 0;
    TextView showItemTitle, showItemType, showItemDescription, modifyItemTypeText;
    EditText modifyTitle, modifyDescription;
    AutoCompleteTextView modifyType;
    ItemDatabase db;
    LayoutInflater mLayoutInflater;
    GridView itemView;
    List<Map<String, Object>> viewItems;
    View view;
    String [] autoCompleteData = {"Android", "HTML5", "Python2.x", "Python3.x", "Java", "PHP",
            "C/C++", "Lua", "Website", "Tool", "Security", "Skill",
            "Other", "Javascript", "Linux", "Windows", "People", "Shell",
            "Network", "Database", "IDE", "Word"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        view = inflater.inflate(R.layout.framgment_main, container, false);

        initialization();
        setViewItemsClickListener(itemView);
        refreshView();

        return view;
    }

    public void initialization() {

        if (db == null) {
            db = new ItemDatabase(mLayoutInflater.getContext(), "itemDb.db3", null, 1);
        }
        if (checkAppMode()) {
            db.getReadableDatabase().execSQL("insert into App_Mode values(?, ?)", new String [] {"viewMode", "2"});
            Cursor cursor = db.getReadableDatabase().rawQuery("select value from App_Mode where id=?", new String [] {"viewMode"});
            cursor.moveToNext();
            this.viewMode = Integer.valueOf(cursor.getColumnName(0));
            cursor.close();
        } else {
            Cursor cursor = db.getReadableDatabase().rawQuery("select value from App_Mode where id=?", new String [] {"viewMode"});
            cursor.moveToNext();
            this.viewMode = Integer.parseInt(cursor.getString(0));
            cursor.close();
        }
        itemView = (GridView) view.findViewById(R.id.item_view);
        itemView.setNumColumns(viewMode);

        FloatingActionButton fab1 = (FloatingActionButton) getActivity().findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton) getActivity().findViewById(R.id.fab2);

        fab1.show();
        fab2.hide();

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder addDialog = new AlertDialog.Builder(mLayoutInflater.getContext());
                View view = mLayoutInflater.inflate(R.layout.add_new_item, null, false);
                final EditText editTitle = (EditText) view.findViewById(R.id.editTitle);
                final EditText editDescription = (EditText) view.findViewById(R.id.editDescription);
                final AutoCompleteTextView editType = (AutoCompleteTextView) view.findViewById(R.id.editType);
                ArrayAdapter adapter = new ArrayAdapter(mLayoutInflater.getContext(), android.R.layout.simple_list_item_1, autoCompleteData);
                editType.setAdapter(adapter);
                addDialog.setView(view);
                addDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newItemTitle = editTitle.getText().toString();
                        String newItemType = editType.getText().toString();
                        String newItemDescription = editDescription.getText().toString();
                        if (newItemType.equals("") || newItemType.equals(" ")) {
                            Toast.makeText(mLayoutInflater.getContext(), "Item type Do Not Be Null", Toast.LENGTH_SHORT).show();
                        } else if (newItemTitle.equals("") || newItemTitle.equals(" ")) {
                            Toast.makeText(mLayoutInflater.getContext(), "Item Title Do Not Be Null", Toast.LENGTH_SHORT).show();
                        } else if (newItemDescription.equals("") || newItemDescription.equals(" ")) {
                            Toast.makeText(mLayoutInflater.getContext(), "Item Description Do Not Be Null", Toast.LENGTH_SHORT).show();
                        } else {
                            newItemTitle = editTitle.getText().toString().substring(0, 1).toUpperCase() + editTitle.getText().toString().substring(1);
                            newItemType = editType.getText().toString().substring(0, 1).toUpperCase() + editType.getText().toString().substring(1);
                            newItemDescription = editDescription.getText().toString().substring(0, 1).toUpperCase() + editDescription.getText().toString().substring(1);
                            String id = getNewItemId();
                            addNewItem(id, newItemType, newItemTitle, getCurrentTime(), newItemDescription);
                            refreshView();
                            itemView.setSelection(itemView.getCount());
                            Toast.makeText(mLayoutInflater.getContext(), "Add success.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                addDialog.setNegativeButton("Cancel", null);
                addDialog.create().show();
            }
        });
    }

    public void refreshView() {
        viewItems = new ArrayList<>();
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from Item_Form", null);
        while (cursor.moveToNext()) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", cursor.getString(0));
            item.put("type", cursor.getString(1));
            item.put("title", cursor.getString(2));
            item.put("date", cursor.getString(3));
            item.put("description", cursor.getString(4));
            viewItems.add(item);
        }
        cursor.close();
        String [] from = {"title", "description", "type"};
        int [] to = {R.id.mShowTitle, R.id.mShowDescription, R.id.mShowType};
        MyAdapter adapter = new MyAdapter(mLayoutInflater.getContext(), viewItems, R.layout.item_layout, from, to);
        itemView.setAdapter(adapter);
    }

    public void refreshViewAfterSort() {
        viewItems = new ArrayList<>();
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from Item_Form order by type", null);
        while (cursor.moveToNext()) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", cursor.getString(0));
            item.put("type", cursor.getString(1));
            item.put("title", cursor.getString(2));
            item.put("date", cursor.getString(3));
            item.put("description", cursor.getString(4));
            viewItems.add(item);
        }
        cursor.close();
        String [] from = {"title", "description", "type"};
        int [] to = {R.id.mShowTitle, R.id.mShowDescription, R.id.mShowType};
        MyAdapter adapter = new MyAdapter(mLayoutInflater.getContext(), viewItems, R.layout.item_layout, from, to);
        itemView.setAdapter(adapter);
    }

    public void setViewItemsClickListener(GridView view) {
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                AlertDialog.Builder detailDialog = new AlertDialog.Builder(mLayoutInflater.getContext());
                View v = mLayoutInflater.inflate(R.layout.detail_item_layout, null, false);
                showItemType = (TextView) v.findViewById(R.id.showItemType);
                showItemType.setText(getItemObject(position, "type"));
                showItemTitle = (TextView) v.findViewById(R.id.showItemTitle);
                showItemTitle.setText(getItemObject(position, "title"));
                showItemDescription = (TextView) v.findViewById(R.id.showItemDescription);
                showItemDescription.setText(getItemObject(position, "description"));
                LinearLayout l = (LinearLayout) v.findViewById(R.id.detailDialogBackground);
                switch(showItemType.getText().toString()) {
                    case "Android":
                        l.setBackgroundColor(Color.rgb(140, 180, 70));
                        break;
                    case "Other":
                        l.setBackgroundColor(Color.rgb(40, 90, 150));
                        setDetailColor();
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
                        setDetailColor();
                        break;
                    case "C/C++":
                        l.setBackgroundColor(Color.rgb(100, 100, 100));
                        setDetailColor();
                        break;
                    case "Lua":
                        l.setBackgroundColor(Color.rgb(180, 140, 130));
                        setDetailColor();
                        break;
                    case "Security":
                        l.setBackgroundColor(Color.rgb(50, 50, 50));
                        setDetailColor();
                        break;
                    case "IDE":
                        l.setBackgroundColor(Color.rgb(60, 150, 80));
                        setDetailColor();
                        break;
                    case "Tool":
                        l.setBackgroundColor(Color.rgb(150, 150, 150));
                        setDetailColor();
                        break;
                    case "Website":
                        l.setBackgroundColor(Color.rgb(100, 140, 130));
                        setDetailColor();
                        break;
                    case "PHP":
                        l.setBackgroundColor(Color.rgb(80, 100, 170));
                        setDetailColor();
                        break;
                    case "Database":
                        l.setBackgroundColor(Color.rgb(150, 230, 230));
                        break;
                    case "Network":
                        l.setBackgroundColor(Color.rgb(200, 200, 255));
                        break;
                    case "Windows":
                        l.setBackgroundColor(Color.rgb(71, 148, 180));
                        setDetailColor();
                        break;
                    case "Javascript":
                        l.setBackgroundColor(Color.rgb(200, 150, 100));
                        setDetailColor();
                        break;
                    case "HTML5":
                        l.setBackgroundColor(Color.rgb(210, 120, 70));
                        setDetailColor();
                        break;
                    case "Shell":
                        l.setBackgroundColor(Color.rgb(200, 200, 100));
                        break;
                    case "People":
                        l.setBackgroundColor(Color.rgb(230, 200, 180));
                        break;
                    case "Linux":
                        l.setBackgroundColor(Color.rgb(200, 90, 160));
                        setDetailColor();
                        break;
                    case "Skill":
                        l.setBackgroundColor(Color.rgb(200, 40, 40));
                        setDetailColor();
                        break;
                }
                detailDialog.setView(v);
                detailDialog.setPositiveButton("More", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
                        Bundle data = new Bundle();
                        data.putString("id", getItemObject(position, "id"));
                        data.putString("title", getItemObject(position, "title"));
                        data.putString("type", getItemObject(position, "type"));
                        data.putString("date", getItemObject(position, "date"));
                        data.putString("description", getItemObject(position, "description"));
                        intent.putExtras(data);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.hold, R.anim.fade_in);
                    }
                });
                detailDialog.setNegativeButton("Modify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder modifyDialog = new AlertDialog.Builder(mLayoutInflater.getContext());
                        View v = mLayoutInflater.inflate(R.layout.modify_item_layout, null, false);
                        LinearLayout l = (LinearLayout) v.findViewById(R.id.modifyItemBackground);
                        modifyItemTypeText = (TextView) v.findViewById(R.id.modifyItemTypeText);
                        modifyTitle = (EditText) v.findViewById(R.id.modifyItemTitle);
                        modifyDescription = (EditText) v.findViewById(R.id.modifyItemDescription);
                        modifyDescription.requestFocus();
                        modifyType = (AutoCompleteTextView) v.findViewById(R.id.modifyItemType);
                        ArrayAdapter adapter = new ArrayAdapter(mLayoutInflater.getContext(), android.R.layout.simple_list_item_1, autoCompleteData);
                        modifyType.setAdapter(adapter);
                        modifyDescription.setText(getItemObject(position, "description"));
                        modifyTitle.setText(getItemObject(position, "title"));
                        modifyType.setText(getItemObject(position, "type"));
                        switch(modifyType.getText().toString()) {
                            case "Android":
                                l.setBackgroundColor(Color.rgb(140, 180, 70));
                                break;
                            case "Other":
                                l.setBackgroundColor(Color.rgb(40, 90, 150));
                                setModifyColor();
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
                                setModifyColor();
                                break;
                            case "C/C++":
                                l.setBackgroundColor(Color.rgb(100, 100, 100));
                                setModifyColor();
                                break;
                            case "Lua":
                                l.setBackgroundColor(Color.rgb(180, 140, 130));
                                setModifyColor();
                                break;
                            case "Security":
                                l.setBackgroundColor(Color.rgb(50, 50, 50));
                                setModifyColor();
                                break;
                            case "IDE":
                                l.setBackgroundColor(Color.rgb(60, 150, 80));
                                setModifyColor();
                                break;
                            case "Tool":
                                l.setBackgroundColor(Color.rgb(150, 150, 150));
                                setModifyColor();
                                break;
                            case "Website":
                                l.setBackgroundColor(Color.rgb(100, 140, 130));
                                setModifyColor();
                                break;
                            case "PHP":
                                l.setBackgroundColor(Color.rgb(80, 100, 170));
                                setModifyColor();
                                break;
                            case "Database":
                                l.setBackgroundColor(Color.rgb(150, 230, 230));
                                break;
                            case "Network":
                                l.setBackgroundColor(Color.rgb(200, 200, 255));
                                break;
                            case "Windows":
                                l.setBackgroundColor(Color.rgb(71, 148, 180));
                                setModifyColor();
                                break;
                            case "Javascript":
                                l.setBackgroundColor(Color.rgb(200, 150, 100));
                                setModifyColor();
                                break;
                            case "HTML5":
                                l.setBackgroundColor(Color.rgb(210, 120, 70));
                                setModifyColor();
                                break;
                            case "Shell":
                                l.setBackgroundColor(Color.rgb(200, 200, 100));
                                break;
                            case "People":
                                l.setBackgroundColor(Color.rgb(230, 200, 180));
                                break;
                            case "Linux":
                                l.setBackgroundColor(Color.rgb(200, 90, 160));
                                setModifyColor();
                                break;
                            case "Skill":
                                l.setBackgroundColor(Color.rgb(200, 40, 40));
                                setModifyColor();
                                break;
                        }
                        final String itemId = getItemObject(position, "id");
                        modifyDialog.setView(v);
                        modifyDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String newItemTitle = modifyTitle.getText().toString();
                                String newItemType = modifyType.getText().toString();
                                String newItemDescription = modifyDescription.getText().toString();
                                if (newItemType.equals("") || newItemType.equals(" ")) {
                                    Toast.makeText(mLayoutInflater.getContext(), "Item type Do Not Be Null", Toast.LENGTH_SHORT).show();
                                } else if (newItemTitle.equals("") || newItemTitle.equals(" ")) {
                                    Toast.makeText(mLayoutInflater.getContext(), "Item Title Do Not Be Null", Toast.LENGTH_SHORT).show();
                                } else if (newItemDescription.equals("") || newItemDescription.equals(" ")) {
                                    Toast.makeText(mLayoutInflater.getContext(), "Item Description Do Not Be Null", Toast.LENGTH_SHORT).show();
                                } else {
                                    updateItem(itemId, newItemType, newItemTitle, newItemDescription);
                                    refreshView();
                                    itemView.setSelection(position);
                                    Toast.makeText(mLayoutInflater.getContext(), "Modification success.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        modifyDialog.setNegativeButton("Cancel", null);
                        modifyDialog.create().show();
                    }
                });
                detailDialog.create().show();

            }
        });

        view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder delDialog = new AlertDialog.Builder(mLayoutInflater.getContext());
                View v = mLayoutInflater.inflate(R.layout.delete_item_layout, null, false);
                String msg = "Are you sure to delete \"" + getItemObject(position, "title") + "\" ? ";
                ((TextView) v.findViewById(R.id.delete_item_msg)).setText(msg);
                delDialog.setView(v);
                delDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String itemId = getItemObject(position, "id");
                        deleteItem(itemId);
                        refreshView();
                        itemView.setSelection(position);
                        Toast.makeText(mLayoutInflater.getContext(), "Delete success.", Toast.LENGTH_SHORT).show();
                    }
                });

                delDialog.setNegativeButton("No", null);
                delDialog.create().show();

                return false;
            }
        });

        view.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public List<String> getItemsType() {
        List<String> types = new ArrayList<>();
        Cursor cursor = db.getReadableDatabase().rawQuery("select type from Item_Form group by type order by type", null);
        while (cursor.moveToNext()) {
            types.add(cursor.getString(0));
        }
        cursor.close();
        return types;
    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR) + "";
        String month = (calendar.get(Calendar.MONTH ) + 1) + "";
        String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH) + "";
        return year + "-" + month + "-" + day + " " + (calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : calendar.get(Calendar.HOUR_OF_DAY)) + ":" + (calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE));
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

    public void addNewItem(String id, String type, String title, String date, String description) {
        String [] data = {id, type, title, date, description};
        db.getReadableDatabase().execSQL("insert into Item_Form values(?, ?, ?, ?, ?)", data);
    }

    public void updateItem(String id, String type, String title, String description) {
        String [] data = {type, title, description, id};
        db.getReadableDatabase().execSQL("update Item_Form set type=?, title=?, description=? where id=? ", data);
    }

    public void deleteItem(String id) {
        db.getReadableDatabase().execSQL("delete from Item_Form where id=?", new String [] {id});
        db.getReadableDatabase().execSQL("delete from Item_Detail_Form where id=?", new String [] {id});
    }

    private String getItemObject(int position, String object) {
        return viewItems.get(position).get(object).toString();
    }

    public void setDetailColor() {
        int rgb = 200;
        showItemDescription.setTextColor(Color.rgb(rgb, rgb, rgb));
        showItemType.setTextColor(Color.rgb(rgb, rgb, rgb));
        showItemTitle.setTextColor(Color.rgb(rgb, rgb, rgb));
    }

    public void setModifyColor() {
        int rgb = 200;
        modifyItemTypeText.setTextColor(Color.rgb(rgb, rgb, rgb));
        modifyTitle.setTextColor(Color.rgb(rgb, rgb, rgb));
        modifyDescription.setTextColor(Color.rgb(rgb, rgb, rgb));
        modifyType.setTextColor(Color.rgb(rgb, rgb, rgb));
    }

    public boolean checkAppMode() {
        if (db.getReadableDatabase().rawQuery("select value from App_Mode where id=?", new String [] {"viewMode"}).getCount() > 0)
            return false;
        else
            return true;
    }

    public void changeViewMode(int i) {
        if (db == null) {
            db = new ItemDatabase(mLayoutInflater.getContext(), "itemDb.db3", null, 1);
        }
        db.getReadableDatabase().execSQL("update App_Mode set value=? where id=?", new String [] {i + "", "viewMode"});
        this.viewMode = i;
    }

    public void showSearchResultByTitle(String result) {
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from Item_Form where title=?", new String [] {result});
        if (cursor.getCount() > 0) {
            viewItems = new ArrayList<>();
            while (cursor.moveToNext()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", cursor.getString(0));
                item.put("type", cursor.getString(1));
                item.put("title", cursor.getString(2));
                item.put("date", cursor.getString(3));
                item.put("description", cursor.getString(4));
                viewItems.add(item);
            }
            cursor.close();
            String [] from = {"title", "description", "type"};
            int [] to = {R.id.mShowTitle, R.id.mShowDescription, R.id.mShowType};
            MyAdapter adapter = new MyAdapter(mLayoutInflater.getContext(), viewItems, R.layout.item_layout, from, to);
            itemView.setAdapter(adapter);
        } else {
            Toast.makeText(mLayoutInflater.getContext(), "Not complete data.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showSearchResultByDescription(String result) {
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from Item_Form where description=?", new String [] {result});
        if (cursor.getCount() > 0) {
            viewItems = new ArrayList<>();
            while (cursor.moveToNext()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", cursor.getString(0));
                item.put("type", cursor.getString(1));
                item.put("title", cursor.getString(2));
                item.put("date", cursor.getString(3));
                item.put("description", cursor.getString(4));
                viewItems.add(item);
            }
            cursor.close();
            String [] from = {"title", "description", "type"};
            int [] to = {R.id.mShowTitle, R.id.mShowDescription, R.id.mShowType};
            MyAdapter adapter = new MyAdapter(mLayoutInflater.getContext(), viewItems, R.layout.item_layout, from, to);
            itemView.setAdapter(adapter);
        } else {
            Toast.makeText(mLayoutInflater.getContext(), "Not complete data.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showSearchResultByType(String result) {
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from Item_Form where type=?", new String [] {result});
        if (cursor.getCount() > 0) {
            viewItems = new ArrayList<>();
            while (cursor.moveToNext()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", cursor.getString(0));
                item.put("type", cursor.getString(1));
                item.put("title", cursor.getString(2));
                item.put("date", cursor.getString(3));
                item.put("description", cursor.getString(4));
                viewItems.add(item);
            }
            cursor.close();
            String [] from = {"title", "description", "type"};
            int [] to = {R.id.mShowTitle, R.id.mShowDescription, R.id.mShowType};
            MyAdapter adapter = new MyAdapter(mLayoutInflater.getContext(), viewItems, R.layout.item_layout, from, to);
            itemView.setAdapter(adapter);
        } else {
            Toast.makeText(mLayoutInflater.getContext(), "Not complete data.", Toast.LENGTH_SHORT).show();
        }
    }

}
