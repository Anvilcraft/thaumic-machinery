package net.anvilcraft.thaummach.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelDuplicator extends ModelBase {
    ModelRenderer press1;
    ModelRenderer piston1;

    public ModelDuplicator() {
        super.textureWidth = 64;
        super.textureHeight = 32;
        this.press1 = new ModelRenderer(this, 0, 0);
        this.press1.addBox(-4.0F, 0.0F, -4.0F, 8, 4, 8);
        this.press1.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.press1.setTextureSize(64, 32);
        this.press1.mirror = true;
        this.setRotation(this.press1, 0.0F, 0.0F, 0.0F);
        this.piston1 = new ModelRenderer(this, 0, 12);
        this.piston1.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4);
        this.piston1.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.piston1.setTextureSize(64, 32);
        this.piston1.mirror = true;
        this.setRotation(this.piston1, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.press1.render(f5);
        this.piston1.render(f5);
    }

    public void render() {
        this.press1.render(0.0625F);
        this.piston1.render(0.0625F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
