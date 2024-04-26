package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    doLogin(username, password);
                }
            }
        });
    }

    private void doLogin(String username, String password) {
        System.out.println("delog调用了");
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = null;
        try {
            formBody = new FormBody.Builder()
                    .add("user_name", URLEncoder.encode(username, "UTF-8"))
                    .add("user_password", URLEncoder.encode(password, "UTF-8"))
                    .build();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        Request request = new Request.Builder()
                .url("http://120.26.205.81:9000")
                .post(formBody)
                .build();
        /*Request request = new Request.Builder()
                .url("https://120.26.205.81:9000")
                .get()
                .build();*/

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                final String message = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求失败: " + message, Toast.LENGTH_SHORT).show();
                        System.out.println("请求失败: " + message);
                    }
                });
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseString = response.body().string();  // 获取并打印出响应体
                    System.out.println("Response: " + responseString);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println("发送成功");
                                Toast.makeText(MainActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else {
                    System.out.println(response.code());
                }
            }
        });
    }
}