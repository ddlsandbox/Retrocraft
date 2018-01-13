package com.retrocraft.entity.teleportpipe.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelTeleportPipe extends ModelBase
{

  private ModelRenderer top;
  private ModelRenderer baseBottom;

  public ModelTeleportPipe()
  {
    textureWidth = 256;
    textureHeight = 256;

    top = new ModelRenderer(this, 0, 0);
    top.addBox(-16f, -32f, -16f, 32, 10, 32);
    
    baseBottom = new ModelRenderer(this, 0, 43);
    baseBottom.addBox(-10f, -22f, -10f, 20, 22, 20);
  }

  public void renderAll()
  {
    float f = 0.0625f;
    top.render(f);
    baseBottom.render(f);
  }

  public void renderPillar()
  {
    float f = 0.0625f;
    baseBottom.render(f);
  }
}