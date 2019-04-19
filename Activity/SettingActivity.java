package th.co.octagoninteractive.groof.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.octagoninteractive.groof.R;
import th.co.octagoninteractive.groof.preference.GroofPreference;

/**
 * Created by mac on 8/24/18.
 */

public class SettingActivity extends Activity implements View.OnClickListener{

    @BindView(R.id.ll_site)
    LinearLayout ll_site;
    @BindView(R.id.tv_title_toolbar)
    TextView tv_title_toolbar;
    @BindView(R.id.main_content)
    RelativeLayout main_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initInstance();
    }

    private void initInstance(){

        ll_site.setOnClickListener(this);
        genaratedTheme();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.ll_site){
            goToSiteListPage();
        }
    }

    private void genaratedTheme(){

        if(GroofPreference.instance().getTheme().equals("night")){
            main_content.setBackgroundResource(R.drawable.bg_main_groof_night);
            tv_title_toolbar.setTextColor(getResources().getColor(R.color.title_color_night));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToMainPage();
    }

    private void backToMainPage(){

        Intent intent = new Intent(this, MainActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        this.startActivity(intent, options.toBundle());
        this.finish();
    }

    private void goToSiteListPage(){

        Intent intent = new Intent(this, SiteListActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        this.startActivity(intent, options.toBundle());
    }
}
