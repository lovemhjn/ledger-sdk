package lib.dehaat.ledger.presentation.ledger.revamp.state.creditnote.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dehaat.androidbase.helper.isNotNull
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.initializer.toDateMonthYear
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ShowProgressDialog
import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.ledger.revamp.state.creditnote.CreditNoteDetailsViewModel
import lib.dehaat.ledger.presentation.ledger.ui.component.ProductDetailsScreen
import lib.dehaat.ledger.presentation.ledger.ui.component.RevampKeyValuePair
import lib.dehaat.ledger.resources.Background
import lib.dehaat.ledger.resources.BlueGreen10
import lib.dehaat.ledger.resources.FrenchBlue120
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.textButtonB2
import lib.dehaat.ledger.resources.textParagraphT2Highlight

@Composable
fun RevampCreditNoteDetailsScreen(
    viewModel: CreditNoteDetailsViewModel,
    ledgerColors: LedgerColors,
    onError: (Exception) -> Unit,
    onBackPress: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val summary = uiState.viewData?.summary
    CommonContainer(
        title = stringResource(id = R.string.credit_note_details),
        onBackPress = onBackPress,
        scaffoldState = rememberScaffoldState(),
        backgroundColor = Background,
        ledgerColors = ledgerColors
    ) {
        when (uiState.state) {
            UIState.SUCCESS -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 20.dp)
                    ) {
                        VerticalSpacer(height = 24.dp)
                        Text(
                            modifier = Modifier
                                .background(color = BlueGreen10, RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            text = summary?.reason ?: "",
                            style = textParagraphT2Highlight(FrenchBlue120)
                        )

                        VerticalSpacer(height = 20.dp)

                        RevampKeyValuePair(
                            pair = Pair(
                                stringResource(id = R.string.credit_note_amount),
                                summary?.amount ?: ""
                            ),
                            style = Pair(
                                textParagraphT2Highlight(Neutral90),
                                textButtonB2(Neutral90)
                            )
                        )

                        RevampKeyValuePair(
                            pair = Pair(
                                stringResource(id = R.string.credit_note_creation_date),
                                summary?.timestamp.toDateMonthYear()
                            ),
                            style = Pair(
                                textParagraphT2Highlight(Neutral90),
                                textParagraphT2Highlight(Neutral80)
                            )
                        )

                        VerticalSpacer(height = 16.dp)
                    }

                    VerticalSpacer(height = 16.dp)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 20.dp)
                    ) {
                        VerticalSpacer(height = 12.dp)
                        RevampKeyValuePair(
                            pair = Pair(
                                stringResource(id = R.string.invoice_id),
                                summary?.invoiceNumber ?: ""
                            ),
                            style = Pair(
                                textParagraphT2Highlight(Neutral80),
                                textButtonB2(Neutral90)
                            )
                        )

                        if (summary?.invoiceDate.isNotNull()) {
                            RevampKeyValuePair(
                                pair = Pair(
                                    stringResource(id = R.string.invoice_date),
                                    summary?.invoiceDate.toDateMonthYear()
                                ),
                                style = Pair(
                                    textParagraphT2Highlight(Neutral80),
                                    textParagraphT2Highlight(Neutral90)
                                )
                            )
                        }

                        VerticalSpacer(height = 12.dp)
                    }
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ProductDetailsScreen(
                            productDetails = uiState.viewData?.productsInfo
                        )
                    }
                }
            }
            UIState.LOADING -> {
                ShowProgressDialog(ledgerColors) {
                    viewModel.updateProgressDialog(false)
                }
            }
            is UIState.ERROR -> {
                NoDataFound((uiState.state as? UIState.ERROR)?.message, onError)
            }
        }
    }
}
