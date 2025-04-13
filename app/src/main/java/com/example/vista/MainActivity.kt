package com.example.vista

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Xml
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.edit
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.vista.ui.theme.LocalExtraColors
import com.example.vista.ui.theme.VistaTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.threetenabp.AndroidThreeTen
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Properties
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.String
import kotlin.math.abs

fun encryptAES(data: ByteArray): ByteArray {
    val key = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.ENCRYPT_MODE, key)
    return cipher.doFinal(data)
}

fun decryptAES(data: ByteArray): ByteArray {
    val key = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.DECRYPT_MODE, key)
    return cipher.doFinal(data)
}

// Lang.get(0, context)
object Lang {
    private val keys: Map<Int, Int> by lazy {
        val fields = R.string::class.java.fields
        fields
            .filter { it.name.startsWith("lang_") }
            .associate { it.name.removePrefix("lang_").toInt() to it.getInt(null) }
    }

    fun get(key: Int, context: Context): String {
        return keys[key]?.let { context.getString(it) } ?: "Empty"
    }
}

// Define data classes for Settings and Transaction
data class VistaSettings(
    val name: String,
    val email: String,
    val close: String,
    val expire: String,
    val notify: Boolean = true
)

data class Card(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val frontImage: String,
    val backImage: String
)

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val date: String,
    val value: String,
    val divider: String,
    val executed: Boolean
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this) // Inicializa a biblioteca ThreeTenABP

        WindowCompat.setDecorFitsSystemWindows(window, false) // Permite desenhar sob a status bar
        setStatusBarAppearance()

        val startPage = intent.getStringExtra("navigateTo")

        setContent {
            VistaTheme {
                AppNavigation(startPage = startPage)
            }
        }

        requestPermissions()
        readOrCreateFile()
    }

    // Função para solicitar permissões
    private fun requestPermissions() {
        // Se já estiver em uma versão Android 13 ou superior, você pode pedir permissões de notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }
    }

    private fun setStatusBarAppearance() {
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false // Ícones brancos na status bar
    }

    private fun readOrCreateFile() {
        val vistaDir = File(filesDir, "vista").apply {
            if (!exists()) mkdir()
        }

        val filePath = File(vistaDir, "vista.xml")

        if (!filePath.exists()) {
            Log.w("Vista", "vista.xml não existe! Criando novo arquivo.")
            try {
                filePath.writeText(
                    "<vista><settings></settings><cards></cards><transactions></transactions></vista>"
                )
                Log.d("Vista", "vista.xml foi criado")
            } catch (e: Exception) {
                Log.e("Vista", "Erro ao criar o arquivo: ${e.message}")
            }
        }
    }
}

fun saveVistaToXML(
    settings: VistaSettings,
    cards: List<Card>,
    transactions: List<Transaction>,
    context: Context
) {
    val vistaDir = File(context.filesDir, "vista").apply { if (!exists()) mkdir() }

    val byteOutputStream = ByteArrayOutputStream()
    val xml = Xml.newSerializer()

    xml.setOutput(byteOutputStream, "UTF-8")
    xml.startDocument("UTF-8", true)
    xml.startTag("", "Vista")

    xml.startTag("", "Settings")
    xml.attribute("", "Name", settings.name)
    xml.attribute("", "Email", settings.email)
    xml.attribute("", "Close", settings.close)
    xml.attribute("", "Expire", settings.expire)
    xml.attribute("", "Notify", settings.notify.toString())
    xml.endTag("", "Settings")

    xml.startTag("", "Cards")
    for (card in cards) {
        xml.startTag("", "Card")
        xml.attribute("", "ID", card.id)
        xml.attribute("", "Name", card.name)
        xml.attribute("", "FrontImage", card.frontImage)
        xml.attribute("", "BackImage", card.backImage)
        xml.endTag("", "Card")
    }
    xml.endTag("", "Cards")

    xml.startTag("", "Transactions")
    for (transaction in transactions) {
        xml.startTag("", "Transaction")
        xml.attribute("", "ID", transaction.id)
        xml.attribute("", "Name", transaction.name)
        xml.attribute("", "Date", transaction.date)
        xml.attribute("", "Value", transaction.value)
        xml.attribute("", "Divider", transaction.divider)
        xml.attribute("", "Executed", transaction.executed.toString())
        xml.endTag("", "Transaction")
    }
    xml.endTag("", "Transactions")

    xml.endTag("", "Vista")
    xml.endDocument()

    val encryptedBytes = encryptAES(byteOutputStream.toByteArray())
    val file = File(vistaDir, "vista.xml")
    FileOutputStream(file).use { it.write(encryptedBytes) }
}


fun loadVistaFromXML(context: Context): Triple<VistaSettings?, List<Card>, List<Transaction>> {
    var settings: VistaSettings? = null
    val cards = mutableListOf<Card>()
    val transactions = mutableListOf<Transaction>()

    try {
        val vistaDir = File(context.filesDir, "vista")
        val encryptedBytes = File(vistaDir, "vista.xml").readBytes()
        val decryptedBytes = decryptAES(encryptedBytes)

        val inputStream = ByteArrayInputStream(decryptedBytes)
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(inputStream, "UTF-8")

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    "Settings" -> {
                        val name = parser.getAttributeValue(null, "Name") ?: "Guest"
                        val email = parser.getAttributeValue(null, "Email") ?: ""
                        val close = parser.getAttributeValue(null, "Close") ?: "1"
                        val expire = parser.getAttributeValue(null, "Expire") ?: "2"
                        val notify = parser.getAttributeValue(null, "Notify")?.toBoolean() ?: false
                        settings = VistaSettings(name, email, close, expire, notify)
                    }

                    "Card" -> {
                        val id = parser.getAttributeValue(null, "ID")?.takeIf { it.isNotBlank() }
                            ?: UUID.randomUUID().toString()
                        val name = parser.getAttributeValue(null, "Name") ?: ""
                        val frontImage = parser.getAttributeValue(null, "FrontImage") ?: ""
                        val backImage = parser.getAttributeValue(null, "BackImage") ?: ""
                        cards.add(Card(id, name, frontImage, backImage))
                    }

                    "Transaction" -> {
                        val id = parser.getAttributeValue(null, "ID")?.takeIf { it.isNotBlank() }
                            ?: UUID.randomUUID().toString()
                        val name = parser.getAttributeValue(null, "Name") ?: ""
                        val date = parser.getAttributeValue(null, "Date") ?: ""
                        val value = parser.getAttributeValue(null, "Value") ?: ""
                        val divider = parser.getAttributeValue(null, "Divider") ?: ""
                        val executed =
                            parser.getAttributeValue(null, "Executed")?.toBoolean() == true
                        transactions.add(Transaction(id, name, date, value, divider, executed))
                    }
                }
            }
            eventType = parser.next()
        }

        inputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return Triple(settings, cards, transactions)
}

