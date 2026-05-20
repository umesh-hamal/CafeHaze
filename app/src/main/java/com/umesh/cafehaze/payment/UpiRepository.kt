package com.umesh.cafehaze.payment

import javax.inject.Inject

class UpiRepository @Inject constructor() {

    fun buildUpiLink(
        upiId: String,
        name: String,
        amount: Double,
        note: String
    ): String {
        return "upi://pay?pa=$upiId&pn=$name&am=$amount&cu=INR&tn=$note"
    }
}