package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.channel.Channel;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.channel.ChannelRequest;
import com.wifosell.zeus.repository.ChannelRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service("Channel")
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChannelServiceImpl(ChannelRepository channelRepository, UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public List<Channel> getChannelsByUserId(Long userId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return channelRepository.findChannelsByGeneralManagerId(gm.getId());
    }

    @Override
    public List<Channel> getChannelsByShopId(Long shopId) {
        return channelRepository.findChannelsByShopId(shopId);
    }

    @Override
    public Channel getChannel(Long channelId) {
        return channelRepository.findChannelById(channelId);
    }

    @Override
    public Channel addChannel(Long userId, ChannelRequest channelRequest) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Channel channel = new Channel();
        this.updateChannelByRequest(channel, channelRequest);
        channel.setGeneralManager(gm);
        return channelRepository.save(channel);
    }

    @Override
    public Channel updateChannel(Long channelId, ChannelRequest channelRequest) {
        Channel channel = channelRepository.findChannelById(channelId);
        this.updateChannelByRequest(channel, channelRequest);
        return channelRepository.save(channel);
    }

    @Override
    public Channel activateChannel(Long channelId) {
        Channel channel = channelRepository.findChannelById(channelId, true);
        channel.setIsActive(true);
        return channelRepository.save(channel);
    }

    @Override
    public Channel deactivateChannel(Long channelId) {
        Channel channel = channelRepository.findChannelById(channelId);
        channel.setIsActive(false);
        return channelRepository.save(channel);
    }

    private void updateChannelByRequest(Channel channel, ChannelRequest channelRequest) {
        Optional.ofNullable(channelRequest.getChannelName()).ifPresent(channel::setChannelName);
        Optional.ofNullable(channelRequest.getShortName()).ifPresent(channel::setShortName);
        Optional.ofNullable(channelRequest.getDescription()).ifPresent(channel::setDescription);
    }
}
