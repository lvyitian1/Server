package org.cloudburstmc.server.block.behavior;

import org.cloudburstmc.server.block.Block;
import org.cloudburstmc.server.item.behavior.Item;
import org.cloudburstmc.server.item.behavior.ItemIds;
import org.cloudburstmc.server.item.behavior.ItemTool;
import org.cloudburstmc.server.item.enchantment.Enchantment;
import org.cloudburstmc.server.utils.BlockColor;

import java.util.Random;

public class BlockBehaviorMelon extends BlockBehaviorSolid {

    public float getHardness() {
        return 1;
    }

    @Override
    public float getResistance() {
        return 5;
    }

    @Override
    public Item[] getDrops(Block block, Item hand) {
        Random random = new Random();
        int count = 3 + random.nextInt(5);

        Enchantment fortune = hand.getEnchantment(Enchantment.ID_FORTUNE_DIGGING);
        if (fortune != null && fortune.getLevel() >= 1) {
            count += random.nextInt(fortune.getLevel() + 1);
        }

        return new Item[]{
                Item.get(ItemIds.MELON, 0, Math.min(9, count))
        };
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public BlockColor getColor(Block block) {
        return BlockColor.LIME_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}
