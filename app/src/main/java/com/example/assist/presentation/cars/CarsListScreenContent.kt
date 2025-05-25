 package com.example.assist.presentation.cars

 import androidx.compose.foundation.BorderStroke
 import androidx.compose.foundation.background
 import androidx.compose.foundation.clickable
 import androidx.compose.foundation.interaction.MutableInteractionSource
 import androidx.compose.foundation.layout.Arrangement
 import androidx.compose.foundation.layout.Box
 import androidx.compose.foundation.layout.Column
 import androidx.compose.foundation.layout.fillMaxSize
 import androidx.compose.foundation.layout.fillMaxWidth
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.layout.size
 import androidx.compose.foundation.layout.systemBarsPadding
 import androidx.compose.foundation.lazy.LazyColumn
 import androidx.compose.foundation.lazy.items
 import androidx.compose.foundation.selection.selectable
 import androidx.compose.foundation.shape.CircleShape
 import androidx.compose.foundation.shape.RoundedCornerShape
 import androidx.compose.material.icons.Icons
 import androidx.compose.material.icons.filled.Add
 import androidx.compose.material.icons.filled.Delete
 import androidx.compose.material3.CardDefaults
 import androidx.compose.material3.ExperimentalMaterial3Api
 import androidx.compose.material3.FloatingActionButton
 import androidx.compose.material3.Icon
 import androidx.compose.material3.MaterialTheme
 import androidx.compose.material3.OutlinedCard
 import androidx.compose.material3.Text
 import androidx.compose.material3.TopAppBar
 import androidx.compose.material3.ripple
 import androidx.compose.runtime.Composable
 import androidx.compose.runtime.getValue
 import androidx.compose.runtime.mutableStateOf
 import androidx.compose.runtime.remember
 import androidx.compose.runtime.setValue
 import androidx.compose.ui.Alignment
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.draw.clip
 import androidx.compose.ui.graphics.Color
 import androidx.compose.ui.text.font.FontWeight
 import androidx.compose.ui.text.style.TextAlign
 import androidx.compose.ui.unit.dp
 import cafe.adriel.voyager.navigator.LocalNavigator
 import cafe.adriel.voyager.navigator.currentOrThrow
 import com.example.assist.domain.car.Car
 import com.example.assist.presentation.swipe.RevealSwipe

 @OptIn(ExperimentalMaterial3Api::class)
 @Composable
fun CarsListScreenContent(state: CarsListScreen.State, onAction: (CarsListScreen.Action) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        val navigator = LocalNavigator.currentOrThrow

        Column {
            TopAppBar(title = {
                Text(
                    text = "Список ТС",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    textAlign = TextAlign.Center
                )
            })

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = state.listState,
                reverseLayout = false,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(state.cars, key = { it.id }) { item ->
                    CarItem(
                        modifier = Modifier.fillMaxWidth().animateItem(),
                        car = item,
                        isSelected = item.id == state.selected?.id,
                        delete = { onAction(CarsListScreen.Action.Delete(item.id)) },
                        onClick = {
                            onAction(CarsListScreen.Action.Select(item.id, navigator))
                        },
                    )
                }
            }
        }

        var dialogVisible by remember { mutableStateOf(false) }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 24.dp, end = 24.dp),
            onClick = { dialogVisible = true }
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
        }

        AddCarDialog(
            onDismiss = { dialogVisible = false },
            onConfirm = {
                onAction(CarsListScreen.Action.Add(it))
                dialogVisible = false
            },
            visible = dialogVisible
        )


    }
}

private val border = BorderStroke(1.dp, Color.LightGray)


 @Composable
 private fun CarItem(
     modifier: Modifier,
     car: Car,
     isSelected: Boolean,
     delete: () -> Unit,
     onClick: () -> Unit
 ) {
     val shape = RoundedCornerShape(16.dp)

     RevealSwipe(
         modifier = modifier,
         backgroundStartActionLabel = "",
         backgroundEndActionLabel = "",
         shape = shape,
         backgroundCardEndColor = Color.DarkGray,
         backgroundCardStartColor = Color.DarkGray,
         card = { shape, content ->
             Column(Modifier.clip(shape)) {
                 content()
             }
         },
         hiddenContentStart = {
             Box(
                 modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                 contentAlignment = Alignment.CenterStart
             ) {
                 Icon(
                     modifier = Modifier
                         .padding(end = 24.dp)
                         .size(48.dp)
                         .clip(CircleShape)
                         .clickable(onClick = delete),
                     imageVector = Icons.Filled.Delete,
                     contentDescription = null,
                     tint = MaterialTheme.colorScheme.error
                 )
             }
         },
         hiddenContentEnd = {
             Box(
                 modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                 contentAlignment = Alignment.CenterEnd
             ) {
                 Icon(
                     modifier = Modifier
                         .padding(start = 24.dp)
                         .size(48.dp)
                         .clip(CircleShape)
                         .clickable(onClick = delete),
                     imageVector = Icons.Filled.Delete,
                     contentDescription = null,
                     tint = MaterialTheme.colorScheme.error
                 )
             }
         }
     ) {
         OutlinedCard(
             modifier = Modifier
                 .fillMaxWidth()
                 .selectable(
                     selected = isSelected,
                     interactionSource = remember { MutableInteractionSource() },
                     indication = remember { ripple() },
                     enabled = true,
                     onClick = onClick
                 ),
             border = border,
             shape = shape,
             elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
             colors = CardDefaults.cardColors(
                 containerColor = MaterialTheme.colorScheme.surfaceVariant,
             )
         ) {
             Column(
                 verticalArrangement = Arrangement.spacedBy(4.dp),
                 modifier = Modifier.padding(8.dp)
             ) {
                 Text(text = car.model)
                 Text(text = car.brand)
                 Text(text = car.year.toString())
             }
         }
     }
}


