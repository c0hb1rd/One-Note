package com.myself.c01b1rd.mynote;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ItemDetailActivity extends AppCompatActivity {

    DetailFragment fragment;
    TextView titleView, dateView, typeView, descriptionView;
    String id, title, date, type, description;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Detail");
        setContentView(R.layout.item_detial_activity);

        initialization();
        try {
            id = getIntent().getExtras().get("id").toString();
            title = getIntent().getExtras().get("title").toString();
            date = "Date: " + getIntent().getExtras().get("date").toString();
            type = "Type: " + getIntent().getExtras().get("type").toString();
            description = "Description: " + getIntent().getExtras().get("description").toString();
            ((TextView) findViewById(R.id.showDetailTitle)).setText(title);
            ((TextView) findViewById(R.id.showDetailDescription)).setText(description);
            ((TextView) findViewById(R.id.showDetailDate)).setText(date);
            ((TextView) findViewById(R.id.showDetailType)).setText(type);
            fragment = new DetailFragment();
            fragment.setId(id);
            fragment.setType(getIntent().getExtras().get("type").toString());
            getFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.itemDetailBackground);
            String kind = getIntent().getExtras().get("type").toString();
            switch(kind) {
                case "Android":
                    linearLayout.setBackgroundColor(Color.rgb(140, 180, 70));
                    setColor();
                    break;
                case "Other":
                    linearLayout.setBackgroundColor(Color.rgb(40, 90, 150));
                    break;
                case "Word":
                    linearLayout.setBackgroundColor(Color.rgb(204, 201, 201));
                    setColor();
                    break;
                case "Python2.x":
                    linearLayout.setBackgroundColor(Color.rgb(220, 230, 130));
                    setColor();
                    break;
                case "Python3.x":
                    linearLayout.setBackgroundColor(Color.rgb(240, 230, 100));
                    setColor();
                    break;
                case "Java":
                    linearLayout.setBackgroundColor(Color.rgb(200, 100, 20));
                    break;
                case "C/C++":
                    linearLayout.setBackgroundColor(Color.rgb(100, 100, 100));
                    break;
                case "Lua":
                    linearLayout.setBackgroundColor(Color.rgb(180, 140, 130));
                    break;
                case "Security":
                    linearLayout.setBackgroundColor(Color.rgb(50, 50, 50));
                    break;
                case "IDE":
                    linearLayout.setBackgroundColor(Color.rgb(60, 150, 80));
                    break;
                case "Tool":
                    linearLayout.setBackgroundColor(Color.rgb(150, 150, 150));
                    break;
                case "Website":
                    linearLayout.setBackgroundColor(Color.rgb(100, 140, 130));
                    break;
                case "PHP":
                    linearLayout.setBackgroundColor(Color.rgb(80, 100, 170));
                    break;
                case "Database":
                    linearLayout.setBackgroundColor(Color.rgb(150, 230, 230));
                    setColor();
                    break;
                case "Network":
                    linearLayout.setBackgroundColor(Color.rgb(200, 200, 255));
                    setColor();
                    break;
                case "Windows":
                    linearLayout.setBackgroundColor(Color.rgb(71, 148, 180));
                    break;
                case "Javascript":
                    linearLayout.setBackgroundColor(Color.rgb(200, 150, 100));
                    break;
                case "HTML5":
                    linearLayout.setBackgroundColor(Color.rgb(210, 120, 70));
                    break;
                case "Shell":
                    linearLayout.setBackgroundColor(Color.rgb(200, 200, 100));
                    setColor();
                    break;
                case "People":
                    linearLayout.setBackgroundColor(Color.rgb(230, 200, 180));
                    setColor();
                    break;
                case "Linux":
                    linearLayout.setBackgroundColor(Color.rgb(200, 90, 160));
                    break;
                case "Skill":
                    linearLayout.setBackgroundColor(Color.rgb(200, 40, 40));
                    break;
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, "Services have some errors, try again.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        overridePendingTransition(R.anim.hold, R.anim.fade_in);
        return true;
    }

    public void initialization() {
        titleView = (TextView) findViewById(R.id.showDetailTitle);
        descriptionView = (TextView) findViewById(R.id.showDetailDescription);
        dateView = (TextView) findViewById(R.id.showDetailDate);
        typeView = (TextView) findViewById(R.id.showDetailType);
    }

    public void setColor() {
        int rgb = 80;
        titleView.setTextColor(Color.rgb(rgb, rgb, rgb));
        descriptionView.setTextColor(Color.rgb(rgb, rgb, rgb));
        dateView.setTextColor(Color.rgb(rgb, rgb, rgb));
        typeView.setTextColor(Color.rgb(rgb, rgb, rgb));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search_item_detail) {
            showDialog();
        } else if (id == R.id.refresh_detail_fragment) {
            fragment.refreshView(this.id);
            getFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.search_item_layout, null, false);

        List<String> completeData = new ArrayList<>();
        getCompleteData(completeData);
        final AutoCompleteTextView searchView = (AutoCompleteTextView) view.findViewById(R.id.search_complete_view);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, completeData);
        searchView.setAdapter(adapter);

        dialog.setView(view);
        dialog.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String content = searchView.getText().toString();
                callDetailFragmentShowResult(content);
            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.create().show();
    }

    private void callDetailFragmentShowResult(String content) {
        fragment.showSearchResult(content);
        getFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
    }

    private void getCompleteData(List<String> completeData) {
        Cursor cursor = new ItemDatabase(this, "itemDb.db3", null, 1).getReadableDatabase().rawQuery("select content from Item_Detail_Form where id=? group by content", new String [] {id});
        while (cursor.moveToNext()) {
            completeData.add(cursor.getString(0));
        }
        cursor.close();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.hold, R.anim.fade_in);
    }
}
