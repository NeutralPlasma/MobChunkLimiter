package eu.virtusdevelops.mobchunklimiter.api

import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus

interface MobChunkLimiterAPI {

    companion object {

        private var implementation: MobChunkLimiterAPI? = null

        private var enabled = false

        /**
         * Retrieves the version of the API.
         *
         * @return The version as an integer.
         */
        @JvmStatic
        fun getVersion(): Int = 1

        /**
         * Provides an instance of the MobChunkLimiterAPI if the API is enabled.
         *
         * @return The instance of MobChunkLimiterAPI.
         * @throws NullPointerException if the API is not enabled.
         * @since 1
         */
        @JvmStatic
        fun get(): MobChunkLimiterAPI {
            if(enabled) return implementation!!
            throw NullPointerException("MobChunkLimiterAPI is not enabled!")
        }

        /**
         * Checks whether the MobChunkLimiterAPI is enabled.
         *
         * @return true if the API is enabled, false otherwise.
         * @since 1
         */
        @JvmStatic
        fun isEnabled(): Boolean = enabled


        /**
         * This is an internal method. Do not use it.
         *
         * Loads the specified MobChunkLimiterAPI instance to enable the API.
         *
         * @param mobChunkLimiterAPI The instance of MobChunkLimiterAPI to be loaded.
         * @throws IllegalStateException If the API is already enabled.
         */
        @ApiStatus.Internal
        fun load(mobChunkLimiterAPI: MobChunkLimiterAPI) {
            if(enabled) throw IllegalStateException("MobChunkLimiterAPI is already enabled!")
            implementation = mobChunkLimiterAPI
            enabled = true
        }

    }


    fun getChunkManager(): ChunkManager

    fun getPlugin(): JavaPlugin

}