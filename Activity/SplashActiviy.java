package th.co.octagoninteractive.groof.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.co.octagoninteractive.groof.R;
import th.co.octagoninteractive.groof.preference.GroofPreference;
import th.co.octagoninteractive.groof.preference.SaveSharedPreference;
import th.co.octagoninteractive.groof.repository.remote.dao.ThemeDao;
import th.co.octagoninteractive.groof.service.HttpManager;

/**
 * Created by 8interactive 08/2018
 */

public class SplashActiviy extends Activity {

    private Handler handler;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mContext=this;
        handler = new Handler();
        checkTheme();
    }

    Runnable fetchAppInfoRunnable = new Runnable() {
        @Override
        public void run() {
            SkipToDemoApp();
        }
    };

    private void SkipToDemoApp(){

        Intent intent;
        if(SaveSharedPreference.getUserName(SplashActiviy.this).length()==0) {

            intent = new Intent(SplashActiviy.this, LoginActivity.class);
        }
        else {
            intent = new Intent(SplashActiviy.this, MainActivity.class);
            GroofPreference.instance().setName(SaveSharedPreference.getName(mContext));
            GroofPreference.instance().setEntity_id(SaveSharedPreference.getEntityID(mContext));
            GroofPreference.instance().setAuthKey(SaveSharedPreference.getAuthKey(mContext));
            GroofPreference.instance().setUserName(SaveSharedPreference.getUserName(mContext));
            GroofPreference.instance().setSiteName(SaveSharedPreference.getSitename(mContext));
            SaveSharedPreference.setUserLoggedIN();
        }

        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(mContext, R.anim.fade_in, R.anim.fade_out);
        mContext.startActivity(intent, options.toBundle());
        finish();
    }

    private void checkTheme() {

            Call<ThemeDao> call;
            call = HttpManager.getInstance().getService().getTheme(null);
            call.enqueue(new Callback<ThemeDao>() {
                @Override
                public void onResponse(Call<ThemeDao> call, Response<ThemeDao> response) {

                    if (response.isSuccessful()) {
                        ThemeDao themeDao = response.body();
                        if (themeDao != null) {
                            GroofPreference.instance().setTheme(themeDao.getData());
                            Log.i("getTheme Success: ", themeDao.getData());
                        }
                    } else {
                        Toast.makeText(SplashActiviy.this, "Can't get ThemeData ", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ThemeDao> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });

        handler.postDelayed(fetchAppInfoRunnable, 2000);
    }
}
