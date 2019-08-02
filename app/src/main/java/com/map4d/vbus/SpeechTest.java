package com.map4d.vbus;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

//public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
//
//    private static final int TTS_ENGINE_REQUEST = 101;
//    private TextToSpeech textToSpeech;
//    private EditText TestForSpeech;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        TestForSpeech = findViewById(R.id.speech_text);
//    }
//
//    public void performSpeech(View view) {
//        Intent checkIntent = new Intent();
//        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
//        startActivityForResult(checkIntent, TTS_ENGINE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == TTS_ENGINE_REQUEST && resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
//        {
//            textToSpeech = new TextToSpeech(this, this);
//        }
//        else {
//            Intent installIntent = new Intent();
//            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
//            startActivity(installIntent);
//        }
//
//    }
//
//
//    @Override
//    public void onInit(int status) {
//
//        if (status==TextToSpeech.SUCCESS)
//        {
//            int languageStatus =  textToSpeech.setLanguage(Locale.US);
//            if (languageStatus==TextToSpeech.LANG_MISSING_DATA || languageStatus==TextToSpeech.LANG_NOT_SUPPORTED)
//            {
//                Toast.makeText(this, "Language is not supported", Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                String data  = TestForSpeech.getText().toString();
//                int speechStatus = textToSpeech.speak(data,TextToSpeech.QUEUE_FLUSH,null);
//                if (speechStatus==TextToSpeech.ERROR)
//                {
//                    Toast.makeText(this, "Error while speech...", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//        else
//        {
//            Toast.makeText(this, "Text to speech engine fialed..", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Locale;

import android.widget.TextView;
import android.widget.Toast;

public class SpeechTest extends Activity {
    TextToSpeech t1;
    String ed1 = "dụ mẹ mày, nhã!";
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);
        b1=(Button)findViewById(R.id.speak);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(new Locale("vi"));
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountDownTimer uy = new CountDownTimer(86400000, 2000) {
                    public void onFinish() {
                        Toast.makeText(getApplicationContext(), "finish", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onTick(long l) {
                        String toSpeak = ed1;
                        Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }.start();
            }
        });
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}