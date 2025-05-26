package com.auroali.spectrumplaceablebottles.common.blocks;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AcceptableItemSet implements Collection<Item> {
    public static final AcceptableItemSet EMPTY = AcceptableItemSet.of(Collections::emptySet);
    private final Supplier<Set<Item>> itemSupplier;
    private Set<Item> items;

    protected AcceptableItemSet(Supplier<Set<Item>> items) {
        this.itemSupplier = items;
    }

    private void resolve() {
        if (this.items == null)
            this.items = this.itemSupplier.get();
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
    public boolean add(Item item) {
        throw modifyException(this);
    }

    @Override
    public boolean remove(Object o) {
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

    private static UnsupportedOperationException modifyException(Object obj) {
        return new UnsupportedOperationException(obj.getClass().getSimpleName() + " does not support modification");
    }

    public static AcceptableItemSet of(Supplier<Set<Item>> items) {
        return new AcceptableItemSet(items);
    }

    public static AcceptableItemSet composite(AcceptableItemSet... sets) {
        return new AcceptableItemSet(() -> Arrays.stream(sets)
          .flatMap(AcceptableItemSet::stream)
          .collect(Collectors.toSet())
        );
    }
}
