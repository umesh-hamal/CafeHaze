package com.umesh.cafehaze.utils.qr

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import javax.inject.Inject

class QrGenerator @Inject constructor() {

    fun generate(
        data: String,
        size: Int = 512
    ): Bitmap {

        val bits = QRCodeWriter().encode(
            data,
            BarcodeFormat.QR_CODE,
            size,
            size
        )

        val bitmap = createBitmap(
            size,
            size,
            Bitmap.Config.ARGB_8888
        )

        for (x in 0 until size) {

            for (y in 0 until size) {

                bitmap[x, y] =
                    if (bits[x, y]) {
                        Color.BLACK
                    } else {
                        Color.WHITE
                    }
            }
        }

        return bitmap
    }
}