package it.giovanni.hub.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import it.giovanni.hub.navigation.routes.BottomBarRoutes
import it.giovanni.hub.ui.items.ShimmerItem
import it.giovanni.hub.ui.theme.LocalHubColors
import it.giovanni.hub.ui.theme.md_theme_dark_primary
import it.giovanni.hub.ui.theme.md_theme_light_primary
import it.giovanni.hub.utils.Constants.EMAIL_REGEX
import it.giovanni.hub.utils.Constants.PASSWORD_REGEX
import java.io.File
import java.io.FileOutputStream
import java.util.regex.Pattern
import androidx.core.net.toUri
import androidx.core.graphics.toColorInt
import it.giovanni.hub.presentation.model.ColorItem
import it.giovanni.hub.presentation.model.StyleItem

object Globals {

    @Composable
    fun getBrushLoginColors(): List<Color> {
        return listOf(Color.Magenta, Color.Cyan)
    }

    @Composable
    fun getMainBackgroundColors(): Brush {
        val mainBackgroundColors = listOf(
            // MaterialTheme.colorScheme.surface,
            // MaterialTheme.colorScheme.surfaceVariant,
            LocalHubColors.current.backgroundStartColor,
            LocalHubColors.current.backgroundEndColor
        )
        return Brush.verticalGradient(colors = mainBackgroundColors)
    }

    /**
     * Extension property color of MaterialTheme.
     */
    val hexColor: Color
        @Composable
        get() = if (isSystemInDarkTheme()) md_theme_dark_primary else md_theme_light_primary

    /**
     * We can use this extension property color when for example we try to fetch
     * some Hex color code from an API and we want to parse it directly to my UI.
     */
    val String.hexColor
        get() = Color(this.toColorInt())

    val LazyListState.isScrolled: Boolean
        get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0

    fun formatTime(seconds: String, minutes: String, hours: String): String {
        return "$hours:$minutes:$seconds"
    }

    fun Int.pad(): String {
        return this.toString().padStart(2, '0')
    }

    @Composable
    fun getCurrentRoute(navController: NavHostController): String? {
        val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
        return navBackStackEntry?.destination?.route
    }

    // List of main routes where the drawer and the FAB should be visible.
    val mainRoutes = listOf(
        BottomBarRoutes.Home.route,
        BottomBarRoutes.Profile.route,
        BottomBarRoutes.Settings.route
    )

    fun checkEmail(email: String): Boolean {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches()
    }

    fun checkPassword(password: String): Boolean {
        return Pattern.compile(PASSWORD_REGEX).matcher(password).matches()
    }

    fun parseUriString(uriString: String): Uri {
        return uriString.toUri()
    }

    fun decodeUriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getUriFromBitmap(context: Context, bitmap: Bitmap, fileName: String): Uri? {
        val file = File(context.filesDir, fileName)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return Uri.fromFile(file)
    }

    @Composable
    fun getTransitionColor(durationMillis: Int = 5000): Color {
        val transition = rememberInfiniteTransition(label = "transition")
        val transitionColor: Color by transition.animateColor(
            initialValue = Color.Magenta,
            targetValue = Color.Cyan,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = durationMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "login button color"
        )
        return transitionColor
    }

    @Composable
    fun getContentPadding(paddingValues: PaddingValues): PaddingValues {
        return PaddingValues(
            start = paddingValues.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
            end = paddingValues.calculateEndPadding(layoutDirection = LayoutDirection.Ltr),
            bottom = paddingValues.calculateBottomPadding()
        )
    }

    @Composable
    fun getExtraContentPadding(paddingValues: PaddingValues, extraPadding: Dp): PaddingValues {
        return PaddingValues(
            start = paddingValues.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
            end = paddingValues.calculateEndPadding(layoutDirection = LayoutDirection.Ltr),
            bottom = paddingValues.calculateBottomPadding() + extraPadding
        )
    }

    @Composable
    fun getFloatingActionButtonPadding(paddingValues: PaddingValues): PaddingValues {
        return PaddingValues(
            start = paddingValues.calculateStartPadding(layoutDirection = LayoutDirection.Ltr) + 16.dp,
            end = paddingValues.calculateEndPadding(layoutDirection = LayoutDirection.Ltr) + 16.dp,
            bottom = paddingValues.calculateBottomPadding() + 12.dp
        )
    }

    @Composable
    fun ShimmerItems() {
        repeat(6) {
            ShimmerItem()
        }
    }

