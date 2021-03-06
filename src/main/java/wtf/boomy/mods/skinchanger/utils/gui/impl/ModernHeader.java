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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import wtf.boomy.mods.skinchanger.utils.general.Prerequisites;
import wtf.boomy.mods.skinchanger.utils.gui.InteractiveUIElement;
import wtf.boomy.mods.skinchanger.utils.gui.ModernUIElement;
import wtf.boomy.mods.skinchanger.utils.gui.ModernGui;
import wtf.boomy.mods.skinchanger.utils.gui.StartEndUIElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

/**
 * A simple class for drawing headers. Useful for UI categories.
 *
 * @author boomboompower
 * @version 1.1
 */
public class ModernHeader extends Gui implements InteractiveUIElement {
    
    private int x;
    
    private int y;
    
    private final String headerText;
    
    private float scaleSize;
    
    private boolean drawUnderline;
    private boolean visible = true;
    private boolean drawCentered = false;
    
    private Color headerColor;
    
    private final List<ModernUIElement> children;
    
    private float offsetBetweenChildren = 12;
    
    private final ModernGui modernGui;
    
    private ScaledResolution scaledResolution;
    
    private int widthOfSub;
    private int heightOfSub;
    private int furthestSubX;
    
    /**
     * Basic constructor for UI headers. Scale size is 1.5 of normal text. Draws an underline.
     *
     * @param gui    the parent of this header
     * @param x      the x location of the header
     * @param y      the y location of the header
     * @param header the text which will be rendered
     */
    public ModernHeader(ModernGui gui, int x, int y, String header) {
        this(gui, x, y, header, 1.5F, true);
    }
    
    /**
     * Basic constructor for UI headers. Draws an underline.
     *
     * @param gui       the parent of this header
     * @param x         the x location of the header
     * @param y         the y location of the header
     * @param header    the text which will be rendered
     * @param scaleSize the scale of the text. (scale greater than 1 means bigger)
     */
    public ModernHeader(ModernGui gui, int x, int y, String header, float scaleSize) {
        this(gui, x, y, header, scaleSize, true);
    }
    
    /**
     * Basic constructor for UI headers. Draws an underline.
     *
     * @param gui       the parent of this header
     * @param x         the x location of the header
     * @param y         the y location of the header
     * @param header    the text which will be rendered
     * @param scaleSize the scale of the text. (scale greater than 1 means bigger)
     */
    public ModernHeader(ModernGui gui, int x, int y, String header, int scaleSize) {
        this(gui, x, y, header, scaleSize, true);
    }
    
    /**
     * Basic constructor for UI headers.
     *
     * @param gui           the parent of this header
     * @param x             the x location of the header
     * @param y             the y location of the header
     * @param header        the text which will be rendered
     * @param scaleSize     the scale of the text. (scale greater than 1 means bigger)
     * @param drawUnderline true if an underline should be drawn.
     */
    public ModernHeader(ModernGui gui, int x, int y, String header, float scaleSize, boolean drawUnderline) {
        this(gui, x, y, header, scaleSize, drawUnderline, Color.WHITE);
    }
    
    /**
     * Basic constructor for UI headers.
     *
     * @param gui           the parent of this header
     * @param x             the x location of the header
     * @param y             the y location of the header
     * @param header        the text which will be rendered
     * @param scaleSize     the scale of the text. (scale > 1 means bigger)
     * @param drawUnderline true if an underline should be drawn.
     * @param color         the color of which the elements will be drawn.
     */
    public ModernHeader(ModernGui gui, int x, int y, String header, float scaleSize, boolean drawUnderline, Color color) {
        Prerequisites.notNull(gui);
        Prerequisites.notNull(header);
        Prerequisites.conditionMet(scaleSize > 0, "Scale cannot be less than 0");
        
        this.modernGui = gui;
        
        this.x = x;
        this.y = y;
        
        this.headerText = header;
        this.scaleSize = scaleSize;
        this.drawUnderline = drawUnderline;
        this.headerColor = color;
        
        this.scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        this.children = new ArrayList<>();
        
        this.widthOfSub = (int) (Minecraft.getMinecraft().fontRendererObj.getStringWidth(header) * this.scaleSize);
        this.heightOfSub = (int) (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * this.scaleSize);
        this.furthestSubX = 2;
    }
    
