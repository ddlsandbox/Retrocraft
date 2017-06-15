package com.retrocraft.client.renderer;

import org.lwjgl.opengl.GL11;

import com.retrocraft.RetroCraft;
import com.retrocraft.client.model.HammerModel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class HammerRenderer extends ModelRenderer {

    public HammerModel model;
    private String textureName;
    private ModelBase base;
    
    public HammerRenderer(ModelBase base, String textureName) {
    	super(base, textureName);
    	
        this.model = new HammerModel();
        this.textureName = textureName;
        this.base = base;
    }

    @Override
    public void render(float scale) {
    	renderWithRotation(scale);
    }
    
    @Override
    public void renderWithRotation(float scale) {
        GL11.glPushMatrix();
//        switch (type) {
//            case EQUIPPED:
                GL11.glScalef(-2.5f, -2.5f, 2.5f);
                GL11.glTranslatef(0.25f, -0.75f, -0.575f);
                GL11.glRotatef(-90.0f,-1.0f,1.0f,0.0f);
//                break;
//            case EQUIPPED_FIRST_PERSON:
//                GL11.glScalef(2.0f,2.0f,2.0f);
//                GL11.glTranslatef(0.1f, 1.5f, 0.2f);
//                GL11.glRotatef(180, 1.0f, 0.0f, 1.0f);
//                GL11.glRotatef(130, 0.0f, 1.0f, 0.0f);
//                break;
//            case INVENTORY:
//                GL11.glScalef(1.5f,1.25f,1.5f);
//                GL11.glTranslatef(0,0.9f,0);
//                GL11.glRotatef(180, 1.0f, 0.0f, 1.0f);
//                break;
//            case ENTITY:
//                GL11.glScalef(1.5f, 1.5f, 1.5f);
//                GL11.glTranslatef(0, 1.5f, 0);
//                GL11.glRotatef(180, 1.0f, 0.0f, 1.0f);
//                break;
//        }
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(RetroCraft.modId + ":" + "textures/models/" + textureName + ".png"));
        this.model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
}