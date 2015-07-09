package com.mi.jr.qa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class CustomActivity extends Activity {

    final String DATA_SYSTEM = "/data/system";
    final String STAGING_FILE = DATA_SYSTEM + "/server_staging";
    final String PREVIEW_FILE = DATA_SYSTEM + "/xiaomi_account_preview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        Button saveButton = (Button) findViewById(R.id.saveHosts);
        Button cancelButton = (Button) findViewById(R.id.cancel);

        final EditText customEdit = (EditText) findViewById(R.id.editCustomHost);
        final FileOperator fileOperator = new FileOperator(getApplicationContext());
        customEdit.setText(fileOperator.getFileContent("customHosts"));

        if (!fileOperator.isRoot()) {
            saveButton.setEnabled(false);
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomHost(customEdit);
                fileOperator.switchHost("customHosts");
                Toast.makeText(getApplicationContext(), R.string.toast_switched_custom, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void addCustomHost(EditText customEdit) {
        FileOperator fileOperator = new FileOperator(getApplicationContext());
        String customContent = customEdit.getText().toString();
        fileOperator.addHostFile("customHosts", customContent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_custom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.action_about)
                    .setMessage(R.string.text_about)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }
}
