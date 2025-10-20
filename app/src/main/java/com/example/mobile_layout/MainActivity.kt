package com.example.mobile_layout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
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
            val isAdminLogged = rememberSaveable { mutableStateOf(false) }
            val cardapio = remember { mutableStateListOf<ItemCardapio>() }
            val pedidos = remember { mutableStateListOf<Pedido>() }
            val carrinho = remember { mutableStateListOf<ItemCarrinho>() }
            val viewModel = remember { AppViewModel() }
//            AppNavigation(navController, isAdminLogged,cardapio,pedidos,carrinho)

            // Dados iniciais de exemplo
            LaunchedEffect(Unit) {
                if (cardapio.isEmpty()) {
                    cardapio.addAll(
                        listOf(
                            ItemCardapio(1, "Pão Francês", 0.80),
                            ItemCardapio(2, "Café com Leite", 4.50),
                            ItemCardapio(3, "Coxinha", 6.00),
                            ItemCardapio(4, "Pastel", 7.50)
                        )
                    )
                }
            }

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
                            DrawerItem("Login") {
                                scope.launch { drawerState.close() }
                                navController.navigate("login")
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
//                            AppNavigation(navController = navController, isAdminLogged,cardapio,pedidos,carrinho)
                            AppNavigation(navController = navController,viewModel)
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
            IconButton(onClick = { navController.navigate("cardapio") }) {
                Icon(
                    painter = painterResource(id = R.drawable.utensils_solid_full),
                    contentDescription = "Cardápio",
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
//fun AppNavigation(navController: NavHostController, isAdminLogged: MutableState<Boolean>, cardapio: MutableList<ItemCardapio>, pedidos: MutableList<Pedido>, carrinho: MutableList<ItemCarrinho>) {
fun AppNavigation(navController: NavHostController, viewModel: AppViewModel) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
//        composable("home") { GridTest() }
//        composable("search") { SearchScreen() }
//        composable("cart") { CartScreen(carrinho, pedidos) }
//        composable("adminMenu") { AdminMenuScreen(navController) }
//        composable("login") { AdminLoginScreen(navController, isAdminLogged) }
//        composable("pedidos") { AdminPedidosScreen(navController, pedidos) }
//        composable("edicao") { AdminEditarScreen(navController, cardapio) }
//        composable("config") { ConfigScreen() }
//        composable("cardapio") { CardapioScreen(cardapio,carrinho) }
        composable("home") { GridTest() }
        composable("search") { SearchScreen(navController, viewModel) }
        composable("cart") { CartScreen(navController,viewModel) }
        composable("adminMenu") { AdminMenuScreen(navController) }
        composable("login") { AdminLoginScreen(navController) }
        composable("pedidos") { AdminPedidosScreen(navController, viewModel) }
        composable("edicao") { AdminEditarScreen(navController, viewModel) }
        composable("config") { ConfigScreen() }
        composable("cardapio") { CardapioScreen(navController,viewModel) }
    }
}
//@Composable
//fun AppNavigation(navController: NavHostController, viewModel: AppViewModel) {
//    NavHost(navController, startDestination = "search") {
//        composable("search") { SearchScreen(navController, viewModel) }
//        composable("cardapio") { CardapioScreen(navController, viewModel) }
//        composable("cart") { CartScreen(navController, viewModel) }
//        composable("pedidos") { AdminPedidosScreen(navController, viewModel) }
//        composable("edicao") { AdminEditarScreen(navController, viewModel) }
//    }
//}

// ---------- Telas ----------

@Composable
fun AdminMenuScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEBC8))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Painel Administrativo",
            color = Color(0xFF7A4A00),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("pedidos") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD09409))
        ) {
            Text("Ver Pedidos", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("edicao") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD09409))
        ) {
            Text("Editar Cardápio", color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = { navController.navigate("home") },
            border = BorderStroke(1.dp, Color(0xFF7A4A00))
        ) {
            Text("Voltar ao modo normal", color = Color(0xFF7A4A00))
        }
    }
}

