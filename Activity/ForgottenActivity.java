package th.co.octagoninteractive.groof.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.co.octagoninteractive.groof.R;
import th.co.octagoninteractive.groof.preference.GroofPreference;
import th.co.octagoninteractive.groof.repository.remote.dao.ForgottenDao;
import th.co.octagoninteractive.groof.service.HttpManager;

/**
 * Created by mac on 9/10/18.
 */

public class ForgottenActivity extends Activity implements View.OnClickListener{

    @BindView(R.id.tv_email)
    EditText tv_email;
    @BindView(R.id.btn_forgotten)
    Button btn_forgotten;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten);
        ButterKnife.bind(this);
        mContext=this;

        initInstance();
    }

    private void initInstance(){
        btn_forgotten.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.btn_forgotten){
            Forgotten();
        }
    }

    private void Forgotten() {

        if( TextUtils.isEmpty(tv_email.getText())){
            tv_email.setError( "Email is required!" );
            Toast.makeText(this, getString(R.string.toast_forgotten_add_email), Toast.LENGTH_LONG).show();
        }
        else {

            String userEmail = tv_email.getText().toString().trim();

            Call<ForgottenDao> call;
            call = HttpManager.getInstance().getService().Forgotten(userEmail);
            call.enqueue(new Callback<ForgottenDao>() {

                @Override
                public void onResponse(Call<ForgottenDao> call, Response<ForgottenDao> response) {
                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals("success")) {

                            ForgottenDao forgottenDao = response.body();
                            if (forgottenDao != null) {
                               GroofPreference.instance().showPopup(ForgottenActivity.this,getString(R.string.toast_title_forgotten_password),forgottenDao.getMessage(),"success");
                            }
                        }
                        else{
                            GroofPreference.instance().showPopup(ForgottenActivity.this,getString(R.string.toast_title_forgotten_password),response.body().getMessage(),"fail");
                        }
                    }
                    else{
                        Toast.makeText(mContext, getString(R.string.toast_forgotten_alert), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ForgottenDao> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });


        }
    }

}
