package lib.dehaat.ledger.presentation.ledger.abs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.flow.SharedFlow
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.ledger.components.ShowProgress
import lib.dehaat.ledger.presentation.model.abs.ABSTransactionViewData
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.TextLightGrey
import lib.dehaat.ledger.resources.text12Sp
import lib.dehaat.ledger.resources.textHeadingH3
import lib.dehaat.ledger.resources.textParagraphT1Highlight
import lib.dehaat.ledger.resources.textSemiBold14Sp
import lib.dehaat.ledger.util.getAmountInRupees

@Composable
fun ABSDetailScreen(
    ledgerColors: LedgerColors,
    onBackPress: () -> Unit,
    viewModel: ABSDetailViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsState()
    val transactions = viewModel.transactions.collectAsLazyPagingItems()
    CommonContainer(
        title = stringResource(R.string.advanced_balance_details),
        onBackPress = onBackPress,
        ledgerColors = ledgerColors,
        scaffoldState = scaffoldState
    ) {
        ABSTransactions(transactions, uiState.amount, ledgerColors.AbsAmountColor)
        ShowProgress(ledgerColors, uiState.isLoading)
        ShowSnackBar(viewModel.uiEvent, scaffoldState)
    }
}

@Composable
private fun ABSTransactions(
    transactions: LazyPagingItems<ABSTransactionViewData>,
    amount: Double,
    absAmountColor: Color
) {
    LazyColumn {
        item {
            ABSTransactionsHeader(amount)
        }
        items(transactions) { transaction: ABSTransactionViewData? ->
            transaction?.let { ABSTransactionItem(transaction, absAmountColor) }
        }
    }
}

@Composable
private fun ABSTransactionsHeader(amount: Double) =
    Column(modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 24.dp)) {
        Text(
            text = stringResource(id = R.string.advance_balance),
            style = textParagraphT1Highlight(Neutral90)
        )
        Text(
            text = amount.toString().getAmountInRupees(),
            style = textHeadingH3(Neutral80)
        )
        VerticalSpacer(height = 16.dp)
        Text(
            text = stringResource(R.string.advance_bal_note),
            style = text12Sp(TextLightGrey)
        )
        Divider(modifier = Modifier.padding(top = 16.dp, bottom = 28.dp))
    }

@Composable
private fun ABSTransactionItem(transaction: ABSTransactionViewData, absAmountColor: Color) =
    with(transaction) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth()) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(R.drawable.ic_transactions_payment),
                    contentDescription = stringResource(id = R.string.accessibility_icon)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = schemeName ?: stringResource(R.string.ledger_payment),
                        style = textParagraphT1Highlight(Neutral80)
                    )
                    orderingDate?.let {
                        Text(
                            text = stringResource(
                                R.string.advanced_payment_blocked_till_s, orderingDate
                            ), style = text12Sp(TextLightGrey)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = amount.toString().getAmountInRupees(),
                    style = textSemiBold14Sp(absAmountColor)
                )
            }
            VerticalSpacer(height = 16.dp)
            Divider()
        }
    }

@Composable
private fun ShowSnackBar(uiEvent: SharedFlow<UiEvent>, scaffoldState: ScaffoldState) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { event ->
                if (event is UiEvent.ShowSnackbar) {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
            }
    }
}
