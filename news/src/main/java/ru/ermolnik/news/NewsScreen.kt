package ru.ermolnik.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.mts.data.news.repository.model.News

@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val state = viewModel.state.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(false)
    SwipeRefresh(state = swipeRefreshState, onRefresh = { viewModel.refreshNews() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.news_screen_background))
        ) {
            when (state.value) {
                is NewsState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center)
                    )
                }
                is NewsState.Error -> {
                    Error(
                        error = (state.value as NewsState.Error).throwable.toString(),
                        onClick = { viewModel.refreshNews() }
                    )
                }
                is NewsState.Content -> {
                    NewsList(
                        (state.value as NewsState.Content).news
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(onRefreshClick: () -> Unit) {
    TopAppBar {
        IconButton(onClick = { }) {
            Icon(
                Icons.Filled.ArrowBack, contentDescription = "ArrowBack"
            )
        }
        Text("News Mts-Teta", fontSize = 18.sp, color = Color.White)
        Spacer(Modifier.weight(1f, true))
        IconButton(onClick = onRefreshClick) {
            Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
        }
    }
}

@Composable
private fun Error(error: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Очень неприятная ошибка: $error",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 16.dp)
        )
        Button(onClick = onClick) {
            Text(
                text = "Повторить",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun NewsList(news: List<News>) {
    LazyColumn {
        items(news) { news ->
            NewsCard(news)
        }
    }
}

@Composable
private fun NewsCard(news: News) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 0.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            NewsNameText(news)
            NewsDescriptionText(news)
        }
    }
}

@Composable
private fun NewsNameText(news: News) {
    Text(
        text = news.name,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        textAlign = TextAlign.Left,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .padding(vertical = 2.dp)
    )
}

@Composable
private fun NewsDescriptionText(news: News) {
    Text(
        text = news.description,
        fontSize = 20.sp,
        color = Color.Gray,
        textAlign = TextAlign.Left,
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    )
}