    @Composable
    fun getTextFieldColors(): TextFieldColors {
        return TextFieldColors(
            focusedTextColor = MaterialTheme.colorScheme.primary,
            unfocusedTextColor = MaterialTheme.colorScheme.secondary,
            disabledTextColor = MaterialTheme.colorScheme.tertiary,
            errorTextColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            errorContainerColor = MaterialTheme.colorScheme.errorContainer,
            cursorColor = Color.White,
            errorCursorColor = Color.Red,
            textSelectionColors = TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.onPrimaryContainer,
                backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            focusedIndicatorColor = Color.Transparent, // MaterialTheme.colorScheme.primary
            unfocusedIndicatorColor = Color.Transparent, // MaterialTheme.colorScheme.secondary
            disabledIndicatorColor = Color.Transparent, // MaterialTheme.colorScheme.tertiary
            errorIndicatorColor = Color.Transparent, // MaterialTheme.colorScheme.error
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
            disabledLeadingIconColor = MaterialTheme.colorScheme.tertiary,
            errorLeadingIconColor = MaterialTheme.colorScheme.error,
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
            disabledTrailingIconColor = MaterialTheme.colorScheme.tertiary,
            errorTrailingIconColor = MaterialTheme.colorScheme.error,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
            disabledLabelColor = MaterialTheme.colorScheme.tertiary,
            errorLabelColor = MaterialTheme.colorScheme.error,
            focusedPlaceholderColor = MaterialTheme.colorScheme.primary,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
            disabledPlaceholderColor = MaterialTheme.colorScheme.tertiary,
            errorPlaceholderColor = MaterialTheme.colorScheme.error,
            focusedSupportingTextColor = MaterialTheme.colorScheme.primary,
            unfocusedSupportingTextColor = MaterialTheme.colorScheme.secondary,
            disabledSupportingTextColor = MaterialTheme.colorScheme.tertiary,
            errorSupportingTextColor = MaterialTheme.colorScheme.error,
            focusedPrefixColor = MaterialTheme.colorScheme.primary,
            unfocusedPrefixColor = MaterialTheme.colorScheme.secondary,
            disabledPrefixColor = MaterialTheme.colorScheme.tertiary,
            errorPrefixColor = MaterialTheme.colorScheme.error,
            focusedSuffixColor = MaterialTheme.colorScheme.primary,
            unfocusedSuffixColor = MaterialTheme.colorScheme.secondary,
            disabledSuffixColor = MaterialTheme.colorScheme.secondary,
            errorSuffixColor = MaterialTheme.colorScheme.error
        )
    }

    val colorList: List<Color> = listOf(
        Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan, Color.Magenta
    )

