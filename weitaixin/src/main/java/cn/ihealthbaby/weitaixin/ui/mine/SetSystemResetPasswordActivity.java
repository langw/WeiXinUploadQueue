package cn.ihealthbaby.weitaixin.ui.mine;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ihealthbaby.client.ApiManager;
import cn.ihealthbaby.client.Result;
import cn.ihealthbaby.client.form.ChangePasswordForm;
import cn.ihealthbaby.client.model.User;
import cn.ihealthbaby.weitaixin.AbstractBusiness;
import cn.ihealthbaby.weitaixin.DefaultCallback;
import cn.ihealthbaby.weitaixin.R;
import cn.ihealthbaby.weitaixin.base.BaseActivity;
import cn.ihealthbaby.weitaixin.library.util.SPUtil;
import cn.ihealthbaby.weitaixin.library.util.ToastUtil;
import cn.ihealthbaby.weitaixin.CustomDialog;
import cn.ihealthbaby.weitaixin.ui.login.LoginActivity;


public class SetSystemResetPasswordActivity extends BaseActivity {

    @Bind(R.id.back)
    RelativeLayout back;
    @Bind(R.id.title_text)
    TextView title_text;
    @Bind(R.id.function)
    TextView function;
    //

    //    @Bind(R.id.et_phone_number_reset) EditText et_phone_number_reset;
    @Bind(R.id.et_password_reset)
    EditText et_password_reset;
    @Bind(R.id.et_mark_number_reset)
    EditText et_mark_number_reset;
    @Bind(R.id.tv_mark_num_text_reset)
    TextView tv_mark_num_text_reset;
    @Bind(R.id.tv_reset_password_action_reset)
    TextView tv_reset_password_action_reset;
    @Bind(R.id.ivShowPassword)
    CheckBox ivShowPassword;


    public Handler mHandler = new Handler();
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        ButterKnife.bind(this);

