package lib.dehaat.ledger.presentation.ledger.details.loanlist.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.datasource.DummyDataSource
import lib.dehaat.ledger.initializer.themes.AIMSColors
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.initializer.toDateMonthName
import lib.dehaat.ledger.initializer.toDateMonthYear
import lib.dehaat.ledger.navigation.DetailPageNavigationCallback
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.common.uicomponent.HorizontalSpacer
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ShowProgress
import lib.dehaat.ledger.presentation.ledger.components.ShowProgressDialog
import lib.dehaat.ledger.presentation.ledger.details.invoice.RevampInvoiceDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.invoice.ui.InvoiceInformationChip
import lib.dehaat.ledger.presentation.ledger.details.loanlist.InvoiceListViewModel
import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.ledger.revamp.state.invoicelist.InvoiceLoadingState
import lib.dehaat.ledger.presentation.ledger.revamp.state.invoicelist.InvoiceUiState
import lib.dehaat.ledger.presentation.model.invoicelist.InvoiceListViewData
import lib.dehaat.ledger.resources.Background
import lib.dehaat.ledger.resources.Error100
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.resources.Neutral10
import lib.dehaat.ledger.resources.Neutral60
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.Pumpkin120
import lib.dehaat.ledger.resources.SeaGreen100
import lib.dehaat.ledger.resources.Warning10
import lib.dehaat.ledger.resources.textCaptionCP1
import lib.dehaat.ledger.resources.textParagraphT1Highlight
import lib.dehaat.ledger.resources.textSubHeadingS3
import lib.dehaat.ledger.util.getAmountInRupees

@Preview(
    showBackground = true,
    name = "Invoices not attracting Interest"
)
@Composable
private fun InvoiceWithUpcomingInterestPreview() = LedgerTheme {
    InvoiceWithUpcomingInterest(DummyDataSource.invoice) {}
}

@Preview(
    showBackground = true,
    name = "Invoices on which Interest is charged"
)
@Composable
private fun InvoiceWithAccumulatedInterestPreview() = LedgerTheme {
    InvoiceWithAccumulatedInterest(DummyDataSource.invoice) {}
}

@Preview(
    showBackground = true,
    name = "Invoice List Screen Preview"
)
@Composable
private fun InvoiceListScreenPreview() = LedgerTheme {
    InvoiceList(
        ledgerColors = AIMSColors(),
        interestDueDate = 623784623,
        amountDue = "40000",
        interestApproachedInvoices = InvoiceUiState(
            loadingState = InvoiceLoadingState.Minimize,
            invoices = listOf(
                DummyDataSource.invoice,
                DummyDataSource.invoice,
                DummyDataSource.invoice
            )
        ),
        interestApproachingInvoices = InvoiceUiState(
            loadingState = InvoiceLoadingState.Minimize,
            invoices = listOf(
                DummyDataSource.invoice,
                DummyDataSource.invoice,
                DummyDataSource.invoice
            )
        ),
        interestApproachingMinimizeList = {},
        interestApproachingLoadMore = {},
        interestApproachedMinimizeList = {},
        interestApproachedLoadMore = {}
    ) {}
}

