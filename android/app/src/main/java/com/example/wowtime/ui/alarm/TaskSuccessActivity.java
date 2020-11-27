package com.example.wowtime.ui.alarm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.wowtime.R;

public class TaskSuccessActivity extends AppCompatActivity {

    Button btn_shot_and_share;
    TextView successView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_success_activity);

        Button back_to_home = findViewById(R.id.btn_back_to_home);
        successView = findViewById(R.id.successText);
        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_shot_and_share = findViewById(R.id.ScreenShotAndShare);
        btn_shot_and_share.setOnClickListener(v->share(this));

    }
    public static void share(Activity activity){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        Uri u = Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), takeScreenShot(activity), null,null));//将截图bitmap存系统相册
        intent.putExtra(Intent.EXTRA_STREAM, u);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, ""));
    }
    /**
     * Activity截屏
     * */
    public static Bitmap takeScreenShot(Activity pActivity) {
        View view = pActivity.getWindow().getDecorView();
        // 设置是否可以进行绘图缓存
        view.setDrawingCacheEnabled(true);
        // 如果绘图缓存无法，强制构建绘图缓存
        view.buildDrawingCache();
        // 返回这个缓存视图
        Bitmap bitmap = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        // 测量屏幕宽和高
        view.getWindowVisibleDisplayFrame(frame);
        int stautsHeight = frame.top;
        Point point = new Point();
        pActivity.getWindowManager().getDefaultDisplay().getSize(point);
        int width = point.x ;
        int height = point.y;
        // 根据坐标点和需要的宽和高创建bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, stautsHeight, width, height - stautsHeight);
        return bitmap;
    }
}