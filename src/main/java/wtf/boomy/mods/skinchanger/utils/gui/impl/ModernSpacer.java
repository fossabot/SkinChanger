/*
 *     Copyright (C) 2020 boomboompower
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package wtf.boomy.mods.skinchanger.utils.gui.impl;

import java.awt.Color;

import wtf.boomy.mods.skinchanger.utils.gui.ModernUIElement;
import wtf.boomy.mods.skinchanger.utils.gui.ModernGui;
import wtf.boomy.mods.skinchanger.utils.gui.StartEndUIElement;

import org.lwjgl.input.Keyboard;

/**
 * Provides spacing between elements. Similar to a "break" tag in html
 *
 * @since 3.0.0
 */
public class ModernSpacer implements ModernUIElement, StartEndUIElement {
    
    private int x;
    private int y;
    
    private boolean partOfHeader;
    
    public ModernSpacer(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public int getWidth() {
        return 5;
    }
    
    @Override
    public int getHeight() {
        return 5;
    }
    
    @Override
    public void render(int mouseX, int mouseY, float yTranslation) {
    }
    
    @Override
    public void renderFromHeader(int xPos, int yPos, float yTranslation, int mouseX, int mouseY, int recommendedYOffset) {
        // Debug
        if (Keyboard.isKeyDown(Keyboard.KEY_G) && Keyboard.isKeyDown(Keyboard.KEY_H)) {
            ModernGui.drawRect(
                    xPos + this.x,
                    yPos + recommendedYOffset + this.y,
                    xPos + this.x + this.getWidth(),
                    this.y + yPos + recommendedYOffset + 5,
                    new Color(0.5F, 0.5F, 0.5F, 0.5F).getRGB());
        }
    }
    
    @Override
    public boolean isEnabled() {
        return this.partOfHeader;
    }
    
    @Override
    public void setAsPartOfHeader(ModernHeader parent) {
        this.partOfHeader = true;
        
    }
    
    @Override
    public int getX() {
        return this.x;
    }
    
    @Override
    public int getY() {
        return this.y;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
}