    @Override
    public void render(int mouseX, int mouseY, float yTranslation) {
        if (!this.visible) {
            return;
        }
        
        ScaledResolution resolution = getScaledResolution();
        
        if (resolution == null) {
            return;
        }
        
        // Retrieve the renderer.
        FontRenderer fontRenderer = getFontRenderer();
        
        // Ensure minecraft actually has a font renderer we can use.
        if (fontRenderer == null) {
            return;
        }
        
        int endingBoxY = this.y + this.heightOfSub + 10;
        
        if (this.drawUnderline) {
            endingBoxY += 5;
        }
        
        if (this.children.isEmpty()) {
            endingBoxY -= 8;
        }
        
        // The background box of the component
        // ModernGui.drawRect(this.x - 2, this.y - 2, this.x + this.widthOfSub + this.furthestSubX, endingBoxY, new Color(0.7F, 0.7F, 0.7F, 0.2F).getRGB());
        
        // Push the stack, making our own GL sandbox.
        GlStateManager.pushMatrix();
        
        // Reset the colors.
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
        //GlStateManager.scale(-Minecraft.getMinecraft().gameSettings.guiScale,  -Minecraft.getMinecraft().gameSettings.guiScale, 0F);
        
        // Scales it up
        GlStateManager.scale(this.scaleSize, this.scaleSize, 0F);
        
        float xPos = this.x;/// Minecraft.getMinecraft().gameSettings.guiScale;
        float yPos = this.y;/// Minecraft.getMinecraft().gameSettings.guiScale;
        
        if (this.drawCentered) {
            // Draws the text
            fontRenderer.drawString(this.headerText, (xPos / this.scaleSize) - (getWidth() / this.scaleSize), yPos / this.scaleSize, this.headerColor.getRGB(), false);
        } else {
            // Draws the text
            fontRenderer.drawString(this.headerText, xPos / this.scaleSize, yPos / this.scaleSize, this.headerColor.getRGB(), false);
        }
        
        // Check if the header should have an underline or not.
        if (this.drawUnderline) {
            drawHorizontalLine((int) (xPos / this.scaleSize), (int) ((xPos + (getWidth() * this.scaleSize)) / this.scaleSize), (int) ((yPos + (12 * this.scaleSize)) / this.scaleSize), this.headerColor.getRGB());
        }
        
        // Pop the changes to the gl stack.
        GlStateManager.popMatrix();
        
        if (this.children.size() > 0) {
            float yOffset = (12 * this.scaleSize) + this.offsetBetweenChildren / 2;
            
            for (ModernUIElement child : this.children) {
                // Renders relative to this headers position.
                if (child.renderRelativeToHeader()) {
                    GlStateManager.pushMatrix();
        
                    child.renderFromHeader((int) xPos, (int) yPos, yTranslation, mouseX, mouseY, (int) yOffset);
        
                    GlStateManager.popMatrix();
                } else {
                    child.render(mouseX, mouseY, yTranslation);
                }
                
                if (child instanceof StartEndUIElement) {
                    yOffset += ((StartEndUIElement) child).getHeight() + 4;
                } else {
                    yOffset += this.offsetBetweenChildren;
                }
            }
        }
    }
    
    @Override
    public boolean isInside(int mouseX, int mouseY, float yTranslation) {
        ModernGui.drawRect(this.x, this.y, this.x + getWidthOfHeader(), this.y + getHeightOfHeader(), new Color(0.5F, 0.5F, 0.5F, 0.5F).getRGB());
        
        return this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + getWidthOfHeader() && mouseY < this.y + getHeightOfHeader();
    }
    
    @Override
    public boolean isEnabled() {
        return this.visible;
    }
    
    @Override
    public void setAsPartOfHeader(ModernHeader parent) {
        throw new IllegalStateException("A header cannot be placed in a header");
    }
    
    @Override
    public void renderFromHeader(int xPos, int yPos, float yTranslation, int mouseX, int mouseY, int recommendedYOffset) {
        throw new IllegalStateException("A header cannot be placed in a header");
    }
    
    @Override
    public boolean renderRelativeToHeader() {
        return false;
    }
    
    /**
     * The width of this string based off the FontRenderer's width calculations.
     *
     * @return the width of the string.
     */
    @Override
    public int getWidth() {
        if (getFontRenderer() == null) {
            return 0;
        }
        
        return getFontRenderer().getStringWidth(this.headerText);
    }
    
    /**
     * Retrieves the minecraft font renderer if one exists, or null if not applicable
     *
     * @return the minecraft font renderer.
     */
    private FontRenderer getFontRenderer() {
        if (Minecraft.getMinecraft() == null) {
            return null;
        }
        
        return Minecraft.getMinecraft().fontRendererObj;
    }
    
