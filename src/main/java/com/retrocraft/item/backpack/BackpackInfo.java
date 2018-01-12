package com.retrocraft.item.backpack;

import java.util.UUID;

import com.google.common.base.Preconditions;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

/**
 * The main class to hold info about a backpack.
 *
 * Contains {@link BackpackVariant}, a {@code List<{@link BackpackUpgrade}>},
 * an {@link IBackpackInventoryProvider} and an {@link UUID} of the owner.
 *
 * Also contains simple getters/setters for the above.
 *
 * Finally, has some additional methods, such as NBT serialization and deserialization.
 */
public class BackpackInfo implements INBTSerializable<NBTTagCompound> {

    private IItemHandlerModifiable inventory;
    private UUID owner;
    
    public static final int BACKPACK_SIZE = 45;
    public static final int BACKPACK_ROWS = 5;
    public static final int BACKPACK_COLS = 9;

    // Constructors

    private BackpackInfo() {
    }

    public IItemHandlerModifiable getInventory() {
        return inventory;
    }

    public BackpackInfo setInventory(IItemHandlerModifiable inventory) {
        this.inventory = inventory;
        return this;
    }

    /**
     * Returns the {@link UUID} of the owner.
     * WARNING: May be NULL if no owner is assigned.
     *
     * @return - The UUID of the owner
     */
    public UUID getOwner() {
        return owner;
    }

    /**
     * Sets the {@link UUID} of the owner to this backpack.
     * WARNING: May be NULL for no owner.
     *
     * @param owner - The UUID to set
     * @return - The updated backpack info
     */
    public BackpackInfo setOwner(UUID owner) {
        this.owner = owner;
        return this;
    }

    // INBTSerializable

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();

        if (owner != null)
            tag.setString("own", owner.toString());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("own"))
            owner = UUID.fromString(nbt.getString("own"));
    }

    public static BackpackInfo fromStack(ItemStack stack) {
        Preconditions.checkNotNull(stack, "ItemStack cannot be null");

        if (stack.isEmpty() || !stack.hasTagCompound() || !stack.getTagCompound().hasKey("packInfo"))
            return new BackpackInfo();

        BackpackInfo tagged = fromTag(stack.getTagCompound().getCompoundTag("packInfo"));

        ItemStackHandler stackHandler = new ItemStackHandler(BACKPACK_SIZE);
        NBTTagList tagList = stack.getTagCompound().getTagList("packInv", 10);
        for (int i = 0; i < tagList.tagCount(); i++)
            stackHandler.setStackInSlot(i, new ItemStack(tagList.getCompoundTagAt(i)));

        return tagged.setInventory(stackHandler);
    }

    public static BackpackInfo fromTag(NBTTagCompound tag) {
        BackpackInfo backpackInfo = new BackpackInfo();
        if (tag == null || tag.hasNoTags())
            return backpackInfo;

        backpackInfo.deserializeNBT(tag);
        return backpackInfo;
    }
}
