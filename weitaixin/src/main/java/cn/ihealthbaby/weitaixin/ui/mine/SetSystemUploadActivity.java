package cn.ihealthbaby.weitaixin.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ihealthbaby.weitaixin.R;
import cn.ihealthbaby.weitaixin.base.BaseActivity;
import cn.ihealthbaby.weitaixin.library.data.model.LocalSetting;
import cn.ihealthbaby.weitaixin.library.util.SPUtil;


public class SetSystemUploadActivity extends BaseActivity {

    @Bind(R.id.back)
    RelativeLayout back;
    @Bind(R.id.title_text)
    TextView title_text;
    @Bind(R.id.function)
    TextView function;
    @Bind(R.id.slide_switch_upload)
    ImageView mSlideSwitchViewUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_system_upload);

        ButterKnife.bind(this);
        title_text.setText("上传设置");
//      back.setVisibility(View.INVISIBLE);

        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void initListener() {


        mSlideSwitchViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalSetting localSetting = SPUtil.getLocalSetting(getApplicationContext());
                if (!localSetting.isAutoUploading()) {
                    mSlideSwitchViewUpload.setImageResource(R.drawable.switch_on);
                } else {
                    mSlideSwitchViewUpload.setImageResource(R.drawable.switch_off);
                }
                localSetting.setAutoUploading(!localSetting.isAutoUploading());
                SPUtil.setLocalSetting(getApplicationContext(), localSetting);
            }
        });
    }


    @OnClick(R.id.back)
    public void onBack() {
        this.finish();
    }


    private void initView() {
        LocalSetting localSetting = SPUtil.getLocalSetting(getApplicationContext());
        if (localSetting.isAutoUploading()) {
            mSlideSwitchViewUpload.setImageResource(R.drawable.switch_on);
        } else {
            mSlideSwitchViewUpload.setImageResource(R.drawable.switch_off);
        }
    }

}
