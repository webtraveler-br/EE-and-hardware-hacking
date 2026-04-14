package com.roadmap.eehh

import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random
import org.json.JSONArray
import org.json.JSONObject

private const val OFFLINE_FSRS_STATE_LEARNING = 1
private const val OFFLINE_FSRS_STATE_REVIEW = 2
private const val OFFLINE_FSRS_STATE_RELEARNING = 3
private const val OFFLINE_DAY_MS = 24L * 60L * 60L * 1000L
private const val OFFLINE_FSRS_DEFAULT_DECAY = 0.1542
private const val OFFLINE_FSRS_STABILITY_MIN = 0.001
private const val OFFLINE_FSRS_MIN_DIFFICULTY = 1.0
private const val OFFLINE_FSRS_MAX_DIFFICULTY = 10.0

private val DEFAULT_FSRS_PARAMETERS = listOf(
    0.212,
    1.2931,
    2.3065,
    8.2956,
    6.4133,
    0.8334,
    3.0194,
    0.001,
    1.8722,
    0.1666,
    0.796,
    1.4835,
    0.0614,
    0.2629,
    1.6483,
    0.6014,
    1.8729,
    0.5425,
    0.0912,
    0.0658,
    OFFLINE_FSRS_DEFAULT_DECAY,
)

private val FUZZ_RANGES = listOf(
    Triple(2.5, 7.0, 0.15),
    Triple(7.0, 20.0, 0.10),
    Triple(20.0, Double.POSITIVE_INFINITY, 0.05),
)

data class OfflineSchedulerProfile(
    val parameters: List<Double> = DEFAULT_FSRS_PARAMETERS,
    val desiredRetention: Double = 0.9,
    val learningStepsSeconds: List<Int> = listOf(60, 600),
    val relearningStepsSeconds: List<Int> = listOf(600),
    val maximumInterval: Int = 36500,
    val enableFuzzing: Boolean = false,
) {
    fun toJson(): JSONObject {
        val parametersArray = JSONArray()
        parameters.forEach { parametersArray.put(it) }

        val learningArray = JSONArray()
        learningStepsSeconds.forEach { learningArray.put(it) }

        val relearningArray = JSONArray()
        relearningStepsSeconds.forEach { relearningArray.put(it) }

        return JSONObject()
            .put("parameters", parametersArray)
            .put("desired_retention", desiredRetention)
            .put("learning_steps_seconds", learningArray)
            .put("relearning_steps_seconds", relearningArray)
            .put("maximum_interval", maximumInterval)
            .put("enable_fuzzing", enableFuzzing)
    }

    companion object {
        fun fromJson(item: JSONObject?): OfflineSchedulerProfile {
            if (item == null) {
                return DEFAULT_OFFLINE_SCHEDULER_PROFILE
            }

            val parameters = item.optJSONArray("parameters")?.let { array ->
                buildList {
                    for (index in 0 until array.length()) {
                        add(array.optDouble(index))
                    }
                }
            }?.takeIf { it.size == DEFAULT_FSRS_PARAMETERS.size } ?: DEFAULT_FSRS_PARAMETERS

            val learningSteps = item.optJSONArray("learning_steps_seconds")?.let { array ->
                buildList {
                    for (index in 0 until array.length()) {
                        add(array.optInt(index))
                    }
                }
            }?.filter { it > 0 } ?: DEFAULT_OFFLINE_SCHEDULER_PROFILE.learningStepsSeconds

            val relearningSteps = item.optJSONArray("relearning_steps_seconds")?.let { array ->
                buildList {
                    for (index in 0 until array.length()) {
                        add(array.optInt(index))
                    }
                }
            }?.filter { it > 0 } ?: DEFAULT_OFFLINE_SCHEDULER_PROFILE.relearningStepsSeconds

            return OfflineSchedulerProfile(
                parameters = parameters,
                desiredRetention = item.optDouble(
                    "desired_retention",
                    DEFAULT_OFFLINE_SCHEDULER_PROFILE.desiredRetention,
                ),
                learningStepsSeconds = learningSteps,
                relearningStepsSeconds = relearningSteps,
                maximumInterval = item.optInt(
                    "maximum_interval",
                    DEFAULT_OFFLINE_SCHEDULER_PROFILE.maximumInterval,
                ),
                enableFuzzing = item.optBoolean(
                    "enable_fuzzing",
                    DEFAULT_OFFLINE_SCHEDULER_PROFILE.enableFuzzing,
                ),
            )
        }
    }
}

