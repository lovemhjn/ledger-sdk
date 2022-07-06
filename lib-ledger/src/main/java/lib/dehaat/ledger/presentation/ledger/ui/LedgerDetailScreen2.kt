@file:OptIn(ExperimentalPagerApi::class)

package lib.dehaat.ledger.presentation.ledger.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.navigation.DetailPageNavigationCallback
import lib.dehaat.ledger.presentation.LedgerDetailViewModel
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.ledger.bottomsheets.DaysToFilterContent
import lib.dehaat.ledger.presentation.ledger.bottomsheets.LenderOutStandingDetails
import lib.dehaat.ledger.presentation.ledger.bottomsheets.OverAllOutStandingDetails
import lib.dehaat.ledger.presentation.ledger.credits.ui.CreditsScreen
import lib.dehaat.ledger.presentation.ledger.state.BottomSheetType
import lib.dehaat.ledger.presentation.ledger.transactions.ui.TransactionsListScreen
import lib.dehaat.ledger.presentation.ledger.ui.component.Header
import lib.dehaat.ledger.presentation.ledger.ui.component.Tabs
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun LedgerDetailScreen2(
    viewModel: LedgerDetailViewModel,
    ledgerColors: LedgerColors,
    onBackPress: () -> Unit,
    detailPageNavigationCallback: DetailPageNavigationCallback,
    isLmsActivated: () -> Boolean,
    onPayNowClick: () -> Unit,
    onPaymentOptionsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = {
            if (it == ModalBottomSheetValue.Hidden) {
            }
            true
        })
    if (uiState.isFilteringWithRange) {
        RangeFilterDialog(startRange = {}, endRange = {})
    }

    CommonContainer(
        title = viewModel.dcName,
        onBackPress = onBackPress,
        scaffoldState = scaffoldState
    ) {

        ModalBottomSheetLayout(
            modifier = Modifier,
            sheetContent = {

                when (val bottomSheetType = uiState.bottomSheetType) {
                    is BottomSheetType.LenderOutStanding -> LenderOutStandingDetails(
                        data = bottomSheetType.data,
                        ledgerColors = ledgerColors
                    )
                    is BottomSheetType.OverAllOutStanding ->
                        OverAllOutStandingDetails(
                            data = bottomSheetType.data,
                            ledgerColors = ledgerColors
                        )
                    is BottomSheetType.DaysFilterTypeSheet -> DaysToFilterContent(
                        selectedFilter = bottomSheetType.selectedFilter,
                        ledgerColors = ledgerColors,
                        onFilterSelected = {
                            viewModel.updateSelectedFilter(it)
                            scope.launch {
                                sheetState.animateTo(ModalBottomSheetValue.Hidden)
                            }
                        }
                    )
                }

            },
            sheetBackgroundColor = Color.White,
            sheetState = sheetState,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        ) {
            val nestedScrollViewState = rememberNestedScrollViewState()
            VerticalNestedScrollView(
                state = nestedScrollViewState,
                header = {
                    Header(
                        creditSummaryData = uiState.creditSummaryViewData,
                        ledgerColors = ledgerColors,
                        onPayNowClick = onPayNowClick,
                        onClickTotalOutstandingInfo = {
                            scope.launch {
                                viewModel.openAllOutstandingModal()
                                sheetState.animateTo(ModalBottomSheetValue.Expanded)
                            }
                        },
                        onPaymentOptionsClick = onPaymentOptionsClick
                    )
                },
                content = {
                    val pagerState = rememberPagerState(pageCount = 2)
                    Column(modifier = Modifier.background(ledgerColors.TransactionAndCreditScreenBGColor)) {
                        Tabs(pagerState, ledgerColors) { currentPage ->
                            scope.launch {
                                pagerState.animateScrollToPage(page = currentPage)
                            }
                        }
                        HorizontalPager(
                            modifier = Modifier
                                .weight(1f)
                                .background(color = ledgerColors.TransactionAndCreditScreenBGColor),
                            state = pagerState,
                            verticalAlignment = Alignment.Top
                        ) { index ->
                            when (index) {
                                1 -> CreditsScreen(
                                    ledgerDetailViewModel = viewModel,
                                    ledgerColors = ledgerColors,
                                    isLmsActivated = isLmsActivated,
                                ) {
                                    viewModel.showLenderOutstandingModal(it)
                                    scope.launch { sheetState.animateTo(ModalBottomSheetValue.Expanded) }
                                }
                                else -> TransactionsListScreen(
                                    ledgerColors = ledgerColors,
                                    detailPageNavigationCallback = detailPageNavigationCallback,
                                    ledgerDetailViewModel = viewModel,
                                    openDaysFilter = {
                                        viewModel.showDaysFilterBottomSheet()
                                        scope.launch { sheetState.animateTo(ModalBottomSheetValue.Expanded) }
                                    },
                                    openRangeFilter = {
                                        viewModel.showDaysRangeFilterDialog(true)
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}
