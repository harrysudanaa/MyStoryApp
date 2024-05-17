package com.example.mystoryapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.mystoryapp.data.local.room.StoryDatabase
import com.example.mystoryapp.data.local.room.entity.RemoteKeys
import com.example.mystoryapp.data.local.room.entity.Story
import com.example.mystoryapp.data.remote.retrofit.ApiService
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator @Inject constructor(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase,
    private val token: String
): RemoteMediator<Int, Story>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Story>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getStories(token, page, state.config.pageSize)
            val endOfPaginationReached = responseData.listStory.isEmpty()

            storyDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    storyDatabase.storyDao().deleteAllStories()
                }
                responseData.listStory.forEach { storyItem ->
                    val story = Story(
                        storyItem.id.toString(),
                        storyItem.name,
                        storyItem.description,
                        storyItem.photoUrl,
                        storyItem.createdAt
                    )
                    val prevKey = if (page == 1) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = RemoteKeys(id = storyItem.id.toString(), prevKey = prevKey, nextKey = nextKey)
                    storyDatabase.remoteKeysDao().insertKey(keys)
                    storyDatabase.storyDao().insertStory(story)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }  catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Story>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            storyDatabase.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Story>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            storyDatabase.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Story>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                storyDatabase.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}