val DEFAULT_OFFLINE_SCHEDULER_PROFILE = OfflineSchedulerProfile()

data class OfflineFsrsProjection(
    val state: Int,
    val step: Int?,
    val stability: Double?,
    val difficulty: Double?,
    val dueAtEpochMs: Long,
    val lastReviewEpochMs: Long,
)

object OfflineFsrsScheduler {
    fun project(
        card: OfflineCard,
        rating: Int,
        profile: OfflineSchedulerProfile,
        reviewedAtEpochMs: Long,
    ): OfflineFsrsProjection {
        val params = profile.parameters
        val decay = -params[20]
        val factor = 0.9.pow(1.0 / decay) - 1.0

        var state = if (card.isNew) OFFLINE_FSRS_STATE_LEARNING else (card.fsrsState ?: OFFLINE_FSRS_STATE_REVIEW)
        var step = when {
            card.isNew -> 0
            state == OFFLINE_FSRS_STATE_REVIEW -> null
            else -> card.fsrsStep ?: 0
        }
        var stability = card.fsrsStability
        var difficulty = card.fsrsDifficulty
        val daysSinceLastReview = card.lastReviewEpochMs?.let { lastReviewEpochMs ->
            max(0L, (reviewedAtEpochMs - lastReviewEpochMs) / OFFLINE_DAY_MS)
        }

        val learningStepsMs = profile.learningStepsSeconds.map { it.toLong() * 1000L }
        val relearningStepsMs = profile.relearningStepsSeconds.map { it.toLong() * 1000L }
        var nextIntervalMs = OFFLINE_DAY_MS

        when (state) {
            OFFLINE_FSRS_STATE_LEARNING -> {
                val currentStep = step ?: 0

                if (stability == null || difficulty == null) {
                    stability = initialStability(params, rating)
                    difficulty = initialDifficulty(params, rating, clamp = true)
                } else if (daysSinceLastReview != null && daysSinceLastReview < 1L) {
                    stability = shortTermStability(params, stability, rating)
                    difficulty = nextDifficulty(params, difficulty, rating)
                } else {
                    stability = nextStability(
                        params = params,
                        difficulty = difficulty,
                        stability = stability,
                        retrievability = getCardRetrievability(
                            stability = stability,
                            lastReviewEpochMs = card.lastReviewEpochMs,
                            currentEpochMs = reviewedAtEpochMs,
                            factor = factor,
                            decay = decay,
                        ),
                        rating = rating,
                    )
                    difficulty = nextDifficulty(params, difficulty, rating)
                }

                if (
                    learningStepsMs.isEmpty() ||
                    (currentStep >= learningStepsMs.size && rating in 2..4)
                ) {
                    state = OFFLINE_FSRS_STATE_REVIEW
                    step = null
                    nextIntervalMs = nextIntervalDays(
                        stability = stability,
                        desiredRetention = profile.desiredRetention,
                        factor = factor,
                        decay = decay,
                        maximumInterval = profile.maximumInterval,
                    ) * OFFLINE_DAY_MS
                } else {
                    when (rating) {
                        1 -> {
                            step = 0
                            nextIntervalMs = learningStepsMs[step]
                        }
                        2 -> {
                            step = currentStep
                            nextIntervalMs = when {
                                currentStep == 0 && learningStepsMs.size == 1 ->
                                    (learningStepsMs[0] * 1.5).roundToLongCompat()
                                currentStep == 0 && learningStepsMs.size >= 2 ->
                                    ((learningStepsMs[0] + learningStepsMs[1]) / 2L)
                                else -> learningStepsMs[currentStep.coerceIn(0, learningStepsMs.lastIndex)]
                            }
                        }
                        3 -> {
                            if (currentStep + 1 == learningStepsMs.size) {
                                state = OFFLINE_FSRS_STATE_REVIEW
                                step = null
                                nextIntervalMs = nextIntervalDays(
                                    stability = stability,
                                    desiredRetention = profile.desiredRetention,
                                    factor = factor,
                                    decay = decay,
                                    maximumInterval = profile.maximumInterval,
                                ) * OFFLINE_DAY_MS
                            } else {
                                step = currentStep + 1
                                nextIntervalMs = learningStepsMs[step]
                            }
                        }
                        4 -> {
                            state = OFFLINE_FSRS_STATE_REVIEW
                            step = null
                            nextIntervalMs = nextIntervalDays(
                                stability = stability,
                                desiredRetention = profile.desiredRetention,
                                factor = factor,
                                decay = decay,
                                maximumInterval = profile.maximumInterval,
                            ) * OFFLINE_DAY_MS
                        }
                    }
                }
            }
            OFFLINE_FSRS_STATE_REVIEW -> {
                val currentDifficulty = difficulty ?: initialDifficulty(params, 3, clamp = true)
                val currentStability = stability ?: initialStability(params, 3)

                stability = if (daysSinceLastReview != null && daysSinceLastReview < 1L) {
                    shortTermStability(params, currentStability, rating)
                } else {
                    nextStability(
                        params = params,
                        difficulty = currentDifficulty,
                        stability = currentStability,
                        retrievability = getCardRetrievability(
                            stability = currentStability,
                            lastReviewEpochMs = card.lastReviewEpochMs,
                            currentEpochMs = reviewedAtEpochMs,
                            factor = factor,
                            decay = decay,
                        ),
                        rating = rating,
                    )
                }
                difficulty = nextDifficulty(params, currentDifficulty, rating)

                when (rating) {
                    1 -> {
                        if (relearningStepsMs.isEmpty()) {
                            nextIntervalMs = nextIntervalDays(
                                stability = stability,
                                desiredRetention = profile.desiredRetention,
                                factor = factor,
                                decay = decay,
                                maximumInterval = profile.maximumInterval,
                            ) * OFFLINE_DAY_MS
                        } else {
                            state = OFFLINE_FSRS_STATE_RELEARNING
                            step = 0
                            nextIntervalMs = relearningStepsMs[step]
                        }
                    }
                    2, 3, 4 -> {
                        step = null
                        nextIntervalMs = nextIntervalDays(
                            stability = stability,
                            desiredRetention = profile.desiredRetention,
                            factor = factor,
                            decay = decay,
                            maximumInterval = profile.maximumInterval,
                        ) * OFFLINE_DAY_MS
                    }
                }
            }
            OFFLINE_FSRS_STATE_RELEARNING -> {
                val currentStep = step ?: 0
                val currentDifficulty = difficulty ?: initialDifficulty(params, 3, clamp = true)
                val currentStability = stability ?: initialStability(params, 3)

                if (daysSinceLastReview != null && daysSinceLastReview < 1L) {
                    stability = shortTermStability(params, currentStability, rating)
                    difficulty = nextDifficulty(params, currentDifficulty, rating)
                } else {
                    stability = nextStability(
                        params = params,
                        difficulty = currentDifficulty,
                        stability = currentStability,
                        retrievability = getCardRetrievability(
                            stability = currentStability,
                            lastReviewEpochMs = card.lastReviewEpochMs,
                            currentEpochMs = reviewedAtEpochMs,
                            factor = factor,
                            decay = decay,
                        ),
                        rating = rating,
                    )
                    difficulty = nextDifficulty(params, currentDifficulty, rating)
                }

                if (
                    relearningStepsMs.isEmpty() ||
                    (currentStep >= relearningStepsMs.size && rating in 2..4)
                ) {
                    state = OFFLINE_FSRS_STATE_REVIEW
                    step = null
                    nextIntervalMs = nextIntervalDays(
                        stability = stability,
                        desiredRetention = profile.desiredRetention,
                        factor = factor,
                        decay = decay,
                        maximumInterval = profile.maximumInterval,
                    ) * OFFLINE_DAY_MS
                } else {
                    when (rating) {
                        1 -> {
                            step = 0
                            nextIntervalMs = relearningStepsMs[step]
                        }
                        2 -> {
                            step = currentStep
                            nextIntervalMs = when {
                                currentStep == 0 && relearningStepsMs.size == 1 ->
                                    (relearningStepsMs[0] * 1.5).roundToLongCompat()
                                currentStep == 0 && relearningStepsMs.size >= 2 ->
                                    ((relearningStepsMs[0] + relearningStepsMs[1]) / 2L)
                                else -> relearningStepsMs[currentStep.coerceIn(0, relearningStepsMs.lastIndex)]
                            }
                        }
                        3 -> {
                            if (currentStep + 1 == relearningStepsMs.size) {
                                state = OFFLINE_FSRS_STATE_REVIEW
                                step = null
                                nextIntervalMs = nextIntervalDays(
                                    stability = stability,
                                    desiredRetention = profile.desiredRetention,
                                    factor = factor,
                                    decay = decay,
                                    maximumInterval = profile.maximumInterval,
                                ) * OFFLINE_DAY_MS
                            } else {
                                step = currentStep + 1
                                nextIntervalMs = relearningStepsMs[step]
                            }
                        }
                        4 -> {
                            state = OFFLINE_FSRS_STATE_REVIEW
                            step = null
                            nextIntervalMs = nextIntervalDays(
                                stability = stability,
                                desiredRetention = profile.desiredRetention,
                                factor = factor,
                                decay = decay,
                                maximumInterval = profile.maximumInterval,
                            ) * OFFLINE_DAY_MS
                        }
                    }
                }
            }
        }

        if (profile.enableFuzzing && state == OFFLINE_FSRS_STATE_REVIEW) {
            nextIntervalMs = fuzzedIntervalMs(nextIntervalMs, profile.maximumInterval)
        }

        return OfflineFsrsProjection(
            state = state,
            step = step,
            stability = stability,
            difficulty = difficulty,
            dueAtEpochMs = reviewedAtEpochMs + nextIntervalMs,
            lastReviewEpochMs = reviewedAtEpochMs,
        )
    }

