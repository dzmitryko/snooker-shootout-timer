package com.example.cdtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;
    CountDownTimer cdt = null;

    Button myBut1;
    TextView myTV1;
    EditText myET1;
    EditText myET_alarm;
    Button myStopBut;
    Button myResBut;
    Button butDima;
    Button butPetya;
    Button butSerg;
    Vibrator vibro;

    private SoundPool mSoundPool;
    private int mStreamId;
    private boolean b_first_load = true;

    int v_SoundFile_c1_id = R.raw.c1;
    int v_SoundFile_o3_id = R.raw.o3;
    int vS1;
    int vFin;
    int v_sec;
    int v_alarm_time = 10;


    private void myPlaySound(int pFileId  ){

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float leftVolume = curVolume / maxVolume;
        float rightVolume = curVolume / maxVolume;
        int priority = 1;
        int no_loop = 0;
        float normal_playback_rate = 1f;

        if(pFileId > 0) {
            mStreamId = mSoundPool.play(pFileId, leftVolume, rightVolume, priority, no_loop,
                    normal_playback_rate);
        }
        else{ mSoundPool.pause(mStreamId);}
    }

    private void saveDataToPref(String k, String v)
    {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(k,v).apply();
    }
    
    private String getDataFromPref(String k)
    {
        return pref.getString(k, "0");
    }
    private void setName(View v)
    {

        Integer v_curr_val = Integer.valueOf(getDataFromPref(String.valueOf(v.getId())));
        Button pointerButton = (Button) v;
        TextView pointerTv = null;
        vibro.vibrate(50);
        if(!b_first_load) { v_curr_val++;}

        if(v_curr_val > 20){
            v_curr_val = 0;
        }
        String v_curr_val_str = String.valueOf(v_curr_val);
        saveDataToPref(String.valueOf(pointerButton.getId()),v_curr_val_str );

        if((v.getId()) == R.id.button3)
        {
            pointerTv =  (TextView)findViewById(R.id.tv_dima);
        }
        if((v.getId()) == R.id.button4)
        {
            pointerTv =  (TextView)findViewById(R.id.tv_petya);
        }
        if((v.getId()) == R.id.button5)
        {
            pointerTv =  (TextView)findViewById(R.id.tv_serg);
        }
        if (pointerTv != null) {
            pointerTv.setText(v_curr_val_str);
        }
    }

    private void resetVisualScore() {
        setName(butDima);
        setName(butPetya);
        setName(butSerg);
        b_first_load = false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibro  = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        pref = getSharedPreferences("t1", MODE_PRIVATE);

        myBut1 = (Button)findViewById(R.id.button);

        myTV1 = (TextView)findViewById(R.id.textView);

        myET1 = (EditText)findViewById(R.id.editText);

        myET_alarm = (EditText)findViewById(R.id.editText3);

        myStopBut = (Button) findViewById(R.id.button2);
        myResBut  = (Button) findViewById(R.id.button6);
        butDima = (Button) findViewById(R.id.button3);
        butPetya = (Button) findViewById(R.id.button4);
        butSerg = (Button) findViewById(R.id.button5);

        resetVisualScore();

        v_sec = Integer.valueOf(myET1.getText().toString());
        v_alarm_time = Integer.valueOf(myET_alarm.getText().toString());

        mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC,100);
        vS1= mSoundPool.load(this, v_SoundFile_c1_id, 1);
        vFin= mSoundPool.load(this, v_SoundFile_o3_id, 1);

            //----

        //----

//-------------------
        myStopBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cdt != null)
                cdt.cancel();
                myPlaySound(-1);
            }
        });
//------------------
        myResBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pref.edit().clear().apply();
                b_first_load = true;
                resetVisualScore();
//                Toast.makeText(getApplicationContext(),
//                        "soundPool.play()",
//                        Toast.LENGTH_LONG).show();
            }
        });
//-------------------
        butDima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setName(v);
            }
        });

        //-------------------
        butPetya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setName(v);
            }
        });

        //-------------------
        butSerg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setName(v);
            }
        });

  //--------------------------
        myBut1.setOnClickListener(new View.OnClickListener() {

            void start_timer()
            {
                myPlaySound(-1);
                cdt = new CountDownTimer(v_sec * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String v_remain_time = String.valueOf(millisUntilFinished / 1000);
                        myTV1.setText((v_remain_time));
                        if (Integer.valueOf(v_remain_time) < v_alarm_time) {
                            myPlaySound(1);
                        };
                    }

                    @Override
                    public void onFinish() {
                        myTV1.setText("КАНЭЦ");
                        myPlaySound(2);
                        cdt = null;
                    }

                }.start();
            };
            @Override
            public void onClick(View view) {

                v_sec = Integer.valueOf(myET1.getText().toString()) ;
                v_alarm_time = Integer.valueOf(myET_alarm.getText().toString());
                if(v_sec >0 ){

                    if (cdt == null) {
                        start_timer();
                    }
                    else
                    {
                        cdt.cancel();
                        cdt = null;
                        start_timer();
                    }
                };
            };

        });
    }
}
