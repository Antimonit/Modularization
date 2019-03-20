package me.khol.onboarding.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Contains information provided by the user during investor onboarding.
 * Use [RegistrationDao] to retrieve and update this entity.
 */
@Entity(tableName = "registrations")
data class DBRegistration(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var currentStep: Int = 0,
    var nickname: String? = null,
    var email: String? = null,
    var firstName: String? = null,
    var surname: String? = null,
    var phone: String? = null,
    @Embedded(prefix = "permanent")
    var permanentAddress: DBAddress? = null,
    @Embedded(prefix = "contact")
    var contactAddress: DBAddress? = null
)
