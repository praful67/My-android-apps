package com.dev.praful.trackyourdriver;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.hlab.fabrevealmenu.view.FABRevealMenu;

public class BaseFragment extends AppCompatActivity {

    FABRevealMenu fabMenu;



    public FABRevealMenu getFabMenu() {
        return fabMenu;
    }

    public void setFabMenu(FABRevealMenu fabMenu) {
        this.fabMenu = fabMenu;
    }
}
