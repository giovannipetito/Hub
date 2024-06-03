package it.giovanni.hub.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import it.giovanni.hub.App
import it.giovanni.hub.data.model.realm.Address
import it.giovanni.hub.data.model.realm.Course
import it.giovanni.hub.data.model.realm.Student
import it.giovanni.hub.data.model.realm.Teacher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RealmViewModel: ViewModel() {

    private val realm: Realm = App.realm

    val courses: StateFlow<List<Course>> = realm
        .query<Course>()
        .asFlow()
        .map { results ->
            results.list.toList()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    var course: Course? by mutableStateOf(null)
        private set

    init {
        createSampleEntries()
    }

    private fun createSampleEntries() {
        viewModelScope.launch {
            realm.write {
                val address1 = Address().apply {
                    fullName = "Marco Rossi"
                    street = "Via Primo Maggio"
                    houseNumber = 15
                    zip = 20093
                    city = "Milano"
                }
                val address2 = Address().apply {
                    fullName = "Filippo Bianchi"
                    street = "Via della Resistenza"
                    houseNumber = 24
                    zip = 80014
                    city = "Napoli"
                }
                val course1 = Course().apply {
                    name = "Android Basics"
                }
                val course2 = Course().apply {
                    name = "Kotlin Programming"
                }
                val course3 = Course().apply {
                    name = "Coroutines & RxJava"
                }

                val teacher1 = Teacher().apply {
                    address = address1
                    courses = realmListOf(course1, course2)
                }
                val teacher2 = Teacher().apply {
                    address = address2
                    courses = realmListOf(course3)
                }

                course1.teacher = teacher1
                course2.teacher = teacher1
                course3.teacher = teacher2

                address1.teacher = teacher1
                address2.teacher = teacher2

                val student1 = Student().apply {
                    name = "Davide Russo"
                }
                val student2 = Student().apply {
                    name = "Andrea Esposito"
                }

                course1.enrolledStudents.add(student1)
                course2.enrolledStudents.add(student2)
                course3.enrolledStudents.addAll(listOf(student1, student2))

                copyToRealm(teacher1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(teacher2, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(course1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(course2, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(course3, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(student1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(student2, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }

    fun showCourse(course: Course) {
        this.course = course
    }

    fun hideCourse() {
        course = null
    }

    fun deleteCourse() {
        viewModelScope.launch {
            realm.write {
                val course = course ?: return@write
                val latestCourse = findLatest(course) ?: return@write
                delete(latestCourse)

                this@RealmViewModel.course = null
            }
        }
    }
}