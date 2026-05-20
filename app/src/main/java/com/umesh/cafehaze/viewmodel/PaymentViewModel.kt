package com.umesh.cafehaze.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.umesh.cafehaze.payment.UpiRepository
import com.umesh.cafehaze.utils.qr.QrGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val upiRepository: UpiRepository,
    private val qrGenerator: QrGenerator
) : ViewModel() {

    fun getQrBitmap(
        amount: Double,
        billId: Int
    ): Bitmap {

        val safeAmount = if (amount <= 0) 1.0 else amount

        val upiLink = upiRepository.buildUpiLink(
            upiId = "umeshhamal880@oksbi",
            name = "CafeHaze",
            amount = safeAmount,
            note = "Bill#$billId"
        )

        return qrGenerator.generate(upiLink)
    }
}