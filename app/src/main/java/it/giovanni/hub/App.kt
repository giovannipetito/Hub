package it.giovanni.hub

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
/*
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import it.giovanni.hub.data.model.realm.Address
import it.giovanni.hub.data.model.realm.Course
import it.giovanni.hub.data.model.realm.Student
import it.giovanni.hub.data.model.realm.Teacher
*/

@HiltAndroidApp
class App: Application() {
    /*
    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(
                    Address::class,
                    Teacher::class,
                    Course::class,
                    Student::class,
                )
            )
        )
    }
    */
}