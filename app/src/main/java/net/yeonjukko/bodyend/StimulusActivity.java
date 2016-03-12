package net.yeonjukko.bodyend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StimulusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stimulus);

        TextView path = (TextView)findViewById(R.id.path);
        path.setText(InitTargetActivity.mDestinationUri+"");
    }
}
