package network.nishi.interiorextensions.Entities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityZabuton extends Entity {
    public EntityZabuton(World worldIn) {
        super(worldIn);
        this.setSize(0.0f,0.0f);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

    public void onUpdate() {
        // 座ってなかったらEntity消す
        if(!this.isBeingRidden()) {
            this.getEntityWorld().removeEntity(this);
        }
    }
}
