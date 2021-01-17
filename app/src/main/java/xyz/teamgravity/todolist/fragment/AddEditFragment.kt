package xyz.teamgravity.todolist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import xyz.teamgravity.todolist.R
import xyz.teamgravity.todolist.databinding.FragmentAddEditBinding
import xyz.teamgravity.todolist.helper.util.Helper
import xyz.teamgravity.todolist.viewmodel.AddEditViewModel

@AndroidEntryPoint
class AddEditFragment : Fragment() {

    private var _binding: FragmentAddEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddEditViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddEditBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        updateUI()
    }

    private fun updateUI() {
        binding.apply {
            taskField.setText(viewModel.taskName)
            importantC.isChecked = viewModel.taskImportance
            importantC.jumpDrawablesToCurrentState()
            timestampT.visibility = if (viewModel.task == null) View.GONE else View.VISIBLE
            timestampT.text = Helper.addWithPoint(getString(R.string.created), viewModel.task?.timestamp.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}