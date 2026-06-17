package com.example.coffeebliss.ui.components

import java.text.NumberFormat
import java.util.Locale

/**
 * Formats a number of rupiah for display, e.g. 150000.0 -> "Rp 150.000".
 * Indonesian formatting uses a dot as the thousands separator.
 */
fun formatRupiah(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
    return "Rp " + formatter.format(amount.toLong())
}

/** Builds a member ID label like "MBR00007" from the database id. */
fun memberIdLabel(id: Long): String = "MBR" + id.toString().padStart(5, '0')
