package site.felipeschoffen.todoapp.profile

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.Folder
import site.felipeschoffen.todoapp.common.user.UserInformation

class ProfilePresenter(private val view: Profile.View, private val coroutineScope: CoroutineScope) : Profile.Presenter {
    override fun getUserFolders() {
        coroutineScope.launch {
            val folders = DataSource.getFolders()

            val foldersWithCreateFolderReference = mutableListOf<Folder>()
            foldersWithCreateFolderReference.addAll(folders)

            foldersWithCreateFolderReference.add(Constants.CREATE_NEW_FOLDER_REFERENCE)

            view.setAdapterFolders(foldersWithCreateFolderReference)
        }
    }

    override fun getUserInfo() {
        view.displayUserInfo(
            UserInformation.getUserInfo().name,
            UserInformation.getUserInfo().email
        )
    }
}