    private fun initialStability(parameters: List<Double>, rating: Int): Double {
        return clampStability(parameters[(rating - 1).coerceIn(0, 3)])
    }

    private fun initialDifficulty(parameters: List<Double>, rating: Int, clamp: Boolean): Double {
        val raw = parameters[4] - exp(parameters[5] * (rating - 1)) + 1.0
        return if (clamp) clampDifficulty(raw) else raw
    }

    private fun getCardRetrievability(
        stability: Double?,
        lastReviewEpochMs: Long?,
        currentEpochMs: Long,
        factor: Double,
        decay: Double,
    ): Double {
        if (stability == null || lastReviewEpochMs == null) {
            return 0.0
        }

        val elapsedDays = max(0L, (currentEpochMs - lastReviewEpochMs) / OFFLINE_DAY_MS).toDouble()
        return (1.0 + factor * elapsedDays / stability).pow(decay)
    }

    private fun nextIntervalDays(
        stability: Double?,
        desiredRetention: Double,
        factor: Double,
        decay: Double,
        maximumInterval: Int,
    ): Long {
        val safeStability = stability ?: OFFLINE_FSRS_STABILITY_MIN
        val raw = (safeStability / factor) * (desiredRetention.pow(1.0 / decay) - 1.0)
        return min(max(raw.roundToInt(), 1), maximumInterval).toLong()
    }

