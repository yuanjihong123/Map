package com.example.administrator.mapclient.activity.homepage;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.administrator.mapclient.R;
import com.example.administrator.mapclient.view.AudioRecorderButton;
import com.example.administrator.mapclient.view.MediaManager;
import com.example.administrator.mapclient.view.RecoderAdapter;
import com.example.administrator.mapclient.view.Recorder;
import java.util.ArrayList;
import java.util.List;
/**
 * 求助附近
 */
public class CustodianActivity extends AppCompatActivity {
    private ListView mListView;
    private ArrayAdapter<Recorder> mAdapter;
    private List<Recorder> mDatas = new ArrayList<Recorder>();
    private AudioRecorderButton mAudioRecorderButton;
    private View animView;
    private TextView phonregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custodian);
       initdate();
    }
    private void initdate() {
        findview();
        mListView = (ListView) findViewById(R.id.list_custodian);
        mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.butt_custodian);
        mAudioRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            public void onFinish(float seconds, String filePath) {
                Recorder recorder = new Recorder(seconds, filePath);
                mDatas.add(recorder);
                mAdapter.notifyDataSetChanged(); //通知更新的内容
                mListView.setSelection(mDatas.size() - 1); //将lisview设置为最后一个
            }
        });
        mAdapter = new RecoderAdapter(this, mDatas);
        mListView.setAdapter(mAdapter);
        //listView的item点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                // 播放动画（帧动画）
                if (animView != null) {
                    animView.setBackgroundResource(R.drawable.adj);
                    animView = null;
                }
                animView = view.findViewById(R.id.id_recoder_anim);
                animView.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable animation = (AnimationDrawable) animView.getBackground();
                animation.start();
                // 播放录音
                MediaManager.playSound(mDatas.get(position).filePath,new MediaPlayer.OnCompletionListener() {

                    public void onCompletion(MediaPlayer mp) {
                        animView.setBackgroundResource(R.drawable.adj);
                    }
                });
            }
        });
    }
    private void findview() {
        phonregister = (TextView) findViewById(R.id.phonregister);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }
}
