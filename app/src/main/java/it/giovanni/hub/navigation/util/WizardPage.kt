package it.giovanni.hub.navigation.util

import androidx.annotation.DrawableRes
import it.giovanni.hub.R

sealed class WizardPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    data object First : WizardPage(
        image = R.drawable.first_logo,
        title = "Meeting",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    data object Second : WizardPage(
        image = R.drawable.second_logo,
        title = "Coordination",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    data object Third : WizardPage(
        image = R.drawable.third_logo,
        title = "Dialogue",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )
}