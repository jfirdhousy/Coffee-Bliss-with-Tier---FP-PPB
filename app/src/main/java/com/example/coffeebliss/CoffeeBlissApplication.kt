package com.example.coffeebliss

import android.app.Application
import com.example.coffeebliss.data.CoffeeBlissDatabase
import com.example.coffeebliss.data.CoffeeBlissRepository

class CoffeeBlissApplication : Application() {

    // "by lazy" = build it the first time it is actually used, then reuse it.
    private val database by lazy { CoffeeBlissDatabase.getDatabase(this) }

    val repository by lazy {
        CoffeeBlissRepository(database.memberDao(), database.transactionDao())
    }
}
