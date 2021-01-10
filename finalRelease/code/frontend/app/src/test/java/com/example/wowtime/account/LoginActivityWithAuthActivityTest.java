package com.example.wowtime.account;

import android.content.Intent;
import com.example.wowtime.BuildConfig;
import com.example.wowtime.R;
import com.example.wowtime.ui.account.LoginActivityWithAuthActivity;
import com.example.wowtime.ui.account.LoginActivityWithPasswordActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.internal.IShadow;
import org.robolectric.shadows.*;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ShadowLog.class},sdk = 23)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*", "org.json.*", "sun.security.*", "javax.net.*"})
public class LoginActivityWithAuthActivityTest {
    @Before
    public void setUp(){
        ShadowLog.stream = System.out;
    }

    @Test
    public void test1(){
        LoginActivityWithAuthActivity loginActivityWithAuthActivity = Robolectric.setupActivity(LoginActivityWithAuthActivity.class);

        loginActivityWithAuthActivity.findViewById(R.id.go_to_another_in_auth).performClick();

        Intent expectedIntent = new Intent(loginActivityWithAuthActivity,
                                           LoginActivityWithPasswordActivity.class);

//        Intent actual = shadowOf(RuntimeEnvironment.application);
    }
}
