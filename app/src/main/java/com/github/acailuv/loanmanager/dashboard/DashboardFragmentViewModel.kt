package com.github.acailuv.loanmanager.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.acailuv.loanmanager.database.*
import com.github.acailuv.loanmanager.getNamesFrom
import com.jjoe64.graphview.series.DataPoint
import kotlinx.coroutines.*
import java.util.*

class DashboardFragmentViewModel(
    val userTable: UserDao,
    val cardTable: CardDao,
    val installmentTable: InstallmentDao,
    application: Application
) : AndroidViewModel(application) {

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    // Live Data
    private val _rawSpinnerContent = MutableLiveData<List<Card>>()
    val rawSpinnerContent: LiveData<List<Card>>
        get() = _rawSpinnerContent

    private val _spinnerContent = MutableLiveData<List<String>>()
    val spinnerContent: LiveData<List<String>>
        get() = _spinnerContent

    init {
        initializeSpinnerContent()
    }

    val user = MutableLiveData<User>()

    init {
        initializeUserInfo()
    }

    val monthlyLoan = MutableLiveData<Long>()

    init {
        initializeMonthlyLoan()
    }

    // Initialize Monthly Loan
    fun initializeMonthlyLoan() {
        uiScope.launch {
            val installmentList = withContext(Dispatchers.IO) {
                installmentTable.getInstallments()
            }

            var sumMonthlyLoan = 0L

            for (i in installmentList.indices) {

                val currentInstallment = installmentList[i]

                if (currentInstallment.endDate >= Calendar.getInstance().timeInMillis) {
                    sumMonthlyLoan += (currentInstallment.total / currentInstallment.tenorMonth + (currentInstallment.total * currentInstallment.interest)).toLong()
                }
            }

            monthlyLoan.value = sumMonthlyLoan
        }
    }

    // Initialize User Info
    fun initializeUserInfo() {
        uiScope.launch {
            val userTableContent = withContext(Dispatchers.IO) { userTable.getUsers() }

            if (userTableContent.isEmpty()) {
                withContext(Dispatchers.IO) {
                    userTable.insert(User(1, "User", 0))
                }
            }
            user.value = withContext(Dispatchers.IO) {
                userTable.getUser(1)
            }
        }
    }

    // Spinner Content
    fun initializeSpinnerContent() {
        uiScope.launch {
            _rawSpinnerContent.value = withContext(Dispatchers.IO) {
                cardTable.getCards()
            }
            _spinnerContent.value = getNamesFrom(_rawSpinnerContent.value)
        }
    }

    val totalLoanInCard = MutableLiveData<Long>()

    // Total Loan in a card
    fun getTotalLoan(card: Card?) {
        val cardId = card?.id

        uiScope.launch {
            val installmentList = withContext(Dispatchers.IO) {
                installmentTable.getInstallments()
            }

            var sumTotalLoan = 0L

            for (i in installmentList.indices) {

                val currentInstallment = installmentList[i]

                if (currentInstallment.cardId == cardId) {
                    sumTotalLoan += currentInstallment.total
                }
            }
            totalLoanInCard.value = sumTotalLoan
        }
    }

    val monthlyIncomeSeries = MutableLiveData<Array<DataPoint>>()
    val monthlyLoanSeries = MutableLiveData<Array<DataPoint>>()
    val lowestDate = MutableLiveData<Date>()
    val highestDate = MutableLiveData<Date>()

    // Build graph series
    fun buildGraphSeries() {

        uiScope.launch {
            val monthlyIncomeSeriesHolder: MutableList<DataPoint> = mutableListOf()
            val monthlyLoanSeriesHolder: MutableList<DataPoint> = mutableListOf()
            val endDate = Calendar.getInstance()
            val beginningDate = Calendar.getInstance()

            withContext(Dispatchers.Default) {
                val installmentsByEndDate = installmentTable.getInstallmentsByEndDate()
                val installments = installmentTable.getInstallments()
                val user = userTable.getUser(1)

                val current: Long
                val currentDate: Calendar

                if (installments.isNotEmpty()) {
                    current = installments[0].startDate
                    currentDate = Calendar.getInstance()
                    currentDate.timeInMillis = current
                    beginningDate.timeInMillis = current
                } else {
                    current = Calendar.getInstance().timeInMillis
                    currentDate = Calendar.getInstance()
                    currentDate.timeInMillis = current
                    beginningDate.timeInMillis = current
                }

                val end: Long

                if (installmentsByEndDate.isNotEmpty()) {
                    end = installmentsByEndDate[installmentsByEndDate.size - 1].endDate
                    endDate.timeInMillis = end
                } else {
                    val placeholderEnd = Calendar.getInstance()
                    placeholderEnd.add(Calendar.MONTH, 1)
                    end = placeholderEnd.timeInMillis
                    endDate.timeInMillis = end
                }

                var currentMonthLoan: Int

                while (currentDate.timeInMillis <= end) {
                    monthlyIncomeSeriesHolder.add(
                        DataPoint(Date(currentDate.timeInMillis), user.monthlyIncome.toDouble())
                    )

                    currentMonthLoan = 0

                    for (i in installments.indices) {
                        // println("start date: ${installments[i].startDate}   end date: ${installments[i].endDate}   current(beginningDate): ${beginningDate.timeInMillis}")
                        if (installments[i].startDate <= currentDate.timeInMillis && installments[i].endDate >= currentDate.timeInMillis) {
                            val loanTotal = installments[i].total
                            val interest = installments[i].interest
                            val tenor = installments[i].tenorMonth

                            // println(currentMonthLoan)
                            currentMonthLoan += ((loanTotal/tenor) + (loanTotal*interest)).toInt()
                        }
                    }
                    monthlyLoanSeriesHolder.add(
                        DataPoint(Date(currentDate.timeInMillis), currentMonthLoan.toDouble())
                    )
                    currentDate.add(Calendar.MONTH, 1)
                }
            }
            // println("monthlyIncomeHolder size: ${monthlyIncomeSeriesHolder.size}   monthlyLoeanHolder size: ${monthlyLoanSeriesHolder.size}")
            highestDate.value = endDate.time
            lowestDate.value = beginningDate.time
            monthlyIncomeSeries.value = monthlyIncomeSeriesHolder.toTypedArray()
            monthlyLoanSeries.value = monthlyLoanSeriesHolder.toTypedArray()
        }
    }

    // DEBUG: Clear all table
    fun clearAllTable() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                userTable.clear()
                cardTable.clear()
                installmentTable.clear()
            }
            _spinnerContent.value = listOf()
            _rawSpinnerContent.value = listOf()
        }
    }
}