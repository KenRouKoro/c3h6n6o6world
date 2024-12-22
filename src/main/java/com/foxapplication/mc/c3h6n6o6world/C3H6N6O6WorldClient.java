package com.foxapplication.mc.c3h6n6o6world;

import com.foxapplication.mc.c3h6n6o6world.thread.CalculationController;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.api.ClientModInitializer;
@Slf4j
public class C3H6N6O6WorldClient  implements ClientModInitializer {
    /**
     * 入口
     */
    @Override
    public void onInitializeClient() {
        log.info("黑索金检测到在客户端运行，将使用客户端策略！");
        CalculationController.getClient().set(true);

    }
}
