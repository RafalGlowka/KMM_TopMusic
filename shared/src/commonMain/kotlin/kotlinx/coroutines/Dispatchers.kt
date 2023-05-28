package kotlinx.coroutines

@OptIn(ExperimentalCoroutinesApi::class)
val IODispatcher: CoroutineDispatcher = newFixedThreadPoolContext(nThreads = 200, name = "IO")
val Dispatchers.IO: CoroutineDispatcher get() = IODispatcher
