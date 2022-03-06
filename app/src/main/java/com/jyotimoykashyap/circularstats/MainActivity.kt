package com.jyotimoykashyap.circularstats

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jyotimoykashyap.circularstats.ui.theme.CircularStatsTheme
import com.jyotimoykashyap.circularstatswidget.AnimateCompletion
import com.jyotimoykashyap.circularstatswidget.CircularStats



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CircularStatsTheme {
                Surface {
                    MainView()
                }
            }
        }
    }
}

@Composable
fun MainView(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var textState by remember{ mutableStateOf(10)}
        CircularStats(
            canvasSize = 200.dp,
            indicatorValue = textState,
            animationType = AnimateCompletion.CHECKMARK
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(30.dp))



        TextField(
            value = textState.toString(),
            onValueChange = {textState =
                if(it.isNotEmpty()) it.toInt() else 0
                            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),

        )
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CircularStatsTheme {
        MainView()
    }
}
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DarkModePreview(){
    CircularStatsTheme {
        MainView()
    }
}