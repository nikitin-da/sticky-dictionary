package com.example.dmitry.handheld_dictionary.model;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.pushtorefresh.bamboostorage.ABambooStorableItem;
import com.pushtorefresh.bamboostorage.BambooStorableTypeMeta;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
@BambooStorableTypeMeta(
        contentPath = Word.TableInfo.TABLE_NAME
)
public class Word extends ABambooStorableItem implements Parcelable {

    private String foreign;
    private String translate;

    public Word() {
    }

    public Word(String foreign, String translate) {
        this.foreign = foreign;
        this.translate = translate;
    }

    // region getters & setters
    public long getId() {
        return getInternalId();
    }

    public String getForeign() {
        return foreign;
    }

    public void setForeign(String foreign) {
        this.foreign = foreign;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    // endregion

    // region for storage
    @NonNull
    @Override
    public ContentValues toContentValues(@NonNull Resources resources) {
        final ContentValues cv = new ContentValues();

        cv.put(TableInfo.COLUMN_FOREIGN, foreign);
        cv.put(TableInfo.COLUMN_TRANSLATE, translate);

        return cv;
    }

    @Override
    public void fillFromCursor(@NonNull Cursor cursor) {
        foreign = cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_FOREIGN));
        translate = cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_TRANSLATE));
    }

    public interface TableInfo {

        String TABLE_NAME = "table_word";

        String COLUMN_ID = "_id";
        String COLUMN_FOREIGN = "_foreign";
        String COLUMN_TRANSLATE = "_translate";

        String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
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
        dest.writeString(this.foreign);
        dest.writeString(this.translate);
        dest.writeLong(this.getInternalId());
    }

    private Word(Parcel in) {
        this.foreign = in.readString();
        this.translate = in.readString();
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

