package com.chinshry.application

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chinshry.application.bean.Router
import com.chinshry.application.util.DevicesCheckUtil
import com.chinshry.application.util.GlideEngine
import com.luck.picture.lib.PictureMediaScannerConnection
import com.luck.picture.lib.PictureSelectionModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.tools.MediaUtils
import kotlinx.android.synthetic.main.activity_test.*
import com.luck.picture.lib.thread.PictureThreadUtils
import kotlinx.android.synthetic.main.tab_item_layout.view.*
import java.io.File


@Route(path = Router.ACTIVITY_TEST)
class TestActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TestActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initView()
    }

    @SuppressLint("InflateParams")
    private fun initView() {
        for (index in 0..3) {
            val childView = LayoutInflater.from(this).inflate(R.layout.tab_item_layout, null)
            childView.rootView.item_text.text = "aaa" + index.toString()
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
            ll_list.addView(childView, layoutParams)
        }

        btn_test.setOnClickListener {
            val oldTimeStamp = 1638876626000
            val day = TimeUtils.getTimeSpanByNow(TimeUtils.getNowMills(), TimeConstants.DAY)
            println("chengshu day = " + day)
        }

        btn_root.setOnClickListener {
            if (!DevicesCheckUtil.checkRootAndEmulator(this)) {
                Toast.makeText(this, "您的设备未root，且为真机", Toast.LENGTH_LONG).show()
            }
        }

        btn_gallery.setOnClickListener {
            PermissionUtils.permission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback { isAllGranted, _, _, _ ->
                    if (isAllGranted) {
                        gallerySelect()
                        return@callback
                    }
                }
                .request()
        }

        btn_camera.setOnClickListener {
            PermissionUtils.permission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)
                .callback { isAllGranted, _, _, _ ->
                    if (isAllGranted) {
                        takePicture()
                        return@callback
                    }
                }
                .request()
        }

        btn_clear_cache.setOnClickListener {
            clearCache()
        }
    }

    /**
     * 创建自定义拍照输出目录
     *
     * @return
     */
    private fun createCustomCameraOutPath(): String {
        val customFile = getCacheFilePath() ?: return ""
        if (!customFile.exists()) {
            customFile.mkdirs()
        }
        return customFile.absolutePath + File.separator
    }

    private fun getCacheFilePath(): File? {
        val externalFilesDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        externalFilesDir?.absolutePath?.let {
            return File(it)
        }
        return null
    }

    private fun clearCache() {
        val dirPictures: File = getCacheFilePath() ?: return
        val files = dirPictures.listFiles() ?: return

        for (file in files) {
            if (file.isFile) {
                val isResult = file.delete()
                if (isResult) {
                    PictureThreadUtils.runOnUiThread {
                        PictureMediaScannerConnection(
                            this,
                            file.absolutePath
                        )
                    }
                }
            }
        }

    }

    private fun gallerySelect() {
        val gallerySelector = PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.createGlideEngine())
            .selectionMode(PictureConfig.SINGLE) // 单选
            .isPageStrategy(true, false) // 开启分页模式，默认开启另提供两个参数；pageSize每页总数；isFilterInvalidFile是否过滤损坏图片
            .isCamera(false) // 列表是否显示拍照按钮

        setCropConfig(gallerySelector)
    }

    private fun takePicture() {
        // 单独拍照
        val cameraSelector = PictureSelector.create(this)
            .openCamera(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.createGlideEngine())
            .setOutputCameraPath(createCustomCameraOutPath()) // 自定义相机输出目录

        setCropConfig(cameraSelector)
    }

    private fun setCropConfig(pictureSelectionModel: PictureSelectionModel) {
        pictureSelectionModel
            .isEnableCrop(true) //是否开启裁剪
            .cropImageWideHigh(200, 200) // 裁剪宽高比，设置如果大于图片本身宽高则无效
            .withAspectRatio(1, 1) // 裁剪比例
            .setCropDimmedColor(ColorUtils.getColor(R.color.half_black))// 设置圆形裁剪背景色值
            // .freeStyleCropMode(OverlayView.FREESTYLE_CROP_MODE_ENABLE_WITH_PASS_THROUGH) // 裁剪框拖动模式
            // .isCropDragSmoothToCenter(true) // 裁剪框拖动时图片自动跟随居中
            .forResult(MyResultCallback(this, iv_preview))
    }

    /**
     * 返回结果回调
     */
    private class MyResultCallback(val activity: Activity, val imageView: ImageView) :
        OnResultCallbackListener<LocalMedia> {

        private fun showPreview(media: LocalMedia) {
            val path: String = if (media.isCut && !media.isCompressed) {
                // 裁剪过
                media.cutPath
            } else if (media.isCut || media.isCompressed) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                media.compressPath
            } else {
                // 原图
                media.path
            }

            Glide.with(activity)
                .load(
                    if (PictureMimeType.isContent(path) && !media.isCut && !media.isCompressed)
                        Uri.parse(path)
                    else
                        path
                )
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }

        override fun onResult(result: List<LocalMedia>) {
            Log.i(TAG, "返回数据: " + JSON.toJSONString(result))

            for (media in result) {
                if (media.width == 0 || media.height == 0) {
                    if (PictureMimeType.isHasImage(media.mimeType)) {
                        val imageExtraInfo = MediaUtils.getImageSize(media.path)
                        media.width = imageExtraInfo.width
                        media.height = imageExtraInfo.height
                    }
                }

                showPreview(media)

                Log.i(TAG, "文件名: " + media.fileName)
                Log.i(TAG, "是否压缩:" + media.isCompressed)
                Log.i(TAG, "压缩:" + media.compressPath)
                Log.i(TAG, "原图:" + media.path)
                Log.i(TAG, "绝对路径:" + media.realPath)
                Log.i(TAG, "是否裁剪:" + media.isCut)
                Log.i(TAG, "裁剪:" + media.cutPath)
                Log.i(TAG, "是否开启原图:" + media.isOriginal)
                Log.i(TAG, "原图路径:" + media.originalPath)
                Log.i(TAG, "Android Q 特有Path:" + media.androidQToPath)
                Log.i(TAG, "宽高: " + media.width + "x" + media.height)
                Log.i(TAG, "Size: " + media.size)
                Log.i(TAG, "onResult: $media")
            }

        }

        override fun onCancel() {
            Log.i(TAG, "PictureSelector Cancel")
        }

    }

}