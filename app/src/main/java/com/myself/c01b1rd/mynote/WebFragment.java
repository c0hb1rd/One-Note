package com.myself.c01b1rd.mynote;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by crackgg on 1/8/16.
 */
public class WebFragment extends Fragment {

    View view;
    WebView webView;
    WebSettings webSettings;
    ProgressDialog dialog;
    RadioButton b1, b2, b3, b4;
    ItemDatabase db;
    LayoutInflater mLayoutInflater;
    String [] autoCompleteData = {"Android", "HTML5", "Python2.x", "Python3.x", "Java", "PHP",
            "C/C++", "Lua", "Website", "Tool", "Security", "Skill",
            "Other", "Javascript", "Linux", "Windows", "People", "Shell",
            "Network", "Database", "IDE", "Word"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        view = inflater.inflate(R.layout.web_layout, container, false);

        initialization();

        b1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    webView.loadUrl("https://www.google.com");
            }
        });

        b2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    webView.loadUrl("http://m.youdao.com");
            }
        });

        b3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    webView.loadUrl("https://zh.m.wikipedia.org#");
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mLayoutInflater.getContext());
                View view = mLayoutInflater.inflate(R.layout.choose_list3, null);
                dialog.setView(view);
                final EditText editText = (EditText) view.findViewById(R.id.editText);
                editText.setHint("E.g：www.google.com");
                dialog.setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        webView.loadUrl("http://" + editText.getText().toString());
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.create().show();
            }
        });

        return view;
    }

    public void initialization() {
        db = new ItemDatabase(mLayoutInflater.getContext(), "itemDb.db3", null, 1);
        webView = (WebView) view.findViewById(R.id.webView);
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        b1 = (RadioButton) view.findViewById(R.id.b1);
        b2 = (RadioButton) view.findViewById(R.id.b2);
        b3 = (RadioButton) view.findViewById(R.id.b3);
        b4 = (RadioButton) view.findViewById(R.id.b4);

        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }


        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    closeDialog();
                } else {
                    openDialog(newProgress);
                }

                super.onProgressChanged(view, newProgress);
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) getActivity().findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton) getActivity().findViewById(R.id.fab2);

        fab1.hide();
        fab2.show();

        fab2.setOnClickListener(new View.OnClickListener() {
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
                            Toast.makeText(mLayoutInflater.getContext(), "Add success.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                addDialog.setNegativeButton("Cancel", null);
                addDialog.create().show();
            }
        });

    }

    public boolean canGoBack() {
        try {
            return webView.canGoBack();
        } catch (Exception e) {
            return false;
        }
    }

    public void goBack() {
        webView.goBack();
    }

    private void openDialog(int newProgress) {
        if (dialog == null) {
            dialog = new ProgressDialog(mLayoutInflater.getContext());
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setTitle("Loading...");
            dialog.setProgress(newProgress);
            dialog.show();
        } else {
            dialog.setProgress(newProgress);
        }
    }

    private void closeDialog() {
        if ( dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
        dialog = null;
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

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR) + "";
        String month = (calendar.get(Calendar.MONTH ) + 1) + "";
        String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH) + "";
        return year + "-" + month + "-" + day + " " + (calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : calendar.get(Calendar.HOUR_OF_DAY)) + ":" + (calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE));
    }

    public void refreshPage() {
        webView.reload();
    }
}
