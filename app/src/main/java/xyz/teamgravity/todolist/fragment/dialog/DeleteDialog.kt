package xyz.teamgravity.todolist.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import xyz.teamgravity.todolist.R
import xyz.teamgravity.todolist.viewmodel.DeleteViewModel

@AndroidEntryPoint
class DeleteDialog : DialogFragment() {

    private val viewModel by viewModels<DeleteViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.confirm_deletion)
            .setMessage(R.string.wanna_delete_all)
            .setNegativeButton(R.string.no, null)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.onConfirmClick()
            }.create()
}