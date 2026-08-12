package com.anggrayudi.storage.contract

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.storage.StorageManager
import android.provider.DocumentsContract
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions.Companion.ACTION_REQUEST_PERMISSIONS
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions.Companion.EXTRA_PERMISSIONS
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions.Companion.EXTRA_PERMISSION_GRANT_RESULTS
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.EmptyActivity
import com.anggrayudi.storage.SimpleStorage.Companion.cleanupRedundantUriPermissions
import com.anggrayudi.storage.SimpleStorage.Companion.externalStoragePath
import com.anggrayudi.storage.SimpleStorage.Companion.getDefaultExternalStorageIntent
import com.anggrayudi.storage.SimpleStorage.Companion.hasStoragePermission
import com.anggrayudi.storage.SimpleStorage.Companion.isSdCardPresent
import com.anggrayudi.storage.callback.StorageAccessCallback
import com.anggrayudi.storage.extension.fromSingleUri
import com.anggrayudi.storage.extension.fromTreeUri
import com.anggrayudi.storage.extension.getStorageId
import com.anggrayudi.storage.extension.isDocumentsDocument
import com.anggrayudi.storage.extension.isDownloadsDocument
import com.anggrayudi.storage.extension.isExternalStorageDocument
import com.anggrayudi.storage.file.DocumentFileCompat
import com.anggrayudi.storage.file.FileFullPath
import com.anggrayudi.storage.file.MimeType
import com.anggrayudi.storage.file.PublicDirectory
import com.anggrayudi.storage.file.StorageId.PRIMARY
import com.anggrayudi.storage.file.StorageType
import com.anggrayudi.storage.file.canModify
import com.anggrayudi.storage.file.getAbsolutePath
import com.anggrayudi.storage.file.getBasePath
import java.io.File
import java.io.FileNotFoundException
import kotlin.concurrent.thread

internal fun saveUriPermission(context: Context, root: Uri) =
  try {
    val writeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    context.contentResolver.takePersistableUriPermission(root, writeFlags)
    thread { cleanupRedundantUriPermissions(context.applicationContext) }
    true
  } catch (_: SecurityException) {
    false
  }

/** It returns an intent to be dispatched via [Activity.startActivityForResult] */
internal fun getExternalStorageRootAccessIntent(context: Context): Intent =
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    val sm = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
    sm.primaryStorageVolume.createOpenDocumentTreeIntent()
  } else {
    getDefaultExternalStorageIntent(context)
  }

/**
 * It returns an intent to be dispatched via [Activity.startActivityForResult] to access to the
 * first removable no primary storage. This function requires at least Nougat because on previous
 * Android versions there's no reliable way to get the volume/path of SdCard, and of course, SdCard
 * != External Storage.
 */
@Suppress("DEPRECATION")
@RequiresApi(api = Build.VERSION_CODES.N)
internal fun getSdCardRootAccessIntent(context: Context): Intent {
  val sm = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
  return sm.storageVolumes
    .firstOrNull { it.isRemovable }
    ?.let {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        it.createOpenDocumentTreeIntent()
      } else {
        // Access to the entire volume is only available for non-primary volumes
        if (it.isPrimary) {
          getDefaultExternalStorageIntent(context)
        } else {
          it.createAccessIntent(null)
        }
      }
    } ?: getDefaultExternalStorageIntent(context)
}

internal fun addInitialPathToIntent(context: Context, intent: Intent, initialPath: FileFullPath?) {
  if (Build.VERSION.SDK_INT >= 26) {
    initialPath?.toDocumentUri(context)?.let {
      intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, it)
    }
  }
}

internal fun intentToDocumentFiles(context: Context, intent: Intent?): List<DocumentFile> {
  Log.d("tornaco", "intentToDocumentFiles $intent")
  val uris =
    intent?.clipData?.run {
      val list = mutableListOf<Uri>()
      Log.d("tornaco", "itemCount $itemCount")
      for (i in 0 until itemCount) {
        list.add(getItemAt(i).uri)
      }
      list.takeIf { it.isNotEmpty() }
    } ?: listOf(intent?.data ?: return emptyList())
  Log.d("tornaco", "uris $uris")
  // content://com.android.fileexplorer.myprovider/external_files/Pictures/WeiXin/wx_camera_1774928587692.jpg
  return uris
    .mapNotNull { uri ->
      if (
        uri.isDownloadsDocument &&
          Build.VERSION.SDK_INT < 28 &&
          uri.path?.startsWith("/document/raw:") == true
      ) {
        val fullPath = uri.path.orEmpty().substringAfterLast("/document/raw:")
        DocumentFile.fromFile(File(fullPath))
      } else {
        context.fromSingleUri(uri)
      }
    }
    .filter {
      Log.d("tornaco", "filter $it ${it.isFile}")
      it.isFile || true
    }
}

