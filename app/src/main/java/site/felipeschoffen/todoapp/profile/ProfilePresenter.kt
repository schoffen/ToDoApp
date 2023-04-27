package site.felipeschoffen.todoapp.profile

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.Folder

class ProfilePresenter(private val view: Profile.View, private val coroutineScope: CoroutineScope) : Profile.Presenter {
    override fun getUserFolders() {
        coroutineScope.launch {
            val folders = DataSource.getFolders()

            val foldersWithCreateFolderReference = mutableListOf<Folder>()
            foldersWithCreateFolderReference.addAll(folders)

            foldersWithCreateFolderReference.add(Constants.createNewFolderReference)

            view.setAdapterFolders(foldersWithCreateFolderReference)
        }
    }

    override fun getUserInfo() {
        view.displayUserInfo(
            DataSource.userInfo.name,
            DataSource.userInfo.email
        )
    }
}