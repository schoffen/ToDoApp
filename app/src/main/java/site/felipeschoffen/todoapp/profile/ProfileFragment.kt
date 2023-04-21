package site.felipeschoffen.todoapp.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.adapters.FoldersAdapter
import site.felipeschoffen.todoapp.common.datas.Folder
import site.felipeschoffen.todoapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(), Profile.View {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: FoldersAdapter
    private lateinit var presenter: ProfilePresenter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        presenter = ProfilePresenter(this, viewLifecycleOwner.lifecycleScope)

        presenter.getUserInfo()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileFoldersRV.layoutManager = GridLayoutManager(view.context, 2)
        adapter = FoldersAdapter(
            emptyList(), viewLifecycleOwner.lifecycleScope, this, this.childFragmentManager
        )
        binding.profileFoldersRV.adapter = adapter

        getFolders()
    }

    override fun displayFolders(folders: List<Folder>) {
        adapter.folders = folders
        adapter.notifyDataSetChanged()
    }

    override fun displayUserInfo(name: String, email: String) {
        binding.profileName.text = name
        binding.profileEmail.text = email
    }

    override fun getFolders() {
        presenter.getUserFolders()
    }
}