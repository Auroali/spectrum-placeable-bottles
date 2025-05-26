package com.auroali.spectrumplaceablebottles.common.events;

import com.auroali.spectrumplaceablebottles.common.blocks.AcceptableItemSet;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.Item;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Allows adding additional entries to an {@link AcceptableItemSet}
 */
public interface PopulateAcceptableItemSetCallback {
    Event<PopulateAcceptableItemSetCallback> CALLBACK = EventFactory.createArrayBacked(PopulateAcceptableItemSetCallback.class, callbacks -> (context) -> {
        for (PopulateAcceptableItemSetCallback callback : callbacks) {
            callback.populate(context);
        }
    });

    void populate(Context context);

    class Context {
        private final AcceptableItemSet key;
        private final Set<Item> items;
        private boolean isModified;

        public Context(AcceptableItemSet key, Set<Item> items) {
            this.key = key;
            this.items = items;
            this.isModified = false;
        }

        /**
         * Adds an item to the set
         *
         * @param item the item to add
         */
        public void add(Item item) {
            if (this.items.add(item))
                this.isModified = true;
        }

        /**
         * Removes an item from the set
         *
         * @param item the item to remove
         */
        public void remove(Item item) {
            if (this.items.remove(item))
                this.isModified = true;
        }

        /**
         * Removes multiple items using a filter
         *
         * @param predicate the filter to use
         */
        public void removeIf(Predicate<Item> predicate) {
            Iterator<Item> iter = this.items.iterator();
            while (iter.hasNext()) {
                Item next = iter.next();
                if (predicate.test(next)) {
                    iter.remove();
                    this.isModified = true;
                }
            }
        }

        /**
         * @param set the set to compare against
         * @return if the set this context is working on is the same as the provided set
         */
        public boolean is(AcceptableItemSet set) {
            return this.key == set;
        }

        public boolean isModified() {
            return this.isModified;
        }
    }
}
