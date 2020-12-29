package com.crazy.demo.entity.request

data class CommonRequest(
    var username: String? = null,
    var password: String? = null,
    var area: String? = null,
    var mobile: String? = null,
    var sms_code: String? = null,
    var email: String? = null,
    var email_code: String? = null,
    var coin_id: String? = null,
    var number: String? = null,
    var pay_password: String? = null,
    var to_username: String? = null,
    var transfer_number: String? = null,
    var transfer_type: String? = null
)