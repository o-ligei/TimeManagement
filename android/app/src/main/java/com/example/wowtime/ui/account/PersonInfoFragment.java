package com.example.wowtime.ui.account;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.wowtime.R;
import com.example.wowtime.service.Accumulation;
import com.example.wowtime.util.Ajax;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;
import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class PersonInfoFragment extends Fragment {

    TextView usernameText;
    TextView usernameTitle;
    TextView emailText;
    TextView creditText;
    TextView phoneNumberText;
    ImageView icon;
    TextView forgetPassword;
    ImageView modifyEmail;
    TextView loginText;

    //    ConstraintLayout passwordLayout;
//    ConstraintLayout emailLayout;
//    ConstraintLayout creditLayout;
    public PersonInfoFragment() {}

    public PersonInfoFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.person_info_activity, container, false);

        icon = root.findViewById(R.id.UserIcon);
        forgetPassword = root.findViewById(R.id.resetPassword);
        modifyEmail = root.findViewById(R.id.modifyEmail);
        creditText = root.findViewById(R.id.Credit);
        usernameText = root.findViewById(R.id.Username);
        usernameTitle = root.findViewById(R.id.UsernameTitle);
        emailText = root.findViewById(R.id.Email);
        phoneNumberText = root.findViewById(R.id.PhoneNumber);
        loginText = root.findViewById(R.id.textLoginFirst);

        if (UserInfoAfterLogin.userid != -1) { loginText.setVisibility(View.INVISIBLE); } else {
            loginText.setOnClickListener(v -> startActivity(
                    new Intent(getActivity(), LoginActivityWithAuthActivity.class)));
            return root;
        }

        forgetPassword.setOnClickListener(v -> startActivity(
                new Intent(getActivity(), CaptchaConfirmActivity.class)
                        .putExtra("target", "password")));
        modifyEmail.setOnClickListener(v -> startActivity(
                new Intent(getActivity(), CaptchaConfirmActivity.class)
                        .putExtra("target", "email")));
        Accumulation accumulation = new Accumulation(requireContext());
        creditText.setText(String.valueOf(accumulation.getAccumulation()));
//        creditText.setOnClickListener(v -> startActivity(new Intent(getActivity(), CreditDetailListActivity.class)));
        OKGetProfile();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
//        OKGetProfile();
    }

    private void OKGetProfile() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("userid", String.valueOf(UserInfoAfterLogin.userid));

        Handler handler = new Handler(message -> {
            if (message.what != InternetConstant.FETCH) { return false; }
            String msg = message.getData().get("msg").toString();
            String str_data = message.getData().get("data").toString();
            String username = null;
            String userIcon = null;
            String phone = null;
            String email = null;
            byte[] bytes = null;
            try {
                JSONObject data = new JSONObject(str_data);
                username = data.get("username").toString();
                userIcon = data.get("userIcon").toString();
                phone = data.get("phone").toString();
                email = data.get("email").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inSampleSize = 2;
            if (msg.equals("success")) {
                usernameText.setText(username);
                usernameTitle.setText(username);
                emailText.setText(email);
                phoneNumberText.setText(phone);
                bytes = Base64.decode(userIcon, Base64.DEFAULT);
                icon.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, op));
            }
            if (!emailText.getText().toString().equals("null")) { emailText.setEnabled(false); }

            return false;
        });

        Ajax ajax = new Ajax("/User/GetPersonalProfile", formBody, handler, InternetConstant.FETCH);
        ajax.fetch();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient client = new OkHttpClient();
//                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//                formBody.add("userid", String.valueOf(UserInfoAfterLogin.userid));
//                Request request = new Request.Builder()
//                        .url(InternetConstant.host + "/User/GetPersonalProfile")
//                        .post(formBody.build()).build();
//                try {
//                    Response response = client.newCall(request).execute();//发送请求
//                    String result = response.body().string();
//                    GetProfile(result);
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

//    private void GetProfile(String result) throws JSONException {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                JSONObject jsonObject = null;
//                String msg = null;
//                String str_data = null;
//                String username = null;
//                String userIcon = null;
//                String phone = null;
//                String email = null;
//                byte[] bytes = null;
//                try {
//                    jsonObject = new JSONObject(result);
//                    msg = jsonObject.get("msg").toString();
//                    str_data = jsonObject.get("data").toString();
//                    JSONObject data = new JSONObject(str_data);
//                    username = data.get("username").toString();
//                    userIcon = data.get("userIcon").toString();
////                    bytes = Base64.decode(data.get("userIcon").toString(), Base64.DEFAULT);
//                    phone = data.get("phone").toString();
//                    email = data.get("email").toString();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                BitmapFactory.Options op = new BitmapFactory.Options();
//                op.inSampleSize = 2;
//                if (msg.equals("success")) {
//                    usernameText.setText(username);
//                    usernameTitle.setText(username);
//                    emailText.setText(email);
//                    phoneNumberText.setText(phone);
//                    bytes = Base64.decode(userIcon, Base64.DEFAULT);
//                    icon.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, op));
//                }
//                if (!emailText.getText().toString().equals("null")) { emailText.setEnabled(false); }
//            }
//        });
//    }
}