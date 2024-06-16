package com.weit2nd.domain.repository.searchlocation

import com.weit2nd.domain.model.Location

interface SearchLocationRepository {

    suspend fun searchLocation(searchWord: String): List<Location>
}
