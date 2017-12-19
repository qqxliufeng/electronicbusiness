package com.android.ql.lf.electronicbusiness.ui.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import com.android.ql.lf.electronicbusiness.R;

import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by lf on 2017/11/22 0022.
 *
 * @author lf on 2017/11/22 0022
 */

/**
 * @author lf
 */
public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    /**
     * 权限标识
     */
    private static final int WRITE_AND_CAMERA = 0;

    /**
     * 需要的权限
     */
    private static final String[] REQUEST_PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final String[] REQUEST_PERMISSIONS_DESCRIPTION = new String[]{"相机", "读取SD卡"};

    @BindView(R.id.mIvSplash)
    ImageView iv_splash;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash_layout;
    }

    @Override
    public void initView() {
        if (hasPermissions()) {
            iv_splash.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMainActivity();
                }
            }, 2500);
        } else {
            requestPermission();
        }
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        EasyPermissions.requestPermissions(this, "需要相机和存储权限", WRITE_AND_CAMERA, REQUEST_PERMISSIONS);
    }

    /**
     * 检测是否有相应的权限
     *
     * @return true if all permissions already have granted ,otherwise false
     */
    private boolean hasPermissions() {
        return EasyPermissions.hasPermissions(this, REQUEST_PERMISSIONS);
    }

    /**
     * 请求权限回调的方法
     *
     * @param requestCode  请求code
     * @param permissions  权限列表
     * @param grantResults 请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 设置好权限回调的方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (hasPermissions()) {
                startMainActivity();
            } else {
                requestPermission();
            }
        }
    }

    /**
     * 所有的权限都同意回调的方法
     *
     * @param requestCode 请求code
     * @param perms       请求权限列表
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        startMainActivity();
    }

    /**
     * 所有有权限都已经请求到了，直接进入到主页面
     */
    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * 权限拒绝回调方法
     *
     * @param requestCode 请求code
     * @param perms       请求权限列表
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri packageURI = Uri.parse("package:" + getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    startActivityForResult(intent, AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setTitle("系统需要以下权限").setItems(REQUEST_PERMISSIONS_DESCRIPTION, null).create().show();

        } else {
            requestPermission();
        }
    }
}
