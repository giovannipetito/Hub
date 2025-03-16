package it.giovanni.hub.navigation.util.entries

import androidx.annotation.DrawableRes
import it.giovanni.hub.R
import it.giovanni.hub.utils.Constants

sealed class WizardEntries(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    data object First : WizardEntries(
        image = R.drawable.first_logo,
        title = "Home",
        description = Constants.LOREM_IPSUM_SHORT_TEXT
    )

    data object Second : WizardEntries(
        image = R.drawable.second_logo,
        title = "Profile",
        description = Constants.LOREM_IPSUM_SHORT_TEXT
    )

    data object Third : WizardEntries(
        image = R.drawable.third_logo,
        title = "Settings",
        description = Constants.LOREM_IPSUM_SHORT_TEXT
    )
}