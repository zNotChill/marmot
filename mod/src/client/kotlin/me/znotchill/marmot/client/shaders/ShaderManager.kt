package me.znotchill.marmot.client.shaders

import com.mojang.blaze3d.buffers.GpuBuffer
import com.mojang.blaze3d.buffers.GpuBufferSlice
import com.mojang.blaze3d.buffers.Std140Builder
import com.mojang.blaze3d.buffers.Std140SizeCalculator
import com.mojang.blaze3d.systems.RenderSystem
import org.joml.Vector2fc
import org.joml.Vector3fc
import org.joml.Vector4fc
import org.lwjgl.system.MemoryStack
import java.util.function.Supplier

object ShaderManager {

    private val buffers = mutableMapOf<String, GpuBuffer>()
    private val slices = mutableMapOf<String, GpuBufferSlice>()

    val vec2s = HashMap<String, Vector2fc>()
    val vec3s = HashMap<String, Vector3fc>()
    val vec4s = HashMap<String, Vector4fc>()
    val floats = HashMap<String, Float>()

    fun createBuffers() {
        destroy()

        vec2s.forEach { (name, value) -> makeVec2Buffer(name, value) }
        vec3s.forEach { (name, value) -> makeVec3Buffer(name, value) }
        vec4s.forEach { (name, value) -> makeVec4Buffer(name, value) }
        floats.forEach { (name, value) -> makeFloatBuffer(name, value) }
    }

    private fun makeVec2Buffer(name: String, value: Vector2fc) {
        // size
        val sizeCalc = Std140SizeCalculator()
        sizeCalc.putVec2()
        val size = sizeCalc.get()

        // allocate & upload
        val buffer = RenderSystem.getDevice().createBuffer(
            Supplier { "UniformBuffer:$name" },
            GpuBuffer.USAGE_UNIFORM or GpuBuffer.USAGE_COPY_DST,
            size
        )

        MemoryStack.stackPush().use { stack ->
            val builder = Std140Builder.onStack(stack, size)
            builder.putVec2(value)
            val byteBuffer = builder.get()
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(buffer.slice(), byteBuffer)
        }

        buffers[name] = buffer
        slices[name] = buffer.slice()
    }

    private fun makeVec3Buffer(name: String, value: Vector3fc) {
        val sizeCalc = Std140SizeCalculator()
        sizeCalc.putVec3()
        val size = sizeCalc.get()

        val buffer = RenderSystem.getDevice().createBuffer(
            Supplier { "UniformBuffer:$name" },
            GpuBuffer.USAGE_UNIFORM or GpuBuffer.USAGE_COPY_DST,
            size
        )

        MemoryStack.stackPush().use { stack ->
            val builder = Std140Builder.onStack(stack, size)
            builder.putVec3(value)
            val byteBuffer = builder.get()
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(buffer.slice(), byteBuffer)
        }

        buffers[name] = buffer
        slices[name] = buffer.slice()
    }

    private fun makeVec4Buffer(name: String, value: Vector4fc) {
        val sizeCalc = Std140SizeCalculator()
        sizeCalc.putVec4()
        val size = sizeCalc.get()

        val buffer = RenderSystem.getDevice().createBuffer(
            Supplier { "UniformBuffer:$name" },
            GpuBuffer.USAGE_UNIFORM or GpuBuffer.USAGE_COPY_DST,
            size
        )

        MemoryStack.stackPush().use { stack ->
            val builder = Std140Builder.onStack(stack, size)
            builder.putVec4(value)
            val byteBuffer = builder.get()
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(buffer.slice(), byteBuffer)
        }

        buffers[name] = buffer
        slices[name] = buffer.slice()
    }

    private fun makeFloatBuffer(name: String, value: Float) {
        val sizeCalc = Std140SizeCalculator()
        sizeCalc.putFloat()
        val size = sizeCalc.get()

        val buffer = RenderSystem.getDevice().createBuffer(
            Supplier { "UniformBuffer:$name" },
            GpuBuffer.USAGE_UNIFORM or GpuBuffer.USAGE_COPY_DST,
            size
        )

        MemoryStack.stackPush().use { stack ->
            val builder = Std140Builder.onStack(stack, size)
            builder.putFloat(value)
            val byteBuffer = builder.get()
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(buffer.slice(), byteBuffer)
        }

        buffers[name] = buffer
        slices[name] = buffer.slice()
    }

    fun getSlice(name: String): GpuBufferSlice? = slices[name]

    fun getAllSlices(): Map<String, GpuBufferSlice> = slices.toMap()

    fun getAllSlicesList(): List<Pair<String, GpuBufferSlice>> = slices.entries.map { it.key to it.value }

    fun destroy() {
        buffers.values.forEach { it.close() }
        buffers.clear()
        slices.clear()
    }
}
