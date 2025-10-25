package me.znotchill.marmot.common.ui.classes

import kotlin.math.*

enum class Easing {
    LINEAR {
        override fun getValue(t: Double): Double =
            t
    },

    EASE_IN {
        override fun getValue(t: Double): Double =
            t * t
    },

    EASE_OUT {
        override fun getValue(t: Double): Double =
            1 - (1 - t) * (1 - t)
    },

    EASE_IN_OUT {
        override fun getValue(t: Double): Double =
            if (t < 0.5) 2 * t * t
            else 1 - (-2 * t + 2).pow(2.0) / 2
    },

    EASE_IN_CUBIC {
        override fun getValue(t: Double): Double =
            t.pow(3.0)
    },

    EASE_OUT_CUBIC {
        override fun getValue(t: Double): Double =
            1 - (1 - t).pow(3.0)
    },

    EASE_IN_OUT_CUBIC {
        override fun getValue(t: Double): Double =
            if (t < 0.5) 4 * t.pow(3.0)
            else 1 - (-2 * t + 2).pow(3.0) / 2
    },

    EASE_IN_QUINT {
        override fun getValue(t: Double): Double =
            t.pow(5.0)
    },

    EASE_OUT_QUINT {
        override fun getValue(t: Double): Double =
            1 - (1 - t).pow(5.0)
    },

    EASE_IN_OUT_QUINT {
        override fun getValue(t: Double): Double =
            if (t < 0.5) 16 * t.pow(5.0)
            else 1 - (-2 * t + 2).pow(5.0) / 2
    },

    EASE_IN_BACK {
        override fun getValue(t: Double): Double =
            c3 * t.pow(3.0) - c1 * t.pow(2.0)
    },

    EASE_OUT_BACK {
        override fun getValue(t: Double): Double =
            1 + c3 * (t - 1).pow(3.0) + c1 * (t - 1).pow(2.0)
    },

    EASE_IN_OUT_BACK {
        override fun getValue(t: Double): Double =
            if (t < 0.5)
                ((2 * t).pow(2.0) * ((c2 + 1) * 2 * t - c2)) / 2
            else
                ((2 * t - 2).pow(2.0) * ((c2 + 1) * (t * 2 - 2) + c2) + 2) / 2
    },

    EASE_IN_ELASTIC {
        override fun getValue(t: Double): Double = when (t) {
            0.0 -> 0.0
            1.0 -> 1.0
            else -> (-2.0).pow(10 * t - 10) * sin((t * 10 - 10.75) * c4)
        }
    },

    EASE_OUT_ELASTIC {
        override fun getValue(t: Double): Double = when (t) {
            0.0 -> 0.0
            1.0 -> 1.0
            else -> 2.0.pow(-10 * t) * sin((t * 10 - 0.75) * c4) + 1
        }
    },

    EASE_IN_OUT_ELASTIC {
        override fun getValue(t: Double): Double = when {
            t == 0.0 -> 0.0
            t == 1.0 -> 1.0
            t < 0.5 ->
                -(2.0.pow(20 * t - 10) * sin((20 * t - 11.125) * c5)) / 2
            else ->
                (2.0.pow(-20 * t + 10) * sin((20 * t - 11.125) * c5)) / 2 + 1
        }
    },

    EASE_OUT_BOUNCE {
        override fun getValue(t: Double): Double {
            val n1 = 7.5625
            val d1 = 2.75
            return when {
                t < 1 / d1 -> n1 * t * t
                t < 2 / d1 -> n1 * (t - 1.5 / d1).pow(2.0) + 0.75
                t < 2.5 / d1 -> n1 * (t - 2.25 / d1).pow(2.0) + 0.9375
                else -> n1 * (t - 2.625 / d1).pow(2.0) + 0.984375
            }
        }
    },

    EASE_IN_BOUNCE {
        override fun getValue(t: Double): Double =
            1 - EASE_OUT_BOUNCE.getValue(1 - t)
    },

    EASE_IN_OUT_BOUNCE {
        override fun getValue(t: Double): Double =
            if (t < 0.5)
                (1 - EASE_OUT_BOUNCE.getValue(1 - 2 * t)) / 2
            else
                (1 + EASE_OUT_BOUNCE.getValue(2 * t - 1)) / 2
    },

    EASE_IN_SINE {
        override fun getValue(t: Double): Double =
            1 - cos((t * Math.PI) / 2)
    },

    EASE_OUT_SINE {
        override fun getValue(t: Double): Double =
            sin((t * Math.PI) / 2)
    },

    EASE_IN_OUT_SINE {
        override fun getValue(t: Double): Double =
            -(cos(Math.PI * t) - 1) / 2
    },

    EASE_IN_EXPO {
        override fun getValue(t: Double): Double =
            if (t == 0.0) 0.0
            else 2.0.pow(10 * t - 10)
    },

    EASE_OUT_EXPO {
        override fun getValue(t: Double): Double =
            if (t == 1.0) 1.0
            else 1 - 2.0.pow(-10 * t)
    },

    EASE_IN_OUT_EXPO {
        override fun getValue(t: Double): Double = when {
            t == 0.0 -> 0.0
            t == 1.0 -> 1.0
            t < 0.5 -> 2.0.pow(20 * t - 10) / 2
            else -> (2 - 2.0.pow(-20 * t + 10)) / 2
        }
    };

    abstract fun getValue(t: Double): Double

    companion object {
        private const val c1 = 1.70158
        private const val c2 = c1 * 1.525
        private const val c3 = c1 + 1
        private const val c4 = (2 * Math.PI) / 3
        private const val c5 = (2 * Math.PI) / 4.5
    }
}
