package com.example.dmitry.handheld_dictionary.model;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.pushtorefresh.bamboostorage.ABambooStorableItem;
import com.pushtorefresh.bamboostorage.BambooStorableTypeMeta;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */

@BambooStorableTypeMeta(
        contentPath = Group.TableInfo.TABLE_NAME
)
public class Group extends ABambooStorableItem implements Parcelable {

    public static final String GROUP_WITH_ID = TableInfo.COLUMN_GROUP_ID + "= ?";
    public static final String GROUPS_FROM_DICTIONARY = TableInfo.COLUMN_GROUP_ID + "= ?";

    private static final DateTimeZone dateTimeZone = DateTimeZone.forTimeZone(TimeZone.getDefault()); // todo check
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private int mGroupId;
    private String mName;
    private DateTime mDate;
    private final List<Word> mWords = new ArrayList<>();

    public Group() {
        fillGroupIdWithRandom();
    }

    public Group(String name) {
        fillGroupIdWithRandom();
        mName = name;
        mDate = DateTime.now();
    }

    public Group(String name, String dateStr) {
        fillGroupIdWithRandom();
        mName = name;
        setDate(dateStr);
    }

    public Group(int groupId, String name, String dateStr) {
        mGroupId = groupId;
        mName = name;
        setDate(dateStr);
    }

    private void fillGroupIdWithRandom() {
        Random random = new Random();
        mGroupId = random.nextInt();
    }

    public void setDate(String dateStr) {
        mDate = DATE_FORMATTER.parseDateTime(dateStr);
    }

    public void addWords(List<Word> words) {
        mWords.addAll(words);
    }

    public void addWord(Word word) {
        mWords.add(word);
    }

    // region getters & setters

    public int getGroupId() {
        return mGroupId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public DateTime getDate() {
        return mDate;
    }

    public void setDate(DateTime date) {
        mDate = date;
    }

    public void setWords(List<Word> words) {
        mWords.clear();
        mWords.addAll(words);
    }

    public List<Word> getWords() {
        return mWords;
    }

    // endregion

    // region for storage
    @NonNull
    @Override
    public ContentValues toContentValues(@NonNull Resources resources) {
        final ContentValues cv = new ContentValues();

        cv.put(TableInfo.COLUMN_GROUP_ID, mGroupId);
        cv.put(TableInfo.COLUMN_NAME, mName);
        cv.put(TableInfo.COLUMN_DATE, DATE_FORMATTER.print(mDate));

        return cv;
    }

    @Override
    public void fillFromCursor(@NonNull Cursor cursor) {
        mGroupId = cursor.getInt(cursor.getColumnIndex(TableInfo.COLUMN_GROUP_ID));
        mName = cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_NAME));
        String dateStr = cursor.getString(cursor.getColumnIndex(TableInfo.COLUMN_DATE));
        setDate(dateStr);
    }

    public interface TableInfo {

        String TABLE_NAME = "table_group";

        String COLUMN_ID = "_id";
        String COLUMN_GROUP_ID = "_group_id";
        String COLUMN_NAME = "_name";
        String COLUMN_DATE = "_date";

        String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_GROUP_ID + " INTEGER, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DATE + " TEXT);";
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
        dest.writeString(this.mName);
        dest.writeSerializable(this.mDate);
        dest.writeTypedList(mWords);
        dest.writeLong(this.getInternalId());
    }

    private Group(Parcel in) {
        this.mGroupId = in.readInt();
        this.mName = in.readString();
        this.mDate = (DateTime) in.readSerializable();
        in.readTypedList(mWords, Word.CREATOR);
        this.setInternalId(in.readLong());
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
    // endregion
}

