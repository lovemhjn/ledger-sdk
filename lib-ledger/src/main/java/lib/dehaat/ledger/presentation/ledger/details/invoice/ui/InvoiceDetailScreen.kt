package lib.dehaat.ledger.presentation.ledger.details.invoice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dehaat.androidbase.helper.showToast
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.initializer.toDateMonthYear
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.common.uicomponent.SpaceMedium
import lib.dehaat.ledger.presentation.common.uicomponent.SpaceSmall12
import lib.dehaat.ledger.presentation.ledger.components.CreditNoteKeyValue
import lib.dehaat.ledger.presentation.ledger.components.CreditNoteKeyValueInSummaryView
import lib.dehaat.ledger.presentation.ledger.components.CreditNoteKeyValueInSummaryViewWithTopPadding
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ProductView
import lib.dehaat.ledger.presentation.ledger.components.ShowProgressDialog
import lib.dehaat.ledger.presentation.ledger.details.invoice.InvoiceDetailViewModel
import lib.dehaat.ledger.presentation.model.invoicedownload.InvoiceDownloadData
import lib.dehaat.ledger.resources.text18Sp
import lib.dehaat.ledger.resources.textBold14Sp
import lib.dehaat.ledger.resources.textMedium14Sp
import lib.dehaat.ledger.util.HandleAPIErrors
import lib.dehaat.ledger.util.getAmountInRupees

