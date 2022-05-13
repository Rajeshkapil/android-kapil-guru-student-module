package com.kapilguru.student.payment.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

//Same class is  used for initTransactionRequest and TransactionStatusRequest
data class InitiateTransactionRequest(
    @SerializedName("amount") var amount: Double? = 0.0,
    @SerializedName("cgst") var centralGST: Double? = 0.0,
    @SerializedName("grandTotal") var grandTotal: Double? = 0.0,
    @SerializedName("orderId") var orderId: String? = "",
    @SerializedName("platformCharges") var platformCharges: Double? = 50.0,
    @SerializedName("product_code") var productCode: String? = "",
    @SerializedName("product_id") var productId: Int? = 0,
    @SerializedName("product_type") var productType: String? = "", //webinar, batch
    @SerializedName("sgst") var stateGST: Double? = 0.0,
    @SerializedName("user_code") var userCode: String? = "",
    @SerializedName("user_id") var userId: Int? = 0,
    @SerializedName("course_id") var courseId: Int? = null, // null for webinar thus never included in webinar, used for course
    @SerializedName("amount_after_discount") var amountAfterDiscount: Double? = 0.0,
    @SerializedName("discount_percent") val discountPercent: Int? = 0,
    @SerializedName("discounted_amount") val discountedAmount: Int? = 0,
) : Parcelable{
    init {
        amountAfterDiscount = amount
        platformCharges = getMobilePlatformCharges()
    }

    constructor(parcel: Parcel) : this(
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    fun getMobilePlatformCharges(): Double {
        amount?.let { amountNotNull ->
            if (amountNotNull > 0 && amountNotNull <= 500) {
                return 50.0
            } else if (amountNotNull > 500 && amountNotNull <= 1000) {
                return 70.0
            } else if (amountNotNull > 1000 && amountNotNull <= 1500) {
                return 80.0
            } else if (amountNotNull > 1500 && amountNotNull <= 2000) {
                return 90.0
            } else if (amountNotNull > 2000 && amountNotNull <= 3000) {
                return 140.0
            } else if (amountNotNull > 3000 && amountNotNull <= 5000) {
                return 160.0
            } else if (amountNotNull > 5000) {
                return 200.0
            }
        }
        return 0.0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(amount)
        parcel.writeValue(centralGST)
        parcel.writeValue(grandTotal)
        parcel.writeString(orderId)
        parcel.writeValue(platformCharges)
        parcel.writeString(productCode)
        parcel.writeValue(productId)
        parcel.writeString(productType)
        parcel.writeValue(stateGST)
        parcel.writeString(userCode)
        parcel.writeValue(userId)
        parcel.writeValue(courseId)
        parcel.writeValue(amountAfterDiscount)
        parcel.writeValue(discountPercent)
        parcel.writeValue(discountedAmount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InitiateTransactionRequest> {
        override fun createFromParcel(parcel: Parcel): InitiateTransactionRequest {
            return InitiateTransactionRequest(parcel)
        }

        override fun newArray(size: Int): Array<InitiateTransactionRequest?> {
            return arrayOfNulls(size)
        }
    }
}