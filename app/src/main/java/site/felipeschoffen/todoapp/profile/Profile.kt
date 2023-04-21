package site.felipeschoffen.todoapp.profile

import site.felipeschoffen.todoapp.common.datas.Folder

interface Profile {
    interface View {
        fun displayFolders(folders: List<Folder>)
        fun displayUserInfo(name: String, email: String)
        fun getFolders()
    }

    interface Presenter {
        fun getUserFolders()
        fun getUserInfo()
    }

}