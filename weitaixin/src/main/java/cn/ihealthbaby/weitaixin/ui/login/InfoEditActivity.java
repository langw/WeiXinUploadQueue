package cn.ihealthbaby.weitaixin.ui.login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ihealthbaby.weitaixin.R;
import cn.ihealthbaby.weitaixin.library.log.LogUtil;
import cn.ihealthbaby.weitaixin.library.tools.DateTimeTool;
import cn.ihealthbaby.weitaixin.pickerview.TimePopupWindow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ihealthbaby.client.form.UserInfoForm;
import cn.ihealthbaby.weitaixin.CustomDialog;
import cn.ihealthbaby.weitaixin.UploadFileEngine;
import cn.ihealthbaby.weitaixin.WeiTaiXinApplication;
import cn.ihealthbaby.weitaixin.base.BaseActivity;
import cn.ihealthbaby.weitaixin.library.tools.ImageTool;
import cn.ihealthbaby.weitaixin.library.util.SPUtil;
import cn.ihealthbaby.weitaixin.library.util.ToastUtil;
import cn.ihealthbaby.weitaixin.pickerview.lib.NumericWheelAdapter;
import cn.ihealthbaby.weitaixin.ui.MeMainFragmentActivity;
import cn.ihealthbaby.weitaixin.ui.mine.GradedActivity;
import cn.ihealthbaby.weitaixin.ui.widget.MyPoPoWin;
import cn.ihealthbaby.weitaixin.ui.widget.RoundImageView;



public class InfoEditActivity extends BaseActivity implements MyPoPoWin.ISelectPhoto {


    //-28天≤当前日期≤300天；
    public void showInfoEdit() {

//        Date date = new Date();
        long time =  System.currentTimeMillis();

        SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日期


        //-28
        long preTime = time - 28L * 24 * 3600 * 1000;

        //300
        long postTime = time + 300L * 24 * 3600 * 1000;

        Date date1 = new Date(preTime);

        Date date2 = new Date(postTime);

        newD(date1);

        newD(date2);
    }

    public void newD(Date date){

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int cuurYear = c.get(Calendar.YEAR);
        int cuurMonth = c.get(Calendar.MONTH)+1;
        int cuurDay = c.get(Calendar.DAY_OF_MONTH);
        LogUtil.d("Calend+==", "Calend+==>" + cuurYear + ":" + cuurMonth + ":" + cuurDay);

//        SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日期
//        String dateTime = dateFm.format(date);
//        LogUtil.d("dateTime+==", "dateTime+==>" + dateTime);

    }

    @Bind(R.id.back)
    RelativeLayout back;
    @Bind(R.id.title_text)
    TextView title_text;
    @Bind(R.id.function)
    TextView function;

    @Bind(R.id.iv_head_icon_info)
    RoundImageView iv_head_icon_info;
    @Bind(R.id.et_birthdate_info)
    TextView et_birthdate_info;
    @Bind(R.id.et_date_info)
    TextView et_date_info;
    @Bind(R.id.tv_info_edit_action)
    TextView tv_info_edit_action;
    @Bind(R.id.et_name_info)
    EditText et_name_info;

    public boolean isDone1 = false;
    public boolean isDone2 = false;
    public boolean isDone3 = false;
    public boolean isDone4 = false;

    public Bitmap photo;

    TimePopupWindow mTimePopupWindow;
    TimePopupWindow mExpectTimePopupWindow;
    @Bind(R.id.tv_birthday)
    EditText mTvBirthday;
    @Bind(R.id.tv_expect_day)
    EditText mTvExpectDay;
    private boolean birthDateShow;
    private boolean expectedDateShow;
    public MyPoPoWin ppWin;
    public static boolean isBirthDateShow;
    public static boolean isExpectedDateShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_edit);
        ButterKnife.bind(this);

        title_text.setText("完善个人信息");
        back.setVisibility(View.INVISIBLE);

        form = new UserInfoForm();

    }

    public Date getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            return sdf.parse("1888年1月1日");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }


