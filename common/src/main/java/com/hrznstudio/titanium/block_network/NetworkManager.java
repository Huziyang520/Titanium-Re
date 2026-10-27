// Titanium Lite Network Manager

package com.hrznstudio.titanium.block_network;

import com.hrznstudio.titanium.block_network.element.NetworkElement;
import com.hrznstudio.titanium.block_network.element.NetworkElementFactory;
import com.hrznstudio.titanium.block_network.element.NetworkElementRegistry;
import com.hrznstudio.titanium.block_network.graph.NetworkGraphScannerResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

public class NetworkManager extends SavedData {
    private static final String NAME =  "titanium_networks";
    private static final Logger LOGGER = LogManager.getLogger(NetworkManager.class);
    private final Level level;
    private final Map<String, Network> networks = new HashMap<>();
    private final Map<BlockPos, NetworkElement> elements = new HashMap<>();

    public NetworkManager(Level level) {
        this.level = level;
    }

    public static NetworkManager get(Level level) {
        return get((ServerLevel) level);
    }

    public static NetworkManager get(ServerLevel level) {
        SavedDataType<NetworkManager> type = new SavedDataType<>(
            Identifier.fromNamespaceAndPath("titanium", NAME),
            ignored -> new NetworkManager(level),
            ignored -> CompoundTag.CODEC.xmap(tag -> {
                NetworkManager manager = new NetworkManager(level);
                manager.load(tag);
                return manager;
            }, manager -> manager.save(new CompoundTag(), level.registryAccess()))
        );
        return level.getDataStorage().computeIfAbsent(type);
    }

    public void addNetwork(Network network) {
        if (networks.containsKey(network.getId())) {
            throw new RuntimeException("Duplicate network " + network.getId());
        }

        networks.put(network.getId(), network);

        LOGGER.debug("Network {} created", network.getId());

        setDirty();
    }

    public void removeNetwork(String id) {
        if (!networks.containsKey(id)) {
            throw new RuntimeException("Network " + id + " not found");
        }

        networks.remove(id);

        LOGGER.debug("Network {} removed", id);

        setDirty();
    }

    private void formNetworkAt(Level level, BlockPos pos, Identifier type) {
        Network network = NetworkRegistry.INSTANCE.getFactory(type).create(pos);

        addNetwork(network);

        network.scanGraph(level, pos);
    }

    private void mergeNetworksIntoOne(Set<NetworkElement> candidates, Level level, BlockPos pos) {
        if (candidates.isEmpty()) {
            throw new RuntimeException("Cannot merge networks: no candidates");
        }

        Set<Network> networkCandidates = new HashSet<>();

        for (NetworkElement candidate : candidates) {
            if (candidate.getNetwork() == null) {
                Network network = NetworkRegistry.INSTANCE.getFactory(candidate.getNetworkType()).create(pos);
                addNetwork(network);
                candidate.joinNetwork(network);
            }

            networkCandidates.add(candidate.getNetwork());
        }

        Iterator<Network> networks = networkCandidates.iterator();

        Network mainNetwork = networks.next();

        Set<Network> mergedNetworks = new HashSet<>();

        while (networks.hasNext()) {

            Network otherNetwork = networks.next();

            boolean canMerge = mainNetwork.getType().equals(otherNetwork.getType());

            if (canMerge) {
                mergedNetworks.add(otherNetwork);

                removeNetwork(otherNetwork.getId());
            }
        }

        mainNetwork.scanGraph(level, pos);

        mergedNetworks.forEach(n -> n.onMergedWith(mainNetwork));
    }

    public void addElement(NetworkElement networkElement) {
        if (elements.containsKey(networkElement.getPos())) {
            throw new RuntimeException("Network element at " + networkElement.getPos() + " already exists");
        }

        elements.put(networkElement.getPos(), networkElement);

        LOGGER.debug("Network element added at {}", networkElement.getPos());

        setDirty();

        Set<NetworkElement> adjacentElement = findAdjacentElements(networkElement, networkElement.getNetworkType());

        if (adjacentElement.isEmpty()) {
            formNetworkAt(networkElement.getLevel(), networkElement.getPos(), networkElement.getNetworkType());
        } else {
            mergeNetworksIntoOne(adjacentElement, networkElement.getLevel(), networkElement.getPos());
        }
    }

    public void removeElement(BlockPos pos) {
        NetworkElement element = getElement(pos);
        if (element == null) {
            throw new RuntimeException("Element at " + pos + " was not found");
        }

        if (element.getNetwork() == null) {
            LOGGER.warn("Removed element at {} has no associated network", element.getPos());
        }

        elements.remove(element.getPos());

        LOGGER.debug("Element removed at {}", element.getPos());

        setDirty();

        if (element.getNetwork() != null) {
            splitNetworks(element);
        }
    }

