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

data class CartItem (var itemName : String, var store : String, var image: String, var price : Double, var itemDetails : String,
                        var quantity : Int, var total : Double) {
    constructor() : this("", "", "", 0.0, "", 1, 0.0)
}

data class Order (var orderId : String, var username : String, var mobile : String, var address: String,
                  var paymentMethod : String, var paymentMerchant : String, var orderPayment : Double,
                  var paymentDate : String, var orderStatus : String, var orderTracking : String) {
    constructor() : this("","", "", "", "", ""
        ,0.0,"", "", "")
}

data class Feedback (var feedbackId : String, var image : String, var feedback : String, var feedbackDate : String, var user : String) {
    constructor() : this("", "", "", "", "")
}
