package com.myself.c01b1rd.mynote;

import android.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentTransaction manager = getFragmentManager().beginTransaction();
    NavigationView navigationView;
    WebFragment onlineFragment;
    MyFragment offlineFragment;
    List<String> completeData;
    ItemDatabase db = new ItemDatabase(this, "itemDb.db3", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Primary");

        initialization();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setEnabled(false);
        navigationView.getMenu().getItem(2).setEnabled(false);
    }

    public void initialization() {
        onlineFragment = new WebFragment();
        offlineFragment = new MyFragment();
        try {
            manager.remove(offlineFragment);
        } catch (Exception e) {

        }
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, offlineFragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.viewMode1) {
            offlineFragment.changeViewMode(1);
            offlineFragment.initialization();
            getFragmentManager().beginTransaction().replace(R.id.main_fragment, offlineFragment).commit();
        } else if (id == R.id.viewMode2) {
            offlineFragment.changeViewMode(2);
            offlineFragment.initialization();
            getFragmentManager().beginTransaction().replace(R.id.main_fragment, offlineFragment).commit();
        } else if (id == R.id.viewMode3) {
            offlineFragment.changeViewMode(3);
            offlineFragment.initialization();
            getFragmentManager().beginTransaction().replace(R.id.main_fragment, offlineFragment).commit();
        } else if (id == R.id.viewMode4) {
            offlineFragment.changeViewMode(4);
            offlineFragment.initialization();
            getFragmentManager().beginTransaction().replace(R.id.main_fragment, offlineFragment).commit();
        } else if (id == R.id.search_item_by_title) {
            completeData = new ArrayList<>();
            getCompleteDataByTitle(completeData);
            showResult(completeData, 0);
        } else if (id == R.id.search_item_by_type) {
            completeData = new ArrayList<>();
            getCompleteDataByType(completeData);
            showResult(completeData, 1);
        }else if (id == R.id.search_item_by_description) {
            completeData = new ArrayList<>();
            getCompleteDataByDescription(completeData);
            showResult(completeData, 2);
        } else if (id == R.id.refresh_fragment) {
            if(getFragmentManager().findFragmentById(R.id.main_fragment).equals(offlineFragment)) {
                offlineFragment.refreshView();
                getFragmentManager().beginTransaction().replace(R.id.main_fragment, offlineFragment).commit();
            } else if(getFragmentManager().findFragmentById(R.id.main_fragment).equals(onlineFragment)) {
                onlineFragment.refreshPage();
                getFragmentManager().beginTransaction().replace(R.id.main_fragment, onlineFragment).commit();
            }
        } else if (id == R.id.exit) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.sort_by_type) {
            offlineFragment.refreshViewAfterSort();
            getFragmentManager().beginTransaction().replace(R.id.main_fragment, offlineFragment).commit();
        } else if (id == R.id.i2) {

        } else if (id == R.id.i3) {

        } else if (id == R.id.online_fragment) {
            manager.remove(offlineFragment);
            getFragmentManager().beginTransaction().replace(R.id.main_fragment, onlineFragment).commit();
            navigationView.getMenu().getItem(0).setEnabled(false);
            navigationView.getMenu().getItem(1).setEnabled(false);
            navigationView.getMenu().getItem(2).setEnabled(false);
        } else if (id == R.id.offline_fragment) {
            manager.remove(onlineFragment);
            getFragmentManager().beginTransaction().replace(R.id.main_fragment, offlineFragment).commit();
            navigationView.getMenu().getItem(0).setEnabled(true);
//            navigationView.getMenu().getItem(2).setEnabled(true);
//            navigationView.getMenu().getItem(1).setEnabled(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onlineFragment.canGoBack()) {
                onlineFragment.goBack();
                getFragmentManager().beginTransaction().replace(R.id.main_fragment, onlineFragment).commit();
            } else {
                if(getFragmentManager().findFragmentById(R.id.main_fragment).equals(offlineFragment)) {
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "On the oldest page.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return true;
    }

    public void getCompleteDataByTitle(List<String> completeData) {
        Cursor cursor = db.getReadableDatabase().rawQuery("select title from Item_Form group by title", null);
        while (cursor.moveToNext()) {
            completeData.add(cursor.getString(0));
        }
        cursor.close();
    }

    public void getCompleteDataByDescription(List<String> completeData) {
        Cursor cursor = db.getReadableDatabase().rawQuery("select description from Item_Form group by description", null);
        while (cursor.moveToNext()) {
            completeData.add(cursor.getString(0));
        }
        cursor.close();
    }

    public void getCompleteDataByType(List<String> completeData) {
        Cursor cursor = db.getReadableDatabase().rawQuery("select type from Item_Form group by type", null);
        while (cursor.moveToNext()) {
            completeData.add(cursor.getString(0));
        }
        cursor.close();
    }

    public void showResult(List<String> completeData,final int mode) {
        AlertDialog.Builder schDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.search_item_layout, null, false);
        final AutoCompleteTextView searchView = (AutoCompleteTextView) view.findViewById(R.id.search_complete_view);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, completeData);
        searchView.setAdapter(adapter);
        schDialog.setView(view);
        schDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mode == 0) {
                    offlineFragment.showSearchResultByTitle(searchView.getText().toString());
                } else if (mode == 1) {
                    offlineFragment.showSearchResultByType(searchView.getText().toString());
                } else if (mode == 2) {
                    offlineFragment.showSearchResultByDescription(searchView.getText().toString());
                }
                getFragmentManager().beginTransaction().replace(R.id.main_fragment, offlineFragment).commit();
            }
        });
        schDialog.setNegativeButton("Cancel", null);
        schDialog.create().show();
    }

}
