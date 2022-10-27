package lib.dehaat.ledger.presentation.ledger.details.creditnote.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.initializer.toDateMonthYear
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.common.uicomponent.SpaceMedium
import lib.dehaat.ledger.presentation.ledger.components.CreditNoteKeyValue
import lib.dehaat.ledger.presentation.ledger.components.CreditNoteKeyValueInSummaryView
import lib.dehaat.ledger.presentation.ledger.components.CreditNoteKeyValueInSummaryViewWithTopPadding
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ProductView
import lib.dehaat.ledger.presentation.ledger.components.ShowProgressDialog
import lib.dehaat.ledger.presentation.ledger.details.creditnote.CreditNoteDetailViewModel
import lib.dehaat.ledger.resources.text18Sp
import lib.dehaat.ledger.resources.textMedium14Sp
import lib.dehaat.ledger.util.HandleAPIErrors
import lib.dehaat.ledger.util.getAmountInRupees

@Composable
fun CreditNoteDetailScreen(
    viewModel: CreditNoteDetailViewModel,
    ledgerColors: LedgerColors,
    onBackPress: () -> Unit
) {
    HandleAPIErrors(viewModel.uiEvent)
    val uiState by viewModel.uiState.collectAsState()
    val creditNoteDetailViewData = uiState.creditNoteDetailViewData
    val scrollState = rememberScrollState()

    CommonContainer(
        title = stringResource(id = R.string.credit_note_details),
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
                NoDataFound {}
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

                    CreditNoteKeyValue(
                        "Credit Note Amount",
                        creditNoteDetailViewData?.summary?.amount.getAmountInRupees(),
                        keyTextStyle = text18Sp(textColor = ledgerColors.CtaDarkColor),
                        valueTextStyle = text18Sp(
                            fontWeight = FontWeight.Bold,
                            textColor = ledgerColors.CtaDarkColor
                        ),
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .background(
                                shape = RoundedCornerShape(9.dp),
                                color = ledgerColors.CreditNoteReasonLightColor
                            )
                            .padding(17.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = ledgerColors.CreditNoteReasonDarkColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Reason: ")
                            }

                            withStyle(
                                style = SpanStyle(
                                    fontSize = 14.sp,
                                    color = ledgerColors.CreditNoteReasonDarkColor,
                                )
                            ) {
                                append(creditNoteDetailViewData?.summary?.reason ?: "")
                            }
                        })
                    }

                    Column(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .background(
                                shape = RoundedCornerShape(9.dp),
                                color = ledgerColors.InfoContainerBgColor
                            )
                            .padding(16.dp)
                    ) {
                        CreditNoteKeyValueInSummaryView(
                            "Credit Note Creation date",
                            creditNoteDetailViewData?.summary?.timestamp.toDateMonthYear(),
                            ledgerColors = ledgerColors
                        )
                        CreditNoteKeyValueInSummaryViewWithTopPadding(
                            "Invoice ID",
                            creditNoteDetailViewData?.summary?.invoiceNumber ?: "",
                            ledgerColors = ledgerColors
                        )
                    }

                    Divider(
                        modifier = Modifier.padding(vertical = 20.dp),
                        thickness = 4.dp,
                        color = ledgerColors.CreditViewHeaderDividerBColor
                    )

                    creditNoteDetailViewData?.productsInfo?.productList?.let {
                        Column(modifier = Modifier) {
                            Text(
                                modifier = Modifier,
                                text = "Product Details",
                                style = text18Sp(textColor = ledgerColors.CtaDarkColor),
                                maxLines = 1
                            )
                            Text(
                                modifier = Modifier.padding(top = 8.dp),
                                text = "Items: ${it.size}",
                                style = textMedium14Sp(textColor = ledgerColors.CtaColor),
                                maxLines = 1
                            )

                            SpaceMedium()

                            it.forEachIndexed { index, product ->
                                ProductView(
                                    modifier = Modifier.padding(end = 16.dp),
                                    ledgerColors = ledgerColors,
                                    name = product.name,
                                    image = product.fname,
                                    qty = product.quantity,
                                    price = product.priceTotal
                                )
                                if (index < it.lastIndex)
                                    Divider(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        color = ledgerColors.CreditViewHeaderDividerBColor,
                                        thickness = 1.dp
                                    )
                            }
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
                        CreditNoteKeyValueInSummaryView(
                            "Item Total",
                            creditNoteDetailViewData?.productsInfo?.itemTotal.getAmountInRupees(),
                            ledgerColors = ledgerColors
                        )
                        CreditNoteKeyValueInSummaryViewWithTopPadding(
                            "GST",
                            creditNoteDetailViewData?.productsInfo?.gst.getAmountInRupees(),
                            ledgerColors = ledgerColors
                        )

                        Divider(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .background(color = ledgerColors.TabBorderColorDefault),
                            thickness = 1.dp
                        )

                        CreditNoteKeyValue(
                            "Total Amount",
                            creditNoteDetailViewData?.productsInfo?.subTotal.getAmountInRupees(),
                            keyTextStyle = text18Sp(textColor = ledgerColors.CtaDarkColor),
                            valueTextStyle = text18Sp(
                                fontWeight = FontWeight.Bold,
                                textColor = ledgerColors.CtaDarkColor
                            ),
                            modifier = Modifier,
                        )
                    }
                }
            }
        }
    }
}