@Composable
fun AdminLoginScreen(navController: NavHostController) {
//    fun AdminLoginScreen(navController: NavHostController, isAdminLogged: MutableState<Boolean>) {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEBC8))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login Administrativo", color = Color(0xFF7A4A00), fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = user, onValueChange = { user = it }, label = { Text("Usuário") })
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                if (user == "admin" && pass == "1234") {
//                    isAdminLogged.value = true
                    navController.navigate("adminMenu")
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD09409))
        ) {
            Text("Entrar", color = Color.White)
        }
    }
}



@Composable
fun AdminEditarScreen(navController: NavHostController, viewModel: AppViewModel) {
//    fun AdminEditarScreen(navController: NavHostController, cardapio: MutableList<ItemCardapio>) {
    var nome by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left_solid_full),
                    contentDescription = "Voltar",
                    tint = Color.Unspecified, // mantém as cores originais da imagem
//                    modifier = Modifier.size(24.dp)
                )
            }
            Text("Editar Cardápio", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") })
        OutlinedTextField(value = preco, onValueChange = { preco = it }, label = { Text("Preço") })

        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            if (nome.isNotBlank() && preco.isNotBlank()) {
//                cardapio.add(ItemCardapio(cardapio.size + 1, nome, preco.toDouble()))
                nome = ""; preco = ""
            }
        }) {
            Text("Adicionar Item")
        }

        Spacer(Modifier.height(16.dp))
        Text("Itens atuais:")
//        for (item in cardapio) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text("${item.nome} - R$ %.2f".format(item.preco))
//                IconButton(onClick = { navController.popBackStack() }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.trash_solid_full),
//                        contentDescription = "Remover",
//                        tint = Color.Unspecified, // mantém as cores originais da imagem
////                    modifier = Modifier.size(24.dp)
//                    )
//                }
//            }
//        }
    }
}


@Composable
fun AdminPedidosScreen(navController: NavHostController, viewModel: AppViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left_solid_full),
                    contentDescription = "Voltar",
                    tint = Color.Unspecified
                )
            }
            Text("Pedidos", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(8.dp))
        Text("Exemplo: pedidos aparecerão aqui quando finalizados.")
    }
}

//@Composable
//fun AdminPedidosScreen(navController: NavHostController, pedidos: MutableList<Pedido>) {
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            IconButton(onClick = { navController.popBackStack() }) {
//                Icon(
//                    painter = painterResource(id = R.drawable.arrow_left_solid_full),
//                    contentDescription = "Remover",
//                    tint = Color.Unspecified, // mantém as cores originais da imagem
////                    modifier = Modifier.size(24.dp)
//                )
//            }
//            Text("Pedidos", fontWeight = FontWeight.Bold)
//        }
//        Spacer(Modifier.height(8.dp))
//        for (pedido in pedidos) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//                    .border(1.dp, Color.Gray)
//                    .padding(8.dp)
//            ) {
//                Text("Pedido #${pedido.id}")
//                pedido.itens.forEach {
//                    Text("- ${it.item.nome} x${it.quantidade}")
//                }
//                if (!pedido.completo) {
//                    Button(onClick = { pedido.completo = true }) { Text("Marcar como completo") }
//                } else {
//                    Text("✅ Completo", color = Color.Green)
//                }
//            }
//        }
//    }
//}


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
fun CardapioScreen(navController: NavHostController, viewModel: AppViewModel) {
    val padaria = viewModel.currentBakery
    if (padaria == null) {
        Text("Selecione uma padaria primeiro.")
        return
    }

    var search by remember { mutableStateOf("") }
    var gridMode by remember { mutableStateOf(true) }

    val itens = remember {
        mutableStateListOf(
            MenuItem("1", "Pão Francês", 0.8, R.drawable.checkered),
            MenuItem("2", "Café com Leite", 4.5, R.drawable.checkered),
            MenuItem("3", "Coxinha", 6.0, R.drawable.checkered),
        )
    }

    val filtrados = itens.filter { it.name.contains(search, ignoreCase = true) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Padaria: ${padaria.name}", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Pesquisar no cardápio") },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { gridMode = !gridMode }) {
                Icon(
                    painterResource(
                        id = if (gridMode) R.drawable.list_solid_full else R.drawable.list_solid_full
                    ),
                    contentDescription = "Trocar layout",
                    tint = Color.Gray
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        if (gridMode)
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(filtrados) { item -> MenuCard(item, viewModel) }
            }
        else
            LazyColumn {
                items(filtrados) { item -> MenuCard(item, viewModel) }
            }
    }
}

