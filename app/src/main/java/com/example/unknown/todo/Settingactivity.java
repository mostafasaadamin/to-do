package com.example.unknown.todo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class Settingactivity extends AppCompatActivity {
    EditText pick_time;
 TextView song;
    static SharedPreferences sharedpreferences;
    public final String MyPREFERENCES="userSetting";
    Uri uri=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingactivity);
        pick_time = findViewById(R.id.Sps_time);
            song=findViewById(R.id.pic_song);

    }
    public void SaveStatus(View view) {
    if(uri!=null&&!TextUtils.isEmpty(pick_time.getText().toString().trim())) {
    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    final SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putString("uri", uri.toString());
    editor.putString("time",pick_time.getText().toString().trim());
    editor.apply();
    Toast.makeText(this, "Data saved sucessfully", Toast.LENGTH_SHORT).show();
    }else
    {
        Toast.makeText(this, "Plz Enter all Filleds", Toast.LENGTH_SHORT).show();
    }
   }
    public void PickSong(View view) {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                uri = data.getData();
                song.setText("Selected!!");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
