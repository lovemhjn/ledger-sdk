package lib.dehaat.ledger.navigation

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import lib.dehaat.ledger.initializer.callbacks.LedgerCallBack
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.LedgerDetailViewModel
import lib.dehaat.ledger.presentation.RevampLedgerViewModel
import lib.dehaat.ledger.presentation.ledger.details.availablecreditlimit.AvailableCreditLimitScreenArgs
import lib.dehaat.ledger.presentation.ledger.details.availablecreditlimit.ui.AvailableCreditLimitDetailsScreen
import lib.dehaat.ledger.presentation.ledger.details.creditnote.CreditNoteDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.creditnote.ui.CreditNoteDetailScreen
import lib.dehaat.ledger.presentation.ledger.details.interest.InterestDetailScreenArgs
import lib.dehaat.ledger.presentation.ledger.details.interest.ui.InterestDetailScreen
import lib.dehaat.ledger.presentation.ledger.details.invoice.InvoiceDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.invoice.RevampInvoiceDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.invoice.ui.InvoiceDetailScreen
import lib.dehaat.ledger.presentation.ledger.details.invoice.ui.RevampInvoiceDetailScreen
import lib.dehaat.ledger.presentation.ledger.details.loanlist.InvoiceListViewModel
import lib.dehaat.ledger.presentation.ledger.details.loanlist.ui.InvoiceListScreen
import lib.dehaat.ledger.presentation.ledger.details.payments.PaymentDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.payments.ui.PaymentDetailScreen
import lib.dehaat.ledger.presentation.ledger.details.payments.ui.RevampPaymentDetailScreen
import lib.dehaat.ledger.presentation.ledger.details.totaloutstanding.TotalOutstandingScreenArgs
import lib.dehaat.ledger.presentation.ledger.details.totaloutstanding.ui.TotalOutstandingScreen
import lib.dehaat.ledger.presentation.ledger.revamp.state.creditnote.CreditNoteDetailsViewModel
import lib.dehaat.ledger.presentation.ledger.revamp.state.creditnote.ui.RevampCreditNoteDetailsScreen
import lib.dehaat.ledger.presentation.ledger.ui.LedgerDetailScreen2
import lib.dehaat.ledger.presentation.ledger.ui.RevampLedgerScreen
import lib.dehaat.ledger.presentation.model.invoicedownload.InvoiceDownloadData
import lib.dehaat.ledger.util.navBaseComposable

