package com.example.video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;

public class MainActivity extends AppCompatActivity {

    private ListView main_lv;
    String url = "https://baobab.kaiyanapp.com/api/v4/tabs/selected?udid=11111&vc=168&vn=3.3.1&deviceModel=Huawei6&first_channel=eyepetizer_baidu_market&last_channel=eyepetizer_baidu_market&system_version_code=20";
    List<VideoBean.ItemListDTO> mList;
    private VideoAdapter adapter;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1){
                String json = (String) msg.obj;
                Log.d("fan","--主線程收到了數據-----" + json);
                //解析數據
                VideoBean videoBean = new Gson().fromJson(json, VideoBean.class);
                Log.d("fan","--主線程收到了數據-----" + videoBean);


                //過濾不需要的數據
                List<VideoBean.ItemListDTO> itemList = videoBean.getItemList();

                for (int i = 0; i < itemList.size(); i++) {
                    VideoBean.ItemListDTO listDTO = itemList.get(i);
                    if (listDTO.getType().equals("video")) {
                        mList.add(listDTO);
                    }
                }
                //提示適配器更新數據
                adapter.notifyDataSetChanged();
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("我的視頻");
        main_lv = findViewById(R.id.main_lv);
        //數據源
        mList = new ArrayList<>();
        //創建適配器對象
        adapter = new VideoAdapter(this, mList);
        //設置適配器
        main_lv.setAdapter(adapter);
        //加載網路數據
        loadData();
    }


    private void loadData() {
        //創建新線程，完成數據獲取
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonContent = HttpUtils.getJsonContent(url);
                //子線程不能更新UI，需要通過handler發送數據回到主線程
                Message message = new Message();//發送的消息對象
                message.what = 1;
                message.obj = jsonContent;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //釋放正在撥放的視頻信息
        JzvdStd.releaseAllVideos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manu_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}