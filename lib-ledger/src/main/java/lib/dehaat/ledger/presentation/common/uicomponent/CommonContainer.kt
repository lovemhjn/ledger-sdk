package lib.dehaat.ledger.presentation.common.uicomponent

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.themes.LedgerColors

@Composable
fun CommonContainer(
	modifier: Modifier = Modifier,
	title: String,
	onBackPress: () -> Unit,
	ledgerColors: LedgerColors,
	scaffoldState: ScaffoldState = rememberScaffoldState(),
	backgroundColor: Color = Color.White,
	bottomBar: @Composable () -> Unit = {},
	content: @Composable (PaddingValues) -> Unit
) {
	Scaffold(
		modifier = modifier,
		scaffoldState = scaffoldState,
		topBar = {
			CustomAppBar(
				title,
				onBackPress = onBackPress,
				ledgerColors = ledgerColors
			)
		},
		backgroundColor = backgroundColor,
		bottomBar = bottomBar,
		content = content
	)
}
