package com.jeffmony.videodemo.download;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jeffmony.downloader.model.VideoTaskItem;
import com.jeffmony.downloader.model.VideoTaskState;
import com.jeffmony.downloader.utils.LogUtils;
import com.jeffmony.videodemo.play.PlayerActivity;
import com.jeffmony.videodemo.R;

import java.io.File;

public class VideoDownloadListAdapter extends ArrayAdapter<VideoTaskItem> {

    private static final String TAG = "VideoListAdapter";

    private Context mContext;

    public VideoDownloadListAdapter(Context context, int resource, VideoTaskItem[] items) {
        super(context, resource, items);
        mContext = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.download_item, null);
        VideoTaskItem item = getItem(position);
        TextView urlTextView = (TextView) view.findViewById(R.id.url_text);
        urlTextView.setText(item.getUrl());
        TextView stateTextView = (TextView) view.findViewById(R.id.status_txt);
        TextView playBtn = (TextView) view.findViewById(R.id.download_play_btn);
        playBtn.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, PlayerActivity.class);
            String filePath = item.getFilePath();
            File file = new File(filePath);
            if (file.exists()) {
                intent.putExtra("videoUrl", item.getFilePath());
                mContext.startActivity(intent);
            } else {
                LogUtils.w(TAG, "File = " + filePath + " is gone");
            }

        });
        setStateText(stateTextView, playBtn, item);
        TextView infoTextView = (TextView) view.findViewById(R.id.download_txt);
        setDownloadInfoText(infoTextView, item);
        return view;
    }

    private void setStateText(TextView stateView, TextView playBtn, VideoTaskItem item) {
        switch (item.getTaskState()) {
            case VideoTaskState.PENDING:
            case VideoTaskState.PREPARE:
                playBtn.setVisibility(View.INVISIBLE);
                stateView.setText("?????????");
                break;
            case VideoTaskState.START:
            case VideoTaskState.DOWNLOADING:
                stateView.setText("?????????...");
                break;
            case VideoTaskState.PAUSE:
                playBtn.setVisibility(View.INVISIBLE);
                stateView.setText("????????????, ?????????=" + item.getDownloadSizeString());
                break;
            case VideoTaskState.SUCCESS:
                playBtn.setVisibility(View.VISIBLE);
                stateView.setText("????????????, ?????????=" + item.getDownloadSizeString());
                break;
            case VideoTaskState.ERROR:
                playBtn.setVisibility(View.INVISIBLE);
                stateView.setText("????????????");
                break;
            default:
                playBtn.setVisibility(View.INVISIBLE);
                stateView.setText("?????????");
                break;

        }
    }

    private void setDownloadInfoText(TextView infoView, VideoTaskItem item) {
        switch (item.getTaskState()) {
            case VideoTaskState.DOWNLOADING:
                infoView.setText("??????:" + item.getPercentString() + ", ??????:" + item.getSpeedString() +", ?????????:" + item.getDownloadSizeString());
                break;
            case VideoTaskState.SUCCESS:
                infoView.setText("??????:" + item.getPercentString());
                break;
            case VideoTaskState.PAUSE:
                infoView.setText("??????:" + item.getPercentString());
                break;
            default:
                break;
        }
    }

    public void notifyChanged(VideoTaskItem[] items, VideoTaskItem item) {
        for (int index = 0; index < getCount(); index++) {
            if (getItem(index).equals(item)) {
                items[index] = item;
                notifyDataSetChanged();
            }
        }
    }

}
