package liedge.limatech.lib.upgrades;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import liedge.limatech.LimaTech;
import net.minecraft.core.Holder;

import java.util.function.Function;

public final class MutableUpgradesContainer<U extends UpgradeBase<?, U>, T extends UpgradesContainerBase<?, U>>
{
    private final Object2IntMap<Holder<U>> map;
    private final Function<Object2IntMap<Holder<U>>, T> containerFactory;

    private MutableUpgradesContainer(Object2IntMap<Holder<U>> map, Function<Object2IntMap<Holder<U>>, T> containerFactory)
    {
        this.map = map;
        this.containerFactory = containerFactory;
    }

    public MutableUpgradesContainer(Function<Object2IntMap<Holder<U>>, T> containerFactory)
    {
        this(new Object2IntOpenHashMap<>(), containerFactory);
    }

    public MutableUpgradesContainer(T container, Function<Object2IntMap<Holder<U>>, T> containerFactory)
    {
        this(new Object2IntOpenHashMap<>(container.internalMap), containerFactory);
    }

    public MutableUpgradesContainer<U, T> set(Holder<U> upgrade, int upgradeRank)
    {
        if (checkRank(upgradeRank)) map.put(upgrade, upgradeRank);

        return this;
    }

    public MutableUpgradesContainer<U, T> set(Holder<U> upgrade)
    {
        return set(upgrade, 1);
    }

    public MutableUpgradesContainer<U, T> set(UpgradeBaseEntry<U> entry)
    {
        return set(entry.upgrade(), entry.upgradeRank());
    }

    public int merge(Holder<U> upgrade, int upgradeRank)
    {
        if (checkRank(upgradeRank))
        {
            int previousRank = map.getOrDefault(upgrade, 0);
            if (upgradeRank > previousRank)
            {
                map.put(upgrade, upgradeRank);
                return previousRank;
            }
        }

        return -1;
    }

    public int merge(UpgradeBaseEntry<U> entry)
    {
        return merge(entry.upgrade(), entry.upgradeRank());
    }

    public MutableUpgradesContainer<U, T> remove(Holder<U> holder)
    {
        map.removeInt(holder);
        return this;
    }

    public T toImmutable()
    {
        return containerFactory.apply(map);
    }

    private boolean checkRank(int upgradeRank)
    {
        boolean b = upgradeRank > 0 && upgradeRank <= UpgradeBase.MAX_UPGRADE_RANK;

        if (!b) LimaTech.LOGGER.warn("Invalid upgrade rank: {}", upgradeRank);

        return b;
    }
}