fun salvarBackup(context: Context, uri: Uri) {
    try {
        val inputFile = File(context.filesDir, "vista/vista.xml")
        if (!inputFile.exists()) {
            Toast.makeText(context, Lang.get(1, context), Toast.LENGTH_SHORT).show()
            return
        }

        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            FileInputStream(inputFile).use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        Toast.makeText(context, Lang.get(2, context), Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, Lang.get(3, context), Toast.LENGTH_SHORT).show()
    }
}

fun importarBackup(context: Context, uri: Uri) {
    try {
        val vistaDir = File(context.filesDir, "vista").apply { if (!exists()) mkdir() }
        val outputFile = File(vistaDir, "vista.xml")

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(outputFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        Toast.makeText(context, Lang.get(4, context), Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, Lang.get(6, context), Toast.LENGTH_SHORT).show()
    }
}


fun getLastPage(context: Context) =
    context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        .getString("last_page", "Home") ?: "Home"

fun saveLastPage(context: Context, page: String) =
    context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit {
        putString("last_page", page)
    }

/* ─────────────────────────────────────────────────────────────────────────────
   Funções de persistência para o último card selecionado:
──────────────────────────────────────────────────────────────────────────── */
fun getLastCardId(context: Context): String? =
    context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        .getString("last_card_id", null)

fun saveLastCardId(context: Context, cardId: String?) =
    context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit {
        if (cardId == null) remove("last_card_id")
        else putString("last_card_id", cardId)
    }


fun getAppVersion(context: Context): String {
    return try {
        val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName
            ?: Lang.get(7, context) // Caso versionName seja nulo, retorna "Desconhecida"
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        Lang.get(7, context) // Caso não consiga encontrar a versão
    }
}

fun getAppName(context: Context): String {
    val packageManager = context.packageManager
    val applicationInfo = context.applicationInfo
    return packageManager.getApplicationLabel(applicationInfo).toString()
}


@Composable
fun Header(title: String, onSettingsClick: () -> Unit, headerBackgroundColor: Color) {
    val foreColor = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(headerBackgroundColor), // Usando a cor passada como parâmetro
        contentAlignment = Alignment.Center
    ) {
        // Row para garantir que o título ocupe toda a largura disponível e o botão de configurações fique à direita
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Título clicável
            Text(
                text = title,
                color = foreColor,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f) // Garante que o título ocupe a largura disponível
                    .padding(vertical = 15.dp)
                    .padding(start = 50.dp)
            )

            // Botão de configurações clicável na área inteira
            Box(
                modifier = Modifier
                    .clickable { onSettingsClick() }
                    .align(Alignment.CenterVertically) // Alinha verticalmente no centro
                    .padding(14.dp) // Espaçamento para a área clicável
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = foreColor
                )
            }
        }
    }
}

@Composable
fun Footer(
    title: String,
    onClick: () -> Unit,
    footerBackgroundColor: Color
) {
    val foreColor = MaterialTheme.colorScheme.onBackground
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(footerBackgroundColor.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        // Row para garantir que o título ocupe toda a largura disponível e o botão de configurações fique à direita
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Título clicável
            Text(
                text = title,
                color = foreColor,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f) // Garante que o título ocupe a largura disponível
                    .clickable { onClick() }
                    .padding(vertical = 15.dp)
                //.padding(start = 50.dp)
            )
        }
    }
}

// Função para gerar o nome do arquivo com a data atual
fun getBackupFileName(context: Context): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = dateFormat.format(Date())
    return "${getAppName(context).lowercase(Locale.getDefault())} $date"
}


@Composable
fun AppNavigation(startPage: String? = null) {
    val context = LocalContext.current
    var currentPage by remember {
        mutableStateOf(startPage ?: getLastPage(context))
    }

    var settings by remember { mutableStateOf<VistaSettings?>(null) }
    var cards by remember { mutableStateOf(emptyList<Card>()) }
    var transactions by remember { mutableStateOf(emptyList<Transaction>()) }

    LaunchedEffect(Unit) {
        val (loadedSettings, loadedCards, loadedTransactions) = loadVistaFromXML(context)
        settings = loadedSettings
        cards = loadedCards
        transactions = loadedTransactions
    }

    fun updateData(
        newSettings: VistaSettings = settings ?: VistaSettings("", "", "", ""),
        newCards: List<Card> = cards,
        newTransactions: List<Transaction> = transactions
    ) {
        settings = newSettings
        cards = newCards
        transactions = newTransactions
        saveVistaToXML(newSettings, newCards, newTransactions, context)
    }

    fun resetData() {
        val defaultSettings = VistaSettings("", "", "", "")
        settings = defaultSettings
        cards = emptyList()
        transactions = emptyList()
        saveVistaToXML(defaultSettings, cards, transactions, context)
    }

    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    DisposableEffect(backDispatcher) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentPage = when (currentPage) {
                    "Home" -> {
                        (context as Activity).finish()
                        "Home"
                    }

                    else -> "Home"
                }
                saveLastPage(context, currentPage)
            }
        }
        backDispatcher?.addCallback(callback)
        onDispose { callback.remove() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (currentPage) {
            "Home" -> {
                OverviewPage(
                    settings = settings ?: VistaSettings("", "", "", ""),
                    cards = cards,
                    transactions = transactions,
                    onNavigateToTransactions = { currentPage = "Transactions" },
                    onNavigateToSettings = { currentPage = "Settings" },
                    onAddCard = { newCard ->
                        updateData(newCards = cards + newCard)
                    },
                    onRemoveCard = { cardToRemove ->
                        updateData(newCards = cards.filter { it != cardToRemove })
                    },
                    onEditCard = { updatedCard ->
                        updateData(newCards = cards.map { if (it.id == updatedCard.id) updatedCard else it })
                    },
                    onAddTransaction = { newTransaction ->
                        updateData(newTransactions = transactions + newTransaction)
                    }
                )
            }

            "Transactions" -> {
                TransactionPage(
                    transactions = transactions,
                    onEditTransaction = { editedTransaction ->
                        // Atualiza a lista: substitui a transação com o ID correspondente
                        updateData(newTransactions = transactions.map {
                            if (it.id == editedTransaction.id) editedTransaction else it
                        })
                    },
                    onDeleteTransaction = { transactionToDelete ->
                        updateData(newTransactions = transactions.filter { it.id != transactionToDelete.id })
                    },
                    onAddTransaction = { newTransaction ->
                        updateData(newTransactions = transactions + newTransaction)
                    },
                    onNavigateToSettings = { currentPage = "Settings" },
                    onNavigateBack = { currentPage = "Home" }
                )
            }

            "Settings" -> {
                SettingsPage(
                    settings = settings ?: VistaSettings("", "", "", ""),
                    onBack = { currentPage = getLastPage(context) },
                    onSave = { newSettings ->
                        updateData(newSettings = newSettings)
                    },
                    onBackupImported = { newSettings, newTransactions, newCards ->
                        updateData(
                            newSettings = newSettings,
                            newTransactions = newTransactions,
                            newCards = newCards
                        )
                    },
                    onReset = { resetData() }
                )
            }
        }
    }
}

