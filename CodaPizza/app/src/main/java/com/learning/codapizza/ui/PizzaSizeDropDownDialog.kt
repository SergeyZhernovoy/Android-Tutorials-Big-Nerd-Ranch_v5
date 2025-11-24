package com.learning.codapizza.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.learning.codapizza.R
import com.learning.codapizza.model.Pizza
import com.learning.codapizza.model.PizzaSize

@Composable
fun PizzaSizeDropDownDialog(
    pizza: Pizza,
    onEditPizza: (Pizza) -> Unit,
    onDismissRequest: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box( modifier = Modifier.fillMaxWidth().padding(16.dp) ) {
        IconButton(onClick = { expanded = !expanded }) {}
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.pizza_small_size)) },
                onClick = {
                    expanded = false
                    onEditPizza(pizza.withSize(PizzaSize.Small))
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.pizza_medium_size)) },
                onClick = {
                    expanded = false
                    onEditPizza(pizza.withSize(PizzaSize.Medium))
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.pizza_large_size)) },
                onClick = {
                    expanded = false
                    onEditPizza(pizza.withSize(PizzaSize.Large))
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.pizza_extra_size)) },
                onClick = {
                    expanded = false
                    onEditPizza(pizza.withSize(PizzaSize.Extra))
                }
            )
        }
    }
}