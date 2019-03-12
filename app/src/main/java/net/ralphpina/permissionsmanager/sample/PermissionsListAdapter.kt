package net.ralphpina.permissionsmanager.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.Disposable
import net.ralphpina.permissionsmanager.Permission
import net.ralphpina.permissionsmanager.PermissionsManager
import net.ralphpina.permissionsmanager.sample.databinding.PermissionBinding
import net.ralphpina.permissionsmanager.sample.databinding.PermissionGroupBinding

sealed class PermissionListItem(view: View) : RecyclerView.ViewHolder(view)

private class PermissionGroupViewHolder(
    private val dataBinding: PermissionGroupBinding
) : PermissionListItem(dataBinding.root) {
    fun bind(group: String) {
        dataBinding.group = group
        dataBinding.executePendingBindings()
    }
}

private class PermissionViewHolder(
    private val dataBinding: PermissionBinding,
    private val permissionsManager: PermissionsManager
) : PermissionListItem(dataBinding.root) {

    private var disposable: Disposable? = null

    fun bind(permission: Permission) {
        disposable?.dispose()
        dataBinding.settingsButton.setOnClickListener { permissionsManager.navigateToOsAppSettings() }
        dataBinding.requestPermissionButton.setOnClickListener {
            permissionsManager.request(permission)
                .doOnSuccess {
                    Toast.makeText(
                        dataBinding.root.context,
                        "Permission result for ${it[0].permission}, given: ${it[0].isGranted}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .subscribe()
        }
        disposable = permissionsManager.observe(permission)
            .doOnNext {
                check(it.size == 1) { "List has no result!" }
                dataBinding.result = it[0]
                dataBinding.executePendingBindings()
            }
            .subscribe()
    }

    fun dispose() = disposable?.dispose()
}

private sealed class PermissionsModel {
    class Group(val title: String) : PermissionsModel()
    class Perm(val permission: Permission) : PermissionsModel()
}

private const val GROUP = 101
private const val PERMISSION = 222

class PermissionsListAdapter(
    private val permissionsManager: PermissionsManager
) : RecyclerView.Adapter<PermissionListItem>() {

    private val models: List<PermissionsModel> = buildViewModels()

    override fun getItemViewType(position: Int) =
        when (models[position]) {
            is PermissionsModel.Group -> GROUP
            is PermissionsModel.Perm -> PERMISSION
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            GROUP -> {
                val binding: PermissionGroupBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.permission_group,
                    parent,
                    false
                )
                PermissionGroupViewHolder(binding)
            }
            PERMISSION -> {
                val binding: PermissionBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.permission,
                    parent,
                    false
                )
                PermissionViewHolder(binding, permissionsManager)
            }
            else -> throw IllegalStateException()
        }

    override fun getItemCount() = models.size

    override fun onBindViewHolder(holder: PermissionListItem, position: Int) =
        when (holder) {
            is PermissionGroupViewHolder -> holder.bind((models[position] as PermissionsModel.Group).title)
            is PermissionViewHolder -> holder.bind((models[position] as PermissionsModel.Perm).permission)
        }

    override fun onViewDetachedFromWindow(holder: PermissionListItem) {
        super.onViewDetachedFromWindow(holder)
        when (holder) {
            is PermissionViewHolder -> holder.dispose()
        }
    }
}

private fun buildViewModels() = listOf(
    PermissionsModel.Group("Calendar"),
    PermissionsModel.Perm(Permission.Calendar.Read),
    PermissionsModel.Perm(Permission.Calendar.Write),
    PermissionsModel.Group("CallLog"),
    PermissionsModel.Perm(Permission.CallLog.Read),
    PermissionsModel.Perm(Permission.CallLog.Write),
    PermissionsModel.Perm(Permission.CallLog.ProcessOutgoing),
    PermissionsModel.Group("Camera"),
    PermissionsModel.Perm(Permission.Camera),
    PermissionsModel.Group("Contacts"),
    PermissionsModel.Perm(Permission.Contacts.Read),
    PermissionsModel.Perm(Permission.Contacts.Write),
    PermissionsModel.Perm(Permission.Contacts.GetAccounts),
    PermissionsModel.Group("Location"),
    PermissionsModel.Perm(Permission.Location.Fine),
    PermissionsModel.Perm(Permission.Location.Coarse),
    PermissionsModel.Group("Microphone"),
    PermissionsModel.Perm(Permission.Microphone),
    PermissionsModel.Group("Phone"),
    PermissionsModel.Perm(Permission.Phone.ReadState),
    PermissionsModel.Perm(Permission.Phone.ReadNumbers),
    PermissionsModel.Perm(Permission.Phone.Call),
    PermissionsModel.Perm(Permission.Phone.Answer),
    PermissionsModel.Perm(Permission.Phone.AddVoiceMail),
    PermissionsModel.Perm(Permission.Phone.UseSip),
    PermissionsModel.Group("Sensors"),
    PermissionsModel.Perm(Permission.Sensors),
    PermissionsModel.Group("Sms"),
    PermissionsModel.Perm(Permission.Sms.Send),
    PermissionsModel.Perm(Permission.Sms.Receive),
    PermissionsModel.Perm(Permission.Sms.Read),
    PermissionsModel.Perm(Permission.Sms.ReceiveWapPush),
    PermissionsModel.Perm(Permission.Sms.ReceiveMms),
    PermissionsModel.Group("Storage"),
    PermissionsModel.Perm(Permission.Storage.ReadExternal),
    PermissionsModel.Perm(Permission.Storage.WriteExternal)
)
