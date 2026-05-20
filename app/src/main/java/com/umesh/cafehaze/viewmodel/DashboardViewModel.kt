package com.umesh.cafehaze.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umesh.cafehaze.model.data.Bill
import com.umesh.cafehaze.model.repository.DashboardRepository
import com.umesh.cafehaze.utils.parseToIst
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class DashboardUiState(
    val loading: Boolean = false,
    val todayTotal: Double = 0.0,
    val monthTotal: Double = 0.0,
    val yesterdayTotal: Double = 0.0,
    val lastMonthTotal: Double = 0.0,
    val onlineGraph: List<Float> = emptyList(),
    val offlineGraph: List<Float> = emptyList(),
    val recentTransactions: List<TransactionUi> =
        emptyList()
)

data class TransactionUi(
    val bill: Bill,
    val time: String
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repo: DashboardRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(DashboardUiState())

    val uiState: StateFlow<DashboardUiState> =
        _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    loading = true
                )

            val bills = repo.getBills()

            val zone =
                ZoneId.of("Asia/Kolkata")

            val today =
                LocalDate.now(zone)

            val parsed =
                bills.mapNotNull { bill ->

                    try {
                        parseToIst(
                            bill.createdAt
                        ) to bill
                    } catch (_: Exception) {
                        null
                    }
                }

            // TODAY
            val todayBills =
                parsed.filter {
                    it.first.toLocalDate() ==
                            today
                }

            // MONTH
            val monthBills =
                parsed.filter {
                    it.first.month ==
                            today.month &&
                            it.first.year ==
                            today.year
                }

            // TOTALS
            val todayTotal =
                todayBills.sumOf {
                    it.second.totalAmount
                }

            val monthTotal =
                monthBills.sumOf {
                    it.second.totalAmount
                }

            // YESTERDAY
            val yesterday =
                today.minusDays(1)

            val yesterdayTotal =
                parsed.filter {
                    it.first.toLocalDate() ==
                            yesterday
                }.sumOf {
                    it.second.totalAmount
                }

            // LAST MONTH
            val lastMonth =
                today.minusMonths(1)

            val lastMonthTotal =
                parsed.filter {
                    it.first.month ==
                            lastMonth.month &&
                            it.first.year ==
                            lastMonth.year
                }.sumOf {
                    it.second.totalAmount
                }

            // LAST 7 DAYS GRAPH
            val last7Days =
                (0..6).map {
                    today.minusDays(
                        (6 - it).toLong()
                    )
                }

            val groupedMap =
                parsed.groupBy {
                    it.first.toLocalDate()
                }

            val online =
                last7Days.map { date ->

                    groupedMap[date]
                        ?.filter {
                            it.second
                                .paymentMethod
                                ?.lowercase() ==
                                    "online"
                        }
                        ?.sumOf {
                            it.second
                                .totalAmount
                        }
                        ?.toFloat()
                        ?: 0f
                }

            val offline =
                last7Days.map { date ->

                    groupedMap[date]
                        ?.filter {
                            it.second
                                .paymentMethod
                                ?.lowercase() ==
                                    "offline"
                        }
                        ?.sumOf {
                            it.second
                                .totalAmount
                        }
                        ?.toFloat()
                        ?: 0f
                }

            // RECENT TRANSACTIONS
            val recentTransactions =
                (todayBills + monthBills)
                    .distinctBy {
                        it.second.billId
                    }
                    .sortedByDescending {
                        it.first
                    }
                    .take(5)
                    .map { (
                               dateTime,
                               bill
                           ) ->

                        TransactionUi(
                            bill = bill,
                            time =
                                dateTime.format(
                                    DateTimeFormatter.ofPattern(
                                        "hh:mm a"
                                    )
                                )
                        )
                    }

            _uiState.value =
                DashboardUiState(
                    loading = false,
                    todayTotal =
                        todayTotal,
                    monthTotal =
                        monthTotal,
                    yesterdayTotal =
                        yesterdayTotal,
                    lastMonthTotal =
                        lastMonthTotal,
                    onlineGraph =
                        online,
                    offlineGraph =
                        offline,
                    recentTransactions =
                        recentTransactions
                )
        }
    }
}