/*
private fun sendEmail(to: String, subject: String, body: String) {
    val emailUser = BuildConfig.EMAIL_USER // Unresolved reference 'EMAIL_USER'.
    val emailPass = BuildConfig.EMAIL_PASS // Unresolved reference 'EMAIL_PASS'.

    val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.port", "587")
    }

    val session = Session.getInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(emailUser, emailPass)
        }
    })

    try {
        val message = MimeMessage(session).apply {
            setFrom(InternetAddress(emailUser))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
            setSubject(subject)
            setText(body)
        }

        Transport.send(message)
        Log.d("NotificationWorker", "Email enviado com sucesso para $to")
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("NotificationWorker", "Erro ao enviar email: ${e.message}")
    }
}*/

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val closeDay = inputData.getInt("closeDay", 1)
        val email = inputData.getString("email") ?: return Result.failure()

        // Carrega as configurações e transações (suponha que loadVistaFromXML está implementado)
        val (settings, _, transactions) = loadVistaFromXML(applicationContext)
        val name = settings?.name?.ifBlank { "usuário" } ?: "usuário"

        // Verifica se hoje é o dia de fechar a fatura
        val todayCal = Calendar.getInstance()
        val currentDay = todayCal.get(Calendar.DAY_OF_MONTH)
        if (currentDay != closeDay) {
            // Se não é o dia de fechamento, apenas reagende e sai.
            scheduleNextFatura(applicationContext, closeDay, email)
            return Result.success()
        }

        // Calcula a fatura atual
        val faturaAtual = calcularFaturaAtual(transactions)
        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        val valorFormatado = currencyFormatter.format(faturaAtual)

        val faturaTexto = "${Lang.get(19, applicationContext)} $valorFormatado"
        val emailBody = "${Lang.get(50, applicationContext)} $name!\n\n${Lang.get(19,applicationContext)}: $valorFormatado"

        sendNotification(Lang.get(27, applicationContext), faturaTexto)
        //sendEmail(email, Lang.get(11, applicationContext), emailBody)

        // Agenda a próxima notificação para o próximo mês
        scheduleNextFatura(applicationContext, closeDay, email)

        return Result.success()
    }

    private fun calcularFaturaAtual(transactions: List<Transaction>): Double {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val currentDate = LocalDate.now()

        return transactions.sumOf { tx ->
            val divider = tx.divider.toIntOrNull() ?: 1
            val totalValue = tx.value.toDoubleOrNull() ?: 0.0
            val installmentValue = if (divider > 0) totalValue / divider else totalValue

            try {
                val startDate = LocalDateTime.parse(tx.date, formatter)
                if (divider == 0) {
                    // Para transações com divider 0: considera todas as parcelas do mês de criação até o mês atual
                    val start = startDate.toLocalDate()
                    val monthsBetween = ChronoUnit.MONTHS.between(
                        YearMonth.from(start),
                        YearMonth.from(currentDate)
                    ).toInt() + 1 // inclui o mês atual
                    installmentValue * monthsBetween
                } else {
                    // Para transações com divider > 0: soma os valores das parcelas com data não após o mês atual
                    var sum = 0.0
                    for (i in 0 until divider) {
                        val installmentDate = startDate.plusMonths(i.toLong()).toLocalDate()
                        if (!installmentDate.isAfter(currentDate)) {
                            sum += installmentValue
                        }
                    }
                    sum
                }
            } catch (e: Exception) {
                0.0
            }
        }
    }

    private fun sendNotification(title: String, message: String) {
        val channelId = "fatura_channel"
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificações de Fatura",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        // Cria uma intent que abre a MainActivity e navega para a Overview (você trata isso na MainActivity)
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigateTo", "Home")
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.vista_notify) // Substitua pelo seu ícone real
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(1, notification)
    }

}

/**
 * Agenda a próxima notificação para o dia de fechamento (closeDay) às 00:00.
 * Após executar, ele agendará apenas uma vez para o próximo mês.
 */
fun scheduleNextFatura(context: Context, closeDay: Int, email: String) {
    val now = Calendar.getInstance()
    val nextTrigger = Calendar.getInstance().apply {
        timeInMillis = now.timeInMillis
        // Define para 00:00:00 do dia de fechamento
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        set(Calendar.DAY_OF_MONTH, closeDay)
        // Se a data já passou, agende para o próximo mês
        if (this.timeInMillis <= now.timeInMillis) {
            add(Calendar.MONTH, 1)
        }
    }
    val delay = nextTrigger.timeInMillis - now.timeInMillis

    val inputData = Data.Builder()
        .putInt("closeDay", closeDay)
        .putString("email", email)
        .build()

    val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setInputData(inputData)
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        "faturaNotification",
        ExistingWorkPolicy.REPLACE,
        workRequest
    )
}