    private fun shortTermStability(parameters: List<Double>, stability: Double, rating: Int): Double {
        var increase = exp(parameters[17] * (rating - 3 + parameters[18])) * stability.pow(-parameters[19])
        if (rating == 3 || rating == 4) {
            increase = max(increase, 1.0)
        }
        return clampStability(stability * increase)
    }

    private fun nextDifficulty(parameters: List<Double>, difficulty: Double, rating: Int): Double {
        fun linearDamping(deltaDifficulty: Double, currentDifficulty: Double): Double {
            return (10.0 - currentDifficulty) * deltaDifficulty / 9.0
        }

        val arg1 = initialDifficulty(parameters, 4, clamp = false)
        val deltaDifficulty = -(parameters[6] * (rating - 3))
        val arg2 = difficulty + linearDamping(deltaDifficulty, difficulty)
        val nextDifficulty = parameters[7] * arg1 + (1.0 - parameters[7]) * arg2
        return clampDifficulty(nextDifficulty)
    }

    private fun nextStability(
        params: List<Double>,
        difficulty: Double,
        stability: Double,
        retrievability: Double,
        rating: Int,
    ): Double {
        val next = when (rating) {
            1 -> nextForgetStability(params, difficulty, stability, retrievability)
            2, 3, 4 -> nextRecallStability(params, difficulty, stability, retrievability, rating)
            else -> stability
        }
        return clampStability(next)
    }

