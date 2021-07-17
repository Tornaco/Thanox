package github.tornaco.android.thanos.widget.section;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.android.thanos.module.common.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * SectioningAdapter
 * Represents a list of sections, each containing a list of items and optionally a header and or footer item.
 * SectioningAdapter may be used with a normal RecyclerView.LinearLayoutManager but is meant for use with
 * StickyHeaderLayoutManager to allow for sticky positioning of header items.
 * <p/>
 * When invalidating the adapter's contents NEVER use RecyclerView.Adapter.notify* methods. These methods
 * aren't aware of the section information and internal state of SectioningAdapter. As such, please
 * use the SectioningAdapter.notify* methods.
 * <p/>
 * SectioningAdapter manages four types of items: TYPE_HEADER, TYPE_ITEM, TYPE_FOOTER and TYPE_GHOST_HEADER.
 * Headers are the optional first item in a section. A section then has some number of items in it,
 * and an optional footer. The ghost header is a special item used for layout mechanics. It can
 * be ignored by SectioningAdapter subclasses - but it is made externally accessible just in case.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class SectioningAdapter extends RecyclerView.Adapter<SectioningAdapter.ViewHolder> {

    private static final String TAG = "SectioningAdapter";

    public static final int NO_POSITION = -1;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_GHOST_HEADER = 1;
    public static final int TYPE_ITEM = 2;
    public static final int TYPE_FOOTER = 3;

    private static class Section {
        int adapterPosition;    // adapterPosition of first item (the header) of this sections
        int numberOfItems;      // number of items (not including header or footer)
        int length;             // total number of items in sections including header and footer
        boolean hasHeader;      // if true, sections has a header
        boolean hasFooter;      // if true, sections has a footer
    }

    private static class SectionSelectionState {
        boolean section;
        SparseBooleanArray items = new SparseBooleanArray();
        boolean footer;
    }

    private ArrayList<Section> sections;
    private HashMap<Integer, Boolean> collapsedSections = new HashMap<>();
    private HashMap<Integer, SectionSelectionState> selectionStateBySection = new HashMap<>();
    private int[] sectionIndicesByAdapterPosition;
    private int totalNumberOfItems;
    private Handler mainThreadHandler;


    @SuppressWarnings("WeakerAccess")
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private int section;
        private int numberOfItemsInSection;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public int getItemViewBaseType() {
            return SectioningAdapter.unmaskBaseViewType(getItemViewType());
        }

        public int getItemViewUserType() {
            return SectioningAdapter.unmaskUserViewType(getItemViewType());
        }

        public boolean isHeader() {
            return false;
        }

        public boolean isGhostHeader() {
            return false;
        }

        public boolean isFooter() {
            return false;
        }

        public int getSection() {
            return section;
        }

        private void setSection(int section) {
            this.section = section;
        }

        public int getNumberOfItemsInSection() {
            return numberOfItemsInSection;
        }

        void setNumberOfItemsInSection(int numberOfItemsInSection) {
            this.numberOfItemsInSection = numberOfItemsInSection;
        }
    }

    public static class ItemViewHolder extends ViewHolder {
        private int positionInSection;

        public ItemViewHolder(View itemView) {
            super(itemView);
        }

        public int getPositionInSection() {
            return positionInSection;
        }

        private void setPositionInSection(int positionInSection) {
            this.positionInSection = positionInSection;
        }
    }

    public static class HeaderViewHolder extends ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public boolean isHeader() {
            return true;
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class GhostHeaderViewHolder extends ViewHolder {
        public GhostHeaderViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public boolean isGhostHeader() {
            return true;
        }
    }


    public static class FooterViewHolder extends ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public boolean isFooter() {
            return true;
        }
    }


    /**
     * @return Number of sections
     */
    public int getNumberOfSections() {
        return 0;
    }

    /**
     * @param sectionIndex index of the section in question
     * @return the number of items in the specified section
     */
    public int getNumberOfItemsInSection(int sectionIndex) {
        return 0;
    }

    /**
     * @param sectionIndex index of the section in question
     * @return true if this section has a header
     */
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return false;
    }

    /**
     * For scenarios with multiple types of headers, override this to return an integer in range [0,255] specifying a custom type for this header.
     * The value you return here will be passes to onCreateHeaderViewHolder and onBindHeaderViewHolder as the 'userType'
     *
     * @param sectionIndex the header's section
     * @return the custom type for this header in range [0,255]
     */
    public int getSectionHeaderUserType(int sectionIndex) {
        return 0;
    }

    /**
     * @param sectionIndex index of the section in question
     * @return true if this section has a footer
     */
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return false;
    }


    /**
     * For scenarios with multiple types of footers, override this to return an integer in range [0, 255] specifying a custom type for this footer.
     * The value you return here will be passes to onCreateFooterViewHolder and onBindFooterViewHolder as the 'userType'
     *
     * @param sectionIndex the footer's section
     * @return the custom type for this footer in range [0,255]
     */
    public int getSectionFooterUserType(int sectionIndex) {
        return 0;
    }

    /**
     * For scenarios with multiple types of items, override this to return an integer in range [0,255] specifying a custom type for the item at this position
     * The value you return here will be passes to onCreateItemViewHolder and onBindItemViewHolder as the 'userType'
     *
     * @param sectionIndex the items's section
     * @param itemIndex    the position of the item in the section
     * @return the custom type for this item in range [0,255]
     */
    public int getSectionItemUserType(int sectionIndex, int itemIndex) {
        return 0;
    }

    /**
     * Called when a ViewHolder is needed for a section item view
     *
     * @param parent       The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param itemUserType If getSectionItemUserType is overridden to vend custom types, this will be the specified type
     * @return A new ItemViewHolder holding an item view
     */
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemUserType) {
        return null;
    }

    /**
     * Called when a ViewHolder is needed for a section header view
     *
     * @param parent         The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param headerUserType If getSectionHeaderUserType is overridden to vend custom types, this will be the specified type
     * @return A new HeaderViewHolder holding a header view
     */
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerUserType) {
        return null;
    }

    /**
     * Called when a ViewHolder is needed for a section footer view
     *
     * @param parent         The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param footerUserType If getSectionHeaderUserType is overridden to vend custom types, this will be the specified type
     * @return A new FooterViewHolder holding a footer view
     */
    public FooterViewHolder onCreateFooterViewHolder(ViewGroup parent, int footerUserType) {
        return null;
    }

    /**
     * Called when a ViewHolder is needed for a section ghost header view
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @return A new GhostHeaderViewHolder holding a ghost header view
     */
    public GhostHeaderViewHolder onCreateGhostHeaderViewHolder(ViewGroup parent) {
        View ghostView = new View(parent.getContext());
        return new GhostHeaderViewHolder(ghostView);
    }

    /**
     * Called to display item data at particular position
     *
     * @param viewHolder   the view holder to update
     * @param sectionIndex the index of the section containing the item
     * @param itemIndex    the index of the item in the section where 0 is the first item
     * @param itemUserType if getSectionItemUserType is overridden to provide custom item types, this will be the type for this item
     */
    public void onBindItemViewHolder(ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemUserType) {
    }

    /**
     * Called to display header data for a particular section
     *
     * @param viewHolder     the view holder to update
     * @param sectionIndex   the index of the section containing the header to update
     * @param headerUserType if getSectionHeaderUserType is overridden to provide custom header types, this will be the type for this header
     */
    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int sectionIndex, int headerUserType) {
    }

    /**
     * Called to update the ghost header for a particular section. Note, most implementations will not need to ever touch the ghost header.
     *
     * @param viewHolder   the view holder to update
     * @param sectionIndex the index of the section containing the ghost header to update
     */
    public void onBindGhostHeaderViewHolder(GhostHeaderViewHolder viewHolder, int sectionIndex) {
    }

    /**
     * Called to display footer data for a particular section
     *
     * @param viewHolder     the view holder to update
     * @param sectionIndex   the index of the section containing the footer to update
     * @param footerUserType if getSectionFooterUserType is overridden to provide custom footer types, this will be the type for this footer
     */
    public void onBindFooterViewHolder(FooterViewHolder viewHolder, int sectionIndex, int footerUserType) {
    }

    /**
     * Given a "global" adapter adapterPosition, determine which sections contains that item
     *
     * @param adapterPosition an adapter adapterPosition from 0 to getItemCount()-1
     * @return the index of the sections containing that item
     */
    public int getSectionForAdapterPosition(int adapterPosition) {
        if (sections == null) {
            buildSectionIndex();
        }

        if (getItemCount() == 0) {
            return -1;
        }

        if (adapterPosition < 0 || adapterPosition >= getItemCount()) {
            throw new IndexOutOfBoundsException("adapterPosition " + adapterPosition + " is not in range of items represented by adapter");
        }

        return sectionIndicesByAdapterPosition[adapterPosition];
    }

    /**
     * Given a sectionIndex and an adapter position get the local position of an item relative to the sectionIndex,
     * where the first item has position 0
     *
     * @param sectionIndex    the sectionIndex index
     * @param adapterPosition the adapter adapterPosition
     * @return the position relative to the sectionIndex of an item in that sectionIndex
     * <p/>
     * Note, if the adapterPosition corresponds to a sectionIndex header, this will return -1
     */
    public int getPositionOfItemInSection(int sectionIndex, int adapterPosition) {
        if (sections == null) {
            buildSectionIndex();
        }

        if (sectionIndex < 0) {
            throw new IndexOutOfBoundsException("sectionIndex " + sectionIndex + " < 0");
        }

        if (sectionIndex >= sections.size()) {
            throw new IndexOutOfBoundsException("sectionIndex " + sectionIndex + " >= sections.size (" + sections.size() + ")");
        }

        Section section = this.sections.get(sectionIndex);
        int localPosition = adapterPosition - section.adapterPosition;
        if (localPosition > section.length) {
            throw new IndexOutOfBoundsException("adapterPosition: " + adapterPosition + " is beyond sectionIndex: " + sectionIndex + " length: " + section.length);
        }

        if (section.hasHeader) {
            // adjust for header and ghostHeader
            localPosition -= 2;
        }

        return localPosition;
    }

    /**
     * Given a sectionIndex index, and an offset into the sectionIndex where 0 is the header, 1 is the ghostHeader, 2 is the first item in the sectionIndex, return the corresponding "global" adapter position
     *
     * @param sectionIndex      a sectionIndex index
     * @param offsetIntoSection offset into sectionIndex where 0 is the header, 1 is the first item, etc
     * @return the "global" adapter adapterPosition
     */
    private int getAdapterPosition(int sectionIndex, int offsetIntoSection) {
        if (sections == null) {
            buildSectionIndex();
        }

        if (sectionIndex < 0) {
            throw new IndexOutOfBoundsException("sectionIndex " + sectionIndex + " < 0");
        }

        if (sectionIndex >= sections.size()) {
            throw new IndexOutOfBoundsException("sectionIndex " + sectionIndex + " >= sections.size (" + sections.size() + ")");
        }

        Section section = this.sections.get(sectionIndex);
        int adapterPosition = section.adapterPosition;
        return offsetIntoSection + adapterPosition;
    }

    /**
     * Return the adapter position corresponding to the header of the provided section
     *
     * @param sectionIndex the index of the section
     * @return adapter position of that section's header, or NO_POSITION if section has no header
     */
    public int getAdapterPositionForSectionHeader(int sectionIndex) {
        if (doesSectionHaveHeader(sectionIndex)) {
            return getAdapterPosition(sectionIndex, 0);
        } else {
            return NO_POSITION;
        }
    }

    /**
     * Return the adapter position corresponding to the ghost header of the provided section
     *
     * @param sectionIndex the index of the section
     * @return adapter position of that section's ghost header, or NO_POSITION if section has no ghost header
     */
    public int getAdapterPositionForSectionGhostHeader(int sectionIndex) {
        if (doesSectionHaveHeader(sectionIndex)) {
            return getAdapterPosition(sectionIndex, 1); // ghost header follows the header
        } else {
            return NO_POSITION;
        }
    }

    /**
     * Return the adapter position corresponding to a specific item in the section
     *
     * @param sectionIndex      the index of the section
     * @param offsetIntoSection the offset of the item in the section where 0 would be the first item in the section
     * @return adapter position of the item in the section
     */

    public int getAdapterPositionForSectionItem(int sectionIndex, int offsetIntoSection) {
        if (doesSectionHaveHeader(sectionIndex)) {
            return getAdapterPosition(sectionIndex, offsetIntoSection) + 2; // header is at position 0, ghostHeader at position 1
        } else {
            return getAdapterPosition(sectionIndex, offsetIntoSection);
        }
    }

    /**
     * Return the adapter position corresponding to the footer of the provided section
     *
     * @param sectionIndex the index of the section
     * @return adapter position of that section's footer, or NO_POSITION if section does not have footer
     */
    public int getAdapterPositionForSectionFooter(int sectionIndex) {
        if (doesSectionHaveFooter(sectionIndex)) {
            Section section = this.sections.get(sectionIndex);
            int adapterPosition = section.adapterPosition;
            return adapterPosition + section.length - 1;
        } else {
            return NO_POSITION;
        }
    }

    /**
     * Mark that a section is collapsed or not. By default sections are not collapsed and draw
     * all their child items. By "collapsing" a section, the child items are hidden.
     *
     * @param sectionIndex index of section
     * @param collapsed    if true, section is collapsed, false, it's open
     */
    public void setSectionIsCollapsed(int sectionIndex, boolean collapsed) {
        boolean notify = isSectionCollapsed(sectionIndex) != collapsed;

        collapsedSections.put(sectionIndex, collapsed);

        if (notify) {
            if (sections == null) {
                buildSectionIndex();
            }

            Section section = sections.get(sectionIndex);
            int number = section.numberOfItems;

            if (collapsed) {
                notifySectionItemRangeRemoved(sectionIndex, 0, number, false);
            } else {
                notifySectionItemRangeInserted(sectionIndex, 0, number, false);
            }
        }
    }

    /**
     * @param sectionIndex index of section
     * @return true if that section is collapsed
     */
    public boolean isSectionCollapsed(int sectionIndex) {
        if (collapsedSections.containsKey(sectionIndex)) {
            return collapsedSections.get(sectionIndex);
        }

        return false;
    }

    private SectionSelectionState getSectionSelectionState(int sectionIndex) {
        SectionSelectionState state = selectionStateBySection.get(sectionIndex);
        if (state != null) {
            return state;
        }

        state = new SectionSelectionState();
        selectionStateBySection.put(sectionIndex, state);

        return state;
    }

    /**
     * Clear selection state
     *
     * @param notify if true, notifies data change for recyclerview, if false, silent
     */
    public void clearSelection(boolean notify) {

        HashMap<Integer, SectionSelectionState> selectionState = notify ? new HashMap<>(selectionStateBySection) : null;
        selectionStateBySection = new HashMap<>();

        if (notify) {

            // walk the selection state and update the items which were selected
            for (int sectionIndex : selectionState.keySet()) {
                SectionSelectionState state = selectionState.get(sectionIndex);

                if (state.section) {
                    notifySectionDataSetChanged(sectionIndex);
                } else {
                    for (int i = 0, s = state.items.size(); i < s; i++) {
                        if (state.items.valueAt(i)) {
                            notifySectionItemChanged(sectionIndex, state.items.keyAt(i));
                        }
                    }
                    if (state.footer) {
                        notifySectionFooterChanged(sectionIndex);
                    }
                }
            }
        }
    }

    /**
     * Clear selection state
     */
    public void clearSelection() {
        clearSelection(true);
    }

    /**
     * Quick check if selection is empty
     *
     * @return true iff the selection state is empty
     */
    public boolean isSelectionEmpty() {
        for (int sectionIndex : selectionStateBySection.keySet()) {
            SectionSelectionState state = selectionStateBySection.get(sectionIndex);

            if (state.section) {
                return false;
            } else {
                for (int i = 0, s = state.items.size(); i < s; i++) {
                    if (state.items.valueAt(i)) {
                        return false;
                    }
                }
                if (state.footer) {
                    return false;
                }
            }
        }

        return true;
    }

    public int getSelectedItemCount() {
        int count = 0;
        for (int sectionIndex : selectionStateBySection.keySet()) {
            SectionSelectionState state = selectionStateBySection.get(sectionIndex);

            if (state.section) {

                count += getNumberOfItemsInSection(sectionIndex);

                if (doesSectionHaveFooter(sectionIndex)) {
                    count++;
                }
            } else {
                for (int i = 0, s = state.items.size(); i < s; i++) {
                    boolean selected = state.items.valueAt(i);
                    if (selected) {
                        count++;
                    }
                }
                if (state.footer) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Visitor interface for walking adapter selection state.
     */
    public interface SelectionVisitor {
        void onVisitSelectedSection(int sectionIndex);

        void onVisitSelectedSectionItem(int sectionIndex, int itemIndex);

        void onVisitSelectedFooter(int sectionIndex);
    }

    /**
     * Walks the selection state of the adapter, in reverse order from end to front. This is to ensure that any additions or deletions
     * which are made based on selection are safe to perform.
     *
     * @param visitor visitor which is invoked to process selection state
     */
    public void traverseSelection(SelectionVisitor visitor) {

        // walk the section indices backwards
        List<Integer> sectionIndices = new ArrayList<>(selectionStateBySection.keySet());
        Collections.sort(sectionIndices, Collections.<Integer>reverseOrder());

        for (int sectionIndex : sectionIndices) {
            SectionSelectionState state = selectionStateBySection.get(sectionIndex);
            if (state == null) {
                continue;
            }

            if (state.section) {
                visitor.onVisitSelectedSection(sectionIndex);
            } else {

                if (state.footer) {
                    visitor.onVisitSelectedFooter(sectionIndex);
                }

                // walk items backwards
                for (int i = state.items.size() - 1; i >= 0; i--) {
                    if (state.items.valueAt(i)) {
                        visitor.onVisitSelectedSectionItem(sectionIndex, state.items.keyAt(i));
                    }
                }
            }
        }

    }

    /**
     * Set whether an entire section is selected. this affects ALL items (and footer) in section.
     *
     * @param sectionIndex index of the section
     * @param selected     selection state
     */
    public void setSectionSelected(int sectionIndex, boolean selected) {
        SectionSelectionState state = getSectionSelectionState(sectionIndex);
        if (state.section != selected) {
            state.section = selected;

            // update all items and footers
            state.items.clear();
            for (int i = 0, n = getNumberOfItemsInSection(sectionIndex); i < n; i++) {
                state.items.put(i, selected);
            }

            if (doesSectionHaveFooter(sectionIndex)) {
                state.footer = selected;
            }

            notifySectionDataSetChanged(sectionIndex);
        }
    }

    /**
     * Toggle selection state of an entire section
     *
     * @param sectionIndex index of section
     */
    public void toggleSectionSelected(int sectionIndex) {
        setSectionSelected(sectionIndex, !isSectionSelected(sectionIndex));
    }

    /**
     * Check if section is selected
     *
     * @param sectionIndex index of section
     * @return true if section is selected
     */
    public boolean isSectionSelected(int sectionIndex) {
        return getSectionSelectionState(sectionIndex).section;
    }

    /**
     * Select a specific item in a section. Note, if the section is selected, this is a no-op.
     *
     * @param sectionIndex index of section
     * @param itemIndex    index of item, relative to section
     * @param selected     selection state
     */
    public void setSectionItemSelected(int sectionIndex, int itemIndex, boolean selected) {
        SectionSelectionState state = getSectionSelectionState(sectionIndex);

        if (state.section) {
            return;
        }

        if (selected != state.items.get(itemIndex)) {
            state.items.put(itemIndex, selected);
            notifySectionItemChanged(sectionIndex, itemIndex);
        }
    }

    /**
     * Toggle selection state of a specific item in a section
     *
     * @param sectionIndex index of section
     * @param itemIndex    index of item in section
     */
    public void toggleSectionItemSelected(int sectionIndex, int itemIndex) {
        setSectionItemSelected(sectionIndex, itemIndex, !isSectionItemSelected(sectionIndex, itemIndex));
    }

    /**
     * Check whether a specific item in a section is selected, or if the entire section is selected
     *
     * @param sectionIndex index of section
     * @param itemIndex    index of item in section
     * @return true if the item is selected
     */
    public boolean isSectionItemSelected(int sectionIndex, int itemIndex) {
        SectionSelectionState state = getSectionSelectionState(sectionIndex);
        return state.section || state.items.get(itemIndex);
    }

    /**
     * Select the footer of a section
     *
     * @param sectionIndex index of section
     * @param selected     selection state
     */
    public void setSectionFooterSelected(int sectionIndex, boolean selected) {
        SectionSelectionState state = getSectionSelectionState(sectionIndex);

        if (state.section) {
            return;
        }

        if (state.footer != selected) {
            state.footer = selected;
            notifySectionFooterChanged(sectionIndex);
        }
    }

    /**
     * Toggle selection of footer in a section
     *
     * @param sectionIndex index of section
     */
    public void toggleSectionFooterSelection(int sectionIndex) {
        setSectionFooterSelected(sectionIndex, !isSectionFooterSelected(sectionIndex));
    }

    /**
     * Check whether footer of a section is selected, or if the entire section is selected
     *
     * @param sectionIndex section index
     * @return true if the footer is selected
     */
    public boolean isSectionFooterSelected(int sectionIndex) {
        SectionSelectionState state = getSectionSelectionState(sectionIndex);
        return state.section || state.footer;
    }

    /**
     * Notify that all data in the list is invalid and the entire list should be reloaded.
     * NOTE: This will clear selection state, and collapsed section state.
     * Equivalent to RecyclerView.Adapter.notifyDataSetChanged.
     * Never directly call notifyDataSetChanged.
     */
    public void notifyAllSectionsDataSetChanged() {
        buildSectionIndex();
        notifyDataSetChanged();
        collapsedSections.clear();
        selectionStateBySection.clear();
    }

    /**
     * Notify that all the items in a particular section are invalid and that section should be reloaded
     * Never directly call notifyDataSetChanged.
     * This will clear item selection state for the affected section.
     *
     * @param sectionIndex index of the section to reload.
     */
    public void notifySectionDataSetChanged(int sectionIndex) {
        if (sections == null) {
            buildSectionIndex();
            notifyAllSectionsDataSetChanged();
        } else {
            buildSectionIndex();
            Section section = this.sections.get(sectionIndex);
            notifyItemRangeChanged(section.adapterPosition, section.length);
        }

        // clear item selection state
        getSectionSelectionState(sectionIndex).items.clear();
    }

    /**
     * Notify that a range of items in a section has been inserted
     *
     * @param sectionIndex index of the section
     * @param fromPosition index to start adding
     * @param number       amount of items inserted
     */
    public void notifySectionItemRangeInserted(int sectionIndex, int fromPosition, int number) {
        notifySectionItemRangeInserted(sectionIndex, fromPosition, number, true);
    }

    private void notifySectionItemRangeInserted(int sectionIndex, int fromPosition, int number, boolean updateSelectionState) {
        if (sections == null) {
            buildSectionIndex();
            notifyAllSectionsDataSetChanged();
        } else {
            buildSectionIndex();
            Section section = this.sections.get(sectionIndex);

            // 0 is a valid position to insert from
            if (fromPosition > section.numberOfItems) {
                throw new IndexOutOfBoundsException("itemIndex adapterPosition: " + fromPosition + " exceeds sectionIndex numberOfItems: " + section.numberOfItems);
            }

            int offset = fromPosition;
            if (section.hasHeader) {
                offset += 2;
            }

            notifyItemRangeInserted(section.adapterPosition + offset, number);
        }

        if (updateSelectionState) {
            // update selection state by inserting unselected spaces
            updateSectionItemRangeSelectionState(sectionIndex, fromPosition, +number);
        }
    }

    /**
     * Notify that a range of items in a section has been removed
     *
     * @param sectionIndex index of the section
     * @param fromPosition index to start removing from
     * @param number       amount of items removed
     */
    public void notifySectionItemRangeRemoved(int sectionIndex, int fromPosition, int number) {
        notifySectionItemRangeRemoved(sectionIndex, fromPosition, number, true);
    }

    private void notifySectionItemRangeRemoved(int sectionIndex, int fromPosition, int number, boolean updateSelectionState) {
        if (sections == null) {
            buildSectionIndex();
            notifyAllSectionsDataSetChanged();
        } else {
            Section section = this.sections.get(sectionIndex);

            // 0 is a valid position to remove from
            if (fromPosition > section.numberOfItems) {
                throw new IndexOutOfBoundsException("itemIndex adapterPosition: " + fromPosition + " exceeds sectionIndex numberOfItems: " + section.numberOfItems);
            }

            // Verify we don't run off the end of the section
            if (fromPosition + number > section.numberOfItems) {
                throw new IndexOutOfBoundsException("itemIndex adapterPosition: " + fromPosition + number + " exceeds sectionIndex numberOfItems: " + section.numberOfItems);
            }

            int offset = fromPosition;
            if (section.hasHeader) {
                offset += 2;
            }

            notifyItemRangeRemoved(section.adapterPosition + offset, number);
            buildSectionIndex();
        }

        if (updateSelectionState) {
            // update selection state by removing specified items
            updateSectionItemRangeSelectionState(sectionIndex, fromPosition, -number);
        }
    }


    /**
     * Notify that a particular itemIndex in a section has been invalidated and must be reloaded
     * Never directly call notifyItemChanged
     *
     * @param sectionIndex the index of the section containing the itemIndex
     * @param itemIndex    the index of the item relative to the section (where 0 is the first item in the section)
     */
    public void notifySectionItemChanged(int sectionIndex, int itemIndex) {
        if (sections == null) {
            buildSectionIndex();
            notifyAllSectionsDataSetChanged();
        } else {
            buildSectionIndex();
            Section section = this.sections.get(sectionIndex);
            if (itemIndex >= section.numberOfItems) {
                throw new IndexOutOfBoundsException("itemIndex adapterPosition: " + itemIndex + " exceeds sectionIndex numberOfItems: " + section.numberOfItems);
            }
            if (section.hasHeader) {
                itemIndex += 2;
            }
            notifyItemChanged(section.adapterPosition + itemIndex);
        }
    }

    /**
     * Notify that an item has been added to a section
     * Never directly call notifyItemInserted
     *
     * @param sectionIndex index of the section
     * @param itemIndex    index of the item where 0 is the first position in the section
     */
    public void notifySectionItemInserted(int sectionIndex, int itemIndex) {
        if (sections == null) {
            buildSectionIndex();
            notifyAllSectionsDataSetChanged();
        } else {
            buildSectionIndex();
            Section section = this.sections.get(sectionIndex);

            int offset = itemIndex;
            if (section.hasHeader) {
                offset += 2;
            }
            notifyItemInserted(section.adapterPosition + offset);
        }

        updateSectionItemRangeSelectionState(sectionIndex, itemIndex, 1);
    }

    /**
     * Notify that an item has been removed from a section
     * Never directly call notifyItemRemoved
     *
     * @param sectionIndex index of the section
     * @param itemIndex    index of the item in the section where 0 is the first position in the section
     */
    public void notifySectionItemRemoved(int sectionIndex, int itemIndex) {
        if (sections == null) {
            buildSectionIndex();
            notifyAllSectionsDataSetChanged();
        } else {
            buildSectionIndex();
            Section section = this.sections.get(sectionIndex);

            int offset = itemIndex;
            if (section.hasHeader) {
                offset += 2;
            }
            notifyItemRemoved(section.adapterPosition + offset);
        }

        updateSectionItemRangeSelectionState(sectionIndex, itemIndex, -1);
    }

    /**
     * Notify that a new section has been added
     *
     * @param sectionIndex position of the new section
     */
    public void notifySectionInserted(int sectionIndex) {
        if (sections == null) {
            buildSectionIndex();
            notifyAllSectionsDataSetChanged();
        } else {
            buildSectionIndex();
            Section section = this.sections.get(sectionIndex);
            notifyItemRangeInserted(section.adapterPosition, section.length);
        }

        updateCollapseAndSelectionStateForSectionChange(sectionIndex, +1);
    }

    /**
     * Notify that a section has been removed
     *
     * @param sectionIndex position of the removed section
     */
    public void notifySectionRemoved(int sectionIndex) {
        if (sections == null) {
            buildSectionIndex();
            notifyAllSectionsDataSetChanged();
        } else {
            Section section = this.sections.get(sectionIndex);
            buildSectionIndex();
            notifyItemRangeRemoved(section.adapterPosition, section.length);
        }

        updateCollapseAndSelectionStateForSectionChange(sectionIndex, -1);
    }

    /**
     * Notify that a section has had a footer added to it
     *
     * @param sectionIndex position of the section
     */
    public void notifySectionFooterInserted(int sectionIndex) {
        if (sections == null) {
            buildSectionIndex();
            notifyAllSectionsDataSetChanged();
        } else {
            buildSectionIndex();
            Section section = this.sections.get(sectionIndex);
            if (!section.hasFooter) {
                throw new IllegalArgumentException("notifySectionFooterInserted: adapter implementation reports that section " + sectionIndex + " does not have a footer");
            }
            notifyItemInserted(section.adapterPosition + section.length - 1);
        }
    }

    /**
     * Notify that a section has had a footer removed from it
     *
     * @param sectionIndex position of the section
     */
    public void notifySectionFooterRemoved(int sectionIndex) {
        if (sections == null) {
            buildSectionIndex();
            notifyAllSectionsDataSetChanged();
        } else {
            buildSectionIndex();
            Section section = this.sections.get(sectionIndex);
            if (section.hasFooter) {
                throw new IllegalArgumentException("notifySectionFooterRemoved: adapter implementation reports that section " + sectionIndex + " has a footer");
            }
            notifyItemRemoved(section.adapterPosition + section.length);
        }
    }

    /**
     * Notify that a section's footer's content has changed
     *
     * @param sectionIndex position of the section
     */
    public void notifySectionFooterChanged(int sectionIndex) {
        if (sections == null) {
            buildSectionIndex();
            notifyAllSectionsDataSetChanged();
        } else {
            buildSectionIndex();
            Section section = this.sections.get(sectionIndex);
            if (!section.hasFooter) {
                throw new IllegalArgumentException("notifySectionFooterChanged: adapter implementation reports that section " + sectionIndex + " does not have a footer");
            }
            notifyItemChanged(section.adapterPosition + section.length - 1);
        }
    }

    /**
     * Post an action to be run later.
     * RecyclerView doesn't like being mutated during a scroll. We can't detect when a
     * scroll is actually happening, unfortunately, so the best we can do is post actions
     * from notify* methods to be run at a later date.
     *
     * @param action action to run
     */
    private void post(Runnable action) {
        if (mainThreadHandler == null) {
            mainThreadHandler = new Handler(Looper.getMainLooper());
        }

        mainThreadHandler.post(action);
    }

    private void buildSectionIndex() {
        sections = new ArrayList<>();

        int i = 0;
        for (int s = 0, ns = getNumberOfSections(); s < ns; s++) {
            Section section = new Section();
            section.adapterPosition = i;
            section.hasHeader = doesSectionHaveHeader(s);
            section.hasFooter = doesSectionHaveFooter(s);

            if (isSectionCollapsed(s)) {
                section.length = 0;
                section.numberOfItems = getNumberOfItemsInSection(s);
            } else {
                section.length = section.numberOfItems = getNumberOfItemsInSection(s);
            }

            if (section.hasHeader) {
                section.length += 2; // room for header and ghostHeader
            }
            if (section.hasFooter) {
                section.length++;
            }

            this.sections.add(section);

            i += section.length;
        }

        totalNumberOfItems = i;

        i = 0;
        sectionIndicesByAdapterPosition = new int[totalNumberOfItems];
        for (int s = 0, ns = getNumberOfSections(); s < ns; s++) {
            Section section = sections.get(s);
            for (int p = 0; p < section.length; p++) {
                sectionIndicesByAdapterPosition[i + p] = s;
            }

            i += section.length;
        }
    }

    private void updateSectionItemRangeSelectionState(int sectionIndex, int fromPosition, int delta) {
        SectionSelectionState sectionSelectionState = getSectionSelectionState(sectionIndex);
        SparseBooleanArray itemState = sectionSelectionState.items.clone();
        sectionSelectionState.items.clear();

        for (int i = 0, n = itemState.size(); i < n; i++) {
            int pos = itemState.keyAt(i);

            if (delta < 0 && pos >= fromPosition && pos < fromPosition - delta) { // erasure
                continue;
            }

            int newPos = pos;
            if (pos >= fromPosition) {
                newPos += delta;
            }

            if (itemState.get(pos)) {
                sectionSelectionState.items.put(newPos, true);
            }
        }
    }


    private void updateCollapseAndSelectionStateForSectionChange(int sectionIndex, int delta) {

        // update section collapse state
        HashMap<Integer, Boolean> collapseState = new HashMap<>(collapsedSections);
        collapsedSections.clear();

        for (int i : collapseState.keySet()) {
            // erasure
            if (delta < 0 && i == sectionIndex) {
                continue;
            }

            int j = i;
            if (j >= sectionIndex) {
                j += delta;
            }

            collapsedSections.put(j, collapseState.get(i));
        }

        // update selection state
        HashMap<Integer, SectionSelectionState> selectionState = new HashMap<>(selectionStateBySection);
        selectionStateBySection.clear();

        for (int i : selectionState.keySet()) {
            // erasure
            if (delta < 0 && i == sectionIndex) {
                continue;
            }

            int j = i;
            if (j >= sectionIndex) {
                j += delta;
            }

            selectionStateBySection.put(j, selectionState.get(i));
        }
    }

    @Override
    public int getItemCount() {
        if (sections == null) {
            buildSectionIndex();
        }
        return totalNumberOfItems;
    }

    @Override
    public int getItemViewType(int adapterPosition) {
        if (sections == null) {
            buildSectionIndex();
        }

        if (adapterPosition < 0) {
            throw new IndexOutOfBoundsException("adapterPosition (" + adapterPosition + ") cannot be < 0");
        } else if (adapterPosition >= getItemCount()) {
            throw new IndexOutOfBoundsException("adapterPosition (" + adapterPosition + ")  cannot be > getItemCount() (" + getItemCount() + ")");
        }

        int sectionIndex = getSectionForAdapterPosition(adapterPosition);
        Section section = this.sections.get(sectionIndex);
        int localPosition = adapterPosition - section.adapterPosition;

        int baseType = getItemViewBaseType(section, localPosition);
        int userType = 0;


        switch (baseType) {
            case TYPE_HEADER:
                userType = getSectionHeaderUserType(sectionIndex);
                if (userType < 0 || userType > 0xFF) {
                    throw new IllegalArgumentException("Custom header view type (" + userType + ") must be in range [0,255]");
                }
                break;
            case TYPE_ITEM:
                // adjust local position to accommodate header & ghost header
                if (section.hasHeader) {
                    localPosition -= 2;
                }
                userType = getSectionItemUserType(sectionIndex, localPosition);
                if (userType < 0 || userType > 0xFF) {
                    throw new IllegalArgumentException("Custom item view type (" + userType + ") must be in range [0,255]");
                }
                break;
            case TYPE_FOOTER:
                userType = getSectionFooterUserType(sectionIndex);
                if (userType < 0 || userType > 0xFF) {
                    throw new IllegalArgumentException("Custom footer view type (" + userType + ") must be in range [0,255]");
                }
                break;
        }


        // base is bottom 8 bits, user type next 8 bits
        return ((userType & 0xFF) << 8) | (baseType & 0xFF);
    }

    /**
     * @param adapterPosition the adapterPosition of the item in question
     * @return the base type (TYPE_HEADER, TYPE_GHOST_HEADER, TYPE_ITEM, TYPE_FOOTER) of the item at a given adapter position
     */
    public int getItemViewBaseType(int adapterPosition) {
        return unmaskBaseViewType(getItemViewType(adapterPosition));
    }

    /**
     * @param adapterPosition the adapterPosition of the item in question
     * @return the custom user type of the item at the adapterPosition
     */
    public int getItemViewUserType(int adapterPosition) {
        return unmaskUserViewType(getItemViewType(adapterPosition));
    }

    public static int unmaskBaseViewType(int itemViewTypeMask) {
        return itemViewTypeMask & 0xFF; // base view type (HEADER/ITEM/FOOTER/GHOST_HEADER) is lower 8 bits
    }

    public static int unmaskUserViewType(int itemViewTypeMask) {
        return (itemViewTypeMask >> 8) & 0xFF; // use type is in 0x0000FF00 segment
    }

    int getItemViewBaseType(Section section, int localPosition) {
        if (section.hasHeader && section.hasFooter) {
            if (localPosition == 0) {
                return TYPE_HEADER;
            } else if (localPosition == 1) {
                return TYPE_GHOST_HEADER;
            } else if (localPosition == section.length - 1) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        } else if (section.hasHeader) {
            if (localPosition == 0) {
                return TYPE_HEADER;
            } else if (localPosition == 1) {
                return TYPE_GHOST_HEADER;
            } else {
                return TYPE_ITEM;
            }
        } else if (section.hasFooter) {
            if (localPosition == section.length - 1) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        } else {
            // this sections has no header or footer
            return TYPE_ITEM;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int baseViewType = unmaskBaseViewType(viewType);
        int userViewType = unmaskUserViewType(viewType);

        switch (baseViewType) {
            case TYPE_ITEM:
                return onCreateItemViewHolder(parent, userViewType);
            case TYPE_HEADER:
                return onCreateHeaderViewHolder(parent, userViewType);
            case TYPE_FOOTER:
                return onCreateFooterViewHolder(parent, userViewType);
            case TYPE_GHOST_HEADER:
                return onCreateGhostHeaderViewHolder(parent);
        }

        throw new IndexOutOfBoundsException("unrecognized viewType: " + viewType + " does not correspond to TYPE_ITEM, TYPE_HEADER or TYPE_FOOTER");
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int adapterPosition) {
        int section = getSectionForAdapterPosition(adapterPosition);

        // bind the sections to this view holder
        holder.setSection(section);
        holder.setNumberOfItemsInSection(getNumberOfItemsInSection(section));

        // tag the viewHolder's item so as to make it possible to track in layout manager
        tagViewHolderItemView(holder, section, adapterPosition);

        int baseType = unmaskBaseViewType(holder.getItemViewType());
        int userType = unmaskUserViewType(holder.getItemViewType());
        switch (baseType) {
            case TYPE_HEADER:
                onBindHeaderViewHolder((HeaderViewHolder) holder, section, userType);
                break;

            case TYPE_ITEM:
                ItemViewHolder ivh = (ItemViewHolder) holder;
                int positionInSection = getPositionOfItemInSection(section, adapterPosition);
                ivh.setPositionInSection(positionInSection);
                onBindItemViewHolder(ivh, section, positionInSection, userType);
                break;

            case TYPE_FOOTER:
                onBindFooterViewHolder((FooterViewHolder) holder, section, userType);
                break;

            case TYPE_GHOST_HEADER:
                onBindGhostHeaderViewHolder((GhostHeaderViewHolder) holder, section);
                break;

            default:
                throw new IllegalArgumentException("unrecognized viewType: " + baseType + " does not correspond to TYPE_ITEM, TYPE_HEADER, TYPE_GHOST_HEADER or TYPE_FOOTER");
        }
    }

    /**
     * Tag the itemView of the view holder with information needed for the layout to do its sticky positioning.
     * Specifically, it tags R.id.sectioning_adapter_tag_key_view_type to the item type, R.id.sectioning_adapter_tag_key_view_section
     * to the item's section, and R.id.sectioning_adapter_tag_key_view_adapter_position which is the adapter position of the view
     *
     * @param holder          the view holder containing the itemView to tag
     * @param section         the section index
     * @param adapterPosition the adapter position of the view holder
     */
    void tagViewHolderItemView(ViewHolder holder, int section, int adapterPosition) {
        View view = holder.itemView;
        view.setTag(R.id.sectioning_adapter_tag_key_view_viewholder, holder);
    }

}
