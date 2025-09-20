package me.znotchill.marmot.minestom

import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.InstanceManager
import net.minestom.server.instance.LightingChunk
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit
import net.minestom.server.utils.chunk.ChunkSupplier

class BaseInstance {
    fun createInstance(instanceManager: InstanceManager): InstanceContainer {
        val instanceContainer = instanceManager.createInstanceContainer()

        instanceContainer.setGenerator { unit: GenerationUnit ->
            val start = unit.absoluteStart()
            val sizeX = unit.size().x().toInt()
            val sizeZ = unit.size().z().toInt()

            for (x in 0 until sizeX) {
                for (z in 0 until sizeZ) {
                    val pos = start.add(x.toDouble(), 0.0, z.toDouble())
                    unit.modifier().setBlock(pos, Block.BEDROCK)
                }
            }
        }

        instanceContainer.chunkSupplier = ChunkSupplier { i, x, z -> LightingChunk(i, x, z) }

        return instanceContainer
    }
}