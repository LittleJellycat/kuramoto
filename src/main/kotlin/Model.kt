import kotlin.math.sin
import kotlin.random.Random
import koma.*
import koma.extensions.*

fun main(args: Array<String>) {
    val n = 30
    val time = 30
    val noiseLevels = (0..n).map { 0.0 }
//    val noiseLevels = (0..n).map { Math.random() }
    val k = 6
    val oddValue = 3.0
    val model1 = calculateModel(n, k, time, noiseLevels, oddValue)
    figure(1)
    val f1 = model1.transpose()
    f1.forEach {
        plot(it.toDoubleArray())
    }
    xlabel("Time (s)")
    ylabel("Price")
    title("1 too high")

}

private fun calculateModel(n: Int, k: Int, time: Int, noiseLevels: List<Double>, oddValue: Double): List<List<Double>> {
    val model = (1..time).map { MutableList(size = n) { 0.0 } }.toMutableList()
    model[0] = model[0].map { Random.nextDouble(-1.0, 0.0) }.toMutableList()
    model[0][0] = oddValue
    for (i in 1 until time) {
        model[i] = model[i].mapIndexed { index, _ ->
            model[i - 1][index] + modelFunction(
                noiseLevels[index],
                k,
                n,
                model[i - 1],
                model[i - 1][index]
            )
        }.toMutableList()
    }
    return model
}

private fun modelFunction(noise: Double, k: Int, n: Int, thetas: List<Double>, theta: Double): Double =
    noise + k.toDouble() / n * thetas.sumByDouble { sin(it - theta) }

fun <E> List<List<E>>.transpose(): List<List<E>> {
    if (isEmpty()) return this

    val width = first().size
    if (any { it.size != width }) {
        throw IllegalArgumentException("All nested lists must have the same size, but sizes were ${map { it.size }}")
    }

    return (0 until width).map { col ->
        (0 until size).map { row -> this[row][col] }
    }
}