package com.example.myapplication

data class Category (var title : String, var image : String) {
    constructor() : this("", "")
}

data class Product (var itemName : String, var store : String, var image: String, var price : String) {
    constructor() : this("", "", "", "")
}

data class ProductDetails (var itemName : String, var store : String, var image: String, var price : String, var itemDetails : String) {
    constructor() : this("", "", "", "", "")
}

data class SalesProduct (var itemName : String, var store : String, var image: String,
                         var originalPrice : String, var discountedPrice : String, var itemDetails : String)
{
    constructor() : this("", "", "", "", "", "")
}

data class User (var email : String, var username : String, var password : String, var mobileNo : String,
                         var address : String, var image : String)
{
    constructor() : this("", "", "", "", "", "")
}