@Composable
fun SettingsPage(
    settings: VistaSettings,
    onBack: () -> Unit,
    onSave: (VistaSettings) -> Unit,
    onBackupImported: (VistaSettings, List<Transaction>, List<Card>) -> Unit,
    onReset: () -> Unit
) {
    val backColor = MaterialTheme.colorScheme.background
    val foreColor = MaterialTheme.colorScheme.onBackground
    val lightBackColor = LocalExtraColors.current.lightBackground

    val context = LocalContext.current
    val appVersion = getAppVersion(context)
    val focusManager = LocalFocusManager.current

    // Estados editáveis
    var nameField by remember { mutableStateOf(settings.name) }
    var emailField by remember { mutableStateOf(settings.email) }
    var closeSlider by remember { mutableStateOf(settings.close.toIntOrNull() ?: 1) }
    var notifyChecked by remember { mutableStateOf(settings.notify) }

    fun save() {
        val updatedSettings = settings.copy(
            name = nameField,
            email = emailField,
            close = closeSlider.toString(),
            notify = notifyChecked
        )
        onSave(updatedSettings)

        if (updatedSettings.notify) {
            val closeDay = updatedSettings.close.toIntOrNull() ?: 1
            val email = updatedSettings.email
            scheduleNextFatura(context, closeDay, email)
        } else {
            WorkManager.getInstance(context).cancelUniqueWork("faturaNotification")
        }
    }


    // Launchers para backup/importação
    val saveBackupLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/xml")) { uri ->
            uri?.let { salvarBackup(context, it) }
        }

    val importBackupLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                importarBackup(context, it)
                val (loadedSettings, loadedCards, loadedTransactions) = loadVistaFromXML(context)
                onBackupImported(loadedSettings ?: settings, loadedTransactions, loadedCards)
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backColor)
            .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } }
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Header(
            title = Lang.get(11, context),
            onSettingsClick = {},
            headerBackgroundColor = lightBackColor
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Configurações principais
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(lightBackColor, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        OutlinedTextField(
                            value = nameField,
                            onValueChange = { nameField = it },
                            label = { Text("${Lang.get(25, context)}", color = foreColor) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = emailField,
                            onValueChange = { emailField = it },
                            label = { Text("${Lang.get(28, context)}", color = foreColor) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "${Lang.get(9, context)} $closeSlider",
                            color = foreColor,
                            fontSize = 16.sp
                        )
                        Slider(
                            value = closeSlider.toFloat(),
                            onValueChange = { closeSlider = it.toInt() },
                            valueRange = 1f..25f,
                            steps = 23,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${Lang.get(29, context)}",
                                color = foreColor,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = notifyChecked,
                                onCheckedChange = {
                                    notifyChecked = it
                                },
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                save()
                                Toast.makeText(
                                    context,
                                    "${Lang.get(49, context)}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text("${Lang.get(8, context)}")
                        }
                    }
                }
            }

            // Backup e Reset
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(lightBackColor, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { saveBackupLauncher.launch(getBackupFileName(context)) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                Lang.get(13, context),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Button(
                            onClick = { importBackupLauncher.launch(arrayOf("*/*")) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                Lang.get(14, context),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Button(
                            onClick = {
                                onReset()
                                saveLastCardId(context, null)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                Lang.get(15, context),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // Informações do app
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(lightBackColor, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        val appName = getAppName(context)
                        Text(
                            appName,
                            color = foreColor,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${Lang.get(16, context)} $appVersion",
                            color = foreColor,
                            fontSize = 18.sp
                        )
                        Text(
                            "© ${
                                Calendar.getInstance().get(Calendar.YEAR)
                            } Asen Lab Corporation\n${Lang.get(17, context)}",
                            color = foreColor, fontSize = 16.sp
                        )
                    }
                }
            }
        }

        Footer(
            title = Lang.get(18, context),
            onClick = onBack,
            footerBackgroundColor = LocalExtraColors.current.lightBackground
        )
    }
}



fun compressImage(context: Context, uri: Uri): File? {
    return try {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }

        val file = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
        out.flush()
        out.close()
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


@Composable
fun CardScreen(
    existingCards: List<Card>,
    cardToEdit: Card? = null,
    onDismiss: () -> Unit,
    onSave: (Card) -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Cores do tema
    val lightBackColor = LocalExtraColors.current.lightBackground
    val onBackground = MaterialTheme.colorScheme.onBackground
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface

    var cardName by remember { mutableStateOf(cardToEdit?.name ?: "") }
    var frontImageUri by remember { mutableStateOf(cardToEdit?.frontImage) }
    var backImageUri by remember { mutableStateOf(cardToEdit?.backImage) }
    var captureMode by remember { mutableStateOf("") }
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para UCrop
    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.let { data ->
            val resultUri = UCrop.getOutput(data)
            resultUri?.let {
                if (captureMode == "front") frontImageUri = it.toString()
                else if (captureMode == "back") backImageUri = it.toString()
            }
        }
    }

    // Launcher para a câmera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempPhotoUri != null) {
            val compressedFile = compressImage(context, tempPhotoUri!!)
            if (compressedFile != null) {
                val destinationUri = Uri.fromFile(
                    File(context.cacheDir, "cropped_${System.currentTimeMillis()}.jpg")
                )
                val uCrop = UCrop.of(Uri.fromFile(compressedFile), destinationUri)
                    .withAspectRatio(5f, 3f)
                    .withMaxResultSize(1000, 600)
                cropLauncher.launch(uCrop.getIntent(context))
            } else {
                Toast.makeText(context, "${Lang.get(10, context)}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // Função interna para lançar a câmera (assume que a permissão já foi concedida)
    fun launchCameraInternal(mode: String, context: Context, cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>) { // Unresolved reference 'ManagedActivityResultLauncher'.
        val photoFile = File.createTempFile("temp_photo_", ".jpg", context.cacheDir)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
        tempPhotoUri = uri
        captureMode = mode
        cameraLauncher.launch(uri) // Argument type mismatch: actual type is 'android.net.Uri!', but 'kotlin.coroutines.CoroutineContext' was expected. | No value passed for parameter 'block'.
    }

    // Launcher para solicitar permissão da câmera
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            // Se a permissão for concedida, relança a função de captura
            launchCameraInternal(captureMode, context, cameraLauncher) // Unresolved reference 'launchCameraInternal'.
        } else {
            Toast.makeText(context, "Permissão para usar a câmera negada", Toast.LENGTH_SHORT).show()
        }
    }

    // Função de lançamento da câmera que verifica permissão
    fun launchCamera(mode: String) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCameraInternal(mode, context, cameraLauncher)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Modal com fundo preto transparente para toda a tela;
    // ao tocar fora do conteúdo, o teclado é fechado e o modal é descartado.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable {
                focusManager.clearFocus()
                onDismiss()
            }
            .windowInsetsPadding(WindowInsets.ime)
            .consumeWindowInsets(WindowInsets.ime),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(350.dp)
                .background(lightBackColor, RoundedCornerShape(16.dp))
                .clickable(enabled = false, onClick = {}) // Impede cliques internos de fechar o modal
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo de nome do cartão com fundo "surface" e texto "onSurface"
                OutlinedTextField(
                    value = cardName,
                    onValueChange = { cardName = it },
                    label = { Text("${Lang.get(31, context)}", color = onSurface) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = onSurface, fontSize = 16.sp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Preview das imagens com margem para não colar no campo de texto.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    frontImageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Preview da Frente",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Gray.copy(alpha = 0.1f)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    backImageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Preview do Verso",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Gray.copy(alpha = 0.1f)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Botões de captura de fotos, lado a lado.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { launchCamera("front") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = if (frontImageUri == null) "${Lang.get(32, context)}" else "${Lang.get(33, context)}",
                            color = Color.White
                        )
                    }
                    Button(
                        onClick = { launchCamera("back") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = if (backImageUri == null) "${Lang.get(34, context)}" else "${Lang.get(35, context)}",
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botão para salvar o cartão
                Button(
                    onClick = {
                        if (cardName.trim().isNotEmpty() && frontImageUri != null && backImageUri != null) {
                            onSave(
                                Card(
                                    id = cardToEdit?.id ?: UUID.randomUUID().toString(),
                                    name = cardName.trim(),
                                    frontImage = frontImageUri!!,
                                    backImage = backImageUri!!
                                )
                            )
                            onDismiss()
                        } else {
                            Toast.makeText(context, "${Lang.get(36, context)}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("${Lang.get(37, context)}", color = Color.White)
                }
            }
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OverviewPage(
    settings: VistaSettings,
    cards: List<Card>,
    transactions: List<Transaction>,
    onNavigateToTransactions: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onAddCard: (Card) -> Unit,
    onRemoveCard: (Card) -> Unit,
    onEditCard: (Card) -> Unit,
    onAddTransaction: (Transaction) -> Unit
) {
    val backColor = MaterialTheme.colorScheme.background
    val onBackColor = MaterialTheme.colorScheme.onBackground
    val lightBackColor = LocalExtraColors.current.lightBackground
    val currentDate = LocalDate.now()
    val currentMonth = currentDate.monthValue
    val currentYear = currentDate.year
    val formatter = DateTimeFormatter.ISO_DATE_TIME

    var showCardScreen by remember { mutableStateOf(false) }
    var editingCard by remember { mutableStateOf<Card?>(null) }
    var selectedCardId by remember { mutableStateOf<String?>(null) }
    var fullScreenImage by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    // Carrega o último card selecionado
    LaunchedEffect(Unit) {
        selectedCardId = getLastCardId(context)
        saveLastPage(context, "Home")
    }

    // Calcula o índice inicial no pager (posição 0 = botão +)
    val selectedIndex = selectedCardId?.let { id ->
        cards.indexOfFirst { it.id == id }.takeIf { it >= 0 }?.plus(1)
    } ?: 0

    // Usa Accompanist Pager
    val pagerState = rememberPagerState(
        initialPage = selectedIndex,
        pageCount = { cards.size + 1 }
    )

    // Atualiza a seleção do card ao deslizar
    LaunchedEffect(pagerState.currentPage) {
        val page = pagerState.currentPage
        if (page == 0) {
            selectedCardId = null
            saveLastCardId(context, null)
        } else {
            val card = cards.getOrNull(page - 1)
            selectedCardId = card?.id
            saveLastCardId(context, card?.id)
        }
    }

    // Calcule a fatura do mês atual considerando somente parcelas que caem no mês atual
    val currentInvoice = transactions.sumOf { tx ->
        val divider = tx.divider.toIntOrNull() ?: 1
        val totalValue = tx.value.toDoubleOrNull() ?: 0.0
        val installmentValue = if (divider > 0) totalValue / divider else totalValue

        try {
            val startDate = LocalDateTime.parse(tx.date, formatter)
            val currentDate = LocalDate.now()

            if (divider == 0) {
                val start = startDate.toLocalDate()
                val monthsBetween = ChronoUnit.MONTHS.between(
                    YearMonth.from(start),
                    YearMonth.from(currentDate)
                ).toInt() + 1 // inclui o mês atual

                installmentValue * monthsBetween
            } else {
                var sum = 0.0
                for (i in 0 until divider) {
                    val installmentDate = startDate.plusMonths(i.toLong()).toLocalDate()
                    if (!installmentDate.isAfter(currentDate)) {
                        sum += installmentValue
                    }
                }
                sum
            }
        } catch (e: Exception) {
            0.0
        }
    }

// Somar todas as transações com divider 0 ao total das faturas
    val allInvoices = transactions.sumOf { tx ->
        val divider = tx.divider.toIntOrNull() ?: 1
        val totalValue = tx.value.toDoubleOrNull() ?: 0.0
        val installmentValue = if (divider > 0) totalValue / divider else totalValue

        try {
            val startDate = LocalDateTime.parse(tx.date, formatter)

            if (divider == 0) {
                val current = LocalDate.now()
                val start = startDate.toLocalDate()

                val monthsBetween = ChronoUnit.MONTHS.between(
                    YearMonth.from(start),
                    YearMonth.from(current)
                ).toInt() + 1 // inclui o mês atual

                installmentValue * monthsBetween
            } else {
                totalValue
            }
        } catch (e: Exception) {
            0.0
        }
    }

    val currencyFormatter = NumberFormat.getCurrencyInstance()

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val fullDateFormatter = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy", Locale("pt", "BR"))

    val currentTime = LocalTime.now().format(timeFormatter)
    val currentFullDate = LocalDate.now().format(fullDateFormatter).replaceFirstChar { it.uppercase() }

    val today = LocalDate.now()
    val last5Months = (0..4).map { i ->
        val date = today.minusMonths(i.toLong())
        YearMonth.from(date)
    }.reversed()

    val monthlySums = last5Months.map { ym ->
        transactions.sumOf { tx ->
            try {
                val date = LocalDateTime.parse(tx.date, formatter)
                val value = tx.value.toDoubleOrNull() ?: 0.0
                val divider = tx.divider.toIntOrNull() ?: 1
                val perInstallment = if (divider > 0) value / divider else value

                var sum = 0.0
                if (divider == 0) {
                    if (!YearMonth.from(date.toLocalDate()).isAfter(ym)) {
                        sum += perInstallment
                    }
                } else {
                    for (i in 0 until divider) {
                        val installmentDate = date.plusMonths(i.toLong()).toLocalDate()
                        if (YearMonth.from(installmentDate) == ym) {
                            sum += perInstallment
                        }
                    }
                }
                sum
            } catch (e: Exception) {
                0.0
            }
        }.toInt()
    }

    val maxValue = monthlySums.maxOrNull() ?: 0
    val labels = last5Months.map { it.month.name.take(3).capitalize() }
    val years = last5Months.map { it.year.toString() }


    ClickableBackgroundColumn(
        focusManager = focusManager,
        backColor = backColor
    ) {
        Header(
            title = context.getString(R.string.app_name),
            onSettingsClick = onNavigateToSettings,
            headerBackgroundColor = lightBackColor
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            /*
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(lightBackColor, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Bem-vindo ${settings.name}, agora são $currentTime,\nhoje é $currentFullDate.",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
*/
            // Box de faturas
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(lightBackColor, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "${Lang.get(38, context)}",
                            style = MaterialTheme.typography.titleMedium.copy(color = onBackColor)
                        )
                        Text(
                            currencyFormatter.format(allInvoices),
                            style = MaterialTheme.typography.headlineSmall.copy(color = onBackColor)
                        )

                        Text(
                            "${Lang.get(39, context)}",
                            style = MaterialTheme.typography.titleMedium.copy(color = onBackColor)
                        )
                        Text(
                            currencyFormatter.format(currentInvoice),
                            style = MaterialTheme.typography.headlineSmall.copy(color = onBackColor)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        if (currentInvoice < 0) {
                            Button(
                                onClick = {
                                    val newTransaction = Transaction(
                                        name = "${Lang.get(40, context)}",
                                        date = LocalDateTime.now().format(formatter),
                                        value = abs(currentInvoice).toString(), // valor positivo
                                        divider = "1",
                                        executed = false
                                    )
                                    onAddTransaction(newTransaction)
                                    onNavigateToTransactions()
                                },
                                modifier = Modifier.align(Alignment.Start),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text(
                                    "${Lang.get(41, context)}",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
/*
            // Box de gráfico
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(lightBackColor, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    ChartBox(
                        title = "Gráfico Mensal",
                        values = monthlySums,
                        maxValue = maxValue,
                        labels = labels,
                        secondaryLabels = years,
                        barColor = MaterialTheme.colorScheme.primary,
                        backgroundColor = lightBackColor,
                        foreColor = onBackColor
                    )
                }
            }
*/
            // Exibição dos cartões com HorizontalPager
            item {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) { index ->
                    if (index == 0) {
                        // Card para adicionar novo cartão
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                                .background(lightBackColor)
                                .clickable {
                                    editingCard = null
                                    showCardScreen = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "+",
                                style = MaterialTheme.typography.displayMedium,
                                color = onBackColor
                            )
                        }
                    } else {
                        val card = cards.getOrNull(index - 1) // Evita o erro de índice
                        card?.let {
                            CardRowItem(
                                card = it,
                                isSelected = selectedCardId == it.id,
                                onCardClick = { imagePath ->
                                    fullScreenImage = imagePath
                                },
                                onEdit = {
                                    editingCard = it
                                    showCardScreen = true
                                },
                                onRemove = {
                                    if (selectedCardId == it.id) {
                                        selectedCardId = null
                                        saveLastCardId(context, null)
                                    }
                                    onRemoveCard(it)
                                }
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        Footer(
            title = "${Lang.get(42, context)}",
            onClick = onNavigateToTransactions,
            footerBackgroundColor = LocalExtraColors.current.lightBackground
        )
    }


    if (showCardScreen) {
        CardScreen(
            existingCards = cards,
            cardToEdit = editingCard,
            onDismiss = {
                showCardScreen = false
                editingCard = null
            },
            onSave = { newCard ->
                if (editingCard != null) {
                    onEditCard(newCard)
                } else {
                    onAddCard(newCard)
                }
                showCardScreen = false
                editingCard = null
            }
        )
    }

    fullScreenImage?.let { image ->
        Dialog(
            onDismissRequest = { fullScreenImage = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable { fullScreenImage = null },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(5f / 3f)
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationZ = 90f
                            transformOrigin = TransformOrigin.Center
                        }
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@Composable
fun ClickableBackgroundColumn(
    focusManager: FocusManager,
    backColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backColor)
            .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } }
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars),
        content = content
    )
}
@Composable
fun BarChart(value: Int, maxValue: Int, barColor: Color) {
    val foreColor = MaterialTheme.colorScheme.onBackground
    val scaledHeight = if (maxValue > 0) (value.toFloat() / maxValue.toFloat()) * 130 else 0f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.height(130.dp)
    ) {
        Text(
            text = value.toString(),
            color = foreColor,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .width(30.dp)
                .height(scaledHeight.dp)
                .background(barColor)
        )
    }
}
@Composable
fun ChartBox(
    title: String,
    values: List<Int>,
    maxValue: Int,
    labels: List<String>,
    secondaryLabels: List<String>,
    barColor: Color,
    backgroundColor: Color,
    foreColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text(text = title, fontSize = 16.sp, color = foreColor, modifier = Modifier.padding(bottom = 8.dp))

            Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                values.forEach { value -> BarChart(value = value, maxValue = maxValue, barColor = barColor) }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                labels.forEach { label -> Text(text = label, fontSize = 12.sp, color = foreColor) }
            }

            if (secondaryLabels.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                    secondaryLabels.forEach { label -> Text(text = label, fontSize = 12.sp, color = foreColor) }
                }
            }
        }
    }
}

@Composable
fun CardRowItem(
    card: Card,
    isSelected: Boolean,
    onCardClick: (String) -> Unit,
    onEdit: () -> Unit,
    onRemove: () -> Unit
) {
    var isFront by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < -10) isFront = false
                    else if (dragAmount > 10) isFront = true
                }
            }
            .clickable {
                val currentImage = if (isFront) card.frontImage else card.backImage
                if (currentImage.isNotBlank()) {
                    onCardClick(currentImage)
                }
            }
    ) {
        AsyncImage(
            model = if (isFront) card.frontImage else card.backImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(onClick = { onEdit() }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.White)
            }
            IconButton(onClick = { onRemove() }) {
                Icon(Icons.Default.Delete, contentDescription = "Remover", tint = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    transactionToEdit: Transaction? = null,
    onDismiss: () -> Unit,
    onSave: (Transaction) -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Cores do tema
    val lightBackColor = LocalExtraColors.current.lightBackground
    val foreColor = MaterialTheme.colorScheme.onBackground
    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error

    // Estados iniciais
    var isPayMode by remember {
        mutableStateOf(transactionToEdit?.value?.toDoubleOrNull()?.let { it < 0 } ?: true)
    }
    var name by remember { mutableStateOf(transactionToEdit?.name ?: "") }
    var date by remember {
        mutableStateOf(
            transactionToEdit?.date ?: LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        )
    }
    var valueInput by remember {
        mutableStateOf(
            transactionToEdit?.value?.toBigDecimalOrNull()?.abs()?.toPlainString() ?: ""
        )
    }

    var dividerInput by remember {
        mutableIntStateOf(
            transactionToEdit?.divider?.toIntOrNull() ?: 0
        )
    }
    val dividerRange = 0f..48f

    // Estado para exibir o DatePicker
    var showDatePicker by remember { mutableStateOf(false) }

    // Exibe um DatePickerDialog com tema customizado
    if (showDatePicker) {
        val calendar = Calendar.getInstance().apply {
            if (transactionToEdit != null) {
                try {
                    val dt = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
                    set(dt.year, dt.monthValue - 1, dt.dayOfMonth)
                } catch (e: Exception) {
                    // fallback para data atual
                }
            }
        }
        // Cria o DatePickerDialog usando o tema customizado
        DatePickerDialog(
            context,
            R.style.CustomDatePickerDialogTheme,  // Tema customizado definido no styles.xml
            { _, year, month, dayOfMonth ->
                val pickedDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0)
                date = pickedDate.format(DateTimeFormatter.ISO_DATE_TIME)
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            // Ao cancelar, garante que o flag seja resetado.
            setOnCancelListener { showDatePicker = false }
            show()
        }
    }

    // Switch customizado para Pagar/Receber com botões coloridos
    @Composable
    fun PayReceiveSwitch(
        isPay: Boolean,
        onToggle: (Boolean) -> Unit
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isPay) Color(0xFFEC7480) else MaterialTheme.colorScheme.surface)
                    .clickable { onToggle(true) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${Lang.get(43, context)}",
                    color = if (isPay) LocalExtraColors.current.revForeColor else foreColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (!isPay) Color(0xFF6FDE98) else MaterialTheme.colorScheme.surface)
                    .clickable { onToggle(false) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${Lang.get(44, context)}",
                    color = if (!isPay) LocalExtraColors.current.revForeColor else foreColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { focusManager.clearFocus(); onDismiss() }
            .windowInsetsPadding(WindowInsets.ime)
            .consumeWindowInsets(WindowInsets.ime),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(350.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(lightBackColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PayReceiveSwitch(
                    isPay = isPayMode,
                    onToggle = { isPayMode = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { if (it.length <= 92) name = it }, // Limita a 92 caracteres
                    label = { Text(Lang.get(25, context), color = foreColor) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = date,
                    onValueChange = {},
                    label = { Text(Lang.get(45, context), color = foreColor) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    singleLine = true,
                    enabled = false
                )

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = valueInput,
                    onValueChange = { input ->
                        // Limita a entrada para 12 caracteres
                        val filtered = input
                            .filterIndexed { i, c -> c.isDigit() || (c == '.' && input.indexOf('.') == i) }
                            .take(12) // Limita a 12 caracteres
                        valueInput = filtered
                    },
                    label = { Text(Lang.get(46, context), color = foreColor) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )


                Spacer(modifier = Modifier.height(16.dp))

                Text("${Lang.get(47, context)} ${dividerInput}x", color = foreColor, fontSize = 16.sp)
                Slider(
                    value = dividerInput.toFloat(),
                    onValueChange = { dividerInput = it.toInt() },
                    valueRange = 0f..48f,
                    steps = 47,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (name.isNotBlank() && valueInput.isNotBlank()) {
                            val inputValue = valueInput.toDoubleOrNull() ?: 0.0
                            val finalValue = if (isPayMode) -kotlin.math.abs(inputValue) else kotlin.math.abs(inputValue)
                            val transaction = Transaction(
                                id = transactionToEdit?.id ?: UUID.randomUUID().toString(),
                                name = name,
                                date = date,
                                value = finalValue.toString(),
                                divider = dividerInput.toString(),
                                executed = transactionToEdit?.executed ?: false
                            )
                            onSave(transaction)
                            onDismiss()
                        } else {
                            Toast.makeText(context, Lang.get(48, context), Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = if (transactionToEdit != null) Lang.get(8, context) else Lang.get(21, context),
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }


}

@Composable
fun TransactionPage(
    transactions: List<Transaction>,
    onAddTransaction: (Transaction) -> Unit,
    onEditTransaction: (Transaction) -> Unit,
    onDeleteTransaction: (Transaction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPremiumVersion = true
    val mensalTransactionsLimit = 3

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = 100.dp
    val itemWidthPx = with(density) { itemWidth.toPx() }
    val screenWidthPx = with(density) { screenWidth.toPx() }

    var showTransactionDialog by remember { mutableStateOf(false) }
    var editingTransaction by remember { mutableStateOf<Transaction?>(null) }
    var expandedTransactionId by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val monthFormatter = DateTimeFormatter.ofPattern("MMM yy", Locale.ENGLISH)
    val currentMonthLabel = LocalDate.now().withDayOfMonth(1).format(monthFormatter)

    LaunchedEffect(Unit) {
        saveLastPage(context, "Transactions")
    }

    fun expandTransactionIntoInstallments(transaction: Transaction): List<Transaction> {
        val dateTime = try {
            LocalDateTime.parse(transaction.date, DateTimeFormatter.ISO_DATE_TIME)
        } catch (e: Exception) {
            return listOf(transaction)
        }

        if (transaction.divider == "0") {
            val monthsSinceCreation =
                ChronoUnit.MONTHS.between(dateTime.toLocalDate(), LocalDate.now())
            return (0..monthsSinceCreation.toInt()).map { i ->
                val renewedDate = dateTime.plusMonths(i.toLong())
                transaction.copy(
                    date = renewedDate.format(DateTimeFormatter.ISO_DATE_TIME),
                    name = transaction.name
                )
            }
        }

        val installments = transaction.divider.toIntOrNull() ?: 1
        if (installments <= 1) return listOf(transaction)

        val installmentValue = (transaction.value.toDoubleOrNull() ?: 0.0) / installments

        return List(installments) { i ->
            val installmentDate = dateTime.plusMonths(i.toLong())
            transaction.copy(
                date = installmentDate.format(DateTimeFormatter.ISO_DATE_TIME),
                value = installmentValue.toString(),
                name = "${transaction.name} (${i + 1}/$installments)"
            )
        }
    }

    val expandedTransactions = transactions.flatMap { expandTransactionIntoInstallments(it) }

    val extractedMonthDates = expandedTransactions.mapNotNull {
        try {
            LocalDateTime.parse(it.date, DateTimeFormatter.ISO_DATE_TIME).toLocalDate()
                .withDayOfMonth(1)
        } catch (e: Exception) {
            null
        }
    }.toMutableSet().apply {
        add(LocalDate.now().withDayOfMonth(1))
    }.toList().distinct().sorted()

    val months = extractedMonthDates.map { it.format(monthFormatter) }
    var selectedMonth by remember { mutableStateOf(currentMonthLabel) }

    val selectedMonthDate = try {
        LocalDate.parse(
            "01 $selectedMonth",
            DateTimeFormatter.ofPattern("dd MMM yy", Locale.ENGLISH)
        )
    } catch (e: Exception) {
        LocalDate.now().withDayOfMonth(1)
    }

    LaunchedEffect(months) {
        val initialIndex = months.indexOf(selectedMonth)
        if (initialIndex != -1) {
            val offset = (screenWidthPx / 2 - itemWidthPx / 2).toInt()
            listState.scrollToItem(initialIndex, -offset)
        }
    }

    val filteredTransactions = expandedTransactions.filter {
        try {
            val date = LocalDateTime.parse(it.date, DateTimeFormatter.ISO_DATE_TIME).toLocalDate()
            date.month == selectedMonthDate.month && date.year == selectedMonthDate.year
        } catch (e: Exception) {
            false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) { detectTapGestures { /* fecha teclado, se quiser */ } }
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                title = "${Lang.get(42, context)}",
                onSettingsClick = onNavigateToSettings,
                headerBackgroundColor = LocalExtraColors.current.lightBackground
            )

            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                itemsIndexed(months) { index, month ->
                    val isSelected = month == selectedMonth
                    val backgroundColor =
                        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.52f) else Color.Transparent
                    val textColor =
                        if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground
                    Box(
                        modifier = Modifier
                            .clickable {
                                selectedMonth = month
                                coroutineScope.launch {
                                    val offset = (screenWidthPx / 2 - itemWidthPx / 2).toInt()
                                    listState.animateScrollToItem(index, -offset)
                                }
                            }
                            .background(backgroundColor, shape = MaterialTheme.shapes.medium)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = month,
                            color = textColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .pointerInput(selectedMonth) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            val currentIndex = months.indexOf(selectedMonth)
                            val newIndex = when {
                                dragAmount > 25 && currentIndex > 0 -> currentIndex - 1
                                dragAmount < -25 && currentIndex < months.lastIndex -> currentIndex + 1
                                else -> currentIndex
                            }
                            if (newIndex != currentIndex) {
                                selectedMonth = months[newIndex]
                                coroutineScope.launch {
                                    val offset = (screenWidthPx / 2 - itemWidthPx / 2).toInt()
                                    listState.animateScrollToItem(newIndex, -offset)
                                }
                            }
                        }
                    }
                    .padding(horizontal = 16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(filteredTransactions) { _, transaction ->
                        TransactionRow(
                            transaction = transaction,
                            expandedTransactionKey = expandedTransactionId,
                            onExpand = { key ->
                                expandedTransactionId =
                                    if (expandedTransactionId == key) null else key
                            },
                            onEdit = {
                                editingTransaction = transactions.find { it.id == transaction.id }
                            },
                            onDelete = {
                                onDeleteTransaction(transactions.find { it.id == transaction.id }
                                    ?: transaction)
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(146.dp)) } // espaço para FAB
                }
            }

            Footer(
                title = Lang.get(18, context),
                onClick = onNavigateBack,
                footerBackgroundColor = LocalExtraColors.current.lightBackground
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp)
                .padding(32.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            val context = LocalContext.current

            FloatingActionButton(

                onClick = {
                    val now = LocalDate.now()
                    val currentMonthTransactions = transactions.count {
                        try {
                            val date = LocalDateTime.parse(it.date, DateTimeFormatter.ISO_DATE_TIME).toLocalDate()
                            date.month == now.month && date.year == now.year
                        } catch (e: Exception) {
                            false
                        }
                    }


                    if (!isPremiumVersion && currentMonthTransactions >= mensalTransactionsLimit) {
                        Toast.makeText(
                            context,
                            "${Lang.get(51, context)} (${currentMonthTransactions}/${mensalTransactionsLimit})\n${Lang.get(30, context)}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        editingTransaction = null
                        showTransactionDialog = true
                    }
                },
                modifier = Modifier.size(80.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 30.sp),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

    }

    if (showTransactionDialog || editingTransaction != null) {
        TransactionScreen(
            transactionToEdit = editingTransaction,
            onDismiss = {
                showTransactionDialog = false
                editingTransaction = null
            },
            onSave = { newTransaction ->
                if (editingTransaction != null) {
                    onEditTransaction(newTransaction)
                } else {
                    onAddTransaction(newTransaction)
                }
                showTransactionDialog = false
                editingTransaction = null
            }
        )
    }
}

@Composable
fun TransactionRow(
    transaction: Transaction,
    expandedTransactionKey: String?,
    onExpand: (String) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val valueAmount = transaction.value.toDoubleOrNull() ?: 0.0
    val borderColor = when {
        valueAmount > 0 -> Color(0xFF6FDE98) // Light green
        valueAmount < 0 -> Color(0xFFEC7480) // Light red
        else -> Color.Transparent
    }

    val parsedDate = try {
        LocalDateTime.parse(transaction.date, DateTimeFormatter.ISO_DATE_TIME)
    } catch (e: Exception) {
        null
    }
    val formattedDate =
        parsedDate?.format(DateTimeFormatter.ofPattern("dd MMM yy")) ?: transaction.date
    val currencyFormatter = NumberFormat.getCurrencyInstance()

    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onExpand(transaction.id) }
            .border(2.dp, borderColor, shape = MaterialTheme.shapes.medium)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Primeira linha com data e valor
            // Primeira linha com data e valor
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp)
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Coluna para data
                Column(modifier = Modifier.weight(0.6f)) {
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                // Coluna para valor
                val value = transaction.value.replace("-", "").toDoubleOrNull() ?: 0.0
                Column(modifier = Modifier.weight(1.4f), horizontalAlignment = Alignment.End) {
                    Text(
                        text = currencyFormatter.format(value),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Linha abaixo com o nome da transação, ocupando as duas colunas
            if (transaction.name.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = transaction.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3, // Para garantir a quebra de linha no nome
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Detalhes expandidos
            if (expandedTransactionKey == transaction.id) {
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onEdit,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "${Lang.get(22, context)}",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp
                        )
                    }
                    VerticalDivider()
                    TextButton(
                        onClick = onDelete,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "${Lang.get(24, context)}",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun VerticalDivider(
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
    thickness: Dp = 1.dp,
    height: Dp = 32.dp
) {
    Box(
        modifier = Modifier
            .width(thickness)
            .height(height)
            .background(color = color)
    )
}