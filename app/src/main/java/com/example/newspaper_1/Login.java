package com.example.newspaper_1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private EditText UserNameLogin;
    private EditText PasswordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserNameLogin = findViewById(R.id.login_name);
        PasswordLogin = findViewById(R.id.login_password);

        setEditTextInputSpace(UserNameLogin);
        setEditTextInputSpace(PasswordLogin);

        //控制最大长度
        int maxLengthUserName =12;
        int maxLengthPassword = 12;
        InputFilter[] fArray =new InputFilter[1];
        fArray[0]=new  InputFilter.LengthFilter(maxLengthUserName);
        UserNameLogin.setFilters(fArray);
        InputFilter[] fArray1 =new InputFilter[1];
        fArray1[0]=new  InputFilter.LengthFilter(maxLengthPassword);
        PasswordLogin.setFilters(fArray1);

        //登陆界面 登录 按钮的监听
        Button btn2 = (Button)findViewById(R.id.btn_login);
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //获取输入框中的用户名 UserName 和密码 Password
                String UserName = UserNameLogin.getText().toString();
                String Password = PasswordLogin.getText().toString();

                if (TextUtils.isEmpty(UserName)){
                    Toast.makeText(Login.this,"请输入用户名", Toast.LENGTH_SHORT).show();

                }else if (TextUtils.isEmpty(Password)){
                    Toast.makeText(Login.this, "请输入密码", Toast.LENGTH_SHORT).show();

                }else {

                    MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(Login.this);
                    SQLiteDatabase database = dataBaseHelper.getReadableDatabase(); //打开数据库
                    Cursor cursor = database.query("user", new String[]{"username", "password", "id"}, "username=?", new String[]{UserName}, null, null, null);

                    if (cursor.moveToFirst()) {
                        //如果用户存在，找到表中此用户对应的密码
                        String password_1 = cursor.getString(cursor.getColumnIndex("password"));
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String id_string = String.valueOf(id);
                        //判断密码是否正确
                        if (Password.equals(password_1)){

                            //活动跳转到店铺主页面
                            Toast.makeText(Login.this,"登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Main.class);

                            SharedPreferences sp=getSharedPreferences("user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sp.edit();
                            editor.putString("user_id",id_string);
                            editor.apply();

                            intent.putExtra("user_id", id_string);// 传输数据
                            intent.putExtra("username", UserName);
                            intent.putExtra("password", Password);
                            startActivity(intent);
                            finish();

                        }else {
                            Toast.makeText(Login.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(Login.this, "此用户不存在", Toast.LENGTH_SHORT).show();
                    }

                    cursor.close();
                    database.close();

                }
            }
        });

        //登陆界面 注册 按钮的监听
        Button btn1 = (Button)findViewById(R.id.login_register);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

    }

    //防止空格回车
    public static void setEditTextInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }
}
