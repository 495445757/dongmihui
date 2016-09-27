package com.dongmihui.activity;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.dongmihui.R;
import com.dongmihui.im.DemoModel;
import com.dongmihui.im.activity.BaseActivity;
import com.dongmihui.utils.TLog;
import com.dongmihui.utils.ToastUtil;
import com.dongmihui.widget.CircleImageView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
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
    private String newName;
    private String newSex;
    private Uri uri;
    private int type;
    //调用照相机返回图片临时文件
    private File tempFile;
    OptionsPickerView pvOptions;
    public static void startMemberEditorActivity(Activity activity, String name) {
        if (activity != null) {
            Intent intent = new Intent(activity, MemberEditorActivity.class);
            intent.putExtra("newName", name);
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

    }



    private void initView() {
        String newName = getIntent().getExtras().getString("newName");
        DemoModel model = new DemoModel(this);
        tvUserName.setText(newName);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==RESULT_OK){
            switch (requestCode){
                case CHANGENAME:
                    newName = data.getExtras().getString("newName");
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
                        if (uri1 == null) {
                            return;
                        }
                        String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri1);
                        Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                        if (type == 1) {
                            imAvatar.setImageBitmap(bitMap);
                        }
                        //此处后面可以将bitMap转为二进制上传后台网络
                        //......

                    break;
            }
        }
    }

    @OnClick({R.id.im_back, R.id.im_avatar, R.id.layout_user_name, R.id.layout_user_sex, R.id.layout_user_job, R.id.layout_user_city, R.id.layout_user_brief})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.im_avatar:
                type=1;
                uploadHeadImage();
                break;
            case R.id.layout_user_name:
                startActivity(new Intent(this, ChangeNameActivity.class));
                break;
            case R.id.layout_user_sex:
               sexDialog();
                break;
            case R.id.layout_user_job:
                break;
            case R.id.layout_user_city:
                pvOptions.show();
                break;
            case R.id.layout_user_brief:
                startActivity(new Intent(this, BriefInputActivity.class));
                break;
        }
    }
    //选择性别的对话框
    private void sexDialog(){
        final String items[]={"男","女"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("请选择性别"); //设置标题
        builder.setSingleChoiceItems(items,-1,new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
                //Toast.makeText(MemberEditorActivity.this, items[which], Toast.LENGTH_SHORT).show();
                newSex = items[which];
            }
        });
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(MemberEditorActivity.this, "确定", Toast.LENGTH_SHORT).show();
               setupText();
            }
        });
        builder.create().show();
    }



    private void setupText() {

        tvUserSex.setText(newSex);
    }

    /**
     * 上传头像
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
        //popupWindow在弹窗的时候背景半透明
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
            ToastUtil.showShort(this,"meijinqu");
            TLog.log("uri"+"++++++++++++++");
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
