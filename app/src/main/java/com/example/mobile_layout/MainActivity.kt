package com.example.mobile_layout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile_layout.ui.theme.MOBILELayoutTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MOBILELayoutTheme {
                // Estado do Drawer
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                // Conteúdo do Drawer (menu lateral)
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerContainerColor = Color(0xFFD09409)
                        ) {
                            Text(
                                text = "Menu",
                                color = Color.White,
                                modifier = Modifier.padding(16.dp),
                                fontWeight = FontWeight.Bold
                            )
                            DrawerItem("Início")
                            DrawerItem("Perfil")
                            DrawerItem("Configurações")
                            DrawerItem("Sair")
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Layout") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            if (drawerState.isClosed) drawerState.open()
                                            else drawerState.close()
                                        }
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.bars_solid_full),
                                            contentDescription = "Menu",
                                            tint = Color.White
                                        )
                                    }
                                },
                                colors = TopAppBarColors(
                                    containerColor = Color(0xFFD09409),
                                    scrolledContainerColor = Color(0xFFD09409),
                                    navigationIconContentColor = Color.White,
                                    titleContentColor = Color.White,
                                    actionIconContentColor = Color.White
                                )
                            )
                        },
                        bottomBar = {
                            androidx.compose.material3.BottomAppBar(
                                containerColor = Color(0xFFD09409),
                                contentColor = Color.White
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = { /* ação lista */ }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.utensils_solid_full),
                                            contentDescription = "Lista",
                                            tint = Color.White
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .size(height = 24.dp, width = 1.dp)
                                            .padding(horizontal = 4.dp)
                                            .background(Color.White.copy(alpha = 0.5f))
                                    )

                                    IconButton(onClick = { /* ação lupa */ }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.magnifying_glass_solid_full),
                                            contentDescription = "Buscar",
                                            tint = Color.White
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .size(height = 24.dp, width = 1.dp)
                                            .padding(horizontal = 4.dp)
                                            .background(Color.White.copy(alpha = 0.5f))
                                    )

                                    IconButton(onClick = { /* ação carrinho */ }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.cart_shopping_solid_full),
                                            contentDescription = "Carrinho",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            GridTest()
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun DrawerItem(text: String) {
    Text(
        text = text,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(Color(0x33485C91))
            .padding(12.dp)
    )
}


@Composable
fun GridTest(){
//    val lista = listOf("Teste1","Teste2","Teste3")
    val lista = listOf(
        "Item 1" to "Descrição do item 1",
        "Item 2" to "Descrição do item 2",
        "Item 3" to "Descrição do item 3",
        "Item 4" to "Descrição do item 4"
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(100.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ){
        items(lista) { (nome, descricao) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // deixa cada item quadrado
            ) {
                // Fundo com imagem
                Image(
                    painter = painterResource(id = R.drawable.checkered),
                    contentDescription = "Imagem de fundo do $nome",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                // Sobreposição com gradiente escuro para facilitar leitura do texto
                Canvas(
                    modifier = Modifier.matchParentSize()
                ) {
                    drawRect(Color(0x88000000)) // semitransparente preto
                }

                // Conteúdo: ArtistCardRow + textos
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    // Aqui chamamos ArtistCardRow dentro do card
                    ArtistCardRow(
                        nome = nome,
                        ultimaVezOnline = "Online agora",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = nome,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = descricao,
                        color = Color.White.copy(alpha = 0.8f),
                    )
                }
            }
        }
    }
}

@Composable
fun ArtistCardRow(nome: String, ultimaVezOnline: String, modifier: Modifier){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(8.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.checkered),
            contentDescription = "Imagem do artista $nome",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale =  ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))
        Column{
            Text(
                text = nome,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = ultimaVezOnline,
                color = Color.Gray,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GridPreview() {
    MOBILELayoutTheme {
        GridTest()
    }
}