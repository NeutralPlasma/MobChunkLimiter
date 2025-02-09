package eu.virtusdevelops.mobchunklimiter.api

import org.bukkit.Chunk
import org.bukkit.entity.EntityType

interface ChunkManager {


    fun initConfiguration()

    fun scanChunk(chunk: Chunk)

    fun setLimit(entity: EntityType, limit: Int)

    fun getLimit(entity: EntityType) : Int

    fun setDefaultLimit(limit: Int)

    fun getDefaultLimit() : Int

    fun canSpawnMoreEntities(chunk: Chunk, entity: EntityType) : Boolean

    fun isInited() : Boolean

}