package com.example.dmitry.handheld_dictionary.model;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.bamboostorage.ABambooStorableItem;
import com.pushtorefresh.bamboostorage.BambooStorableTypeMeta;

import java.util.Random;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
@BambooStorableTypeMeta(
        contentPath = Word.TableInfo.TABLE_NAME
)
public class Word extends ABambooStorableItem implements Parcelable {

    public static final String WORD_WITH_ID = TableInfo.COLUMN_ID + "= ?";
    public static final String WORDS_FROM_GROUP = TableInfo.COLUMN_GROUP_ID + "= ?";

    private Integer mGroupId;

    @Expose @SerializedName("date") private String mForeign;
    @Expose @SerializedName("translate") private String mTranslate;

    public Word() {
    }

    public Word(String foreign, String translate) {
        Random random = new Random();
        mGroupId = random.nextInt();

        this.mForeign = foreign;
        this.mTranslate = translate;
    }

    public Word(int groupId, String foreign, String translate) {
        this.mGroupId = groupId;
        this.mForeign = foreign;
        this.mTranslate = translate;
    }

    public Word(@NonNull final Word other) {
        this(other.getGroupId(), other.getForeign(), other.getTranslate());
    }

    // region getters & setters
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

    public Integer getGroupId() {
        return mGroupId;
    }

    public void setGroupId(Integer groupId) {
        mGroupId = groupId;
    }

    // endregion

    // region for storage
    @NonNull
    @Override
    public ContentValues toContentValues(@NonNull Resources resources) {
        final ContentValues cv = new ContentValues();

        cv.put(TableInfo.COLUMN_GROUP_ID, mGroupId);
        cv.put(TableInfo.COLUMN_FOREIGN, mForeign);
        cv.put(TableInfo.COLUMN_TRANSLATE, mTranslate);

        return cv;
    }

    @Override
    public void fillFromCursor(@NonNull Cursor cursor) {
        mGroupId = cursor.getInt(cursor.getColumnIndex(TableInfo.COLUMN_GROUP_ID));
        mForeign = cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_FOREIGN));
        mTranslate = cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_TRANSLATE));
    }

    public interface TableInfo {

        String TABLE_NAME = "table_word";

        String COLUMN_ID = "_id";
        String COLUMN_GROUP_ID = "_group_id";
        String COLUMN_FOREIGN = "_foreign";
        String COLUMN_TRANSLATE = "_translate";

        String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
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
        dest.writeInt(this.mGroupId);
        dest.writeString(this.mForeign);
        dest.writeString(this.mTranslate);
        dest.writeLong(this.getInternalId());
    }

    private Word(Parcel in) {
        this.mGroupId = in.readInt();
        this.mForeign = in.readString();
        this.mTranslate = in.readString();
        this.setInternalId(in.readLong());
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

