import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import br.senai.sp.jandira.vivaris.model.Cliente
import br.senai.sp.jandira.vivaris.model.Sexo
import br.senai.sp.jandira.vivaris.model.SexoResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun converterStringParaDate(data: String): Date? {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return try {
        sdf.parse(data)
    } catch (e: ParseException) {
        Log.e("Cadastro", "Erro ao converter data: ${e.message}")
        null
    }
}

fun formatarDataParaEnviar(data: Date): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(data)
}




fun formatarData(data: String): String? {
    return try {
        val formatoEntrada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formatoSaida = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = formatoEntrada.parse(data)
        formatoSaida.format(date)
    } catch (e: ParseException) {
        Log.e("Erro de Formatação", "Erro ao formatar data: ${e.message}")
        null
    }
}






@Composable
fun Cadastro(controleDeNavegacao: NavHostController) {
    var nomeState by remember { mutableStateOf("") }
    var telefoneState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var dataNascimentoState by remember { mutableStateOf("") }
    var senhaState by remember { mutableStateOf("") }
    var cpfState by remember { mutableStateOf("") }
    var crpState by remember { mutableStateOf("") }
   var preferenciaSelecionada by remember { mutableStateOf(1) }
    var isPsicologoState by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val retrofitFactory = RetrofitFactory()
    val clienteService = retrofitFactory.getClienteService()
    val sexoService = retrofitFactory.getSexoService()
    var sexos by remember { mutableStateOf<List<Sexo>>(emptyList()) }
    var loadingSexos by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        sexoService.getSexo().enqueue(object : Callback<SexoResponse> {
            override fun onResponse(call: Call<SexoResponse>, response: Response<SexoResponse>) {
                if (response.isSuccessful) {
                    sexos = response.body()?.data ?: emptyList()
                    Log.d("Sexos", "Sexos carregados: $sexos")
                } else {
                    Toast.makeText(context, "Erro ao carregar sexos: ${response.code()}", Toast.LENGTH_SHORT).show()
                    Log.e("Erro", "Corpo da resposta: ${response.errorBody()?.string()}")
                }
                loadingSexos = false
            }

            override fun onFailure(call: Call<SexoResponse>, t: Throwable) {
                Toast.makeText(context, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
                loadingSexos = false
            }
        })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF15A27A),
                        Color(0xFF67DEBC)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Cadastre-se",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { isPsicologoState = true },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isPsicologoState) Color.Green else Color(0x4D19493B)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Psicólogo", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { isPsicologoState = false },
                        colors = ButtonDefaults.buttonColors(containerColor = if (!isPsicologoState) Color.Green else Color(0x4D19493B)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cliente", color = Color.White)
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = nomeState,
                    onValueChange = { nomeState = it },
                    label = { Text("Nome Completo", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFFFFF),
                        unfocusedBorderColor = Color(0xFFFFFFFF),
                        focusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = telefoneState,
                    onValueChange = { telefoneState = it },
                    label = { Text("Telefone", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFFFFF),
                        unfocusedBorderColor = Color(0xFFFFFFFF),
                        focusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = emailState,
                    onValueChange = { emailState = it },
                    label = { Text("Email", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFFFFF),
                        unfocusedBorderColor = Color(0xFFFFFFFF),
                        focusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = dataNascimentoState,
                    onValueChange = { input ->
                        if (input.length <= 10) {
                            dataNascimentoState = input
                        }
                    },
                    label = { Text("Data de Nascimento (dd/MM/yyyy)", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFFFFF),
                        unfocusedBorderColor = Color(0xFFFFFFFF),
                        focusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }


            item {
                OutlinedTextField(
                    value = senhaState,
                    onValueChange = { senhaState = it },
                    label = { Text("Senha", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFFFFF),
                        unfocusedBorderColor = Color(0xFFFFFFFF),
                        focusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = cpfState,
                    onValueChange = { cpfState = it },
                    label = { Text("CPF", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFFFFF),
                        unfocusedBorderColor = Color(0xFFFFFFFF),
                        focusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }


            if (isPsicologoState) {
                item {
                    OutlinedTextField(
                        value = crpState,
                        onValueChange = { crpState = it },
                        label = { Text("CRP", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFFFFF),
                            unfocusedBorderColor = Color(0xFFFFFFFF),
                            focusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }


            item {
                Button(
                    onClick = {

                        if (nomeState.isBlank() || telefoneState.isBlank() || emailState.isBlank() ||
                            senhaState.isBlank() || cpfState.isBlank() || (isPsicologoState && crpState.isBlank())
                        ) {
                            Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
                        } else {
                            val cliente = Cliente(
                                nome = nomeState,
                                telefone = telefoneState,
                                email = emailState,
                                dataNascimento = dataNascimentoState,
                                senha = senhaState,
                                id_sexo = 1,
                                cpf = cpfState,
                               // crp = if (isPsicologoState) crpState else null,
                                link_instagram = null,
                                foto_perfil = null,
                                id_preferencias = listOf(preferenciaSelecionada)
                            )


                            Log.d("Cadastro", "Cliente a ser enviado: $cliente")

                            coroutineScope.launch {
                                clienteService.cadastrarCliente(cliente).enqueue(object : Callback<Cliente> {
                                    override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(context, "Cliente cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                                            controleDeNavegacao.navigate("login")
                                        } else {
                                            Toast.makeText(context, "Erro ao cadastrar cliente: ${response.code()}", Toast.LENGTH_SHORT).show()
                                            Log.e("Cadastro", "Erro ao cadastrar: ${response.errorBody()?.string()}")
                                        }
                                    }

                                    override fun onFailure(call: Call<Cliente>, t: Throwable) {
                                        Toast.makeText(context, "Erro: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                                        Log.e("Cadastro", "Falha na chamada: ${t.localizedMessage}")
                                    }
                                })
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x4D19493B)),
                    shape = RoundedCornerShape(13.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.3.dp)
                ) {
                    Text(text = "Salvar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }




            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Já possui uma conta? ", color = Color.White)
                    Text(
                        text = "Faça login",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { controleDeNavegacao.navigate("login") }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCadastro() {
    val navController = rememberNavController()
    Cadastro(controleDeNavegacao = navController)
}
