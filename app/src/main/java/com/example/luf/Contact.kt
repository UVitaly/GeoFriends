package com.example.luf

class Contact
{
    var id: Int? = null
    var name: String? = null
    var locationPermission: Boolean? = null
    var isFavorite:Boolean?=null

    constructor() {}
    constructor(id: Int?, name: String?)
    {
        this.id = id
        this.name = name
        locationPermission=false
        isFavorite=false
    }

}