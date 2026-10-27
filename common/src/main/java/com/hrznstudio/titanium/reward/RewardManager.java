// Titanium Lite Reward Manager

package com.hrznstudio.titanium.reward;

import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RewardManager {

    private static final RewardManager INSTANCE = new RewardManager();
    private final Map<UUID, RewardGiver> rewards;

    private RewardManager() {
        this.rewards = new ConcurrentHashMap<>();
    }

    public static RewardManager get() {
        return INSTANCE;
    }

    public RewardGiver getGiver(UUID uuid, String name) {
        return rewards.computeIfAbsent(uuid, uuid1 -> new RewardGiver(uuid, name));
    }

    public Map<UUID, RewardGiver> getRewards() {
        return rewards;
    }

    public List<Identifier> collectRewardsIdentifiers(UUID uuid) {
        return rewards.values().stream().map(rewardGiver -> rewardGiver.collectRewardsIdentifiers(uuid)).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public Reward getReward(Identifier resourceLocation) {
        for (RewardGiver value : rewards.values()) {
            for (Reward reward : value.getRewards()) {
                if (reward.getIdentifier().equals(resourceLocation)) return reward;
            }
        }
        return null;
    }
}
