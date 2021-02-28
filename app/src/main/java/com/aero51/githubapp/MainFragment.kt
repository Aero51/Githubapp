package com.aero51.githubapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aero51.githubapp.databinding.MainFragmentBinding
import com.aero51.githubapp.model.Repo
import com.aero51.githubapp.utils.Constants
import com.aero51.githubapp.utils.SortOrder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment),MainAdapter.OnItemClickListener {
    private val viewModel by viewModels<MainViewModel>()
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private var searchQuery:String=Constants.DEFAULT_QUERY
    private var sortOrder:SortOrder=SortOrder.BY_STARS
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = MainFragmentBinding.bind(view)


        val adapter = MainAdapter(this)
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator=null
            recyclerView.addItemDecoration(
                    DividerItemDecoration(
                            context,
                            LinearLayoutManager.VERTICAL
                    )
            )
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                    header = GithubLoadStateAdapter {adapter.retry()},
                    footer = GithubLoadStateAdapter {adapter.retry()}
            )
            buttonRetry.setOnClickListener {
                adapter.retry()
            }
        }
        viewModel.repos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        adapter.itemCount < 1) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }
        setHasOptionsMenu(true)
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    searchQuery=query
                    viewModel.searchRepos(searchQuery,sortOrder)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_sort_by_stars -> {
                sortOrder=SortOrder.BY_STARS
                viewModel.searchRepos(searchQuery,sortOrder)
                true
            }
            R.id.action_sort_by_forks -> {
                sortOrder=SortOrder.BY_FORKS
                viewModel.searchRepos(searchQuery,sortOrder)
                true
            }
            R.id.action_sort_by_updated -> {
                sortOrder=SortOrder.BY_UPDATED
                viewModel.searchRepos(searchQuery,sortOrder)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(repo: Repo) {
        val action =MainFragmentDirections.actionMainFragmentToDetailsFragment(repo)
        findNavController().navigate(action)
    }
}