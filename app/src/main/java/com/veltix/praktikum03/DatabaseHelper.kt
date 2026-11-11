package com.veltix.praktikum03

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_MAHASISWA (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nim TEXT NOT NULL UNIQUE,
                nama TEXT NOT NULL,
                alamat TEXT,
                jurusan TEXT,
                nomor_hp TEXT
            )
            """.trimIndent()
        )

        seedData(db)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MAHASISWA")
        onCreate(db)
    }

    private fun toValues(mahasiswa: Mahasiswa): ContentValues {
        return ContentValues().apply {
            put("nim", mahasiswa.nim)
            put("nama", mahasiswa.nama)
            put("alamat", mahasiswa.alamat)
            put("jurusan", mahasiswa.jurusan)
            put("nomor_hp", mahasiswa.nomorHp)
        }
    }

    private fun seedData(db: SQLiteDatabase) {
        val dataAwal = listOf(
            Mahasiswa(
                nim = "21001",
                nama = "Ahmad Fauzi",
                alamat = "Jl. Pettarani No. 5",
                jurusan = "Sistem Informasi",
                nomorHp = "081234567890"
            ),
            Mahasiswa(
                nim = "21002",
                nama = "Siti Nurhaliza",
                alamat = "Jl. Perintis Kemerdekaan",
                jurusan = "Sistem Informasi",
                nomorHp = "082198765432"
            ),
            Mahasiswa(
                nim = "21003",
                nama = "Budi Santoso",
                alamat = "Jl. Rappocini No. 12",
                jurusan = "Sistem Informasi",
                nomorHp = "081345678901"
            )
        )

        dataAwal.forEach { db.insert(TABLE_MAHASISWA, null, toValues(it)) }
    }

    fun insertMahasiswa(mahasiswa: Mahasiswa): Long {
        return writableDatabase.insert(
            TABLE_MAHASISWA,
            null,
            toValues(mahasiswa)
        )
    }

    fun updateMahasiswa(id: Int, mahasiswa: Mahasiswa): Int {
        return writableDatabase.update(
            TABLE_MAHASISWA,
            toValues(mahasiswa),
            "id = ?",
            arrayOf(id.toString())
        )
    }

    fun deleteMahasiswa(id: Int): Int {
        return writableDatabase.delete(
            TABLE_MAHASISWA,
            "id = ?",
            arrayOf(id.toString())
        )
    }

    fun isNimTerdaftar(nim: String, kecualiId: Int? = null): Boolean {
        val db = readableDatabase
        val cursor = if (kecualiId == null) {
            db.query(
                TABLE_MAHASISWA,
                arrayOf("id"),
                "nim = ?",
                arrayOf(nim),
                null, null, null
            )
        } else {
            db.query(
                TABLE_MAHASISWA,
                arrayOf("id"),
                "nim = ? AND id != ?",
                arrayOf(nim, kecualiId.toString()),
                null, null, null
            )
        }

        return cursor.use { it.moveToFirst() }
    }

    fun getAllMahasiswa(): List<Mahasiswa> {
        val hasil = mutableListOf<Mahasiswa>()
        val cursor = readableDatabase.query(
            TABLE_MAHASISWA,
            null,
            null,
            null,
            null,
            null,
            "nama ASC"
        )

        cursor.use {
            while (it.moveToNext()) {
                hasil.add(
                    Mahasiswa(
                        id = it.getInt(it.getColumnIndexOrThrow("id")),
                        nim = it.getString(it.getColumnIndexOrThrow("nim")),
                        nama = it.getString(it.getColumnIndexOrThrow("nama")),
                        alamat = it.getString(it.getColumnIndexOrThrow("alamat")) ?: "",
                        jurusan = it.getString(it.getColumnIndexOrThrow("jurusan")) ?: "",
                        nomorHp = it.getString(it.getColumnIndexOrThrow("nomor_hp")) ?: ""
                    )
                )
            }
        }

        return hasil
    }

    companion object {
        private const val DB_NAME = "praktikum.db"
        private const val DB_VERSION = 1
        const val TABLE_MAHASISWA = "mahasiswa"
    }
}