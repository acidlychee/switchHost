package com.mi.jr.qa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private final String env_preview = "127.0.0.1    localhost" + "\n" +
            "10.102.33.9   fundh5.mipay.com" + "\n" +
            "10.102.33.9   fundres.mipay.com" + "\n";
    private final String env_production = "127.0.0.1    localhost" + "\n";
    private FileOperator fileOperator;

    final String DATA_SYSTEM = "/data/system";
    final String STAGING_FILE = DATA_SYSTEM + "/server_staging";
    final String PREVIEW_FILE = DATA_SYSTEM + "/xiaomi_account_preview";
    final String OAUTH_FILE = DATA_SYSTEM + "/oauth_staging_preview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileOperator = new FileOperator(getApplicationContext());
        Button switchToPreviewButton = (Button) findViewById(R.id.switchToPreview);
        Button switchToProductionButton = (Button) findViewById(R.id.switchToProduction);
        Button switchToStagingButton = (Button) findViewById(R.id.switchToStaging);

        final TextView hostContentView = (TextView) findViewById(R.id.currentHostText);
        hostContentView.setMovementMethod(ScrollingMovementMethod.getInstance());

        if (!fileOperator.isRoot()) {
            switchToPreviewButton.setEnabled(false);
            switchToProductionButton.setEnabled(false);
            switchToStagingButton.setEnabled(false);

            Toast.makeText(getApplicationContext(), R.string.toast_cant_get_root, Toast.LENGTH_LONG).show();
        }
        fileOperator.addHostFile("previewHosts", env_preview);
        fileOperator.addHostFile("productionHosts", env_production);

        switchToPreviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileOperator.deleteFile(STAGING_FILE);
                fileOperator.deleteFile(PREVIEW_FILE);
                fileOperator.deleteFile(OAUTH_FILE);
                fileOperator.switchHost("previewHosts");
                hostContentView.setText(fileOperator.getCurrentHostContent());
                Toast.makeText(getApplicationContext(), R.string.toast_switched_preview, Toast.LENGTH_SHORT).show();
            }
        });

        switchToProductionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileOperator.deleteFile(STAGING_FILE);
                fileOperator.deleteFile(PREVIEW_FILE);
                fileOperator.deleteFile(OAUTH_FILE);
                fileOperator.switchHost("productionHosts");
                hostContentView.setText(fileOperator.getCurrentHostContent());
                Toast.makeText(getApplicationContext(), R.string.toast_switched_production, Toast.LENGTH_SHORT).show();
            }
        });

        switchToStagingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlert();
            }
        });
    }

    private void displayAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.text_logout_your_miid).setCancelable(
                false).setPositiveButton(R.string.button_has_logout,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        fileOperator.createFile(STAGING_FILE);
                        fileOperator.createFile(PREVIEW_FILE);
                        fileOperator.createFile(OAUTH_FILE);
                        Toast.makeText(getApplicationContext(), R.string.toast_switched_staging, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton(R.string.button_hasnot_logout, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_custom) {
            Intent intent = new Intent(MainActivity.this, CustomActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_about) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.action_about)
                    .setMessage(R.string.text_about)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView hostContentView = (TextView) findViewById(R.id.currentHostText);
        hostContentView.setText(fileOperator.getCurrentHostContent());
    }
}
