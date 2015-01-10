package com.example.dmitry.handheld_dictionary.model;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.dmitry.handheld_dictionary.util.RandomUtil;
import com.pushtorefresh.bamboostorage.ABambooStorableItem;
import com.pushtorefresh.bamboostorage.BambooStorableTypeMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
@BambooStorableTypeMeta(
        contentPath = Dictionary.TableInfo.TABLE_NAME
)
public class Dictionary extends ABambooStorableItem implements Parcelable {

    public static final String DICTIONARY_WITH_ID = TableInfo.COLUMN_DICTIONARY_ID + "= ?";

    private Long mId;
    private final List<Group> mGroups = new ArrayList<>();

    public Dictionary() {
    }

    public Dictionary(List<Group> groups) {
        generateId();
        this.mGroups.addAll(groups);
    }

    public Dictionary(Long id, List<Group> groups) {
        this.mId = id;
        this.mGroups.addAll(groups);
    }

    private void generateId() {
        mId = RandomUtil.nextPositiveLong();
    }

    // region getters & setters

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public List<Group> getGroups() {
        return mGroups;
    }

    public void setGroups(List<Group> groups) {
        mGroups.clear();
        mGroups.addAll(groups);
    }
    // endregion

    // region for storage

    @NonNull
    @Override
    public ContentValues toContentValues(@NonNull Resources resources) {
        final ContentValues cv = new ContentValues();
        cv.put(TableInfo.COLUMN_DICTIONARY_ID, mId);
        return cv;
    }

    @Override
    public void fillFromCursor(@NonNull Cursor cursor) {
        mId = cursor.getLong(cursor.getColumnIndex(TableInfo.COLUMN_DICTIONARY_ID));
    }

    public interface TableInfo {

        String TABLE_NAME = "table_dictionary";

        String COLUMN_ID = "_id";
        String COLUMN_DICTIONARY_ID = "_dictionary_id";

        String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_DICTIONARY_ID + " INTEGER);";
    }

    public static String createTableQuery() {
        return TableInfo.CREATE_QUERY;
    }
    // endregion

    // region parcelable


    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeTypedList(mGroups);
        dest.writeLong(this.getInternalId());
    }

    private Dictionary(Parcel in) {
        this.mId = in.readLong();
        in.readTypedList(mGroups, Group.CREATOR);
        this.setInternalId(in.readLong());
    }

    public static final Parcelable.Creator<Dictionary> CREATOR = new Parcelable.Creator<Dictionary>() {
        public Dictionary createFromParcel(Parcel source) {
            return new Dictionary(source);
        }

        public Dictionary[] newArray(int size) {
            return new Dictionary[size];
        }
    };
    // endregion
}
