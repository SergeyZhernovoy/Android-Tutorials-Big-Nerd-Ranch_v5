package com.learning.codapizza.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.learning.codapizza.model.Topping
import com.learning.codapizza.model.ToppingPlacement
import com.learning.codapizza.ui.theme.CodaPizzaTheme

@Composable
fun ToppingCell(topping: Topping,
                placement: ToppingPlacement?,
                modifier: Modifier = Modifier,
                onClickTopping: () -> Unit) {
    //Log.d("ToppingCell", "Called ToppingCell for $topping")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClickTopping() }
            .padding(vertical = 4.dp, horizontal = 16.dp)

    ) {

        Checkbox(
            checked = (placement != null),
            onCheckedChange = { onClickTopping() }
        )
        Column(modifier = Modifier.weight(1f, fill = true)
            .padding(start = 4.dp)) {
            Text(
                text = stringResource(topping.toppingName),
                style = MaterialTheme.typography.bodyLarge
            )
            placement?.let {
                Text(
                    text = stringResource(it.label),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun ToppingCellPreview() {
    CodaPizzaTheme {
        ToppingCell(
            topping = Topping.Pepperoni,
            placement = ToppingPlacement.Left,
            onClickTopping = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun ToppingCellPreviewNotOnPizza() {
    CodaPizzaTheme {
        ToppingCell(
            topping = Topping.Pepperoni,
            placement = null,
            onClickTopping = {}
        )
    }
}