package xyz.phanta.ae2fc.inventory.slot;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class SlotSingleItem extends Slot {

    private final Slot delegate;

    public SlotSingleItem(Slot delegate) {
        super(delegate.inventory, delegate.getSlotIndex(), delegate.xPos, delegate.yPos);
        this.delegate = delegate;
    }

    @Override
    public ItemStack getStack() {
        ItemStack stack = delegate.getStack();
        return stack.isEmpty() ? ItemStack.EMPTY : ItemHandlerHelper.copyStackWithSize(stack, 1);
    }

    // delegated

    @Override
    public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_) {
        delegate.onSlotChange(p_75220_1_, p_75220_2_);
    }

    @Override
    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
        return delegate.onTake(thePlayer, stack);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return delegate.isItemValid(stack);
    }

    @Override
    public boolean getHasStack() {
        return delegate.getHasStack();
    }

    @Override
    public void putStack(ItemStack stack) {
        delegate.putStack(stack);
    }

    @Override
    public void onSlotChanged() {
        delegate.onSlotChanged();
    }

    @Override
    public int getSlotStackLimit() {
        return delegate.getSlotStackLimit();
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return delegate.getItemStackLimit(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nullable
    public String getSlotTexture() {
        return delegate.getSlotTexture();
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        return delegate.decrStackSize(amount);
    }

    @Override
    public boolean isHere(IInventory inv, int slotIn) {
        return delegate.isHere(inv, slotIn);
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return delegate.canTakeStack(playerIn);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isEnabled() {
        return delegate.isEnabled();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getBackgroundLocation() {
        return delegate.getBackgroundLocation();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setBackgroundLocation(ResourceLocation texture) {
        delegate.setBackgroundLocation(texture);
    }

    @Override
    public void setBackgroundName(@Nullable String name) {
        delegate.setBackgroundName(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nullable
    public TextureAtlasSprite getBackgroundSprite() {
        return delegate.getBackgroundSprite();
    }

    @Override
    public int getSlotIndex() {
        return delegate.getSlotIndex();
    }

    @Override
    public boolean isSameInventory(Slot other) {
        return delegate.isSameInventory(other);
    }

}