        title_text.setText("修改密码");
//      back.setVisibility(View.INVISIBLE);
        ivShowPassword.setTag("0");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.back)
    public void onBack() {
        this.finish();
    }


    @OnClick(R.id.ivShowPassword)
    public void ivShowPassword() {
        if ("0".equals(ivShowPassword.getTag())) {
            et_password_reset.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivShowPassword.setTag("1");
        } else {
            et_password_reset.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivShowPassword.setTag("0");
        }
    }


    public int countTime = 10;
    public boolean isSend = true;
    public CountDownTimer countDownTimer;
    public boolean isHasAuthCode = false;


    @OnClick(R.id.tv_mark_num_text_reset)
    public void tv_mark_num_text_reset() {
        if (isSend) {
            newPassword = et_password_reset.getText().toString().trim();
            if (TextUtils.isEmpty(newPassword)) {
                ToastUtil.show(getApplicationContext(), "请输入密码");
                return;
            }
            if (newPassword.length() < 6 && newPassword.length() > 20) {
                ToastUtil.show(getApplicationContext(), "密码必须是6~20位的数字和字母");
                return;
            }

            tv_mark_num_text_reset.setBackgroundResource(R.color.gray1);
            tv_mark_num_text_reset.setTextColor(getResources().getColor(R.color.gray2));

            getAuthCode();

        }
    }


    public void cancel() {
        tv_mark_num_text_reset.setText("发送验证码");
        isSend = true;
        countDownTimer.cancel();
        dialog.dismiss();
    }


    //0 注册验证码 1 登录验证码 2 修改密码验证码.
    public void getAuthCode() {


        User user = SPUtil.getUser(this);
        if(user!=null){
            String mobile = user.getMobile();
            if (TextUtils.isEmpty(mobile)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                final CustomDialog customDialog = new CustomDialog();
                dialog = customDialog.createDialog1(this, "验证码发送中...");
                customDialog.show();

                isSend = false;

                ApiManager.getInstance().accountApi.getAuthCode(user.getMobile(), 2,
                        new DefaultCallback<Boolean>(this, new AbstractBusiness<Boolean>() {
                            @Override
                            public void handleData(Boolean data) {
                                if (data) {
                                    tv_mark_num_text_reset.setBackgroundResource(R.color.gray1);
                                    tv_mark_num_text_reset.setTextColor(getResources().getColor(R.color.gray2));

                                    isHasAuthCode = true;

                                    try {
                                        countDownTimer = new CountDownTimer(60000, 1000) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                tv_mark_num_text_reset.setText(millisUntilFinished / 1000 + "秒之后重发");
                                                isSend = false;
                                            }

                                            @Override
                                            public void onFinish() {
                                                tv_mark_num_text_reset.setText("发送验证码");
                                                isSend = true;
                                                customDialog.dismiss();
                                                tv_mark_num_text_reset.setBackgroundResource(R.drawable.shape_send_verifycode);
                                                tv_mark_num_text_reset.setTextColor(getResources().getColor(R.color.black0));
                                            }
                                        };
                                        countDownTimer.start();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        cancel();
                                    }

                                } else {
                                    isHasAuthCode = false;
                                    cancel();
                                    ToastUtil.show(SetSystemResetPasswordActivity.this.getApplicationContext(), "请重新获取验证码");
                                }
                                customDialog.dismiss();
                            }

                            @Override
                            public void handleClientError(Context context, Exception e) {
                                super.handleClientError(context, e);
                                ToastUtil.show(SetSystemResetPasswordActivity.this.getApplicationContext(), "请重新获取验证码");

                                isHasAuthCode = false;
                                cancel();
                                customDialog.dismiss();
                            }

                            @Override
                            public void handleException(Exception e) {
                                super.handleException(e);
                                ToastUtil.show(SetSystemResetPasswordActivity.this.getApplicationContext(), "请重新获取验证码");

                                isHasAuthCode = false;
                                cancel();
                                customDialog.dismiss();
                            }
                        }), getRequestTag());
            }
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }


    @OnClick(R.id.tv_reset_password_action_reset)
    public void tvResetPasswordActionReset() {
        tvRegistAction2();
    }


    public String newPassword;
    public String mark_number;

    public void tvRegistAction2() {
        newPassword = et_password_reset.getText().toString().trim();
        mark_number = et_mark_number_reset.getText().toString().trim();
        if (TextUtils.isEmpty(newPassword)) {
            ToastUtil.show(getApplicationContext(), "请输入密码");
            return;
        }
        if (newPassword.length() < 6 || newPassword.length() > 20) {
            ToastUtil.show(getApplicationContext(), "密码必须是小于6位和大于20英文字符和数字");
            return;
        }

        if (!isHasAuthCode) {
            ToastUtil.show(getApplicationContext(), "请先获取验证码");
            return;
        }

        if (TextUtils.isEmpty(mark_number)) {
            ToastUtil.show(getApplicationContext(), "请输入验证码");
            return;
        }

        if (mark_number!=null&&mark_number.length() != 6) {
            ToastUtil.show(getApplicationContext(), "验证码必须是6位的数字");
            return;
        }


        final CustomDialog customDialog = new CustomDialog();
        final Dialog dialog = customDialog.createDialog1(this, "登录中...");
        customDialog.show();


        ChangePasswordForm changePasswordForm = new ChangePasswordForm(Integer.parseInt(mark_number), newPassword);

        ApiManager.getInstance().accountApi.changePassword(changePasswordForm,
                new DefaultCallback<Boolean>(this, new AbstractBusiness<Boolean>() {
                    @Override
                    public void handleData(Boolean data) {
                        if (data) {
                            ToastUtil.show(SetSystemResetPasswordActivity.this.getApplicationContext(), "修改密码成功");
                            finish();
                        }
                        customDialog.dismiss();
                    }

                    @Override
                    public void handleException(Exception e) {
                        super.handleException(e);
                        ToastUtil.show(SetSystemResetPasswordActivity.this.getApplicationContext(), "修改密码失败");
                        customDialog.dismiss();
                    }

                    @Override
                    public void handleClientError(Context context, Exception e) {
                        super.handleClientError(context, e);
                        ToastUtil.show(SetSystemResetPasswordActivity.this.getApplicationContext(), "修改密码失败");
                        customDialog.dismiss();
                    }

                    @Override
                    public void handleAllFailure(Context context) {
                        super.handleAllFailure(context);
                        ToastUtil.show(SetSystemResetPasswordActivity.this.getApplicationContext(), "修改密码失败");
                        customDialog.dismiss();
                    }

                    @Override
                    public void handleResult(Result<Boolean> result) {
                        super.handleResult(result);
                        customDialog.dismiss();
                    }
                }), getRequestTag());
    }


}


