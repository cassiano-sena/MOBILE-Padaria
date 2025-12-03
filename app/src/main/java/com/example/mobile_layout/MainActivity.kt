package com.example.mobile_layout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.DocumentSnapshot
import com.example.mobile_layout.ui.theme.MOBILELayoutTheme

// ----------------------------
// MainActivity
// ----------------------------
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inicia farebase
        FirebaseApp.initializeApp(this)

        setContent {
            val viewModel = remember { AppViewModel() } // ViewModel

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
//                            DrawerItem("Sair") {
//                                // ação de sair
//                            }
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
                            AppNavigation(navController = navController, viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------
// UI helpers
// ----------------------------
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

// ----------------------------
// Navigation
// ----------------------------
@Composable
fun AppNavigation(navController: NavHostController, viewModel: AppViewModel) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { GridTest() }
        composable("search") { SearchScreen(navController, viewModel) }
        composable("cart") { CartScreen(navController, viewModel) }
        composable("adminMenu") { AdminMenuScreen(navController) }
        composable("login") { AdminLoginScreen(navController) }
        composable("pedidos") { AdminPedidosScreen(navController, viewModel) }
        composable("edicao") { AdminEditarScreen(navController, viewModel) }
        composable("config") { ConfigScreen() }
        composable("cardapio") { CardapioScreen(navController, viewModel) }
        composable("createPadaria") { CreatePadariaScreen(navController, viewModel) }
    }
}

// ----------------------------
// Screens
// ----------------------------
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

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("createPadaria") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD09409))
        ) {
            Text("Criar nova padaria", color = Color.White)
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
    var nome by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEBC8))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left_solid_full),
                    contentDescription = "Voltar",
                    tint = Color.Unspecified
                )
            }
            Text("Editar Cardápio", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome do item") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = preco,
            onValueChange = { preco = it },
            label = { Text("Preço (ex: 4.50)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                if (nome.isNotBlank() && preco.isNotBlank()) {
                    viewModel.addMenuItemFirestore(nome, preco.toDoubleOrNull() ?: 0.0)
                    nome = ""
                    preco = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar Item ao Cardápio")
        }

        Spacer(Modifier.height(16.dp))

        Text("Itens atuais:", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        viewModel.menuItems.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${item.name} - R$ %.2f".format(item.price))
                IconButton(onClick = { viewModel.removeMenuItemFirestore(item) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.trash_solid_full),
                        contentDescription = "Remover",
                        tint = Color.Unspecified
                    )
                }
            }
        }
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

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(viewModel.pedidos) { pedido ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(1.dp, Color.Gray)
                        .padding(8.dp)
                ) {
                    Text("Pedido #${pedido.id}")
                    Text("Mesa: ${pedido.mesa ?: "Não definido"}")
                    Spacer(Modifier.height(4.dp))

                    pedido.itens.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${item.item.name} x${item.quantity.value}")
                            Text("R$ %.2f".format(item.item.price * item.quantity.value))
                        }
                    }

                    Spacer(Modifier.height(4.dp))
                    val total = pedido.itens.sumOf { it.item.price * it.quantity.value }
                    Text("Total: R$ %.2f".format(total), fontWeight = FontWeight.Bold)

                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Status: ${
                            when (pedido.status.value) {
                                PedidoStatus.EM_ESPERA -> "Em espera"
                                PedidoStatus.PREPARANDO -> "Preparando"
                                PedidoStatus.PRONTO -> "Pronto"
                                PedidoStatus.CONCLUIDO -> "Concluído"
                            }
                        }"
                    )

                    Row {
                        Button(onClick = { viewModel.avancarPedidoFirestore(pedido) }) {
                            Text("Avançar")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { viewModel.regredirPedidoFirestore(pedido) }) {
                            Text("Regredir")
                        }
                    }
                }
            }
        }
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
fun CardapioScreen(navController: NavHostController, viewModel: AppViewModel) {
    val padaria = viewModel.currentPadaria
    if (padaria == null) {
        Text("Selecione uma padaria primeiro.")
        return
    }

    var search by remember { mutableStateOf("") }
    var gridMode by remember { mutableStateOf(true) }

    val itens = viewModel.menuItems
    val filtrados = itens.filter { it.name.contains(search, ignoreCase = true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bakery),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp))
                    .background(
                        color = Color.White.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Padaria: ${padaria.name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

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
fun SearchScreen(
    navController: NavHostController,
    viewModel: AppViewModel
) {
    var query by remember { mutableStateOf("") }

    // Carrega padarias quando a tela abre
    LaunchedEffect(Unit) {
        viewModel.loadPadariasFromFirestore()
    }

    val padariasFiltradas = viewModel.padariaState.filter { padaria ->
        padaria.name.contains(query, ignoreCase = true) ||
                padaria.id.contains(query, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

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
                        viewModel.selectPadaria(padaria)
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
fun CreatePadariaScreen(
    navController: NavHostController,
    viewModel: AppViewModel
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_left_solid_full),
                contentDescription = "Voltar",
                tint = Color.Unspecified
            )
        }
        Text("Editar Cardápio", fontWeight = FontWeight.Bold)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(Modifier.height(32.dp))


        Text(
            text = "DEBUG: Tela de criação de padarias",
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome da padaria") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.createPadaria(name, description)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar padaria")
        }
    }
}


