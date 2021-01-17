package xyz.teamgravity.todolist.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.teamgravity.todolist.R
import xyz.teamgravity.todolist.databinding.FragmentTaskListBinding
import xyz.teamgravity.todolist.helper.adapter.TaskAdapter
import xyz.teamgravity.todolist.helper.extensions.onQueryTextChanged
import xyz.teamgravity.todolist.model.TaskModel
import xyz.teamgravity.todolist.viewmodel.TaskSort
import xyz.teamgravity.todolist.viewmodel.TaskViewModel

@AndroidEntryPoint
class TaskListFragment : Fragment(), TaskAdapter.OnTaskListener {

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
        events()
    }

    private fun recyclerView() {
        val adapter = TaskAdapter(this)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                // swipe
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewModel.onTaskSwiped(adapter.currentList[viewHolder.adapterPosition])
                }
            }).attachToRecyclerView(recyclerView)
        }

        // observer data
        viewModel.tasks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun events() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.taskEvent.collect { event ->
                when (event) {
                    is TaskViewModel.TaskEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(requireView(), R.string.deleted_successfully, Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo) {
                                viewModel.onUndoTaskDelete(event.task)
                            }.show()
                    }
                }
            }
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

        // update hide completed when app starts
        viewLifecycleOwner.lifecycleScope.launch {
            // it gets only first and cancels coroutine
            menu.findItem(R.id.action_hide).isChecked =
                viewModel.preferencesFlow.first().hideCompleted
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_name -> {
                viewModel.onTaskSort(TaskSort.BY_NAME)
                true
            }

            R.id.action_sort_date -> {
                viewModel.onTaskSort(TaskSort.BY_DATE)
                true
            }

            R.id.action_hide -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompleted(item.isChecked)
                true
            }

            R.id.action_delete_completed -> {

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // task click
    override fun onTaskClick(task: TaskModel) {
        viewModel.onTaskClicked(task)
    }

    // task check click
    override fun onTaskCheck(task: TaskModel, isChecked: Boolean) {
        viewModel.onTaskChecked(task, isChecked)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}