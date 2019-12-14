package com.example.newspaper_1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    private EditText UserNameEnrol;
    private EditText PasswordEnrol;
    private ImageButton register_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserNameEnrol = findViewById(R.id.register_name);
        PasswordEnrol = findViewById(R.id.register_password);
        register_back = findViewById(R.id.register_back);

        setEditTextInputSpace(UserNameEnrol);
        setEditTextInputSpace(PasswordEnrol);

        final MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(Register.this);
        final SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

        /* 注册界面的 注册 按钮的监听 接收数据 检验数据 转到登陆界面 */
        Button btn2 = (Button)findViewById(R.id.register);
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //接收 EditText 中的数据
                String UserName = UserNameEnrol.getText().toString();
                String Password = PasswordEnrol.getText().toString();

                //检查输入数据
                if (TextUtils.isEmpty(UserName)){
                    Toast.makeText(Register.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(Password)){
                    Toast.makeText(Register.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    int find = 0;
                    MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(Register.this);
                    SQLiteDatabase database = dataBaseHelper.getReadableDatabase(); //打开数据库
                    Cursor cursor = database.query("user", new String[]{"username"}, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            String username = cursor.getString(cursor.getColumnIndex("username"));
                            if (UserName.equals(username)){
                                find = 1;
                                break;
                            }

                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    if (find == 0) {
                        //向表中添加数据
                        ContentValues values = new ContentValues();
                        values.put("username", UserName);
                        values.put("password", Password);
                        database.insert("user", null, values);
                        values.clear();
                        database.close();
                        Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (find == 1){
                        Toast.makeText(Register.this,"该用户名已存在", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //返回功能
        register_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
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
