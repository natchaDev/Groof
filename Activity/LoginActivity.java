package th.co.octagoninteractive.groof.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.co.octagoninteractive.groof.R;
import th.co.octagoninteractive.groof.preference.GroofPreference;
import th.co.octagoninteractive.groof.preference.SaveSharedPreference;
import th.co.octagoninteractive.groof.repository.remote.dao.LoginDao;
import th.co.octagoninteractive.groof.repository.remote.dao.LoginData;
import th.co.octagoninteractive.groof.repository.remote.dao.LoginInfo;
import th.co.octagoninteractive.groof.service.HttpManager;

public class LoginActivity extends Activity implements View.OnClickListener{

    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.tv_username)
    EditText tv_username;
    @BindView(R.id.tv_password)
    EditText tv_password;
    @BindView(R.id.tv_demo_account)
    TextView tv_demo_account;
    @BindView(R.id.tv_forgot_password)
    TextView tv_forgot_password;
    @BindView(R.id.tv_sign_up)
    TextView tv_signup;

    Context mContext;
    private String fcmToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=this;
        ButterKnife.bind(this);
        initInstance();
    }

    private void initInstance(){

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LoginActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                Log.e("fcmToken ",mToken);
                fcmToken=mToken;
            }
        });

        btn_login.setOnClickListener(this);
        tv_demo_account.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);
        tv_signup.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        Log.i("OnClick",id+"");
        if(id==R.id.btn_login){
            UserLogin();
        }
        if(id==R.id.tv_demo_account){
            DemoLogin();
        }
        if(id==R.id.tv_forgot_password){
            nextToForgotten();
        }
        if(id==R.id.tv_sign_up){
            nextToWebView();
        }
    }

    private void UserLogin() {

            if (TextUtils.isEmpty(tv_username.getText())) {
                tv_username.setError("Username is required!");
                Toast.makeText(this, "กรุณากรอกชื่อผู้ใช้งาน", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(tv_password.getText())) {
                tv_password.setError("Password is required!");
                Toast.makeText(this, "กรุณากรอกรหัสผ่าน", Toast.LENGTH_LONG).show();
            } else {
                GroofPreference.instance().showProcessDialog(this, "เข้าสู่ระบบ");
                String userName = tv_username.getText().toString().trim();
                String userPassword = tv_password.getText().toString().trim();

                Call<LoginDao> call;
                call = HttpManager.getInstance().getService().UserLogin(userName, userPassword, fcmToken);
                call.enqueue(new Callback<LoginDao>() {

                    @Override
                    public void onResponse(Call<LoginDao> call, Response<LoginDao> response) {
                        if (response.isSuccessful()) {

                            LoginData loginData = response.body().getData();

                            if (response.body().getStatus().equals("success")) {

                                if (loginData != null) {
                                    LoginInfo loginInfo = response.body().getData().getProject();

                                    if (loginInfo != null) {

                                        GroofPreference.instance().setName(loginData.getName());
                                        GroofPreference.instance().setEntity_id(loginInfo.getEntityId());
                                        GroofPreference.instance().setAuthKey(loginData.getAuthKey());
                                        GroofPreference.instance().setUserName(loginData.getUsername());
                                        GroofPreference.instance().setSiteName(loginInfo.getName());

                                        SaveSharedPreference.savedUserAccount(mContext,
                                                loginData.getName(),
                                                loginInfo.getEntityId(),
                                                loginData.getAuthKey(),
                                                loginData.getUsername(),
                                                loginInfo.getName()
                                        );
                                        nextToMain();
                                    }
                                }
                            }
                            else {
                                GroofPreference.instance().showPopup(LoginActivity.this, "เข้าสู่ระบบ", response.body().getMessage().toString(), "fail");
                                GroofPreference.instance().closeProcessDialog();
                            }
                        }
                        else {
                            Toast.makeText(mContext, "ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้ กรุณาตรวจสอบการเชื่อมต่อ", Toast.LENGTH_LONG).show();
                            GroofPreference.instance().closeProcessDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginDao> call, Throwable t) {
                        Log.d("onFailure", t.toString());
                        GroofPreference.instance().closeProcessDialog();
                    }
                });
            }
    }

    private void DemoLogin() {

                GroofPreference.instance().showProcessDialog(this, "เข้าสู่ระบบ");
                tv_demo_account.setEnabled(false);

                String userName = GroofPreference.instance().getDemoUsername();
                String userPassword = GroofPreference.instance().getDemoPassword();

                Call<LoginDao> call;
                call = HttpManager.getInstance().getService().UserLogin(userName, userPassword, fcmToken);
                call.enqueue(new Callback<LoginDao>() {

                    @Override
                    public void onResponse(Call<LoginDao> call, Response<LoginDao> response) {
                        if (response.isSuccessful()) {
                            LoginData loginData = response.body().getData();

                            if (loginData != null) {
                                LoginInfo loginInfo = response.body().getData().getProject();

                                if (loginInfo != null) {
                                    GroofPreference.instance().setName(loginData.getName());
                                    GroofPreference.instance().setEntity_id(loginInfo.getEntityId());
                                    GroofPreference.instance().setAuthKey(loginData.getAuthKey());
                                    GroofPreference.instance().setUserName(loginData.getUsername());
                                    GroofPreference.instance().setSiteName(loginInfo.getName());

                                    SaveSharedPreference.savedUserAccount(mContext,
                                            loginData.getName(),
                                            loginInfo.getEntityId(),
                                            loginData.getAuthKey(),
                                            loginData.getUsername(),
                                            loginInfo.getName()
                                    );

                                    nextToMain();
                                    tv_demo_account.setEnabled(true);
                                }
                            }
                        } else {
                            tv_demo_account.setEnabled(true);
                            GroofPreference.instance().closeProcessDialog();
                            Toast.makeText(mContext, "ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginDao> call, Throwable t) {
                        tv_demo_account.setEnabled(true);
                        GroofPreference.instance().closeProcessDialog();
                        Log.d("onFailure", t.toString());
                    }
                });
    }

    private void nextToMain(){

        Intent intent = new Intent(mContext, MainActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(mContext, R.anim.fade_in, R.anim.fade_out);
        mContext.startActivity(intent, options.toBundle());
        GroofPreference.instance().closeProcessDialog();
        finish();
    }

    private void nextToForgotten(){

        Intent intent = new Intent(mContext, ForgottenActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(mContext, R.anim.fade_in, R.anim.fade_out);
        mContext.startActivity(intent, options.toBundle());
    }

    private void nextToWebView(){
        Intent in=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.gunkulroof.com/#/"));
        startActivity(in);
    }
}