@Composable
fun MenuCard(item: MenuItem, viewModel: AppViewModel) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column {
            Image(
                painter = painterResource(id = item.image),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(item.name, fontWeight = FontWeight.Bold)
                    Text("R$ %.2f".format(item.price))
                }
                Button(onClick = { viewModel.addToCart(item) }) {
                    Text("+")
                }
            }
        }
    }
}



@Composable
fun SearchScreen(navController: NavHostController, viewModel: AppViewModel) {
    var query by remember { mutableStateOf("") }

    val padariasFiltradas = viewModel.bakeries.filter {
        it.name.contains(query, ignoreCase = true) || it.id.contains(query)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar padaria por nome ou ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        padariasFiltradas.forEach { padaria ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        viewModel.selectBakery(padaria)
                        navController.navigate("cardapio")
                    }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(padaria.name, fontWeight = FontWeight.Bold)
                    Text(padaria.description)
                }
            }
        }
    }
}



@Composable
fun CartScreen(navController: NavHostController, viewModel: AppViewModel) {
    val padaria = viewModel.currentBakery ?: return Text("Selecione uma padaria.")
    val carrinho = viewModel.cartItems

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Carrinho - ${padaria.name}", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        if (carrinho.isEmpty()) {
            Text("Carrinho vazio.")
        } else {
            carrinho.forEach { item ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${item.item.name} x${item.quantity}")
                    Row {
                        IconButton(onClick = { viewModel.removeFromCart(item.item) }) {
                            Text("-")
                        }
                        IconButton(onClick = { viewModel.addToCart(item.item) }) {
                            Text("+")
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                if (carrinho.isNotEmpty()) {
                    // aqui salvaria em pedidos admin
                    carrinho.clear()
                    navController.navigate("pedidos")
                }
            }) {
                Text("Finalizar Pedido")
            }
        }
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

// ---------- Persistencia ----------

data class ItemCardapio(
    val id: Int,
    var nome: String,
    var preco: Double
)

data class ItemCarrinho(
    val item: ItemCardapio,
    var quantidade: Int
)

data class Pedido(
    val id: Int,
    val itens: List<ItemCarrinho>,
    var completo: Boolean = false
)

class AppViewModel : ViewModel() {
    var currentBakery by mutableStateOf<Bakery?>(null)
    var cartItems = mutableStateListOf<CartItem>()
    var bakeries = listOf(
        Bakery("1", "Padaria Central", "Pães e bolos variados"),
        Bakery("2", "Padaria do Bairro", "Café, salgados e doces")
    )

    fun selectBakery(bakery: Bakery) {
        currentBakery = bakery
        cartItems.clear() // limpa carrinho ao trocar padaria
    }

    fun addToCart(item: MenuItem) {
        val existing = cartItems.find { it.item.id == item.id }
        if (existing != null) existing.quantity++
        else cartItems.add(CartItem(item, 1))
    }

    fun removeFromCart(item: MenuItem) {
        val existing = cartItems.find { it.item.id == item.id }
        if (existing != null) {
            if (existing.quantity > 1) existing.quantity--
            else cartItems.remove(existing)
        }
    }
}

data class Bakery(val id: String, val name: String, val description: String)
data class MenuItem(val id: String, val name: String, val price: Double, val image: Int)
data class CartItem(val item: MenuItem, var quantity: Int)


@Preview(showBackground = true)
@Composable
fun GridPreview() {
    MOBILELayoutTheme {
        GridTest()
    }
}