package th.co.octagoninteractive.groof.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.co.octagoninteractive.groof.R;
import th.co.octagoninteractive.groof.preference.GroofPreference;
import th.co.octagoninteractive.groof.repository.remote.dao.ProfileDao;
import th.co.octagoninteractive.groof.repository.remote.dao.ProfileData;
import th.co.octagoninteractive.groof.repository.remote.dao.UpdateProfileDao;
import th.co.octagoninteractive.groof.service.HttpManager;

public class ProfileActivity extends Activity implements View.OnClickListener{

    @BindView(R.id.tv_forgot_password) TextView tv_forgot_password;
    @BindView(R.id.edit_name) EditText edit_name;
    @BindView(R.id.edit_lastaname) EditText edit_lastname;
    @BindView(R.id.edit_email) EditText edit_mail;
    @BindView(R.id.edit_lineID) EditText edite_lineID;
    @BindView(R.id.edit_phone) EditText edit_phone;
    @BindView(R.id.btn_submit) Button btn_submit;

    private String aut,name,lastname,email,phone,lineId;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        mContext = this;
        initInstance();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.tv_forgot_password){
            goChangePasswordPage();
        }
        else if(id == R.id.btn_submit){
            updateProfile();
        }
        else if(id == R.id.edit_name){
            edit_name.setCursorVisible(true);
        }
        else if(id == R.id.edit_lastaname){
            edit_lastname.setCursorVisible(true);
        }
        else if(id == R.id.edit_lineID){
            edite_lineID.setCursorVisible(true);
        }
        else if(id == R.id.edit_email){
            edit_mail.setCursorVisible(true);
        }
        else if(id == R.id.edit_phone){
            edit_phone.setCursorVisible(true);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToMainPage();
    }

    private void initInstance(){
        edit_name.setOnClickListener(this);
        edit_lastname.setOnClickListener(this);
        edit_phone.setOnClickListener(this);
        edit_mail.setOnClickListener(this);
        edite_lineID.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        getProfile();
    }

    private void updateProfile(){

        aut = GroofPreference.instance().getAuthKey();
        name = edit_name.getText().toString();
        lastname = edit_lastname.getText().toString();
        email = edit_mail.getText().toString();
        lineId = edite_lineID.getText().toString();
        phone = edit_phone.getText().toString();

        Call<UpdateProfileDao> call;
        call = HttpManager.getInstance().getService().getUpdateProfile(aut,name,lastname,email,lineId,phone);
        call.clone().enqueue(new Callback<UpdateProfileDao>() {
            @Override
            public void onResponse(Call<UpdateProfileDao> call, Response<UpdateProfileDao> response) {
                if(response.isSuccessful()){
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    backToMainPage();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileDao> call, Throwable t) {
                Toast.makeText(mContext, "Update Profile Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProfile(){

        aut = GroofPreference.instance().getAuthKey();

        Call<ProfileDao> call;
        call = HttpManager.getInstance().getService().getProfile(aut);
        call.clone().enqueue(new Callback<ProfileDao>() {
            @Override
            public void onResponse(Call<ProfileDao> call, Response<ProfileDao> response) {
                if (response.isSuccessful() && response.body() != null) {

                    ProfileData profileData = response.body().getData();

                    if(profileData!=null){
                        edit_name.setText(profileData.getName());
                        edit_lastname.setText(profileData.getLastname());
                        edit_mail.setText(profileData.getEmail());
                        edite_lineID.setText(profileData.getLineId());
                        edit_phone.setText(profileData.getPhone());

                        name = profileData.getName();
                        lastname = profileData.getLastname();
                        email = profileData.getEmail();
                        lineId = profileData.getLineId();
                        phone = profileData.getPhone();

                        GroofPreference.instance().setName(profileData.getName()+" "+profileData.getLastname());
                    }
                }
                else {
                    Toast.makeText(mContext, "Can't get Profile Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileDao> call, Throwable t) {

            }
        });

    }

    private void backToMainPage(){

        Intent intent = new Intent(this, MainActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        this.startActivity(intent, options.toBundle());
        this.finish();
    }

    private void goChangePasswordPage(){
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        this.startActivity(intent, options.toBundle());
        this.finish();
    }
}
