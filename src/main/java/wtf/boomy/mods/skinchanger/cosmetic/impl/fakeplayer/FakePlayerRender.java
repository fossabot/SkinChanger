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

package wtf.boomy.mods.skinchanger.cosmetic.impl.fakeplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import wtf.boomy.mods.skinchanger.cosmetic.CosmeticFactory;

/**
 * Cosmetic class for the Fake Player renderer
 *
 * @author boomboompower
 * @since 3.0.0
 */
public class FakePlayerRender {
    
    // The player should be the same across every instance and every thread.
    private static final FakePlayer fakePlayer = new FakePlayer(Minecraft.getMinecraft().thePlayer);
    
    private final CosmeticFactory cosmeticFactory;
    
    /**
     * Simple constructor for the FakePlayer renderer
     *
     * @param factory the cosmetic factory this is part of
     */
    public FakePlayerRender(CosmeticFactory factory) {
        this.cosmeticFactory = factory;
    }
    
    /**
     * Renders the FakePlayer onto the screen
     *
     * @param posX     the x position of the entity
     * @param posY     the y position of the entity
     * @param scale    the scale of the entity
     * @param rotation the rotation of the entity
     */
    public void renderFakePlayer(int posX, int posY, int scale, float rotation) {
        FakePlayer entity = fakePlayer;
        
        // Stops entity clipping behind the screen
        GlStateManager.translate(0, 0, 100);
        
        GlStateManager.enableColorMaterial();
        
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
        GlStateManager.pushMatrix();
        
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
        
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        
        // Rotates based on the rotation variable
        GlStateManager.rotate(rotation, 0F, 270F, 0F);
        
        // Store original values
        float prevYawOffset = entity.renderYawOffset;
        float prevYaw = entity.rotationYaw;
        float prevPitch = entity.rotationPitch;
        float prevYawRotation = entity.prevRotationYawHead;
        float prevHeadRotation = entity.rotationYawHead;
        
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        
        RenderHelper.enableStandardItemLighting();
        
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
        
        entity.renderYawOffset = 0.0F;
        entity.rotationYaw = 0.0F;
        entity.rotationPitch = 0.0F;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;
        
        entity.prevChasingPosX = 2;
        entity.chasingPosX = 0;
        
        entity.prevChasingPosY = 0;
        entity.chasingPosY = 0;
        
        entity.prevChasingPosZ = 0;
        entity.chasingPosZ = 0;
        
        entity.prevRenderYawOffset = 0;
        entity.prevCameraYaw = 0;
        
        entity.prevDistanceWalkedModified = 1;
        entity.distanceWalkedModified = 0;
        
        // Simulate player movement
        if (this.cosmeticFactory.getMod().getConfigurationHandler().isUsingAnimatedPlayer()) {
            entity.limbSwingAmount += (0.6F - entity.limbSwingAmount) * 0.4F;
            entity.limbSwing += (entity.limbSwingAmount) / 6;
        }
        
        entity.prevPosX = 0;
        entity.posX = 0;
        
        entity.prevPosY = 0;
        entity.posY = 0;
        
        entity.prevPosZ = entity.posZ;
        
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    
        if (this.cosmeticFactory.getMod().getConfigurationHandler().isUsingAnimatedCape()) {
            float capeSwing = MathHelper.cos(entity.limbSwing / 2 * 0.662F) * 1.1F * entity.limbSwingAmount / 2;
    
            entity.posZ = lerp(0, capeSwing, 0.5F);
            entity.posZ += 0.5;
        } else {
            entity.posZ = 0;
        }
        
        GlStateManager.disableLighting();
        
        rendermanager.setPlayerViewY(rotation);
        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, true);
        rendermanager.setRenderShadow(true);
        
        GlStateManager.enableLighting();
        
        entity.renderYawOffset = prevYawOffset;
        entity.rotationYaw = prevYaw;
        entity.rotationPitch = prevPitch;
        entity.prevRotationYawHead = prevYawRotation;
        entity.rotationYawHead = prevHeadRotation;
        
        GlStateManager.popMatrix();
        
        RenderHelper.disableStandardItemLighting();
        
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    /**
     * Returns the location of the current FakePlayer skin
     *
     * @return the ResourceLocation of the current skin
     */
    public ResourceLocation getSkinLocation() {
        return fakePlayer.getPlayerInfo().getLocationSkin();
    }
    
    /**
     * Sets the cape of the FakePlayer
     *
     * @param skin the skin which should be applied.
     */
    public void setSkinLocation(ResourceLocation skin) {
        fakePlayer.getPlayerInfo().setLocationSkin(skin);
    }
    
    /**
     * Returns the location of the current FakePlayer cape
     *
     * @return the ResourceLocation of the current cape
     */
    public ResourceLocation getCapeLocation() {
        return fakePlayer.getPlayerInfo().getLocationCape();
    }
    
    /**
     * Sets the cape of the FakePlayer
     *
     * @param cape the cape which should be applied.
     */
    public void setCapeLocation(ResourceLocation cape) {
        fakePlayer.getPlayerInfo().setLocationCape(cape);
    }
    
    /**
     * Returns the current Skin type (either "default" or "slim")
     *
     * @return the skin type of the FakePlayer
     */
    public String getSkinType() {
        return fakePlayer.getPlayerInfo().getSkinType();
    }
    
    /**
     * Sets the Skin type of the FakePlayer
     * should either be "default" or "slim"
     *
     * @param in the skin type which should be set
     */
    public void setSkinType(String in) {
        fakePlayer.getPlayerInfo().setSkinType(in);
    }
    
    /**
     * Copies the resources to the FakePlayer
     *
     * @param skin     the skin tp copy
     * @param cape     the cape to copy
     * @param skinType the skin type to copy
     */
    public void copyFrom(ResourceLocation skin, ResourceLocation cape, String skinType) {
        fakePlayer.copyFrom(skin, cape, skinType);
    }
    
    /**
     * Copies resources from one player to another.
     *
     * @param player the player to copy resources from.
     */
    public void copyFrom(AbstractClientPlayer player) {
        fakePlayer.copyFrom(player);
    }
    
    /**
     * Gets the fake player instance
     *
     * @return the fake player instance
     */
    public FakePlayer getFakePlayer() {
        return fakePlayer;
    }
    
    /**
     * Simple linear interpolation.
     * <p>
     * https://en.wikipedia.org/wiki/Linear_interpolation
     * <p>
     * When alpha = 1, it will return point2
     * When alpha = 0, it will return point1
     *
     * @param point1 the first point
     * @param point2 the second point
     * @param alpha  the alpha
     *
     * @return the lerp value of the point
     */
    @SuppressWarnings("SameParameterValue")
    private float lerp(float point1, float point2, float alpha) {
        return (1 - alpha) * point1 + alpha * point2;
    }
    
    public CosmeticFactory getCosmeticFactory() {
        return this.cosmeticFactory;
    }
}
