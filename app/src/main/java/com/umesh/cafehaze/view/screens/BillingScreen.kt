package com.umesh.cafehaze.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.umesh.cafehaze.model.data.MenuItem
import com.umesh.cafehaze.view.components.ExpandedBillSection
import com.umesh.cafehaze.viewmodel.OrderViewModel
import com.umesh.cafehaze.viewmodel.PaymentViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val BackgroundColor = Color(0xFFF8F6F1)
private val SurfaceColor = Color.White

private val PrimaryText = Color(0xFF1E1B16)
private val SecondaryText = Color(0xFF7A7269)

private val AccentBrown = Color(0xFF8B5E3C)
private val AccentGold = Color(0xFFD6B38A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingScreen(
    orderViewModel: OrderViewModel,
    menuItems: List<MenuItem>,
    paymentViewModel: PaymentViewModel
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    val bills = orderViewModel.bills

    val currentBillId = orderViewModel.currentBillId

    val currentItems = orderViewModel.currentOrders

    val expandedBills = remember {
        mutableStateMapOf<Int, Boolean>()
    }

    var isProcessing by remember {
        mutableStateOf(false)
    }

    var showQrSheet by remember {
        mutableStateOf(false)
    }

    var qrAmount by remember {
        mutableDoubleStateOf(0.0)
    }

    var qrBillId by remember {
        mutableIntStateOf(0)
    }

    var selectedPayment by remember {
        mutableStateOf("ONLINE")
    }

    val menuMap = remember(menuItems) {
        menuItems.associateBy { it.id }
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(showQrSheet) {

        if (showQrSheet) {

            sheetState.show()

            delay(120000)

            showQrSheet = false

        } else {

            sheetState.hide()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),

                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 20.dp
                ),

                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                items(bills.keys.toList()) { billId ->

                    val isSelected = billId == currentBillId

                    val billItems = bills[billId] ?: emptyList()

                    val isExpanded = expandedBills[billId] ?: false

                    val billTotal = billItems.sumOf {
                        it.priceAtTime * it.quantity
                    }

                    val isLocked = showQrSheet && qrBillId == billId

                    Surface(
                        onClick = {

                            if (!showQrSheet) {
                                orderViewModel.switchBill(billId)
                            }
                        },

                        shape = RoundedCornerShape(30.dp),

                        color =
                            if (isSelected)
                                Color(0xFFFFF8F1)
                            else
                                SurfaceColor,

                        tonalElevation =
                            if (isSelected) 10.dp else 3.dp,

                        shadowElevation =
                            if (isSelected) 14.dp else 6.dp,

                        border =
                            if (isSelected)
                                BorderStroke(
                                    1.5.dp,
                                    AccentGold.copy(alpha = 0.7f)
                                )
                            else
                                null,

                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),

                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = "Customer #$billId",

                                        style = MaterialTheme.typography.titleLarge,

                                        fontWeight = FontWeight.Bold,

                                        color = PrimaryText
                                    )

                                    Spacer(
                                        modifier = Modifier.height(6.dp)
                                    )

                                    Text(
                                        text = "₹${"%.0f".format(billTotal)}",

                                        style = MaterialTheme.typography.titleMedium,

                                        fontWeight = FontWeight.SemiBold,

                                        color = AccentBrown
                                    )
                                }

                                Surface(
                                    onClick = {
                                        expandedBills[billId] = !isExpanded
                                    },

                                    shape = CircleShape,

                                    color = Color(0xFFF5EFE7)
                                ) {

                                    Icon(
                                        imageVector =
                                            if (isExpanded)
                                                Icons.Default.KeyboardArrowUp
                                            else
                                                Icons.Default.KeyboardArrowDown,

                                        contentDescription = null,

                                        tint = AccentBrown,

                                        modifier = Modifier
                                            .padding(10.dp)
                                            .size(22.dp)
                                    )
                                }
                            }

                            if (isExpanded) {

                                ExpandedBillSection(
                                    billItems = billItems,

                                    billId = billId,

                                    billTotal = billTotal,

                                    isLocked = isLocked,

                                    menuMap = menuMap,

                                    orderViewModel = orderViewModel,

                                    onShowQr = { amount, id ->

                                        qrAmount = amount

                                        qrBillId = id

                                        showQrSheet = true
                                    },

                                    secondaryText = SecondaryText,

                                    accentBrown = AccentBrown
                                )
                            }
                        }
                    }
                }

                item {

                    AssistChip(
                        onClick = {

                            if (!showQrSheet) {
                                orderViewModel.createNewBill()
                            }
                        },

                        label = {
                            Text("New Customer")
                        },

                        leadingIcon = {
                            Icon(Icons.Default.Add, null)
                        }
                    )
                }
            }

            Column(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
            ) {

                val total = currentItems.sumOf {
                    it.priceAtTime * it.quantity
                }

                Surface(
                    shape = RoundedCornerShape(32.dp),

                    color = Color.White,

                    tonalElevation = 8.dp,

                    shadowElevation = 12.dp,

                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "Bill Summary",

                            style = MaterialTheme.typography.titleLarge,

                            fontWeight = FontWeight.Bold,

                            color = PrimaryText
                        )

                        Spacer(
                            Modifier.height(18.dp)
                        )

                        currentItems.forEach { item ->

                            val name =
                                menuMap[item.menuItemId]?.name ?: "Item"

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),

                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = name,

                                    modifier = Modifier.weight(1f),

                                    color = PrimaryText,

                                    style = MaterialTheme.typography.bodyLarge,

                                    fontWeight = FontWeight.Medium
                                )

                                Surface(
                                    shape = RoundedCornerShape(20.dp),

                                    color = Color(0xFFF3ECE4)
                                ) {

                                    Text(
                                        text = "x${item.quantity}",

                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 5.dp
                                        ),

                                        color = AccentBrown,

                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(
                                Modifier.height(10.dp)
                            )
                        }

                        Spacer(
                            Modifier.height(16.dp)
                        )

                        HorizontalDivider(
                            color = Color(0xFFE8DED2)
                        )

                        Spacer(
                            Modifier.height(18.dp)
                        )

                        Text(
                            text = "Payment Method",

                            style = MaterialTheme.typography.titleMedium,

                            fontWeight = FontWeight.Bold,

                            color = PrimaryText
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        Surface(
                            shape = RoundedCornerShape(28.dp),

                            color = Color(0xFFF5EEE6),

                            border = BorderStroke(
                                1.dp,
                                Color(0xFFE9DDD0)
                            ),

                            tonalElevation = 2.dp,

                            shadowElevation = 4.dp,

                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp),

                                horizontalArrangement =
                                    Arrangement.spacedBy(10.dp)
                            ) {

                                Surface(
                                    onClick = {
                                        selectedPayment = "ONLINE"
                                    },

                                    modifier = Modifier.weight(1f),

                                    shape = RoundedCornerShape(22.dp),

                                    color =
                                        if (selectedPayment == "ONLINE")
                                            AccentBrown
                                        else
                                            Color.Transparent,

                                    tonalElevation =
                                        if (selectedPayment == "ONLINE")
                                            6.dp
                                        else
                                            0.dp,

                                    shadowElevation =
                                        if (selectedPayment == "ONLINE")
                                            10.dp
                                        else
                                            0.dp
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp),

                                        horizontalArrangement =
                                            Arrangement.Center,

                                        verticalAlignment =
                                            Alignment.CenterVertically
                                    ) {

                                        Text(
                                            text = "💳"
                                        )

                                        Spacer(
                                            modifier = Modifier.width(8.dp)
                                        )

                                        Text(
                                            text = "Online",

                                            color =
                                                if (selectedPayment == "ONLINE")
                                                    Color.White
                                                else
                                                    PrimaryText,

                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Surface(
                                    onClick = {
                                        selectedPayment = "OFFLINE"
                                    },

                                    modifier = Modifier.weight(1f),

                                    shape = RoundedCornerShape(22.dp),

                                    color =
                                        if (selectedPayment == "OFFLINE")
                                            AccentBrown
                                        else
                                            Color.Transparent,

                                    tonalElevation =
                                        if (selectedPayment == "OFFLINE")
                                            6.dp
                                        else
                                            0.dp,

                                    shadowElevation =
                                        if (selectedPayment == "OFFLINE")
                                            10.dp
                                        else
                                            0.dp
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp),

                                        horizontalArrangement =
                                            Arrangement.Center,

                                        verticalAlignment =
                                            Alignment.CenterVertically
                                    ) {

                                        Text(
                                            text = "💵"
                                        )

                                        Spacer(
                                            modifier = Modifier.width(8.dp)
                                        )

                                        Text(
                                            text = "Offline",

                                            color =
                                                if (selectedPayment == "OFFLINE")
                                                    Color.White
                                                else
                                                    PrimaryText,

                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(
                            Modifier.height(20.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = "Total",

                                style = MaterialTheme.typography.titleMedium,

                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "₹${"%.2f".format(total)}",

                                style = MaterialTheme.typography.titleMedium,

                                fontWeight = FontWeight.ExtraBold,

                                color = AccentBrown
                            )
                        }
                    }
                }

                Spacer(
                    Modifier.height(18.dp)
                )

                Button(
                    onClick = {

                        if (isProcessing) return@Button

                        isProcessing = true

                        orderViewModel.confirmAndPay(
                            billId = currentBillId,

                            paymentMethod = selectedPayment,

                            onSuccess = { dbBillId ->

                                isProcessing = false

                                showQrSheet = false

                                orderViewModel.createNewBill()

                                scope.launch {

                                    snackbarHostState.showSnackbar(
                                        "Successfully bill created • #$dbBillId"
                                    )
                                }
                            },

                            onError = { error ->

                                isProcessing = false

                                scope.launch {
                                    snackbarHostState.showSnackbar(error)
                                }
                            }
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),

                    shape = RoundedCornerShape(50.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentBrown,
                        contentColor = Color.White
                    ),

                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp
                    ),

                    enabled =
                        currentItems.isNotEmpty() &&
                                !isProcessing
                ) {

                    if (isProcessing) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),

                            strokeWidth = 2.dp,

                            color = Color.White
                        )

                    } else {

                        Text(
                            text = "Confirm & Pay",

                            style = MaterialTheme.typography.titleMedium,

                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (showQrSheet) {

            ModalBottomSheet(
                onDismissRequest = {
                    showQrSheet = false
                },

                sheetState = sheetState,

                shape = RoundedCornerShape(
                    topStart = 34.dp,
                    topEnd = 34.dp
                ),

                containerColor = BackgroundColor
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 24.dp,
                            vertical = 20.dp
                        ),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Scan to Pay",

                        style = MaterialTheme.typography.headlineSmall,

                        fontWeight = FontWeight.Bold,

                        color = PrimaryText
                    )

                    Spacer(
                        Modifier.height(6.dp)
                    )

                    Text(
                        text = "UPI Payment",

                        color = SecondaryText
                    )

                    Spacer(
                        Modifier.height(26.dp)
                    )

                    Surface(
                        shape = RoundedCornerShape(32.dp),

                        color = SurfaceColor,

                        tonalElevation = 8.dp,

                        shadowElevation = 14.dp
                    ) {

                        Column(
                            modifier = Modifier.padding(22.dp),

                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            val qrBitmap =
                                paymentViewModel.getQrBitmap(
                                    qrAmount,
                                    qrBillId
                                )

                            Image(
                                bitmap = qrBitmap.asImageBitmap(),

                                contentDescription = null,

                                modifier = Modifier.size(220.dp)
                            )

                            Spacer(
                                Modifier.height(18.dp)
                            )

                            Text(
                                text = "₹${"%.0f".format(qrAmount)}",

                                style = MaterialTheme.typography.headlineMedium,

                                fontWeight = FontWeight.ExtraBold,

                                color = AccentBrown
                            )

                            Spacer(
                                Modifier.height(6.dp)
                            )

                            Text(
                                text = "Bill #$qrBillId",

                                color = SecondaryText
                            )
                        }
                    }

                    Spacer(
                        Modifier.height(24.dp)
                    )

                    Text(
                        text = "Scan using any UPI app",

                        color = SecondaryText
                    )

                    Spacer(
                        Modifier.height(20.dp)
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,

            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
        )
    }
}