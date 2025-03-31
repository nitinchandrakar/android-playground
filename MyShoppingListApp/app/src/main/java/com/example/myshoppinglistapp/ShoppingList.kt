package com.example.myshoppinglistapp

import android.Manifest
import android.content.Context
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController

data class ShoppingItem(val id: Int,
                        var name: String,
                        var quantity: Int,
                        var isEditing: Boolean = false,
                        var address: String = ""
    )

@Composable
fun ShoppingListApp(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    navController: NavController,
    context: Context,
    address: String
){
    var sItems by remember{ mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuality by remember { mutableStateOf("") }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            // Handle permission result
                permissions ->
            if(permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
                && permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true){
                // Permission granted, update the location

                locationUtils.requestLocationUpdates(viewModel = viewModel)
            }else{
                // Permission denied, handle accordingly
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as ComponentActivity,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context as ComponentActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )

                if(!rationaleRequired){
                    // Explain why the app needs the permission
                    Toast.makeText(
                        context,
                        "Location permission is required to display location",
                        Toast.LENGTH_LONG
                    ).show()
                } else{
                    // Permission denied, handle accordingly
                    Toast.makeText(
                        context,
                        "Location permission is required to display location, " +
                                "Please enable from settings",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ){
        Spacer(modifier = Modifier.height(50.dp)) // Adds 16dp of vertical space
        Button(
            onClick = {
                showDialog = true
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {

            Text(text = "Add Item")
        }
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            items(sItems) {
                    item: ShoppingItem ->
                        if (item.isEditing){
                            ShoppingItemEditor(item = item,onEditComplete = {
                                editedName, editedQuantity ->
                                sItems = sItems.map {
                                    it.copy(isEditing = false)
                                }

                                val editedItem = sItems.find { it.id == item.id }
                                editedItem?.let {
                                    it.name = editedName
                                    it.quantity = editedQuantity
                                    it.address = address
                                }
                            })
                        }else{
                            ShoppingItem(item = item,
                                onEdit = {
                                    // finding which item we are editing and changing the isEditing flag to true
                                    sItems = sItems.map {
                                        it.copy(isEditing = it.id == item.id)
                                    }
                                },
                                onDelete = {
                                    sItems = sItems - item
                                })
                        }
            }
        }
    }

    if(showDialog){
        AlertDialog(onDismissRequest = {showDialog=false},
            confirmButton = {
                Row(modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.End){
                    Button(onClick = {
                        if (itemName.isNotBlank()){
                            val newItem = ShoppingItem(
                                id = sItems.size + 1,
                                name = itemName,
                                quantity = itemQuality.toInt(),
                                address = address
                            )

                            sItems = sItems + newItem
                            showDialog = false
                            itemName = ""
                            itemQuality = ""
                        }
                    }) {
                        Text(text = "Add")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {showDialog =false}
                    ) {
                        Text(text = "Cancel")
                    }
                }
            },
            title = { Text("Add shopping item") },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        singleLine = true,
                        label = { Text("Item name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = itemQuality,
                        onValueChange = { itemQuality = it },
                        singleLine = true,
                        label = { Text("Item quantity") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    Button(
                        onClick = {
                            if(locationUtils.hasLocationPermission(context)){
                                locationUtils.requestLocationUpdates(viewModel)
                                navController.navigate("locationscreen"){
                                    this.launchSingleTop
                                }
                            }else{
                                requestPermissionLauncher.launch(arrayOf(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ))
                            }
                        }
                    ){
                        Text(text = "Address")
                    }
                }
            }
        )
    }
}

@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditComplete: (String, Int) -> Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }

    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Column {
            BasicTextField(
                value = editedName,
                onValueChange = { editedName = it },
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )
            BasicTextField(
                value = editedQuantity,
                onValueChange = { editedQuantity = it },
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )
        }

        Button(onClick = {
            isEditing = false
            onEditComplete(editedName, editedQuantity.toIntOrNull()?:1)
        }) {
            Text(text = "Save")
        }
    }
}

@Composable
fun ShoppingItem(item: ShoppingItem,
                 onEdit: (ShoppingItem) -> Unit,
                 onDelete: (ShoppingItem) -> Unit
){
    Row (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0XFF018788)),
                shape = RoundedCornerShape(20)
            ),
            horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column(
            modifier = Modifier.weight(1f).padding(8.dp)
        ){
            Row {
                Text(text = item.name, modifier = Modifier.padding(8.dp))
                Text(text = "Qt: ${item.quantity}", modifier = Modifier.padding(8.dp))
            }

            Row (modifier = Modifier.fillMaxWidth()) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                Text(text = item.address)
            }
        }

        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qt: ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row (
            modifier = Modifier.padding(8.dp)
        ){
            IconButton(
                onClick = { onEdit(item) }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }

            IconButton(onClick = { onDelete(item) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }
        }

    }


}