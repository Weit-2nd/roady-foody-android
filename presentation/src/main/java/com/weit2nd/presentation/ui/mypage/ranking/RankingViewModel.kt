package com.weit2nd.presentation.ui.mypage.ranking

import com.weit2nd.domain.model.ranking.RankingType
import com.weit2nd.domain.usecase.ranking.GetRankingUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val getRankingUseCase: GetRankingUseCase,
) : BaseViewModel<RankingState, RankingSideEffect>() {
    override val container: Container<RankingState, RankingSideEffect> =
        container(RankingState())

    private var loadRankingJob: Job =
        Job().apply {
            complete()
        }
    private val hasNext = AtomicBoolean(true)

    fun onCloseButtonClick() {
        loadRankingJob.cancel()
        RankingIntent.CloseDialog.post()
    }

    fun onCreate() {
        loadRankingJob.cancel()
        loadRankingJob =
            RankingIntent
                .LoadNextRank(
                    rankingType = container.stateFlow.value.selectedRankingType,
                    lastRank = null,
                ).post()
    }

    fun onTabChanged(rankingType: RankingType) {
        loadRankingJob.cancel()
        loadRankingJob =
            RankingIntent
                .LoadNextRank(
                    rankingType = rankingType,
                    lastRank = null,
                ).post()
    }

    fun onLastVisibleItemChanged(position: Int) {
        val currentReviewSize = container.stateFlow.value.rankingItems.size
        val needNextPage = (currentReviewSize - position) <= REMAINING_PAGE_FOR_REQUEST
        if (needNextPage) {
            onLoadNextRank()
        }
    }

    private fun onLoadNextRank() {
        val needNextPage = loadRankingJob.isCompleted && hasNext.get()
        if (needNextPage) {
            val lastRank =
                container.stateFlow.value.rankingItems
                    .lastOrNull()
                    ?.ranking
            loadRankingJob =
                RankingIntent
                    .LoadNextRank(
                        rankingType = container.stateFlow.value.selectedRankingType,
                        lastRank = lastRank,
                    ).post()
        }
    }

    private fun RankingIntent.post() =
        intent {
            when (this@post) {
                RankingIntent.CloseDialog -> {
                    postSideEffect(RankingSideEffect.CloseDialog)
                }
                is RankingIntent.LoadNextRank -> {
                    reduce {
                        state.copy(
                            selectedRankingType = rankingType,
                        )
                    }
                    val startRank = (lastRank ?: 0) + 1
                    if (lastRank == null) {
                        hasNext.set(true)
                    }
                    runCatching {
                        getRankingUseCase.invoke(
                            rankingType = rankingType,
                            startRank = startRank,
                            size = DEFAULT_LOAD_COUNT,
                        )
                    }.onSuccess { rankingItems ->
                        hasNext.set(rankingItems.isNotEmpty())
                        val updatedItems =
                            if (lastRank == null) {
                                rankingItems
                            } else {
                                state.rankingItems + rankingItems
                            }
                        reduce {
                            state.copy(
                                rankingItems = updatedItems,
                            )
                        }
                    }.onFailure {
                        val errorMessage = it.message ?: "네트워크 에러 일걸..?"
                        postSideEffect(RankingSideEffect.ShowErrorMessage(errorMessage))
                    }
                }
            }
        }

    companion object {
        private const val DEFAULT_LOAD_COUNT = 10
        private const val REMAINING_PAGE_FOR_REQUEST = 3
    }
}
