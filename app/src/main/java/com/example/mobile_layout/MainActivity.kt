package com.example.mobile_layout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.*
import com.example.mobile_layout.ui.theme.MOBILELayoutTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MOBILELayoutTheme {

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()

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
                            DrawerItem("Início") {
                                scope.launch { drawerState.close() }
                                navController.navigate("home")
                            }
                            DrawerItem("Perfil") {
                                scope.launch { drawerState.close() }
                                navController.navigate("perfil")
                            }
                            DrawerItem("Configurações") {
                                scope.launch { drawerState.close() }
                                navController.navigate("config")
                            }
                            DrawerItem("Sair") {
                                // ação de sair
                            }
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
                            BottomAppBarCustom(navController)
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            AppNavigation(navController = navController)
                        }
                    }
                }
            }
        }
    }
}


// ---------- Bottom Bar ----------
@Composable
fun BottomAppBarCustom(navController: NavHostController) {
    BottomAppBar(
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
            IconButton(onClick = { navController.navigate("home") }) {
                Icon(
                    painter = painterResource(id = R.drawable.utensils_solid_full),
                    contentDescription = "Início",
                    tint = Color.White
                )
            }

            DividerVertical()

            IconButton(onClick = { navController.navigate("search") }) {
                Icon(
                    painter = painterResource(id = R.drawable.magnifying_glass_solid_full),
                    contentDescription = "Buscar",
                    tint = Color.White
                )
            }

            DividerVertical()

            IconButton(onClick = { navController.navigate("cart") }) {
                Icon(
                    painter = painterResource(id = R.drawable.cart_shopping_solid_full),
                    contentDescription = "Carrinho",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun DividerVertical() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(24.dp)
            .background(Color.White.copy(alpha = 0.5f))
    )
}


// ---------- Drawer Item ----------
@Composable
fun DrawerItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(Color(0x33485C91))
            .padding(12.dp)
            .clickable { onClick() }
    )
}

// ---------- Navegação ----------
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { GridTest() }
        composable("search") { SearchScreen() }
        composable("cart") { CartScreen() }
        composable("perfil") { PerfilScreen() }
        composable("config") { ConfigScreen() }
        composable("cardapio") { CardapioScreen() }
    }
}

// ---------- Telas ----------

@Composable
fun PerfilScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEBC8))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tela de Perfil", color = Color(0xFF7A4A00), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ConfigScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF5E1))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tela de Configurações", color = Color(0xFF7A4A00), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CardapioScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Tela de Cardápio", color = Color.DarkGray)
    }
}

@Composable
fun SearchScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Tela de Busca", color = Color.DarkGray)
    }
}

@Composable
fun CartScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Tela do Carrinho", color = Color.DarkGray)
    }
}

@Composable
fun AdminLoginScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Tela de Login de Admin", color = Color.DarkGray)
    }
}

@Composable
fun AdminPedidosScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Tela dos Pedidos, Admin Only!", color = Color.DarkGray)
    }
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
fun ArtistCardRow(nome: String, ultimaVezOnline: String, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.checkered),
            contentDescription = "Imagem do artista $nome",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))
        Column {
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