    private void splitNetworks(NetworkElement originElement) {

        for (NetworkElement adjacent : findAdjacentElements(originElement, originElement.getNetworkType())) {
            if (adjacent.getNetwork() == null) {
                throw new RuntimeException("Adjacent element has no network");
            }

            if (adjacent.getNetwork() != originElement.getNetwork()) {

            }
        }

        NetworkElement otherElementInNetwork = findFirstAdjacentElement(originElement, originElement.getNetworkType());

        if (otherElementInNetwork != null) {
            otherElementInNetwork.getNetwork().setOriginPos(otherElementInNetwork.getPos());
            setDirty();

            NetworkGraphScannerResult result = otherElementInNetwork.getNetwork().scanGraph(
                    otherElementInNetwork.getLevel(),
                    otherElementInNetwork.getPos()
            );

            boolean foundRemovedElement = false;

            for (NetworkElement removed : result.getRemovedElements()) {

                if (removed.getPos().equals(originElement.getPos())) {
                    foundRemovedElement = true;
                    continue;
                }

                if (removed.getNetwork() == null) {
                    formNetworkAt(removed.getLevel(), removed.getPos(), removed.getNetworkType());
                }
            }

            if (!foundRemovedElement) {
                throw new RuntimeException("Didn't find removed element when splitting network");
            }
        } else {
            LOGGER.debug("Removing empty network {}", originElement.getNetwork().getId());

            removeNetwork(originElement.getNetwork().getId());
        }
    }

    private Set<NetworkElement> findAdjacentElements(NetworkElement current, Identifier networkType) {
        Set<NetworkElement> elements = new HashSet<>();
        for (Direction dir : Direction.values()) {
            NetworkElement element = getElement(current.getPos().relative(dir));
            if (!current.canConnectFrom(dir)) continue;
            if (element != null && element.getNetworkType().equals(networkType) && element.canConnectFrom(dir.getOpposite())) {
                elements.add(element);
            }
        }

        return elements;
    }

    @Nullable
    private NetworkElement findFirstAdjacentElement(NetworkElement current, Identifier networkType) {
        for (Direction dir : Direction.values()) {
            if (!current.canConnectFrom(dir)) continue;
            NetworkElement element = getElement(current.getPos().relative(dir));
            if (element != null && element.getNetworkType().equals(networkType) && element.canConnectFrom(dir.getOpposite()) && current.getNetwork() == element.getNetwork()) {
                return element;
            }
        }

        return null;
    }

    @Nullable
    public NetworkElement getElement(BlockPos pos) {
        return elements.get(pos);
    }

    public Collection<Network> getNetworks() {
        return networks.values();
    }

    public void load(CompoundTag tag) {
        ListTag elements = tag.getListOrEmpty("elements");
        for (Tag elementTag : elements) {
            CompoundTag elementTagCompound = (CompoundTag) elementTag;

            Identifier factoryId = Identifier.parse(elementTagCompound.getStringOr("id", ""));

            NetworkElementFactory factory = NetworkElementRegistry.INSTANCE.getFactory(factoryId);
            if (factory == null) {
                LOGGER.warn("Element {} no longer exists", factoryId.toString());
                continue;
            }

            NetworkElement element = factory.createFromNbt(level, elementTagCompound);

            this.elements.put(element.getPos(), element);
        }

        ListTag nets = tag.getListOrEmpty("networks");
        for (Tag netTag : nets) {
            CompoundTag netTagCompound = (CompoundTag) netTag;
            if (!netTagCompound.contains("type")) {
                LOGGER.warn("Skipping network without type");
                continue;
            }

            Identifier type = Identifier.parse(netTagCompound.getStringOr("type", ""));

            NetworkFactory factory = NetworkRegistry.INSTANCE.getFactory(type);
            if (factory == null) {
                LOGGER.warn("Unknown network type {}", type.toString());
                continue;
            }

            Network network = factory.create(netTagCompound);

            networks.put(network.getId(), network);
        }

        LOGGER.debug("Read {} elements", elements.size());
        LOGGER.debug("Read {} networks", networks.size());
    }

    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag elements = new ListTag();
        this.elements.values().forEach(p -> {
            CompoundTag elementTag = new CompoundTag();
            elementTag.putString("id", p.getId().toString());
            elements.add(p.writeToNbt(elementTag));
        });
        tag.put("elements", elements);

        ListTag networks = new ListTag();
        this.networks.values().forEach(n -> {
            CompoundTag networkTag = new CompoundTag();
            networkTag.putString("type", n.getType().toString());
            networks.add(n.writeToNbt(networkTag));
        });
        tag.put("networks", networks);

        return tag;
    }

    public Map<BlockPos, NetworkElement> getElements() {
        return elements;
    }
}
