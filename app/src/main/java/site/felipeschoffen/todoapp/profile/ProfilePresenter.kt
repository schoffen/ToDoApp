package site.felipeschoffen.todoapp.profile

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.common.database.DataSource

class ProfilePresenter(private val view: Profile.View, private val coroutineScope: CoroutineScope) : Profile.Presenter {
    override fun getUserFolders() {
        coroutineScope.launch {
            val folders = DataSource.getFolders()
            view.displayFolders(folders)
        }
    }

    override fun getUserInfo() {
        view.displayUserInfo(
            DataSource.userInfo.name,
            DataSource.userInfo.email
        )
    }
}