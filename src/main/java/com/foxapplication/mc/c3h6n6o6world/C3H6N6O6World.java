package com.foxapplication.mc.c3h6n6o6world;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.setting.Setting;
import com.foxapplication.mc.c3h6n6o6world.thread.CalculationController;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.api.ModInitializer;
@Slf4j
public class C3H6N6O6World implements ModInitializer {
    @Getter
    private static final Setting setting = new Setting(FileUtil.touch(System.getProperty("user.dir") + "/config/C3H6N6O6World.setting"), CharsetUtil.CHARSET_UTF_8, true);
    /**
     * 服务器线程
     */
    public static Thread ServerThread;
    @Override
    public void onInitialize() {
        log.info("C3H6N6O6World is loading!");
        setting.autoLoad(true);
        log.info("C3H6N6O6World is being initialised.");
        CalculationController.Init();
        log.info("Initialisation of C3H6N6O6World is complete.");
    }
}
