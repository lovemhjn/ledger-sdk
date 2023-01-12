package lib.dehaat.ledger.presentation.ledger.transactions.ui.component

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.LibLedgerAnalytics
import lib.dehaat.ledger.presentation.LedgerConstants.ABS_AMOUNT
import lib.dehaat.ledger.presentation.ledger.ui.component.orZero
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.ABSViewData
import lib.dehaat.ledger.resources.BlueGreen10
import lib.dehaat.ledger.resources.Neutral70
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.TextLightGrey
import lib.dehaat.ledger.resources.text12Sp
import lib.dehaat.ledger.resources.textMedium14Sp
import lib.dehaat.ledger.resources.textMediumNoto14Sp
import lib.dehaat.ledger.resources.textMediumNoto16Sp
import lib.dehaat.ledger.resources.textNoto12Sp
import lib.dehaat.ledger.util.getAmountInRupees

@Composable
fun AbsBanner(
	abs: ABSViewData?,
	ledgerAnalytics: LibLedgerAnalytics,
	openABSDetailPage: (Bundle) -> Unit
) = Column(
	Modifier
		.padding(bottom = 16.dp)
		.background(BlueGreen10)
		.padding(horizontal = 20.dp)
		.padding(top = 8.dp, bottom = 12.dp)
) {
	Text(
		text = stringResource(R.string.advance_balance_booking), style = textMediumNoto14Sp(Neutral90)
	)
	if (!abs?.lastMoveScheme.isNullOrEmpty()) {
		Text(
			text = stringResource(R.string.adjust_remaining_balance, abs?.lastMoveScheme.orEmpty()),
			style = textNoto12Sp(TextLightGrey)
		)
	}
	Row(Modifier.padding(top = 18.dp)) {
		val amount = remember(abs?.amount) { abs?.amount?.orZero().toString().getAmountInRupees() }
		Text(text = amount, style = textMediumNoto16Sp(Neutral80))
		Spacer(Modifier.weight(1f))
		if (abs?.amount.orZero() > 0) {
			Row(modifier = Modifier.clickable {
				ledgerAnalytics.onABSViewDetailsClicked()
				openABSDetailPage(bundleOf(ABS_AMOUNT to abs?.amount.orZero()))
			}, verticalAlignment = Alignment.CenterVertically) {
				Text(text = stringResource(R.string.see_details), style = textMediumNoto16Sp(Neutral70))
				Image(
					painter = painterResource(R.drawable.ic_transaction_arrow_right),
					contentDescription = null,
					colorFilter = ColorFilter.tint(Neutral70),
					modifier = Modifier.padding(start = 4.dp)
				)
			}
		}
	}
	LaunchedEffect(Unit) {
		ledgerAnalytics.onABSWidgetViewed()
	}
}