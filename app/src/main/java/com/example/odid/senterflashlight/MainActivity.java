package com.example.odid.senterflashlight;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private String cameraId;
    private ImageButton imangeButtonOnOff;
    private ImageView imageViewLight;
    private Boolean isTorchOn;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imangeButtonOnOff = (ImageButton) findViewById(R.id.imageButton_on_off);
        imageViewLight = (ImageView) findViewById(R.id.imageViewLight);
        isTorchOn = false;
        Boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {

            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error !!");
            alert.setMessage("Device anda tidak mendukung flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                    System.exit(0);
                }
            });
            alert.show();
            return;
        }

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {

                cameraId = cameraManager.getCameraIdList()[0];

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        imangeButtonOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isTorchOn) {
                        turnOffFlashLight();
                        isTorchOn = false;
                    } else {
                        turnOnFlashLight();
                        isTorchOn = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void turnOnFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, true);
                playOnOffSound();
                imangeButtonOnOff.setImageResource(R.drawable.button_off);
                imageViewLight.setImageResource(R.drawable.light_on);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnOffFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, false);
                playOnOffSound();
                imangeButtonOnOff.setImageResource(R.drawable.button_on);
                imageViewLight.setImageResource(R.drawable.light_off);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playOnOffSound(){

        mp = MediaPlayer.create(MainActivity.this, R.raw.sound_on_off);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp.start();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(isTorchOn){
            turnOffFlashLight();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isTorchOn){
            turnOffFlashLight();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isTorchOn){
            turnOnFlashLight();
        }
    }
    }

