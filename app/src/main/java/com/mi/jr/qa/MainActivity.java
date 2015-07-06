package com.mi.jr.qa;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileOperator = new FileOperator(getApplicationContext());
        Button switchToPreviewButton = (Button) findViewById(R.id.switchToPreview);
        Button switchToProductionButton = (Button) findViewById(R.id.switchToProduction);
        Button customizeButton = (Button) findViewById(R.id.custom);
        final TextView hostContentView = (TextView) findViewById(R.id.currentHostText);

        if (!fileOperator.isRoot()) {
            switchToPreviewButton.setEnabled(false);
            switchToProductionButton.setEnabled(false);
            Toast.makeText(getApplicationContext(), R.string.toast_cant_get_root, Toast.LENGTH_LONG).show();
        }
        fileOperator.addHostFile("previewHosts", env_preview);
        fileOperator.addHostFile("productionHosts", env_production);

        switchToPreviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileOperator.switchHost("previewHosts");
                hostContentView.setText(fileOperator.getCurrentHostContent());
                Toast.makeText(getApplicationContext(), R.string.toast_switched_preview, Toast.LENGTH_SHORT).show();
            }
        });

        switchToProductionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileOperator.switchHost("productionHosts");
                hostContentView.setText(fileOperator.getCurrentHostContent());
                Toast.makeText(getApplicationContext(), R.string.toast_switched_production, Toast.LENGTH_SHORT).show();
            }
        });

        customizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), R.string.toast_todo, Toast.LENGTH_SHORT).show();
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView hostContentView = (TextView) findViewById(R.id.readCurrentHostText);
        hostContentView.setText(fileOperator.getCurrentHostContent());
    }
}
