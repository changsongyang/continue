package com.github.continuedev.continueintellijextension.services

import com.github.continuedev.continueintellijextension.`continue`.CoreMessenger
import com.github.continuedev.continueintellijextension.`continue`.CoreMessengerManager
import com.github.continuedev.continueintellijextension.`continue`.IdeProtocolClient
import com.github.continuedev.continueintellijextension.toolWindow.ContinuePluginToolWindowFactory
import com.github.continuedev.continueintellijextension.utils.uuid
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

@Service(Service.Level.PROJECT)
class ContinuePluginService(project: Project) : Disposable, DumbAware {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    var continuePluginWindow: ContinuePluginToolWindowFactory.ContinuePluginWindow? = null
    var ideProtocolClient: IdeProtocolClient? = null
    var coreMessengerManager: CoreMessengerManager? = null
    val coreMessenger: CoreMessenger?
        get() = coreMessengerManager?.coreMessenger
    var workspacePaths: Array<String>? = null

    override fun dispose() {
        coroutineScope.cancel()
        coreMessenger?.coroutineScope?.let {
            it.cancel()
            coreMessenger?.killSubProcess()
        }
    }

    fun sendToWebview(
        messageType: String,
        data: Any?,
        messageId: String = uuid()
    ) {
        continuePluginWindow?.browser?.sendToWebview(messageType, data, messageId)
    }
}