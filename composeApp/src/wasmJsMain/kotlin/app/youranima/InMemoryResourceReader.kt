package app.youranima

import kotlinx.coroutines.async
import kotlinx.coroutines.await
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceReader
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.toByteArray
import kotlin.js.Promise
import kotlin.js.toList

/**
 * Serves the Compose resource files in [files] from memory, without suspending, and everything else through
 * [delegate] (Compose's own web reader). That reader goes through the Cache Storage API on every read, one string at
 * a time, so a screen's texts would appear over a couple of seconds; with the files here, `stringResource`,
 * `painterResource` and `Font` resolve within the first composition. Fill [files] with [loadPreloadedResources].
 */
@OptIn(ExperimentalResourceApi::class, InternalResourceApi::class)
class InMemoryResourceReader(
    private val delegate: ResourceReader,
    private val files: Map<String, ByteArray>,
) : ResourceReader {
    override suspend fun read(path: String): ByteArray = files[path] ?: delegate.read(path)

    override suspend fun readPart(
        path: String,
        offset: Long,
        size: Long,
    ): ByteArray =
        files[path]?.copyOfRange(offset.toInt(), (offset + size).toInt())
            ?: delegate.readPart(path, offset, size)

    override fun getUri(path: String): String = delegate.getUri(path)
}

/**
 * Collects the first-frame composeResources files that index.html started downloading right after it was parsed
 * (`window.webPreloads`: path → promise of the bytes, generated in build.gradle.kts). Keys are the resource paths as
 * Compose knows them (`composeResources/<package>/<type>/<file>`). The dev server's index.html has no such script,
 * so this returns an empty map there. A file that failed to load is skipped: Compose then loads it the usual way.
 */
suspend fun loadPreloadedResources(): Map<String, ByteArray> =
    coroutineScope {
        preloadedPaths()
            .toList()
            .map { it.toString() }
            .map { path -> async { runCatching { path to preloadedBytes(path) }.getOrNull() } }
            .awaitAll()
            .filterNotNull()
            .toMap()
    }

private fun preloadedPaths(): JsArray<JsString> = js("Object.keys(window.webPreloads || {})")

private fun preloadedFile(path: String): Promise<ArrayBuffer> = js("window.webPreloads[path]")

private suspend fun preloadedBytes(path: String): ByteArray = Int8Array(preloadedFile(path).await<ArrayBuffer>()).toByteArray()
