package net.anvilcraft.thaummach.render;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import net.anvilcraft.thaummach.RuneTileData;
import net.anvilcraft.thaummach.tiles.TileSeal;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

public class PortalRenderer {
    public static Set<PortalRenderer> INSTANCES = new HashSet<>();

    public int portalTexture;
    public int depthRenderBuffer;
    public int frameBuffer;
    public TileSeal seal;

    public PortalRenderer(TileSeal seal) {
        this.frameBuffer = EXTFramebufferObject.glGenFramebuffersEXT();
        this.portalTexture = GL11.glGenTextures();
        this.depthRenderBuffer = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
        GL11.glBindTexture(3553, this.portalTexture);
        GL11.glTexParameterf(3553, 10241, 9729.0F);
        GL11.glTexImage2D(3553, 0, 32856, 512, 512, 0, 6408, 5124, (ByteBuffer) null);
        EXTFramebufferObject.glFramebufferTexture2DEXT(
            36160, 36064, 3553, this.portalTexture, 0
        );
        EXTFramebufferObject.glBindRenderbufferEXT(36161, this.depthRenderBuffer);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 35056, 512, 512);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(
            36160, 33306, 36161, this.depthRenderBuffer
        );
        EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
        this.seal = seal;
        INSTANCES.add(this);
    }

    public void deinit() {
        // TODO: WTF
        //GL11.glDeleteFramebuffers(this.frameBuffer);
        GL11.glDeleteTextures(this.portalTexture);
        INSTANCES.remove(this);
    }

    public void createPortalView() {
        if (this.seal.otherSeal == null)
            return;

        RuneTileData target = this.seal.otherSeal;
        Minecraft mc = Minecraft.getMinecraft();
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        int prevFbo = GL11.glGetInteger(EXTFramebufferObject.GL_FRAMEBUFFER_BINDING_EXT);
        EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);

        // TODO: stencils aren't stenciling
        GL11.glEnable(2960);
        GL11.glStencilFunc(519, 1, 1);
        GL11.glStencilOp(7680, 7680, 7681);
        GL11.glViewport(0, 0, 512, 512);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glDisable(3553);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glBegin(6);
        GL11.glVertex2f(0.0F, 0.0F);

        for (int oh = 0; oh <= 10; ++oh) {
            double aa = 6.283185307179586 * (double) oh / 10.0;
            GL11.glVertex2f((float) Math.cos(aa), (float) Math.sin(aa));
        }

        GL11.glEnd();
        GL11.glStencilFunc(514, 1, 1);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glEnable(3553);
        Entity rve = mc.renderViewEntity;

        mc.renderViewEntity = new EntityPlayer(
            mc.theWorld, mc.getSession().func_148256_e()
        ) {
            @Override
            public void addChatMessage(IChatComponent p_145747_1_) {}

            @Override
            public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
                return false;
            }

            @Override
            public ChunkCoordinates getPlayerCoordinates() {
                return null;
            }
        };

        int orientation = target.orientation;
        float yaw = 0.0f;
        float pitch = 0.0f;
        switch (orientation) {
            case 0: {
                pitch = 90.0f;
                break;
            }
            case 1: {
                pitch = -90.0f;
                break;
            }
            case 2: {
                yaw = 180.0f;
                break;
            }
            case 3: {
                yaw = 0.0f;
                break;
            }
            case 4: {
                yaw = 90.0f;
                break;
            }
            case 5: {
                yaw = 270.0f;
                break;
            }
        }
        int xm = 0;
        int zm = 0;
        int ym = 0;
        switch (orientation) {
            case 0: {
                ym = -1;
                break;
            }
            case 1: {
                ym = 1;
                break;
            }
            case 2: {
                zm = -1;
                break;
            }
            case 3: {
                zm = 1;
                break;
            }
            case 4: {
                xm = -1;
                break;
            }
            case 5: {
                xm = 1;
                break;
            }
        }
        mc.renderViewEntity.setPositionAndRotation(
            target.x + 0.5 + xm, target.y + 0.5f + ym, target.z + 0.5 + zm, yaw, pitch
        );
        final boolean di = mc.gameSettings.showDebugInfo;
        mc.gameSettings.showDebugInfo = false;
        final float fov = mc.gameSettings.fovSetting;
        final int width = mc.displayWidth;
        final int height = mc.displayHeight;
        int tpv = mc.gameSettings.thirdPersonView;
        mc.displayWidth = 512;
        mc.displayHeight = 512;
        mc.gameSettings.thirdPersonView = 0;
        mc.gameSettings.fovSetting = 120.0f;
        mc.renderViewEntity.rotationYaw = yaw;
        mc.renderViewEntity.rotationPitch = pitch;
        final boolean hg = mc.gameSettings.hideGUI;
        mc.gameSettings.hideGUI = true;
        mc.entityRenderer.renderWorld(1F, 0L);
        mc.renderViewEntity = (EntityLivingBase) rve;
        mc.displayWidth = width;
        mc.displayHeight = height;
        mc.gameSettings.showDebugInfo = di;
        mc.gameSettings.hideGUI = hg;
        mc.gameSettings.fovSetting = fov;
        mc.gameSettings.thirdPersonView = tpv;
        GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
        GL11.glDisable(2960);
        EXTFramebufferObject.glBindFramebufferEXT(36160, prevFbo);
        GL11.glPopMatrix();
    }
}
