package net.anvilcraft.thaummach.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPump extends ModelBase {
    public ModelRenderer front;
    public ModelRenderer moveBase;
    public ModelRenderer moveFrill;
    public ModelRenderer center;
    public ModelRenderer back;

    public ModelPump() {
        super.textureWidth = 128;
        super.textureHeight = 64;
        this.front = new ModelRenderer(this, 0, 0);
        this.front.addBox(0.0F, 0.0F, 0.0F, 12, 6, 12);
        this.front.setRotationPoint(-6.0F, 18.0F, -6.0F);
        this.front.setTextureSize(128, 64);
        this.front.mirror = true;
        this.setRotation(this.front, 0.0F, 0.0F, 0.0F);
        this.moveBase = new ModelRenderer(this, 0, 18);
        this.moveBase.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12);
        this.moveBase.setRotationPoint(-6.0F, 8.0F, -6.0F);
        this.moveBase.setTextureSize(128, 64);
        this.moveBase.mirror = true;
        this.setRotation(this.moveBase, 0.0F, 0.0F, 0.0F);
        this.moveFrill = new ModelRenderer(this, 0, 32);
        this.moveFrill.addBox(0.0F, 0.0F, 0.0F, 10, 4, 10);
        this.moveFrill.setRotationPoint(-5.0F, 10.0F, -5.0F);
        this.moveFrill.setTextureSize(128, 64);
        this.moveFrill.mirror = true;
        this.setRotation(this.moveFrill, 0.0F, 0.0F, 0.0F);
        this.center = new ModelRenderer(this, 48, 0);
        this.center.addBox(0.0F, 0.0F, 0.0F, 16, 4, 16);
        this.center.setRotationPoint(-8.0F, 14.0F, -8.0F);
        this.center.setTextureSize(128, 64);
        this.center.mirror = true;
        this.setRotation(this.center, 0.0F, 0.0F, 0.0F);
        this.back = new ModelRenderer(this, 0, 46);
        this.back.addBox(0.0F, 0.0F, 0.0F, 6, 6, 6);
        this.back.setRotationPoint(-3.0F, 8.0F, -3.0F);
        this.back.setTextureSize(128, 64);
        this.back.mirror = true;
        this.setRotation(this.back, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void
    render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.front.render(f5);
        this.moveBase.render(f5);
        this.moveFrill.render(f5);
        this.center.render(f5);
        this.back.render(f5);
    }

    public void render() {
        this.front.render(0.0625F);
        this.center.render(0.0625F);
        this.back.render(0.0625F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