@Composable
fun InvoiceDetailScreen(
    viewModel: InvoiceDetailViewModel,
    ledgerColors: LedgerColors,
    onBackPress: () -> Unit,
    onDownloadInvoiceClick: (InvoiceDownloadData) -> Unit
) {
    HandleAPIErrors(viewModel.uiEvent)

    val uiState by viewModel.uiState.collectAsState()
    val invoiceData = uiState.invoiceDetailDataViewData
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    CommonContainer(
        title = "Invoice Detail",
        onBackPress = onBackPress,
        ledgerColors = ledgerColors
    ) {
        when {
            uiState.isLoading -> {
                ShowProgressDialog(ledgerColors) {
                    viewModel.updateProgressDialog(false)
                }
            }
            uiState.isError -> {
                NoDataFound{}
            }
            else -> {
                Column(
                    modifier = Modifier
                        .verticalScroll(
                            state = scrollState,
                            enabled = true,
                        )
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            invoiceData?.summary?.let { summary ->
                                CreditNoteKeyValue(
                                    key = "Invoice Amount",
                                    value = summary.amount.getAmountInRupees(),
                                    keyTextStyle = text18Sp(
                                        fontWeight = FontWeight.Bold,
                                        textColor = ledgerColors.CtaDarkColor
                                    ),
                                    valueTextStyle = text18Sp(
                                        fontWeight = FontWeight.Bold,
                                        textColor = ledgerColors.CtaDarkColor
                                    )
                                )

                                SpaceSmall12()

                                CreditNoteKeyValue(
                                    key = "Invoice ID",
                                    value = summary.number,
                                    keyTextStyle = text18Sp(
                                        textColor = ledgerColors.CtaDarkColor
                                    ),
                                    valueTextStyle = text18Sp(
                                        textColor = ledgerColors.CtaDarkColor
                                    )
                                )

                                SpaceSmall12()

                                CreditNoteKeyValue(
                                    key = "Invoice Date",
                                    value = summary.timestamp.toDateMonthYear(),
                                    keyTextStyle = text18Sp(
                                        textColor = ledgerColors.CtaDarkColor
                                    ),
                                    valueTextStyle = text18Sp(
                                        textColor = ledgerColors.CtaDarkColor
                                    )
                                )
                            }
                        }
                    }

                    /*Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {

                        SpaceMedium()

                        invoiceData?.loans?.forEachIndexed { index, loan ->
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                if (index == 0) {
                                    Text(
                                        text = "This invoice is part of a credit whose details are as mentioned below:",
                                        style = text16Sp()
                                    )
                                }

                                SpaceSmall12()

                                Divider()

                                Spacer(modifier = Modifier.height(8.dp))

                                if (!loan.belongsToGapl) {
                                    CreditNoteKeyValueInSummaryView(
                                        "Credit Account Number",
                                        loan.loanAccountNo,
                                        ledgerColors = ledgerColors,
                                    )

                                    CreditNoteKeyValueInSummaryViewWithTopPadding(
                                        "Credit Status",
                                        loan.status,
                                        ledgerColors = ledgerColors,
                                    )
                                }

                                CreditNoteKeyValueInSummaryViewWithTopPadding(
                                    "Credit Amount",
                                    loan.amount.getAmountInRupees(),
                                    ledgerColors = ledgerColors,
                                )

                                CreditNoteKeyValueInSummaryViewWithTopPadding(
                                    "Invoice Contribution in Credit Amount",
                                    loan.invoiceContributionInLoan.getAmountInRupees(),
                                    ledgerColors = ledgerColors,
                                )

                                if (!loan.belongsToGapl) {
                                    CreditNoteKeyValueInSummaryViewWithTopPadding(
                                        "Outstanding",
                                        loan.totalOutstandingAmount.getAmountInRupeesOrDash(),
                                        ledgerColors = ledgerColors,
                                    )

                                    CreditNoteKeyValueInSummaryViewWithTopPadding(
                                        "Principal o/s",
                                        loan.principalOutstandingAmount.getAmountInRupeesOrDash(),
                                        ledgerColors = ledgerColors,
                                    )

                                    CreditNoteKeyValueInSummaryViewWithTopPadding(
                                        "Interest o/s",
                                        loan.interestOutstandingAmount.getAmountInRupeesOrDash(),
                                        ledgerColors = ledgerColors,
                                    )

                                    CreditNoteKeyValueInSummaryViewWithTopPadding(
                                        "Penalty o/s",
                                        loan.penaltyOutstandingAmount.getAmountInRupeesOrDash(),
                                        ledgerColors = ledgerColors,
                                    )

                                    CreditNoteKeyValueInSummaryViewWithTopPadding(
                                        "Overdue Interest o/s",
                                        loan.overdueInterestOutstandingAmount.getAmountInRupeesOrDash(),
                                        ledgerColors = ledgerColors,
                                    )
                                }

                                CreditNoteKeyValueInSummaryViewWithTopPadding(
                                    "Disbursal Date",
                                    loan.disbursalDate.toDateMonthYear(),
                                    ledgerColors = ledgerColors,
                                )

                                if (!loan.belongsToGapl) {
                                    CreditNoteKeyValueInSummaryViewWithTopPadding(
                                        "Interest-Free Period End Date",
                                        loan.interestFreeEndDate.toDateMonthYear(),
                                        ledgerColors = ledgerColors,
                                    )
                                }

                                CreditNoteKeyValueInSummaryViewWithTopPadding(
                                    "Financier",
                                    loan.financier,
                                    ledgerColors = ledgerColors,
                                )
                                if (invoiceData.loans.size > 1 && index == 0) {
                                    Text(
                                        text = "This credit is also mapped to the below invoices:",
                                        style = text14Sp()
                                    )
                                }
                            }
                        }
                    }*/

                    invoiceData?.overdueInfo?.overdueDate?.let {
                        if (viewModel.isLmsActivated() == false) {
                            SpaceMedium()

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                CreditNoteKeyValue(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 16.dp
                                    ),
                                    key = "Overdue Date",
                                    value = it.toDateMonthYear(),
                                    keyTextStyle = text18Sp(
                                        textColor = ledgerColors.CtaDarkColor
                                    ),
                                    valueTextStyle = text18Sp(
                                        textColor = ledgerColors.CtaDarkColor
                                    )
                                )
                            }
                        }
                    }

                    SpaceMedium()

                    Column(modifier = Modifier) {
                        Text(
                            modifier = Modifier,
                            text = "Product Details",
                            style = text18Sp(textColor = ledgerColors.CtaDarkColor),
                            maxLines = 1
                        )
                        val products = invoiceData?.productsInfo?.productList.orEmpty()
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = "Items: ${products.size}",
                            style = textMedium14Sp(textColor = ledgerColors.CtaColor),
                            maxLines = 1
                        )

                        SpaceMedium()
                        products.forEachIndexed { index, product ->
                            ProductView(
                                modifier = Modifier.padding(end = 16.dp),
                                ledgerColors = ledgerColors,
                                name = product.name,
                                image = product.fname,
                                qty = product.quantity,
                                price = product.priceTotal
                            )
                            if (index < products.lastIndex)
                                Divider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = ledgerColors.CreditViewHeaderDividerBColor,
                                    thickness = 1.dp
                                )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .background(
                                shape = RoundedCornerShape(9.dp),
                                color = ledgerColors.InfoContainerBgColor
                            )
                            .padding(16.dp)
                    ) {
                        invoiceData?.productsInfo?.itemTotal?.let {
                            CreditNoteKeyValueInSummaryView(
                                "Item Total", it.getAmountInRupees(),
                                ledgerColors = ledgerColors
                            )
                        }

                        invoiceData?.productsInfo?.discount?.let {
                            CreditNoteKeyValueInSummaryViewWithTopPadding(
                                "Discount",
                                it.getAmountInRupees(),
                                ledgerColors = ledgerColors,
                                valueTextStyle = textBold14Sp(textColor = ledgerColors.DownloadInvoiceColor),
                            )
                        }

                        invoiceData?.productsInfo?.gst?.let {
                            CreditNoteKeyValueInSummaryViewWithTopPadding(
                                "GST",
                                it.getAmountInRupees(),
                                ledgerColors = ledgerColors
                            )
                        }

                        Divider(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .background(color = ledgerColors.TabBorderColorDefault),
                            thickness = 1.dp
                        )
                        invoiceData?.productsInfo?.subTotal?.let {
                            CreditNoteKeyValue(
                                "Total Amount",
                                it.getAmountInRupees(),
                                keyTextStyle = text18Sp(textColor = ledgerColors.CtaDarkColor),
                                valueTextStyle = text18Sp(
                                    fontWeight = FontWeight.Bold,
                                    textColor = ledgerColors.CtaDarkColor
                                ),
                                modifier = Modifier,
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .clickable {
                                    LedgerSDK
                                        .getFile(context)
                                        ?.let {
                                            viewModel.downloadInvoice(
                                                it,
                                                onDownloadInvoiceClick
                                            )
                                        } ?: kotlin.run {
                                        context.showToast(R.string.tech_problem)
                                        LedgerSDK.currentApp.ledgerCallBack.exceptionHandler(
                                            Exception("Unable to create file")
                                        )
                                    }
                                }
                                .padding(top = 16.dp)
                                .background(shape = RoundedCornerShape(40.dp), color = Color.White)
                                .border(
                                    width = 1.dp,
                                    color = ledgerColors.DownloadInvoiceColor,
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .padding(vertical = 16.dp, horizontal = 40.dp),
                            text = "Download Invoice",
                            color = ledgerColors.DownloadInvoiceColor,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