@Composable
fun LedgerNavigation(
    dcName: String,
    partnerId: String,
    isDCFinanced: Boolean,
    ledgerColors: LedgerColors,
    ledgerCallbacks: LedgerCallBack,
    resultLauncher: ActivityResultLauncher<Intent?>,
    viewModel: LedgerDetailViewModel,
    onDownloadClick: (InvoiceDownloadData) -> Unit,
    finishActivity: () -> Unit
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (isDCFinanced) {
            LedgerRoutes.RevampLedgerScreen.screen
        } else {
            LedgerRoutes.LedgerDetailScreen.screen
        }
    ) {
        navBaseComposable(
            route = LedgerRoutes.LedgerDetailScreen.screen,
            arguments = listOf(
                navArgument(LedgerConstants.KEY_PARTNER_ID) {
                    type = NavType.StringType
                    defaultValue = partnerId
                }
            ),
            logScreenName = ledgerCallbacks.firebaseScreenLogger
        ) {
            viewModel.dcName = dcName
            LedgerDetailScreen2(
                viewModel = viewModel,
                ledgerColors = ledgerColors,
                onBackPress = finishActivity,
                detailPageNavigationCallback = provideDetailPageNavCallBacks(navController = navController),
                isLmsActivated = {
                    viewModel.isLMSActivated()
                },
                onPayNowClick = {
                    ledgerCallbacks.onClickPayNow(viewModel.uiState.value.creditSummaryViewData)
                },
                onPaymentOptionsClick = {
                    ledgerCallbacks.onPaymentOptionsClick(resultLauncher)
                },
                onError = { ledgerCallbacks.exceptionHandler(it) }
            )
        }

        navBaseComposable(
            route = LedgerRoutes.RevampLedgerScreen.screen,
            arguments = listOf(
                navArgument(LedgerConstants.KEY_PARTNER_ID) {
                    type = NavType.StringType
                    defaultValue = partnerId
                },
                navArgument(LedgerConstants.KEY_DC_NAME) {
                    type = NavType.StringType
                    defaultValue = dcName
                }
            ),
            logScreenName = ledgerCallbacks.firebaseScreenLogger
        ) {
            val revampLedgerViewModel = hiltViewModel<RevampLedgerViewModel>()
            RevampLedgerScreen(
                viewModel = revampLedgerViewModel,
                ledgerColors = ledgerColors,
                onBackPress = finishActivity,
                detailPageNavigationCallback = provideDetailPageNavCallBacks(navController),
                onPayNowClick = { ledgerCallbacks.onRevampPayNowClick(it) },
                onOtherPaymentModeClick = { ledgerCallbacks.onPaymentOptionsClick(resultLauncher) },
                onError = { ledgerCallbacks.exceptionHandler(it) }
            )
        }

        navBaseComposable(
            route = LedgerRoutes.TotalOutstandingDetailScreen.screen,
            logScreenName = ledgerCallbacks.firebaseScreenLogger
        ) {
            val outstandingArgs = it.arguments?.let { args -> TotalOutstandingScreenArgs(args) }
            TotalOutstandingScreen(
                uiState = outstandingArgs?.viewState,
                ledgerColors = ledgerColors
            ) {
                navController.popBackStack()
            }
        }

        navBaseComposable(
            route = LedgerRoutes.InvoiceListScreen.screen,
            logScreenName = ledgerCallbacks.firebaseScreenLogger
        ) {
            val invoiceListViewModel = hiltViewModel<InvoiceListViewModel>()
            InvoiceListScreen(
                viewModel = invoiceListViewModel,
                ledgerColors = ledgerColors,
                detailPageNavigationCallback = provideDetailPageNavCallBacks(navController),
                onError = { ledgerCallbacks.exceptionHandler(it) }
            ) {
                navController.popBackStack()
            }
        }

        navBaseComposable(
            route = LedgerRoutes.TotalAvailableCreditLimitScreen.screen,
            logScreenName = ledgerCallbacks.firebaseScreenLogger
        ) {
            val uiState = it.arguments?.let { args -> AvailableCreditLimitScreenArgs(args) }
            AvailableCreditLimitDetailsScreen(
                uiState = uiState?.getArgs(),
                ledgerColors = ledgerColors
            ) {
                navController.popBackStack()
            }
        }

        navBaseComposable(
            route = LedgerRoutes.LedgerInvoiceDetailScreen.screen,
            logScreenName = ledgerCallbacks.firebaseScreenLogger
        ) {
            val invoiceDetailViewModel = hiltViewModel<InvoiceDetailViewModel>()
            invoiceDetailViewModel.setIsLmsActivated(viewModel.isLMSActivated())

            InvoiceDetailScreen(
                viewModel = invoiceDetailViewModel,
                ledgerColors = ledgerColors,
                onBackPress = {
                    navController.popBackStack()
                },
                onDownloadInvoiceClick = onDownloadClick
            )
        }

        navBaseComposable(
            route = LedgerRoutes.RevampLedgerInvoiceDetailScreen.screen,
            logScreenName = ledgerCallbacks.firebaseScreenLogger
        ) {
            val invoiceDetailViewModel = hiltViewModel<RevampInvoiceDetailViewModel>()
            RevampInvoiceDetailScreen(
                viewModel = invoiceDetailViewModel,
                ledgerColors = ledgerColors,
                onDownloadInvoiceClick = onDownloadClick,
                onError = { ledgerCallbacks.exceptionHandler(it) }
            ) {
                navController.popBackStack()
            }
        }

        navBaseComposable(
            route = LedgerRoutes.LedgerCreditNoteDetailScreen.screen,
            logScreenName = ledgerCallbacks.firebaseScreenLogger
        ) {
            val creditNoteDetailViewModel = hiltViewModel<CreditNoteDetailViewModel>()

            CreditNoteDetailScreen(
                viewModel = creditNoteDetailViewModel,
                ledgerColors = ledgerColors
            ) {
                navController.popBackStack()
            }

        }

        navBaseComposable(
            route = LedgerRoutes.RevampLedgerCreditNoteDetailScreen.screen,
            logScreenName = ledgerCallbacks.firebaseScreenLogger
        ) {
            val creditNoteDetailsViewModel = hiltViewModel<CreditNoteDetailsViewModel>()
            RevampCreditNoteDetailsScreen(
                viewModel = creditNoteDetailsViewModel,
                ledgerColors = ledgerColors,
                onError = { ledgerCallbacks.exceptionHandler(it) },
            ) {
                navController.popBackStack()
            }
        }

        navBaseComposable(
            route = LedgerRoutes.LedgerPaymentDetailScreen.screen,
            logScreenName = ledgerCallbacks.firebaseScreenLogger
        ) {
            val paymentDetailViewModel = hiltViewModel<PaymentDetailViewModel>()
            paymentDetailViewModel.setIsLmsActivated(viewModel.isLMSActivated())

            PaymentDetailScreen(
                viewModel = paymentDetailViewModel,
                ledgerColors = ledgerColors
            ) {
                navController.popBackStack()
            }
        }

        navBaseComposable(
            route = LedgerRoutes.RevampLedgerPaymentDetailScreen.screen,
            logScreenName = ledgerCallbacks.firebaseScreenLogger
        ) {
            val paymentDetailViewModel = hiltViewModel<PaymentDetailViewModel>()
            RevampPaymentDetailScreen(
                viewModel = paymentDetailViewModel,
                ledgerColors = ledgerColors,
                onError = { ledgerCallbacks.exceptionHandler(it) }
            ) {
                navController.popBackStack()
            }
        }

        navBaseComposable(
            route = LedgerRoutes.RevampLedgerWeeklyInterestDetailScreen.screen,
            logScreenName = ledgerCallbacks.firebaseScreenLogger
        ) {
            val interestViewData = it.arguments?.let { args -> InterestDetailScreenArgs(args) }
            InterestDetailScreen(
	            interestViewData = interestViewData?.getArgs(),
                ledgerColors = ledgerColors
            ) {
                navController.popBackStack()
            }
        }
    }
}

