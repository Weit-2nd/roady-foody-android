package com.weit2nd.data.service

import com.weit2nd.data.model.ranking.RankingItemDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface RankingService {
    @GET("/api/v1/ranking/total")
    suspend fun getTotalRanking(
        @Query("size") size: Int,
        @Query("start") start: Int,
    ): List<RankingItemDTO>

    @GET("/api/v1/ranking/review")
    suspend fun getReviewRanking(
        @Query("size") size: Int,
        @Query("start") start: Int,
    ): List<RankingItemDTO>

    @GET("/api/v1/ranking/report")
    suspend fun getReportRanking(
        @Query("size") size: Int,
        @Query("start") start: Int,
    ): List<RankingItemDTO>

    @GET("/api/v1/ranking/like")
    suspend fun getLikeRanking(
        @Query("size") size: Int,
        @Query("start") start: Int,
    ): List<RankingItemDTO>
}
