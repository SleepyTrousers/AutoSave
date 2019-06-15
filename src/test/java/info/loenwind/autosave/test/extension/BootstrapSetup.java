package info.loenwind.autosave.test.extension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import cpw.mods.modlauncher.Launcher;
import net.minecraft.util.registry.Bootstrap;
import net.minecraftforge.fml.loading.FMLLoader;

public class BootstrapSetup implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL)
            .getOrComputeIfAbsent(MyFixture.class);
    }

    static class MyFixture implements ExtensionContext.Store.CloseableResource {
        MyFixture() throws Exception {
            Constructor<Launcher> ctor = Launcher.class.getDeclaredConstructor();
            ctor.setAccessible(true);
            Launcher.INSTANCE = ctor.newInstance();
            
            Field f = FMLLoader.class.getDeclaredField("mcVersion");
            f.setAccessible(true);
            f.set(null, "1.14.2");
            
            f = FMLLoader.class.getDeclaredField("forgeVersion");
            f.setAccessible(true);
            f.set(null, "26.0.0");
            
            Bootstrap.register();
        }

        @Override
        public void close() {
        }
    }
}