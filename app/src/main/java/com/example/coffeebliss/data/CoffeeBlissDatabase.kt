package com.example.coffeebliss.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * The Room database. It lists every table (entity) and gives access to the DAOs.
 *
 * `version = 1`     -> bump this number whenever you change the tables.
 * `exportSchema`    -> turned off to keep this learning project simple.
 */
@Database(
    entities = [Member::class, Transaction::class],
    version = 2,
    exportSchema = false
)
abstract class CoffeeBlissDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        // @Volatile makes sure every thread sees the latest value of INSTANCE.
        @Volatile
        private var INSTANCE: CoffeeBlissDatabase? = null

        /**
         * Returns the one shared database for the whole app, creating it the first time.
         * This "singleton" pattern avoids opening the database more than once.
         */
        fun getDatabase(context: Context): CoffeeBlissDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CoffeeBlissDatabase::class.java,
                    "coffee_bliss_database"
                )
                    // Learning project: when the table layout changes (version bump),
                    // just rebuild the database and re-seed the demo members instead of
                    // writing a hand-rolled migration.
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .addCallback(SeedCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Runs only the very first time the database is created and adds 3 demo members,
     * so the app is not empty when you open it for the first time.
     *
     * We insert with plain SQL on the database connection Room hands us here. This is the
     * safe way to seed: it does NOT call the DAOs again while the database is still being
     * created (which can crash the app), and it finishes before the first screen loads.
     */
    private class SeedCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // totalPointsEarned is each member's lifetime points (>= current points),
            // chosen so the demo shows all three tiers: Andi = Platinum (>=300),
            // Citra = Gold (>=100), Budi = Silver (<100).
            db.execSQL(
                "INSERT INTO members (name, email, phone, points, totalPointsEarned) " +
                    "VALUES ('Andi Pratama', 'andipr@gmail.com', '0812-3456-7890', 120, 320)"
            )
            db.execSQL(
                "INSERT INTO members (name, email, phone, points, totalPointsEarned) " +
                    "VALUES ('Budi Santoso', 'budi.santoso@gmail.com', '0813-1111-2222', 50, 50)"
            )
            db.execSQL(
                "INSERT INTO members (name, email, phone, points, totalPointsEarned) " +
                    "VALUES ('Citra Lestari', 'citra.lestari@gmail.com', '0814-3333-4444', 80, 180)"
            )
        }
    }
}
