package com.erp.checking

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import kotlinx.android.synthetic.main.scan_act.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.io.FileNotFoundException

class ScanQRCodeActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    companion object {
        private const val PICK_PHOTO = 1441
    }

    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scan_act)
        initListener()

        //Scan properties
        qrCodeScanner.setFormats(listOf(BarcodeFormat.QR_CODE))
        qrCodeScanner.setAutoFocus(true)
    }

    override fun onResume() {
        super.onResume()
        qrCodeScanner.setResultHandler(this)
        qrCodeScanner.startCamera()
    }

    override fun onPause() {
        super.onPause()
        qrCodeScanner.stopCamera()
    }

    override fun handleResult(p0: Result?) {
        if (p0 != null) {
            qrCodeScanner.stopCamera()
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(MainActivity.QR_CODE_DATA, p0.toString())
                Log.d(" p0.toString()", p0.toString())
            })
            finish()
        }
    }

    private fun initListener() {
        imgFlash.setOnClickListener {
            qrCodeScanner.flash = !qrCodeScanner.flash
        }
        imgClose.setOnClickListener {
            qrCodeScanner.stopCamera()
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        imgSelect.setOnClickListener {
            var intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_PICK
            }
            intent = Intent.createChooser(intent, "Select Photo")
            startActivityForResult(intent, PICK_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri: Uri =
                if (data.data.toString().startsWith("content://com.google.android.apps.photos.content")) {
                    Uri.parse(data.data.toString())
                } else {
                    data.data
                }
            try {
                val inputStream = this.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                if (bitmap == null) {
                    Log.e(TAG, "Uri is not a bitmap $uri")
                    Toast.makeText(this, R.string.not_recognized, Toast.LENGTH_LONG).show()
                } else {
                    val width = bitmap.width
                    val height = bitmap.height
                    val pixels = IntArray(width * height)
                    bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
                    bitmap.recycle()
                    val source = RGBLuminanceSource(width, height, pixels)
                    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
                    val reader = MultiFormatReader()
                    try {
                        val result = reader.decode(binaryBitmap)
                        handleResult(result)
                    } catch (e: NotFoundException) {
                        Log.e(TAG, "Decode exception $e")
                        Toast.makeText(this, R.string.not_recognized, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: FileNotFoundException) {

            }
        }
    }
}
