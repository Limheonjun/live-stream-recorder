package com.emgc.admin.presentation

import com.emgc.admin.application.ChannelService
import com.emgc.admin.presentation.request.ChannelRegisterRequest
import com.emgc.livestreamrecorder.enums.ChannelType
import com.emgc.livestreamrecorder.enums.RecordingStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class ChannelController(
    private val channelService: ChannelService,
) {
    @GetMapping("/")
    fun getChannelList(model: Model): String {
        val channelList = channelService.getChannelList()
        model.addAttribute("channelList", channelList)
        return "index"
    }

    @GetMapping("/register")
    fun getRegister(model: Model): String {
        model.addAttribute("channelTypes", ChannelType.entries)
        model.addAttribute("recordingStatuses", RecordingStatus.entries)
        return "register"
    }

    @PostMapping("/register")
    fun register(@ModelAttribute request: ChannelRegisterRequest, model: Model): String {
        channelService.register(request)
        return "redirect:/"
    }
}