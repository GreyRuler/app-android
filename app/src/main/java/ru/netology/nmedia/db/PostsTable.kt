package ru.netology.nmedia.db

object PostsTable {

    const val NAME = "posts"

    val DDL = """
    CREATE TABLE ${NAME}} (
        ${Column.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Column.AUTHOR} TEXT NOT NULL,
        ${Column.CONTENT} TEXT NOT NULL,
        ${Column.PUBLISHED} TEXT NOT NULL,
        ${Column.LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
        ${Column.LIKES} INTEGER NOT NULL DEFAULT 0
    );
    """.trimIndent()

    val ALL_COLUMNS_NAMES = Column.values().map {
        it.columnName
    }.toTypedArray()

    enum class Column(val columnName: String) {
        ID("id"),
        AUTHOR("author"),
        CONTENT("content"),
        PUBLISHED("published"),
        LIKED_BY_ME("likeByMe"),
        LIKES("likes")
    }


}