@Composable
fun InvoiceListScreen(
    viewModel: InvoiceListViewModel,
    ledgerColors: LedgerColors,
    detailPageNavigationCallback: DetailPageNavigationCallback,
    onError: (Exception) -> Unit,
    onBackPress: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    CommonContainer(
        title = stringResource(id = R.string.invoice_list),
        onBackPress = onBackPress,
        scaffoldState = rememberScaffoldState(),
        backgroundColor = Background,
        ledgerColors = ledgerColors
    ) {
        when (uiState.state) {
            UIState.SUCCESS -> {
                InvoiceList(
                    ledgerColors = ledgerColors,
                    interestDueDate = viewModel.dueDate,
                    amountDue = viewModel.amountDue,
                    interestApproachedInvoices = uiState.interestApproachedInvoices,
                    interestApproachingInvoices = uiState.interestApproachingInvoices,
                    interestApproachedLoadMore = { viewModel.loadMoreInterestApproachedInvoices() },
                    interestApproachedMinimizeList = { viewModel.minimizeInterestApproachedList() },
                    interestApproachingLoadMore = { viewModel.loadMoreInterestApproachingInvoices() },
                    interestApproachingMinimizeList = { viewModel.minimizeInterestApproachingList() }
                ) { transaction ->
                    detailPageNavigationCallback.navigateToRevampInvoiceDetailPage(
                        RevampInvoiceDetailViewModel.getBundle(
                            transaction.ledgerId,
                            transaction.source,
                            null
                        )
                    )
                }
            }
            UIState.LOADING -> {
                ShowProgressDialog(ledgerColors) {}
            }
            is UIState.ERROR -> {
                Column {
                    SaveInterestHeader(
                        interestDueDate = viewModel.dueDate,
                        amountDue = viewModel.amountDue
                    )
                    NoDataFound((uiState.state as? UIState.ERROR)?.message, onError)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InvoiceList(
    ledgerColors: LedgerColors,
    interestDueDate: Long?,
    amountDue: String?,
    interestApproachedInvoices: InvoiceUiState?,
    interestApproachingInvoices: InvoiceUiState?,
    interestApproachedLoadMore: () -> Unit,
    interestApproachedMinimizeList: () -> Unit,
    interestApproachingLoadMore: () -> Unit,
    interestApproachingMinimizeList: () -> Unit,
    onInvoiceClick: (InvoiceListViewData) -> Unit
) = LazyColumn(
    modifier = Modifier
        .fillMaxWidth(),
) {
    item {
        SaveInterestHeader(
            interestDueDate = interestDueDate,
            amountDue = amountDue
        )
    }
    item { VerticalSpacer(height = 16.dp) }
    interestApproachedInvoices?.let { invoiceState ->
        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 22.dp, bottom = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ledger_attention),
                        contentDescription = stringResource(id = R.string.accessibility_icon),
                        tint = Error100
                    )
                    HorizontalSpacer(width = 10.dp)
                    Text(
                        text = stringResource(id = R.string.invoice_on_which_interest_is_charged),
                        style = textSubHeadingS3(Error100)
                    )
                }
                Divider()
            }
        }

        item { White16Spacer() }

        item {
            invoiceState.invoices.forEach {
                InvoiceWithAccumulatedInterest(it) { onInvoiceClick(it) }
            }
        }

        item {
            White16Spacer()
            InvoiceListFooter(invoiceState.loadingState, ledgerColors) {
                if (invoiceState.loadingState is InvoiceLoadingState.LoadMore) {
                    interestApproachedLoadMore()
                } else {
                    interestApproachedMinimizeList()
                }
            }
            White16Spacer()
        }
    }
    item { VerticalSpacer(height = 16.dp) }
    interestApproachingInvoices?.let { invoiceState ->
        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp, bottom = 12.dp),
                    text = stringResource(id = R.string.invoice_on_which_interest_going_to_charged),
                    style = textSubHeadingS3(Neutral80)
                )

                Divider()
            }
        }

        item { White16Spacer() }

        item {
            invoiceState.invoices.forEach {
                InvoiceWithUpcomingInterest(it) { onInvoiceClick(it) }
            }
        }

        item {
            White16Spacer()
            InvoiceListFooter(invoiceState.loadingState, ledgerColors) {
                if (invoiceState.loadingState is InvoiceLoadingState.LoadMore) {
                    interestApproachingLoadMore()
                } else {
                    interestApproachingMinimizeList()
                }
            }
            White16Spacer()
        }
    }
}

@Composable
private fun SaveInterestHeader(
    interestDueDate: Long?,
    amountDue: String?,
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .background(Warning10)
        .padding(horizontal = 20.dp)
        .padding(top = 24.dp, bottom = 16.dp)
) {
    Text(
        text = stringResource(id = R.string.save_interest),
        style = textSubHeadingS3(Pumpkin120)
    )

    VerticalSpacer(height = 4.dp)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        interestDueDate?.let {
            Text(
                text = stringResource(
                    id = R.string.pay_till_date,
                    it.toDateMonthName()
                ),
                style = textParagraphT1Highlight(Neutral90)
            )
        }

        amountDue?.let {
            Text(
                text = it.getAmountInRupees(),
                style = textSubHeadingS3(Neutral90)
            )
        }
    }
}

