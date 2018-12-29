package com.pospi.pai.yunpos.pay;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.pax.api.scanner.ScannerManager;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanPayActivity extends BaseActivity {

    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.scan_iv)
    ImageView scanIv;
    @Bind(R.id.btn_sure)
    Button btnSure;
    private ScannerManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_pay);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.scan_iv, R.id.btn_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_iv:
                break;
            case R.id.btn_sure:
                break;
        }
    }
}
