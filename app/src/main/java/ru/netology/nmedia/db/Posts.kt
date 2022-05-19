package ru.netology.nmedia.db

import android.database.Cursor
import ru.netology.nmedia.Post

fun Cursor.toPost() = Post(
    id = getLong(getColumnIndexOrThrow(PostsTable.Column.ID.columnName)),
    author = getString(getColumnIndexOrThrow(PostsTable.Column.AUTHOR.columnName)),
    content = getString(getColumnIndexOrThrow(PostsTable.Column.CONTENT.columnName)),
    published = getString(getColumnIndexOrThrow(PostsTable.Column.PUBLISHED.columnName)),
    countLike = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKES.columnName)),
    likeByMe = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKED_BY_ME.columnName)) != 0,
    countShare = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKES.columnName)),
    url = getString(getColumnIndexOrThrow(PostsTable.Column.PUBLISHED.columnName))
)