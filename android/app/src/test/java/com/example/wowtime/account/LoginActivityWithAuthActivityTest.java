package com.example.wowtime.account;

import com.example.wowtime.BuildConfig;
import com.example.wowtime.ui.account.LoginActivityWithAuthActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

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
    }
}