    private fun nextForgetStability(
        params: List<Double>,
        difficulty: Double,
        stability: Double,
        retrievability: Double,
    ): Double {
        val longTerm =
            params[11] *
                difficulty.pow(-params[12]) *
                ((stability + 1.0).pow(params[13]) - 1.0) *
                exp((1.0 - retrievability) * params[14])
        val shortTerm = stability / exp(params[17] * params[18])
        return min(longTerm, shortTerm)
    }

    private fun nextRecallStability(
        params: List<Double>,
        difficulty: Double,
        stability: Double,
        retrievability: Double,
        rating: Int,
    ): Double {
        val hardPenalty = if (rating == 2) params[15] else 1.0
        val easyBonus = if (rating == 4) params[16] else 1.0
        return stability * (
            1.0 +
                exp(params[8]) *
                (11.0 - difficulty) *
                stability.pow(-params[9]) *
                (exp((1.0 - retrievability) * params[10]) - 1.0) *
                hardPenalty *
                easyBonus
            )
    }

    private fun fuzzedIntervalMs(intervalMs: Long, maximumInterval: Int): Long {
        val intervalDays = intervalMs.toDouble() / OFFLINE_DAY_MS.toDouble()
        if (intervalDays < 2.5) {
            return intervalMs
        }

        var delta = 1.0
        for ((start, end, factor) in FUZZ_RANGES) {
            delta += factor * max(min(intervalDays, end) - start, 0.0)
        }

        var minInterval = max(2, (intervalDays - delta).roundToInt())
        var maxInterval = min(maximumInterval, (intervalDays + delta).roundToInt())
        minInterval = min(minInterval, maxInterval)

        val fuzzedDays = min(
            (Random.nextDouble() * (maxInterval - minInterval + 1) + minInterval).roundToInt(),
            maximumInterval,
        )
        return fuzzedDays.toLong() * OFFLINE_DAY_MS
    }

    private fun clampDifficulty(value: Double): Double {
        return value.coerceIn(OFFLINE_FSRS_MIN_DIFFICULTY, OFFLINE_FSRS_MAX_DIFFICULTY)
    }

    private fun clampStability(value: Double): Double {
        return max(value, OFFLINE_FSRS_STABILITY_MIN)
    }
}

private fun Double.roundToLongCompat(): Long = roundToInt().toLong()