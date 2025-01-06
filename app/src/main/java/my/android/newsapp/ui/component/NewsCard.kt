package my.android.newsapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.MaterialTheme
import coil3.compose.AsyncImage
import my.android.newsapp.data.model.Article

@Composable
fun NewsCard(
    article: Article,
    modifier: Modifier = Modifier,
    onFocusChange: (Boolean) -> Unit,
) {
    Card(
        onClick = {
        },
        modifier = modifier
            .width(124.dp)
            .aspectRatio(.66f)
            .padding(12.dp)
            .onFocusChanged { state ->
                onFocusChange(state.isFocused)
            },
        shape = CardDefaults.shape(shape = RoundedCornerShape(8.dp)),
        border = CardDefaults.border(
            border = Border(
                border = BorderStroke(
                    width = 2.dp,
                    color = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp)
            ),
            focusedBorder = Border(
                border = BorderStroke(
                    width = 2.dp,
                    color = Color(0xAF000000)
                ),
                shape = RoundedCornerShape(10.dp)
            )
        ),
        colors = CardDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        ),
        scale = CardDefaults.scale(
            focusedScale = 1.1f
        ),
    ) {
        AsyncImage(
            model = article.urlToImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}