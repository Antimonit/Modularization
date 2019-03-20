package me.khol.onboarding.room

import androidx.room.*
import io.reactivex.Flowable

/**
 * Dao for [DBRegistration] entity.
 * Stores progress and information provided by the user during investor onboarding.
 * There should always be at most one record in the table.
 */
@Dao
abstract class RegistrationDao {

    /**
     * There can only be single Registration in database so clear whole table when creating a new one
     */
    @Transaction
    open fun clearAndInsert(registration: DBRegistration) {
        clearTable()
        insertRegistration(registration)
    }

    @Query("DELETE FROM registrations")
    abstract fun clearTable()

    @Insert
    protected abstract fun insertRegistration(app: DBRegistration): Long

    @Query("select count(*) from registrations")
    abstract fun getRegistrationsCount(): Flowable<Int>

    @Update
    abstract fun updateRegistration(registration: DBRegistration)

    @Query("SELECT * from registrations")
    abstract fun getRegistrations(): Flowable<List<DBRegistration>>

    @Query("UPDATE registrations set firstName = :name, surname = :surname where id = :id")
    abstract fun setName(id: Int, name: String, surname: String)

    @Query("UPDATE registrations set currentStep = :step where id = :id")
    abstract fun setStep(id: Int, step: Int)

    @Query("UPDATE registrations set nickname = :nick where id = :id")
    abstract fun setNick(id: Int, nick: String)
}