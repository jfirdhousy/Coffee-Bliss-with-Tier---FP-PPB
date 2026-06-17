package com.example.coffeebliss.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.coffeebliss.CoffeeBlissApplication
import com.example.coffeebliss.data.CoffeeBlissRepository
import com.example.coffeebliss.data.Member
import com.example.coffeebliss.data.Reward
import com.example.coffeebliss.data.Transaction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * The ViewModel is the "VM" in MVVM. It holds the screen state and the actions the
 * user can take. The UI (Composable screens) only reads state from here and calls
 * these functions — it never touches the database directly.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CoffeeBlissViewModel(
    private val repository: CoffeeBlissRepository
) : ViewModel() {

    // Every member in the database (used by the "switch member" screen).
    // stateIn() turns the database Flow into a StateFlow the UI can observe and that
    // always has a current value (starts as an empty list).
    val members: StateFlow<List<Member>> = repository.getAllMembers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Which member is currently "logged in" on the dashboard.
    private val _activeMemberId = MutableStateFlow<Long?>(null)

    // The active member, kept live so points update on screen automatically.
    // flatMapLatest swaps to a new database Flow whenever the active member changes.
    val activeMember: StateFlow<Member?> = _activeMemberId
        .flatMapLatest { id ->
            if (id == null) flowOf(null) else repository.getMemberById(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    // The active member's transaction history (newest first).
    val transactions: StateFlow<List<Transaction>> = _activeMemberId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else repository.getTransactionsForMember(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        // As soon as members load, automatically log in the first one.
        viewModelScope.launch {
            members.collect { list ->
                if (_activeMemberId.value == null && list.isNotEmpty()) {
                    _activeMemberId.value = list.first().id
                }
            }
        }
    }

    /** Switch which member is shown on the dashboard. */
    fun setActiveMember(memberId: Long) {
        _activeMemberId.value = memberId
    }

    /** FR-01 Registrasi Member: save a new member, then log them in. */
    fun registerMember(name: String, email: String, phone: String, onDone: () -> Unit) {
        viewModelScope.launch {
            val newId = repository.insertMember(
                Member(
                    name = name.trim(),
                    email = email.trim(),
                    phone = phone.trim(),
                    points = 0
                )
            )
            _activeMemberId.value = newId
            onDone()
        }
    }

    /** Update a member's saved details (used by Edit Profile). */
    fun updateMember(member: Member) {
        viewModelScope.launch { repository.updateMember(member) }
    }

    /**
     * FR-04 Tambah Transaksi: record a purchase and add points automatically.
     * PRD rule: 1 point for every Rp 10.000 spent.
     */
    fun addTransaction(amount: Double) {
        val memberId = _activeMemberId.value ?: return
        viewModelScope.launch {
            val pointsEarned = pointsFor(amount)
            repository.insertTransaction(
                Transaction(
                    memberId = memberId,
                    amount = amount,
                    pointEarned = pointsEarned,
                    date = today(),
                    description = "Pembelian"
                )
            )
            // Add the earned points to BOTH the spendable balance and the lifetime
            // total (the lifetime total decides the membership tier).
            repository.addEarnedPoints(memberId, pointsEarned)
        }
    }

    /**
     * FR-06 Redeem Reward: exchange points for a reward.
     * Returns success = false if the member does not have enough points.
     */
    fun redeemReward(reward: Reward, onResult: (success: Boolean) -> Unit) {
        val memberId = _activeMemberId.value
        if (memberId == null) {
            onResult(false)
            return
        }
        viewModelScope.launch {
            // Re-read the latest points from the database before redeeming.
            val member = repository.getMemberOnce(memberId)
            if (member == null || member.points < reward.cost) {
                onResult(false)
                return@launch
            }
            // Subtract the cost from the spendable balance (the lifetime total, and so
            // the member's tier, is left untouched).
            repository.addPoints(memberId, -reward.cost)
            // Record the redemption in the point history as a subtraction, so it shows
            // up alongside purchases (e.g. "-50 Poin").
            repository.insertTransaction(
                Transaction(
                    memberId = memberId,
                    amount = 0.0,
                    pointEarned = -reward.cost,
                    date = today(),
                    description = "Tukar reward: ${reward.name}"
                )
            )
            onResult(true)
        }
    }

    private fun today(): String =
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

    companion object {
        /** PRD point rule, shared with the UI so it can preview the points before saving. */
        fun pointsFor(amount: Double): Int = (amount / 10_000).toInt()

        /**
         * Tells Compose how to build this ViewModel. It reaches the Application object
         * (via APPLICATION_KEY) to get the shared repository.
         */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as CoffeeBlissApplication
                CoffeeBlissViewModel(application.repository)
            }
        }
    }
}
