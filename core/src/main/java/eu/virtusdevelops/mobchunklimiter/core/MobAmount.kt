package eu.virtusdevelops.mobchunklimiter.core

import org.bukkit.entity.EntityType

data class MobAmount(
    val entityType: EntityType,
    var amount: Int,
    val maxAmount: Int
) : Comparable<MobAmount>{

    fun getRatio(): Double{
        return  amount.toDouble() / maxAmount
    }

    override fun compareTo(other: MobAmount): Int {
        return getRatio().compareTo(other.getRatio())
    }


}