package com.wifosell.zeus.service;

import com.wifosell.zeus.model.channel.Channel;
import com.wifosell.zeus.payload.request.channel.ChannelRequest;


import java.util.List;

public interface ChannelService {
    List<Channel> getAllChannels();
    List<Channel> getChannelsByUserId(Long userId);
    List<Channel> getChannelsByShopId(Long shopId);
    Channel getChannel(Long channelId);
    Channel addChannel(Long userId, ChannelRequest channelRequest);
    Channel updateChannel(Long channelId, ChannelRequest channelRequest);
    Channel activateChannel(Long channelId);
    Channel deactivateChannel(Long channelId);
}
