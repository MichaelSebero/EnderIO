package crazypants.enderio.integration.tic;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import com.enderio.core.common.Lang;
import com.enderio.core.common.mixin.SimpleMixinLoader;

import crazypants.enderio.api.EIOTags;
import crazypants.enderio.api.addon.IEnderIOAddon;
import crazypants.enderio.base.Log;
import crazypants.enderio.base.init.RegisterModObject;
import crazypants.enderio.integration.tic.init.TicObject;

@Mod(modid = EnderIOIntegrationTic.MODID,
     name = EnderIOIntegrationTic.MOD_NAME,
     version = EnderIOIntegrationTic.VERSION,
     dependencies = EnderIOIntegrationTic.DEPENDENCIES)
@EventBusSubscriber
public class EnderIOIntegrationTic implements IEnderIOAddon {

    public static final @Nonnull String MODID = "enderiointegrationtic";
    public static final @Nonnull String DOMAIN = "enderio";
    public static final @Nonnull String MOD_NAME = "Ender IO Integration with Tinkers' Construct";
    public static final @Nonnull String VERSION = EIOTags.VERSION;

    private static final @Nonnull String DEFAULT_DEPENDENCIES = "before:tconstruct;before:enderiobase";
    public static final @Nonnull String DEPENDENCIES = DEFAULT_DEPENDENCIES;

    public EnderIOIntegrationTic() {
        SimpleMixinLoader.loadMixinSources(this);
    }

    @EventHandler
    public static void init(FMLPreInitializationEvent event) {
        if (isLoaded()) {
            Log.debug("PHASE PRE-INIT EIO TIC E");
            TicControl.preInitBeforeTic(event);
            Log.warn("TConstruct, you fail again, muhaha! The world is mine, mine!");
        } else {
            Log.warn("Tinkers' Construct integration NOT loaded. Tinkers' Construct is not installed");
        }
    }

    @EventHandler
    public static void init(FMLInitializationEvent event) {
        if (isLoaded()) {
            Log.debug("PHASE INIT EIO TIC E");
            TicControl.initBeforeTic(event);
        }
    }

    @EventHandler
    public static void init(FMLPostInitializationEvent event) {
        if (isLoaded()) {
            Log.debug("PHASE POST-INIT EIO TIC E");
            TicControl.postInitBeforeTic(event);
        }
    }

    public static final @Nonnull Lang lang = new Lang(DOMAIN);

    @Override
    @Nullable
    public Configuration getConfiguration() {
        return null;
    }

    @Override
    public void injectBlocks(@Nonnull IForgeRegistry<Block> registry) {
        if (isLoaded()) {
            TicControl.injectBlocks(registry);
        }
    }

    static boolean enableBook = false; // TODO: Move book to its own submod that only depends on Mantle

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void registerBlocksEarly(@Nonnull RegisterModObject event) {
        if (enableBook && isLoaded()) {
            event.register(TicObject.class);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerRenderers(@Nonnull ModelRegistryEvent event) {
        if (isLoaded()) {
            TicControl.registerRenderers(event);
        }
    }

    public static boolean isLoaded() {
        return Loader.isModLoaded("tconstruct");
    }
}
