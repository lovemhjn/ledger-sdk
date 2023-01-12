package lib.dehaat.ledger.framework.model.abs


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseABSTransactions(
    @Json(name = "data")
    val transactions: List<ABSTransaction>?
)

@JsonClass(generateAdapter = true)
data class ABSTransaction(
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "ordering_date")
    val orderingDate: Double?,
    @Json(name = "scheme_name")
    val schemeName: String?
)