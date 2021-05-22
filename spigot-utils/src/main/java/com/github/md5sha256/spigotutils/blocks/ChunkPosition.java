package com.github.md5sha256.spigotutils.blocks;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * Chunk coordinates packed into a long.
 * <p>
 * Originally taken from https://github.com/TheBusyBiscuit/CS-CoreLib2
 * (MIT License)
 *
 * @see BlockPosition
 */
public final class ChunkPosition {

    private final WeakReference<World> world;
    private final long position;

    public ChunkPosition(@NotNull World world, long position) {
        this.world = new WeakReference<>(world);
        this.position = position;
    }

    public ChunkPosition(@NotNull World world, int x, int z) {
        this.world = new WeakReference<>(world);
        this.position = getAsLong(x, z);
    }

    public ChunkPosition(@NotNull Chunk chunk) {
        this(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }

    public ChunkPosition(@NotNull Location l) {
        this(Objects.requireNonNull(l.getWorld(), "Location cannot have a null world"),
                l.getBlockX() >> 4,
                l.getBlockZ() >> 4);
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
     * This checks whether this {@link Chunk} is loaded.
     * If the {@link World} is not loaded, this method will
     * return false.
     *
     * @return Whether this {@link Chunk} is loaded
     */
    public boolean isLoaded() {
        final World ref = this.world.get();

        if (ref == null) {
            return false;
        } else {
            return ref.isChunkLoaded(getX(), getZ());
        }
    }

    /**
     * Gets the long position of this block. This is constructed of the x and z. <br>
     * This is encoded as follows: {@code (x << 32) | (z & 0xFFFFFFFFL)}
     *
     * @return The position of this block.
     */
    public long getPosition() {
        return position;
    }

    /**
     * Gets the x for this {@link Chunk}.
     *
     * @return This chunks x coordinate.
     */
    public int getX() {
        return (int) (this.position >> 32);
    }

    /**
     * Gets the y for this {@link Chunk}.
     *
     * @return This chunks z coordinate.
     */
    public int getZ() {
        return (int) this.position;
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

        return ref.getChunkAt(getX(), getZ());
    }

    /**
     * This compacts the two provided integers into one {@link Long}.
     * This allows us to save a lot memory-wise.
     *
     * @param x The x component
     * @param z The z component
     * @return The compacted {@link Long}
     * @see #decodeFromLong(long)
     */
    public static long getAsLong(int x, int z) {
        return (((long) x) << 32) | (z & 0xFFFFFFFFL);
    }

    /**
     * Decodes the provided {@link Long} into a two-length
     * {@link Integer[]}.
     *
     * @param encoded THe compacted {@link Long}
     * @return An int[] of length two. The x component is at index 0, the z component at index 1.
     * @see #getAsLong(int, int)
     */
    public static int[] decodeFromLong(long encoded) {
        int x = (int) (encoded >> 32);
        int z = (int) encoded;
        return new int[]{x, z};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof ChunkPosition) {
            final ChunkPosition pos = (ChunkPosition) o;

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

        return String.format("ChunkPosition(world=%s, x=%d, z=%d, position=%d)", worldName, getX(), getZ(), getPosition());
    }
}
