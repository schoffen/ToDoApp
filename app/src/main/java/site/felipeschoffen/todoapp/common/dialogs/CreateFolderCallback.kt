package site.felipeschoffen.todoapp.common.dialogs

import site.felipeschoffen.todoapp.common.datas.Folder

interface CreateFolderCallback {
    fun newFolderCreated(folder: Folder)
}