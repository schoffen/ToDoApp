package site.felipeschoffen.todoapp.profile

import site.felipeschoffen.todoapp.common.datas.Folder

interface Profile {
    interface View {
        fun setAdapterFolders(folders: List<Folder>)
        fun displayUserInfo(name: String, email: String)
        fun notifyAdapterItemInserted(position: Int)
        fun notifyAdapterItemRemoved(position: Int)
    }

    interface Presenter {
        fun getUserFolders()
        fun getUserInfo()
    }

}