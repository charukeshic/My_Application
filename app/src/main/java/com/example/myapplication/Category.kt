package com.example.myapplication

data class Category (var title : String, var image : String) {
    constructor() : this("", "")
}

data class Product (var itemName : String, var itemDetails : String) {
    constructor() : this("", "")
}