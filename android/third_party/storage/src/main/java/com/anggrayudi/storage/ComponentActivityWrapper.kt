package com.anggrayudi.storage

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.anggrayudi.storage.contract.FileCreationContract
import com.anggrayudi.storage.contract.OpenFilePickerContract
import com.anggrayudi.storage.contract.OpenFolderPickerContract
import com.anggrayudi.storage.contract.RequestStorageAccessContract
import com.anggrayudi.storage.file.StorageType

/**
 * Created on 18/08/20
 *
 * @author Anggrayudi H
 */
internal class ComponentActivityWrapper(private val _activity: ComponentActivity) :
  ComponentWrapper, DefaultLifecycleObserver {

  init {
    _activity.lifecycle.addObserver(this)
  }

  lateinit var storage: SimpleStorage

  lateinit var storageAccessContract: RequestStorageAccessContract
  lateinit var requestStorageAccessLauncher:
    ActivityResultLauncher<RequestStorageAccessContract.Options>

  lateinit var fileCreationContract: FileCreationContract
  lateinit var requestFileCreationLauncher: ActivityResultLauncher<FileCreationContract.Options>

  lateinit var filePickerContract: OpenFilePickerContract
  lateinit var requestFilePickerLauncher: ActivityResultLauncher<OpenFilePickerContract.Options>

  lateinit var folderPickerContract: OpenFolderPickerContract
  lateinit var requestFolderPickerLauncher: ActivityResultLauncher<OpenFolderPickerContract.Options>

  override fun onCreate(owner: LifecycleOwner) {
    storageAccessContract = RequestStorageAccessContract(_activity, StorageType.UNKNOWN, "")
    requestStorageAccessLauncher =
      _activity.registerForActivityResult(storageAccessContract) { result ->
        storage.onRequestStorageAccessResult(result)
      }

    fileCreationContract = FileCreationContract(_activity)
    requestFileCreationLauncher =
      _activity.registerForActivityResult(fileCreationContract) { result ->
        storage.onFileCreationResult(result)
      }

    filePickerContract = OpenFilePickerContract(_activity)
    requestFilePickerLauncher =
      _activity.registerForActivityResult(filePickerContract) { result ->
        storage.onFilePickedResult(result)
      }

    folderPickerContract = OpenFolderPickerContract(_activity)
    requestFolderPickerLauncher =
      _activity.registerForActivityResult(folderPickerContract) { result ->
        storage.onFolderPickedResult(result)
      }
  }

  override val context: Context
    get() = _activity

  override val activity: ComponentActivity
    get() = _activity

  var requestCode = 0
  private val activityResultLauncher =
    _activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      storage.onActivityResult(requestCode, it.resultCode, it.data)
    }

  override fun startActivityForResult(intent: Intent, requestCode: Int): Boolean {
    return try {
      activityResultLauncher.launch(intent)
      this.requestCode = requestCode
      true
    } catch (_: ActivityNotFoundException) {
      false
    }
  }
}