@Composable
fun CartScreen(navController: NavHostController, viewModel: AppViewModel) {
    val padaria = viewModel.currentPadaria ?: return Text("Selecione uma padaria.")
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
                    Text("${item.item.name} x${item.quantity.value}")
                    IconButton(onClick = { if (item.quantity.value > 1) item.quantity.value-- }) { Text("-") }
                    IconButton(onClick = { item.quantity.value++ }) { Text("+") }
                }
            }

            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                if (carrinho.isNotEmpty()) {
                    viewModel.criarPedidoFirestore(carrinho.toList())
                    carrinho.clear()
                }
            }) {
                Text("Finalizar Pedido")
            }
        }
    }
}

@Composable
fun GridTest() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bread_basket),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color.White.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "MOBILE-PADARIA",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B4513),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


// ----------------------------
// Persistencia com Firestore
// ----------------------------
data class Padaria(val id: String, val name: String, val description: String)
data class MenuItem(val id: String, val name: String, val price: Double, val image: Int)
data class CartItem(
    val item: MenuItem,
    var quantity: MutableState<Int> = mutableStateOf(1)
)

enum class PedidoStatus {
    EM_ESPERA,
    PREPARANDO,
    PRONTO,
    CONCLUIDO
}

data class Pedido(
    val id: Int,
    val itens: List<CartItem>,
    var status: MutableState<PedidoStatus> = mutableStateOf(PedidoStatus.EM_ESPERA),
    val mesa: String? = null,
    val firestoreId: String? = null
)

class AppViewModel : ViewModel() {

    // -------------------------------
    // Estados do app
    // -------------------------------
    var isAdminLogged = mutableStateOf(false)
    var currentPadaria by mutableStateOf<Padaria?>(null)

    var cartItems = mutableStateListOf<CartItem>()      // carrinho do usuário
    var pedidos = mutableStateListOf<Pedido>()          // lista de pedidos
    var menuItems = mutableStateListOf<MenuItem>()      // cardápio sincronizado com Firestore

    var padariaState = mutableStateListOf<Padaria>()

