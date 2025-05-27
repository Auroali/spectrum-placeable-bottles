package com.auroali.spectrumplaceablebottles.common.blocks;

import com.auroali.spectrumplaceablebottles.common.events.PopulateAcceptableItemSetCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Wrapper around a {@link Set} that both prevents modification and supports lazy loading
 */
public class AcceptableItemSet implements Collection<Item> {
    public static final AcceptableItemSet EMPTY = new AcceptableItemSet();
    private final Object lock = new Object();
    private final Supplier<Set<Item>> itemSupplier;
    private Set<Item> items;

    protected AcceptableItemSet(Supplier<Set<Item>> items) {
        this.itemSupplier = items;
    }

    /**
     * Only used for {@link AcceptableItemSet#EMPTY}
     */
    private AcceptableItemSet() {
        this.items = Collections.emptySet();
        this.itemSupplier = Collections::emptySet;
    }

    private void resolve() {
        if (this.items == null) {
            synchronized (this.lock) {
                if (this.items != null)
                    return;

                Set<Item> items = this.itemSupplier.get();
                Set<Item> eventSet = new HashSet<>(items);
                PopulateAcceptableItemSetCallback.Context ctx = new PopulateAcceptableItemSetCallback.Context(
                  this,
                  eventSet
                );

                PopulateAcceptableItemSetCallback.CALLBACK.invoker().populate(ctx);
                if (ctx.isModified())
                    items = Set.of(eventSet.toArray(Item[]::new));

                this.items = items;
            }
        }
    }

    @Override
    public boolean contains(Object item) {
        this.resolve();
        return this.items.contains(item);
    }

    public boolean contains(ItemStack stack) {
        return this.contains(stack.getItem());
    }

    @Override
    public boolean isEmpty() {
        this.resolve();
        return this.items.isEmpty();
    }

    @Override
    public int size() {
        this.resolve();
        return this.items.size();
    }

    @Override
    public @NotNull Iterator<Item> iterator() {
        this.resolve();
        return this.items.iterator();
    }

    @Override
    public @NotNull Object[] toArray() {
        this.resolve();
        return this.items.toArray();
    }

    @Override
    public @NotNull <T> T[] toArray(@NotNull T[] ts) {
        this.resolve();
        return this.items.toArray(ts);
    }

    @Override
    public <T> T[] toArray(@NotNull IntFunction<T[]> generator) {
        this.resolve();
        return this.items.toArray(generator);
    }

    @Override
    public boolean add(Item item) {
        throw modifyException(this);
    }

    @Override
    public boolean remove(Object o) {
        throw modifyException(this);
    }

    @Override
    public boolean removeIf(@NotNull Predicate<? super Item> filter) {
        throw modifyException(this);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> collection) {
        this.resolve();
        return this.items.containsAll(collection);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Item> collection) {
        throw modifyException(this);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> collection) {
        throw modifyException(this);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> collection) {
        throw modifyException(this);
    }

    @Override
    public void clear() {
        throw modifyException(this);
    }

    @Override
    public Spliterator<Item> spliterator() {
        this.resolve();
        return this.items.spliterator();
    }

    @Override
    public Stream<Item> stream() {
        this.resolve();
        return this.items.stream();
    }

    @Override
    public @NotNull Stream<Item> parallelStream() {
        this.resolve();
        return this.items.parallelStream();
    }

    private static UnsupportedOperationException modifyException(Object obj) {
        return new UnsupportedOperationException(obj.getClass().getSimpleName() + " does not support modification");
    }

    /**
     * Creates a new {@link AcceptableItemSet}. The set will not be resolved
     * until necessary, to prevent issues from loading certain objects earlier than expected
     *
     * @param items the item set supplier
     * @return the new {@link AcceptableItemSet}
     */
    public static AcceptableItemSet of(Supplier<Set<Item>> items) {
        return new AcceptableItemSet(items);
    }

    /**
     * Creates a new {@link AcceptableItemSet} that contains the contents of all provided sets
     *
     * @param sets all sets to use
     * @return the new {@link AcceptableItemSet}
     */
    public static AcceptableItemSet composite(AcceptableItemSet... sets) {
        return new AcceptableItemSet(() -> Arrays.stream(sets)
          .flatMap(AcceptableItemSet::stream)
          .collect(Collectors.toSet())
        );
    }
}