/** This contract may throws [ActivityNotFoundException] or [StoragePermissionDeniedException]. */
class OpenFolderPickerContract(context: Context) :
  ActivityResultContract<OpenFolderPickerContract.Options, FolderPickerResult>() {

  private val appContext = context.applicationContext

  override fun createIntent(context: Context, input: Options): Intent {
    input.initialPath?.checkIfStorageIdIsAccessibleInSafSelector()
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P || hasStoragePermission(context)) {
      val intent =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
          Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        } else {
          getExternalStorageRootAccessIntent(context)
        }
      addInitialPathToIntent(context, intent, input.initialPath)
      return intent
    }
    throw StoragePermissionDeniedException()
  }

  override fun parseResult(resultCode: Int, intent: Intent?): FolderPickerResult {
    val uri =
      intent?.takeIf { resultCode == Activity.RESULT_OK }?.data
        ?: return FolderPickerResult.CanceledByUser

    val folder = appContext.fromTreeUri(uri)
    val storageId = uri.getStorageId(appContext)
    val storageType = StorageType.fromStorageId(storageId)

    if (folder == null || !folder.canModify(appContext)) {
      return FolderPickerResult.AccessDenied(folder, storageType, storageId)
    }
    if (
      uri.toString().let {
        it == DocumentFileCompat.DOWNLOADS_TREE_URI || it == DocumentFileCompat.DOCUMENTS_TREE_URI
      } ||
        DocumentFileCompat.isRootUri(uri) &&
          (Build.VERSION.SDK_INT < Build.VERSION_CODES.N && storageType == StorageType.SD_CARD ||
            Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) &&
          !DocumentFileCompat.isStorageUriPermissionGranted(appContext, storageId)
    ) {
      saveUriPermission(appContext, uri)
    }
    if (
      Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && storageType == StorageType.EXTERNAL ||
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && saveUriPermission(appContext, uri) ||
        folder.canModify(appContext) &&
          (uri.isDocumentsDocument || !uri.isExternalStorageDocument) ||
        DocumentFileCompat.isStorageUriPermissionGranted(appContext, storageId)
    ) {
      return FolderPickerResult.Picked(folder)
    } else {
      return FolderPickerResult.AccessDenied(folder, storageType, storageId)
    }
  }

  class Options
  @JvmOverloads
  constructor(
    /** It only takes effect on API 26+ */
    val initialPath: FileFullPath? = null
  )
}

/** This contract may throws [ActivityNotFoundException] */
class OpenFilePickerContract(context: Context) :
  ActivityResultContract<OpenFilePickerContract.Options, FilePickerResult>() {

  private val appContext = context.applicationContext

  override fun createIntent(context: Context, input: Options): Intent {
    input.initialPath?.checkIfStorageIdIsAccessibleInSafSelector()
    val mimeTypes = input.filterMimeTypes
    val intent =
      Intent(Intent.ACTION_OPEN_DOCUMENT).putExtra(Intent.EXTRA_ALLOW_MULTIPLE, input.allowMultiple)
    if (mimeTypes.size > 1) {
      intent.setType(MimeType.UNKNOWN).putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes.toTypedArray())
    } else {
      intent.type = mimeTypes.firstOrNull() ?: MimeType.UNKNOWN
    }
    addInitialPathToIntent(context, intent, input.initialPath)
    return intent
  }

  override fun parseResult(resultCode: Int, intent: Intent?): FilePickerResult {
    if (resultCode != Activity.RESULT_OK) {
      return FilePickerResult.CanceledByUser
    }
    val files = intentToDocumentFiles(appContext, intent)
    Log.d("tornaco", "parseResult $files")
    return if (files.isNotEmpty() && files.all {
        Log.d("tornaco", "canRead ${it.canRead()}")
      it.canRead() || true
    }) {
      FilePickerResult.Picked(files)
    } else {
      FilePickerResult.StoragePermissionDenied(files)
    }
  }

  class Options
  @JvmOverloads
  constructor(
    val allowMultiple: Boolean = false,
    /** It only takes effect on API 26+ */
    val initialPath: FileFullPath? = null,
    val filterMimeTypes: Set<String> = emptySet(),
  )
}

