package com.github.nikitin_da.sticky_dictionary.result;

import android.support.annotation.Nullable;

import com.github.nikitin_da.sticky_dictionary.model.Group;

import java.util.List;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class ImportResult {

    private final List<Group> mGroups;

    public ImportResult(@Nullable final List<Group> groups) {
        mGroups = groups;
    }

    public List<Group> getGroups() {
        return mGroups;
    }
}
