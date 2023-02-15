package com.example.weight_tracker_kotlin

class UserMeasurements {
    private var uid = "default" // default
    private var weight = 0.0 // default
    private var bmi = 0.0 // default
    private var circumference = 0.0 // default
    private var bodyFat = 0.0 // default
    private var date = "default" // default

    // Setters
    fun setUID(uid: String) {
        this.uid = uid
    }

    fun setWeight(weight: Double) {
        this.weight = weight
    }

    fun setBMI(bmi: Double) {
        this.bmi = bmi
    }

    fun setCircumference(circumference: Double) {
        this.circumference = circumference
    }

    fun setBodyFat(bodyFat: Double) {
        this.bodyFat = bodyFat
    }

    fun setDate(date: String) {
        this.date = date
    }

    // Getters
    fun getUID(): String {
        return uid
    }

    fun getWeight(): Double {
        return weight
    }

    fun getBMI(): Double {
        return bmi
    }

    fun getCircumference(): Double {
        return circumference
    }

    fun getBodyFat(): Double {
        return bodyFat
    }

    fun getDate(): String {
        return date
    }
}