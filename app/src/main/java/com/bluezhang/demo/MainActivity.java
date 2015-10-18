package com.bluezhang.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements PlatformActionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ShareSDK.initSDK(this);
        setContentView(R.layout.activity_main);
        showShare();
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("normal");
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("normal");
        MobclickAgent.onPause(this);
    }

    private void showShare() {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl("http://a.hiphotos.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=167af1f4d6160924c828aa49b56e5e9f/6a63f6246b600c3327c3486a1c4c510fd8f9a1de.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        //设置是否显示内容编辑框 设置是不是允许编辑
        oks.setSilent(false);
        oks.setCallback(this);
        //设置分享只使用一个平台进行分享的操作
        //oks.setPlatform(SinaWeibo.NAME);

// 启动分享GUI
        oks.show(this);
    }


    public void btnShare(View view) {
        showShare();
    }

    /**
     * 执行成功
     * i 参数 使用Platform。Action_XXX
     * @param platform
     * @param i  代表当前的操作  是分享还是获取用户信息
     * @param hashMap
     */
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        //TODO 处理成功的情况
        String pName = platform.getName();
        //获取当前用户授权的数据

        if(i == platform.ACTION_USER_INFOR) {
            PlatformDb db = platform.getDb();
            String userName = db.getUserName();
            String userId = db.getUserId();
            String userIcon = db.getUserIcon();
            Log.d("MainActivity", "userName = " + userName);
            Log.d("MainActivity", "userId = " + userId);
            Log.d("MainActivity", "userIcon = " + userIcon);
        }else if(i == platform.ACTION_AUTHORIZING){
            //获取用户ID的方式的第三方登录
            PlatformDb db = platform.getDb();
            db.getUserId();//获取用户ID
        }else {
            Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }

    /**
     * 获取用户资料
     * @param view
     */
    public void btnUserInfo(View view) {

        //第一步获取指定的平台通过字符串获取
        Platform platform = ShareSDK.getPlatform(this,SinaWeibo.NAME);

        //设置获取用户信息完成时后的接口和上面的是一样的
        platform.setPlatformActionListener(this);
        //platform showUser(String account) 获取制定平台下 指定用户信息
        //当account是空的时候代表获取当钱授权用户信息
        platform.showUser(null);
    }

    /**
     * 获取Id方式的登陆 ，服务器没有用户系统
     * @param view
     */

    public void shouQuan(View view) {

        Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
        platform.setPlatformActionListener(this);
        //进行用户的授权 授权的时候获取用户的ID
        platform.authorize();

    }


    public void more(View view) {
        Intent intent = new Intent(this,MoreActivity.class);
        startActivity(intent);
    }
}
