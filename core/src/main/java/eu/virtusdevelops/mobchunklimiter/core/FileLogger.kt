package eu.virtusdevelops.mobchunklimiter.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.entity.Entity
import sun.rmi.server.Dispatcher
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.logging.Level
import java.util.logging.Logger

class FileLogger (logFileName: String) {

    private val logFile: File = File(logFileName)
    private val logger: Logger = Logger.getLogger("MobChunkLimiter")

    init {
        try {
            // Ensure the log file exists
            if (!logFile.exists()) {
                logFile.parentFile?.mkdirs() // Create directories if necessary
                logFile.createNewFile()
            }
        } catch (e: IOException) {
            logger.log(Level.SEVERE, "Failed to create the log file: ${logFile.absolutePath}", e)
        }
    }

    // Log formatted information to the log file
    fun logInfo(message: String) {
        logToFile("[INFO]", message)
    }

    fun logWarning(message: String) {
        logToFile("[WARNING]", message)
    }

    fun logError(message: String, throwable: Throwable? = null) {
        logToFile("[ERROR]", message)
        throwable?.let { logToFile("[ERROR]", it.stackTraceToString()) }
    }

    fun logMobKill(entity: Entity, time: Long) {
        val entityType = entity.type.toString() // Get entity type as a string
        val x = entity.location.blockX
        val y = entity.location.blockY
        val z = entity.location.blockZ
        val world = entity.location.world.name

        logInfo("Mob killed: Type=$entityType, Location=($x, $y, $z, $world)")
    }


    private fun logToFile(level: String, message: String) {
        Dispatchers.IO.run {
            val date = Date()
            val timestampday = SimpleDateFormat("yyyy-MM-dd").format(date)
            val timestamptime = SimpleDateFormat("HH:mm:ss").format(date)
            val formattedMessage = "[$timestampday] [$timestamptime] $level $message\n"

            // Write the log message to the file
            try {
                FileWriter(logFile, true).use { writer ->
                    writer.write(formattedMessage)
                }
            } catch (e: IOException) {
                logger.log(Level.SEVERE, "Failed to write to the log file: ${logFile.absolutePath}", e)
            }
        }
    }

}