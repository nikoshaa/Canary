package com.example.wildan_canary.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.wildan_canary.data.network.response.story.ItemStory
import com.example.wildan_canary.helper.DataDUmmy
import com.example.wildan_canary.helper.MainDispatcheRule
import com.example.wildan_canary.helper.adapter.AdapterStory
import com.example.wildan_canary.helper.getOrAwaitValue
import com.example.wildan_canary.helper.repository.UniversalRepository
import com.example.wildan_canary.view.story.view.ViewStoryViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListStoryViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcheRule()

    @Mock private lateinit var storyRepository: UniversalRepository

    private val dummyStoriesResponse = DataDUmmy.generateDummyStories()

    @Test
    fun `ketika getStories Harus Tidak Null dan Return Success`() = runTest {
        val data: PagingData<ItemStory> = StoryPagingSource.snapshot(dummyStoriesResponse.listStory)
        val expectedStories = MutableLiveData<PagingData<ItemStory>>()
        expectedStories.value = data
        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedStories)

        val listStoryViewModel = ViewStoryViewModel(storyRepository)
        val actualStories: PagingData<ItemStory> = listStoryViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = AdapterStory.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStories)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStoriesResponse.listStory, differ.snapshot())
        Assert.assertEquals(dummyStoriesResponse.listStory.size, differ.snapshot().size)

        // Memastikan data pertama sesuai
        val expectedFirstItem = dummyStoriesResponse.listStory[0]
        val actualFirstItem = differ.snapshot()[0]
        Assert.assertEquals(expectedFirstItem.id, actualFirstItem?.id)
        Assert.assertEquals(expectedFirstItem.name, actualFirstItem?.name)
        Assert.assertEquals(expectedFirstItem.description, actualFirstItem?.description)
        // Lakukan pengecekan atribut lainnya sesuai dengan yang diharapkan
    }

    @Test
    fun `ketika GetQuote Kosong Harus Return No Data`() = runTest {
        val data: PagingData<ItemStory> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<ItemStory>>()
        expectedQuote.value = data
        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedQuote)

        val viewStoryViewModel = ViewStoryViewModel(storyRepository)
        val actualQuote: PagingData<ItemStory> = viewStoryViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = AdapterStory.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualQuote)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<ItemStory>>>() {
    companion object {
        fun snapshot(items: List<ItemStory>): PagingData<ItemStory> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ItemStory>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ItemStory>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
