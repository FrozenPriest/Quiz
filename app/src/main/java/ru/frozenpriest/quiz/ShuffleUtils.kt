package ru.frozenpriest.quiz

import java.util.*
import java.util.concurrent.ThreadLocalRandom

object ShuffleUtils {
    fun <E> pickNRandomElements(list: List<E>, n: Int, r: Random): List<E> {
        val length = list.size
        if (length < n)
            throw IllegalArgumentException("List size must be greater than amount of elements to take")

        for (i in length - 1 downTo length - n) {
            Collections.swap(list, i, r.nextInt(i + 1))
        }
        return list.subList(length - n, length)
    }

    fun <E> pickNRandomElements(list: List<E>, n: Int): List<E> {
        return pickNRandomElements(list, n, ThreadLocalRandom.current())
    }
}