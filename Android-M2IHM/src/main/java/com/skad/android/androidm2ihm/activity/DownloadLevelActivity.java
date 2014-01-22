package com.skad.android.androidm2ihm.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.*;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.task.GetLevelFromServer;
import com.skad.android.androidm2ihm.task.GetListLevelFromServer;
import com.skad.android.androidm2ihm.utils.FileUtils;

import java.util.ArrayList;

public class DownloadLevelActivity extends ActionBarActivity implements Button.OnClickListener, DialogInterface.OnClickListener {

    private static final String TAG = "DownloadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_file);
        GetListLevelFromServer getList = new GetListLevelFromServer(this);
        getList.execute(null);
    }


    public void parsingList(String list) {
        ScrollView listButtonLvl = (ScrollView) findViewById(R.id.download_list_button);
        //clean the view
        listButtonLvl.removeAllViews();
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ArrayList<String> listLvl = FileUtils.listLvlfromstring(list, "|");
        for (String lvl : listLvl) {
            LinearLayout linearLayoutsub = new LinearLayout(this);
            linearLayoutsub.setOrientation(LinearLayout.HORIZONTAL);
            //TODO
            //Make the sublayout fill the parent
            //linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT));
            //download button
            TextView name = new TextView(this);
            name.setText(lvl);

            ImageButton btndownload = new ImageButton(this);
            btndownload.setImageResource(R.drawable.ic_action_download);
            btndownload.setTag(R.id.main_lvl_dir_tag, lvl);
            btndownload.setOnClickListener(this);


            linearLayoutsub.addView(name);
            linearLayoutsub.addView(btndownload);

            linearLayout.addView(linearLayoutsub);
        }
        listButtonLvl.addView(linearLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        String namelvl = view.getTag(R.id.main_lvl_dir_tag).toString();
        GetLevelFromServer getFile = new GetLevelFromServer(this);
        getFile.execute(namelvl);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }
}
