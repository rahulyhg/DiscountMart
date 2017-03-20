package sourabh.ichiapp.activities;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import sourabh.ichiapp.R;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simulateDayNight(/* DAY */ 0);

        View aboutPage = new AboutPage(this)

//                .isRTL(false)
//                .setImage1(R.drawable.)
                .addItem(new Element().setTitle("Version 1.0"))
//                .addItem(new Element().setTitle("About Us Information"))
//
//                .addGroup("Connect with us")
//                .addEmail("elmehdi.sakout@gmail.com")
//                .addWebsite("http://medyo.github.io/")
//                .addFacebook("the.medy")
//                .addTwitter("medyo80")
//                .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
//                .addPlayStore("com.ideashower.readitlater.pro")
//                .addInstagram("medyo80")
//                .addGitHub("medyo")
                .create();

        setContentView(aboutPage);



    }




    void simulateDayNight(int currentSetting) {
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }


}
