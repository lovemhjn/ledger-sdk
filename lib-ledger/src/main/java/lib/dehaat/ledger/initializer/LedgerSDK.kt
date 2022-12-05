package lib.dehaat.ledger.initializer

import android.content.Context
import android.os.Environment
import androidx.annotation.DrawableRes
import kotlinx.coroutines.flow.MutableStateFlow
import lib.dehaat.ledger.framework.model.outstanding.OutstandingData
import lib.dehaat.ledger.presentation.ledger.LedgerDetailActivity
import java.io.File
import java.math.BigDecimal

object LedgerSDK {

    internal val outstandingDataFlow = MutableStateFlow(OutstandingData(false))
    internal lateinit var currentApp: LedgerParentApp
    internal lateinit var bucket: String
    internal var locale: String = "en"
    internal var appIcon: Int = 0
        private set

    fun init(
        context: Context,
        app: LedgerParentApp,
        bucket: String,
        @DrawableRes appIcon: Int,
        debugMode: Boolean
    ) {
        currentApp = app
        this.bucket = bucket
        this.appIcon = appIcon
        this.isDebug = debugMode
    }

    fun isCurrentAppAvailable() = ::currentApp.isInitialized && ::bucket.isInitialized

    @Throws(Exception::class)
    fun openLedger(
        context: Context,
        partnerId: String,
        dcName: String,
        isDCFinanced: Boolean,
        language: String? = null
    ) = if (isCurrentAppAvailable()) {
        LedgerDetailActivity.Companion.Args(
            partnerId = partnerId,
            dcName = dcName,
            isDCFinanced = isDCFinanced,
            language = language
        ).also {
            language?.let { lang -> locale = lang }
            context.startActivity(it.build(context))
        }
    } else {
        throw Exception("Ledger not initialised Exception")
    }

    fun getFile(context: Context): File? = try {
        File(
            context.getExternalFilesDir(
                Environment.DIRECTORY_DOWNLOADS
            ),
            "DeHaat"
        ).apply { mkdir() }
    } catch (e: Exception) {
        if (::currentApp.isInitialized) {
            currentApp.ledgerCallBack.exceptionHandler(e)
        }
        null
    }

    suspend fun updateOutStanding(showDialog: Boolean, amount: BigDecimal?) {
        outstandingDataFlow.emit(OutstandingData(showDialog, amount))
    }

    val isDBA: Boolean
        get() = currentApp is LedgerParentApp.DBA

    val isAIMS: Boolean
        get() = currentApp is LedgerParentApp.AIMS

    var isDebug: Boolean = false
        private set
}