    /**
     * Retrieves Minecraft's scaled resolution.
     *
     * @return scaled resolution.
     */
    private ScaledResolution getScaledResolution() {
        if (this.scaledResolution != null) {
            return this.scaledResolution;
        }
        
        if (Minecraft.getMinecraft() == null) {
            return null;
        }
        
        this.scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        
        return this.scaledResolution;
    }
    
    /**
     * Force updates the resolution.
     */
    public void updateResolution() {
        this.scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    }
    
    public int getWidthOfHeader() {
        return this.widthOfSub;
    }
    
    public int getHeightOfHeader() {
        return this.heightOfSub;
    }
    
    public void addChild(ModernUIElement element) {
        element.setAsPartOfHeader(this);
        
        if (element.renderRelativeToHeader()) {
            if (element.getX() > this.furthestSubX) {
                this.furthestSubX = element.getX();
            }
            
            if (element.getWidth() + element.getX() > this.widthOfSub) {
                this.widthOfSub = element.getWidth() + element.getX() + 2;
            }
        } else if (element.getWidth() > this.widthOfSub) {
            this.widthOfSub = element.getWidth() + 2;
        }
        
        if (element instanceof StartEndUIElement) {
            StartEndUIElement startEnd = (StartEndUIElement) element;
            
            this.heightOfSub += startEnd.getHeight() + 4;
        } else {
            this.heightOfSub += Objects.requireNonNull(getFontRenderer()).FONT_HEIGHT;
        }
        
        this.children.add(element);
    }
    
    @Override
    public void onLeftClick(int mouseX, int mouseY, float yTranslation) {
        if (this.children.size() > 0) {
            for (ModernUIElement child : this.children) {
                // Special case :V
                if (child instanceof ModernButton) {
                    ModernButton button = (ModernButton) child;
                    
                    if (!child.isEnabled()) {
                        continue;
                    }
                    
                    if (button.isHovered()) {
                        button.onLeftClick(mouseX, mouseY, yTranslation);
                        
                        if (this.modernGui != null) {
                            this.modernGui.buttonPressed(button);
                        }
                    }
                } else if (child instanceof InteractiveUIElement) {
                    InteractiveUIElement interactive = (InteractiveUIElement) child;
                    
                    if (interactive.isInside(mouseX, mouseY, yTranslation)) {
                        interactive.onLeftClick(mouseX, mouseY, yTranslation);
                    }
                }
            }
        }
    }
    
    /**
     * Sets all the children to this enabled state o.o
     *
     * @param enabled true if the elements should be enabled.
     */
    public void setEnabled(boolean enabled) {
        for (ModernUIElement element : this.children) {
            if (element instanceof ModernButton) {
                ((ModernButton) element).setEnabled(enabled);
            } else if (element instanceof ModernTextBox) {
                ((ModernTextBox) element).setEnabled(enabled);
            } else if (element instanceof ModernSlider) {
                ((ModernSlider) element).setEnabled(enabled);
            } else if (element instanceof ModernCheckbox) {
                ((ModernCheckbox) element).setEnabled(enabled);
            }
        }
    }
    
    @Override
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    @Override
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public String getHeaderText() {
        return this.headerText;
    }
    
    public float getScaleSize() {
        return scaleSize;
    }
    
    public void setScaleSize(float scaleSize) {
        this.scaleSize = scaleSize;
    }
    
    public float getOffsetBetweenChildren() {
        return offsetBetweenChildren;
    }
    
    public void setOffsetBetweenChildren(float offsetBetweenChildren) {
        this.offsetBetweenChildren = offsetBetweenChildren;
    }
    
    public boolean isDrawCentered() {
        return drawCentered;
    }
    
    public void setDrawCentered(boolean drawCentered) {
        this.drawCentered = drawCentered;
    }
    
    public boolean isDrawUnderline() {
        return drawUnderline;
    }
    
    public void setDrawUnderline(boolean drawUnderline) {
        this.drawUnderline = drawUnderline;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public Color getHeaderColor() {
        return headerColor;
    }
    
    public void setHeaderColor(Color headerColor) {
        this.headerColor = headerColor;
    }
    
    /* Some good scales.
     *
     * Largest  2.0
     * Larger   1.58
     * Large    1.27 (a bit weird)
     * Skinny   1.24 (larger skinny text)
     * Skinny   1.05 (normal skinny text)
     * Normal   1.0
     * Fat      0.94 (only k's are slightly distorted, seems fat).
     * Skinny   0.75 (a bit distorted, but skinny)
     * Half     0.5 (lowest scale where text is not distorted)
     */
}
