package com.github.nikitin_da.sticky_dictionary.ui.navigation_drawer;

import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.ui.fragment.AllGroupsWordListFragment;
import com.github.nikitin_da.sticky_dictionary.ui.fragment.CheckableGroupListFragment;
import com.github.nikitin_da.sticky_dictionary.ui.fragment.GroupListFragment;
import com.github.nikitin_da.sticky_dictionary.ui.fragment.ImportExportFragment;

/**
 * Items of navigation drawer, which include appropriate view id's and
 * classes, that will present, when item selected.
 *
 * @author Dmitry Nikitin on 10/13/2014.
 */
public enum NavigationDrawerItem {

    /**
     * Group item.
     */
    GROUP(
            R.id.navigation_drawer_item_group,
            R.id.navigation_drawer_item_group_title,
            GroupListFragment.class),
    /**
     * Check item.
     */
    CHECK(
            R.id.navigation_drawer_item_check,
            R.id.navigation_drawer_item_check_title,
            CheckableGroupListFragment.class),
    /**
     * All item.
     */
    ALL(
            R.id.navigation_drawer_item_all,
            R.id.navigation_drawer_item_all_title,
            AllGroupsWordListFragment.class),
    /**
     * Import/Export item.
     */
    IMPORT_EXPORT(
            R.id.navigation_drawer_item_import_export,
            R.id.navigation_drawer_item_import_export_title,
            ImportExportFragment.class);

    /**
     * Container id of the item.
     */
    public final int containerId;
    /**
     * Title id of the item.
     */
    public final int titleId;
    /**
     * Fragment class of the item.
     */
    public final Class clazz;

    NavigationDrawerItem(final int newId, final int newTitleId, final Class newClazz) {
        containerId = newId;
        titleId = newTitleId;
        clazz = newClazz;
    }
}
