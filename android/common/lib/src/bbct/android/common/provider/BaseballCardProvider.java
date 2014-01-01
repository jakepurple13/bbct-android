/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT for Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT for Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.android.common.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import bbct.android.common.R;
import bbct.android.common.exception.SQLHelperCreationException;

/**
 *
 */
public class BaseballCardProvider extends ContentProvider {

    private static final int ALL_CARDS = 1;
    private static final int CARD_ID = 2;

    public static final UriMatcher uriMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(BaseballCardContract.AUTHORITY,
                BaseballCardContract.TABLE_NAME, ALL_CARDS);
        uriMatcher.addURI(BaseballCardContract.AUTHORITY,
                BaseballCardContract.TABLE_NAME + "/#", CARD_ID);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate()");

        try {
            this.sqlHelper = SQLHelperFactory.getSQLHelper(this.getContext());

            return true;
        } catch (SQLHelperCreationException ex) {
            // TODO Show a dialog and exit app
            Toast.makeText(this.getContext(), R.string.database_error,
                    Toast.LENGTH_LONG).show();
            Log.e(TAG, ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query()");

        Cursor cursor = null;
        SQLiteDatabase db = this.sqlHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case ALL_CARDS:
                cursor = db.query(BaseballCardContract.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case CARD_ID:
                String where = "ID = ? AND (" + selection + ")";
                long id = ContentUris.parseId(uri);
                String[] whereArgs = this.getWhereArgsWithId(selectionArgs, id);
                cursor = db.query(BaseballCardContract.TABLE_NAME, projection,
                        where, whereArgs, null, null, sortOrder);
                break;

            default:
                String errorFormat = this.getContext().getString(
                        R.string.invalid_uri_error);
                String error = String.format(errorFormat, uri.toString());
                throw new IllegalArgumentException(error);
        }

        cursor.setNotificationUri(this.getContext().getContentResolver(),
                BaseballCardContract.CONTENT_URI);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_CARDS:
                return BaseballCardContract.BASEBALL_CARD_LIST_MIME_TYPE;

            case CARD_ID:
                return BaseballCardContract.BASEBALL_CARD_ITEM_MIME_TYPE;

            default:
                String errorFormat = this.getContext().getString(
                        R.string.invalid_uri_error);
                String error = String.format(errorFormat, uri.toString());
                throw new IllegalArgumentException(error);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != ALL_CARDS) {
            String errorFormat = this.getContext().getString(
                    R.string.invalid_uri_error);
            String error = String.format(errorFormat, uri.toString());
            throw new IllegalArgumentException(error);
        }

        long row = this.sqlHelper.getWritableDatabase().insert(
                BaseballCardContract.TABLE_NAME, null, values);

        if (row > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, row);
            this.getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int affected = 0;
        SQLiteDatabase db = this.sqlHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case ALL_CARDS:
                affected = db.delete(BaseballCardContract.TABLE_NAME,
                        selection, selectionArgs);
                break;

            case CARD_ID:
                String where = "ID = ? AND (" + selection + ")";
                long id = ContentUris.parseId(uri);
                String[] whereArgs = this.getWhereArgsWithId(selectionArgs, id);
                affected = db.delete(BaseballCardContract.TABLE_NAME, where,
                        whereArgs);
                break;

            default:
                String errorFormat = this.getContext().getString(
                        R.string.invalid_uri_error);
                String error = String.format(errorFormat, uri.toString());
                throw new IllegalArgumentException(error);
        }

        this.getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        SQLiteDatabase db = this.sqlHelper.getWritableDatabase();

        int affected = 0;

        switch (uriMatcher.match(uri)) {
            case ALL_CARDS:
                affected = db.update(BaseballCardContract.TABLE_NAME, values,
                        selection, selectionArgs);
                break;

            case CARD_ID:
                String where = "ID = ? AND (" + selection + ")";
                long id = ContentUris.parseId(uri);
                String[] whereArgs = this.getWhereArgsWithId(selectionArgs, id);
                affected = db.update(BaseballCardContract.TABLE_NAME, values,
                        where, whereArgs);
                break;

            default:
                String errorFormat = this.getContext().getString(
                        R.string.invalid_uri_error);
                String error = String.format(errorFormat, uri.toString());
                throw new IllegalArgumentException(error);
        }

        this.getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }

    private String[] getWhereArgsWithId(String[] selectionArgs, long id) {
        String[] whereArgs = new String[selectionArgs.length + 1];
        whereArgs[0] = Long.toString(id);

        for (int i = 0; i < selectionArgs.length; ++i) {
            whereArgs[i + 1] = selectionArgs[i];
        }

        return whereArgs;
    }

    private BaseballCardSQLHelper sqlHelper = null;
    private static final String TAG = BaseballCardProvider.class.getName();
}
