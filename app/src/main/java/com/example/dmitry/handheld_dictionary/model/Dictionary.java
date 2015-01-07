package com.example.dmitry.handheld_dictionary.model;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.pushtorefresh.bamboostorage.ABambooStorableItem;
import com.pushtorefresh.bamboostorage.BambooStorableTypeMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
@BambooStorableTypeMeta(
        contentPath = Dictionary.TableInfo.TABLE_NAME
)
public class Dictionary extends ABambooStorableItem implements Parcelable {

    public static final String DICTIONARY_WITH_ID = TableInfo.COLUMN_DICTIONARY_ID + "= ?";

    private int mDictionaryId;
    private final List<Group> mGroups = new ArrayList<>();

    public Dictionary() {
    }

    public Dictionary(int dictionaryId, List<Group> groups) {
        this.mDictionaryId = dictionaryId;
        this.mGroups.addAll(groups);
    }

    // region getters & setters
    public long getId() {
        return getInternalId();
    }


    // endregion

    public int getDictionaryId() {
        return mDictionaryId;
    }

    public void setDictionaryId(int dictionaryId) {
        mDictionaryId = dictionaryId;
    }

    public List<Group> getGroups() {
        return mGroups;
    }

    public void setGroups(List<Group> groups) {
        mGroups.clear();
        mGroups.addAll(groups);
    }
    // region for storage

    @NonNull
    @Override
    public ContentValues toContentValues(@NonNull Resources resources) {
        final ContentValues cv = new ContentValues();
        cv.put(TableInfo.COLUMN_DICTIONARY_ID, mDictionaryId);
        return cv;
    }

    @Override
    public void fillFromCursor(@NonNull Cursor cursor) {
        mDictionaryId = cursor.getInt(cursor.getColumnIndex(TableInfo.COLUMN_DICTIONARY_ID));
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


    // endregion

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mDictionaryId);
        dest.writeTypedList(mGroups);
        dest.writeLong(this.getInternalId());
    }

    private Dictionary(Parcel in) {
        this.mDictionaryId = in.readInt();
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
}
