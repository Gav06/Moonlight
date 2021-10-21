package dev.moonlight.ui.clickgui.comps;

import dev.moonlight.Moonlight;
import dev.moonlight.module.Module;
import dev.moonlight.ui.clickgui.api.DragComponent;
import net.minecraft.client.gui.Gui;

import java.util.ArrayList;
import java.util.List;

public class CategoryComponent extends DragComponent {
    public Module.Category category;
    public List<ModuleButton> moduleButtons = new ArrayList<>();

    public CategoryComponent(Module.Category category, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.category = category;
        this.width = 100;
        this.height = 150;
        for(Module module : Moonlight.INSTANCE.getModuleManager().getModuleList()) {
            moduleButtons.add(new ModuleButton(module, x, y, width, height));
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        Moonlight.INSTANCE.getFontRenderer().drawStringWithShadow(category.toString(), x + 2, y + 2, -1);
        Gui.drawRect(x, y, x + width, y + height, 0x90000000);
        for(ModuleButton button : moduleButtons) {
            button.draw(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void click(int x, int y, int moduleButton) {
        for(ModuleButton button : moduleButtons) {
            button.click(x, y, moduleButton);
        }
    }
}
