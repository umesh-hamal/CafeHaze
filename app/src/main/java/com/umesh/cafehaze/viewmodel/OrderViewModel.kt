package com.umesh.cafehaze.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umesh.cafehaze.model.data.BillItem
import com.umesh.cafehaze.model.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class BillStatus {
    PENDING,
    PROCESSING,
    PAID
}

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    var currentBillId by mutableIntStateOf(1)
        private set

    var bills by mutableStateOf<Map<Int, List<BillItem>>>(emptyMap())
        private set

    var billStatus by mutableStateOf<Map<Int, BillStatus>>(emptyMap())
        private set

    var billPaymentMethod by mutableStateOf<Map<Int, String>>(emptyMap())
        private set

    val currentOrders: List<BillItem>
        get() = bills[currentBillId] ?: emptyList()

    val currentTotal: Double
        get() = currentOrders.sumOf { it.priceAtTime * it.quantity }

    init {
        bills = mapOf(1 to emptyList())
        billStatus = mapOf(1 to BillStatus.PENDING)
        currentBillId = 1
    }

    fun createNewBill() {
        val billId = (bills.keys.maxOrNull() ?: 0) + 1
        bills = bills + (billId to emptyList())
        billStatus = billStatus + (billId to BillStatus.PENDING)
        currentBillId = billId
    }

    fun switchBill(billId: Int) {
        if (bills.containsKey(billId)) {
            currentBillId = billId
        }
    }

    fun increaseItem(menuItemId: Int, price: Double) {

        if (billStatus[currentBillId] == BillStatus.PROCESSING) return

        val list = bills[currentBillId]?.toMutableList() ?: mutableListOf()
        val index = list.indexOfFirst { it.menuItemId == menuItemId }

        if (index >= 0) {
            val item = list[index]
            list[index] = item.copy(quantity = item.quantity + 1)
        } else {
            list.add(
                BillItem(
                    menuItemId = menuItemId,
                    quantity = 1,
                    priceAtTime = price
                )
            )
        }

        bills = bills + (currentBillId to list)
    }

    fun decreaseItem(menuItemId: Int) {

        if (billStatus[currentBillId] == BillStatus.PROCESSING) return

        val list = bills[currentBillId]?.toMutableList() ?: return
        val index = list.indexOfFirst { it.menuItemId == menuItemId }

        if (index == -1) return

        val item = list[index]

        if (item.quantity <= 1) {
            list.removeAt(index)
        } else {
            list[index] = item.copy(quantity = item.quantity - 1)
        }

        bills = bills + (currentBillId to list)
    }

    fun confirmAndPay(
        billId: Int,
        paymentMethod: String,
        onSuccess: (Int) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {

        val items = bills[billId] ?: emptyList()

        if (items.isEmpty()) {
            onError("No items")
            return
        }

        if (billStatus[billId] == BillStatus.PROCESSING) return

        billStatus = billStatus + (billId to BillStatus.PROCESSING)

        viewModelScope.launch {
            try {

                val dbBillId = repository.createOrder(items)

                repository.markAsPaid(dbBillId, paymentMethod)

                billPaymentMethod = billPaymentMethod + (billId to paymentMethod)

                billStatus = billStatus + (billId to BillStatus.PAID)

                onSuccess(dbBillId)

                // 🔥 remove bill
                bills = bills - billId
                billStatus = billStatus - billId

                if (bills.isEmpty()) {
                    bills = mapOf(1 to emptyList())
                    billStatus = mapOf(1 to BillStatus.PENDING)
                    currentBillId = 1
                } else {
                    currentBillId = bills.keys.first()
                }

            } catch (e: Exception) {

                // 🔥 revert state
                billStatus = billStatus + (billId to BillStatus.PENDING)

                e.printStackTrace()
                onError(e.message ?: "Something went wrong")
            }
        }
    }

    fun deleteBill(localBillId: Int) {

        if (billStatus[localBillId] == BillStatus.PROCESSING) return

        if (!bills.containsKey(localBillId)) return

        bills = bills - localBillId
        billStatus = billStatus - localBillId

        if (bills.isEmpty()) {
            bills = mapOf(1 to emptyList())
            billStatus = mapOf(1 to BillStatus.PENDING)
            currentBillId = 1
            return
        }

        if (currentBillId == localBillId) {
            currentBillId = bills.keys.first()
        }
    }
}