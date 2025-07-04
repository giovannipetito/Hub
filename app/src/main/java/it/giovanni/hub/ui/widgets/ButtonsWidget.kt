package it.giovanni.hub.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.Switch
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.ToggleableStateKey
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import it.giovanni.hub.MainActivity
import it.giovanni.hub.R

class ButtonsWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = ButtonsWidget
}

object ButtonsWidget: GlanceAppWidget() {

    val counter = intPreferencesKey("counter")

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            ButtonsWidgetContent()
        }
    }
}

private val CheckboxKey = booleanPreferencesKey("checkbox")
private val SwitchKey = booleanPreferencesKey("switch")
private val SelectedKey = ActionParameters.Key<String>("key")

@SuppressLint("RestrictedApi")
@Composable
fun ButtonsWidgetContent() {
    val counter = currentState(key = ButtonsWidget.counter) ?: 0
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(12.dp)
            // .appWidgetBackground()
            // .background(GlanceTheme.colors.background)
            .background(Color.Black.copy(alpha = 0.7f)),
        verticalAlignment = Alignment.Vertical.Top,
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        Text(
            text = LocalContext.current.getString(R.string.widget_buttons_title),
            modifier = GlanceModifier.fillMaxWidth(),
            style = TextStyle(
                color = GlanceTheme.colors.primary,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            ),
        )

        Spacer(modifier = GlanceModifier.height(height = 12.dp))

        LazyColumn(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = counter.toString(),
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        color = ColorProvider(Color.White),
                        fontSize = 14.sp
                    )
                )
            }
            item {
                Spacer(modifier = GlanceModifier.height(height = 12.dp))
            }
            item {
                Button(
                    modifier = GlanceModifier.fillMaxWidth(),
                    text = "Increment",
                    onClick = actionRunCallback(CounterActionCallback::class.java)
                )
            }
            item {
                Spacer(modifier = GlanceModifier.height(height = 12.dp))
            }
            item {
                Button(
                    text = "Open Hub",
                    modifier = GlanceModifier.fillMaxWidth(),
                    onClick = actionStartActivity<MainActivity>()
                )
            }
            item {
                Spacer(modifier = GlanceModifier.height(height = 12.dp))
            }
            item {
                CheckBox(
                    text = "Checkbox",
                    checked = currentState(key = CheckboxKey) == true,
                    onCheckedChange = actionRunCallback<CompoundButtonAction>(
                        actionParametersOf(SelectedKey to CheckboxKey.name)
                    )
                )
            }
            item {
                Spacer(modifier = GlanceModifier.height(height = 12.dp))
            }
            item {
                Switch(
                    text = "Switch ",
                    checked = currentState(key = SwitchKey) == true,
                    onCheckedChange = actionRunCallback<CompoundButtonAction>(
                        actionParametersOf(SelectedKey to SwitchKey.name)
                    )
                )
            }
        }
    }
}

class CounterActionCallback: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { preferences ->
            val currentCounter = preferences[ButtonsWidget.counter]
            if (currentCounter != null) {
                preferences[ButtonsWidget.counter] = currentCounter + 1
            } else {
                preferences[ButtonsWidget.counter] = 1
            }
        }
        ButtonsWidget.update(context, glanceId)
    }
}

class CompoundButtonAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val toggle: Boolean = parameters[ToggleableStateKey] == true
        updateAppWidgetState(context, glanceId) { preferences ->
            val key: Preferences.Key<Boolean> = booleanPreferencesKey(parameters[SelectedKey] ?: return@updateAppWidgetState)
            preferences[key] = toggle
        }
        ButtonsWidget.update(context, glanceId)
    }
}