    @Composable
    fun getColorItems(): List<ColorItem> {
        val colorItems = remember {
            listOf(
                // Blacks
                ColorItem("Jet Black", Color(0xFF0B0B0D)),
                ColorItem("Blue-Black", Color(0xFF0A0F1A)),
                ColorItem("Soft Black", Color(0xFF1A1614)),
                ColorItem("Natural Black", Color(0xFF141112)),
                ColorItem("Espresso Black", Color(0xFF1B1412)),
                ColorItem("Black Cherry", Color(0xFF2A0F18)),
                ColorItem("Licorice", Color(0xFF0F0E10)),
                ColorItem("Raven", Color(0xFF101015)),

                // Browns (cool/neutral/warm)
                ColorItem("Darkest Brown", Color(0xFF2A1B17)),
                ColorItem("Dark Chocolate", Color(0xFF2F1C16)),
                ColorItem("Espresso Brown", Color(0xFF2B1A14)),
                ColorItem("Mocha Brown", Color(0xFF3A261F)),
                ColorItem("Cocoa Brown", Color(0xFF3B271E)),
                ColorItem("Chestnut Brown", Color(0xFF4A2D22)),
                ColorItem("Mahogany Brown", Color(0xFF4B2226)),
                ColorItem("Walnut Brown", Color(0xFF4A352C)),
                ColorItem("Dark Ash Brown", Color(0xFF3A332F)),
                ColorItem("Cool Brown", Color(0xFF3E322B)),
                ColorItem("Sable Brown", Color(0xFF3A241C)),
                ColorItem("Truffle Brown", Color(0xFF3D2A24)),
                ColorItem("Deep Brunette", Color(0xFF3B2620)),
                ColorItem("Dark Caramel Brown", Color(0xFF4C3326)),
                ColorItem("Rich Brown", Color(0xFF4D342B)),
                ColorItem("Medium Brown", Color(0xFF5A3A2E)),
                ColorItem("Neutral Brown", Color(0xFF583B31)),
                ColorItem("Medium Ash Brown", Color(0xFF524842)),
                ColorItem("Smoky Brown", Color(0xFF4F413A)),
                ColorItem("Mushroom Brown", Color(0xFF4D4540)),
                ColorItem("Taupe Brown", Color(0xFF5A514A)),
                ColorItem("Light Brown", Color(0xFF6A4B3A)),
                ColorItem("Golden Brown", Color(0xFF6E4B2F)),
                ColorItem("Honey Brown", Color(0xFF6B4A2F)),
                ColorItem("Toffee Brown", Color(0xFF7A5433)),
                ColorItem("Hazelnut Brown", Color(0xFF7A593F)),

                // Blondes / Bronde
                ColorItem("Dark Blonde", Color(0xFF7A5B3B)),
                ColorItem("Dark Ash Blonde", Color(0xFF6F6256)),
                ColorItem("Natural Blonde", Color(0xFFD2B889)),
                ColorItem("Golden Blonde", Color(0xFFE0C06E)),
                ColorItem("Honey Blonde", Color(0xFFD7A85A)),
                ColorItem("Warm Blonde", Color(0xFFE3C37C)),
                ColorItem("Butter Blonde", Color(0xFFF2D179)),
                ColorItem("Caramel Blonde", Color(0xFFD3A066)),
                ColorItem("Bronde", Color(0xFFC7A06A)),
                ColorItem("Champagne Blonde", Color(0xFFE9D9B1)),
                ColorItem("Vanilla Blonde", Color(0xFFF1E0B8)),
                ColorItem("Platinum Blonde", Color(0xFFF6F0E2)),
                ColorItem("Icy Blonde", Color(0xFFEDEFF2)),
                ColorItem("Pearl Blonde", Color(0xFFF3EEE4)),
                ColorItem("Ash Blonde", Color(0xFFD0C7B7)),
                ColorItem("Silver Blonde", Color(0xFFD8DCE2)),
                ColorItem("Strawberry Blonde", Color(0xFFD8A07A)),
                ColorItem("Rose Gold Blonde", Color(0xFFE3B2A5)),
                ColorItem("Apricot Blonde", Color(0xFFE2B07F)),
                ColorItem("Cream Blonde", Color(0xFFF3E6CC)),
                ColorItem("Light Blonde", Color(0xFFEAD2A5)),

                // Coppers / Auburns
                ColorItem("Copper", Color(0xFFC4682D)),
                ColorItem("Light Copper", Color(0xFFD07A3D)),
                ColorItem("Soft Copper", Color(0xFFC97A4A)),
                ColorItem("Ginger", Color(0xFFB85B2A)),
                ColorItem("Cinnamon", Color(0xFF9B4A2A)),
                ColorItem("Burnt Copper", Color(0xFF9C4C24)),
                ColorItem("Auburn", Color(0xFF7A2E1B)),
                ColorItem("Light Auburn", Color(0xFF8A3A24)),
                ColorItem("Dark Auburn", Color(0xFF5E2317)),
                ColorItem("Copper Brown", Color(0xFF6A3A25)),

                // Reds (natural + bold)
                ColorItem("True Red", Color(0xFFB2181C)),
                ColorItem("Cherry Red", Color(0xFFA3151B)),
                ColorItem("Ruby Red", Color(0xFF8D1118)),
                ColorItem("Crimson", Color(0xFF7E0F18)),
                ColorItem("Scarlet", Color(0xFFBF1E2E)),
                ColorItem("Burgundy", Color(0xFF4A0F23)),
                ColorItem("Wine", Color(0xFF3E0E1D)),
                ColorItem("Merlot", Color(0xFF3A0C18)),
                ColorItem("Blackberry", Color(0xFF2E0A1E)),
                ColorItem("Plum Red", Color(0xFF4B1430)),
                ColorItem("Mahogany Red", Color(0xFF5A1E22)),
                ColorItem("Garnet", Color(0xFF5A1020)),

                // Grays / Silvers
                ColorItem("Silver", Color(0xFFD6D9DD)),
                ColorItem("Steel Gray", Color(0xFF8A9199)),
                ColorItem("Graphite", Color(0xFF4A4F55)),
                ColorItem("Smoke Gray", Color(0xFF9AA0A6)),
                ColorItem("Pearl Gray", Color(0xFFE2E4E8)),
                ColorItem("Ash Gray", Color(0xFFB8B1A8)),
                ColorItem("Salt and Pepper", Color(0xFF7C7772)),
                ColorItem("Charcoal Gray", Color(0xFF2F3338)),

                // Purples
                ColorItem("Violet", Color(0xFF5A2A7A)),
                ColorItem("Deep Purple", Color(0xFF3B1B5A)),
                ColorItem("Plum", Color(0xFF4E1F4E)),
                ColorItem("Eggplant", Color(0xFF2B1230)),
                ColorItem("Lavender", Color(0xFFB08ACB)),

                // Pinks
                ColorItem("Hot Pink", Color(0xFFE91E63)),
                ColorItem("Rose Pink", Color(0xFFD85B7D)),
                ColorItem("Dusty Rose", Color(0xFFB66A73)),
                ColorItem("Pastel Pink", Color(0xFFF2B6C8)),
                ColorItem("Magenta", Color(0xFFB0006D)),

                // Fashion blues/greens (common dye shades)
                ColorItem("Teal Blue", Color(0xFF008B8B)),
                ColorItem("Emerald Green", Color(0xFF007A3D)),
                ColorItem("Forest Green", Color(0xFF0B4F2E)),
                ColorItem("Ocean Blue", Color(0xFF0D5EA6)),
                ColorItem("Navy Blue", Color(0xFF0B2A4A)),
            )
        }
        return colorItems
    }

