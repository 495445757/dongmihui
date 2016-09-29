package com.dongmihui.utils;

/**
 * Created by Administrator on 2016/9/27.
 */

public interface ApiConstant {

//    public static String url = "http://192.168.2.3/";
    public static final String url = "http://dmh2.hnwxq.com/";

    /**
     * 验证邮箱激活码接口
     *  传值
     {
     "email":"1450185529@qq.com",//邮箱
     "activate":"123456987"//激活码
     }
     返回值
     1.成功
     {
     "code":1,
     "msg":"success",
     "result":{
     "userId":2
     }
     }
     2. 失败
     {
     "code": "0",//状态码
     "msg": "错误内容"//状态值
     }
     *
     *
     */
    public static String URL_ACTIVATE = "app/index.php/Member/activate";

    /**
     * 发送手机验证码接口  "phone":"15237164690",
     "symBol":1 //状态码 1注册 2找回密码 3修改手机号
     */
    public static String URL_VERIFY = "app/index.php/Member/verify";

    /**
     * 验证手机号验证码完成激活
     */
    public static String URL_REGISTER = "app/index.php/Member/register";
    /**
     * 登陸接口
     */
    public static String URL_LOGIN = "app/index.php/Member/login";

    /**
     * 找回密码接口(验证手机号验证码)
     */
    public static String URL_PASSVERIFY = "app/index.php/Member/passVerify";
    /**
     * 我的界面的用户信息接口
     */
    public static String URL_MEMBER="app/index.php/Usercenter/userInfo";
    /**
     * 上传用户修改信息
     */
    public static String URL_ABOUTMEMBER="app/index.php/Usercenter/userEdit";
    /**
     * 上传用户头像
     */

    public static String URL_AVATAR ="app/index.php/Usercenter/avatar";
    /**
     * 修改密码
     */

    public static String URL_MODIFYPWD ="app/index.php/Usercenter/passWord";
    /**
     * 修改手机号码
     */

    public static String URL_MODIFYPHONE="app/index.php/Usercenter/phone";
}