/** Show interactive UI to create a file. This contract may throws [ActivityNotFoundException] */
class FileCreationContract(context: Context) :
  ActivityResultContract<FileCreationContract.Options, FileCreationResult>() {

  private val appContext = context.applicationContext

  override fun createIntent(context: Context, input: Options): Intent {
    input.initialPath?.checkIfStorageIdIsAccessibleInSafSelector()
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).setType(input.mimeType)
    addInitialPathToIntent(context, intent, input.initialPath)
    input.fileName?.let { intent.putExtra(Intent.EXTRA_TITLE, it) }
    return intent
  }

  override fun parseResult(resultCode: Int, intent: Intent?): FileCreationResult {
    // resultCode is always OK for creating files
    val uri = intent?.data ?: return FileCreationResult.CanceledByUser
    val file =
      DocumentFileCompat.fromUri(appContext, uri)
        ?: return FileCreationResult.StoragePermissionDenied
    return FileCreationResult.Created(file)
  }

  class Options
  @JvmOverloads
  constructor(
    val mimeType: String,
    val fileName: String? = null,
    val initialPath: FileFullPath? = null,
  )
}

/**
 * Requests `android.permission.READ_EXTERNAL_STORAGE` and
 * `android.permission.WRITE_EXTERNAL_STORAGE`. It only takes effect on API 28-, because API 29+ has
 * scoped storage.
 */
class StoragePermissionContract() :
  ActivityResultContract<Unit, Map<String, @JvmSuppressWildcards Boolean>>() {

  fun getPermissions() =
    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

  override fun createIntent(context: Context, input: Unit): Intent {
    return Intent(ACTION_REQUEST_PERMISSIONS).putExtra(EXTRA_PERMISSIONS, getPermissions())
  }

  override fun getSynchronousResult(
    context: Context,
    input: Unit,
  ): SynchronousResult<Map<String, @JvmSuppressWildcards Boolean>>? {
    val permissions = getPermissions()
    val allGranted =
      permissions.all { permission ->
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
      }
    return if (allGranted) {
      SynchronousResult(permissions.associateWith { true })
    } else null
  }

  override fun parseResult(resultCode: Int, intent: Intent?): Map<String, Boolean> {
    if (resultCode != Activity.RESULT_OK || intent == null) {
      return emptyMap()
    }
    val permissions = intent.getStringArrayExtra(EXTRA_PERMISSIONS)
    val grantResults = intent.getIntArrayExtra(EXTRA_PERMISSION_GRANT_RESULTS)
    if (grantResults == null || permissions == null) {
      return emptyMap()
    }
    val grantState = grantResults.map { result -> result == PackageManager.PERMISSION_GRANTED }
    return permissions.filterNotNull().zip(grantState).toMap()
  }
}

/**
 * Managing files in direct storage requires root access. Thus we need to make sure users select
 * root path. This contract may throws [ActivityNotFoundException] or
 * [StoragePermissionDeniedException].
 */
