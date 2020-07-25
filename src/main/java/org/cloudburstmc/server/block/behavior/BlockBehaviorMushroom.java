package org.cloudburstmc.server.block.behavior;

import com.nukkitx.math.vector.Vector3f;
import net.daporkchop.lib.random.impl.ThreadLocalPRandom;
import org.cloudburstmc.server.block.Block;
import org.cloudburstmc.server.block.BlockState;
import org.cloudburstmc.server.block.BlockTypes;
import org.cloudburstmc.server.item.Item;
import org.cloudburstmc.server.item.ItemIds;
import org.cloudburstmc.server.level.Level;
import org.cloudburstmc.server.level.feature.WorldFeature;
import org.cloudburstmc.server.level.feature.tree.TreeSpecies;
import org.cloudburstmc.server.level.particle.BoneMealParticle;
import org.cloudburstmc.server.math.BlockFace;
import org.cloudburstmc.server.player.Player;
import org.cloudburstmc.server.utils.BlockColor;
import org.cloudburstmc.server.utils.Identifier;
import org.cloudburstmc.server.utils.data.DyeColor;

import java.util.concurrent.ThreadLocalRandom;

public abstract class BlockBehaviorMushroom extends FloodableBlockBehavior {

    public BlockBehaviorMushroom(Identifier id) {
        super(id);
    }

    @Override
    public int onUpdate(Block block, int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!canStay()) {
                getLevel().useBreakOn(this.getPosition());

                return Level.BLOCK_UPDATE_NORMAL;
            }
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, Vector3f clickPos, Player player) {
        if (canStay()) {
            getLevel().setBlock(blockState.getPosition(), this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Block block, Item item, Player player) {
        if (item.getId() == ItemIds.DYE && item.getMeta() == DyeColor.WHITE.getDyeData()) {
            if (player != null && player.getGamemode().isSurvival()) {
                item.decrementCount();
            }

            if (ThreadLocalRandom.current().nextFloat() < 0.4) {
                this.grow();
            }

            this.level.addParticle(new BoneMealParticle(this.getPosition()));
            return true;
        }
        return false;
    }

    public boolean grow() {
        this.level.setBlock(this.getPosition(), BlockState.AIR, true, false);

        WorldFeature feature = TreeSpecies.fromItem(this.getId(), this.getMeta()).getDefaultGenerator();

        if (feature.place(this.level, ThreadLocalPRandom.current(), this.getX(), this.getY(), this.getZ())) {
            return true;
        } else {
            this.level.setBlock(this.getPosition(), this, true, false);
            return false;
        }
    }

    public boolean canStay() {
        BlockState blockState = this.down();
        return blockState.getId() == BlockTypes.MYCELIUM || blockState.getId() == BlockTypes.PODZOL ||
                (!blockState.isTransparent() && this.level.getFullLight(this.getPosition()) < 13);
    }

    @Override
    public BlockColor getColor(BlockState state) {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    protected abstract int getType();
}