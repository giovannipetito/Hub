package it.giovanni.hub.navigation.util

import androidx.annotation.DrawableRes
import it.giovanni.hub.R
import it.giovanni.hub.utils.Constants

sealed class WizardPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    data object First : WizardPage(
        image = R.drawable.first_logo,
        title = "Home",
        description = Constants.loremIpsumShortText
    )

    data object Second : WizardPage(
        image = R.drawable.second_logo,
        title = "Profile",
        description = Constants.loremIpsumShortText
    )

    data object Third : WizardPage(
        image = R.drawable.third_logo,
        title = "Settings",
        description = Constants.loremIpsumShortText
    )
}