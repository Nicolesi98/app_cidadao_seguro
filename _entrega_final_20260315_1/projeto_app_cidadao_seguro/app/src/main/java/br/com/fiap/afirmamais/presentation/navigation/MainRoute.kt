package br.com.fiap.afirmamais.presentation.navigation

sealed class MainRoute(val route: String) {
    data object Home : MainRoute("home")
    data object About : MainRoute("about")
    data object Opportunities : MainRoute("opportunities")
    data object Profile : MainRoute("profile")
    data object JobDetails : MainRoute("job/{jobId}") {
        fun createRoute(jobId: Int): String = "job/$jobId"
    }
}