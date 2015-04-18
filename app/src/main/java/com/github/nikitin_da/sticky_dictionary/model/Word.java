package com.github.nikitin_da.sticky_dictionary.model;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.github.nikitin_da.sticky_dictionary.util.RandomUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.bamboostorage.ABambooStorableItem;
import com.pushtorefresh.bamboostorage.BambooStorableTypeMeta;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
@BambooStorableTypeMeta(
        contentPath = Word.TableInfo.TABLE_NAME
)
public class Word extends ABambooStorableItem implements Parcelable {

    public static final String WORD_WITH_ID = TableInfo.COLUMN_WORD_ID + "= ?";
    public static final String WORDS_FROM_GROUP = TableInfo.COLUMN_GROUP_ID + "= ?";

    @Expose @SerializedName("word_id") private Long mId;

    private Long mGroupId;

    @Expose @SerializedName("foreign") private String mForeign;
    @Expose @SerializedName("translate") private String mTranslate;

    public Word() {
    }

    public Word(String foreign, String translate) {
        generateId();
        this.mForeign = foreign;
        this.mTranslate = translate;
    }

    public Word(long groupId, String foreign, String translate) {
        this(foreign, translate);
        this.mGroupId = groupId;
    }

    public Word(long id, long groupId, String foreign, String translate) {
        this.mId = id;
        this.mGroupId = groupId;
        this.mForeign = foreign;
        this.mTranslate = translate;
    }

    private void generateId() {
        mId = RandomUtil.nextPositiveLong();
    }

    public Word(@NonNull final Word other) {
        this(other.getId(), other.getGroupId(), other.getForeign(), other.getTranslate());
    }

    // region getters & setters

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getForeign() {
        return mForeign;
    }

    public void setForeign(String foreign) {
        this.mForeign = foreign;
    }

    public String getTranslate() {
        return mTranslate;
    }

    public void setTranslate(String translate) {
        this.mTranslate = translate;
    }

    public Long getGroupId() {
        return mGroupId;
    }

    public void setGroupId(Long groupId) {
        mGroupId = groupId;
    }

    // endregion

    // region for storage
    @NonNull
    @Override
    public ContentValues toContentValues(@NonNull Resources resources) {
        final ContentValues cv = new ContentValues();

        cv.put(TableInfo.COLUMN_WORD_ID, mId);
        cv.put(TableInfo.COLUMN_GROUP_ID, mGroupId);
        cv.put(TableInfo.COLUMN_FOREIGN, mForeign);
        cv.put(TableInfo.COLUMN_TRANSLATE, mTranslate);

        return cv;
    }

    @Override
    public void fillFromCursor(@NonNull Cursor cursor) {
        mId = cursor.getLong(cursor.getColumnIndex(TableInfo.COLUMN_WORD_ID));
        mGroupId = cursor.getLong(cursor.getColumnIndex(TableInfo.COLUMN_GROUP_ID));
        mForeign = cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_FOREIGN));
        mTranslate = cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_TRANSLATE));
    }

    public interface TableInfo {

        String TABLE_NAME = "table_word";

        String COLUMN_ID = "_id";
        String COLUMN_WORD_ID = "_word_id";
        String COLUMN_GROUP_ID = "_group_id";
        String COLUMN_FOREIGN = "_foreign";
        String COLUMN_TRANSLATE = "_translate";

        String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_WORD_ID + " INTEGER, " +
                COLUMN_GROUP_ID + " INTEGER, " +
                COLUMN_FOREIGN + " TEXT, " +
                COLUMN_TRANSLATE + " TEXT);";
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
        dest.writeLong(this.getInternalId());
        dest.writeLong(this.mId);
        dest.writeLong(this.mGroupId);
        dest.writeString(this.mForeign);
        dest.writeString(this.mTranslate);
    }

    private Word(Parcel in) {
        this.setInternalId(in.readLong());
        this.mId = in.readLong();
        this.mGroupId = in.readLong();
        this.mForeign = in.readString();
        this.mTranslate = in.readString();
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        public Word createFromParcel(Parcel source) {
            return new Word(source);
        }

        public Word[] newArray(int size) {
            return new Word[size];
        }
    };
    // endregion
}

