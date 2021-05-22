package com.github.md5sha256.spigotutils.blocks;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * Compressed block position of a world represent by a long. Each coordinate effectively
 * only has 18-bits of representation.
 * <p>
 * Originally taken from: https://github.com/TheBusyBiscuit/CS-CoreLib2
 * (MIT License)
 */
public final class BlockPosition {

    private final WeakReference<World> world;
    private final long position;

    public BlockPosition(@NotNull World world, long position) {
        this.world = new WeakReference<>(world);
        this.position = position;
    }

    public BlockPosition(@NotNull World world, int x, int y, int z) {
        this.world = new WeakReference<>(world);
        this.position = getAsLong(x, y, z);
    }

    public BlockPosition(@NotNull Block b) {
        this(b.getWorld(), b.getX(), b.getY(), b.getZ());
    }

    public BlockPosition(@NotNull Location l) {
        this(Objects.requireNonNull(l.getWorld(), "Location cannot have a null world!"),
                l.getBlockX(),
                l.getBlockY(),
                l.getBlockZ()
        );
    }

    /**
     * Gets the {@link World} this block is in. If this {@link World} has been unloaded it will throw
     * an {@link IllegalStateException}. This should be getting handled properly by yourself! <br>
     * <b>Note: This is held as a weak reference!</b>
     *
     * @return The {@link World} for this block.
     */
    public World getWorld() {
        final World ref = this.world.get();
        if (ref == null) {
            throw new IllegalStateException("The reference of this BlockPositions world has been cleared");
        }

        return ref;
    }

    /**
     * Gets the long position of this block. This is constructed of the x, y and z. <br>
     * This is encoded as follows: {@code ((x & 0x3FFFFFF) << 38) | ((z & 0x3FFFFFF) << 12) | (y & 0xFFF)}
     *
     * @return The position of this block.
     */
    public long getPosition() {
        return position;
    }

    /**
     * Gets the x for this block.
     *
     * @return This blocks x coordinate.
     */
    public int getX() {
        return (int) (this.position >> 38);
    }

    /**
     * Gets the y for this block.
     *
     * @return This blocks y coordinate.
     */
    public int getY() {
        return (int) (this.position & 0xFFF);
    }

    /**
     * Gets the z for this block.
     *
     * @return This blocks z coordinate.
     */
    public int getZ() {
        return (int) (this.position << 26 >> 38);
    }

    /**
     * Gets the {@link Block} at this position. Note, Bukkit will create a new instance so if you can avoid doing this
     * then do as it is a bit costly.
     *
     * @return The {@link Block} at this location.
     */
    public Block getBlock() {
        return getChunk().getBlock((getX() & 0xF), getY(), (getZ() & 0xF));
    }

    /**
     * Gets the {@link Chunk} where this block is located.
     *
     * @return This blocks {@link Chunk}.
     */
    public Chunk getChunk() {
        final World ref = this.world.get();

        if (ref == null) {
            throw new IllegalStateException("Cannot get Chunk when the world isn't loaded");
        }

        return ref.getChunkAt(getChunkX(), getChunkZ());
    }

    /**
     * Gets the chunks x coordinate for this block.
     *
     * @return The blocks chunks x coordinate.
     */
    public int getChunkX() {
        return this.getX() >> 4;
    }

    /**
     * Gets the chunks z coordinate for this block.
     *
     * @return The blocks chunks z coordinate.
     */
    public int getChunkZ() {
        return this.getZ() >> 4;
    }

    /**
     * Transform this BlockPosition into a standard Bukkit {@link Location}.
     *
     * @return A Bukkit {@link Location}.
     */
    public Location toLocation() {
        return new Location(this.world.get(), getX(), getY(), getZ());
    }


    /**
     * Decode a given {@link Long} to a location.
     *
     * @param world   The {@link World} of this location
     * @param encoded The compacted {@link Long}
     * @return Returns a never-null {@link Location}
     * @see #getAsLong(int, int, int)
     */
    public static @NotNull Location toLocation(@Nullable World world, long encoded) {
        int x = (int) (encoded >> 38);
        int y = (int) (encoded & 0xFFF);
        int z = (int) (encoded << 26 >> 38);
        return new Location(world, x, y, z);
    }

    /**
     * This compacts the three provided integers into one {@link Long}.
     * This allows us to save a lot memory-wise.
     *
     * @param x The x component
     * @param y The y component
     * @param z The z component
     * @return The compacted {@link Long}
     * @see #toLocation(World, long)
     */
    public static long getAsLong(int x, int y, int z) {
        return ((long) (x & 0x3FFFFFF) << 38) | ((long) (z & 0x3FFFFFF) << 12) | (long) (y & 0xFFF);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof BlockPosition) {
            final BlockPosition pos = (BlockPosition) o;

            if (pos.world.get() == null) {
                return false;
            }

            return this.getWorld().getUID().equals(pos.getWorld().getUID()) && this.position == pos.position;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        final World ref = this.world.get();

        int result = 0;
        result += prime * (ref == null ? 0 : ref.hashCode());
        result += prime * Long.hashCode(position);

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        World w = this.world.get();
        final String worldName = w != null ? w.getName() : "<no reference>";

        return String.format("BlockPosition(world=%s, x=%d, y=%d, z=%d, position=%d)", worldName, getX(), getY(), getZ(), getPosition());
    }

}