@Composable
private fun InvoiceWithAccumulatedInterest(
    invoiceListViewData: InvoiceListViewData,
    onClick: () -> Unit
) = Column(
    modifier = Modifier
        .clickable(onClick = onClick)
        .background(Color.White)
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .padding(top = 12.dp)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_revamp_invoice),
            contentDescription = stringResource(id = R.string.accessibility_icon)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.invoice),
                    style = textParagraphT1Highlight(Neutral80)
                )
                Text(
                    text = stringResource(
                        id = R.string.outstanding_amount_rs,
                        invoiceListViewData.outstandingAmount.getAmountInRupees()
                    ),
                    style = textParagraphT1Highlight(Error100)
                )
            }
            VerticalSpacer(height = 4.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(
                        id = R.string.until_today,
                        invoiceListViewData.date.toDateMonthYear()
                    ),
                    style = textCaptionCP1(Neutral60)
                )

                InvoiceInformationChip(
                    title = stringResource(
                        id = R.string.invoice_amount_rs,
                        invoiceListViewData.amount.getAmountInRupees()
                    ),
                    backgroundColor = Neutral10,
                    textColor = Neutral80
                )
            }
        }
    }
    VerticalSpacer(height = 16.dp)
    Divider()
}

@Composable
fun InvoiceWithUpcomingInterest(
    invoiceListViewData: InvoiceListViewData,
    onClick: () -> Unit
) = Column(
    modifier = Modifier
        .clickable(onClick = onClick)
        .background(Color.White)
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .padding(top = 12.dp)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_revamp_invoice),
            contentDescription = stringResource(id = R.string.accessibility_icon)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.invoice),
                    style = textParagraphT1Highlight(Neutral80)
                )
                Text(
                    text = invoiceListViewData.amount.getAmountInRupees(),
                    style = textParagraphT1Highlight(Neutral80)
                )
            }
            VerticalSpacer(height = 4.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(
                        id = R.string.until_today,
                        invoiceListViewData.date.toDateMonthYear()
                    ),
                    style = textCaptionCP1(Neutral60)
                )

                InvoiceInformationChip(
                    title = invoiceListViewData.interestDays?.let {
                        if (it == 0) {
                            stringResource(id = R.string.interest_charged_from_tomorrow)
                        } else {
                            stringResource(
                                R.string.interest_will_be_starting_in,
                                invoiceListViewData.interestDays.toString()
                            )
                        }
                    } ?: "",
                    backgroundColor = Warning10,
                    textColor = Pumpkin120
                )
            }
        }
    }
    VerticalSpacer(height = 16.dp)
    Divider()
}

@Composable
private fun InvoiceListFooter(
    state: InvoiceLoadingState,
    ledgerColors: LedgerColors,
    onClick: () -> Unit
) = Box(
    modifier = Modifier
        .fillMaxWidth()
        .background(Color.White),
    contentAlignment = Alignment.Center
) {
    when (state) {
        InvoiceLoadingState.Loading -> ShowProgress(ledgerColors)
        InvoiceLoadingState.LoadMore -> {
            Text(
                modifier = Modifier
                    .clickable(onClick = onClick)
                    .border(
                        shape = RoundedCornerShape(8.dp),
                        color = SeaGreen100, width = 1.dp
                    )
                    .padding(vertical = 14.dp, horizontal = 35.dp),
                text = stringResource(R.string.load_more),
                style = textSubHeadingS3(SeaGreen100),
                textAlign = TextAlign.Center
            )
        }
        InvoiceLoadingState.Minimize -> {
            Text(
                modifier = Modifier
                    .clickable(onClick = onClick)
                    .border(
                        shape = RoundedCornerShape(8.dp),
                        color = SeaGreen100, width = 1.dp
                    )
                    .padding(vertical = 14.dp, horizontal = 35.dp),
                text = stringResource(R.string.close_list),
                style = textSubHeadingS3(SeaGreen100),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun White16Spacer() = Column(
    modifier = Modifier
        .fillMaxWidth()
        .height(16.dp)
        .background(Color.White),
    content = {}
)
