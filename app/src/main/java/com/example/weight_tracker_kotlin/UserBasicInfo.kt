package com.example.weight_tracker_kotlin

class UserBasicInfo {
    private var uid = "" // default
    private var gender = "" // default
    private var age = 0 // default
    private var height = 0.0 // default
    private var goal = 0.0 // default


    // Setters
    fun setUID(uid: String) {
        this.uid = uid
    }

    fun setGender(gender: String) {
        this.gender = gender
    }

    fun setAge(age: Int) {
        this.age = age
    }

    fun setHeight(height: Double) {
        this.height = height
    }

    fun setGoal(goal: Double) {
        this.goal = goal
    }
}