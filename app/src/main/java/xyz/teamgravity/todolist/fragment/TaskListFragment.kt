package xyz.teamgravity.todolist.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import xyz.teamgravity.todolist.R
import xyz.teamgravity.todolist.databinding.FragmentTaskListBinding
import xyz.teamgravity.todolist.helper.adapter.TaskAdapter
import xyz.teamgravity.todolist.helper.extensions.onQueryTextChanged
import xyz.teamgravity.todolist.viewmodel.TaskSort
import xyz.teamgravity.todolist.viewmodel.TaskViewModel

@AndroidEntryPoint
class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TaskViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        recyclerView()
    }

    private fun recyclerView() {
        val adapter = TaskAdapter()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        // observer data
        viewModel.tasks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.task_list_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        // text change
        searchView.onQueryTextChanged {
            viewModel.query.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_name -> {
                viewModel.sortOrder.value = TaskSort.BY_NAME
                true
            }

            R.id.action_sort_date -> {
                viewModel.sortOrder.value = TaskSort.BY_DATE
                true
            }

            R.id.action_hide -> {
                item.isChecked = !item.isChecked
                viewModel.hideCompleted.value = item.isChecked
                true
            }

            R.id.action_delete_completed -> {

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}