package my.android.newsapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
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
    val categories =
        listOf("General", "Entertainment", "Business", "Health", "Technology", "Sports", "Science")
    val countries = listOf("US", "UK", "Canada", "Australia")

    Row {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Top Headlines",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(24.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TabCategorySelector(categories = categories,
                    selectedCategory = categories.indexOf(currentCategory),
                    onCategorySelected = { index ->
                        currentCategory = categories[index]
                        viewModel.updateCategory(categories[index])
                    },
                    onCategoryFocus = { focusIndex ->
                        currentCategory = categories[focusIndex]
                    })
            }
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