//    @OnClick(R.id.back)
//    public void onBack(RelativeLayout view) {
//        this.finish();
//    }


    @OnClick(R.id.iv_head_icon_info)
    public void ivHeadIconInfo(ImageView iv_head_icon_info) {
        ppWin = new MyPoPoWin(this);
        ppWin.showAtLocation(iv_head_icon_info);
        ppWin.setListener(this);
    }

    private int year, monthOfYear, dayOfMonth, hourOfDay, minute;

    @OnClick(R.id.et_birthdate_info)
    public void etBirthdateInfo() {
        showInfoEdit();
        setDate(false, et_birthdate_info);
    }

    @OnClick(R.id.et_date_info)
    public void etDateInfo() {
        showInfoEdit();
        setDate(true, et_date_info);
    }


    public void setDate(final boolean bool, final TextView textView) {
        Calendar calendar = Calendar.getInstance();
//      calendar.setTimeInMillis(System.currentTimeMillis());
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        //
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                String dateStr="你的生日  " + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth+"日";
                SpannableString ss = new SpannableString(dateStr);
                ss.setSpan(new ForegroundColorSpan(Color.parseColor("#FFB8B8B8")), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                form.setBirthday(new Date(year,(monthOfYear+1),dayOfMonth));
                if(bool){
                    dateStr="预产日期  " + year + "年"+ (monthOfYear + 1) + "月" + dayOfMonth+"日";
                    ss = new SpannableString(dateStr);
                    ss.setSpan(new ForegroundColorSpan(Color.parseColor("#FFB8B8B8")), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setText(ss);
                    isDone3=true;
                    form.setDeliveryTime(new Date(year,(monthOfYear+1),dayOfMonth));
                }else {
                    textView.setText(ss);
                    isDone4=true;
                }
            }
        }, year, monthOfYear, dayOfMonth);

        if (bool) {
            Calendar cd = Calendar.getInstance();
            cd.setTime(new Date());
            cd.add(Calendar.DATE, -28);
            long preTime = cd.getTimeInMillis();


            Calendar cd2 = Calendar.getInstance();
            cd2.setTime(new Date());
            cd2.add(Calendar.DATE, 300);
            long postTime = cd2.getTimeInMillis();

            datePickerDialog.getDatePicker().setMinDate(preTime);
            datePickerDialog.getDatePicker().setMaxDate(postTime);
        } else {
            Calendar cd = Calendar.getInstance();
            cd.setTime(new Date());
            cd.add(Calendar.YEAR, -60);
            long preTime = cd.getTimeInMillis();


            Calendar cd2 = Calendar.getInstance();
            cd2.setTime(new Date());
            cd2.add(Calendar.YEAR, -15);
            long postTime = cd2.getTimeInMillis();

            datePickerDialog.getDatePicker().setMinDate(preTime);
            datePickerDialog.getDatePicker().setMaxDate(postTime);
        }

        datePickerDialog.show();
    }


    UserInfoForm form;
    UploadFileEngine engine;

    @OnClick(R.id.tv_info_edit_action)
    public void tvInfoEditAction() {

        if (!TextUtils.isEmpty(et_name_info.getText().toString())
                && !TextUtils.isEmpty(et_birthdate_info.getText().toString())
                && !TextUtils.isEmpty(et_date_info.getText().toString())) {
            if (et_name_info.getText().toString().length() < 2) {
                ToastUtil.show(getApplicationContext(), "名字至少两个字符");
                return;
            }
            form.setName(et_name_info.getText().toString());
            CustomDialog customDialog = new CustomDialog();
            if (engine == null) {
                engine = new UploadFileEngine(this, form, customDialog);
            }
            Dialog dialog = customDialog.createDialog1(this, "正在完善个人信息...");
            dialog.show();
            engine.customDialog = customDialog;
            engine.isUpdateInfo = true;
            engine.setOnFinishActivity(new UploadFileEngine.FinishActivity() {
                @Override
                public void onFinishActivity(boolean isFinish) {
                    if (isFinish) {
                        WeiTaiXinApplication.getInstance().putValue("InfoEdit", "true");
                        if (!SPUtil.getUser(InfoEditActivity.this).getHasRiskscore()) {
                            Intent intentHasRiskscore = new Intent(InfoEditActivity.this, GradedActivity.class);
                            startActivity(intentHasRiskscore);
                        } else {
                            Intent intent = new Intent(InfoEditActivity.this, MeMainFragmentActivity.class);
                            startActivity(intent);
                        }
                        InfoEditActivity.this.finish();
                    }
                }
            });
            engine.completeInfoAction();
        } else {
            WeiTaiXinApplication.getInstance().putValue("InfoEdit", "");
            ToastUtil.show(getApplicationContext(), "请完善个人信息");
        }

    }
