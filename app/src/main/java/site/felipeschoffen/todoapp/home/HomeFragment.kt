package site.felipeschoffen.todoapp.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.adapters.ShortTaskAdapter
import site.felipeschoffen.todoapp.common.adapters.TaskAdapterListener
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.Folder
import site.felipeschoffen.todoapp.common.datas.TaskStatus
import site.felipeschoffen.todoapp.common.datas.UserTask
import site.felipeschoffen.todoapp.common.user.UserInformation
import site.felipeschoffen.todoapp.databinding.FragmentHomeBinding
import site.felipeschoffen.todoapp.folderTasks.FolderTasksActivity
import java.util.Calendar

class HomeFragment : Fragment(), Home.View, TaskAdapterListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var presenter: Home.Presenter
    private lateinit var adapter: ShortTaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        adapter = ShortTaskAdapter(this, childFragmentManager, viewLifecycleOwner.lifecycleScope)
        presenter = HomePresenter(this, viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeTodayTaskLayout.homeTasksRV.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.homeTodayTaskLayout.homeTasksRV.adapter = adapter

        binding.homeTopLayout.homeWelcomeText.text =
            getString(R.string.hello_message, UserInformation.getUserInfo().name)

        presenter.getTodayTasks()

        binding.homeMyTasksLayout.homeCompleteContainer.completedContainerTaskBackground.setOnClickListener {
            openFolderActivity(Constants.COMPLETED_FOLDER)
        }
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(this.requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showProgress(show: Boolean) {
        when (show) {
            true -> binding.homeTodayTaskLayout.homeProgressBar.visibility = View.VISIBLE
            false -> binding.homeTodayTaskLayout.homeProgressBar.visibility = View.GONE
        }
    }

    override fun displayTodayTasks(userTasks: List<UserTask>) {
        showProgress(false)
        binding.homeTodayTaskLayout.homeNoTaskDisplayText.visibility = View.GONE
        binding.homeTodayTaskLayout.homeTasksRV.visibility = View.VISIBLE

        adapter.userTasks = userTasks.sortedWith(
            compareBy<UserTask> {
                val calendar = Calendar.getInstance()
                calendar.time = it.timestamp.toDate()
                calendar.get(Calendar.HOUR_OF_DAY)
            }.thenBy {
                val calendar = Calendar.getInstance()
                calendar.time = it.timestamp.toDate()
                calendar.get(Calendar.MINUTE)
            }
        )

        adapter.notifyDataSetChanged()
    }

    override fun displayEmptyTasks() {
        binding.homeTodayTaskLayout.homeNoTaskDisplayText.visibility = View.VISIBLE
        binding.homeTodayTaskLayout.homeTasksRV.visibility = View.GONE
    }

    override fun reloadTasks() {
        presenter.getTodayTasks()
    }

    override fun onDeleteTask(taskUID: String) {
        presenter.deleteTask(taskUID)
    }

    override fun onUpdateTaskStatus(taskUID: String, taskStatus: TaskStatus) {
        presenter.updateTaskStatus(taskUID, taskStatus)
    }

    override fun onEditTask() {
        reloadTasks()
    }

    private fun openFolderActivity(folder: Folder) {
        startActivity(Intent(requireContext(), FolderTasksActivity::class.java).apply {
            putExtra(Constants.EXTRA_FOLDER_ID, folder.uid)
        })
    }
}