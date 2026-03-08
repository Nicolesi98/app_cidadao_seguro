package br.com.fiap.afirmamais.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.fiap.afirmamais.core.util.SimpleViewModelFactory
import br.com.fiap.afirmamais.di.AppContainer
import br.com.fiap.afirmamais.domain.model.AuthUser
import br.com.fiap.afirmamais.presentation.components.AppTopBar
import br.com.fiap.afirmamais.presentation.components.BottomNavigationBar
import br.com.fiap.afirmamais.presentation.screen.about.AboutScreen
import br.com.fiap.afirmamais.presentation.screen.auth.LoginScreen
import br.com.fiap.afirmamais.presentation.screen.auth.LoginViewModel
import br.com.fiap.afirmamais.presentation.screen.home.HomeScreen
import br.com.fiap.afirmamais.presentation.screen.home.HomeViewModel
import br.com.fiap.afirmamais.presentation.screen.job.JobDetailsScreen
import br.com.fiap.afirmamais.presentation.screen.job.JobDetailsViewModel
import br.com.fiap.afirmamais.presentation.screen.opportunities.OpportunitiesScreen
import br.com.fiap.afirmamais.presentation.screen.opportunities.OpportunitiesViewModel
import br.com.fiap.afirmamais.presentation.screen.profile.ProfileScreen
import br.com.fiap.afirmamais.presentation.screen.profile.ProfileViewModel

@Composable
fun AfirmaApp(appContainer: AppContainer) {
    val sessionViewModel: SessionViewModel = viewModel(
        factory = SimpleViewModelFactory {
            SessionViewModel(authRepository = appContainer.authRepository)
        },
    )

    val sessionUiState by sessionViewModel.uiState.collectAsState()

    when {
        !sessionUiState.isReady -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        sessionUiState.user == null -> {
            val loginViewModel: LoginViewModel = viewModel(
                key = "login-screen",
                factory = SimpleViewModelFactory {
                    LoginViewModel(authRepository = appContainer.authRepository)
                },
            )
            LoginScreen(viewModel = loginViewModel)
        }

        else -> {
            MainShell(
                user = sessionUiState.user,
                onLogout = sessionViewModel::logout,
                appContainer = appContainer,
            )
        }
    }
}

@Composable
private fun MainShell(
    user: AuthUser?,
    onLogout: () -> Unit,
    appContainer: AppContainer,
) {
    if (user == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Sessao invalida", color = Color(0xFFEF4444))
        }
        return
    }

    val navController = rememberNavController()

    Scaffold(
        topBar = {
            AppTopBar(
                user = user,
                onLogout = onLogout,
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        containerColor = Color(0xFFF8FAFC),
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainRoute.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = 12.dp),
        ) {
            composable(MainRoute.Home.route) {
                val vm: HomeViewModel = viewModel(
                    key = "home-${user.email}",
                    factory = SimpleViewModelFactory {
                        HomeViewModel(
                            currentUser = user,
                            jobRepository = appContainer.jobRepository,
                            userDataRepository = appContainer.userDataRepository,
                        )
                    },
                )
                HomeScreen(
                    viewModel = vm,
                    onOpenJob = { jobId -> navController.navigate(MainRoute.JobDetails.createRoute(jobId)) },
                )
            }

            composable(MainRoute.About.route) {
                AboutScreen()
            }

            composable(MainRoute.Opportunities.route) {
                val vm: OpportunitiesViewModel = viewModel(
                    key = "opportunities-${user.email}",
                    factory = SimpleViewModelFactory {
                        OpportunitiesViewModel(
                            currentUser = user,
                            jobRepository = appContainer.jobRepository,
                            userDataRepository = appContainer.userDataRepository,
                        )
                    },
                )
                OpportunitiesScreen(
                    email = user.email,
                    viewModel = vm,
                    onOpenJob = { jobId -> navController.navigate(MainRoute.JobDetails.createRoute(jobId)) },
                )
            }

            composable(MainRoute.Profile.route) {
                val vm: ProfileViewModel = viewModel(
                    key = "profile-${user.email}",
                    factory = SimpleViewModelFactory {
                        ProfileViewModel(
                            currentUser = user,
                            userDataRepository = appContainer.userDataRepository,
                        )
                    },
                )
                ProfileScreen(
                    viewModel = vm,
                    onLogout = onLogout,
                )
            }

            composable(
                route = MainRoute.JobDetails.route,
                arguments = listOf(navArgument("jobId") { type = NavType.IntType }),
            ) { backStackEntry ->
                val jobId = backStackEntry.arguments?.getInt("jobId") ?: return@composable
                val vm: JobDetailsViewModel = viewModel(
                    key = "job-$jobId-${user.email}",
                    factory = SimpleViewModelFactory {
                        JobDetailsViewModel(
                            currentUser = user,
                            jobId = jobId,
                            jobRepository = appContainer.jobRepository,
                            userDataRepository = appContainer.userDataRepository,
                        )
                    },
                )
                JobDetailsScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}