//        if (!TextUtils.isEmpty(et_name_info.getText().toString().trim())) {
//            isDone2 = true;
//        }
//
//        if (isDone1 && isDone2 && isDone3 && isDone4) {
//            form.setName(et_name_info.getText().toString().trim());
//            CustomDialog customDialog = new CustomDialog();
//            Dialog dialog = customDialog.createDialog1(this, "正在完善个人信息...");
//            if (photo != null) {
//                dialog.show();
//                engine.customDialog = customDialog;
//                engine.isUpdateInfo = true;
//                engine.setOnFinishActivity(new UploadFileEngine.FinishActivity() {
//                    @Override
//                    public void onFinishActivity(boolean isFinish) {
//                        if (isFinish) {
//                            InfoEditActivity.this.finish();
//                        }
//                    }
//                });
//                engine.completeInfoAction();
//            } else {
//                ToastUtil.show(getApplicationContext(), "头像没有");
//            }
//        } else {
//            ToastUtil.show(getApplicationContext(), "请完善个人信息");
//        }
//}

    @Override
    public void onSelectPhoto(int flag) {
        switch (flag) {
            case MyPoPoWin.FLAG_TAKE_PHOTO:
//              Toast.makeText(this, "拍照2", Toast.LENGTH_SHORT).show();
                Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, MyPoPoWin.FLAG_TAKE_PHOTO);
                break;

            case MyPoPoWin.FLAG_PICK_PHOTO:
//              Toast.makeText(this,"从相册中选择2",Toast.LENGTH_SHORT).show();
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, MyPoPoWin.FLAG_PICK_PHOTO);
                } else {
                    ToastUtil.show(getApplicationContext(), "请确认已经插入SD卡");
                }
                break;
        }
    }


    public void upLoadHeadPic() {
        CustomDialog customDialog = new CustomDialog();
        Dialog dialog = customDialog.createDialog1(this, "头像上传中...");
        engine = new UploadFileEngine(this, form, customDialog);
        if (photo != null) {
            dialog.show();
            engine.init(ImageTool.Bitmap2Bytes(photo));
//            engine.init(bitmapSaveFile(getApplicationContext(), photo, "headImageFile.png"));
        } else {
            ToastUtil.show(getApplicationContext(), "头像没有");
        }
    }


//    public File bitmapSaveFile(Context context, Bitmap bitmap, String fileName){
//        File filesDir = context.getFilesDir();
//        File headImageFile = new File(filesDir,fileName);
//        try {
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(headImageFile));
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
//            bos.flush();
//            bos.close();
//            return headImageFile;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;

        switch (requestCode) {
            case MyPoPoWin.FLAG_TAKE_PHOTO: //拍照
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    if (bitmap != null) {
                        bitmap = ImageTool.compressBitmap(bitmap);
                        photo = bitmap;
                        iv_head_icon_info.setImageBitmap(bitmap);
                        isDone1 = true;
                        upLoadHeadPic();
                    }
                } else {
                    isDone1 = false;
                }
                break;

            case MyPoPoWin.FLAG_PICK_PHOTO:
                ContentResolver cr = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(data.getData()));
                    if (bitmap != null) {
                        bitmap = ImageTool.compressBitmap(bitmap);
                        photo = bitmap;
                        iv_head_icon_info.setImageBitmap(bitmap);
                        isDone1 = true;
                        upLoadHeadPic();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isDone1 = false;
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!TextUtils.isEmpty(et_name_info.getText().toString().trim())) {
                isDone2 = true;
            }
            if (isDone1 && isDone2 && isDone3 && isDone4) {
                return false;
            } else {
                ToastUtil.show(getApplicationContext(), "请完善个人信息");
            }
            return true;
        }

        return false;
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }

}
