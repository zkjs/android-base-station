package net.chaoc.blescanner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.chaoc.blescanner.utils.CacheUtil;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yunba.android.manager.YunBaManager;

/**
 * Created by yejun on 10/31/16.
 * Copyright (C) 2016 qinyejun
 */

public class YunbaSettingActivity extends AppCompatActivity {

    @BindView(R.id.et_alias)
    EditText mEtAlias;
    @BindView(R.id.et_apid)
    EditText mEtAPId;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yunba_setting);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        String alias = CacheUtil.getInstance().getYunbaAlias();
        String apid = CacheUtil.getInstance().getAPID();

        mEtAlias.setText(alias);
        mEtAPId.setText(apid);
    }

    @OnClick(R.id.btn_confirm)
    public void onClick() {
        String alias = mEtAlias.getText().toString().trim();
        String apid = mEtAPId.getText().toString().trim();

        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(this,"频道名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(apid)) {
            Toast.makeText(this,"AP ID不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        CacheUtil.getInstance().setYunbaAlias(alias);
        CacheUtil.getInstance().setAPID(apid);

        YunBaManager.setAlias(this, apid, new IMqttActionListener(){

            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.i("Yunba", "Yunba: set Alias succeed");
                finish();
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Toast.makeText(YunbaSettingActivity.this,"监听失败，请确保网络畅通，再次尝试", Toast.LENGTH_LONG).show();
            }
        });
    }
}