    @Composable
    fun getColorItems2(): List<ColorItem> {
        val colorItems = remember {
            listOf(
                ColorItem("Red", Color(0xFFE57373)),
                ColorItem("Orange", Color(0xFFFFB74D)),
                ColorItem("Yellow", Color(0xFFFFF176)),
                ColorItem("Green", Color(0xFF81C784)),
                ColorItem("Teal", Color(0xFF4DB6AC)),
                ColorItem("Blue", Color(0xFF64B5F6)),
                ColorItem("Indigo", Color(0xFF7986CB)),
                ColorItem("Purple", Color(0xFFBA68C8)),
                ColorItem("Pink", Color(0xFFF06292)),
                ColorItem("Brown", Color(0xFFA1887F))
            )
        }
        return colorItems
    }

    @Composable
    fun getStyleItems(): List<StyleItem> {
        val gridItems: List<StyleItem> = remember {
            mutableStateListOf(
                StyleItem(styleName = "Sharp", stylePrompt = "cinematic still, {prompt}, emotional, harmonious, vignette, 4k epic detailed, shot on kodak, 35mm photo, sharp focus, high budget, cinemascope, moody, epic, gorgeous, film grain, grainy", "https://picsum.photos/id/1/300"),
                StyleItem("Style 2", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/2/300"),
                StyleItem("Style 3", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/3/300"),
                StyleItem("Style 4", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/4/300"),
                StyleItem("Style 5", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/5/300"),
                StyleItem("Style 6", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/6/300"),
                StyleItem("Style 7", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/7/300"),
                StyleItem("Style 8", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/8/300"),
                StyleItem("Style 9", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/9/300"),
                StyleItem("Style 10", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/10/300"),
                StyleItem("Style 11", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/11/300"),
                StyleItem("Style 12", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/12/300"),
                StyleItem("Style 13", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/13/300"),
                StyleItem("Style 14", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/14/300"),
                StyleItem("Style 15", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/15/300"),
                StyleItem("Style 16", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/16/300"),
                StyleItem("Style 17", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/17/300"),
                StyleItem("Style 18", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/18/300"),
                StyleItem("Style 19", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/19/300"),
                StyleItem("Style 20", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/20/300"),
                StyleItem("Style 21", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/21/300"),
                StyleItem("Style 22", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/22/300"),
                StyleItem("Style 23", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/23/300"),
                StyleItem("Style 24", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/24/300"),
                StyleItem("Style 25", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/25/300"),
                StyleItem("Style 26", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/26/300"),
                StyleItem("Style 27", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/27/300"),
                StyleItem("Style 28", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/28/300"),
                StyleItem("Style 29", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/29/300"),
                StyleItem("Style 30", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/30/300"),
                StyleItem("Style 31", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/31/300"),
                StyleItem("Style 32", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/32/300"),
                StyleItem("Style 33", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/33/300"),
                StyleItem("Style 34", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/34/300"),
                StyleItem("Style 35", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/35/300"),
                StyleItem("Style 36", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/36/300"),
                StyleItem("Style 37", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/37/300"),
                StyleItem("Style 38", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/38/300"),
                StyleItem("Style 39", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/39/300"),
                StyleItem("Style 40", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/40/300"),
                StyleItem("Style 41", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/41/300"),
                StyleItem("Style 42", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/42/300"),
                StyleItem("Style 43", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/43/300"),
                StyleItem("Style 44", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/44/300"),
                StyleItem("Style 45", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/45/300"),
                StyleItem("Style 46", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/46/300"),
                StyleItem("Style 47", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/47/300"),
                StyleItem("Style 48", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/48/300"),
                StyleItem("Style 49", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/49/300"),
                StyleItem("Style 50", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/50/300"),
                StyleItem("Style 51", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/51/300"),
                StyleItem("Style 52", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/52/300"),
                StyleItem("Style 53", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/53/300"),
                StyleItem("Style 54", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/54/300"),
                StyleItem("Style 55", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/55/300"),
                StyleItem("Style 56", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/56/300"),
                StyleItem("Style 57", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/57/300"),
                StyleItem("Style 58", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/58/300"),
                StyleItem("Style 59", "anime artwork {prompt}, highly detailed", "https://picsum.photos/id/59/300"),
                StyleItem(
                    "Style 60",
                    "anime artwork {prompt}, highly detailed",
                    "https://picsum.photos/id/60/300"
                )
            )
        }
        return gridItems
    }
}