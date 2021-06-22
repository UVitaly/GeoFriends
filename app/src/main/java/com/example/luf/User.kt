package com.example.luf

class User {
    var fullName: String? = null
    var age: String? = null
    var email: String? = null
    var currentLocX: String? = null
    var currentLocY: String? = null

    constructor(){}

    constructor( fullName: String, age: String, email: String)
    {
        this.fullName=fullName
        this.age=age
        this.email=email
    }
}