    fun loadPadariasFromFirestore() {
        db.collection("padaria")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { doc ->
                    val id = doc.id
                    val name = doc.getString("name") ?: return@mapNotNull null
                    val description = doc.getString("description") ?: ""
                    Padaria(id, name, description)
                }

                padariaState.clear()
                padariaState.addAll(list)
            }
    }

    // Firestore
    private val db = Firebase.firestore
    private var menuListener: ListenerRegistration? = null
    private var pedidosListener: ListenerRegistration? = null

    init {
        // start listening to collections
        listenMenuFirestore()
        listenPedidosFirestore()
    }

    override fun onCleared() {
        super.onCleared()
        menuListener?.remove()
        pedidosListener?.remove()
    }

    // -------------------------------
    // Firestore: Menu
    // -------------------------------
    private fun listenMenuFirestore() {
        val padaria = currentPadaria ?: return

        menuListener = db.collection("menu")
            .whereEqualTo("padariaId", padaria.id)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // log error if needed
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    // rebuild list
                    val newList = snapshot.documents.mapNotNull { doc ->
                        val name = doc.getString("name") ?: return@mapNotNull null
                        val price = doc.getDouble("price") ?: (doc.getLong("price")?.toDouble())
                        val id = doc.id
                        MenuItem(
                            id = id,
                            name = name,
                            price = price ?: 0.0,
                            image = R.drawable.checkered // aqui trocar imagm
                        )
                    }
                    menuItems.apply {
                        clear()
                        addAll(newList)
                    }
                }
            }
    }

    fun addMenuItemFirestore(name: String, price: Double) {
        val padaria = currentPadaria ?: return

        val data = hashMapOf(
            "name" to name,
            "price" to price,
            "padariaId" to padaria.id
        )

        db.collection("menu").add(data)
    }


    fun removeMenuItemFirestore(item: MenuItem) {
        if (item.id.isNotBlank()) {
            db.collection("menu").document(item.id).delete()
                .addOnFailureListener {
                    // handle error
                }
        } else {
            // fallback: remove locally
            menuItems.remove(item)
        }
    }

    // -------------------------------
    // Firestore: Pedidos
    // -------------------------------
    private fun listenPedidosFirestore() {
        pedidosListener?.remove()

        val padaria = currentPadaria ?: return

        pedidosListener = db.collection("pedidos")
            .whereEqualTo("padariaId", padaria.id)
            .orderBy("createdAt")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    pedidos.clear()
                    pedidos.addAll(
                        snapshot.documents.mapNotNull { mapDocumentToPedido(it) }
                    )
                }
            }
    }

    fun criarPedidoFirestore(itens: List<CartItem>) {
        val padaria = currentPadaria ?: return

        val idGerado = System.currentTimeMillis()

        val data = hashMapOf(
            "id" to idGerado,
            "padariaId" to padaria.id,
            "createdAt" to idGerado,
            "status" to "EM_ESPERA",
            "mesa" to "1",
            "itens" to itens.map {
                hashMapOf(
                    "id" to it.item.id,
                    "name" to it.item.name,
                    "price" to it.item.price,
                    "quantity" to it.quantity.value
                )
            }
        )

        db.collection("pedidos").add(data)
    }

    private fun mapDocumentToPedido(doc: DocumentSnapshot): Pedido? {

        val id = doc.getLong("id")?.toInt() ?: return null
        val mesa = doc.getString("mesa") ?: "?"
        val statusString = doc.getString("status") ?: "EM_ESPERA"
        val status = PedidoStatus.valueOf(statusString)

        val itensListRaw = doc.get("itens") as? List<Map<String, Any?>> ?: emptyList()

        val itens = itensListRaw.map { item ->
            CartItem(
                item = MenuItem(
                    id = item["id"] as String,
                    name = item["name"] as String,
                    price = (item["price"] as Number).toDouble(),
                    image = R.drawable.checkered
                ),
                quantity = mutableStateOf((item["quantity"] as Number).toInt())
            )
        }

        return Pedido(
            id = id,
            itens = itens,
            mesa = mesa,
            status = mutableStateOf(status),
            firestoreId = doc.id
        )
    }



    // avançar e regredir no Firestore
    fun avancarPedidoFirestore(pedido: Pedido) {
        val next = when (pedido.status.value) {
            PedidoStatus.EM_ESPERA -> PedidoStatus.PREPARANDO
            PedidoStatus.PREPARANDO -> PedidoStatus.PRONTO
            PedidoStatus.PRONTO -> PedidoStatus.CONCLUIDO
            PedidoStatus.CONCLUIDO -> PedidoStatus.CONCLUIDO
        }
        updatePedidoStatusFirestore(pedido, next)
    }

    fun regredirPedidoFirestore(pedido: Pedido) {
        val prev = when (pedido.status.value) {
            PedidoStatus.CONCLUIDO -> PedidoStatus.PRONTO
            PedidoStatus.PRONTO -> PedidoStatus.PREPARANDO
            PedidoStatus.PREPARANDO -> PedidoStatus.EM_ESPERA
            PedidoStatus.EM_ESPERA -> PedidoStatus.EM_ESPERA
        }
        updatePedidoStatusFirestore(pedido, prev)
    }

    private fun updatePedidoStatusFirestore(pedido: Pedido, status: PedidoStatus) {
        val docId = pedido.firestoreId ?: return
        db.collection("pedidos").document(docId)
            .update("status", status.name)
            .addOnFailureListener {
                // handle error
            }
    }

    // -------------------------------
    // Cliente: Carrinho / Menu local
    // -------------------------------
    fun selectPadaria(p: Padaria) {
        currentPadaria = p
        listenMenuFirestore()
        listenPedidosFirestore()
    }

    fun createPadaria(name: String, description: String) {
        val data = hashMapOf(
            "name" to name,
            "description" to description
        )

        Firebase.firestore.collection("padaria")
            .add(data)
    }


    fun addToCart(item: MenuItem) {
        val existing = cartItems.find { it.item.id == item.id }
        if (existing != null) {
            existing.quantity.value++
        } else {
            cartItems.add(CartItem(item))
        }
    }
}

// ----------------------------
// Preview
// ----------------------------
@Preview(showBackground = true)
@Composable
fun GridPreview() {
    MOBILELayoutTheme {
        GridTest()
    }
}
