package non.shahad.stayhomegallery.utils.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T> onMain(crossinline block : CoroutineScope.() -> T): T{
    return withContext(Dispatchers.Main){block.invoke(this@withContext)}
}

suspend inline fun <T, R> T.onMain(crossinline block: (T) -> R): R {
    return withContext(Dispatchers.Main) { this@onMain.let(block) }
}

suspend inline fun <T> onDefault(crossinline block: CoroutineScope.() -> T): T {
    return withContext(Dispatchers.Default) { block.invoke(this@withContext) }
}

suspend inline fun <T, R> T.onDefault(crossinline block: (T) -> R): R {
    return withContext(Dispatchers.Default) { this@onDefault.let(block) }
}

suspend inline fun <T> onIO(crossinline block: CoroutineScope.() -> T): T {
    return withContext(Dispatchers.IO) { block.invoke(this@withContext) }
}

suspend inline fun <T, R> T.onIO(crossinline block: (T) -> R): R {
    return withContext(Dispatchers.IO) { this@onIO.let(block) }
}