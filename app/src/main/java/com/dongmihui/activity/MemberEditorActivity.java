package com.dongmihui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dongmihui.R;
import com.dongmihui.api.MyApi;
import com.dongmihui.bean.ApiMessage;
import com.dongmihui.bean.MemberBean;
import com.dongmihui.bean.ProvinceBean;
import com.dongmihui.common.AppContext;
import com.dongmihui.im.DemoHelper;
import com.dongmihui.im.activity.BaseActivity;
import com.dongmihui.im.utils.PreferenceManager;
import com.dongmihui.utils.JsonFileReader;
import com.dongmihui.utils.SpUtils;
import com.dongmihui.utils.TLog;
import com.dongmihui.utils.ToastUtil;
import com.dongmihui.widget.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 编辑个人信息的界面
 */
public class MemberEditorActivity extends BaseActivity {
    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;

    //更改的名字
    private static final int CHANGENAME = 1000;

    //更改的名字
    private static final int CHANGEBEIEF = 1003;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    private static final int CHANGEJOB = 1100;

    String avatar;

    //  省份
    ArrayList<ProvinceBean> provinceBeanList = new ArrayList<>();
    //  城市
    ArrayList<String> cities;
    ArrayList<List<String>> cityList = new ArrayList<>();
    //  区/县
    ArrayList<String> district;
    ArrayList<List<String>> districts;
    ArrayList<List<List<String>>> districtList = new ArrayList<>();
    @Bind(R.id.im_back)
    ImageView imBack;
    @Bind(R.id.im_avatar)
    CircleImageView imAvatar;
    @Bind(R.id.layout_user_name)
    RelativeLayout layoutUserName;
    @Bind(R.id.layout_user_sex)
    RelativeLayout layoutUserSex;
    @Bind(R.id.layout_user_job)
    RelativeLayout layoutUserJob;
    @Bind(R.id.layout_user_city)
    RelativeLayout layoutUserCity;
    @Bind(R.id.layout_user_brief)
    RelativeLayout layoutUserBrief;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.tv_user_sex)
    TextView tvUserSex;
    @Bind(R.id.tv_user_job)
    TextView tvUserJob;
    @Bind(R.id.im_user_job)
    ImageView imUserJob;
    @Bind(R.id.tv_user_city)
    TextView tvUserCity;
    @Bind(R.id.tv_user_brief)
    TextView tvUserBrief;
    @Bind(R.id.btn_update)
    Button update;
    private String newName;
    private String newSex;
    private Uri uri;
    private int type;
    //调用照相机返回图片临时文件
    private File tempFile;
    private MyApi api = new MyApi();
    OptionsPickerView pvOptions;
    private String newJob;

    ProgressDialog pd;
    private int anInt;

    public static void startMemberEditorActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, MemberEditorActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_editor);
        pvOptions = new OptionsPickerView(this);
        createCameraTempFile(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        initPicker();

    }

    private void initPicker() {


        //  获取json数据
        String province_data_json = JsonFileReader.getJson(this, "province_data.json");
        //  解析json数据
        parseJson(province_data_json);

        //  设置三级联动效果
        pvOptions.setPicker(provinceBeanList, cityList, districtList, true);


        //  设置选择的三级单位
        //pvOptions.setLabels("省", "市", "区");
        //pvOptions.setTitle("选择城市");

        //  设置是否循环滚动
        pvOptions.setCyclic(false, false, false);


        // 设置默认选中的三级项目
        pvOptions.setSelectOptions(0, 0, 0);
        //  监听确定选择按钮
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String city = provinceBeanList.get(options1).getPickerViewText();
                String address;
                //  如果是直辖市或者特别行政区只设置市和区/县
                if ("北京市".equals(city) || "上海市".equals(city) || "天津市".equals(city) || "重庆市".equals(city) || "澳门".equals(city) || "香港".equals(city)) {
                    address = provinceBeanList.get(options1).getPickerViewText()
                            + " " + districtList.get(options1).get(option2).get(options3);
                } else {
                    address = provinceBeanList.get(options1).getPickerViewText()
                            + " " + cityList.get(options1).get(option2)
                            + " " + districtList.get(options1).get(option2).get(options3);
                }
                tvUserCity.setText(address);
            }
        });

    }
    //  解析json填充集合
    public void parseJson(String str) {
        try {
            //  获取json中的数组
            JSONArray jsonArray = new JSONArray(str);
            //  遍历数据组
            for (int i = 0; i < jsonArray.length(); i++) {
                //  获取省份的对象
                JSONObject provinceObject = jsonArray.optJSONObject(i);
                //  获取省份名称放入集合
                String provinceName = provinceObject.getString("name");

                provinceBeanList.add(new ProvinceBean(provinceName));
                //  获取城市数组
                JSONArray cityArray = provinceObject.optJSONArray("city");
                cities = new ArrayList<>();//   声明存放城市的集合
                districts = new ArrayList<>();//声明存放区县集合的集合
                //  遍历城市数组
                for (int j = 0; j < cityArray.length(); j++) {
                    //  获取城市对象
                    JSONObject cityObject = cityArray.optJSONObject(j);
                    //  将城市放入集合
                    String cityName = cityObject.optString("name");
                    cities.add(cityName);
                    district = new ArrayList<>();// 声明存放区县的集合
                    //  获取区县的数组
                    JSONArray areaArray = cityObject.optJSONArray("area");
                    //  遍历区县数组，获取到区县名称并放入集合
                    for (int k = 0; k < areaArray.length(); k++) {
                        String areaName = areaArray.getString(k);
                        district.add(areaName);
                    }
                    //  将区县的集合放入集合
                    districts.add(district);
                }
                //  将存放区县集合的集合放入集合
                districtList.add(districts);
                //  将存放城市的集合放入集合
                cityList.add(cities);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initView() {
        if(pd==null){
            pd=new ProgressDialog(MemberEditorActivity.this);
        }
        pd.setCanceledOnTouchOutside(true);
        pd.setMessage("加载中。。。");
        pd.show();
        anInt = SpUtils.getInt(this, SpUtils.USER_ID);
        api.getMember(anInt, new Callback<MemberBean>() {
            @Override
            public void onResponse(Call<MemberBean> call, Response<MemberBean> response) {
                MemberBean body = response.body();
                if (body.getCode() == 0) {
                    ToastUtil.showShort(AppContext.getInstance(),"网络异常");
                    pd.dismiss();
                } else if (body.getCode() == 1) {
                    tvUserName.setText(body.result.getUserName());
                    tvUserSex.setText(body.result.getSex());
                    tvUserJob.setText(body.result.getJobs());
                    tvUserCity.setText(body.result.home);
                    tvUserBrief.setText(body.result.getDesc());
                    avatar = body.result.getAvatar();
                    Glide.with(AppContext.getInstance())
                            .load(body.result.getAvatar())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.im_member_head)
                            .into(imAvatar);
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MemberBean> call, Throwable t) {
                ToastUtil.showShort(AppContext.getInstance(),"网络异常");
                pd.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {

            Log.d("MmberFragment", requestCode + "");
            switch (requestCode) {
                case CHANGENAME:
                    this.newName = data.getStringExtra("newName");
                    Log.d("MmberFragment", "NewName:" + newName + this.newName);
//                    setupText();
                    tvUserName.setText(newName);
                    break;
                case CHANGEJOB:
                    newJob = data.getStringExtra("newJob");
                    tvUserJob.setText(newJob);
                    break;
                case CHANGEBEIEF:
                    String newDec = data.getStringExtra("newDec");
                    tvUserBrief.setText(newDec);
                    break;
                case REQUEST_CAPTURE: //调用系统相机返回
                    gotoClipActivity(Uri.fromFile(tempFile));
                    break;
                case REQUEST_PICK:  //调用系统相册返回
                    Uri uri = data.getData();
                    gotoClipActivity(uri);
                    break;
                case REQUEST_CROP_PHOTO:  //剪切图片返回
                    final Uri uri1 = data.getData();
                    Log.d("URL", uri1.toString());
                    if (uri1 == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri1);
                    api.putFiel(anInt,cropImagePath, new Callback<ApiMessage<List<String>>>() {
                        @Override
                        public void onResponse(Call<ApiMessage<List<String>>> call, Response<ApiMessage<List<String>>> response) {
                            ApiMessage<List<String>> avatarBody = response.body();
                            if(response.body().getCode()==1){
                                avatar=avatarBody.getResult().get(0);
                                ToastUtil.showShort(AppContext.getInstance(),"头像修改完成");
                                //将头像存入本地
                                PreferenceManager.getInstance().setCurrentUserAvatar(avatar);
                                //通知更新
                                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                            }


                        }

                        @Override
                        public void onFailure(Call<ApiMessage<List<String>>> call, Throwable t) {
                            ToastUtil.showShort(AppContext.getInstance(),"头像修改失败");
                        }
                    });
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    if (type == 1) {
                        imAvatar.setImageBitmap(bitMap);
                    }else{
                        imAvatar.setImageBitmap(bitMap);
                    }


                    break;
            }

        }
    }

    @OnClick({R.id.btn_update,R.id.im_back, R.id.im_avatar, R.id.layout_user_name, R.id.layout_user_sex, R.id.layout_user_job, R.id.layout_user_city, R.id.layout_user_brief})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.im_avatar:
                type = 1;
                uploadHeadImage();
                break;
            case R.id.layout_user_name:
                startActivityForResult(new Intent(this, ChangeNameActivity.class), CHANGENAME);
                break;
            case R.id.layout_user_sex:
                sexDialog();
                break;
            case R.id.layout_user_job:
                startActivityForResult(new Intent(this, JobSetActivity.class), CHANGEJOB);
                break;
            case R.id.layout_user_city:
                pvOptions.show();
                break;
            case R.id.layout_user_brief:
                startActivityForResult(new Intent(this, BriefInputActivity.class), CHANGEBEIEF);
                break;
            case R.id.btn_update:
                update();

                break;
        }
    }

    /**
     * 提交用户的信息到服务器
     */

    private void update() {
        if(pd==null){
            pd=new ProgressDialog(MemberEditorActivity.this);
        }
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("加载中。。。");
        pd.show();
        String name = tvUserName.getText().toString();
        String sex = tvUserSex.getText().toString();
        String job = tvUserJob.getText().toString();
        String city = tvUserCity.getText().toString();



        String desc =tvUserBrief.getText().toString();

        api.setAboutMember(anInt, name, sex,job ,city,desc ,avatar, new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String body = response.body();
                        ToastUtil.showShort(AppContext.getInstance(),"修改完成");
                        DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                        pd.dismiss();
                        finish();
//                        ToastUtil.showShort(AppContext.getInstance(),body.getCode());
//                        if(body.getCode()==0){
//                            ToastUtil.showShort(AppContext.getInstance(),"失败");
//                        }else if(body.getCode()==1){
//                            ToastUtil.showShort(AppContext.getInstance(),"成功");
//                        }
                        Log.d("LOG", body);

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        ToastUtil.showShort(AppContext.getInstance(),"修改失败");
                        pd.dismiss();
                    }
                });
    }

    //选择性别的对话框
    private void sexDialog() {
        final String items[] = {"男", "女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("请选择性别"); //设置标题
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
                //Toast.makeText(MemberEditorActivity.this, items[which], Toast.LENGTH_SHORT).show();
                newSex = items[which];
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //Toast.makeText(MemberEditorActivity.this, "确定", Toast.LENGTH_SHORT).show();
                tvUserSex.setText(newSex);
            }
        });
        builder.create().show();
    }


    /**
     * 从本地上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景全透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 1f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到调用系统相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(intent, REQUEST_CAPTURE);
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到调用系统图库
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }


    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {

            TLog.log("uri" + "++++++++++++++");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }


    /**
     * Try to return the absolute file path from the given Uri  兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
