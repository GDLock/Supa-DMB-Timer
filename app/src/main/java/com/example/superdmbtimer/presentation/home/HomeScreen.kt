package com.example.superdmbtimer.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SportsScore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.superdmbtimer.R
import com.example.superdmbtimer.ui.components.CustomCard
import com.example.superdmbtimer.ui.components.TopBarTitle
import com.example.superdmbtimer.ui.theme.dimensions
import com.example.superdmbtimer.ui.theme.padding
import java.math.RoundingMode
import kotlin.math.roundToInt

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {

    val state by viewModel.state.collectAsState()

    HomeScaffold(
        name = state.name,
        remain = state.remain,
        percent = state.donePercent,
        done = state.done,
        navigate = { viewModel.action(HomeAction.Navigate) }
    )
}

@Composable
fun HomeScaffold(name: String, remain: Int, percent: Float, done: Int, navigate: () -> Unit) {

    Column {
        HomeTopBar(navigate)

        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceLarge))

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "$name идет к своей цели",
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceLarge))

        HomeProgress(percent)

        Spacer(Modifier.height(MaterialTheme.dimensions.spaceMedium))

        CustomCard(Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(MaterialTheme.padding.general)) {
                Summary(
                    time = done,
                    title = "Прошло",
                    alignment = Alignment.Start
                )
                Summary(
                    time = remain,
                    title = "Осталось",
                    alignment = Alignment.End
                )
            }
        }
    }
}

@Composable
fun ColumnScope.HomeProgress(percent: Float) {

    BoxWithConstraints {
        val width = with(LocalDensity.current) { constraints.maxWidth.toDp() }
        val padding = remember { 15.dp }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .padding(horizontal = padding)
                .align(Alignment.BottomStart)
        ) {
            val footPrintSize = remember { 12.dp }
            val flagSize = remember { 36.dp }
            val max =
                remember(width) { ((width - padding * 2 - flagSize) / footPrintSize).roundToInt() }
            val count = (percent * max).roundToInt()

            repeat(count) {
                Icon(
                    painter = painterResource(R.drawable.ic_footprint),
                    contentDescription = null,
                    modifier = Modifier
                        .size(footPrintSize)
                        .rotate(if (it % 2 == 0) 120f else 60f)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Outlined.SportsScore,
                contentDescription = null,
                modifier = Modifier.size(flagSize)
            )
        }
        Row {
            val chickenSize = remember { 64.dp }

            val walked = (width - chickenSize) * percent

            Spacer(modifier = Modifier.width(walked))
            Icon(
                painter = painterResource(R.drawable.ic_chicken),
                contentDescription = null,
                modifier = Modifier.size(chickenSize)
            )
        }
    }

    Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceLarge))

    val done = (percent * 100).toBigDecimal().setScale(6, RoundingMode.UP)
    Text(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        text = "$done %",
        style = MaterialTheme.typography.h5
    )
}

@Composable
fun HomeTopBar(navigate: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.padding.topBar)
    ) {
        TopBarTitle("Таймер")

        IconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = navigate
        ) {
            Icon(imageVector = Icons.Outlined.Settings, null)
        }
    }
}

@Composable
fun RowScope.Summary(
    time: Int,
    title: String,
    alignment: Alignment.Horizontal
) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = alignment
    ) {
        val days = time / 86400
        val hours = (time % 86400) / 3600
        val minutes = ((time % 86400) % 3600) / 60
        val seconds = ((time % 86400) % 3600) % 60

        val list = listOf(
            "$days дней",
            "$hours часов",
            "$minutes минут",
            "$seconds секунд"
        )

        Text(
            text = title,
            fontSize = 18.sp,
            color = MaterialTheme.colors.primary
        )
        Spacer(Modifier.height(MaterialTheme.dimensions.spaceSmall))

        list.forEach {
            Text(it)
            Spacer(Modifier.height(MaterialTheme.dimensions.spaceMin))
        }
    }
}