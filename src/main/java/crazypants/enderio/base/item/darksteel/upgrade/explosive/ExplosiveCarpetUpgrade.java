package crazypants.enderio.base.item.darksteel.upgrade.explosive;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.enderio.core.common.util.NNList;

import crazypants.enderio.api.upgrades.IDarkSteelItem;
import crazypants.enderio.api.upgrades.IDarkSteelUpgrade;
import crazypants.enderio.api.upgrades.IRule;
import crazypants.enderio.base.EnderIO;
import crazypants.enderio.base.config.config.DarkSteelConfig;
import crazypants.enderio.base.handler.darksteel.AbstractUpgrade;
import crazypants.enderio.base.handler.darksteel.Rules;
import crazypants.enderio.base.item.darksteel.upgrade.energy.EnergyUpgrade;
import crazypants.enderio.base.lang.Lang;

@EventBusSubscriber(modid = EnderIO.MODID)
public class ExplosiveCarpetUpgrade extends AbstractUpgrade {

    private static final @Nonnull String UPGRADE_NAME = "carpet";

    public static final @Nonnull ExplosiveCarpetUpgrade INSTANCE = new ExplosiveCarpetUpgrade();

    @SubscribeEvent
    public static void registerDarkSteelUpgrades(@Nonnull RegistryEvent.Register<IDarkSteelUpgrade> event) {
        event.getRegistry().register(INSTANCE);
    }

    public ExplosiveCarpetUpgrade() {
        super(UPGRADE_NAME, "enderio.darksteel.upgrade." + UPGRADE_NAME, DarkSteelConfig.explosiveCarpetUpgradeCost);
    }

    @Override
    @Nonnull
    public List<IRule> getRules() {
        return new NNList<>(ExplosiveUpgrade.HAS_ANY, Rules.callbacksFor(ExplosiveUpgrade.INSTANCE),
                Rules.staticCheck(item -> item.isPickaxe()),
                EnergyUpgrade.HAS_ANY, Rules.itemTypeTooltip(Lang.DSU_CLASS_TOOLS_PICKAXE));
    }

    @Override
    public boolean canOtherBeRemoved(@Nonnull ItemStack stack, @Nonnull IDarkSteelItem item,
                                     @Nonnull IDarkSteelUpgrade other) {
        return other != ExplosiveUpgrade.INSTANCE;
    }
}
