package com.umesh.cafehaze.model.data

data class BillWithItems(
    val bill: Bill,
    val items: List<BillItem>
)