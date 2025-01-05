package my.android.newsapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import my.android.newsapp.R
import my.android.newsapp.data.model.Article
import my.android.newsapp.ui.component.ErrorScreen
import my.android.newsapp.ui.component.HeadlineCard
import my.android.newsapp.ui.component.LoadingScreen
import my.android.newsapp.ui.theme.NewsAppTheme
import my.android.newsapp.ui.viewmodel.HomeViewmodel
import my.android.newsapp.ui.viewmodel.NewsUiState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewModel: HomeViewmodel) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedCountry by viewModel.selectedCountry.collectAsState()

    NewsAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (val state = uiState) {
                is NewsUiState.Loading -> LoadingScreen()
                is NewsUiState.Success -> NewsScreen(
                    articles = state.articles,
                    viewModel = viewModel,
                    selectedCategory = selectedCategory,
                    selectedCountry = selectedCountry
                )

                is NewsUiState.Error -> ErrorScreen(
                    message = state.message,
                    onRetry = viewModel::loadHeadlines
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsScreen(
    articles: List<Article>,
    viewModel: HomeViewmodel,
    selectedCategory: String,
    selectedCountry: String
) {
    var currentCategory by remember { mutableStateOf(selectedCategory) }
    var currentCountry by remember { mutableStateOf(selectedCountry) }

    val categories =
        listOf("General", "Entertainment", "Business", "Health", "Technology", "Sports", "Science")

    val countryWithCodes = mapOf(
        "US" to "us",
        "UK" to "gb",
        "India" to "in",
        "Canada" to "ca",
        "Australia" to "au"
    )

    var showDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Image(
                painter = painterResource(R.drawable.ic_tv),
                contentDescription = "App Icon"
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = "NewsTV",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TabCategorySelector(
                categories = categories,
                selectedCategory = categories.indexOf(currentCategory),
                onCategorySelected = { index ->
                    currentCategory = categories[index]
                    viewModel.updateCategory(categories[index])
                },
                onCategoryFocus = { focusIndex ->
                    currentCategory = categories[focusIndex]
                }
            )

            Box {
                IconButton(
                    onClick = { showDropdown = !showDropdown },
                    modifier = Modifier
                        .size(28.dp)
                        .focusable()
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(24.dp)
                    )
                }

                DropdownMenu(
                    expanded = showDropdown,
                    onDismissRequest = { showDropdown = false },
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Change Country",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_public),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        onClick = { },
                    )
                    HorizontalDivider()
                    countryWithCodes.forEach { (country, code) ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    country,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            onClick = {
                                currentCountry = country
                                viewModel.updateCountry(code)
                                showDropdown = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        if (articles.isEmpty()) {
            EmptyResult()
        } else {
            HeadlinesList(articles)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HeadlinesList(articles: List<Article>) {
    LazyColumn {
        items(articles) { article ->
            HeadlineCard(article)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TabCategorySelector(
    categories: List<String>,
    selectedCategory: Int,
    onCategorySelected: (Int) -> Unit,
    onCategoryFocus: (Int) -> Unit
) {
    TabRow(selectedTabIndex = selectedCategory, modifier = Modifier.focusRestorer()) {
        categories.forEachIndexed { index, tab ->
            key(index) {
                Tab(
                    selected = index == selectedCategory,
                    onFocus = { onCategoryFocus(index) },
                    onClick = { onCategorySelected(index) }
                ) {
                    Text(
                        text = tab,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyResult() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No news headlines found for this combination of news category and country " +
                        "please try some other combination...",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}