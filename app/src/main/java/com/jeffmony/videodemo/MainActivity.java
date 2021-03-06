package com.jeffmony.videodemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jeffmony.videodemo.download.DownloadSettingsActivity;
import com.jeffmony.videodemo.download.VideoDownloadListActivity;
import com.jeffmony.videodemo.merge.VideoMergeActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 1001;

    private Button mDownloadSettingBtn;
    private Button mDownloadListBtn;
    private Button mVideoMergeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        initViews();
    }

    private void initViews() {
        mDownloadSettingBtn = findViewById(R.id.download_settings_btn);
        mDownloadListBtn = findViewById(R.id.download_list_btn);
        mVideoMergeBtn = findViewById(R.id.video_merge_btn);

        mDownloadSettingBtn.setOnClickListener(this);
        mDownloadListBtn.setOnClickListener(this);
        mVideoMergeBtn.setOnClickListener(this);
    }

    private void requestPermissions() {
        List<String> permissionList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int index = 0; index < grantResults.length; index++) {
                if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "[" + permissions[index] + "]???????????????", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mDownloadSettingBtn) {
            Intent intent = new Intent(this, DownloadSettingsActivity.class);
            startActivity(intent);
        } else if (v == mDownloadListBtn) {
            Intent intent = new Intent(this, VideoDownloadListActivity.class);
            startActivity(intent);
        } else if (v == mVideoMergeBtn) {
            Intent intent = new Intent(this, VideoMergeActivity.class);
            startActivity(intent);
        }
    }
}