fun provideDetailPageNavCallBacks(
    navController: NavHostController
) = object : DetailPageNavigationCallback {

    override fun navigateToInvoiceDetailPage(args: Bundle) {
        navigateToInvoiceDetailScreen(
            navController = navController,
            args = args
        )
    }

    override fun navigateToCreditNoteDetailPage(args: Bundle) {
        navigateToCreditNoteDetailScreen(
            navController = navController,
            args = args
        )
    }

    override fun navigateToPaymentDetailPage(args: Bundle) {
        navigateToPaymentDetailScreen(
            navController = navController,
            args = args
        )
    }


    override fun navigateToOutstandingDetailPage(args: Bundle) {
        navigateToOutstandingDetailPage(navController, args)
    }

    override fun navigateToInvoiceListPage(args: Bundle) {
        navigateToInvoiceListPage(navController, args)
    }

    override fun navigateToAvailableCreditLimitDetailPage(args: Bundle) {
        navigateToAvailableCreditLimitDetailPage(navController, args)
    }

    override fun navigateToRevampInvoiceDetailPage(args: Bundle) {
        navigateToRevampInvoiceDetailPage(navController, args)
    }

    override fun navigateToRevampCreditNoteDetailPage(args: Bundle) {
        navigateToRevampCreditNoteDetailPage(navController, args)
    }

    override fun navigateToRevampPaymentDetailPage(args: Bundle) {
        navigateToRevampPaymentDetailPage(navController, args)
    }

    override fun navigateToRevampWeeklyInterestDetailPage(args: Bundle) {
        navigateToRevampWeeklyInterestDetailPage(navController, args)
    }
}
