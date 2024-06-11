package com.weit2nd.data.repository.searchhistory

import com.weit2nd.data.source.SearchHistoryDataSource
import com.weit2nd.domain.repository.searchhistory.SearchHistoryRepository
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val dataSource: SearchHistoryDataSource,
) : SearchHistoryRepository {
    override suspend fun getSearchHistories(): List<String> {
        return dataSource.getSearchHistories()
    }

    override suspend fun addSearchHistory(searchWord: String) {
        val newHistories = dataSource.getSearchHistories().toMutableList()
        // 넣으려는 검색어가 첫번째에 있다면 동작이 불필요하기 때문에 패스
        val targetIndex = newHistories.indexOf(searchWord).takeIf { it != 0 } ?: return
        if (targetIndex != -1) {
            // 순서가 달라도 같은 요소가 들어있다면 DataStore는 같은 데이터로 인식해 갱신을 안한다.
            // 그래서 갱신전에 한번 clear를 함
            // TODO: 6/12/24 (minseong1231) clear 동작을 DataSource로 이동하기
            // dataSource의 동작을 알기 때문에 이런 동작을 할 수 있는건데 되게 쿨하지 않음
            // 성능을 챙기면서 로직을 옮길 수 있는 멋진 방법을 생각해보자
            clearSearchHistory()
            newHistories.removeAt(targetIndex)
        }
        newHistories.add(0, searchWord)
        dataSource.setSearchHistory(newHistories)
    }

    override suspend fun removeSearchHistory(searchWord: String) {
        val newHistories = dataSource.getSearchHistories().minus(searchWord)
        dataSource.setSearchHistory(newHistories)
    }

    override suspend fun clearSearchHistory() {
        dataSource.setSearchHistory(emptyList())
    }
}