class RequestStorageAccessContract(
  context: Context,
  /**
   * For example, if you set [StorageType.SD_CARD] but the user selects [StorageType.EXTERNAL], then
   * trigger [StorageAccessCallback.onRootPathNotSelected]. Set to [StorageType.UNKNOWN] to accept
   * any storage type.
   */
  var expectedStorageType: StorageType = StorageType.UNKNOWN,
  /** Applicable for API 30+ only, because Android 11 does not allow selecting the root path. */
  var expectedBasePath: String = "",
) : ActivityResultContract<RequestStorageAccessContract.Options, RequestStorageAccessResult>() {

  class Options
  @JvmOverloads
  constructor(
    /** It only takes effect on API 26+ */
    val initialPath: FileFullPath? = null
  )

  private val appContext = context.applicationContext

  override fun createIntent(context: Context, input: Options): Intent {
    input.initialPath?.checkIfStorageIdIsAccessibleInSafSelector()
    if (expectedStorageType == StorageType.DATA) {
      throw IllegalArgumentException(
        "Cannot use StorageType.DATA because it is never available in Storage Access Framework's folder selector."
      )
    }

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
      if (hasStoragePermission(context)) {
        if (expectedStorageType == StorageType.EXTERNAL && !isSdCardPresent) {
          val root =
            DocumentFileCompat.getRootDocumentFile(context, PRIMARY, true)
              ?: throw StoragePermissionDeniedException()
          saveUriPermission(context, root.uri)
          return Intent(context, EmptyActivity::class.java).setData(root.uri)
        }
      } else {
        throw StoragePermissionDeniedException()
      }
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      getExternalStorageRootAccessIntent(context).also {
        addInitialPathToIntent(context, it, input.initialPath)
      }
    } else if (
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && expectedStorageType == StorageType.SD_CARD
    ) {
      getSdCardRootAccessIntent(context)
    } else {
      getExternalStorageRootAccessIntent(context)
    }
  }

  override fun parseResult(resultCode: Int, intent: Intent?): RequestStorageAccessResult {
    val uri =
      intent?.takeIf { resultCode == Activity.RESULT_OK }?.data
        ?: return RequestStorageAccessResult.CanceledByUser
    val storageId = uri.getStorageId(appContext)
    val storageType = StorageType.fromStorageId(storageId)

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
      val selectedFolder =
        appContext.fromTreeUri(uri) ?: throw SecurityException("Lost access to URI: $uri")
      if (
        !expectedStorageType.isExpected(storageType) ||
          expectedBasePath.isNotEmpty() &&
            selectedFolder.getBasePath(appContext) != expectedBasePath
      ) {
        return RequestStorageAccessResult.ExpectedStorageNotSelected(
          selectedFolder,
          storageType,
          expectedBasePath,
          expectedStorageType,
        )
      }
    } else if (!expectedStorageType.isExpected(storageType)) {
      val rootPath = appContext.fromTreeUri(uri)?.getAbsolutePath(appContext).orEmpty()
      return RequestStorageAccessResult.RootPathNotSelected(
        rootPath,
        uri,
        storageType,
        expectedStorageType,
      )
    }

    if (uri.isDownloadsDocument) {
      if (uri.toString() == DocumentFileCompat.DOWNLOADS_TREE_URI) {
        saveUriPermission(appContext, uri)
        return RequestStorageAccessResult.RootPathPermissionGranted(
          appContext.fromTreeUri(uri)
            ?: throw FileNotFoundException("Failed to get root path from URI: $uri")
        )
      }
      return RequestStorageAccessResult.RootPathNotSelected(
        PublicDirectory.DOWNLOADS.absolutePath,
        uri,
        StorageType.EXTERNAL,
        expectedStorageType,
      )
    }

    if (uri.isDocumentsDocument) {
      if (uri.toString() == DocumentFileCompat.DOCUMENTS_TREE_URI) {
        saveUriPermission(appContext, uri)
        return RequestStorageAccessResult.RootPathPermissionGranted(
          appContext.fromTreeUri(uri)
            ?: throw FileNotFoundException("Failed to get root path from URI: $uri")
        )
      }
      return RequestStorageAccessResult.RootPathNotSelected(
        PublicDirectory.DOCUMENTS.absolutePath,
        uri,
        StorageType.EXTERNAL,
        expectedStorageType,
      )
    }

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R && !uri.isExternalStorageDocument) {
      return RequestStorageAccessResult.RootPathNotSelected(
        externalStoragePath,
        uri,
        StorageType.EXTERNAL,
        expectedStorageType,
      )
    }

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && storageId == PRIMARY) {
      saveUriPermission(appContext, uri)
      return RequestStorageAccessResult.RootPathPermissionGranted(
        appContext.fromTreeUri(uri)
          ?: throw FileNotFoundException("Failed to get root path from URI: $uri")
      )
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R || DocumentFileCompat.isRootUri(uri)) {
      return if (saveUriPermission(appContext, uri)) {
        RequestStorageAccessResult.RootPathPermissionGranted(
          appContext.fromTreeUri(uri)
            ?: throw FileNotFoundException("Failed to get root path from URI: $uri")
        )
      } else {
        RequestStorageAccessResult.StoragePermissionDenied
      }
    } else {
      if (storageId == PRIMARY) {
        return RequestStorageAccessResult.RootPathNotSelected(
          externalStoragePath,
          uri,
          StorageType.EXTERNAL,
          expectedStorageType,
        )
      } else {
        var sdCardIntent: Intent? = null
        if (
          Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
        ) {
          val sm = appContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
          @Suppress("DEPRECATION")
          sdCardIntent = sm.storageVolumes.firstOrNull { !it.isPrimary }?.createAccessIntent(null)
        }
        return RequestStorageAccessResult.RootPathNotSelected(
          "/storage/$storageId",
          uri,
          StorageType.SD_CARD,
          expectedStorageType,
          sdCardIntent,
        )
      }
    }
  }
}
