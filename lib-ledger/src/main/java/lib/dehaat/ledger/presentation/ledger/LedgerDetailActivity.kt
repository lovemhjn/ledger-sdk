package lib.dehaat.ledger.presentation.ledger

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowInsetsControllerCompat
import com.amazonaws.mobile.client.AWSMobileClient
import com.dehaat.androidbase.helper.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.navigation.LedgerNavigation
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.LedgerDetailViewModel
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.util.NotificationHandler

@AndroidEntryPoint
class LedgerDetailActivity : ComponentActivity() {

    val viewModel: LedgerDetailViewModel by viewModels()

    private lateinit var args: Args

    @Inject
    lateinit var notificationHandler: NotificationHandler

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it?.resultCode == Activity.RESULT_OK) {
            viewModel.getLedgerData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = getArgs(intent)

        args.language?.let {
            setLedgerLanguage(it)
        }

        if (!LedgerSDK.isCurrentAppAvailable()) {
            showToast(getString(R.string.initialise_ledger))
            finish()
            return
        }

        if (args.partnerId.isEmpty() && LedgerSDK.isDebug) {
            showToast("PartnerId missing (check dehaat-center-API)")
            finish()
            return
        }

        modifyStatusBar(LedgerSDK.currentApp.ledgerColors)

        AWSMobileClient.getInstance().initialize(this).execute()

        setContent {
            LedgerTheme {
                LedgerNavigation(
                    dcName = args.dcName,
                    partnerId = args.partnerId,
                    isDCFinanced = args.isDCFinanced,
                    ledgerColors = LedgerSDK.currentApp.ledgerColors,
                    resultLauncher = resultLauncher,
                    finishActivity = { finish() },
                    viewModel = viewModel,
                    ledgerCallbacks = LedgerSDK.currentApp.ledgerCallBack,
                    onDownloadClick = {
                        val ledgerCallbacks = LedgerSDK.currentApp.ledgerCallBack
                        notificationHandler.notificationBuilder.setSmallIcon(LedgerSDK.appIcon)
                        if (it.isFailed) {
                            showToast(getString(R.string.tech_problem))
                        } else {
                            notificationHandler.apply {
                                if (it.progressData.bytesCurrent == 100) {
                                    notificationBuilder.apply {
                                        setContentText(getString(R.string.invoice_download_success))
                                        setContentIntent(
                                            ledgerCallbacks.downloadInvoiceIntent.invoke(
                                                this@LedgerDetailActivity,
                                                it.filePath
                                            )
                                        )
                                    }
                                    ledgerCallbacks.onDownloadInvoiceSuccess(it)
                                    showToast(getString(R.string.invoice_download_success))
                                } else {
                                    notificationBuilder.setContentText(getString(R.string.invoice_download_in_progress))
                                }
                                notificationBuilder.setProgress(
                                    it.progressData.bytesTotal,
                                    it.progressData.bytesCurrent,
                                    false
                                )
                                notifyBuilder()
                            }
                        }
                    }
                )
            }
        }
    }

    private fun setLedgerLanguage(appLanguage: String) {
        val locale = Locale(appLanguage)
        val res = this.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)
        Locale.setDefault(locale)
    }

    private fun modifyStatusBar(ledgerColors: LedgerColors) {
        if(LedgerSDK.isDBA) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ledgerColors.ActionBarColor.toArgb()
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                true
        }
    }

    companion object {
        private const val KEY_DC_FINANCED = "KEY_DC_FINANCED"
        private const val KEY_APP_LANGUAGE = "KEY_APP_LANGUAGE"

        fun getArgs(intent: Intent) = Args(
            partnerId = intent.getStringExtra(LedgerConstants.KEY_PARTNER_ID) ?: "",
            dcName = intent.getStringExtra(LedgerConstants.KEY_DC_NAME) ?: "",
            isDCFinanced = intent.getBooleanExtra(KEY_DC_FINANCED, false),
            language = intent.getStringExtra(KEY_APP_LANGUAGE)
        )

        data class Args(
            val partnerId: String,
            val dcName: String,
            val isDCFinanced: Boolean,
            val language: String?
        ) {
            fun build(context: Context) = Intent(
                context,
                LedgerDetailActivity::class.java
            ).apply {
                putExtra(LedgerConstants.KEY_PARTNER_ID, partnerId)
                putExtra(LedgerConstants.KEY_DC_NAME, dcName)
                putExtra(KEY_DC_FINANCED, isDCFinanced)
                putExtra(KEY_APP_LANGUAGE, language)
            }
        }
    }
}
