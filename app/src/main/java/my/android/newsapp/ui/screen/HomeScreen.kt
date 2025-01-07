package my.android.newsapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.android.newsapp.R
import my.android.newsapp.data.model.Article
import my.android.newsapp.ui.component.ErrorScreen
import my.android.newsapp.ui.component.FeaturedNewsCard
import my.android.newsapp.ui.component.LoadingScreen
import my.android.newsapp.ui.component.NewsCard
import my.android.newsapp.ui.theme.NewsAppTheme
import my.android.newsapp.ui.viewmodel.HomeViewmodel
import my.android.newsapp.ui.viewmodel.NewsUiState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewModel: HomeViewmodel) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedCountry by viewModel.selectedCountry.collectAsState()
    var keyPressJob by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()

    NewsAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.DirectionDown) {
                        when (keyEvent.type) {
                            KeyEventType.KeyDown -> {
                                keyPressJob = scope.launch {
                                    delay(4000)
                                    viewModel.loadHeadlines()
                                }
                                false
                            }
                            KeyEventType.KeyUp -> {
                                keyPressJob?.cancel()
                                keyPressJob = null
                                false
                            }
                            else -> false
                        }
                    } else false
                }
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
    var focusedArticle by remember { mutableStateOf(articles.firstOrNull()) }

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
    var focusedCountry by remember { mutableStateOf<String?>(selectedCountry) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        focusedArticle?.let { article ->
            FeaturedNewsCard(
                modifier = Modifier.fillMaxSize(),
                article = article
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                )

                Box {
                    androidx.tv.material3.Button(onClick = { showDropdown = !showDropdown }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(currentCountry)
                            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                        }
                    }

                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false },
                        containerColor = Color.DarkGray
                    ) {
                        DropdownMenuItem(
                            text = { Text("Change Country") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_public),
                                    contentDescription = null
                                )
                            },
                            onClick = {}
                        )
                        HorizontalDivider()
                        countryWithCodes.forEach { (country, code) ->
                            DropdownMenuItem(
                                text = { Text(country) },
                                modifier = Modifier.background(if (focusedCountry == country) Color.White else Color.Transparent),
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

            if (articles.isEmpty()) {
                EmptyResult()
            } else {
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Text("Top Headlines", style = MaterialTheme.typography.titleMedium)
                    HeadlinesList(articles) { focusedArticle = it }
                }
            }
        }
    }
}

@Composable
fun HeadlinesList(
    articles: List<Article>,
    onArticleFocus: (Article) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
        items(articles) { article ->
            if (article.title != "[Removed]")
                NewsCard(
                    article = article,
                    modifier = Modifier,
                    onFocusChange = { isFocused ->
                        if (isFocused) {
                            onArticleFocus(article)
                        }
                    },
                )
        }
    }
}

@Composable
fun TabCategorySelector(
    categories: List<String>,
    selectedCategory: Int,
    onCategorySelected: (Int) -> Unit,
) {
    TabRow(selectedTabIndex = selectedCategory,
        contentColor = Color.Black ) {
        categories.forEachIndexed { index, tab ->
            key(index) {
                Tab(
                    selected = index == selectedCategory,
                    onClick = { onCategorySelected(index) },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Gray,
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
        Text(
            text = "No news headlines found for this combination of news category and country " +
                    "please try some other combination...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}